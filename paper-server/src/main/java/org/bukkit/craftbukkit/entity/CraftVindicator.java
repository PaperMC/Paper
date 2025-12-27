package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vindicator;

public class CraftVindicator extends CraftIllager implements Vindicator {

    public CraftVindicator(CraftServer server, net.minecraft.world.entity.monster.illager.Vindicator entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.illager.Vindicator getHandle() {
        return (net.minecraft.world.entity.monster.illager.Vindicator) this.entity;
    }

    @Override
    public boolean isJohnny() {
        return this.getHandle().isJohnny;
    }

    @Override
    public void setJohnny(boolean johnny) {
        this.getHandle().isJohnny = johnny;
    }
}
