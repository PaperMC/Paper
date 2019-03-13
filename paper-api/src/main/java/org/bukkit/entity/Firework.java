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
}
