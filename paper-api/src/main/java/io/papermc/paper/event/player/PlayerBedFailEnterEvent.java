package io.papermc.paper.event.player;

import io.papermc.paper.block.bed.BedEnterAction;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PlayerBedFailEnterEvent extends PlayerEventNew, Cancellable { // todo javadocs?

    /**
     * @deprecated This enum has been replaced with a system that better
     * represents how beds work. See {@link #enterAction}
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    FailReason getFailReason();

    /**
     * This describes the default outcome of this event.
     *
     * @return the action representing the default outcome of this event
     */
    @ApiStatus.Experimental
    BedEnterAction enterAction();

    Block getBed();

    boolean getWillExplode();

    void setWillExplode(boolean willExplode);

    @Nullable Component getMessage();

    void setMessage(@Nullable Component message);

    /**
     * {@inheritDoc}
     * <p>
     * <b>NOTE</b>: This does not cancel the player getting in the bed, but any messages/explosions
     * that may occur because of the interaction.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * @deprecated Enums no longer represents reliably how beds work and fail. This has been
     * replaced with {@link BedEnterAction} that better fits the new beds
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    enum FailReason {
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
