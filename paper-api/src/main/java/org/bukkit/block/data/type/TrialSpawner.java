package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 'trial_spawner_state' indicates the current operational phase of the spawner.
 */
@MinecraftExperimental
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

    public enum State {

        INACTIVE,
        WAITING_FOR_PLAYERS,
        ACTIVE,
        WAITING_FOR_REWARD_EJECTION,
        EJECTING_REWARD,
        COOLDOWN;
    }
}
