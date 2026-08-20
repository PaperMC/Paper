package io.papermc.paper.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerLecternPageChangeEvent extends CraftPlayerEvent implements PlayerLecternPageChangeEvent {

    private final Lectern lectern;
    private final ItemStack book;
    private final PageChangeDirection pageChangeDirection;
    private final int oldPage;
    private int newPage;

    private boolean cancelled;

    public PaperPlayerLecternPageChangeEvent(final Player player, final Lectern lectern, final ItemStack book, final PageChangeDirection pageChangeDirection, final int oldPage, final int newPage) {
        super(player);
        this.lectern = lectern;
        this.book = book;
        this.pageChangeDirection = pageChangeDirection;
        this.oldPage = oldPage;
        this.newPage = newPage;
    }

    @Override
    public Lectern getLectern() {
        return this.lectern;
    }

    @Override
    public ItemStack getBook() {
        return this.book;
    }

    @Override
    public PageChangeDirection getPageChangeDirection() {
        return this.pageChangeDirection;
    }

    @Override
    public int getOldPage() {
        return this.oldPage;
    }

    @Override
    public int getNewPage() {
        return this.newPage;
    }

    @Override
    public void setNewPage(final int newPage) {
        this.newPage = newPage;
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
        return PlayerLecternPageChangeEvent.getHandlerList();
    }
}
