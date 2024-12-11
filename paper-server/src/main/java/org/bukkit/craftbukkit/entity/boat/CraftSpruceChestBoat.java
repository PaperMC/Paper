package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.SpruceChestBoat;

public class CraftSpruceChestBoat extends CraftChestBoat implements SpruceChestBoat {

    public CraftSpruceChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
