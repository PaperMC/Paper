package com.destroystokyo.paper.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents information about a targeted entity
 * @deprecated use {@link org.bukkit.util.RayTraceResult}
 */
@Deprecated(forRemoval = true, since = "1.19.3")
public class TargetEntityInfo {
    private final Entity entity;
    private final Vector hitVec;

    public TargetEntityInfo(@NotNull Entity entity, @NotNull Vector hitVec) {
        this.entity = entity;
        this.hitVec = hitVec;
    }

    /**
     * Get the entity that is targeted
     *
     * @return Targeted entity
     */
    @NotNull
    public Entity getEntity() {
        return entity;
    }

    /**
     * Get the position the entity is targeted at
     *
     * @return Targeted position
     */
    @NotNull
    public Vector getHitVector() {
        return hitVec;
    }
}
