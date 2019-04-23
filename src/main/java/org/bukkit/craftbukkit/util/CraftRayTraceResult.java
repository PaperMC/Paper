package org.bukkit.craftbukkit.util;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.MovingObjectPosition.EnumMovingObjectType;
import net.minecraft.server.MovingObjectPositionBlock;
import net.minecraft.server.MovingObjectPositionEntity;
import net.minecraft.server.Vec3D;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static RayTraceResult fromNMS(World world, MovingObjectPosition nmsHitResult) {
        if (nmsHitResult == null || nmsHitResult.getType() == EnumMovingObjectType.MISS) return null;

        Vec3D nmsHitPos = nmsHitResult.getPos();
        Vector hitPosition = new Vector(nmsHitPos.x, nmsHitPos.y, nmsHitPos.z);
        BlockFace hitBlockFace = null;

        if (nmsHitResult.getType() == EnumMovingObjectType.ENTITY) {
            Entity hitEntity = ((MovingObjectPositionEntity) nmsHitResult).getEntity().getBukkitEntity();
            return new RayTraceResult(hitPosition, hitEntity, null);
        }

        Block hitBlock = null;
        BlockPosition nmsBlockPos = null;
        if (nmsHitResult.getType() == EnumMovingObjectType.BLOCK) {
            MovingObjectPositionBlock blockHitResult = (MovingObjectPositionBlock) nmsHitResult;
            hitBlockFace = CraftBlock.notchToBlockFace(blockHitResult.getDirection());
            nmsBlockPos = blockHitResult.getBlockPosition();
        }
        if (nmsBlockPos != null && world != null) {
            hitBlock = world.getBlockAt(nmsBlockPos.getX(), nmsBlockPos.getY(), nmsBlockPos.getZ());
        }
        return new RayTraceResult(hitPosition, hitBlock, hitBlockFace);
    }
}
