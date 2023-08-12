package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.EntityTurtle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Turtle;

public class CraftTurtle extends CraftAnimals implements Turtle {

    public CraftTurtle(CraftServer server, EntityTurtle entity) {
        super(server, entity);
    }

    @Override
    public EntityTurtle getHandle() {
        return (EntityTurtle) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTurtle";
    }

    @Override
    public boolean hasEgg() {
        return getHandle().hasEgg();
    }

    @Override
    public boolean isLayingEgg() {
        return getHandle().isLayingEgg();
    }
}
