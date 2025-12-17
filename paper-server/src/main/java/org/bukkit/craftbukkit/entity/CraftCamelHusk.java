package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import org.bukkit.craftbukkit.CraftServer;

public class CraftCamelHusk extends CraftCamel implements org.bukkit.entity.CamelHusk {
    public CraftCamelHusk(final CraftServer server, final CamelHusk entity) {
        super(server, entity);
    }

    @Override
    public CamelHusk getHandle() {
        return (CamelHusk) this.entity;
    }
}
