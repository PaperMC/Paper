package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent;
import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperAsyncPlayerSendSuggestionsEvent extends CraftPlayerEvent implements AsyncPlayerSendSuggestionsEvent {

    private Suggestions suggestions;
    private final String buffer;

    private boolean cancelled;

    public PaperAsyncPlayerSendSuggestionsEvent(final Player player, final Suggestions suggestions, final String buffer) {
        super(!Bukkit.isPrimaryThread(), player);
        this.suggestions = suggestions;
        this.buffer = buffer;
    }

    @Override
    public String getBuffer() {
        return this.buffer;
    }

    @Override
    public Suggestions getSuggestions() {
        return this.suggestions;
    }

    @Override
    public void setSuggestions(final Suggestions suggestions) {
        this.suggestions = suggestions;
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
        return AsyncPlayerSendSuggestionsEvent.getHandlerList();
    }
}
