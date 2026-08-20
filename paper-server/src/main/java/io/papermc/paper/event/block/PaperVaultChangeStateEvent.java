package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Vault;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperVaultChangeStateEvent extends CraftBlockEvent implements VaultChangeStateEvent {

    private final @Nullable Player player;
    private final Vault.State currentState;
    private final Vault.State newState;

    private boolean cancelled;

    public PaperVaultChangeStateEvent(final Block vaultBlock, final @Nullable Player player, final Vault.State currentState, final Vault.State newState) {
        super(vaultBlock);
        this.player = player;
        this.currentState = currentState;
        this.newState = newState;
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public Vault.State getCurrentState() {
        return this.currentState;
    }

    @Override
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
        return VaultChangeStateEvent.getHandlerList();
    }
}
