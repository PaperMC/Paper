package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
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
    public String toString() {
        return "CraftGoat";
    }

    @Override
    public boolean hasLeftHorn() {
        return this.getHandle().hasLeftHorn();
    }

    @Override
    public void setLeftHorn(boolean hasHorn) {
        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_LEFT_HORN, hasHorn);
    }

    @Override
    public boolean hasRightHorn() {
        return this.getHandle().hasRightHorn();
    }

    @Override
    public void setRightHorn(boolean hasHorn) {
        this.getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_RIGHT_HORN, hasHorn);
    }

    @Override
    public boolean isScreaming() {
        return this.getHandle().isScreamingGoat();
    }

    @Override
    public void setScreaming(boolean screaming) {
        this.getHandle().setScreamingGoat(screaming);
    }

    // Paper start - Goat ram API
    @Override
    public void ram(@org.jetbrains.annotations.NotNull org.bukkit.entity.LivingEntity entity) {
        this.getHandle().ram(((CraftLivingEntity) entity).getHandle());
    }
    // Paper end
}
