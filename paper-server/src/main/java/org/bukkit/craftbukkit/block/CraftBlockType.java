package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
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
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class CraftBlockType<B extends BlockData> implements BlockType.Typed<B>, Handleable<Block> {

    private final NamespacedKey key;
    private final Block block;
    private final Class<B> blockDataClass;
    private final boolean interactable;

    public static Material minecraftToBukkit(Block block) {
        return CraftMagicNumbers.getMaterial(block);
    }

    public static Block bukkitToMinecraft(Material material) {
        return CraftMagicNumbers.getBlock(material);
    }

    public static BlockType minecraftToBukkitNew(Block minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BLOCK, Registry.BLOCK);
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

    public CraftBlockType(NamespacedKey key, Block block) {
        this.key = key;
        this.block = block;
        this.blockDataClass = (Class<B>) CraftBlockData.fromData(block.defaultBlockState()).getClass().getInterfaces()[0];
        this.interactable = CraftBlockType.isInteractable(block);
    }

    @Override
    public Block getHandle() {
        return this.block;
    }

    @NotNull
    @Override
    public Typed<BlockData> typed() {
        return this.typed(BlockData.class);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <Other extends BlockData> Typed<Other> typed(@NotNull Class<Other> blockDataType) {
        if (blockDataType.isAssignableFrom(this.blockDataClass)) return (Typed<Other>) this;
        throw new IllegalArgumentException("Cannot type block type " + this.key.toString() + " to blockdata type " + blockDataType.getSimpleName());
    }

    @Override
    public boolean hasItemType() {
        if (this == AIR) {
            return true;
        }

        return this.block.asItem() != Items.AIR;
    }

    @NotNull
    @Override
    public ItemType getItemType() {
        if (this == AIR) {
            return ItemType.AIR;
        }

        Item item = this.block.asItem();
        Preconditions.checkArgument(item != Items.AIR, "The block type %s has no corresponding item type", this.getKey());
        return CraftItemType.minecraftToBukkitNew(item);
    }

    @Override
    public Class<B> getBlockDataClass() {
        return this.blockDataClass;
    }

    @Override
    public B createBlockData() {
        return this.createBlockData((String) null);
    }

    @Override
    public B createBlockData(Consumer<? super B> consumer) {
        B data = this.createBlockData();

        if (consumer != null) {
            consumer.accept(data);
        }

        return data;
    }

    @Override
    public B createBlockData(String data) {
        return (B) CraftBlockData.newData(this, data);
    }

    @Override
    public boolean isSolid() {
        return this.block.defaultBlockState().blocksMotion();
    }

    @Override
    public boolean isAir() {
        return this.block.defaultBlockState().isAir();
    }

    @Override
    public boolean isEnabledByFeature(@NotNull World world) {
        Preconditions.checkNotNull(world, "World cannot be null");
        return this.getHandle().isEnabled(((CraftWorld) world).getHandle().enabledFeatures());
    }

    @Override
    public boolean isFlammable() {
        return this.block.defaultBlockState().ignitedByLava();
    }

    @Override
    public boolean isBurnable() {
        return ((FireBlock) Blocks.FIRE).igniteOdds.getOrDefault(this.block, 0) > 0;
    }

    @Override
    public boolean isOccluding() {
        return this.block.defaultBlockState().isRedstoneConductor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
    }

    @Override
    public boolean hasGravity() {
        return this.block instanceof Fallable;
    }

    @Override
    public boolean isInteractable() {
        return this.interactable;
    }

    @Override
    public float getHardness() {
        return this.block.defaultBlockState().destroySpeed;
    }

    @Override
    public float getBlastResistance() {
        return this.block.getExplosionResistance();
    }

    @Override
    public float getSlipperiness() {
        return this.block.getFriction();
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return this.block.getDescriptionId();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public Material asMaterial() {
        return Registry.MATERIAL.get(this.key);
    }
}
