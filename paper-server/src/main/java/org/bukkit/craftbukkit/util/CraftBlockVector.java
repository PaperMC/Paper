package org.bukkit.craftbukkit.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.bukkit.util.BlockVector;

public final class CraftBlockVector {

    private CraftBlockVector() {
    }

    public static BlockPos toBlockPosition(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    public static BlockVector toBukkit(Vec3i vec3i) {
        return new BlockVector(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }
}
