package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityZombieVillager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;

public class CraftVillagerZombie extends CraftZombie implements ZombieVillager {

    public CraftVillagerZombie(CraftServer server, EntityZombieVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityZombieVillager getHandle() {
        return (EntityZombieVillager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVillagerZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return Villager.Profession.values()[getHandle().getProfession() + Villager.Profession.FARMER.ordinal()];
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        getHandle().setProfession(profession == null ? 0 : profession.ordinal() - Villager.Profession.FARMER.ordinal());
    }
}
