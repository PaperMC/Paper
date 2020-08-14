package org.bukkit.event.entity;

import org.bukkit.entity.Spellcaster;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Spellcaster} casts a spell.
 */
public class EntitySpellCastEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    //
    private boolean cancelled = false;
    private final Spellcaster.Spell spell;

    public EntitySpellCastEvent(@NotNull Spellcaster what, @NotNull Spellcaster.Spell spell) {
        super(what);
        this.spell = spell;
    }

    @Override
    @NotNull
    public Spellcaster getEntity() {
        return (Spellcaster) entity;
    }

    /**
     * Get the spell to be cast in this event.
     *
     * This is a convenience method equivalent to
     * {@link Spellcaster#getSpell()}.
     *
     * @return the spell to cast
     */
    @NotNull
    public Spellcaster.Spell getSpell() {
        return spell;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
