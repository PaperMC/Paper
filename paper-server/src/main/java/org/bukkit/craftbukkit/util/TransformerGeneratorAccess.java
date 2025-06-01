package org.bukkit.craftbukkit.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.TagValueInput;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.jetbrains.annotations.Nullable;

public class TransformerGeneratorAccess extends DelegatedGeneratorAccess {

    private CraftStructureTransformer structureTransformer;

    public void setStructureTransformer(CraftStructureTransformer structureTransformer) {
        this.structureTransformer = structureTransformer;
    }

    public CraftStructureTransformer getStructureTransformer() {
        return this.structureTransformer;
    }

    public boolean canTransformBlocks() {
        return this.structureTransformer != null && this.structureTransformer.canTransformBlocks();
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(entity)) {
            return false;
        }
        return super.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity entity, @Nullable SpawnReason reason) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(entity)) {
            return false;
        }
        return super.addFreshEntity(entity, reason);
    }

    public boolean setCraftBlock(BlockPos position, CraftBlockState craftBlockState, int flags, int recursionLeft) {
        craftBlockState = this.structureTransformer.transformCraftState(craftBlockState);
        // This code is based on the method 'net.minecraft.world.level.levelgen.structure.StructurePiece#placeBlock'
        // It ensures that any kind of block is updated correctly upon placing it
        BlockState snapshot = craftBlockState.getHandle();
        boolean result = super.setBlock(position, snapshot, flags, recursionLeft);
        FluidState fluidState = this.getFluidState(position);
        if (!fluidState.isEmpty()) {
            this.scheduleTick(position, fluidState.getType(), 0);
        }
        if (StructurePiece.SHAPE_CHECK_BLOCKS.contains(snapshot.getBlock())) {
            this.getChunk(position).markPosForPostprocessing(position);
        }
        BlockEntity blockEntity = this.getBlockEntity(position);
        if (blockEntity != null && craftBlockState instanceof CraftBlockEntityState<?> craftEntityState) {
            blockEntity.loadWithComponents(TagValueInput.createDiscarding(
                this.registryAccess(), craftEntityState.getSnapshotNBT()
            ));
        }
        return result;
    }

    public boolean setCraftBlock(BlockPos pos, CraftBlockState craftBlockState, int flags) {
        return this.setCraftBlock(pos, craftBlockState, flags, Block.UPDATE_LIMIT);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        if (this.canTransformBlocks()) {
            return this.setCraftBlock(pos, (CraftBlockState) CraftBlockStates.getBlockState(this, pos, state, null), flags, recursionLeft);
        }
        return super.setBlock(pos, state, flags, recursionLeft);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags) {
        return this.setBlock(pos, state, flags, Block.UPDATE_LIMIT);
    }
}
