package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SimpleBlockCapture implements AutoCloseable {

    private final MinecraftCaptureBridge capturingWorldLevel;
    private final ServerLevel level;

    private boolean isOverlayingCaptureOnLevel = false;


    private final SimpleBlockCapture oldCapture;


    public SimpleBlockCapture(BlockPlacementPredictor base, ServerLevel world, SimpleBlockCapture oldCapture) {
        this.capturingWorldLevel = new MinecraftCaptureBridge(world, base);
        this.level = world;
        this.oldCapture = oldCapture;
    }

    public MinecraftCaptureBridge capturingWorldLevel() {
        return this.capturingWorldLevel;
    }

    public boolean isCapturing() {
        return true;
    }

    public Map<Location, BlockState> getCapturedBlockStates() {
        return this.capturingWorldLevel.calculateLatestBlockStates(this.level);
    }

    public net.minecraft.world.level.block.state.BlockState getOverlayBlockState(final BlockPos pos) {
        return this.capturingWorldLevel.getLatestBlockState(pos);
    }

    public @Nullable Optional<@Nullable BlockEntity> getOverlayBlockEntity(final BlockPos pos) {
        return this.capturingWorldLevel.getLatestBlockEntity(pos);
    }


    // This is done so that the captured blocks appear ontop of the world.
    public void overlayCaptureOnLevel() {
        this.isOverlayingCaptureOnLevel = true;
        this.capturingWorldLevel.allowWriteOnLevel();
    }

    public boolean isOverlayingCaptureOnLevel() {
        return isOverlayingCaptureOnLevel;
    }

    public void finalizePlacement() {
        this.level.capturer.releaseCapture(this.oldCapture);
        this.capturingWorldLevel.applyTasks();
    }

    @Override
    public void close() {
        this.level.capturer.releaseCapture(this.oldCapture);
    }

    public net.minecraft.world.level.block.state.BlockState getCaptureBlockStateIfLoaded(BlockPos pos) {
        return this.capturingWorldLevel.getBlockStateIfLoaded(pos);
    }
}
