package io.papermc.paper.event.inventory;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.event.inventory.CraftInventoryEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

public class PaperAnvilDamagedEvent extends CraftInventoryEvent implements AnvilDamagedEvent {

    private DamageState damageState;
    private boolean cancelled;

    public PaperAnvilDamagedEvent(final InventoryView inventory, final @Nullable BlockData blockData) {
        super(inventory);
        this.damageState = DamageState.getState(blockData);
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @Override
    public DamageState getDamageState() {
        return this.damageState;
    }

    @Override
    public void setDamageState(final DamageState damageState) {
        this.damageState = damageState;
    }

    @Override
    public boolean isBreaking() {
        return this.damageState == DamageState.BROKEN;
    }

    @Override
    public void setBreaking(final boolean breaking) {
        if (breaking) {
            this.damageState = DamageState.BROKEN;
        } else if (this.damageState == DamageState.BROKEN) {
            this.damageState = DamageState.DAMAGED;
        }
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
        return AnvilDamagedEvent.getHandlerList();
    }
}
