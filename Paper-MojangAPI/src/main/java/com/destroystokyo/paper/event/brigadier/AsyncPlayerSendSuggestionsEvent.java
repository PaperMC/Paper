package com.destroystokyo.paper.event.brigadier;

import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when sending {@link Suggestions} to the client. Will be called asynchronously if a plugin
 * marks the {@link com.destroystokyo.paper.event.server.AsyncTabCompleteEvent} event handled asynchronously,
 * otherwise called synchronously.
 */
public class AsyncPlayerSendSuggestionsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Suggestions suggestions;
    private final String buffer;

    public AsyncPlayerSendSuggestionsEvent(Player player, Suggestions suggestions, String buffer) {
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
        return buffer;
    }

    /**
     * Gets the suggestions to be sent to client.
     *
     * @return the suggestions
     */
    public Suggestions getSuggestions() {
        return suggestions;
    }

    /**
     * Sets the suggestions to be sent to client.
     *
     * @param suggestions suggestions
     */
    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels sending suggestions to the client.
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
