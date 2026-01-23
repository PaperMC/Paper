package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class CraftPlayerInteractAtEntityEvent extends CraftPlayerInteractEntityEvent implements PlayerInteractAtEntityEvent {

    private final Vector position;

    public CraftPlayerInteractAtEntityEvent(final Player player, final Entity clickedEntity, final Vector position, final EquipmentSlot hand) {
        super(player, clickedEntity, hand);
        this.position = position;
    }

    @Override
    public Vector getClickedPosition() {
        return this.position.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerInteractAtEntityEvent.getHandlerList();
    }
}
