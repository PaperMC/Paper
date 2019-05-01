package org.bukkit.craftbukkit.entity;

import java.util.Locale;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.IRegistry;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class CraftVillager extends CraftAbstractVillager implements Villager {

    public CraftVillager(CraftServer server, EntityVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityVillager getHandle() {
        return (EntityVillager) entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    @Override
    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    @Override
    public Profession getProfession() {
        return Profession.valueOf(IRegistry.VILLAGER_PROFESSION.getKey(getHandle().getVillagerData().getProfession()).getKey().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        getHandle().setVillagerData(getHandle().getVillagerData().withProfession(IRegistry.VILLAGER_PROFESSION.get(CraftNamespacedKey.toMinecraft(profession.getKey()))));
    }

    @Override
    public Type getVillagerType() {
        return Type.valueOf(IRegistry.VILLAGER_TYPE.getKey(getHandle().getVillagerData().getType()).getKey().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerType(Type type) {
        Validate.notNull(type);
        getHandle().setVillagerData(getHandle().getVillagerData().withType(IRegistry.VILLAGER_TYPE.get(CraftNamespacedKey.toMinecraft(type.getKey()))));
    }
}
