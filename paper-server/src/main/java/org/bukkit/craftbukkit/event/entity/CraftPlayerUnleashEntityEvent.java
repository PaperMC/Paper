package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CraftPlayerUnleashEntityEvent extends CraftEntityUnleashEvent implements PlayerUnleashEntityEvent {

    private final Player player;
    private final EquipmentSlot hand;

    public CraftPlayerUnleashEntityEvent(final Entity entity, final Player player, final EquipmentSlot hand, final boolean dropLeash) {
        super(entity, UnleashReason.PLAYER_UNLEASH, dropLeash);
        this.player = player;
        this.hand = hand;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }
}
