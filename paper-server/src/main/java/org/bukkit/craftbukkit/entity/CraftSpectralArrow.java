package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.EntitySpectralArrow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SpectralArrow;

public class CraftSpectralArrow extends CraftAbstractArrow implements SpectralArrow {

    public CraftSpectralArrow(CraftServer server, EntitySpectralArrow entity) {
        super(server, entity);
    }

    @Override
    public EntitySpectralArrow getHandle() {
        return (EntitySpectralArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftSpectralArrow";
    }

    @Override
    public int getGlowingTicks() {
        return getHandle().duration;
    }

    @Override
    public void setGlowingTicks(int duration) {
        getHandle().duration = duration;
    }
}
