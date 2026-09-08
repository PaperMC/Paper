package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.common.value.qual.IntRange;

public class CraftPlayerEditBookEvent extends CraftPlayerEvent implements PlayerEditBookEvent {

    private final BookMeta previousBookMeta;
    private final int slot;
    private BookMeta newBookMeta;
    private boolean signing;

    private boolean cancelled;

    public CraftPlayerEditBookEvent(Player player, int slot, BookMeta previousBookMeta, BookMeta newBookMeta, boolean signing) {
        super(player);
        this.previousBookMeta = previousBookMeta;
        this.newBookMeta = newBookMeta;
        this.slot = slot;
        this.signing = signing;
    }

    public CraftPlayerEditBookEvent(
        final ServerPlayer player,
        final int slot,
        final ItemStack book,
        final ItemStack newBook,
        final boolean signing
    ) {
        this(
            player.getBukkitEntity(),
            slot,
            (BookMeta) CraftItemStack.getItemMeta(book),
            (BookMeta) CraftItemStack.getItemMeta(newBook),
            signing
        );
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
    public void setNewBookMeta(final BookMeta newBookMeta) {
        Preconditions.checkArgument(newBookMeta != null, "New book meta must not be null");
        Bukkit.getItemFactory().equals(newBookMeta, null);
        this.newBookMeta = newBookMeta.clone();
    }

    @Override
    public @IntRange(from = -1, to = 8) int getSlot() {
        return this.slot;
    }

    @Override
    public boolean isSigning() {
        return this.signing;
    }

    @Override
    public void setSigning(final boolean signing) {
        this.signing = signing;
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
