package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPainting;
import net.minecraft.server.EnumArt;
import net.minecraft.server.Packet25EntityPainting;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.WorldServer;

import org.bukkit.Art;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftEntity implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPainting[art=" + getArt() + "]";
    }

    public Art getArt() {
        EnumArt art = getHandle().e;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = getHandle();
        EnumArt oldArt = painting.e;
        EnumArt newArt = CraftArt.BukkitToNotch(art);
        painting.e = newArt;
        painting.b(painting.a);
        if(!force && !painting.i()) {
            // Revert painting since it doesn't fit
            painting.e = oldArt;
            painting.b(painting.a);
            return false;
        }
        this.update();
        return true;
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        Block block = getLocation().getBlock().getRelative(getAttachedFace()).getRelative(face.getOppositeFace()).getRelative(getFacing());
        EntityPainting painting = getHandle();
        int x = painting.b, y = painting.c, z = painting.d, dir = painting.a;
        painting.b = block.getX();
        painting.c = block.getY();
        painting.d = block.getZ();
        switch(face) {
        case EAST:
        default:
            getHandle().b(0);
            break;
        case NORTH:
            getHandle().b(1);
            break;
        case WEST:
            getHandle().b(2);
            break;
        case SOUTH:
            getHandle().b(3);
            break;
        }
        if(!force && !painting.i()) {
            // Revert painting since it doesn't fit
            painting.b = x;
            painting.c = y;
            painting.d = z;
            painting.b(dir);
            return false;
        }
        this.update();
        return true;
    }

    public BlockFace getFacing() {
        switch(getHandle().a) {
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

    private void update() {
        WorldServer world = ((CraftWorld)getWorld()).getHandle();
        EntityPainting painting = new EntityPainting(world);
        painting.b = getHandle().b;
        painting.c = getHandle().c;
        painting.d = getHandle().d;
        painting.e = getHandle().e;
        painting.b(getHandle().a);
        getHandle().die();
        getHandle().velocityChanged = true; // because this occurs when the painting is broken, so it might be important
        world.addEntity(painting);
        this.entity = painting;
    }
}
