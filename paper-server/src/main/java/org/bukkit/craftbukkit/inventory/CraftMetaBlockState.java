package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.DyeColor;
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

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<CustomData> BLOCK_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BLOCK_ENTITY_DATA, "BlockEntityTag");

    final Material material;
    private CraftBlockEntityState<?> blockEntityTag;
    private NBTTagCompound internalTag;

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
            CraftBlockEntityState<?> blockEntityTag = this.blockEntityTag;
            if (blockEntityTag == null) {
                blockEntityTag = getBlockState(material, null);
            }

            // Convert to map
            PatchedDataComponentMap map = new PatchedDataComponentMap(DataComponentMap.EMPTY);
            map.applyPatch(tag);
            // Apply
            Set<DataComponentType<?>> applied = blockEntityTag.applyComponents(map, tag);
            // Mark applied components as handled
            for (DataComponentType<?> seen : applied) {
                unhandledTags.clear(seen);
            }
            // Only set blockEntityTag if something was applied
            if (!applied.isEmpty()) {
                this.blockEntityTag = blockEntityTag;
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
        blockEntityTag = getBlockState(material, internalTag);
        internalTag = null;
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (blockEntityTag != null) {
            tag.put(BLOCK_ENTITY_TAG, CustomData.of(blockEntityTag.getSnapshotNBTWithoutComponents()));

            for (TypedDataComponent<?> component : blockEntityTag.collectComponents()) {
                tag.putIfAbsent(component);
            }
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            internalTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
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
        BlockPosition pos = BlockPosition.ZERO;
        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack(blockEntityTag); // Only actually used for jigsaws
        if (blockEntityTag != null) {
            if (material == Material.SHIELD) {
                blockEntityTag.putString("id", "minecraft:banner");
            } else if (material == Material.BEE_NEST || material == Material.BEEHIVE) {
                blockEntityTag.putString("id", "minecraft:beehive");
            } else if (SHULKER_BOX_MATERIALS.contains(material)) {
                blockEntityTag.putString("id", "minecraft:shulker_box");
            }

            pos = TileEntity.getPosFromTag(blockEntityTag);
        }

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(pos, stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");

        Material stateMaterial = (material != Material.SHIELD) ? material : shieldToBannerHack(null);
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Preconditions.checkArgument(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for %s", material);

        this.blockEntityTag = (CraftBlockEntityState<?>) blockState;
    }

    private static Material shieldToBannerHack(NBTTagCompound tag) {
        if (tag != null) {
            if (tag.contains("components", CraftMagicNumbers.NBT.TAG_COMPOUND)) {
                NBTTagCompound components = tag.getCompound("components");
                if (components.contains("minecraft:base_color", CraftMagicNumbers.NBT.TAG_STRING)) {
                    DyeColor color = DyeColor.getByWoolData((byte) EnumColor.byName(components.getString("minecraft:base_color"), EnumColor.WHITE).getId());

                    return CraftMetaShield.shieldToBannerHack(color);
                }
            }
        }

        return Material.WHITE_BANNER;
    }
}
