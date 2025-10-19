package io.papermc.paper.block.bed;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * An action type that represents the action that will happen after
 * {@link org.bukkit.event.player.PlayerBedEnterEvent} and that is
 * happening during {@link io.papermc.paper.event.player.PlayerBedFailEnterEvent}
 */
@ApiStatus.Experimental
public sealed interface BedEnterAction permits BedEnterActionImpl {

    /**
     * Gets if the player is allowed to sleep in the bed.
     * This can be {@link BedRuleResult#ALLOWED} at the same time as {@link #canSetSpawn()},
     * but will always be {@link BedRuleResult#UNDEFINED} if {@link #problem()} is anything but {@code null}
     *
     * @return whether the player is allowed to sleep
     */
    BedRuleResult canSleep();

    /**
     * Gets if the player is allowed to save its spawn point in the bed.
     * This can be {@link BedRuleResult#ALLOWED} at the same time as {@link #canSleep()},
     * but will always be {@link BedRuleResult#UNDEFINED} if {@link #problem()} is anything but {@code null}
     * <p>
     * Note: This can also be {@link BedRuleResult#UNDEFINED} at any time if the event containing
     * this action is called from sources external to Paper.
     *
     * @return whether the player is allowed to save its spawn point
     */
    BedRuleResult canSetSpawn();

    /**
     * A problem is an issue that prevents the player from sleeping and from saving its spawn point
     * <p>
     * When a problem was found, both {@link #canSleep()} and {@link #canSetSpawn()}
     * will be {@link BedRuleResult#UNDEFINED}
     *
     * @return any of {@link BedEnterProblem}s if one is found, otherwise {@code null}
     */
    @Nullable
    BedEnterProblem problem();

}
