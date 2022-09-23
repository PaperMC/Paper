package org.bukkit.event.entity;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VillagerCareerChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Profession profession;
    private final ChangeReason reason;

    public VillagerCareerChangeEvent(@NotNull Villager what, @NotNull Profession profession, @NotNull ChangeReason reason) {
        super(what);
        this.profession = profession;
        this.reason = reason;
    }

    @NotNull
    @Override
    public Villager getEntity() {
        return (Villager) super.getEntity();
    }

    /**
     * Gets the future profession of the villager.
     *
     * @return The profession the villager will change to
     */
    @NotNull
    public Profession getProfession() {
        return profession;
    }

    /**
     * Sets the profession the villager will become from this event.
     *
     * @param profession new profession
     */
    public void setProfession(@NotNull Profession profession) {
        this.profession = profession;
    }

    /**
     * Gets the reason for why the villager's career is changing.
     *
     * @return Reason for villager's profession changing
     */
    @NotNull
    public ChangeReason getReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Reasons for the villager's profession changing.
     */
    public enum ChangeReason {

        /**
         * Villager lost their job due to too little experience.
         */
        LOSING_JOB,
        /**
         * Villager gained employment.
         */
        EMPLOYED;
    }
}
