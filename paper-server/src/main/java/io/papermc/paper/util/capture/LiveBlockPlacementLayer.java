package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Supplier;

public record LiveBlockPlacementLayer(WorldCapturer level, net.minecraft.server.level.ServerLevel serverLevel) implements BlockPlacementPredictor {
    @Override
    public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
        return Optional.of(provideLive(() -> this.serverLevel.getBlockState(pos)));
    }

    @Override
    public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
        BlockState state = provideLive(() -> this.serverLevel.getBlockStateIfLoaded(pos));
        if (state == null) {
            return LoadedBlockState.UNLOADED;
        }

        return Optional.of(new LoadedBlockState(true, state));
    }

    @Override
    public Optional<BlockEntityPlacement> getLatestTileAt(BlockPos pos) {
        BlockEntity blockEntity = provideLive(() -> this.serverLevel.getBlockEntity(pos));
        if (blockEntity == null) {
            return BlockEntityPlacement.ABSENT;
        }

        return Optional.of(new BlockEntityPlacement(false, blockEntity));
    }


    public <T> T provideLive(Supplier<T> valueProvider) {
        SimpleBlockCapture blockCapture = this.level.getCapture();
        this.level.releaseCapture(null);
        T value = valueProvider.get();
        this.level.releaseCapture(blockCapture);

        return value;
    }
}
