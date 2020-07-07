package org.bukkit.craftbukkit.util;

public final class CraftVector {

    private CraftVector() {
    }

    public static org.bukkit.util.Vector toBukkit(net.minecraft.world.phys.Vec3 nms) {
        return new org.bukkit.util.Vector(nms.x, nms.y, nms.z);
    }

    public static net.minecraft.world.phys.Vec3 toNMS(org.bukkit.util.Vector bukkit) {
        return new net.minecraft.world.phys.Vec3(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
    // Paper start
    public static org.bukkit.util.Vector toBukkit(net.minecraft.core.BlockPos blockPosition) {
        return new org.bukkit.util.Vector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    public static net.minecraft.core.BlockPos toBlockPos(org.bukkit.util.Vector bukkit) {
        return net.minecraft.core.BlockPos.containing(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
    // Paper end
}
