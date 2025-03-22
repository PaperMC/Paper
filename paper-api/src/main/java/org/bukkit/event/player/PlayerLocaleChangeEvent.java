package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes their locale in the client settings.
 */
public class PlayerLocaleChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final String locale;
    private final java.util.Locale adventure$locale;

    public PlayerLocaleChangeEvent(@NotNull Player player, @NotNull String locale) {
        super(player);
        this.locale = locale;
        this.adventure$locale = java.util.Objects.requireNonNullElse(net.kyori.adventure.translation.Translator.parseLocale(locale), java.util.Locale.US); // Paper start
    }

    /**
     * @return the player's new locale
     * @see Player#getLocale()
     * @deprecated in favour of {@link #locale()}
     */
    @NotNull
    @Deprecated // Paper
    public String getLocale() {
        return locale;
    }

    /**
     * @see Player#locale()
     *
     * @return the player's new locale
     */
    public @NotNull java.util.Locale locale() {
        return this.adventure$locale;
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
