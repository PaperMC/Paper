package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperCompostItemEvent extends CraftBlockEvent implements CompostItemEvent {

    private final ItemStack item;
    private boolean willRaiseLevel;

    public PaperCompostItemEvent(final Block composter, final ItemStack item, final boolean willRaiseLevel) {
        super(composter);
        this.item = item;
        this.willRaiseLevel = willRaiseLevel;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean willRaiseLevel() {
        return this.willRaiseLevel;
    }

    @Override
    public void setWillRaiseLevel(final boolean willRaiseLevel) {
        this.willRaiseLevel = willRaiseLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return CompostItemEvent.getHandlerList();
    }
}
