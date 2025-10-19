package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;

/**
 * @hidden
 */
@ApiStatus.Internal
public record BedRuleResultImpl(
    boolean success
) implements BedRuleResult {
}
