package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityHanging;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, EntityHanging entity) {
        super(server, entity);
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        Block block = getLocation().getBlock().getRelative(getAttachedFace()).getRelative(face.getOppositeFace()).getRelative(getFacing());
        EntityHanging hanging = getHandle();
        int x = hanging.x, y = hanging.y, z = hanging.z, dir = hanging.direction;
        hanging.x = block.getX();
        hanging.y = block.getY();
        hanging.z = block.getZ();
        switch (face) {
            case EAST:
            default:
                getHandle().setDirection(0);
                break;
            case NORTH:
                getHandle().setDirection(1);
                break;
            case WEST:
                getHandle().setDirection(2);
                break;
            case SOUTH:
                getHandle().setDirection(3);
                break;
        }
        if (!force && !hanging.survives()) {
            // Revert since it doesn't fit
            hanging.x = x;
            hanging.y = y;
            hanging.z = z;
            hanging.setDirection(dir);
            return false;
        }
        return true;
    }

    public BlockFace getFacing() {
        switch (this.getHandle().direction) {
            case 0:
            default:
                return BlockFace.EAST;
            case 1:
                return BlockFace.NORTH;
            case 2:
                return BlockFace.WEST;
            case 3:
                return BlockFace.SOUTH;
        }
    }

    @Override
    public EntityHanging getHandle() {
        return (EntityHanging) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
