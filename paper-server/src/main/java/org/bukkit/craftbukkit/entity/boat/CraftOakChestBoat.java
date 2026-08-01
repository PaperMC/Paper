package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.OakChestBoat;

public class CraftOakChestBoat extends CraftChestBoat implements OakChestBoat {

    public CraftOakChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
