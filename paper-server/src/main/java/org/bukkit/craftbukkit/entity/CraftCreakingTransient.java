package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.creaking.CreakingTransient;
import org.bukkit.craftbukkit.CraftServer;

public class CraftCreakingTransient extends CraftCreaking implements org.bukkit.entity.CreakingTransient {

    public CraftCreakingTransient(CraftServer server, CreakingTransient entity) {
        super(server, entity);
    }

    @Override
    public CreakingTransient getHandle() {
        return (CreakingTransient) entity;
    }

    @Override
    public String toString() {
        return "CraftCreakingTransient";
    }
}
