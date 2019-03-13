package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes their locale in the client settings.
 */
public class PlayerLocaleChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final String locale;

    public PlayerLocaleChangeEvent(@NotNull Player who, @NotNull String locale) {
        super(who);
        this.locale = locale;
    }

    /**
     * @see Player#getLocale()
     *
     * @return the player's new locale
     */
    @NotNull
    public String getLocale() {
        return locale;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
