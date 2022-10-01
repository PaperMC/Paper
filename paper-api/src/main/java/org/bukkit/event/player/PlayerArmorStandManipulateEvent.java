package org.bukkit.event.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player interacts with an armor stand and will either swap, retrieve or
 * place an item.
 */
public class PlayerArmorStandManipulateEvent extends PlayerInteractEntityEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack playerItem;
    private final ItemStack armorStandItem;
    private final EquipmentSlot slot;

    public PlayerArmorStandManipulateEvent(@NotNull final Player who, @NotNull final ArmorStand clickedEntity, @NotNull final ItemStack playerItem, @NotNull final ItemStack armorStandItem, @NotNull final EquipmentSlot slot, @NotNull EquipmentSlot hand) {
        super(who, clickedEntity, hand);
        this.playerItem = playerItem;
        this.armorStandItem = armorStandItem;
        this.slot = slot;
    }

    @Deprecated
    public PlayerArmorStandManipulateEvent(@NotNull final Player who, @NotNull final ArmorStand clickedEntity, @NotNull final ItemStack playerItem, @NotNull final ItemStack armorStandItem, @NotNull final EquipmentSlot slot) {
        this(who, clickedEntity, playerItem, armorStandItem, slot, EquipmentSlot.HAND);
    }

    /**
     * Returns the item held by the player.
     * <p>
     * If this item is empty and the armor stand item is also empty, there will be no
     * transaction between the player and the armor stand. If the player's item is empty
     * but the armor stand item is not, the player's item will be placed on the armor
     * stand. If both items are not empty, the items will be swapped.
     * <p>
     * In the case that this event is cancelled, the original items will remain the same.
     * @return the item held by the player.
     */
    @NotNull
    public ItemStack getPlayerItem() {
        return this.playerItem;
    }

    /**
     * Returns the item held by the armor stand.
     * <p>
     * If this item is empty and the player's item is also empty, there will be no
     * transaction between the player and the armor stand. If the player's item is empty
     * but the armor stand item is not, then the player will obtain the armor stand item.
     * In the case that the player's item is not empty but the armor stand item is empty,
     * the player's item will be placed on the armor stand. If both items are not empty,
     * the items will be swapped.
     * <p>
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

    /**
     * {@inheritDoc}
     * <p>
     * Note that this is not the hand of the armor stand that was changed, but rather
     * the hand used by the player to swap items with the armor stand.
     */
    @NotNull
    @Override
    public EquipmentSlot getHand() {
        return super.getHand();
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
