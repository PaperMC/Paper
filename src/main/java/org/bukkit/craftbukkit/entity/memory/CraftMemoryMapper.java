package org.bukkit.craftbukkit.entity.memory;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.GlobalPos;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

public final class CraftMemoryMapper {

    private CraftMemoryMapper() {}

    public static Object fromNms(Object object) {
        if (object instanceof GlobalPos) {
            return fromNms((GlobalPos) object);
        } else if (object instanceof Long) {
            return object;
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Object toNms(Object object) {
        if (object instanceof Location) {
            return toNms((Location) object);
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Location fromNms(GlobalPos globalPos) {
        // PAIL: globalPos.a() -> getDimensionManager()
        // PAIL: globalPos.b() -> getBlockPosition()
        return new org.bukkit.Location(((CraftServer) Bukkit.getServer()).getServer().getWorldServer(globalPos.a()).getWorld(), globalPos.b().getX(), globalPos.b().getY(), globalPos.b().getZ());
    }

    public static GlobalPos toNms(Location location) {
        // PAIL: GlobalPos.a(DmensionManager, BlockPosition) -> create()
        return GlobalPos.a(((CraftWorld) location.getWorld()).getHandle().getWorldProvider().getDimensionManager(), new BlockPosition(location.getX(), location.getY(), location.getZ()));
    }
}
