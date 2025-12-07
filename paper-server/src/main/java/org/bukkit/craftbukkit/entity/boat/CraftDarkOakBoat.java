package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.DarkOakBoat;

public class CraftDarkOakBoat extends CraftBoat implements DarkOakBoat {

    public CraftDarkOakBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
