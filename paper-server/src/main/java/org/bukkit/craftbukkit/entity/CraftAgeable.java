package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ageable;

public class CraftAgeable extends CraftCreature implements Ageable {
    public CraftAgeable(CraftServer server, AgeableMob entity) {
        super(server, entity);
    }

    @Override
    public AgeableMob getHandle() {
        return (AgeableMob) this.entity;
    }

    @Override
    public int getAge() {
        return this.getHandle().getAge();
    }

    @Override
    public void setAge(int age) {
        this.getHandle().setAge(age);
    }

    @Override
    public void setAgeLock(boolean lock) {
        this.getHandle().setAgeLocked(lock);
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().isAgeLocked();
    }

    @Override
    public void setBaby() {
        setBaby(this.getHandle(), true);
    }

    @Override
    public void setAdult() {
        setBaby(this.getHandle(), false);
    }

    public static void setBaby(Mob mob, boolean baby) {
        if (baby != mob.isBaby()) {
            mob.setBaby(baby);
        }
    }

    @Override
    public boolean isAdult() {
        return !this.getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return this.getAge() == 0;
    }

    @Override
    public void setBreed(boolean breed) {
        if (breed) {
            this.setAge(0);
        } else if (this.isAdult()) {
            this.setAge(6000);
        }
    }
}
