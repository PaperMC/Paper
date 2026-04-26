package io.papermc.paper.util.capture;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Block state
class SimpleBlockPlacementPredictor implements BlockPlacementPredictor {

    private final CaptureRecordMap guesstimationMap = new CaptureRecordMap();

    public boolean setBlockState(BlockPlacementPredictor layer, BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
        BlockState oldState = layer.getLatestBlockAt(pos).orElse(Blocks.AIR.defaultBlockState());
        // Don't do any processing if the same
        if (oldState == blockState) {
            return false;
        } else {
            Block newBlock = blockState.getBlock();

            this.setLatestBlockAt(pos, blockState, updateFlags);

            // TODO: local heightmaps?
//            this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(i, y, i2, blockState);
//            this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(i, y, i2, blockState);
//            this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(i, y, i2, blockState);
//            this.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(i, y, i2, blockState);

            // LIGHT ENGINE CALCULATIONS

            boolean blockChanged = !oldState.is(newBlock);
            if (blockChanged && oldState.hasBlockEntity() && !blockState.shouldChangedStateKeepBlockEntity(oldState)) {
                this.removeBlockEntity(pos);
            }

//            if ((blockChanged || newBlock instanceof BaseRailBlock) && ((updateFlags & Block.UPDATE_NEIGHBORS) != 0 || updateMoveByPiston)) {
//                BlockState finalOldState = oldState;
//                this.capturingWorldLevel.addTask((level) -> {
//                    finalOldState.affectNeighborsAfterRemoval(level, pos, updateMoveByPiston);
//                });
//            }

            if (blockState.hasBlockEntity()) {
                BlockEntity blockEntity = this.getLatestBlockEntityAt(pos).map(BlockEntityPlacement::blockEntity).orElse(null);
                if (blockEntity != null && !blockEntity.isValidBlockState(blockState)) {
                    blockEntity = null;
                }

                if (blockEntity == null) {
                    blockEntity = ((EntityBlock) newBlock).newBlockEntity(pos, blockState);
                    if (blockEntity != null) {
                        this.addAndRegisterBlockEntity(blockEntity);
                    }
                } else {
                    blockEntity.setBlockState(blockState);
                }
            }
        }

        return true;
    }


    private void addAndRegisterBlockEntity(BlockEntity blockEntity) {
        this.guesstimationMap.setLatestBlockEntityAt(blockEntity.getBlockPos(), false, blockEntity);
    }

    private void removeBlockEntity(BlockPos pos) {
        this.guesstimationMap.setLatestBlockEntityAt(pos, true, null);
    }

    public boolean isEmpty() {
        return this.guesstimationMap.isEmpty();
    }

    @Override
    public Optional<BlockEntityPlacement> getLatestBlockEntityAt(BlockPos pos) {
        Optional<BlockEntity> value = this.guesstimationMap.getLatestBlockEntityAt(pos);
        if (value == null) {
            return Optional.empty();
        } else {
            return value
                    .map(block -> new BlockEntityPlacement(false, block))
                    .or(() -> BlockEntityPlacement.ABSENT);
        }
    }

    @Override
    public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
        return Optional.ofNullable(this.guesstimationMap.getLatestBlockStateAt(pos));
    }

    @Override
    public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
        return Optional.ofNullable(this.guesstimationMap.getLatestBlockStateAt(pos))
                .map((state) -> new LoadedBlockState(true, state));
    }

    public void setLatestBlockAt(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
        this.guesstimationMap.setLatestBlockStateAt(pos, blockState, updateFlags);
    }

    public CaptureRecordMap getRecordMap() {
        return this.guesstimationMap;
    }
}
