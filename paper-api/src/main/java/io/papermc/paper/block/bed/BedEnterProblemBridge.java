package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @hidden
 */
@ApiStatus.Internal
public interface BedEnterProblemBridge {

    static BedEnterProblemBridge instance() {
        final class Holder {
            static final Optional<BedEnterProblemBridge> INSTANCE = ServiceLoader.load(BedEnterProblemBridge.class, BedEnterProblemBridge.class.getClassLoader()).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

    BedEnterProblem createTooFarAwayProblem();

    BedEnterProblem createObstructedProblem();

    BedEnterProblem createMonstersNearbyProblem();

    BedEnterProblem createExplosionProblem();

    BedEnterProblem createOtherProblem();

}
