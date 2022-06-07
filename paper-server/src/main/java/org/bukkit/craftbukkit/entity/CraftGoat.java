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
    public boolean hasLeftHorn() {
        return getHandle().hasLeftHorn();
    }

    @Override
    public void setLeftHorn(boolean hasHorn) {
        getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_LEFT_HORN, hasHorn);
    }

    @Override
    public boolean hasRightHorn() {
        return getHandle().hasRightHorn();
    }

    @Override
    public void setRightHorn(boolean hasHorn) {
        getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_RIGHT_HORN, hasHorn);
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
