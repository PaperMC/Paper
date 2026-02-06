package io.papermc.paper.util.capture;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

// TODO: Cleanup this state, because its held on the server world. I had proper state handling but threw it away at some point
public class WorldCapturer {

    public final ServerLevel world;

    private SimpleBlockCapture capture;

    public WorldCapturer(Level world) {
        this.world = (ServerLevel) world;
    }

    public SimpleBlockCapture createCaptureSession(BlockPlacementPredictor blockPlacementPredictor) {
        this.capture = new SimpleBlockCapture(blockPlacementPredictor, world, this.capture);

        return this.capture;
    }

    public SimpleBlockCapture createCaptureSession() {
        return this.createCaptureSession(new LiveBlockPlacementLayer(this.world));
    }

    public void releaseCapture(SimpleBlockCapture oldCapture) {
        this.capture = oldCapture;
    }

    public SimpleBlockCapture getCapture() {
        return capture;
    }

    public boolean isCapturing() {
        return this.capture != null;
    }
}