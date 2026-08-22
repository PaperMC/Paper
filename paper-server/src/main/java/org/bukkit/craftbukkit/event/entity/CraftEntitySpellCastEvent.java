package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Spellcaster;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntitySpellCastEvent;

public class CraftEntitySpellCastEvent extends CraftEntityEvent implements EntitySpellCastEvent {

    private final Spellcaster.Spell spell;
    private boolean cancelled;

    public CraftEntitySpellCastEvent(final Spellcaster spellcaster, final Spellcaster.Spell spell) {
        super(spellcaster);
        this.spell = spell;
    }

    @Override
    public Spellcaster getEntity() {
        return (Spellcaster) this.entity;
    }

    @Override
    public Spellcaster.Spell getSpell() {
        return this.spell;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntitySpellCastEvent.getHandlerList();
    }
}
