package com.destroystokyo.paper.event.brigadier;

import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when sending {@link Suggestions} to the client. Will be called asynchronously if a plugin
 * marks the {@link com.destroystokyo.paper.event.server.AsyncTabCompleteEvent} event handled asynchronously,
 * otherwise called synchronously.
 */
@NullMarked
public class AsyncPlayerSendSuggestionsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Suggestions suggestions;
    private final String buffer;

    private boolean cancelled;

    @ApiStatus.Internal
    public AsyncPlayerSendSuggestionsEvent(final Player player, final Suggestions suggestions, final String buffer) {
        super(player, !Bukkit.isPrimaryThread());
        this.suggestions = suggestions;
        this.buffer = buffer;
    }

    /**
     * Gets the input buffer sent to request these suggestions.
     *
     * @return the input buffer
     */
    public String getBuffer() {
        return this.buffer;
    }

    /**
     * Gets the suggestions to be sent to client.
     *
     * @return the suggestions
     */
    public Suggestions getSuggestions() {
        return this.suggestions;
    }

    /**
     * Sets the suggestions to be sent to client.
     *
     * @param suggestions suggestions
     */
    public void setSuggestions(final Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels sending suggestions to the client.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
