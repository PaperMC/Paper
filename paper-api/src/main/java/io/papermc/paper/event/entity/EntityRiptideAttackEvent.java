package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;


/**
 * Called when an entity is performing a spin attack and touches other entities.
 * Currently, only player has default damage action which
 * calls {@link org.bukkit.event.entity.EntityDamageByEntityEvent}.
 */
@NullMarked
public class EntityRiptideAttackEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;
    private final LivingEntity touched;


    @ApiStatus.Internal
    public EntityRiptideAttackEvent(final LivingEntity attacker, final LivingEntity touched) {
        super(attacker);
        this.touched = touched;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Gets the entity that gets touched.
     *
     * @return the touched entity
     */
    public LivingEntity getTouched() {
        return touched;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }
}
