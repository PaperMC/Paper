package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBanner;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
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
            Material.SCULK_SENSOR,
            Material.CHISELED_BOOKSHELF,
            Material.DECORATED_POT,
            Material.SUSPICIOUS_SAND
    );

    static {
        // Add shulker boxes to the list of block state materials too
        BLOCK_STATE_MATERIALS.addAll(SHULKER_BOX_MATERIALS);
    }

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");

    final Material material;
    NBTTagCompound blockEntityTag;

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

    CraftMetaBlockState(NBTTagCompound tag, Material material) {
        super(tag);
        this.material = material;

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT).copy();
        } else {
            blockEntityTag = null;
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
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (blockEntityTag != null) {
            tag.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (blockEntityTag != null) {
            internalTags.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
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
        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack(blockEntityTag); // Only actually used for jigsaws
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
        return CraftBlockStates.getBlockState(stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Validate.notNull(blockState, "blockState must not be null");

        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack(blockEntityTag);
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Validate.isTrue(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for " + material);

        blockEntityTag = ((CraftBlockEntityState) blockState).getSnapshotNBT();
        // Set shield base
        if (material == Material.SHIELD) {
            blockEntityTag.putInt(CraftMetaBanner.BASE.NBT, ((CraftBanner) blockState).getBaseColor().getWoolData());
        }
    }

    private static Material shieldToBannerHack(NBTTagCompound tag) {
        if (tag == null || !tag.contains(CraftMetaBanner.BASE.NBT, CraftMagicNumbers.NBT.TAG_INT)) {
            return Material.WHITE_BANNER;
        }

        switch (tag.getInt(CraftMetaBanner.BASE.NBT)) {
            case 0:
                return Material.WHITE_BANNER;
            case 1:
                return Material.ORANGE_BANNER;
            case 2:
                return Material.MAGENTA_BANNER;
            case 3:
                return Material.LIGHT_BLUE_BANNER;
            case 4:
                return Material.YELLOW_BANNER;
            case 5:
                return Material.LIME_BANNER;
            case 6:
                return Material.PINK_BANNER;
            case 7:
                return Material.GRAY_BANNER;
            case 8:
                return Material.LIGHT_GRAY_BANNER;
            case 9:
                return Material.CYAN_BANNER;
            case 10:
                return Material.PURPLE_BANNER;
            case 11:
                return Material.BLUE_BANNER;
            case 12:
                return Material.BROWN_BANNER;
            case 13:
                return Material.GREEN_BANNER;
            case 14:
                return Material.RED_BANNER;
            case 15:
                return Material.BLACK_BANNER;
            default:
                throw new IllegalArgumentException("Unknown banner colour");
        }
    }
}
