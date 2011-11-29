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

    @Override
    public String toString() {
        return "CraftAnimals";
    }
}
