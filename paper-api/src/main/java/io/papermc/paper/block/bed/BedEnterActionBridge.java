package io.papermc.paper.block.bed;

import java.util.Optional;
import java.util.ServiceLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
interface BedEnterActionBridge {

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

}
