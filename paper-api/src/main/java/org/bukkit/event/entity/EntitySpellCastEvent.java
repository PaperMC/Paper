package org.bukkit.event.entity;

import org.bukkit.entity.Spellcaster;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link Spellcaster} casts a spell.
 */
public interface EntitySpellCastEvent extends EntityEvent, Cancellable {

    @Override
    Spellcaster getEntity();

    /**
     * Get the spell to be cast in this event.
     * <br>
     * This is a convenience method equivalent to
     * {@link Spellcaster#getSpell()}.
     *
     * @return the spell to cast
     */
    Spellcaster.Spell getSpell();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
