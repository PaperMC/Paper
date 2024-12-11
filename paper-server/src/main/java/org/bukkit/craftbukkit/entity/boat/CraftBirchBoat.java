package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.entity.boat.BirchBoat;

public class CraftBirchBoat extends CraftBoat implements BirchBoat {

    public CraftBirchBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }
}
