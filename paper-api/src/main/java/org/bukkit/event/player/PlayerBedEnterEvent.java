package org.bukkit.event.player;

import io.papermc.paper.block.bed.BedEnterAction;
import io.papermc.paper.block.bed.BedRuleResult;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player is almost about to enter the bed.
 */
public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block bed;
    private final BedEnterResult bedEnterResult;
    private final @NotNull BedEnterAction enterAction;
    private Result useBed = Result.DEFAULT;

    @ApiStatus.Internal
    public PlayerBedEnterEvent(@NotNull Player player, @NotNull Block bed, @NotNull BedEnterResult bedEnterResult, @NotNull BedEnterAction enterAction) {
        super(player);
        this.bed = bed;
        this.bedEnterResult = bedEnterResult;
        this.enterAction = enterAction;
    }

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    @NotNull
    public Block getBed() {
        return this.bed;
    }

    /**
     * This describes the default outcome of this event.
     *
     * @return the bed enter result representing the default outcome of this event
     * @deprecated This enum has been replaced with a system that better
     * represents how beds work. See {@link #enterAction}
     */
    @NotNull
    @ApiStatus.Obsolete(since = "1.21.11")
    public BedEnterResult getBedEnterResult() {
        return this.bedEnterResult;
    }

    /**
     * This describes the default outcome of this event.
     *
     * @return the action representing the default outcome of this event
     */
    @ApiStatus.Experimental
    public @NotNull BedEnterAction enterAction() {
        return this.enterAction;
    }

    /**
     * This controls the action to take with the bed that was clicked on.
     * <p>
     * In case of {@link Result#DEFAULT}, the default outcome is described by
     * {@link #enterAction()}.
     *
     * @return the action to take with the interacted bed
     * @see #setUseBed(Result)
     */
    @NotNull
    public Result useBed() {
        return this.useBed;
    }

    /**
     * Sets the action to take with the interacted bed.
     * <p>
     * {@link Result#ALLOW} will result in the player sleeping, regardless of
     * the default outcome described by {@link #enterAction()}.
     * <br>
     * {@link Result#DENY} will prevent the player from sleeping. This has the
     * same effect as canceling the event via {@link #setCancelled(boolean)}.
     * <br>
     * {@link Result#DEFAULT} will result in the outcome described by
     * {@link #enterAction()}.
     *
     * @param useBed the action to take with the interacted bed
     * @see #useBed()
     */
    public void setUseBed(@NotNull Result useBed) {
        this.useBed = useBed;
    }

    /**
     * Gets the cancellation state of this event. Set to {@code true} if you want to
     * prevent the player from sleeping.
     * <p>
     * Canceling the event has the same effect as setting {@link #useBed()} to
     * {@link Result#DENY}.
     * <p>
     * For backwards compatibility reasons this also returns {@code true} if
     * {@link #useBed()} is {@link Result#DEFAULT} and the
     * {@link #enterAction() default action} is to prevent bed entering.
     *
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return this.useBed == Result.DENY || this.useBed == Result.DEFAULT && this.enterAction.canSleep() != BedRuleResult.ALLOWED;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Canceling this event will prevent use of the bed.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.setUseBed(cancel ? Result.DENY : useBed() == Result.DENY ? Result.DEFAULT : useBed());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents the default possible outcomes of this event.
     *
     * @deprecated Enums no longer represents reliably how beds work and fail. This has been
     * replaced with {@link BedEnterAction} that better fits the new beds
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    public enum BedEnterResult {
        /**
         * The player will enter the bed.
         */
        OK,
        /**
         * The world doesn't allow sleeping (eg, Nether, The End or Custom Worlds), but
         * saving the spawn point may still be allowed. See {@link com.destroystokyo.paper.event.player.PlayerSetSpawnEvent}.
         * for spawn point changes. This is only called when sleeping isn't allowed and the bed
         * doesn't explode. When the bed explodes, {@link #EXPLOSION} is called instead.
         * <p>
         * Entering the bed is prevented
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
         * Bed was obstructed.
         */
        OBSTRUCTED,
        /**
         * Entering the bed is prevented due to there being monsters nearby.
         */
        NOT_SAFE,
        /**
         * Entering the bed is prevented due to there being some other problem.
         */
        OTHER_PROBLEM,
        /**
         * Entering the bed is prevented and the bed explodes.
         */
        EXPLOSION;
    }
}
