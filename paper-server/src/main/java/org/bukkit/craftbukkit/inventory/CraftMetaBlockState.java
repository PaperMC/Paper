package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.util.BlockVector;

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
    private BlockVector position;
    private CompoundTag internalTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material) {
            this.blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
        this.position = te.position;
    }

    CraftMetaBlockState(DataComponentPatch tag, Material material) {
        super(tag);
        this.material = material;

        getOrEmpty(tag, CraftMetaBlockState.BLOCK_ENTITY_TAG).ifPresent((blockTag) -> {
            CompoundTag nbt = blockTag.copyTag();

            this.blockEntityTag = CraftMetaBlockState.getBlockState(material, nbt);
            if (nbt.contains("x", CraftMagicNumbers.NBT.TAG_ANY_NUMBER) && nbt.contains("y", CraftMagicNumbers.NBT.TAG_ANY_NUMBER) && nbt.contains("z", CraftMagicNumbers.NBT.TAG_ANY_NUMBER)) {
                this.position = new BlockVector(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            }
        });

        if (!tag.isEmpty()) {
            CraftBlockEntityState<?> blockEntityTag = this.blockEntityTag;
            if (blockEntityTag == null) {
                blockEntityTag = CraftMetaBlockState.getBlockState(material, null);
            }

            // Convert to map
            PatchedDataComponentMap map = new PatchedDataComponentMap(DataComponentMap.EMPTY);
            map.applyPatch(tag);
            // Apply
            Set<DataComponentType<?>> applied = blockEntityTag.applyComponents(map, tag);
            // Mark applied components as handled
            for (DataComponentType<?> seen : applied) {
                this.unhandledTags.clear(seen);
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
            this.material = m;
        } else {
            this.material = Material.AIR;
        }
        if (this.internalTag != null) {
            this.blockEntityTag = CraftMetaBlockState.getBlockState(this.material, this.internalTag);
            this.internalTag = null;
        }
        this.position = SerializableMeta.getObject(BlockVector.class, map, "blockPosition", true);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        CompoundTag nbt = null;
        if (this.blockEntityTag != null) {
            nbt = this.blockEntityTag.getItemNBT();

            for (TypedDataComponent<?> component : this.blockEntityTag.collectComponents()) {
                tag.putIfAbsent(component);
            }
        }

        if (this.position != null) {
            if (nbt == null) {
                nbt = new CompoundTag();
            }

            nbt.putInt("x", this.position.getBlockX());
            nbt.putInt("y", this.position.getBlockY());
            nbt.putInt("z", this.position.getBlockZ());
        }

        if (nbt != null && !nbt.isEmpty()) {
            CraftBlockEntityState<?> tile = (this.blockEntityTag != null) ? this.blockEntityTag : CraftMetaBlockState.getBlockState(this.material, null);
            // See ItemBlock#setBlockEntityData
            tile.addEntityType(nbt);

            tag.put(CraftMetaBlockState.BLOCK_ENTITY_TAG, CustomData.of(nbt));
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.internalTag = tag.getCompound(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, Tag> internalTags) {
        if (this.blockEntityTag != null) {
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, this.blockEntityTag.getSnapshotNBT());
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", this.material.name());
        if (this.position != null) {
            builder.put("blockPosition", this.position);
        }
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        if (this.position != null) {
            hash = 61 * hash + this.position.hashCode();
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

            return Objects.equal(this.blockEntityTag, that.blockEntityTag) && Objects.equal(this.position, that.position);
        }
        return true;
    }

    boolean isBlockStateEmpty() {
        return !(this.blockEntityTag != null || this.position != null);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || this.isBlockStateEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBlockStateEmpty();
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState) super.clone();
        if (this.blockEntityTag != null) {
            meta.blockEntityTag = this.blockEntityTag.copy();
        }
        if (this.position != null) {
            meta.position = this.position.clone();
        }
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return this.blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        return (this.blockEntityTag != null) ? this.blockEntityTag.copy() : CraftMetaBlockState.getBlockState(this.material, null);
    }

    private static CraftBlockEntityState<?> getBlockState(Material material, CompoundTag blockEntityTag) {
        BlockPos pos = BlockPos.ZERO;
        Material stateMaterial = (material != Material.SHIELD) ? material : CraftMetaBlockState.shieldToBannerHack(blockEntityTag); // Only actually used for jigsaws
        if (blockEntityTag != null) {
            if (material == Material.SHIELD) {
                blockEntityTag.putString("id", "minecraft:banner");
            } else if (material == Material.BEE_NEST || material == Material.BEEHIVE) {
                blockEntityTag.putString("id", "minecraft:beehive");
            } else if (CraftMetaBlockState.SHULKER_BOX_MATERIALS.contains(material)) {
                blockEntityTag.putString("id", "minecraft:shulker_box");
            }

            pos = BlockEntity.getPosFromTag(blockEntityTag);
        }

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(pos, stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");

        Material stateMaterial = (this.material != Material.SHIELD) ? this.material : CraftMetaBlockState.shieldToBannerHack(null);
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Preconditions.checkArgument(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for %s", this.material);

        this.blockEntityTag = (CraftBlockEntityState<?>) blockState;
    }

    private static Material shieldToBannerHack(CompoundTag tag) {
        if (tag != null) {
            if (tag.contains("components", CraftMagicNumbers.NBT.TAG_COMPOUND)) {
                CompoundTag components = tag.getCompound("components");
                if (components.contains("minecraft:base_color", CraftMagicNumbers.NBT.TAG_STRING)) {
                    DyeColor color = DyeColor.getByWoolData((byte) net.minecraft.world.item.DyeColor.byName(components.getString("minecraft:base_color"), net.minecraft.world.item.DyeColor.WHITE).getId());

                    return CraftMetaShield.shieldToBannerHack(color);
                }
            }
        }

        return Material.WHITE_BANNER;
    }
}
