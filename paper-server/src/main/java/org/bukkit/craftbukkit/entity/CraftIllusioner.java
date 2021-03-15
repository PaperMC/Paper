package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityIllagerIllusioner;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;

public class CraftIllusioner extends CraftSpellcaster implements Illusioner {

    public CraftIllusioner(CraftServer server, EntityIllagerIllusioner entity) {
        super(server, entity);
    }

    @Override
    public EntityIllagerIllusioner getHandle() {
        return (EntityIllagerIllusioner) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllusioner";
    }

    @Override
    public EntityType getType() {
        return EntityType.ILLUSIONER;
    }
}
