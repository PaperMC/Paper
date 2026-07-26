package org.bukkit.craftbukkit.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.Nullable;

public final class CraftLocation {

    private CraftLocation() {
    }

    public static Location toBukkit(Vec3 pos) {
        return toBukkit(pos, (World) null);
    }

    public static Location toBukkit(Vec3 pos, World world) {
        return toBukkit(pos, world, 0.0F, 0.0F);
    }

    public static Location toBukkit(Vec3 pos, World world, float yaw, float pitch) {
        return new Location(world, pos.x(), pos.y(), pos.z(), yaw, pitch);
    }

    public static Location toBukkit(Vec3 pos, Level level) {
        return toBukkit(pos, level.getWorld());
    }

    public static Location toBukkit(Vec3 pos, Level level, float yaw, float pitch) {
        return toBukkit(pos, level.getWorld(), yaw, pitch);
    }

    public static Location toBukkit(Vec3i pos) {
        return toBukkit(pos, (World) null);
    }

    public static Location toBukkit(Vec3i pos, World world) {
        return toBukkit(pos, world, 0.0F, 0.0F);
    }

    public static Location toBukkit(Vec3i pos, World world, float yaw, float pitch) {
        return new Location(world, pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
    }

    public static Location toBukkit(Vec3i pos, Level level) {
        return toBukkit(pos, level.getWorld());
    }

    public static Location toBukkit(Vec3i pos, Level level, float yaw, float pitch) {
        return toBukkit(pos, level.getWorld(), yaw, pitch);
    }

    public static Location toBukkit(Node point, Level level) {
        return new Location(level.getWorld(), point.x, point.y, point.z);
    }

    public static BlockPos toBlockPos(Location loc) {
        return new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static net.minecraft.core.GlobalPos toGlobalPos(Location loc) {
        return net.minecraft.core.GlobalPos.of(((org.bukkit.craftbukkit.CraftWorld) loc.getWorld()).getHandle().dimension(), toBlockPos(loc));
    }

    public static @Nullable Location fromGlobalPos(net.minecraft.core.GlobalPos globalPos) {
        ServerLevel level = MinecraftServer.getServer().getLevel(globalPos.dimension());
        return level != null ? CraftLocation.toBukkit(globalPos.pos(), level) : null;
    }

    public static Vec3 toVec3(Location loc) {
        return new Vec3(loc.getX(), loc.getY(), loc.getZ());
    }
}
