package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.DarkOakChestBoat;

public class CraftDarkOakChestBoat extends CraftChestBoat implements DarkOakChestBoat {

    public CraftDarkOakChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
