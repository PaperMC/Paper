package io.papermc.paper.block.bed;

import net.minecraft.world.entity.player.Player;

public class BedEnterProblemBridgeImpl implements BedEnterProblemBridge {

    @Override
    public BedEnterProblem createTooFarAwayProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.TOO_FAR_AWAY);
    }

    @Override
    public BedEnterProblem createObstructedProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.OBSTRUCTED);
    }

    @Override
    public BedEnterProblem createMonstersNearbyProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.NOT_SAFE);
    }

    @Override
    public BedEnterProblem createExplosionProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.EXPLOSION);
    }

    @Override
    public BedEnterProblem createOtherProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.OTHER_PROBLEM);
    }
}
