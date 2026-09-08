package org.bukkit.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.HandlerList;

/**
 * Called when a players experience changes naturally
 */
public interface PlayerExpChangeEvent extends PlayerEvent {

    /**
     * Get the source that provided the experience.
     *
     * @return The source of the experience
     */
    ExperienceOrb getSource();

    /**
     * Get the amount of experience the player will receive
     *
     * @return The amount of experience
     */
    int getAmount();

    /**
     * Set the amount of experience the player will receive
     *
     * @param amount The amount of experience to set
     */
    void setAmount(int amount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
