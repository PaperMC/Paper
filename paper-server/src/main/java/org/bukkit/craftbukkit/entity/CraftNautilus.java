package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.nautilus.Nautilus;
import org.bukkit.craftbukkit.CraftServer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftNautilus extends CraftAbstractNautilus implements org.bukkit.entity.Nautilus {
    public CraftNautilus(final CraftServer server, final Nautilus entity) {
        super(server, entity);
    }

    @Override
    public Nautilus getHandle() {
        return (Nautilus) this.entity;
    }
}
