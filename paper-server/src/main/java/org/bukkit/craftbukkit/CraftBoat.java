package org.bukkit.craftbukkit;

import net.minecraft.server.EntityBoat;
import org.bukkit.entity.Boat;

/**
 * A minecart.
 * 
 * @author sk89q
 */
public class CraftBoat extends CraftVehicle implements Boat {
    protected EntityBoat boat;

    public CraftBoat(CraftServer server, EntityBoat entity) {
        super(server, entity);
        boat = entity;
    }
}
