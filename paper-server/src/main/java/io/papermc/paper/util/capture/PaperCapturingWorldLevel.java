package io.papermc.paper.util.capture;

import io.papermc.paper.configuration.WorldConfiguration;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gamerules.GameRules;

public interface PaperCapturingWorldLevel extends WorldGenLevel {

    GameRules getGameRules();

    void addTask(Consumer<ServerLevel> level);

    void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, @Block.UpdateFlags int flags);

    void setBlockEntity(BlockEntity blockEntity);

    boolean setBlockSilent(BlockPos pos, BlockState state, @Block.UpdateFlags int flags, int recursionLeft);

    ServerChunkCache getChunkSource();

    boolean setBlockAndUpdate(BlockPos pos, BlockState state);

    WorldConfiguration paperConfig();

    ChunkGenerator getGenerator();

    SimpleBlockCapture forkCaptureSession();
}
