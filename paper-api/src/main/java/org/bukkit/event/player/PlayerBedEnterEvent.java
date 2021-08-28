package org.bukkit.event.player;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player is almost about to enter the bed.
 */
public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    /**
     * Represents the default possible outcomes of this event.
     */
    public enum BedEnterResult {
        /**
         * The player will enter the bed.
         */
        OK,
        /**
         * The world doesn't allow sleeping or saving the spawn point (eg,
         * Nether, The End or Custom Worlds). This is based on
         * {@link World#isBedWorks()} and {@link World#isNatural()}.
         *
         * Entering the bed is prevented and if {@link World#isBedWorks()} is
         * false then the bed explodes.
         */
        NOT_POSSIBLE_HERE,
        /**
         * Entering the bed is prevented due to it not being night nor
         * thundering currently.
         * <p>
         * If the event is forcefully allowed during daytime, the player will
         * enter the bed (and set its bed location), but might get immediately
         * thrown out again.
         */
        NOT_POSSIBLE_NOW,
        /**
         * Entering the bed is prevented due to the player being too far away.
         */
        TOO_FAR_AWAY,
        /**
         * Entering the bed is prevented due to there being monsters nearby.
         */
        NOT_SAFE,
        /**
         * Entering the bed is prevented due to there being some other problem.
         */
        OTHER_PROBLEM;
    }

    private static final HandlerList handlers = new HandlerList();
    private final Block bed;
    private final BedEnterResult bedEnterResult;
    private Result useBed = Result.DEFAULT;

    public PlayerBedEnterEvent(@NotNull Player who, @NotNull Block bed, @NotNull BedEnterResult bedEnterResult) {
        super(who);
        this.bed = bed;
        this.bedEnterResult = bedEnterResult;
    }

    @Deprecated
    public PlayerBedEnterEvent(@NotNull Player who, @NotNull Block bed) {
        this(who, bed, BedEnterResult.OK);
    }

    /**
     * This describes the default outcome of this event.
     *
     * @return the bed enter result representing the default outcome of this event
     */
    @NotNull
    public BedEnterResult getBedEnterResult() {
        return bedEnterResult;
    }

    /**
     * This controls the action to take with the bed that was clicked on.
     * <p>
     * In case of {@link org.bukkit.event.Event.Result#DEFAULT}, the default outcome is described by
     * {@link #getBedEnterResult()}.
     *
     * @return the action to take with the interacted bed
     * @see #setUseBed(org.bukkit.event.Event.Result)
     */
    @NotNull
    public Result useBed() {
        return useBed;
    }

    /**
     * Sets the action to take with the interacted bed.
     * <p>
     * {@link org.bukkit.event.Event.Result#ALLOW} will result in the player sleeping, regardless of
     * the default outcome described by {@link #getBedEnterResult()}.
     * <br>
     * {@link org.bukkit.event.Event.Result#DENY} will prevent the player from sleeping. This has the
     * same effect as canceling the event via {@link #setCancelled(boolean)}.
     * <br>
     * {@link org.bukkit.event.Event.Result#DEFAULT} will result in the outcome described by
     * {@link #getBedEnterResult()}.
     *
     * @param useBed the action to take with the interacted bed
     * @see #useBed()
     */
    public void setUseBed(@NotNull Result useBed) {
        this.useBed = useBed;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you want to
     * prevent the player from sleeping.
     * <p>
     * Canceling the event has the same effect as setting {@link #useBed()} to
     * {@link org.bukkit.event.Event.Result#DENY}.
     * <p>
     * For backwards compatibility reasons this also returns true if
     * {@link #useBed()} is {@link org.bukkit.event.Event.Result#DEFAULT} and the
     * {@link #getBedEnterResult() default action} is to prevent bed entering.
     *
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return (useBed == Result.DENY || useBed == Result.DEFAULT && bedEnterResult != BedEnterResult.OK);
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins.
     * <p>
     * Canceling this event will prevent use of the bed.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        setUseBed(cancel ? Result.DENY : useBed() == Result.DENY ? Result.DEFAULT : useBed());
    }

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    @NotNull
    public Block getBed() {
        return bed;
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
}
