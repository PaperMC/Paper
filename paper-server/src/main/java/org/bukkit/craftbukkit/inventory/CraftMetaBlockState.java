package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
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
    static final ItemMetaKey BLOCK_ENTITY_TAG_CUSTOM_DATA = new ItemMetaKey("block-entity-tag");
    static final ItemMetaKey BLOCK_ENTITY_COMPONENTS = new ItemMetaKey("block-entity-components");

    final Material material;
    // Paper start - store data separately
    DataComponentMap components;
    CustomData blockEntityTag;
    {
        // this is because the fields are possibly assigned in the super constructor (via deserializeInternal)
        // and a direct field initialization happens **after** the super constructor. So we only want to
        // set them to empty if they weren't assigned by the super constructor (via deserializeInternal)
        this.components = this.components != null ? this.components : DataComponentMap.EMPTY;
        this.blockEntityTag = this.blockEntityTag != null ? this.blockEntityTag : CustomData.EMPTY;
    }
    private Material materialForBlockEntityType() {
        return this.material;
    }
    // Paper end
    private CompoundTag internalTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof final CraftMetaBlockState metaBlockState) ||
            metaBlockState.material != material) {
            this.components = DataComponentMap.EMPTY;
            this.blockEntityTag = CustomData.EMPTY;
            return;
        }

        this.components = metaBlockState.components;
        this.blockEntityTag = metaBlockState.blockEntityTag;
    }

    CraftMetaBlockState(DataComponentPatch tag, Material material, final Set<DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper
        this.material = material;

        // Paper start - move to separate method to be re-called
        this.updateBlockState(tag);
    }

    private void updateBlockState(final DataComponentPatch tag) {
        // Paper end
        getOrEmpty(tag, CraftMetaBlockState.BLOCK_ENTITY_TAG).ifPresent((nbt) -> {
            this.blockEntityTag = nbt;
        });

        if (!tag.isEmpty()) {
            // Paper start - store data in a DataComponentMap to be used to construct CraftBlockEntityStates
            final DataComponentMap.Builder map = DataComponentMap.builder();
            final net.minecraft.world.level.block.entity.BlockEntity dummyBlockEntity = java.util.Objects.requireNonNull(
                org.bukkit.craftbukkit.block.CraftBlockStates.createNewBlockEntity(this.materialForBlockEntityType())
            );

            // we don't care about what's in here, all
            // we want is to know which data component types are referenced
            Set<DataComponentType<?>> applied = dummyBlockEntity.applyComponentsSet(DataComponentMap.EMPTY, DataComponentPatch.EMPTY);
            // Paper end - store data in a DataComponentMap to be used to construct CraftBlockEntityStates
            // Mark applied components as handled
            for (DataComponentType<?> seen : applied) {
                this.unhandledTags.clear(seen);
            }
            // Only set blockEntityTag if something was applied
            if (!applied.isEmpty()) {
                for (final DataComponentType type : applied) {
                    if (CraftMetaItem.DEFAULT_HANDLED_DCTS.contains(type)) continue;
                    getOrEmpty(tag, type).ifPresent(value -> {
                        map.set(type, value);
                    });
                }
            }
            this.components = map.build();
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String blockMaterial = SerializableMeta.getString(map, "blockMaterial", true);
        Material material = Material.getMaterial(blockMaterial);
        this.material = material != null ? material : Material.AIR;
        if (this.internalTag != null) {
            this.setBlockState(CraftMetaBlockState.getBlockState(this.material, this.internalTag)); // Paper - general item meta fixes - pass through setter
            this.internalTag = null;
        }
        // Paper start - general item meta fixes - parse spigot legacy position and merge into block entity tag
        final BlockVector legacyPosition = SerializableMeta.getObject(BlockVector.class, map, "blockPosition", true);
        if (legacyPosition != null) {
            this.blockEntityTag = this.blockEntityTag.update(blockEntityTag -> {
                final TagValueOutput output = TagValueOutput.createDiscardingWithContext(blockEntityTag, CraftRegistry.getMinecraftRegistry());
                if (blockEntityTag.isEmpty()) {
                    BlockEntity.addEntityType(output, java.util.Objects.requireNonNull(CraftBlockStates.getBlockEntityType(this.materialForBlockEntityType())));
                }
                output.putInt("x", legacyPosition.getBlockX());
                output.putInt("y", legacyPosition.getBlockY());
                output.putInt("z", legacyPosition.getBlockZ());
            });
        }
        // Paper end - general item meta fixes - parse spigot legacy position and merge into block entity tag
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        // Paper start - accurately replicate logic for creating ItemStack from BlockEntity
        // taken from BlockItem#setBlockEntityData
        final CompoundTag nbt = this.blockEntityTag.copyTag();
        if (!nbt.isEmpty()) {
            if (nbt.getString("id").isEmpty()) {
                BlockEntity.addEntityType(TagValueOutput.createDiscardingWithContext(nbt, CraftRegistry.getMinecraftRegistry()), java.util.Objects.requireNonNull(CraftBlockStates.getBlockEntityType(this.materialForBlockEntityType())));
            }
            tag.put(CraftMetaBlockState.BLOCK_ENTITY_TAG, CustomData.of(nbt));
        }

        for (final TypedDataComponent<?> component : this.components) {
            if (CraftMetaItem.DEFAULT_HANDLED_DCTS.contains(component.type())) continue; // if the component type was already handled by CraftMetaItem, don't add it again
            tag.builder.set(component);
        }
        // Paper end
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        Optional<CompoundTag> blockEntityTag = tag.getCompound(CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT);
        if (blockEntityTag.isPresent()) {
            this.internalTag = blockEntityTag.get();
            return;
        }

        // Paper start - new serialization format
        tag.getCompound(CraftMetaBlockState.BLOCK_ENTITY_TAG_CUSTOM_DATA.NBT).ifPresent(blockEntityCustomTag -> {
            this.blockEntityTag = CustomData.of(blockEntityCustomTag);
        });
        tag.getCompound(CraftMetaBlockState.BLOCK_ENTITY_COMPONENTS.NBT).ifPresent(components -> {
            this.components = DataComponentMap.CODEC.parse(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), components).getOrThrow();
        });
        // Paper end - new serialization format
    }

    @Override
    void serializeInternal(final Map<String, Tag> internalTags) {
        // Paper start - new serialization format
        if (!this.blockEntityTag.isEmpty()) {
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_TAG_CUSTOM_DATA.NBT, this.blockEntityTag.getUnsafe()); // unsafe because it's serialized right away
        }
        if (!this.components.isEmpty()) {
            final Tag componentsTag = DataComponentMap.CODEC.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), this.components).getOrThrow();
            internalTags.put(CraftMetaBlockState.BLOCK_ENTITY_COMPONENTS.NBT, componentsTag);
        }
        // Paper end - new serialization format
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", this.material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        hash = 61 * hash + this.blockEntityTag.hashCode();
        hash = 61 * hash + this.components.hashCode();

        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaBlockState other) {
            return Objects.equals(this.blockEntityTag, other.blockEntityTag) && Objects.equals(this.components, other.components);
        }
        return true;
    }

    boolean isBlockStateEmpty() {
        return this.blockEntityTag == null;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || (this.blockEntityTag.isEmpty() && this.components.isEmpty()));
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.blockEntityTag.isEmpty() && this.components.isEmpty(); // Paper
    }

    @Override
    public CraftMetaBlockState clone() {
        CraftMetaBlockState meta = (CraftMetaBlockState) super.clone();
        // Paper start - no need for "clone" because they are essentially immutables
        meta.blockEntityTag = this.blockEntityTag;
        meta.components = this.components;
        // Paper end
        return meta;
    }

    @Override
    public boolean hasBlockState() {
        return !this.blockEntityTag.isEmpty() || !this.components.isEmpty(); // Paper
    }

    // Paper start - add method to clear block state
    @Override
    public void clearBlockState() {
        this.blockEntityTag = CustomData.EMPTY;
        this.components = DataComponentMap.EMPTY;
    }
    // Paper end - add method to clear block state

    @Override
    // Paper start - create blockstate on-demand
    public CraftBlockEntityState<?> getBlockState() {
        BlockPos pos = BlockPos.ZERO;
        final Material stateMaterial = this.materialForBlockEntityType();
        if (!this.blockEntityTag.isEmpty()) {
            // Paper "id" field is always present now
            pos = BlockEntity.getPosFromTag(null, this.blockEntityTag.getUnsafe()); // unsafe is fine here, just querying
        }
        final net.minecraft.world.level.block.entity.BlockEntityType<?> type = java.util.Objects.requireNonNull(CraftBlockStates.getBlockEntityType(stateMaterial));
        final net.minecraft.world.level.block.state.BlockState nmsBlockState = ((org.bukkit.craftbukkit.block.data.CraftBlockData) this.getBlockData(stateMaterial)).getState();
        final net.minecraft.world.level.block.entity.BlockEntity blockEntity = java.util.Objects.requireNonNull(type.create(pos, nmsBlockState));
        if (!this.blockEntityTag.isEmpty()) {
            this.blockEntityTag.loadInto(blockEntity, CraftRegistry.getMinecraftRegistry());
        }
        final PatchedDataComponentMap patchedMap = new PatchedDataComponentMap(nmsBlockState.getBlock().asItem().components());
        patchedMap.setAll(this.components);
        final Applicator applicator = new Applicator() {};
        super.applyToItem(applicator);
        patchedMap.applyPatch(applicator.build());
        blockEntity.applyComponents(nmsBlockState.getBlock().asItem().components(), patchedMap.asPatch());

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(null, pos, nmsBlockState, blockEntity);
        // Paper end
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

            pos = BlockEntity.getPosFromTag(null, blockEntityTag);
        }

        // This is expected to always return a CraftBlockEntityState for the passed material:
        return (CraftBlockEntityState<?>) CraftBlockStates.getBlockState(CraftRegistry.getMinecraftRegistry(), pos, stateMaterial, blockEntityTag);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");

        Material stateMaterial = (this.material != Material.SHIELD) ? this.material : CraftMetaBlockState.shieldToBannerHack(null);
        Class<?> blockStateType = CraftBlockStates.getBlockStateType(stateMaterial);
        Preconditions.checkArgument(blockStateType == blockState.getClass() && blockState instanceof CraftBlockEntityState, "Invalid blockState for %s", this.material);

        // Paper start - when a new BlockState is set, the components from that block entity
        // have to be used to update the fields on CraftMetaItem
        final CraftBlockEntityState<?> craftBlockState = (CraftBlockEntityState<?>) blockState;
        final CompoundTag data = craftBlockState.getSnapshotCustomNbtOnly();
        final PatchedDataComponentMap patchedMap = new net.minecraft.core.component.PatchedDataComponentMap(craftBlockState.getHandle().getBlock().asItem().components());
        final net.minecraft.core.component.DataComponentMap map = craftBlockState.collectComponents();
        patchedMap.setAll(map);
        if (!data.isEmpty()) {
            patchedMap.set(BLOCK_ENTITY_TAG.TYPE, CustomData.of(data));
        }
        final DataComponentPatch patch = patchedMap.asPatch();
        this.updateFromPatch(patch, null);
        // we have to reset the fields because this should be like a "new" block entity is being used
        this.blockEntityTag = CustomData.EMPTY;
        this.components = DataComponentMap.EMPTY;
        this.updateBlockState(patch);
        // Paper end
    }

    private static Material shieldToBannerHack(CompoundTag tag) {
        if (tag != null) {
            Optional<String> baseColor = tag.getCompound("components").flatMap(components -> components.getString("minecraft:base_color"));
            if (baseColor.isPresent()) {
                DyeColor color = DyeColor.getByWoolData((byte) net.minecraft.world.item.DyeColor.byName(baseColor.get(), net.minecraft.world.item.DyeColor.WHITE).getId());
                return CraftMetaShield.shieldToBannerHack(color);
            }
        }

        return Material.WHITE_BANNER;
    }
}
