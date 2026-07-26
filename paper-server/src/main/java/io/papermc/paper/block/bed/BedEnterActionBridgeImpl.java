package io.papermc.paper.block.bed;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BedEnterActionBridgeImpl implements BedEnterActionBridge {

    @Override
    public BedEnterProblem createTooFarAwayProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.TOO_FAR_AWAY);
    }

    @Override
    public BedEnterProblem createObstructedProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.OBSTRUCTED);
    }

    @Override
    public BedEnterProblem createNotSafeProblem() {
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
