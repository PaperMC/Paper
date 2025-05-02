package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import io.papermc.paper.registry.HolderableBase;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftBlockType<B extends @NonNull BlockData> extends HolderableBase<Block> implements BlockType.Typed<B>, io.papermc.paper.world.flag.PaperFeatureDependent<Block> { // Paper - feature flag API


    public static Material minecraftToBukkit(Block block) {
        return CraftMagicNumbers.getMaterial(block);
    }

    public static Block bukkitToMinecraft(Material material) {
        return CraftMagicNumbers.getBlock(material);
    }

    public static BlockType minecraftToBukkitNew(Block minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BLOCK);
    }

    public static Block bukkitToMinecraftNew(BlockType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private static boolean hasMethod(Class<?> clazz, Class<?>... params) {
        boolean hasMethod = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (Arrays.equals(method.getParameterTypes(), params)) {
                Preconditions.checkArgument(!hasMethod, "More than one matching method for %s, args %s", clazz, Arrays.toString(params));

                hasMethod = true;
            }
        }

        return hasMethod;
    }

    private static final Class<?>[] USE_WITHOUT_ITEM_ARGS = new Class[]{
        BlockState.class, net.minecraft.world.level.Level.class, BlockPos.class, Player.class, BlockHitResult.class
    };
    private static final Class<?>[] USE_ITEM_ON_ARGS = new Class[]{
        net.minecraft.world.item.ItemStack.class, BlockState.class, net.minecraft.world.level.Level.class, BlockPos.class, Player.class, InteractionHand.class, BlockHitResult.class
    };

    private static boolean isInteractable(Block block) {
        Class<?> clazz = block.getClass();

        boolean hasMethod = CraftBlockType.hasMethod(clazz, CraftBlockType.USE_WITHOUT_ITEM_ARGS) || CraftBlockType.hasMethod(clazz, CraftBlockType.USE_ITEM_ON_ARGS);

        if (!hasMethod && clazz.getSuperclass() != BlockBehaviour.class) {
            clazz = clazz.getSuperclass();

            hasMethod = CraftBlockType.hasMethod(clazz, CraftBlockType.USE_WITHOUT_ITEM_ARGS) || CraftBlockType.hasMethod(clazz, CraftBlockType.USE_ITEM_ON_ARGS);
        }

        return hasMethod;
    }

    private final Supplier<Class<B>> blockDataClass;
    private final Supplier<Boolean> interactable;

    @SuppressWarnings("unchecked")
    public CraftBlockType(final Holder<Block> holder) {
        super(holder);
        this.blockDataClass = Suppliers.memoize(() -> (Class<B>) CraftBlockData.fromData(this.getHandle().defaultBlockState()).getClass().getInterfaces()[0]);
        this.interactable = Suppliers.memoize(() -> CraftBlockType.isInteractable(this.getHandle()));
    }

    @Override
    public Typed<BlockData> typed() {
        return this.typed(BlockData.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Other extends BlockData> Typed<Other> typed(final Class<Other> blockDataType) {
        if (blockDataType.isAssignableFrom(this.blockDataClass.get())) return (Typed<Other>) this;
        throw new IllegalArgumentException("Cannot type block type " + this + " to blockdata type " + blockDataType.getSimpleName());
    }

    @Override
    public boolean hasItemType() {
        if (this == AIR) {
            return true;
        }

        return this.getHandle().asItem() != Items.AIR;
    }

    @Override
    public ItemType getItemType() {
        if (this == AIR) {
            return ItemType.AIR;
        }

        Item item = this.getHandle().asItem();
        Preconditions.checkArgument(item != Items.AIR, "The block type %s has no corresponding item type", this.getKey());
        return CraftItemType.minecraftToBukkitNew(item);
    }

    @Override
    public Class<B> getBlockDataClass() {
        return this.blockDataClass.get();
    }

    @Override
    public B createBlockData() {
        return this.createBlockData((String) null);
    }

    @Override
    public Collection<B> createBlockDataStates() {
        final ImmutableList<BlockState> possibleStates = this.getHandle().getStateDefinition().getPossibleStates();
        final ImmutableList.Builder<B> builder = ImmutableList.builderWithExpectedSize(possibleStates.size());
        for (final BlockState possibleState : possibleStates) {
            builder.add(this.blockDataClass.get().cast(possibleState.createCraftBlockData()));
        }
        return builder.build();
    }

    @Override
    public B createBlockData(final @Nullable Consumer<? super B> consumer) {
        B data = this.createBlockData();

        if (consumer != null) {
            consumer.accept(data);
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public B createBlockData(final @Nullable String data) {
        return (B) CraftBlockData.newData(this, data);
    }

    @Override
    public boolean isSolid() {
        return this.getHandle().defaultBlockState().blocksMotion();
    }

    @Override
    public boolean isAir() {
        return this.getHandle().defaultBlockState().isAir();
    }

    @Override
    public boolean isEnabledByFeature(final World world) {
        Preconditions.checkNotNull(world, "World cannot be null");
        return this.getHandle().isEnabled(((CraftWorld) world).getHandle().enabledFeatures());
    }

    @Override
    public boolean isFlammable() {
        return this.getHandle().defaultBlockState().ignitedByLava();
    }

    @Override
    public boolean isBurnable() {
        return ((FireBlock) Blocks.FIRE).igniteOdds.getOrDefault(this.getHandle(), 0) > 0;
    }

    @Override
    public boolean isOccluding() {
        return this.getHandle().defaultBlockState().isRedstoneConductor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
    }

    @Override
    public boolean hasGravity() {
        return this.getHandle() instanceof Fallable;
    }

    @Override
    public boolean isInteractable() {
        return this.interactable.get();
    }

    @Override
    public float getHardness() {
        return this.getHandle().defaultBlockState().destroySpeed;
    }

    @Override
    public float getBlastResistance() {
        return this.getHandle().getExplosionResistance();
    }

    @Override
    public float getSlipperiness() {
        return this.getHandle().getFriction();
    }

    @Override
    public String getTranslationKey() {
        return this.getHandle().getDescriptionId();
    }

    @Override
    public @Nullable Material asMaterial() {
        return Registry.MATERIAL.get(this.getKey());
    }

    // Paper start - add Translatable
    @Override
    public String translationKey() {
        return this.getHandle().getDescriptionId();
    }
    // Paper end - add Translatable

    // Paper start - hasCollision API
    @Override
    public boolean hasCollision() {
        return this.getHandle().hasCollision;
    }
    // Paper end - hasCollision API
}
