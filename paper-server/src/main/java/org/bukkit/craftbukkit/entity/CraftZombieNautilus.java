package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import org.bukkit.craftbukkit.CraftServer;

public class CraftZombieNautilus extends CraftAbstractNautilus implements org.bukkit.entity.ZombieNautilus {
    public CraftZombieNautilus(final CraftServer server, final ZombieNautilus entity) {
        super(server, entity);
    }

    @Override
    public ZombieNautilus getHandle() {
        return (ZombieNautilus) this.entity;
    }
}
