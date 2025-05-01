package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, net.minecraft.world.entity.npc.WanderingTrader entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.WanderingTrader getHandle() {
        return (net.minecraft.world.entity.npc.WanderingTrader) this.entity;
    }

    @Override
    public int getDespawnDelay() {
        return this.getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        this.getHandle().setDespawnDelay(despawnDelay);
    }

    @Override
    public void setCanDrinkPotion(boolean bool) {
        getHandle().canDrinkPotion = bool;
    }

    @Override
    public boolean canDrinkPotion() {
        return getHandle().canDrinkPotion;
    }

    @Override
    public void setCanDrinkMilk(boolean bool) {
        getHandle().canDrinkMilk = bool;
    }

    @Override
    public boolean canDrinkMilk() {
        return getHandle().canDrinkMilk;
    }

    @Override
    public org.bukkit.Location getWanderingTowards() {
        net.minecraft.core.BlockPos pos = this.getHandle().getWanderTarget();
        if (pos == null) {
            return null;
        }

        return CraftLocation.toBukkit(pos, this.getHandle().level());
    }

    @Override
    public void setWanderingTowards(org.bukkit.Location location) {
        net.minecraft.core.BlockPos pos = null;
        if (location != null) {
            pos = CraftLocation.toBlockPosition(location);
        }

        this.getHandle().setWanderTarget(pos);
    }
}
