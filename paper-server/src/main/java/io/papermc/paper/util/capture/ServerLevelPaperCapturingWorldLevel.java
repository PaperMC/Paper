package io.papermc.paper.util.capture;

import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.function.Consumer;

public interface ServerLevelPaperCapturingWorldLevel extends PaperCapturingWorldLevel {

    ServerLevel handle();

    @Override
    default GameRules getGameRules() {
        return this.handle().getGameRules();
    }

    @Override
    default void addTask(Consumer<ServerLevel> levelConsumer) {
        levelConsumer.accept(this.handle());
    }

    @Override
    default void sendBlockUpdated(BlockPos pos, BlockState state, BlockState blockState1, int updateClients) {
        this.handle().sendBlockUpdated(pos, state, blockState1, updateClients);
    }

    @Override
    default void setBlockEntity(BlockEntity blockEntity) {
        this.handle().setBlockEntity(blockEntity);
    }

    @Override
    default boolean setBlockSilent(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        return this.handle().setBlock(pos, state, flags, recursionLeft);
    }

    @Override
    default boolean setBlockAndUpdate(BlockPos blockPos, BlockState blockState) {
        return this.handle().setBlockAndUpdate(blockPos, blockState);
    }

    @Override
    default WorldConfiguration paperConfig() {
        return this.handle().paperConfig();
    }

    @Override
    default ChunkGenerator getGenerator() {
        return this.handle().getGenerator();
    }

    @Override
    default SimpleBlockCapture forkCaptureSession() {
        return this.handle().capturer.createCaptureSession();
    }
}
