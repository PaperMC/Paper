package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEvoker;
import net.minecraft.server.EntityIllagerWizard;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;

public class CraftEvoker extends CraftSpellcaster implements Evoker {

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
    public Evoker.Spell getCurrentSpell() {
        return Evoker.Spell.values()[getHandle().getSpell().ordinal()];
    }

    @Override
    public void setCurrentSpell(Evoker.Spell spell) {
        getHandle().setSpell(spell == null ? EntityIllagerWizard.Spell.NONE : EntityIllagerWizard.Spell.a(spell.ordinal()));
    }

    // Paper start
    @Override
    public org.bukkit.entity.Sheep getWololoTarget() {
        net.minecraft.server.EntitySheep sheep = getHandle().getWololoTarget();
        return sheep == null ? null : (org.bukkit.entity.Sheep) sheep.getBukkitEntity();
    }

    @Override
    public void setWololoTarget(org.bukkit.entity.Sheep sheep) {
        getHandle().setWololoTarget(sheep == null ? null : ((org.bukkit.craftbukkit.entity.CraftSheep) sheep).getHandle());
    }
    // Paper end
}
