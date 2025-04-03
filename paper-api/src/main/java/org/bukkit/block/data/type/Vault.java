package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

/**
 * 'vault_state' indicates the current operational phase of the vault block.
 * <br>
 * 'ominous' indicates if the block has ominous effects.
 */
public interface Vault extends Directional {

    /**
     * Gets the value of the 'vault_state' property.
     *
     * @return the 'vault_state' value
     */
    @NotNull
    State getVaultState();

    /**
     * Gets the value of the 'vault_state' property.
     *
     * @return the 'vault_state' value
     * @deprecated see {@link #getVaultState()}
     */
    @Deprecated(since = "1.21.3", forRemoval = true)
    @NotNull
    default State getTrialSpawnerState() {
        return this.getVaultState();
    }

    /**
     * Sets the value of the 'vault_state' property.
     *
     * @param state the new 'vault_state' value
     */
    void setVaultState(@NotNull State state);

    /**
     * Sets the value of the 'vault_state' property.
     *
     * @param state the new 'vault_state' value
     * @deprecated see {@link #setVaultState(State)}
     */
    @Deprecated(since = "1.21.3", forRemoval = true)
    default void setTrialSpawnerState(@NotNull State state) {
        this.setVaultState(state);
    }

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
