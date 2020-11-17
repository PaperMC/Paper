package org.bukkit.event.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player interacts with an armor stand and will either swap, retrieve or place an item.
 */
public class PlayerArmorStandManipulateEvent extends PlayerInteractEntityEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack playerItem;
    private final ItemStack armorStandItem;
    private final EquipmentSlot slot;

    public PlayerArmorStandManipulateEvent(@NotNull final Player who, @NotNull final ArmorStand clickedEntity, @NotNull final ItemStack playerItem, @NotNull final ItemStack armorStandItem, @NotNull final EquipmentSlot slot) {
        super(who, clickedEntity);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }

    /**
     * Returns the item held by the player. If this Item is null and the armor stand Item is also null,
     * there will be no transaction between the player and the armor stand.
     * If the Player's item is null, but the armor stand item is not then the player will obtain the armor stand item.
     * In the case that the Player's item is not null, but the armor stand item is null, the players item will be placed on the armor stand.
     * If both items are not null, the items will be swapped.
     * In the case that the event is cancelled the original items will remain the same.
     * @return the item held by the player.
     */
    @NotNull
    public ItemStack getPlayerItem() {
        return this.playerItem;
    }

    /**
     * Returns the item held by the armor stand.
     * If this Item is null and the player's Item is also null, there will be no transaction between the player and the armor stand.
     * If the Player's item is null, but the armor stand item is not then the player will obtain the armor stand item.
     * In the case that the Player's item is not null, but the armor stand item is null, the players item will be placed on the armor stand.
     * If both items are not null, the items will be swapped.
     * In the case that the event is cancelled the original items will remain the same.
     * @return the item held by the armor stand.
     */
    @NotNull
    public ItemStack getArmorStandItem() {
        return this.armorStandItem;
    }

    /**
     * Returns the raw item slot of the armor stand in this event.
     *
     * @return the index of the item obtained or placed of the armor stand.
     */
    @NotNull
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @NotNull
    @Override
    public ArmorStand getRightClicked() {
        return (ArmorStand) this.clickedEntity;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
