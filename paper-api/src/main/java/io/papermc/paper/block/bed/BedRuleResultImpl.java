package io.papermc.paper.block.bed;

import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
public record BedRuleResultImpl(
    TriState success,
    @Nullable String ruleId
) implements BedRuleResult {
}
