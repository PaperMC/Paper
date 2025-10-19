package io.papermc.paper.block.bed;

import org.jspecify.annotations.Nullable;

public record BedEnterActionImpl(
    BedRuleResult canSleep,
    BedRuleResult canSetSpawn,
    @Nullable BedEnterProblem problem
) implements BedEnterAction {
}
