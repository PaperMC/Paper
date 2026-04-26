package io.papermc.paper.util.capture;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

public interface PaperCapturingWorldLevel extends WorldGenLevel {

    GameRules getGameRules();

    void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags);

    void setBlockEntity(BlockEntity blockEntity);

    boolean setBlockSilent(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit);

    ServerChunkCache getChunkSource();

    boolean setBlockAndUpdate(BlockPos pos, BlockState blockState);

    void addTask(Consumer<ServerLevel> level);

    SimpleBlockCapture forkCaptureSession();
}
