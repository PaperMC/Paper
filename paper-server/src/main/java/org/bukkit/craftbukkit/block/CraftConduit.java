package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Conduit;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;

public class CraftConduit extends CraftBlockEntityState<ConduitBlockEntity> implements Conduit {

    public CraftConduit(World world, ConduitBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftConduit(CraftConduit state, Location location) {
        super(state, location);
    }

    @Override
    public CraftConduit copy() {
        return new CraftConduit(this, null);
    }

    @Override
    public CraftConduit copy(Location location) {
        return new CraftConduit(this, location);
    }

    @Override
    public boolean isActive() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        return conduit != null && conduit.isActive();
    }

    @Override
    public boolean isHunting() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        return conduit != null && conduit.isHunting();
    }

    @Override
    public Collection<Block> getFrameBlocks() {
        this.ensureNoWorldGeneration();
        Collection<Block> blocks = new ArrayList<>();

        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        if (conduit != null) {
            for (BlockPos position : conduit.effectBlocks) {
                blocks.add(CraftBlock.at(this.getWorldHandle(), position));
            }
        }

        return blocks;
    }

    @Override
    public int getFrameBlockCount() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        return (conduit != null) ? conduit.effectBlocks.size() : 0;
    }

    @Override
    public int getRange() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        return (conduit != null) ? ConduitBlockEntity.getRange(conduit.effectBlocks) : 0;
    }

    @Override
    public boolean setTarget(LivingEntity target) {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        if (conduit == null) {
            return false;
        }

        net.minecraft.world.entity.LivingEntity currentTarget = conduit.destroyTarget;

        if (target == null) {
            if (currentTarget == null) {
                return false;
            }

            conduit.destroyTarget = null;
            conduit.destroyTargetUUID = null;
        } else {
            if (currentTarget != null && target.getUniqueId().equals(currentTarget.getUUID())) {
                return false;
            }

            conduit.destroyTarget = ((CraftLivingEntity) target).getHandle();
            conduit.destroyTargetUUID = target.getUniqueId();
        }

        ConduitBlockEntity.updateDestroyTarget(conduit.getLevel(), this.getPosition(), this.data, conduit.effectBlocks, conduit, false);
        return true;
    }

    @Override
    public LivingEntity getTarget() {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        if (conduit == null) {
            return null;
        }

        net.minecraft.world.entity.LivingEntity nmsEntity = conduit.destroyTarget;
        return (nmsEntity != null) ? (LivingEntity) nmsEntity.getBukkitEntity() : null;
    }

    @Override
    public boolean hasTarget() {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getTileEntityFromWorld();
        return conduit != null && conduit.destroyTarget != null && conduit.destroyTarget.isAlive();
    }

    @Override
    public BoundingBox getHuntingArea() {
        AABB bounds = ConduitBlockEntity.getDestroyRangeAABB(this.getPosition());
        return new BoundingBox(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
    }
}
