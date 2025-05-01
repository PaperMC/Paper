package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.Minecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {

    public CraftMinecartRideable(CraftServer server, Minecart entity) {
        super(server, entity);
    }
}
