package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the result of a bed rule during {@link org.bukkit.event.player.PlayerBedEnterEvent}
 * and {@link io.papermc.paper.event.player.PlayerBedFailEnterEvent}. Bed rules are responsible
 * for allowing players to sleep and to set their spawn point
 */
@ApiStatus.Experimental
public sealed interface BedRuleResult permits BedRuleResultImpl {

    /**
     * Used when the bed rule is allowed
     */
    BedRuleResult ALLOWED = new BedRuleResultImpl(true);

    /**
     * Used when the bed rule is denied due to there
     * being too much light. This is the case during
     * daytime without thunderstorms
     */
    BedRuleResult TOO_MUCH_LIGHT = new BedRuleResultImpl(false);

    /**
     * Used when the bed rule is set to always be denied
     */
    BedRuleResult NEVER = new BedRuleResultImpl(false);

    /**
     * Returns {@code true} if this result is a success
     *
     * @return whether this result is a success
     */
    boolean success();

}
