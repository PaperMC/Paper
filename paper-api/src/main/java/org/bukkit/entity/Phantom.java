package org.bukkit.entity;

import net.kyori.adventure.util.TriState;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

/**
 * Represents a phantom.
 */
public interface Phantom extends Flying, Enemy {

    /**
     * @return The size of the phantom
     */
    public int getSize();

    /**
     * @param size The new size of the phantom.
     */
    public void setSize(int size);

    /**
     * Get the UUID of the entity that caused this phantom to spawn
     *
     * @return UUID
     */
    @Nullable
    public UUID getSpawningEntity();

    /**
     * Check if this phantom will burn in the sunlight
     *
     * @return True if phantom will burn in sunlight
     * @deprecated All mobs can now be changed to burn in daylight, use the {@link org.bukkit.Tag#ENTITY_TYPES_BURN_IN_DAYLIGHT} tag and {@link Mob#getBurnInDaylightOverride()} instead.
     */
    @Deprecated(since = "26.1")
    boolean shouldBurnInDay();

    /**
     * Set if this phantom should burn in the sunlight
     *
     * @param shouldBurnInDay True to burn in sunlight
     * @deprecated All mobs can now be changed to burn in daylight, use {@link Mob#setBurnInDaylightOverride(TriState)} instead.
     */
    @Deprecated(since = "26.1")
    void setShouldBurnInDay(boolean shouldBurnInDay);

    /**
     * Gets the location that this phantom circles around when not attacking a player
     * This will be changed after attacking a player.
     *
     * @return circling location
     */
    @Nullable
    Location getAnchorLocation();

    /**
     * Sets the location that this phantom circles around when not attacking a player
     *
     * @param location circling location (world is ignored, will always use the entity's world)
     */
    void setAnchorLocation(@Nullable Location location);
}
