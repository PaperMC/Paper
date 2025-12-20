package com.destroystokyo.paper.event.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommand;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.bukkit.Warning;
import org.bukkit.command.Command;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired anytime the server synchronizes Bukkit commands to Brigadier.
 *
 * <p>Allows a plugin to control the command node structure for its commands.
 * This is done at Plugin Enable time after commands have been registered, but may also
 * run at a later point in the server lifetime due to plugins, a server reload, etc.</p>
 *
 * @deprecated For removal, use the new brigadier api.
 */
@Deprecated(since = "1.20.6")
@Warning(reason = "This event has been superseded by the Commands API and will be removed in a future release. Listen to LifecycleEvents.COMMANDS instead.", value = true)
public class CommandRegisteredEvent<S extends com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource> extends ServerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String commandLabel;
    private final Command command;
    private final com.destroystokyo.paper.brigadier.BukkitBrigadierCommand<S> brigadierCommand;
    private final RootCommandNode<S> root;
    private final ArgumentCommandNode<S, String> defaultArgs;
    private LiteralCommandNode<S> literal;
    private boolean rawCommand = false;

    private boolean cancelled;

    public CommandRegisteredEvent(String commandLabel, com.destroystokyo.paper.brigadier.BukkitBrigadierCommand<S> brigadierCommand, Command command, RootCommandNode<S> root, LiteralCommandNode<S> literal, ArgumentCommandNode<S, String> defaultArgs) {
        this.commandLabel = commandLabel;
        this.brigadierCommand = brigadierCommand;
        this.command = command;
        this.root = root;
        this.literal = literal;
        this.defaultArgs = defaultArgs;
    }

    /**
     * Gets the command label of the {@link Command} being registered.
     *
     * @return the command label
     */
    public String getCommandLabel() {
        return this.commandLabel;
    }

    /**
     * Gets the {@link BukkitBrigadierCommand} for the {@link Command} being registered. This can be used
     * as the {@link com.mojang.brigadier.Command command executor} or
     * {@link com.mojang.brigadier.suggestion.SuggestionProvider} of a {@link com.mojang.brigadier.tree.CommandNode}
     * to delegate to the {@link Command} being registered.
     *
     * @return the {@link BukkitBrigadierCommand}
     */
    public BukkitBrigadierCommand<S> getBrigadierCommand() {
        return this.brigadierCommand;
    }

    /**
     * Gets the {@link Command} being registered.
     *
     * @return the {@link Command}
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Gets the {@link RootCommandNode} which is being registered to.
     *
     * @return the {@link RootCommandNode}
     */
    public RootCommandNode<S> getRoot() {
        return this.root;
    }

    /**
     * Gets the Bukkit APIs default arguments node (greedy string), for if
     * you wish to reuse it.
     *
     * @return default arguments node
     */
    public ArgumentCommandNode<S, String> getDefaultArgs() {
        return this.defaultArgs;
    }

    /**
     * Gets the {@link LiteralCommandNode} to be registered for the {@link Command}.
     *
     * @return the {@link LiteralCommandNode}
     */
    public LiteralCommandNode<S> getLiteral() {
        return this.literal;
    }

    /**
     * Sets the {@link LiteralCommandNode} used to register this command. The default literal is mutable, so
     * this is primarily if you want to completely replace the object.
     *
     * @param literal new node
     */
    public void setLiteral(LiteralCommandNode<S> literal) {
        this.literal = literal;
    }

    /**
     * Gets whether this command should is treated as "raw".
     *
     * @see #setRawCommand(boolean)
     * @return whether this command is treated as "raw"
     */
    public boolean isRawCommand() {
        return this.rawCommand;
    }

    /**
     * Sets whether this command should be treated as "raw".
     *
     * <p>A "raw" command will only use the node provided by this event for
     * sending the command tree to the client. For execution purposes, the default
     * greedy string execution of a standard Bukkit {@link Command} is used.</p>
     *
     * <p>On older versions of Paper, this was the default and only behavior of this
     * event.</p>
     *
     * @param rawCommand whether this command should be treated as "raw"
     */
    public void setRawCommand(final boolean rawCommand) {
        this.rawCommand = rawCommand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels registering this command to Brigadier, but will remain in Bukkit Command Map. Can be used to hide a
     * command from all players.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
