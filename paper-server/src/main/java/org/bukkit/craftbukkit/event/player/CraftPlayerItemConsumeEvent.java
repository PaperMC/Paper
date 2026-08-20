package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPlayerItemConsumeEvent extends CraftPlayerEvent implements PlayerItemConsumeEvent {

    private final EquipmentSlot hand;
    private ItemStack item;
    private @Nullable ItemStack replacement;

    private boolean cancelled;

    public CraftPlayerItemConsumeEvent(final Player player, final ItemStack item, final EquipmentSlot hand) {
        super(player);

        this.item = item;
        this.hand = hand;
    }

    @Override
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    public void setItem(final @Nullable ItemStack item) {
        this.item = item == null ? ItemStack.empty() : item;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public @Nullable ItemStack getReplacement() {
        return this.replacement;
    }

    @Override
    public void setReplacement(final @Nullable ItemStack replacement) {
        this.replacement = replacement;
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
        return PlayerItemConsumeEvent.getHandlerList();
    }
}
