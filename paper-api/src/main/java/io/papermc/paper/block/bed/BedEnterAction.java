package io.papermc.paper.block.bed;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * An action type that represents the action that will happen after
 * {@link org.bukkit.event.player.PlayerBedEnterEvent} and that is
 * happening during {@link io.papermc.paper.event.player.PlayerBedFailEnterEvent}
 */
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface BedEnterAction {

    /**
     * Gets if the player is allowed to sleep in the bed.
     * This can be {@link BedRuleResult#ALLOWED} at the same time as {@link #canSetSpawn()}
     * <p>
     * It is advised to check for {@link BedEnterProblem}s first, as if it returns anything but {@code null}
     * the bed interaction is prevented
     *
     * @return whether the player is allowed to sleep
     */
    BedRuleResult canSleep();

    /**
     * Gets if the player is allowed to save its spawn point in the bed.
     * This can be {@link BedRuleResult#ALLOWED} at the same time as {@link #canSleep()}
     * <p>
     * It is advised to check for {@link BedEnterProblem}s first, as if it returns anything but {@code null}
     * the bed interaction is prevented
     *
     * @return whether the player is allowed to save its spawn point
     */
    BedRuleResult canSetSpawn();

    /**
     * A problem is an issue that prevents the player from sleeping and from saving its spawn point.
     * No problem being found doesn't mean the player successfully slept or set its spawn point,
     * see {@link #canSleep()} and {@link #canSetSpawn()} for individual successes
     *
     * @return any of {@link BedEnterProblem}s if one is found, otherwise {@code null}
     */
    @Nullable
    BedEnterProblem problem();

    /**
     * Returns the default error message to be shown as an actionbar message to the player
     *
     * @return the error message
     */
    @Nullable
    Component errorMessage();

}
