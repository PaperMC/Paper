package io.papermc.paper.event.player;

import io.papermc.paper.block.bed.BedEnterAction;
import io.papermc.paper.block.bed.BedEnterActionImpl;
import io.papermc.paper.block.bed.BedEnterProblem;
import io.papermc.paper.block.bed.BedRuleResult;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PlayerBedFailEnterEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Deprecated(since = "1.21.11") FailReason failReason;
    private final Block bed;
    private final BedEnterAction enterAction;
    private boolean willExplode;
    private @Nullable Component message;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerBedFailEnterEvent(final Player player, final FailReason failReason, final Block bed, final boolean willExplode, final @Nullable Component message, BedEnterAction enterAction) {
        super(player);
        this.failReason = failReason;
        this.bed = bed;
        this.enterAction = enterAction;
        this.willExplode = willExplode;
        this.message = message;
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.21.11")
    public PlayerBedFailEnterEvent(final Player player, final FailReason failReason, final Block bed, final boolean willExplode, final @Nullable Component message) {
        this(player, failReason, bed, willExplode, message, PlayerBedFailEnterEvent.fromFailReason(failReason));
    }

    // This is what we have to do for backwards compatibility...
    private static BedEnterAction fromFailReason(FailReason failReason) {
        return switch (failReason) {
            case NOT_POSSIBLE_HERE -> new BedEnterActionImpl(BedRuleResult.NEVER, BedRuleResult.UNDEFINED, null);
            case NOT_POSSIBLE_NOW -> new BedEnterActionImpl(BedRuleResult.TOO_MUCH_LIGHT, BedRuleResult.UNDEFINED, null);
            case TOO_FAR_AWAY -> new BedEnterActionImpl(BedRuleResult.UNDEFINED, BedRuleResult.UNDEFINED, BedEnterProblem.TOO_FAR_AWAY);
            case OBSTRUCTED -> new BedEnterActionImpl(BedRuleResult.UNDEFINED, BedRuleResult.UNDEFINED, BedEnterProblem.OBSTRUCTED);
            case NOT_SAFE -> new BedEnterActionImpl(BedRuleResult.UNDEFINED, BedRuleResult.UNDEFINED, BedEnterProblem.NOT_SAFE);
            case OTHER_PROBLEM -> new BedEnterActionImpl(BedRuleResult.UNDEFINED, BedRuleResult.UNDEFINED, BedEnterProblem.OTHER);
            case EXPLOSION -> new BedEnterActionImpl(BedRuleResult.UNDEFINED, BedRuleResult.UNDEFINED, BedEnterProblem.EXPLOSION);
        };
    }

    @Deprecated(since = "1.21.11")
    public FailReason getFailReason() {
        return this.failReason;
    }

    /**
     * This describes the default outcome of this event.
     *
     * @return the action representing the default outcome of this event
     */
    public BedEnterAction enterAction() {
        return this.enterAction;
    }

    public Block getBed() {
        return this.bed;
    }

    public boolean getWillExplode() {
        return this.willExplode;
    }

    public void setWillExplode(final boolean willExplode) {
        this.willExplode = willExplode;
    }

    public @Nullable Component getMessage() {
        return this.message;
    }

    public void setMessage(final @Nullable Component message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>NOTE</b>: This does not cancel the player getting in the bed, but any messages/explosions
     * that may occur because of the interaction.
     */
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

    public enum FailReason {
        /**
         * The world doesn't allow sleeping (ex. Nether or The End). Entering
         * the bed is prevented but the bed doesn't explode. When the bed
         * explodes, {@link #EXPLOSION} is used instead.
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
         * Bed is obstructed.
         */
        OBSTRUCTED,
        /**
         * Entering the bed is prevented due to there being some other problem.
         */
        OTHER_PROBLEM,
        /**
         * Entering the bed is prevented due to there being monsters nearby.
         */
        NOT_SAFE,
        /**
         * Entering the bed is prevented and the bed explodes.
         */
        EXPLOSION
    }
}
