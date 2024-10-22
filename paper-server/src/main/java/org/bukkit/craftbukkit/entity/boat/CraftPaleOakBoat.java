package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.PaleOakBoat;

public class CraftPaleOakBoat extends CraftBoat implements PaleOakBoat {

    public CraftPaleOakBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
