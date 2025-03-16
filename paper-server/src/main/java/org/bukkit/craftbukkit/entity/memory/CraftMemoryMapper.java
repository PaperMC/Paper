package org.bukkit.craftbukkit.entity.memory;

import java.util.UUID;
import net.minecraft.core.GlobalPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;

public final class CraftMemoryMapper {

    private CraftMemoryMapper() {}

    public static Object fromNms(Object object) {
        if (object instanceof GlobalPos) {
            return CraftLocation.fromGlobalPos((GlobalPos) object);
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof UUID) {
            return (UUID) object;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof Integer) {
            return (Integer) object;
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Object toNms(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Location) {
            return CraftLocation.toGlobalPos((Location) object);
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof UUID) {
            return (UUID) object;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof Integer) {
            return (Integer) object;
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }
}
