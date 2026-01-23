package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CraftPlayerInteractEntityEvent extends CraftPlayerEvent implements PlayerInteractEntityEvent {

    protected Entity clickedEntity;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public CraftPlayerInteractEntityEvent(final Player player, final Entity clickedEntity, final EquipmentSlot hand) {
        super(player);
        this.clickedEntity = clickedEntity;
        this.hand = hand;
    }

    @Override
    public Entity getRightClicked() {
        return this.clickedEntity;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
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
        return PlayerInteractEntityEvent.getHandlerList();
    }
}
