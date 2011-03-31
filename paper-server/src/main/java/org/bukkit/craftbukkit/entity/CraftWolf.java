
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWolf;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftAnimals implements Wolf {
    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    public boolean isAngry() {
        return getHandle().x();
    }

    public void setAngry(boolean angry) {
        getHandle().c(angry);
    }

    public boolean isSitting() {
        return getHandle().y();
    }

    public void setSitting(boolean sitting) {
        getHandle().d(sitting);
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

    @Override
    public String toString() {
        return "CraftWolf[anger=" + isAngry() + ",owner=" + getTarget() + "]";
    }
}
