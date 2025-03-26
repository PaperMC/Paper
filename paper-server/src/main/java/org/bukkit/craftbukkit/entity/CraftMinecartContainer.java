package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.CraftServer;

public abstract class CraftMinecartContainer extends CraftMinecart implements com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper

    public CraftMinecartContainer(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public AbstractMinecartContainer getHandle() {
        return (AbstractMinecartContainer) this.entity;
    }
}
