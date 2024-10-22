package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.MangroveChestBoat;

public class CraftMangroveChestBoat extends CraftChestBoat implements MangroveChestBoat {

    public CraftMangroveChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
