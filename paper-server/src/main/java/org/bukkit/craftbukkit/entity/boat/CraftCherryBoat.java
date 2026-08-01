package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.CherryBoat;

public class CraftCherryBoat extends CraftBoat implements CherryBoat {

    public CraftCherryBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
