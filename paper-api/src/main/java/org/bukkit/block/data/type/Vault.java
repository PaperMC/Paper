package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

/// 'vault\_state' indicates the current operational phase of the vault block.
///
/// 'ominous' indicates if the block has ominous effects.
public interface Vault extends Directional {

    /// Gets the value of the 'vault\_state' property.
    ///
    /// @return the 'vault\_state' value
    @NotNull
    State getVaultState();

    /// Gets the value of the 'vault\_state' property.
    ///
    /// @return the 'vault\_state' value
    /// @deprecated see [#getVaultState()]
    @Deprecated(since = "1.21.3", forRemoval = true)
    @NotNull
    default State getTrialSpawnerState() {
        return this.getVaultState();
    }

    /// Sets the value of the 'vault\_state' property.
    ///
    /// @param state the new 'vault\_state' value
    void setVaultState(@NotNull State state);

    /// Sets the value of the 'vault\_state' property.
    ///
    /// @param state the new 'vault\_state' value
    /// @deprecated see [#setVaultState(State)]
    @Deprecated(since = "1.21.3", forRemoval = true)
    default void setTrialSpawnerState(@NotNull State state) {
        this.setVaultState(state);
    }

    /// Gets the value of the 'ominous' property.
    ///
    /// @return the 'ominous' value
    boolean isOminous();

    /// Sets the value of the 'ominous' property.
    ///
    /// @param ominous the new 'ominous' value
    void setOminous(boolean ominous);

    public enum State {

        INACTIVE,
        ACTIVE,
        UNLOCKING,
        EJECTING
    }
}
