package com.destroystokyo.paper.event.brigadier;

import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when sending {@link Suggestions} to the client. Will be called asynchronously if a plugin
 * marks the {@link com.destroystokyo.paper.event.server.AsyncTabCompleteEvent} event handled asynchronously,
 * otherwise called synchronously.
 */
@NullMarked
public interface AsyncPlayerSendSuggestionsEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the input buffer sent to request these suggestions.
     *
     * @return the input buffer
     */
    String getBuffer();

    /**
     * Gets the suggestions to be sent to client.
     *
     * @return the suggestions
     */
    Suggestions getSuggestions();

    /**
     * Sets the suggestions to be sent to client.
     *
     * @param suggestions suggestions
     */
    void setSuggestions(Suggestions suggestions);

    /**
     * Cancels sending suggestions to the client.
     * <p>
     * {@inheritDoc}
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
