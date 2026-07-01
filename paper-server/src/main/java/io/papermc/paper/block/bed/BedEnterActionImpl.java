package io.papermc.paper.block.bed;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public record BedEnterActionImpl(
    BedRuleResult canSleep,
    BedRuleResult canSetSpawn,
    @Nullable BedEnterProblem problem,
    @Nullable Component errorMessage
) implements BedEnterAction {
}
