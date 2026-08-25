package com.destroystokyo.paper.event.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommand;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.bukkit.Warning;
import org.bukkit.command.Command;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEventNew;

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
public interface CommandRegisteredEvent<S extends BukkitBrigadierCommandSource> extends ServerEventNew, Cancellable {

    /**
     * Gets the command label of the {@link Command} being registered.
     *
     * @return the command label
     */
    String getCommandLabel();

    /**
     * Gets the {@link BukkitBrigadierCommand} for the {@link Command} being registered. This can be used
     * as the {@link com.mojang.brigadier.Command command executor} or
     * {@link com.mojang.brigadier.suggestion.SuggestionProvider} of a {@link com.mojang.brigadier.tree.CommandNode}
     * to delegate to the {@link Command} being registered.
     *
     * @return the {@link BukkitBrigadierCommand}
     */
    BukkitBrigadierCommand<S> getBrigadierCommand();

    /**
     * Gets the {@link Command} being registered.
     *
     * @return the {@link Command}
     */
    Command getCommand();

    /**
     * Gets the {@link RootCommandNode} which is being registered to.
     *
     * @return the {@link RootCommandNode}
     */
    RootCommandNode<S> getRoot();

    /**
     * Gets the {@link LiteralCommandNode} to be registered for the {@link Command}.
     *
     * @return the {@link LiteralCommandNode}
     */
    LiteralCommandNode<S> getLiteral();

    /**
     * Sets the {@link LiteralCommandNode} used to register this command. The default literal is mutable, so
     * this is primarily if you want to completely replace the object.
     *
     * @param literal new node
     */
    void setLiteral(LiteralCommandNode<S> literal);

    /**
     * Gets the Bukkit APIs default arguments node (greedy string), for if
     * you wish to reuse it.
     *
     * @return default arguments node
     */
    ArgumentCommandNode<S, String> getDefaultArgs();

    /**
     * Gets whether this command should is treated as "raw".
     *
     * @see #setRawCommand(boolean)
     * @return whether this command is treated as "raw"
     */
    boolean isRawCommand();

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
    void setRawCommand(boolean rawCommand);

    /**
     * Cancels registering this command to Brigadier, but will remain in Bukkit Command Map. Can be used to hide a
     * command from all players.
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
