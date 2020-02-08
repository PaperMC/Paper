package org.bukkit.craftbukkit;

import org.bukkit.HeightMap;

final class CraftHeightMap {

    private CraftHeightMap() {
    }

    public static net.minecraft.server.HeightMap.Type toNMS(HeightMap bukkitHeightMap) {
        switch (bukkitHeightMap) {
            case MOTION_BLOCKING_NO_LEAVES:
                return net.minecraft.server.HeightMap.Type.MOTION_BLOCKING_NO_LEAVES;
            case OCEAN_FLOOR:
                return net.minecraft.server.HeightMap.Type.OCEAN_FLOOR;
            case OCEAN_FLOOR_WG:
                return net.minecraft.server.HeightMap.Type.OCEAN_FLOOR_WG;
            case WORLD_SURFACE:
                return net.minecraft.server.HeightMap.Type.WORLD_SURFACE;
            case WORLD_SURFACE_WG:
                return net.minecraft.server.HeightMap.Type.WORLD_SURFACE_WG;
            case MOTION_BLOCKING:
                return net.minecraft.server.HeightMap.Type.MOTION_BLOCKING;
            default:
                throw new EnumConstantNotPresentException(net.minecraft.server.HeightMap.Type.class, bukkitHeightMap.name());
        }
    }

    public static HeightMap fromNMS(net.minecraft.server.HeightMap.Type nmsHeightMapType) {
        switch (nmsHeightMapType) {
            case WORLD_SURFACE_WG:
                return HeightMap.WORLD_SURFACE_WG;
            case WORLD_SURFACE:
                return HeightMap.WORLD_SURFACE;
            case OCEAN_FLOOR_WG:
                return HeightMap.OCEAN_FLOOR_WG;
            case OCEAN_FLOOR:
                return HeightMap.OCEAN_FLOOR;
            case MOTION_BLOCKING_NO_LEAVES:
                return HeightMap.MOTION_BLOCKING_NO_LEAVES;
            case MOTION_BLOCKING:
                return HeightMap.MOTION_BLOCKING;
            default:
                throw new EnumConstantNotPresentException(HeightMap.class, nmsHeightMapType.name());
        }
    }
}
