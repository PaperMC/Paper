package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.BambooRaft;

public class CraftBambooRaft extends CraftBoat implements BambooRaft {

    public CraftBambooRaft(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
