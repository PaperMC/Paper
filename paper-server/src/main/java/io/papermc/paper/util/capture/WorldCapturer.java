package io.papermc.paper.util.capture;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// TODO: Cleanup this state, because its held on the server world. I had proper state handling but threw it away at some point
public class WorldCapturer {

    public final ServerLevel world;

    private SimpleBlockCapture capture;

    public WorldCapturer(Level world) {
        this.world = (ServerLevel) world;
    }

    public SimpleBlockCapture createCaptureSession() {
        this.capture = new SimpleBlockCapture(this, world);

        return this.capture;
    }

    public void releaseCapture() {
        this.capture = null;
    }

    public SimpleBlockCapture getCapture() {
        return capture;
    }

    public boolean isCapturing() {
        return this.capture != null;
    }
}