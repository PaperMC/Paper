package org.bukkit.craftbukkit.event.player;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public class CraftPlayerShearEntityEvent extends CraftPlayerEvent implements PlayerShearEntityEvent {

    private final Entity entity;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private List<ItemStack> drops;

    private boolean cancelled;

    public CraftPlayerShearEntityEvent(final Player player, final Entity entity, final ItemStack item, final EquipmentSlot hand, final List<ItemStack> drops) {
        super(player);
        this.entity = entity;
        this.item = item;
        this.hand = hand;
        this.drops = drops;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public final @Unmodifiable List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public void setDrops(final List<ItemStack> drops) {
        this.drops = List.copyOf(drops);
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
        return PlayerShearEntityEvent.getHandlerList();
    }
}
