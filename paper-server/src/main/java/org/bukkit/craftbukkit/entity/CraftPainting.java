package org.bukkit.craftbukkit.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, net.minecraft.world.entity.decoration.Painting entity) {
        super(server, entity);
    }

    @Override
    public Art getArt() {
        return CraftArt.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public boolean setArt(Art art) {
        return this.setArt(art, false);
    }

    @Override
    public boolean setArt(Art art, boolean force) {
        net.minecraft.world.entity.decoration.Painting painting = this.getHandle();
        Holder<PaintingVariant> oldArt = painting.getVariant();
        painting.setVariant(CraftArt.bukkitToMinecraftHolder(art));
        painting.setDirection(painting.getDirection());
        if (!force && !this.getHandle().generation && !painting.survives()) {
            // Revert painting since it doesn't fit
            painting.setVariant(oldArt);
            painting.setDirection(painting.getDirection());
            return false;
        }
        //this.update(); Paper - Don't resent entity on art update
        return true;
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            this.update();
            return true;
        }

        return false;
    }

    @Override
    public net.minecraft.world.entity.decoration.Painting getHandle() {
        return (net.minecraft.world.entity.decoration.Painting) this.entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + this.getArt() + "}";
    }
}
