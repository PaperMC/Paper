package io.papermc.paper.util.capture;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

public interface ServerLevelPaperCapturingWorldLevel extends PaperCapturingWorldLevel {

    @Override
    default GameRules getGameRules() {
        return this.getLevel().getGameRules();
    }

    @Override
    default void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, @Block.UpdateFlags int flags) {
        this.getLevel().sendBlockUpdated(pos, oldState, newState, flags);
    }

    @Override
    default void setBlockEntity(BlockEntity blockEntity) {
        this.getLevel().setBlockEntity(blockEntity);
    }

    @Override
    default boolean setBlockSilent(BlockPos pos, BlockState state, @Block.UpdateFlags int flags, int recursionLeft) {
        return this.getLevel().setBlock(pos, state, flags, recursionLeft);
    }

    @Override
    default boolean setBlockAndUpdate(BlockPos pos, BlockState state) {
        return this.getLevel().setBlockAndUpdate(pos, state);
    }

    @Override
    default void addTask(Consumer<ServerLevel> level) {
        level.accept(this.getLevel());
    }

    @Override
    default SimpleBlockCapture forkCaptureSession() {
        return this.getLevel().capturer.createCaptureSession();
    }
}
