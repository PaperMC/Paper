package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerBucketEntityEvent extends CraftPlayerEvent implements PlayerBucketEntityEvent {

    private final Entity entity;
    private final ItemStack originalBucket;
    private final ItemStack entityBucket;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public CraftPlayerBucketEntityEvent(final Player player, final Entity entity, final ItemStack originalBucket, final ItemStack entityBucket, final EquipmentSlot hand) {
        super(player);
        this.entity = entity;
        this.originalBucket = originalBucket;
        this.entityBucket = entityBucket;
        this.hand = hand;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public ItemStack getOriginalBucket() {
        return this.originalBucket;
    }

    @Override
    public ItemStack getEntityBucket() {
        return this.entityBucket;
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
        return PlayerBucketEntityEvent.getHandlerList();
    }
}
