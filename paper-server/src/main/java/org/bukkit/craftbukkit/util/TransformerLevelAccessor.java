package org.bukkit.craftbukkit.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ProblemReporter;
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
import org.slf4j.Logger;

public class TransformerLevelAccessor extends DelegatedLevelAccessor {

    private static final Logger LOGGER = LogUtils.getLogger();

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

    public boolean setCraftBlock(BlockPos pos, CraftBlockState craftBlockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        craftBlockState = this.structureTransformer.transformCraftState(craftBlockState);
        // This code is based on the method 'net.minecraft.world.level.levelgen.structure.StructurePiece#placeBlock'
        // It ensures that any kind of block is updated correctly upon placing it
        BlockState snapshot = craftBlockState.getHandle();
        boolean result = super.setBlock(pos, snapshot, updateFlags, updateLimit);
        FluidState fluidState = this.getFluidState(pos);
        if (!fluidState.isEmpty()) {
            this.scheduleTick(pos, fluidState.getType(), 0);
        }
        if (StructurePiece.SHAPE_CHECK_BLOCKS.contains(snapshot.getBlock())) {
            this.getChunk(pos).markPosForPostprocessing(pos);
        }
        BlockEntity blockEntity = this.getBlockEntity(pos);
        if (blockEntity != null && craftBlockState instanceof CraftBlockEntityState<?> craftEntityState) {
            try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
                () -> "TransformerLevelAccessor@" + pos.toShortString(), LOGGER
            )) {
                blockEntity.loadWithComponents(TagValueInput.create(
                    problemReporter, this.registryAccess(), craftEntityState.getSnapshotNBT()
                ));
            }
        }
        return result;
    }

    public boolean setCraftBlock(BlockPos pos, CraftBlockState craftBlockState, @Block.UpdateFlags int updateFlags) {
        return this.setCraftBlock(pos, craftBlockState, updateFlags, Block.UPDATE_LIMIT);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        if (this.canTransformBlocks()) {
            return this.setCraftBlock(pos, (CraftBlockState) CraftBlockStates.getBlockState(this, pos, blockState, null), updateFlags, updateLimit);
        }
        return super.setBlock(pos, blockState, updateFlags, updateLimit);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
        return this.setBlock(pos, blockState, updateFlags, Block.UPDATE_LIMIT);
    }
}
