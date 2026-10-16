package org.bukkit.event.entity;

import org.bukkit.entity.Spellcaster;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Spellcaster} casts a spell.
 */
public class EntitySpellCastEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Spellcaster.Spell spell;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntitySpellCastEvent(@NotNull Spellcaster spellcaster, @NotNull Spellcaster.Spell spell) {
        super(spellcaster);
        this.spell = spell;
    }

    @Override
    @NotNull
    public Spellcaster getEntity() {
        return (Spellcaster) this.entity;
    }

    /**
     * Get the spell to be cast in this event.
     * <br>
     * This is a convenience method equivalent to
     * {@link Spellcaster#getSpell()}.
     *
     * @return the spell to cast
     */
    @NotNull
    public Spellcaster.Spell getSpell() {
        return this.spell;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
