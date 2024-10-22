package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.SpruceBoat;

public class CraftSpruceBoat extends CraftBoat implements SpruceBoat {

    public CraftSpruceBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
