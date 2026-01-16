package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Vault;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a vault block changes state.
 */
@NullMarked
public class VaultChangeStateEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable Player player;
    private final Vault.State currentState;
    private final Vault.State newState;

    private boolean cancelled;

    @ApiStatus.Internal
    public VaultChangeStateEvent(final Block vaultBlock, final @Nullable Player player, final Vault.State currentState, final Vault.State newState) {
        super(vaultBlock);
        this.player = player;
        this.currentState = currentState;
        this.newState = newState;
    }

    /**
     * Gets the player associated with this state change, if applicable.
     *
     * @return The associated player, or {@code null} if not known.
     */
    public @Nullable Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the state the vault is currently in.
     *
     * @return The current vault state.
     */
    public Vault.State getCurrentState() {
        return this.currentState;
    }

    /**
     * Gets the state the vault is attempting to transition to.
     *
     * @return The new vault state.
     */
    public Vault.State getNewState() {
        return this.newState;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
