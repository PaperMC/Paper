package org.bukkit.craftbukkit.util;

import org.bukkit.util.Vector;

public final class CraftVector {

    private CraftVector() {
    }

    public static Vector toBukkit(net.minecraft.world.phys.Vec3 vec3) {
        return new Vector(vec3.x(), vec3.y(), vec3.z());
    }

    public static net.minecraft.world.phys.Vec3 toVec3(Vector vector) {
        return new net.minecraft.world.phys.Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector toBukkit(net.minecraft.core.BlockPos pos) {
        return new Vector(pos.getX(), pos.getY(), pos.getZ());
    }

    public static net.minecraft.core.BlockPos toBlockPos(Vector vector) {
        return net.minecraft.core.BlockPos.containing(vector.getX(), vector.getY(), vector.getZ());
    }
}
