package org.bukkit.craftbukkit.util;

public final class CraftVector {

    private CraftVector() {
    }

    public static org.bukkit.util.Vector toBukkit(net.minecraft.world.phys.Vec3D nms) {
        return new org.bukkit.util.Vector(nms.x, nms.y, nms.z);
    }

    public static net.minecraft.world.phys.Vec3D toNMS(org.bukkit.util.Vector bukkit) {
        return new net.minecraft.world.phys.Vec3D(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
