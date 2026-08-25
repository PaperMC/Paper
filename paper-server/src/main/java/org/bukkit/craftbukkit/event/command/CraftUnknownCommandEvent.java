package org.bukkit.craftbukkit.event.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.command.UnknownCommandEvent;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftUnknownCommandEvent extends CraftEvent implements UnknownCommandEvent {

    private final CommandSourceStack commandSource;
    private final String commandLine;
    private @Nullable Component message;

    public CraftUnknownCommandEvent(final CommandSourceStack commandSource, final String commandLine, final @Nullable Component message) {
        super(false);
        this.commandSource = commandSource;
        this.commandLine = commandLine;
        this.message = message;
    }

    @Override
    public CommandSender getSender() {
        return this.commandSource.getSender();
    }

    @Override
    public CommandSourceStack getCommandSource() {
        return this.commandSource;
    }

    @Override
    public String getCommandLine() {
        return this.commandLine;
    }

    @Override
    @Deprecated
    public @Nullable String getMessage() {
        return this.message == null ? null : LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    @Override
    @Deprecated
    public void setMessage(final @Nullable String message) {
        this.message(message == null ? null : LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    @Contract(pure = true)
    public @Nullable Component message() {
        return this.message;
    }

    @Override
    public void message(final @Nullable Component message) {
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return UnknownCommandEvent.getHandlerList();
    }
}

