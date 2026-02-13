package io.papermc.paper.util.capture;

import io.papermc.paper.configuration.WorldConfiguration;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gamerules.GameRules;

public interface ServerLevelPaperCapturingWorldLevel extends PaperCapturingWorldLevel {

    ServerLevel handle();

    @Override
    default GameRules getGameRules() {
        return this.handle().getGameRules();
    }

    @Override
    default void addTask(Consumer<ServerLevel> level) {
        level.accept(this.handle());
    }

    @Override
    default void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, @Block.UpdateFlags int flags) {
        this.handle().sendBlockUpdated(pos, oldState, newState, flags);
    }

    @Override
    default void setBlockEntity(BlockEntity blockEntity) {
        this.handle().setBlockEntity(blockEntity);
    }

    @Override
    default boolean setBlockSilent(BlockPos pos, BlockState state, @Block.UpdateFlags int flags, int recursionLeft) {
        return this.handle().setBlock(pos, state, flags, recursionLeft);
    }

    @Override
    default boolean setBlockAndUpdate(BlockPos pos, BlockState state) {
        return this.handle().setBlockAndUpdate(pos, state);
    }

    @Override
    default WorldConfiguration paperConfig() {
        return this.handle().paperConfig();
    }

    @Override
    default ChunkGenerator getGenerator() {
        return this.handle().getGenerator(); // todo StackOverflowError when bonemealing a moss block
    }

    @Override
    default SimpleBlockCapture forkCaptureSession() {
        return this.handle().capturer.createCaptureSession();
    }
}
