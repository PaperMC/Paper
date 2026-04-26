package io.papermc.paper.util.capture;

import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record LiveBlockPlacementLayer(WorldCapturer capturer, ServerLevel level) implements BlockPlacementPredictor {

    @Override
    public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
        return Optional.of(provideLive(() -> this.level.getBlockState(pos)));
    }

    @Override
    public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
        BlockState state = provideLive(() -> this.level.getBlockStateIfLoaded(pos));
        if (state == null) {
            return LoadedBlockState.UNLOADED;
        }

        return Optional.of(new LoadedBlockState(true, state));
    }

    @Override
    public Optional<BlockEntityPlacement> getLatestBlockEntityAt(BlockPos pos) {
        BlockEntity blockEntity = provideLive(() -> this.level.getBlockEntity(pos));
        if (blockEntity == null) {
            return BlockEntityPlacement.ABSENT;
        }

        return Optional.of(new BlockEntityPlacement(false, blockEntity));
    }

    public <T> @Nullable T provideLive(Supplier<@Nullable T> valueProvider) {
        SimpleBlockCapture blockCapture = this.capturer.getCapture();
        this.capturer.releaseCapture(null);
        T value = valueProvider.get();
        this.capturer.releaseCapture(blockCapture);

        return value;
    }
}
