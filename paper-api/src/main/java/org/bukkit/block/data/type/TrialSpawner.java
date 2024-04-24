package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 'trial_spawner_state' indicates the current operational phase of the spawner.
  * <br>
 * 'ominous' indicates if the block has ominous effects.
 */
@MinecraftExperimental(Requires.UPDATE_1_21)
@ApiStatus.Experimental
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
     */
    boolean isOminous();

    /**
     * Sets the value of the 'ominous' property.
     *
     * @param ominous the new 'ominous' value
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
