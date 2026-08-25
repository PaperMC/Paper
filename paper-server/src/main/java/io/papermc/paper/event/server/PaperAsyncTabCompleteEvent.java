package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.google.common.base.Preconditions;
import io.papermc.paper.util.TransformingRandomAccessList;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperAsyncTabCompleteEvent extends CraftEvent implements AsyncTabCompleteEvent {

    private final CommandSender sender;
    private final String buffer;
    private final boolean isCommand;
    private final @Nullable Location location;
    private final List<Completion> completions = new ArrayList<>();
    private final List<String> stringCompletions = new TransformingRandomAccessList<>(
        this.completions,
        Completion::suggestion,
        Completion::completion
    );
    private boolean handled;
    private boolean cancelled;

    public PaperAsyncTabCompleteEvent(final CommandSender sender, final String buffer, final boolean isCommand, final @Nullable Location loc) {
        super(true);
        this.sender = sender;
        this.buffer = buffer;
        this.isCommand = isCommand;
        this.location = loc;
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public List<String> getCompletions() {
        return this.stringCompletions;
    }

    @Override
    public void setCompletions(final List<String> completions) {
        Preconditions.checkArgument(completions != null, "Completions list cannot be null");
        if (completions == this.stringCompletions) {
            return;
        }
        this.completions.clear();
        this.completions.addAll(fromStrings(completions));
    }

    @Override
    public List<Completion> completions() {
        return this.completions;
    }

    @Override
    public void completions(final List<Completion> newCompletions) {
        Preconditions.checkArgument(newCompletions != null, "new completions cannot be null");
        this.completions.clear();
        this.completions.addAll(newCompletions);
    }

    @Override
    public String getBuffer() {
        return this.buffer;
    }

    @Override
    public boolean isCommand() {
        return this.isCommand;
    }

    @Override
    public @Nullable Location getLocation() {
        return this.location != null ? this.location.clone() : null;
    }

    @Override
    public boolean isHandled() {
        return !this.completions.isEmpty() || this.handled;
    }

    @Override
    public void setHandled(final boolean handled) {
        this.handled = handled;
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
        return AsyncTabCompleteEvent.getHandlerList();
    }

    private static List<Completion> fromStrings(final List<String> suggestions) {
        final List<Completion> list = new ArrayList<>(suggestions.size());
        for (final String suggestion : suggestions) {
            list.add(Completion.completion(suggestion, null));
        }
        return list;
    }
}
