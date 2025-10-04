package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EntityBlockingDelayCheckEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity entity;
    private final ItemStack itemStack;
    private final int ticksUsing;
    private int blockingDelay;
    private final int originalBlockingDelay = blockingDelay;
    private final Reason reason;

    @ApiStatus.Internal
    public EntityBlockingDelayCheckEvent(
        LivingEntity entity,
        ItemStack itemStack,
        int ticksUsing,
        int blockingDelay,
        Reason reason
    ) {
        super(entity);
        this.entity = entity;
        this.itemStack = itemStack;
        this.ticksUsing = ticksUsing;
        this.blockingDelay = blockingDelay;
        this.reason = reason;
    }

    public enum Reason {
        APPLY_ITEM_BLOCKED,
        RESOLVE_BLOCKED_DAMAGE,
        IS_BLOCKING,
        CAN_BLOCK_ATTACK,
        BLOCKING_ITEM_EFFECTS,
        BLOCK_USING_ITEM
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getTicksUsing() {
        return ticksUsing;
    }

    public int getBlockingDelay() {
        return blockingDelay;
    }

    public void setBlockingDelay(int blockingDelay) {
        this.blockingDelay = blockingDelay;
    }

    public int getOriginalBlockingDelay() {
        return originalBlockingDelay;
    }

    public Reason getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
