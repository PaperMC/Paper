package org.bukkit.craftbukkit.entity.boat;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.boat.JungleChestBoat;

public class CraftJungleChestBoat extends CraftChestBoat implements JungleChestBoat {

    public CraftJungleChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
    }
}
