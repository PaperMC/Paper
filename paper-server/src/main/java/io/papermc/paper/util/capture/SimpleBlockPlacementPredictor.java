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

    public boolean setBlockState(BlockPlacementPredictor layer, BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
        BlockState blockState = layer.getLatestBlockAt(pos).orElse(Blocks.AIR.defaultBlockState());
        // Dont do any processing if the same
        if (blockState == state) {
            return false;
        } else {
            Block block = state.getBlock();

            this.setLatestBlockAt(pos, state, flags);

            // TODO: local heightmaps?
//            this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(i, y, i2, state);
//            this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(i, y, i2, state);
//            this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(i, y, i2, state);
//            this.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(i, y, i2, state);

            // LIGHT ENGINE CALCULATIONS

            boolean differentState = !blockState.is(block);
            if (differentState && blockState.hasBlockEntity() && !state.shouldChangedStateKeepBlockEntity(blockState)) {
                this.removeBlockEntity(pos);
            }


//            if ((differentState || block instanceof BaseRailBlock) && ((flags & Block.UPDATE_NEIGHBORS) != 0 || updateMoveByPiston)) {
//                BlockState finalBlockState = blockState;
//                this.capturingWorldLevel.addTask((level) -> {
//                    finalBlockState.affectNeighborsAfterRemoval(level, pos, updateMoveByPiston);
//                });
//            }

            if (state.hasBlockEntity()) {
                BlockEntity blockEntity = this.getLatestBlockEntityAt(pos).map(BlockEntityPlacement::blockEntity).orElse(null);
                if (blockEntity != null && !blockEntity.isValidBlockState(state)) {
                    blockEntity = null;
                }

                if (blockEntity == null) {
                    blockEntity = ((EntityBlock) block).newBlockEntity(pos, state);
                    if (blockEntity != null) {
                        this.addAndRegisterBlockEntity(blockEntity);
                    }
                } else {
                    blockEntity.setBlockState(state);
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

    public void setLatestBlockAt(BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
        this.guesstimationMap.setLatestBlockStateAt(pos, state, flags);
    }

    public CaptureRecordMap getRecordMap() {
        return this.guesstimationMap;
    }
}
