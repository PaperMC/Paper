package org.bukkit.craftbukkit.event.inventory;

import net.kyori.adventure.text.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

public class CraftInventoryOpenEvent extends CraftInventoryEvent implements InventoryOpenEvent {

    private @Nullable Component titleOverride;
    private boolean cancelled;

    public CraftInventoryOpenEvent(final InventoryView transaction) {
        super(transaction);
    }

    public CraftInventoryOpenEvent(final AbstractContainerMenu menu) {
        this(menu.getBukkitView());
    }

    @Override
    public HumanEntity getPlayer() {
        return this.transaction.getPlayer();
    }

    @Override
    public @Nullable Component titleOverride() {
        return this.titleOverride;
    }

    @Override
    public void titleOverride(final @Nullable Component titleOverride) {
        this.titleOverride = titleOverride;
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
        return InventoryOpenEvent.getHandlerList();
    }
}
