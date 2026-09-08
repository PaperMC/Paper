package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

    public CraftPlayerBucketEntityEvent(
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.entity.Entity entity,
        final net.minecraft.world.item.ItemStack originalBucket,
        final net.minecraft.world.item.ItemStack entityBucket,
        final InteractionHand hand
    ) {
        this(
            (Player) player.getBukkitEntity(),
            entity.getBukkitEntity(),
            CraftItemStack.asBukkitCopy(originalBucket),
            CraftItemStack.asBukkitCopy(entityBucket),
            CraftEquipmentSlot.getHand(hand)
        );
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
