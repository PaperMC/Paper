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
    default void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags) {
        this.getLevel().sendBlockUpdated(pos, old, current, updateFlags);
    }

    @Override
    default void setBlockEntity(BlockEntity blockEntity) {
        this.getLevel().setBlockEntity(blockEntity);
    }

    @Override
    default boolean setBlockSilent(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        return this.getLevel().setBlock(pos, blockState, updateFlags, updateLimit);
    }

    @Override
    default boolean setBlockAndUpdate(BlockPos pos, BlockState blockState) {
        return this.getLevel().setBlockAndUpdate(pos, blockState);
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
