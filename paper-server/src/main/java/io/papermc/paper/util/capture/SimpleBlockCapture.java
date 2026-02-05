package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SimpleBlockCapture implements AutoCloseable  {

    private final MinecraftCaptureBridge capturingWorldLevel;
    private final ServerLevel level;

    private boolean openLegacyCapturing = false;

    public SimpleBlockCapture(WorldCapturer blockCapturer, ServerLevel world) {
        this.capturingWorldLevel = new MinecraftCaptureBridge(world);
        this.level = world;
    }

    public PaperCapturingWorldLevel capturingWorldLevel() {
        return this.capturingWorldLevel;
    }

    public boolean isCapturing() {
        return true;
    }

    public Map<Location, BlockState> getCapturedBlockStates() {
        return this.capturingWorldLevel.calculateLatestBlockStates(level);
    }

    public net.minecraft.world.level.block.state.BlockState getCaptureBlockState(final BlockPos pos) {
        return this.capturingWorldLevel.getLatestBlockState(pos);
    }

    public @Nullable Optional<@Nullable BlockEntity> getCaptureBlockEntity(final BlockPos pos) {
        return this.capturingWorldLevel.getLatestBlockEntity(pos);
    }


    public void openLegacySupport() {
        this.openLegacyCapturing = true;
        this.capturingWorldLevel.activateLegacyCapture();

    }

    public boolean isViewingCaptureState() {
        return openLegacyCapturing;
    }

    public void finalizePlacement() {
        this.level.capturer.releaseCapture();
        this.capturingWorldLevel.applyTasks();
    }

    @Override
    public void close() {
        this.level.capturer.releaseCapture();
    }

    public net.minecraft.world.level.block.state.BlockState getCaptureBlockStateIfLoaded(BlockPos pos) {
        return this.capturingWorldLevel.getBlockStateIfLoaded(pos);
    }
}
