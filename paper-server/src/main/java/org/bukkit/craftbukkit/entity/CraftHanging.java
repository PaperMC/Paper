package org.bukkit.craftbukkit.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftBlockAttachedEntity implements Hanging {
    public CraftHanging(CraftServer server, HangingEntity entity) {
        super(server, entity);
    }

    @Override
    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        this.setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        HangingEntity hanging = this.getHandle();
        Direction dir = hanging.getDirection();
        switch (face) {
            case SOUTH:
                this.getHandle().setDirection(Direction.SOUTH);
                break;
            case WEST:
                this.getHandle().setDirection(Direction.WEST);
                break;
            case NORTH:
                this.getHandle().setDirection(Direction.NORTH);
                break;
            case EAST:
                this.getHandle().setDirection(Direction.EAST);
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid facing direction", face));
        }
        if (!force && !this.getHandle().generation && !hanging.survives()) {
            // Revert since it doesn't fit
            hanging.setDirection(dir);
            return false;
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        Direction direction = this.getHandle().getDirection();
        if (direction == null) return BlockFace.SELF;
        return CraftBlock.notchToBlockFace(direction);
    }

    @Override
    public HangingEntity getHandle() {
        return (HangingEntity) this.entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }
}
