package org.bukkit.event.entity;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public interface VillagerCareerChangeEvent extends EntityEvent, Cancellable {

    @Override
    Villager getEntity();

    /**
     * Gets the future profession of the villager.
     *
     * @return The profession the villager will change to
     */
    Profession getProfession();

    /**
     * Sets the profession the villager will become from this event.
     *
     * @param profession new profession
     */
    void setProfession(Profession profession);

    /**
     * Gets the reason for why the villager's career is changing.
     *
     * @return Reason for villager's profession changing
     */
    ChangeReason getReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Reasons for the villager's profession changing.
     */
    enum ChangeReason {

        /**
         * Villager lost their job due to too little experience.
         */
        LOSING_JOB,
        /**
         * Villager gained employment.
         */
        EMPLOYED
    }
}
