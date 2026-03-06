package io.papermc.paper.util.capture;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// TODO: Cleanup this state, because its held on the server world. I had proper state handling but threw it away at some point
public class WorldCapturer {

    public final ServerLevel level;

    private @Nullable SimpleBlockCapture capture;

    public WorldCapturer(Level level) {
        this.level = (ServerLevel) level;
    }

    public SimpleBlockCapture createCaptureSession(BlockPlacementPredictor blockPlacementPredictor) {
        this.capture = new SimpleBlockCapture(blockPlacementPredictor, this.level, this.capture);
        return this.capture;
    }

    public SimpleBlockCapture createCaptureSession() {
        return this.createCaptureSession(new LiveBlockPlacementLayer(this, this.level));
    }

    public void releaseCapture(@Nullable SimpleBlockCapture oldCapture) {
        this.capture = oldCapture;
    }

    public @Nullable SimpleBlockCapture getCapture() {
        return this.capture;
    }

    public boolean isCapturing() {
        return this.capture != null;
    }
}
