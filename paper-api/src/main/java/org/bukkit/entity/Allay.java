package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// An Allay.
public interface Allay extends Creature, InventoryHolder {

    /// Gets if the allay can duplicate.
    ///
    /// **Note:** Duplication is based when the
    /// [#getDuplicationCooldown] its lower than zero.
    ///
    /// @return if the allay can duplicate itself.
    public boolean canDuplicate();

    /// Sets if the allay can duplicate.
    ///
    /// **Note:** this value can be overridden later by
    /// [#getDuplicationCooldown] if is lower than zero. You can also use
    /// [#setDuplicationCooldown] to allow the allay to duplicate
    ///
    /// @param canDuplicate if the allay can duplicate itself
    public void setCanDuplicate(boolean canDuplicate);

    /// Gets the cooldown for duplicating the allay.
    ///
    /// @return the time in ticks when allay can duplicate
    public long getDuplicationCooldown();

    /// Sets the cooldown before the allay can duplicate again.
    ///
    /// @param cooldown the cooldown, use a negative number to deny allay to
    /// duplicate again.
    public void setDuplicationCooldown(long cooldown);

    /// Reset the cooldown for duplication.
    /// This will set the cooldown ticks to the same value as is set after an
    /// Allay has duplicated.
    public void resetDuplicationCooldown();

    /// Gets if the allay is dancing.
    ///
    /// @return `True` if it is dancing, false otherwise.
    public boolean isDancing();

    /// Causes the allay to start dancing because of the provided jukebox
    /// location.
    ///
    /// @param location the location of the jukebox
    ///
    /// @throws IllegalArgumentException if the block at the location is not a
    /// jukebox
    public void startDancing(@NotNull Location location);

    /// Force sets the dancing status of the allay.
    ///
    /// **Note:** This method forces the allay to dance, ignoring any nearby
    /// jukebox being required.
    public void startDancing();

    /// Makes the allay stop dancing.
    public void stopDancing();

    /// This make the current allay duplicate itself without dance or item
    /// necessary.
    /// **Note:** this will fire a [CreatureSpawnEvent]
    ///
    /// @return the new entity [Allay] or null if the spawn was cancelled
    @Nullable
    public Allay duplicateAllay();

    /// Gets the jukebox the allay is set to dance to.
    ///
    /// @return the location of the jukebox to dance if it exists
    @Nullable
    public Location getJukebox();
}
