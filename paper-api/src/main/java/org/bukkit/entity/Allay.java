package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An Allay.
 *
 * @since 1.19
 */
public interface Allay extends Creature, InventoryHolder {

    /**
     * Gets if the allay can duplicate.
     * <br>
     * <b>Note:</b> Duplication is based when the
     * {@link #getDuplicationCooldown} its lower than zero.
     *
     * @return if the allay can duplicate itself.
     * @since 1.19.2
     */
    public boolean canDuplicate();

    /**
     * Sets if the allay can duplicate.
     * <br>
     * <b>Note:</b> this value can be overridden later by
     * {@link #getDuplicationCooldown} if is lower than zero. You can also use
     * {@link #setDuplicationCooldown} to allow the allay to duplicate
     *
     * @param canDuplicate if the allay can duplicate itself
     * @since 1.19.2
     */
    public void setCanDuplicate(boolean canDuplicate);

    /**
     * Gets the cooldown for duplicating the allay.
     *
     * @return the time in ticks when allay can duplicate
     * @since 1.19.2
     */
    public long getDuplicationCooldown();

    /**
     * Sets the cooldown before the allay can duplicate again.
     *
     * @param cooldown the cooldown, use a negative number to deny allay to
     * duplicate again.
     * @since 1.19.2
     */
    public void setDuplicationCooldown(long cooldown);

    /**
     * Reset the cooldown for duplication.
     *
     * This will set the cooldown ticks to the same value as is set after an
     * Allay has duplicated.
     *
     * @since 1.19.2
     */
    public void resetDuplicationCooldown();

    /**
     * Gets if the allay is dancing.
     *
     * @return {@code True} if it is dancing, false otherwise.
     * @since 1.19.2
     */
    public boolean isDancing();

    /**
     * Causes the allay to start dancing because of the provided jukebox
     * location.
     *
     * @param location the location of the jukebox
     *
     * @throws IllegalArgumentException if the block at the location is not a
     * jukebox
     * @since 1.19.2
     */
    public void startDancing(@NotNull Location location);

    /**
     * Force sets the dancing status of the allay.
     * <br>
     * <b>Note:</b> This method forces the allay to dance, ignoring any nearby
     * jukebox being required.
     *
     * @since 1.19.2
     */
    public void startDancing();

    /**
     * Makes the allay stop dancing.
     *
     * @since 1.19.2
     */
    public void stopDancing();

    /**
     * This make the current allay duplicate itself without dance or item
     * necessary.
     * <b>Note:</b> this will fire a {@link CreatureSpawnEvent}
     *
     * @return the new entity {@link Allay} or null if the spawn was cancelled
     * @since 1.19.2
     */
    @Nullable
    public Allay duplicateAllay();

    /**
     * Gets the jukebox the allay is set to dance to.
     *
     * @return the location of the jukebox to dance if it exists
     * @since 1.19.2
     */
    @Nullable
    public Location getJukebox();
}
