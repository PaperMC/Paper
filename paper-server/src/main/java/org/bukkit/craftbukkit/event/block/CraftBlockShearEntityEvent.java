package org.bukkit.craftbukkit.event.block;

import java.util.Collections;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public class CraftBlockShearEntityEvent extends CraftBlockEvent implements BlockShearEntityEvent {

    private final Entity sheared;
    private final ItemStack tool;
    private List<ItemStack> drops;

    private boolean cancelled;

    public CraftBlockShearEntityEvent(final Block dispenser, final Entity sheared, final ItemStack tool, final List<ItemStack> drops) {
        super(dispenser);
        this.sheared = sheared;
        this.tool = tool;
        this.drops = Collections.unmodifiableList(drops);
    }

    @Override
    public Entity getEntity() {
        return this.sheared;
    }

    @Override
    public ItemStack getTool() {
        return this.tool.clone();
    }

    @Override
    public @Unmodifiable List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public void setDrops(final List<ItemStack> drops) {
        this.drops = List.copyOf(drops);
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
        return BlockShearEntityEvent.getHandlerList();
    }
}
