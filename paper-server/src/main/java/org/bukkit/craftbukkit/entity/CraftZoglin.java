package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Zoglin;

public class CraftZoglin extends CraftMonster implements Zoglin {

    public CraftZoglin(CraftServer server, net.minecraft.world.entity.monster.Zoglin entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Zoglin getHandle() {
        return (net.minecraft.world.entity.monster.Zoglin) this.entity;
    }

    @Override
    public boolean isBaby() {
        return this.getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        CraftAgeable.setBaby(this.getHandle(), baby);
    }

    @Override
    public int getAge() {
        return this.getHandle().isBaby() ? -1 : 0;
    }

    @Override
    public void setAge(int age) {
        this.getHandle().setBaby(age < 0);
    }

    @Override
    public void setAgeLock(boolean lock) {
    }

    @Override
    public boolean getAgeLock() {
        return false;
    }

    @Override
    public void setBaby() {
        CraftAgeable.setBaby(this.getHandle(), true);
    }

    @Override
    public void setAdult() {
        CraftAgeable.setBaby(this.getHandle(), false);
    }

    @Override
    public boolean isAdult() {
        return !this.getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(boolean b) {
    }
}
