package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEvoker;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;

public class CraftEvoker extends CraftMonster implements Evoker {

    public CraftEvoker(CraftServer server, EntityEvoker entity) {
        super(server, entity);
    }

    @Override
    public EntityEvoker getHandle() {
        return (EntityEvoker) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvoker";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER;
    }

    @Override
    public Spell getCurrentSpell() {
        return Spell.values()[getHandle().getSpell()];
    }

    @Override
    public void setCurrentSpell(Spell spell) {
        getHandle().a(spell == null ? 0 : spell.ordinal());
    }
}
