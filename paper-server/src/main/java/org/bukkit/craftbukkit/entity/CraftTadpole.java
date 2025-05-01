package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.frog.Tadpole;
import org.bukkit.craftbukkit.CraftServer;

public class CraftTadpole extends CraftFish implements org.bukkit.entity.Tadpole {

    public CraftTadpole(CraftServer server, Tadpole entity) {
        super(server, entity);
    }

    @Override
    public Tadpole getHandle() {
        return (Tadpole) this.entity;
    }

    @Override
    public int getAge() {
        return this.getHandle().age;
    }

    @Override
    public void setAge(int age) {
        this.getHandle().age = age;
    }

    @Override
    public void setAgeLock(boolean lock) {
        this.getHandle().ageLocked = lock;
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().ageLocked;
    }
}
