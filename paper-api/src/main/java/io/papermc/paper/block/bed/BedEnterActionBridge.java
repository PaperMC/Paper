package io.papermc.paper.block.bed;

import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.ApiStatus;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @hidden
 */
@ApiStatus.Internal
public interface BedEnterActionBridge {

    static BedEnterActionBridge instance() {
        final class Holder {
            static final Optional<BedEnterActionBridge> INSTANCE = ServiceLoader.load(BedEnterActionBridge.class, BedEnterActionBridge.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    BedEnterProblem createTooFarAwayProblem();

    BedEnterProblem createObstructedProblem();

    BedEnterProblem createNotSafeProblem();

    BedEnterProblem createExplosionProblem();

    BedEnterProblem createOtherProblem();

    @Deprecated(since = "1.21.11", forRemoval = true)
    BedEnterAction fromBedEnterResult(Player player, PlayerBedEnterEvent.BedEnterResult bedEnterResult);

    @Deprecated(since = "1.21.11", forRemoval = true)
    BedEnterAction fromFailReason(Player player, PlayerBedFailEnterEvent.FailReason failReason);

}
