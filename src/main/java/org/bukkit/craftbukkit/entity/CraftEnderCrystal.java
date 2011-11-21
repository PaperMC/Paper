package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;

public class CraftEnderCrystal extends CraftEntity implements EnderSignal {
    public CraftEnderCrystal(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }
}
