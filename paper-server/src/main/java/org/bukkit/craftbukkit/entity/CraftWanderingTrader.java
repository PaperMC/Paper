package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
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
    public String toString() {
        return "CraftWanderingTrader";
    }

    @Override
    public int getDespawnDelay() {
        return this.getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        this.getHandle().setDespawnDelay(despawnDelay);
    }

    // Paper start - Add more WanderingTrader API
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
    // Paper end
}
