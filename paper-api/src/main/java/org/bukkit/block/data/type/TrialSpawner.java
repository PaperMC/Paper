package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * 'trial_spawner_state' indicates the current operational phase of the spawner.
  * <br>
 * 'ominous' indicates if the block has ominous effects.
 *
 * @since 1.20.4
 */
public interface TrialSpawner extends BlockData {

    /**
     * Gets the value of the 'trial_spawner_state' property.
     *
     * @return the 'trial_spawner_state' value
     */
    @NotNull
    State getTrialSpawnerState();

    /**
     * Sets the value of the 'trial_spawner_state' property.
     *
     * @param state the new 'trial_spawner_state' value
     */
    void setTrialSpawnerState(@NotNull State state);

    /**
     * Gets the value of the 'ominous' property.
     *
     * @return the 'ominous' value
     * @since 1.20.6
     */
    boolean isOminous();

    /**
     * Sets the value of the 'ominous' property.
     *
     * @param ominous the new 'ominous' value
     * @since 1.20.6
     */
    void setOminous(boolean ominous);

    public enum State {

        INACTIVE,
        WAITING_FOR_PLAYERS,
        ACTIVE,
        WAITING_FOR_REWARD_EJECTION,
        EJECTING_REWARD,
        COOLDOWN;
    }
}
