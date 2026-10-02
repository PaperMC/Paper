package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record BedRuleResultImpl(
    boolean success
) implements BedRuleResult {
}
