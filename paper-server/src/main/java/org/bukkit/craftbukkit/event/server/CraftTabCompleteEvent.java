package org.bukkit.craftbukkit.event.server;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.TabCompleteEvent;
import org.jspecify.annotations.Nullable;

public class CraftTabCompleteEvent extends CraftEvent implements TabCompleteEvent {

    private final CommandSender sender;
    private final String buffer;
    private final boolean isCommand;
    private final Location location;
    private List<String> completions;

    private boolean cancelled;

    public CraftTabCompleteEvent(final CommandSender sender, final String buffer, final List<String> completions) {
        this(sender, buffer, completions, sender instanceof ConsoleCommandSender || buffer.startsWith("/"), null);
    }

    public CraftTabCompleteEvent(final CommandSender sender, final String buffer, final List<String> completions, final boolean isCommand, final @Nullable Location location) {
        this.sender = sender;
        this.buffer = buffer;
        this.completions = new ArrayList<>(completions);
        this.isCommand = isCommand;
        this.location = location;
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public String getBuffer() {
        return this.buffer;
    }

    @Override
    public List<String> getCompletions() {
        return this.completions;
    }

    @Override
    public void setCompletions(final List<String> completions) {
        Preconditions.checkArgument(completions != null, "completions cannot be null");
        this.completions = new ArrayList<>(completions);
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
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return TabCompleteEvent.getHandlerList();
    }
}
