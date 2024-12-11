package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.AcaciaBoat;

public class CraftAcaciaBoat extends CraftBoat implements AcaciaBoat {

    public CraftAcaciaBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
