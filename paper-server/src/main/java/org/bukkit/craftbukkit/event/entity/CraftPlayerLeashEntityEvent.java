package org.bukkit.craftbukkit.event.entity;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CraftPlayerLeashEntityEvent extends CraftEvent implements PlayerLeashEntityEvent {

    private final Entity leashHolder;
    private final Entity entity;
    private final Player player;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public CraftPlayerLeashEntityEvent(final Entity entity, final Entity leashHolder, final Player leasher, final EquipmentSlot hand) {
        this.leashHolder = leashHolder;
        this.entity = entity;
        this.player = leasher;
        this.hand = hand;
    }

    @Override
    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public Player getPlayer() {
        return this.player;
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
        return PlayerLeashEntityEvent.getHandlerList();
    }
}
