package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityZombie;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, EntityZombie entity) {
        super(server, entity);
    }

    @Override
    public EntityZombie getHandle() {
        return (EntityZombie) entity;
    }

    @Override
    public String toString() {
        return "CraftZombie";
    }

    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    public boolean isBaby() {
        return getHandle().isBaby();
    }

    public void setBaby(boolean flag) {
        getHandle().setBaby(flag);
    }

    public boolean isVillager() {
        return getHandle().isVillager();
    }

    public void setVillager(boolean flag) {
        if (flag) {
            getHandle().setVillagerType(0);
        } else {
            getHandle().clearVillagerType();
        }
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        if (profession == null) {
            getHandle().clearVillagerType();
        } else {
            getHandle().setVillagerType(profession.getId());
        }
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        if (!isVillager()) return null;
        return Villager.Profession.getProfession(getHandle().getVillagerType());
    }
}
