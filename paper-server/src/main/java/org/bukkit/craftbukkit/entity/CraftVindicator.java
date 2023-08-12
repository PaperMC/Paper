package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityVindicator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vindicator;

public class CraftVindicator extends CraftIllager implements Vindicator {

    public CraftVindicator(CraftServer server, EntityVindicator entity) {
        super(server, entity);
    }

    @Override
    public EntityVindicator getHandle() {
        return (EntityVindicator) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVindicator";
    }

    @Override
    public boolean isJohnny() {
        return getHandle().isJohnny;
    }

    @Override
    public void setJohnny(boolean johnny) {
        getHandle().isJohnny = johnny;
    }
}
