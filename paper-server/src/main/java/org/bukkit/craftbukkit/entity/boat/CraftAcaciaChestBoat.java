package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.AcaciaChestBoat;

public class CraftAcaciaChestBoat extends CraftChestBoat implements AcaciaChestBoat {

    public CraftAcaciaChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
