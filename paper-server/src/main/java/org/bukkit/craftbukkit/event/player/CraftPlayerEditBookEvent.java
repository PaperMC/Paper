package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class CraftPlayerEditBookEvent extends CraftPlayerEvent implements PlayerEditBookEvent {

    private final BookMeta previousBookMeta;
    private final int slot;
    private BookMeta newBookMeta;
    private boolean isSigning;

    private boolean cancelled;

    public CraftPlayerEditBookEvent(Player player, int slot, BookMeta previousBookMeta, BookMeta newBookMeta, boolean isSigning) {
        super(player);
        this.previousBookMeta = previousBookMeta;
        this.newBookMeta = newBookMeta;
        this.slot = slot;
        this.isSigning = isSigning;
    }

    @Override
    public BookMeta getPreviousBookMeta() {
        return this.previousBookMeta.clone();
    }

    @Override
    public BookMeta getNewBookMeta() {
        return this.newBookMeta.clone();
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public void setNewBookMeta(final BookMeta newBookMeta) {
        Preconditions.checkArgument(newBookMeta != null, "New book meta must not be null");
        Bukkit.getItemFactory().equals(newBookMeta, null);
        this.newBookMeta = newBookMeta.clone();
    }

    @Override
    public boolean isSigning() {
        return this.isSigning;
    }

    @Override
    public void setSigning(final boolean signing) {
        this.isSigning = signing;
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
        return PlayerEditBookEvent.getHandlerList();
    }
}
