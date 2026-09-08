
package org.bukkit.craftbukkit.event.block;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.craftbukkit.event.inventory.CraftInventoryEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class CraftInventoryCloseEvent extends CraftInventoryEvent implements InventoryCloseEvent {

    private final Reason reason;

    public CraftInventoryCloseEvent(final InventoryView transaction, final Reason reason) {
        super(transaction);
        this.reason = reason;
    }

    public CraftInventoryCloseEvent(final AbstractContainerMenu menu, final Reason reason) {
        this(menu.getBukkitView(), reason);
    }

    @Override
    public HumanEntity getPlayer() {
        return this.transaction.getPlayer();
    }

    @Override
    public Reason getReason() {
        return this.reason;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryCloseEvent.getHandlerList();
    }
}
