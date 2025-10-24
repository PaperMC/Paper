package io.papermc.paper.block.bed;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Represents a problem that prevents a player from continuing
 * the sleeping process, preventing it from sleeping and from setting
 * its spawn point
 */
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface BedEnterProblem {

    /**
     * Happens when the bed is too far away from the player.
     * Makes no explosion and has an error message
     *
     * @see #errorMessage()
     */
    BedEnterProblem TOO_FAR_AWAY = BedEnterActionBridge.instance().createTooFarAwayProblem();

    /**
     * Happens when the bed has blocks one block above it.
     * Makes no explosion and has an error message
     *
     * @see #errorMessage()
     */
    BedEnterProblem OBSTRUCTED = BedEnterActionBridge.instance().createObstructedProblem();

    /**
     * Happens when there are monsters in a 16x10x16 box centered on the bed's head
     * and the player is not in creative mode. Makes no explosion and has an error message
     *
     * @see #errorMessage()
     */
    BedEnterProblem NOT_SAFE = BedEnterActionBridge.instance().createNotSafeProblem();

    /**
     * Happens when the bed is set to explode. This is defined in the environment attributes of the world.
     * This doesn't have a fixed error message, see {@link BedEnterAction#errorMessage()}
     */
    BedEnterProblem EXPLOSION = BedEnterActionBridge.instance().createExplosionProblem();

    /**
     * Happens when a player tries to sleep when an invalid state, for example when a player tries
     * to sleep but is already sleeping or is dead.
     * This doesn't have an error message at all, the interaction is simply discarded
     */
    BedEnterProblem OTHER = BedEnterActionBridge.instance().createOtherProblem();

    /**
     * Returns the error message associated with this problem, if any.
     * This component is sent to the client as an actionbar message
     * when this problem occur
     *
     * @return the error message
     */
    @Nullable
    Component errorMessage();

}
