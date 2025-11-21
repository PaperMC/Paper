package org.bukkit.craftbukkit.entity.memory;

import java.util.UUID;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.SpearAttack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;

public final class CraftMemoryMapper {

    private CraftMemoryMapper() {}

    public static Object fromNms(Object object) {
        if (object instanceof GlobalPos globalPos) {
            return CraftLocation.fromGlobalPos(globalPos);
        } else if (object instanceof SpearAttack.SpearStatus spearStatus) {
            return CraftSpearAttack.fromNms(spearStatus);
        } else if (object instanceof Long longValue) {
            return longValue;
        } else if (object instanceof UUID uuid) {
            return uuid;
        } else if (object instanceof Boolean booleanValue) {
            return booleanValue;
        } else if (object instanceof Integer integerValue) {
            return integerValue;
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }

    public static Object toNms(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof org.bukkit.entity.memory.SpearAttack.SpearStatus spearStatus) {
            return CraftSpearAttack.toNms(spearStatus);
        } else if (object instanceof Location location) {
            return CraftLocation.toGlobalPos(location);
        } else if (object instanceof Long longValue) {
            return longValue;
        } else if (object instanceof UUID uuid) {
            return uuid;
        } else if (object instanceof Boolean booleanValue) {
            return booleanValue;
        } else if (object instanceof Integer integerValue) {
            return integerValue;
        }

        throw new UnsupportedOperationException("Do not know how to map " + object);
    }
}
