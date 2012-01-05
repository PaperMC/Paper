package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftCreature implements Animals {

    public CraftAnimals(CraftServer server, EntityAnimal entity) {
        super(server, entity);
    }

    public int getAge() {
        return getHandle().getAge();
    }

    public void setAge(int age) {
        getHandle().setAge(age);
    }

    @Override
    public EntityAnimal getHandle() {
        return (EntityAnimal) entity;
    }

    public void setAgeLock(boolean lock) {
        getHandle().ageLocked = lock;
    }

    public boolean getAgeLock() {
        return getHandle().ageLocked;
    }

    public void setBaby() {
        if (isAdult()) {
            setAge(-24000);
        }
    }

    public void setAdult() {
        if (!isAdult()) {
            setAge(0);
        }
    }

    public boolean isAdult() {
        return getAge() >= 0;
    }

    public boolean canBreed() {
        return getAge() == 0;
    }

    public void setBreed(boolean breed) {
        if (breed) {
            setAge(0);
        } else if (isAdult()) {
            setAge(6000);
        }
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }
}
