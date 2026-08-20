package com.destroystokyo.paper.event.brigadier;

import com.mojang.brigadier.tree.RootCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired any time a Brigadier RootCommandNode is generated for a player to inform the client of commands.
 * You may manipulate this CommandNode to change what the client sees.
 *
 * <p>This event may fire on login, world change, and permission rebuilds, by plugin request, and potentially future means.</p>
 *
 * <p>This event will fire before {@link org.bukkit.event.player.PlayerCommandSendEvent}, so no filtering has been done by
 * other plugins yet.</p>
 *
 * <p>WARNING: This event will potentially (and most likely) fire twice! Once for Async, and once again for Sync.
 * It is important that you check event.isAsynchronous() and event.hasFiredAsync() to ensure you only act once.
 * If for some reason we are unable to send this asynchronously in the future, only the sync method will fire.</p>
 *
 * <p>Your logic should look like this:
 * {@code if (event.isAsynchronous() || !event.hasFiredAsync()) { // do stuff }}</p>
 *
 * <p>If your logic is not safe to run asynchronously, only react to the synchronous version.</p>
 *
 * <p>This is a draft/experimental API and is subject to change.</p>
 */
@NullMarked
@ApiStatus.Experimental
public interface AsyncPlayerSendCommandsEvent<S extends CommandSourceStack> extends PlayerEvent {

    /**
     * Gets the full Root Command Node being sent to the client, which is mutable.
     *
     * @return the root command node
     */
    RootCommandNode<S> getCommandNode();

    /**
     * Gets if this event has already fired asynchronously.
     *
     * @return whether this event has already fired asynchronously
     */
    boolean hasFiredAsync();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
