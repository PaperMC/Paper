package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.potion.PotionEffectType;

/**
 * An event that is triggered when an entity receives a potion effect instantly
 * or when the potion effect is applied on each tick (e.g. every 25 ticks for Poison level 1).
 * <p>
 * For example, this event may be called when an entity regenerates health
 * or takes poison damage as a result of a potion effect.
 */
public interface EntityEffectTickEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the type of the potion effect associated with this event.
     *
     * @return the {@link PotionEffectType} of the effect
     */
    PotionEffectType getType();

    /**
     * Gets the amplifier level of the potion effect associated with this event.
     *
     * @return the amplifier level of the potion effect
     */
    int getAmplifier();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
