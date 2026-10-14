package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractCow;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class CraftAbstractCow extends CraftAnimals implements AbstractCow {

    public CraftAbstractCow(CraftServer server, net.minecraft.world.entity.animal.cow.AbstractCow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.cow.AbstractCow getHandle() {
        return (net.minecraft.world.entity.animal.cow.AbstractCow) this.entity;
    }
}
