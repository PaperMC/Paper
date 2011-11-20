package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }
}
