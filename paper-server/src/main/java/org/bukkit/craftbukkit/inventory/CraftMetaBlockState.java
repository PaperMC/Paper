package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta {

    private static final Set<Material> SHULKER_BOX_MATERIALS = Sets.newHashSet(
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX
    );

    private static final Set<Material> BLOCK_STATE_MATERIALS = Sets.newHashSet(
            Material.FURNACE,
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.JUKEBOX,
            Material.DISPENSER,
            Material.DROPPER,
            Material.ACACIA_HANGING_SIGN,
            Material.ACACIA_SIGN,
            Material.ACACIA_WALL_HANGING_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BAMBOO_HANGING_SIGN,
            Material.BAMBOO_SIGN,
            Material.BAMBOO_WALL_HANGING_SIGN,
            Material.BAMBOO_WALL_SIGN,
            Material.BIRCH_HANGING_SIGN,
            Material.BIRCH_SIGN,
            Material.BIRCH_WALL_HANGING_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.CHERRY_HANGING_SIGN,
            Material.CHERRY_SIGN,
            Material.CHERRY_WALL_HANGING_SIGN,
            Material.CHERRY_WALL_SIGN,
            Material.CRIMSON_HANGING_SIGN,
            Material.CRIMSON_SIGN,
            Material.CRIMSON_WALL_HANGING_SIGN,
            Material.CRIMSON_WALL_SIGN,
            Material.DARK_OAK_HANGING_SIGN,
            Material.DARK_OAK_SIGN,
            Material.DARK_OAK_WALL_HANGING_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.JUNGLE_HANGING_SIGN,
            Material.JUNGLE_SIGN,
            Material.JUNGLE_WALL_HANGING_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.MANGROVE_HANGING_SIGN,
            Material.MANGROVE_SIGN,
            Material.MANGROVE_WALL_HANGING_SIGN,
            Material.MANGROVE_WALL_SIGN,
            Material.OAK_HANGING_SIGN,
            Material.OAK_SIGN,
            Material.OAK_WALL_HANGING_SIGN,
            Material.OAK_WALL_SIGN,
            Material.SPRUCE_HANGING_SIGN,
            Material.SPRUCE_SIGN,
            Material.SPRUCE_WALL_HANGING_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.WARPED_HANGING_SIGN,
            Material.WARPED_SIGN,
            Material.WARPED_WALL_HANGING_SIGN,
            Material.WARPED_WALL_SIGN,
            Material.SPAWNER,
            Material.BREWING_STAND,
            Material.ENCHANTING_TABLE,
            Material.COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.BEACON,
            Material.DAYLIGHT_DETECTOR,
            Material.HOPPER,
            Material.COMPARATOR,
            Material.SHIELD,
            Material.STRUCTURE_BLOCK,
            Material.ENDER_CHEST,
            Material.BARREL,
            Material.BELL,
            Material.BLAST_FURNACE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.JIGSAW,
            Material.LECTERN,
            Material.SMOKER,
            Material.BEEHIVE,
            Material.BEE_NEST,
            Material.SCULK_CATALYST,
            Material.SCULK_SHRIEKER,
            Material.CALIBRATED_SCULK_SENSOR,
            Material.SCULK_SENSOR,
            Material.CHISELED_BOOKSHELF,
            Material.DECORATED_POT,
            Material.SUSPICIOUS_SAND,
            Material.SUSPICIOUS_GRAVEL,
            Material.TRIAL_SPAWNER,
            Material.CRAFTER,
            Material.VAULT
    );

    private static final class TrackedDataComponentMap implements DataComponentMap {

        private final Set<DataComponentType<?>> seen = new HashSet<>();
        private final DataComponentMap handle;

        public TrackedDataComponentMap(DataComponentMap map) {
            this.handle = map;
        }

        @Override
        public <T> T get(DataComponentType<? extends T> type) {
            seen.add(type);
            return handle.get(type);
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return handle.keySet();
        }

        @Override
        public Iterator<TypedDataComponent<?>> iterator() {
            return handle.iterator();
        }
    }

    static {
        // Add shulker boxes to the list of block state materials too
        BLOCK_STATE_MATERIALS.addAll(SHULKER_BOX_MATERIALS);
    }

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<CustomData> BLOCK_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BLOCK_ENTITY_DATA, "BlockEntityTag");

    final Material material;
    private CraftBlockEntityState<?> blockEntityTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material) {
            blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaBlockState(DataComponentPatch tag, Material material) {
        super(tag);
        this.material = material;

        getOrEmpty(tag, BLOCK_ENTITY_TAG).ifPresent((nbt) -> {
            blockEntityTag = getBlockState(material, nbt.copyTag());
        });

        if (!tag.isEmpty()) {
            if (blockEntityTag == null) {
                blockEntityTag = getBlockState(material, null);
            }

            // Convert to map
            PatchedDataComponentMap map = new PatchedDataComponentMap(DataComponentMap.EMPTY);
            map.applyPatch(tag);
            // Setup tracking
            TrackedDataComponentMap track = new TrackedDataComponentMap(map);
            // Apply
            blockEntityTag.applyComponents(track, tag);
            // Mark applied components as handled
            for (DataComponentType<?> seen : track.seen) {
                unhandledTags.clear(seen);
            }
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = SerializableMeta.getString(map, "blockMaterial", true);
        Material m = Material.getMaterial(matName);
        if (m != null) {
            material = m;
        } else {
            material = Material.AIR;
        }
        blockEntityTag = getBlockState(material, null);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (blockEntityTag != null) {
            tag.put(BLOCK_ENTITY_TAG, CustomData.of(blockEntityTag.getSnapshotNBTWithoutComponents()));

            for (TypedDataComponent<?> component : blockEntityTag.collectComponents()) {
                tag.builder.set(component);
            }
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            blockEntityTag = getBlockState(material, tag.getCompound(BLOCK_ENTITY_TAG.NBT));
        }
    }

    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (blockEntityTag != null) {
            internalTags.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag.getSnapshotNBT());
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            CraftMetaBlockState that = (CraftMetaBlockState) meta;

            return Objects.equal(this.blockEntityTag, that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return BLOCK_STATE_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState) super.clone();
        if (blockEntityTag != null) {
            meta.blockEntityTag = blockEntityTag.copy();
        }
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        return (blockEntityTag != null) ? blockEntityTag.copy() : getBlockState(material, null);
    }

    private static CraftBlockEntityState<?> getBlockState(Material material, NBTTagCompound blockEntityTag) {
        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack(); // Only actually used for jigsaws
        if (blockEntityTag != null) {
            if (material == Material.SHIELD) {
                blockEntityTag.putString("id", "minecraft:banner");
            } else if (material == Material.BEE_NEST || material == Material.BEEHIVE) {
                blockEntityTag.putString("id", "minecraft:beehive");
            } else if (SHULKER_BOX_MATERIALS.contains(material)) {
                blockEntityTag.putString("id", "minecraft:shulker_box");
            }
        }

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");

        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack();
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Preconditions.checkArgument(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for " + material);

        this.blockEntityTag = (CraftBlockEntityState<?>) blockState;
    }

    private static Material shieldToBannerHack() {
        return Material.WHITE_BANNER;
    }
}
