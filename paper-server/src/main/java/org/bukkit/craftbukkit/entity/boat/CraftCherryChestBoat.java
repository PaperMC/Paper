package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.CherryChestBoat;

public class CraftCherryChestBoat extends CraftChestBoat implements CherryChestBoat {

    public CraftCherryChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
