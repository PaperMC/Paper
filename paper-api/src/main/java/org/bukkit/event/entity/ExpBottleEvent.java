package org.bukkit.event.entity;

import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.HandlerList;

/**
 * Called when a ThrownExpBottle hits and releases experience.
 */
public interface ExpBottleEvent extends ProjectileHitEvent {

    @Override
    ThrownExpBottle getEntity();

    /**
     * This method indicates if the particle effect should be shown.
     *
     * @return {@code true} if the effect will be shown, {@code false} otherwise
     */
    boolean getShowEffect();

    /**
     * This method sets if the particle effect will be shown.
     * <p>
     * This does not change the experience created.
     *
     * @param showEffect {@code true} indicates the effect will be shown, false
     *     indicates no effect will be shown
     */
    void setShowEffect(boolean showEffect);

    /**
     * This method retrieves the amount of experience to be created.
     * <p>
     * The number indicates a total amount to be divided into orbs.
     *
     * @return the total amount of experience to be created
     */
    int getExperience();

    /**
     * This method sets the amount of experience to be created.
     * <p>
     * The number indicates a total amount to be divided into orbs.
     *
     * @param exp the total amount of experience to be created
     */
    void setExperience(int exp);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
