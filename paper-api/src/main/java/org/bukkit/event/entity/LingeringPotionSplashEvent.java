package org.bukkit.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.HandlerList;

/**
 * Called when a splash potion hits an area
 */
public interface LingeringPotionSplashEvent extends ProjectileHitEvent {

    @Override
    ThrownPotion getEntity();

    /**
     * Gets the AreaEffectCloud spawned
     *
     * @return The spawned AreaEffectCloud
     */
    AreaEffectCloud getAreaEffectCloud();

    /**
     * Sets if an Empty AreaEffectCloud may be created
     *
     * @param allowEmptyAreaEffectCreation If an Empty AreaEffectCloud may be created
     */
    void allowsEmptyCreation(boolean allowEmptyAreaEffectCreation);

    /**
     * Gets if an empty AreaEffectCloud may be created
     *
     * @return if an empty AreaEffectCloud may be created
     */
    boolean allowsEmptyCreation();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
