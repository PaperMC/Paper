package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 'vault_state' indicates the current operational phase of the vault block.
 * <br>
 * 'ominous' indicates if the block has ominous effects.
 */
@MinecraftExperimental(Requires.UPDATE_1_21)
@ApiStatus.Experimental
public interface Vault extends Directional {

    /**
     * Gets the value of the 'vault_state' property.
     *
     * @return the 'vault_state' value
     */
    @NotNull
    State getTrialSpawnerState();

    /**
     * Sets the value of the 'vault_state' property.
     *
     * @param state the new 'vault_state' value
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
        ACTIVE,
        UNLOCKING,
        EJECTING
    }
}
