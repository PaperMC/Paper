package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityReference;
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

    public CraftConduit(World world, ConduitBlockEntity blockEntity) {
        super(world, blockEntity);
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
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        return conduit != null && conduit.isActive();
    }

    @Override
    public boolean isHunting() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        return conduit != null && conduit.isHunting();
    }

    @Override
    public Collection<Block> getFrameBlocks() {
        this.ensureNoWorldGeneration();
        Collection<Block> blocks = new ArrayList<>();

        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
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
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        return (conduit != null) ? conduit.effectBlocks.size() : 0;
    }

    @Override
    public int getRange() {
        this.ensureNoWorldGeneration();
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        return (conduit != null) ? ConduitBlockEntity.getRange(conduit.effectBlocks) : 0;
    }

    @Override
    public boolean setTarget(LivingEntity target) {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        if (conduit == null) {
            return false;
        }

        EntityReference<net.minecraft.world.entity.LivingEntity> currentTarget = conduit.destroyTarget;

        if (target == null) {
            if (currentTarget == null) {
                return false;
            }

            conduit.destroyTarget = null;
        } else {
            if (currentTarget != null && target.getUniqueId().equals(currentTarget.getUUID())) {
                return false;
            }

            conduit.destroyTarget = new EntityReference<>(((CraftLivingEntity) target).getHandle());
        }

        ConduitBlockEntity.updateAndAttackTarget(
            conduit.getLevel().getMinecraftWorld(),
            this.getPosition(),
            this.data,
            conduit,
            conduit.effectBlocks.size() >= ConduitBlockEntity.MIN_KILL_SIZE,
            false
        );
        return true;
    }

    @Override
    public LivingEntity getTarget() {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        if (conduit == null) {
            return null;
        }

        final net.minecraft.world.entity.LivingEntity nmsEntity = EntityReference.get(conduit.destroyTarget, this.getWorldHandle().getMinecraftWorld(), net.minecraft.world.entity.LivingEntity.class);
        return nmsEntity == null ? null : nmsEntity.getBukkitLivingEntity();
    }

    @Override
    public boolean hasTarget() {
        ConduitBlockEntity conduit = (ConduitBlockEntity) this.getBlockEntityFromWorld();
        if (conduit == null) return false;

        final net.minecraft.world.entity.LivingEntity destroyTarget = EntityReference.get(conduit.destroyTarget, this.getWorldHandle().getMinecraftWorld(), net.minecraft.world.entity.LivingEntity.class);
        return destroyTarget != null && destroyTarget.isAlive();
    }

    @Override
    public BoundingBox getHuntingArea() {
        AABB bounds = ConduitBlockEntity.getDestroyRangeAABB(this.getPosition());
        return new BoundingBox(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
    }
}
