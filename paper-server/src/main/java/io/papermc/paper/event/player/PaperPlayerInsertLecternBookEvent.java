package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerInsertLecternBookEvent extends CraftPlayerEvent implements PlayerInsertLecternBookEvent {

    private final Block block;
    private ItemStack book;

    private boolean cancelled;

    public PaperPlayerInsertLecternBookEvent(final Player player, final Block block, final ItemStack book) {
        super(player);
        this.block = block;
        this.book = book;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Lectern getLectern() {
        final BlockState state = this.block.getState();
        Preconditions.checkState(state instanceof Lectern, "Block state of lectern block is no longer a lectern tile state!");
        return (Lectern) state;
    }

    @Override
    public ItemStack getBook() {
        return this.book.clone();
    }

    @Override
    public void setBook(final ItemStack book) {
        Preconditions.checkArgument(book != null, "Cannot set book to null");
        this.book = book.clone();
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
        return PlayerInsertLecternBookEvent.getHandlerList();
    }
}
