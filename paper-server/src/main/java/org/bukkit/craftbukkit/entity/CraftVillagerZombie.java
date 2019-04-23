package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.server.EntityZombieVillager;
import net.minecraft.server.IRegistry;
import net.minecraft.server.MinecraftKey;
import org.apache.commons.lang.Validate;
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
        return Villager.Profession.valueOf(IRegistry.VILLAGER_PROFESSION.getKey(getHandle().getVillagerData().getProfession()).getKey().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        Validate.notNull(profession);
        getHandle().setVillagerData(getHandle().getVillagerData().withProfession(IRegistry.VILLAGER_PROFESSION.get(new MinecraftKey(profession.name().toLowerCase(Locale.ROOT)))));
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");

        return getHandle().conversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().conversionTime = -1;
            getHandle().getDataWatcher().set(EntityZombieVillager.CONVERTING, false);
            getHandle().persistent = false; // CraftBukkit - SPIGOT-4684 update persistence
        } else {
            getHandle().startConversion((UUID) null, time);
        }
    }
}
