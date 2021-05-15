package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.decoration.EntityPainting;
import net.minecraft.world.entity.decoration.Paintings;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    @Override
    public Art getArt() {
        Paintings art = getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }

    @Override
    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    @Override
    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = this.getHandle();
        Paintings oldArt = painting.art;
        painting.art = CraftArt.BukkitToNotch(art);
        painting.setDirection(painting.getDirection());
        if (!force && !painting.survives()) {
            // Revert painting since it doesn't fit
            painting.art = oldArt;
            painting.setDirection(painting.getDirection());
            return false;
        }
        this.update();
        return true;
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
