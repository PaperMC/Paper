package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerArmorStandManipulateEvent extends CraftPlayerInteractEntityEvent implements PlayerArmorStandManipulateEvent {

    private final ItemStack playerItem;
    private final ItemStack armorStandItem;
    private final EquipmentSlot slot;

    public CraftPlayerArmorStandManipulateEvent(final Player player, final ArmorStand clickedEntity, final ItemStack playerItem, final ItemStack armorStandItem, final EquipmentSlot slot, final EquipmentSlot hand) {
        super(player, clickedEntity, hand);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }

    @Override
    public ItemStack getPlayerItem() {
        return this.playerItem;
    }

    @Override
    public ItemStack getArmorStandItem() {
        return this.armorStandItem;
    }

    @Override
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @Override
    public ArmorStand getRightClicked() {
        return (ArmorStand) super.getRightClicked();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerArmorStandManipulateEvent.getHandlerList();
    }
}
