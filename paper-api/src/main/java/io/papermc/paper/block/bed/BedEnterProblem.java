package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;

/**
 *
 */
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface BedEnterProblem {

    /**
     *
     */
    BedEnterProblem TOO_FAR_AWAY = BedEnterProblemBridge.instance().createTooFarAwayProblem();

    /**
     *
     */
    BedEnterProblem OBSTRUCTED = BedEnterProblemBridge.instance().createObstructedProblem();

    /**
     *
     */
    BedEnterProblem NOT_SAFE = BedEnterProblemBridge.instance().createMonstersNearbyProblem();

    /**
     *
     */
    BedEnterProblem EXPLOSION = BedEnterProblemBridge.instance().createExplosionProblem();

    /**
     *
     */
    BedEnterProblem OTHER = BedEnterProblemBridge.instance().createOtherProblem();

}
