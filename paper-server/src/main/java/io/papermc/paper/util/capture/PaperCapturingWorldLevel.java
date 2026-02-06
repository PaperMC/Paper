package io.papermc.paper.util.capture;

import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.gamerules.GameRules;

public interface PaperCapturingWorldLevel extends WorldGenLevel {

    GameRules getGameRules();

    void addTask(java.util.function.Consumer<net.minecraft.server.level.ServerLevel> levelConsumer);

    void sendBlockUpdated(BlockPos pos, BlockState state, BlockState blockState1, int updateClients);

    void setBlockEntity(BlockEntity blockEntity);

    boolean setBlockSilent(BlockPos pos, BlockState state, int flags, int recursionLeft);

    ServerChunkCache getChunkSource();

    boolean setBlockAndUpdate(BlockPos blockPos, BlockState blockState);

    WorldConfiguration paperConfig();

    ChunkGenerator getGenerator();

    SimpleBlockCapture fork();
}

