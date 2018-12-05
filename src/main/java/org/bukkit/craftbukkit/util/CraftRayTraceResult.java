package org.bukkit.craftbukkit.util;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.MovingObjectPosition.EnumMovingObjectType;
import net.minecraft.server.Vec3D;

public class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static RayTraceResult fromNMS(World world, MovingObjectPosition nmsHitResult) {
        if (nmsHitResult == null || nmsHitResult.type == EnumMovingObjectType.MISS) return null;

        Vec3D nmsHitPos = nmsHitResult.pos;
        Vector hitPosition = new Vector(nmsHitPos.x, nmsHitPos.y, nmsHitPos.z);
        BlockFace hitBlockFace = null;

        if (nmsHitResult.direction != null) {
            hitBlockFace = CraftBlock.notchToBlockFace(nmsHitResult.direction);
        }

        if (nmsHitResult.entity != null) {
            Entity hitEntity = nmsHitResult.entity.getBukkitEntity();
            return new RayTraceResult(hitPosition, hitEntity, hitBlockFace);
        }

        Block hitBlock = null;
        BlockPosition nmsBlockPos = nmsHitResult.getBlockPosition();
        if (nmsBlockPos != null && world != null) {
            hitBlock = world.getBlockAt(nmsBlockPos.getX(), nmsBlockPos.getY(), nmsBlockPos.getZ());
        }
        return new RayTraceResult(hitPosition, hitBlock, hitBlockFace);
    }
}
