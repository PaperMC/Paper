package org.bukkit.event.player;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player changes their locale in the client settings.
 */
public interface PlayerLocaleChangeEvent extends PlayerEvent {

    /**
     * @return the player's new locale
     * @see Player#getLocale()
     * @deprecated in favour of {@link #locale()}
     */
    @Deprecated
    String getLocale();

    /**
     * @see Player#locale()
     * @return the player's new locale
     */
    Locale locale();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
