package io.papermc.paper.block.bed;

import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;

/**
 *
 */
@ApiStatus.Experimental
public sealed interface BedRuleResult permits BedRuleResultImpl {

    /**
     *
     */
    BedRuleResult ALLOWED = new BedRuleResultImpl(TriState.TRUE, null);

    /**
     *
     */
    BedRuleResult TOO_MUCH_LIGHT = new BedRuleResultImpl(TriState.FALSE, "when_dark");

    /**
     *
     */
    BedRuleResult NEVER = new BedRuleResultImpl(TriState.FALSE, "never");

    /**
     *
     */
    BedRuleResult UNDEFINED = new BedRuleResultImpl(TriState.NOT_SET, null);

    /**
     * Returns {@link TriState#TRUE} if this result is a success, or {@link TriState#NOT_SET} when {@link #UNDEFINED}
     *
     * @return whether this result is a success
     */
    TriState success();

}
