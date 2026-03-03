package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Raft;

public class CraftRaft extends CraftBoat implements Raft {

    public CraftRaft(final CraftServer server, final net.minecraft.world.entity.vehicle.boat.Raft entity) {
        super(server, entity);
    }
}
