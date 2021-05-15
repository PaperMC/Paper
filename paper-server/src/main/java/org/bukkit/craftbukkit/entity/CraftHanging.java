package org.bukkit.craftbukkit.entity;

import net.minecraft.core.EnumDirection;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.decoration.EntityHanging;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, EntityHanging entity) {
        super(server, entity);
    }

    @Override
    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        EntityHanging hanging = getHandle();
        EnumDirection dir = hanging.getDirection();
        switch (face) {
            case SOUTH:
            default:
                getHandle().setDirection(EnumDirection.SOUTH);
                break;
            case WEST:
                getHandle().setDirection(EnumDirection.WEST);
                break;
            case NORTH:
                getHandle().setDirection(EnumDirection.NORTH);
                break;
            case EAST:
                getHandle().setDirection(EnumDirection.EAST);
                break;
        }
        if (!force && !hanging.survives()) {
            // Revert since it doesn't fit
            hanging.setDirection(dir);
            return false;
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        EnumDirection direction = this.getHandle().getDirection();
        if (direction == null) return BlockFace.SELF;
        return CraftBlock.notchToBlockFace(direction);
    }

    @Override
    public EntityHanging getHandle() {
        return (EntityHanging) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    protected void update() {
        if (!getHandle().isAlive()) {
            return;
        }

        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        PlayerChunkMap.EntityTracker entityTracker = world.getChunkProvider().playerChunkMap.trackedEntities.get(getEntityId());

        if (entityTracker == null) {
            return;
        }

        entityTracker.broadcast(getHandle().P());
    }
}
