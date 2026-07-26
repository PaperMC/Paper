package org.bukkit.craftbukkit.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CraftRayTraceResult {

    private CraftRayTraceResult() {}

    public static @Nullable RayTraceResult convertFromInternal(LevelAccessor world, @Nullable HitResult hitResult) {
        if (hitResult == null || hitResult.getType() == Type.MISS) return null;

        Vector hitPos = CraftVector.toBukkit(hitResult.getLocation());
        BlockFace hitBlockFace = null;

        if (hitResult.getType() == Type.ENTITY) {
            Entity hitEntity = ((EntityHitResult) hitResult).getEntity().getBukkitEntity();
            return new RayTraceResult(hitPos, hitEntity, null);
        }

        BlockPos pos = null;
        if (hitResult.getType() == Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            hitBlockFace = CraftBlock.notchToBlockFace(blockHitResult.getDirection());
            pos = blockHitResult.getBlockPos();
        }

        Block hitBlock = null;
        if (pos != null) {
            hitBlock = CraftBlock.at(world, pos);
        }
        return new RayTraceResult(hitPos, hitBlock, hitBlockFace);
    }
}
