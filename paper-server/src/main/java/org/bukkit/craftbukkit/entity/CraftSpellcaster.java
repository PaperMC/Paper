package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Spellcaster;

public class CraftSpellcaster extends CraftIllager implements Spellcaster {

    public CraftSpellcaster(CraftServer server, SpellcasterIllager entity) {
        super(server, entity);
    }

    @Override
    public SpellcasterIllager getHandle() {
        return (SpellcasterIllager) this.entity;
    }

    @Override
    public Spell getSpell() {
        return CraftSpellcaster.toBukkitSpell(this.getHandle().getCurrentSpell());
    }

    @Override
    public void setSpell(Spell spell) {
        Preconditions.checkArgument(spell != null, "Use Spell.NONE");

        this.getHandle().setIsCastingSpell(CraftSpellcaster.toNMSSpell(spell));
    }

    public static Spell toBukkitSpell(SpellcasterIllager.IllagerSpell spell) {
        return Spell.valueOf(spell.name());
    }

    public static SpellcasterIllager.IllagerSpell toNMSSpell(Spell spell) {
        return SpellcasterIllager.IllagerSpell.byId(spell.ordinal());
    }
}
