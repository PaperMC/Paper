package org.bukkit.entity;

import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

public interface Firework extends Entity {

    /**
     * Get a copy of the fireworks meta
     *
     * @return A copy of the current Firework meta
     */
    @NotNull
    FireworkMeta getFireworkMeta();

    /**
     * Apply the provided meta to the fireworks
     *
     * @param meta The FireworkMeta to apply
     */
    void setFireworkMeta(@NotNull FireworkMeta meta);

    /**
     * Cause this firework to explode at earliest opportunity, as if it has no
     * remaining fuse.
     */
    void detonate();

    /**
     * Gets if the firework was shot at an angle (i.e. from a crossbow).
     *
     * A firework which was not shot at an angle will fly straight upwards.
     *
     * @return shot at angle status
     */
    boolean isShotAtAngle();

    /**
     * Sets if the firework was shot at an angle (i.e. from a crossbow).
     *
     * A firework which was not shot at an angle will fly straight upwards.
     *
     * @param shotAtAngle
     */
    void setShotAtAngle(boolean shotAtAngle);
}
