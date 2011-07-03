package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPigZombie;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, EntityPigZombie entity) {
        super(server, entity);
    }

    @Override
    public EntityPigZombie getHandle() {
        return (EntityPigZombie) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    public int getAnger() {
        return getHandle().angerLevel;
    }

    public void setAnger(int level) {
        getHandle().angerLevel = level;
    }

    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    public boolean isAngry() {
        return getAnger() > 0;
    }

}
