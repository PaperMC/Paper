package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import org.bukkit.craftbukkit.CraftServer;

public class CraftAbstractNautilus extends CraftAnimals implements org.bukkit.entity.AbstractNautilus {
    public CraftAbstractNautilus(final CraftServer server, final AbstractNautilus entity) {
        super(server, entity);
    }

    @Override
    public AbstractNautilus getHandle() {
        return (AbstractNautilus) this.entity;
    }
}
