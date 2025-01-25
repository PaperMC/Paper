package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An event that is triggered when an entity receives a potion effect instantly
 * or when the potion effect is applied on each tick (e.g. every 25 ticks for Poison level 1).
 * <p>
 * For example, this event may be called when an entity regenerates health
 * or takes poison damage as a result of a potion effect.
 */
@NullMarked
public class EntityEffectTickEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PotionEffectType type;
    private final int amplifier;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityEffectTickEvent(final LivingEntity entity, final PotionEffectType type, final int amplifier) {
        super(entity);
        this.type = type;
        this.amplifier = amplifier;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    /**
     * Gets the type of the potion effect associated with this event.
     *
     * @return the {@link PotionEffectType} of the effect
     */
    public PotionEffectType getType() {
        return type;
    }

    /**
     * Gets the amplifier level of the potion effect associated with this event.
     *
     * @return the amplifier level of the potion effect
     */
    public int getAmplifier() {
        return amplifier;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
