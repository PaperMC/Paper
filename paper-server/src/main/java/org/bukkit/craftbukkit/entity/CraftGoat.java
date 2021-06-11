package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Goat;

public class CraftGoat extends CraftAnimals implements Goat {

    public CraftGoat(CraftServer server, net.minecraft.world.entity.animal.goat.Goat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.goat.Goat getHandle() {
        return (net.minecraft.world.entity.animal.goat.Goat) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.GOAT;
    }

    @Override
    public String toString() {
        return "CraftGoat";
    }

    @Override
    public boolean isScreaming() {
        return getHandle().isScreamingGoat();
    }

    @Override
    public void setScreaming(boolean screaming) {
        getHandle().setScreamingGoat(screaming);
    }
}
