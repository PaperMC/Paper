package net.minecraft.server;

public class ShapeDetectorShape {

    public final Vec3D position;
    public final Vec3D velocity;
    public final float yaw;
    public final float pitch;

    public ShapeDetectorShape(Vec3D vec3d, Vec3D vec3d1, float f, float f1) {
        this.position = vec3d;
        this.velocity = vec3d1;
        this.yaw = f;
        this.pitch = f1;
    }
}
