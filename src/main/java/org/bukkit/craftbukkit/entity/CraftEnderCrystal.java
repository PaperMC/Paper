package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEnderCrystal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;

public class CraftEnderCrystal extends CraftEntity implements EnderSignal {
    public CraftEnderCrystal(CraftServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }
}
