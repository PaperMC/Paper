package io.papermc.paper.event.block;

import org.bukkit.block.data.type.Vault;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jspecify.annotations.Nullable;

/**
 * Called when a vault block changes state.
 */
public interface VaultChangeStateEvent extends BlockEvent, Cancellable {

    /**
     * Gets the player associated with this state change, if applicable.
     *
     * @return The associated player, or {@code null} if not known.
     */
    @Nullable Player getPlayer();

    /**
     * Gets the state the vault is currently in.
     *
     * @return The current vault state.
     */
    Vault.State getCurrentState();

    /**
     * Gets the state the vault is attempting to transition to.
     *
     * @return The new vault state.
     */
    Vault.State getNewState();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
