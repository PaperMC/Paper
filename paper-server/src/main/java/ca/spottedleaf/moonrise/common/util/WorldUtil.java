package ca.spottedleaf.moonrise.common.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;

public final class WorldUtil {

    // min, max are inclusive

    public static int getMaxSection(final LevelHeightAccessor world) {
        return world.getMaxSectionY();
    }

    public static int getMaxSection(final Level world) {
        return world.getMaxSectionY();
    }

    public static int getMinSection(final LevelHeightAccessor world) {
        return world.getMinSectionY();
    }

    public static int getMinSection(final Level world) {
        return world.getMinSectionY();
    }

    public static int getMaxLightSection(final LevelHeightAccessor world) {
        return getMaxSection(world) + 1;
    }

    public static int getMinLightSection(final LevelHeightAccessor world) {
        return getMinSection(world) - 1;
    }



    public static int getTotalSections(final LevelHeightAccessor world) {
        return getMaxSection(world) - getMinSection(world) + 1;
    }

    public static int getTotalLightSections(final LevelHeightAccessor world) {
        return getMaxLightSection(world) - getMinLightSection(world) + 1;
    }

    public static int getMinBlockY(final LevelHeightAccessor world) {
        return getMinSection(world) << 4;
    }

    public static int getMaxBlockY(final LevelHeightAccessor world) {
        return (getMaxSection(world) << 4) | 15;
    }

    public static String getWorldName(final Level world) {
        if (world == null) {
            return "null world";
        }
        return world.getWorld().getName(); // Paper
    }

    private WorldUtil() {
        throw new RuntimeException();
    }
}
