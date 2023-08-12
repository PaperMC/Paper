package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.npc.EntityVillagerTrader;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, EntityVillagerTrader entity) {
        super(server, entity);
    }

    @Override
    public EntityVillagerTrader getHandle() {
        return (EntityVillagerTrader) entity;
    }

    @Override
    public String toString() {
        return "CraftWanderingTrader";
    }

    @Override
    public int getDespawnDelay() {
        return getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        getHandle().setDespawnDelay(despawnDelay);
    }
}
