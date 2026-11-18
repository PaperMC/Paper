package com.destroystokyo.paper.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Represents information about a targeted entity.
 *
 * @deprecated use {@link org.bukkit.util.RayTraceResult}
 */
@Deprecated(forRemoval = true, since = "1.19.3")
public class TargetEntityInfo {
    private final Entity entity;
    private final Vector hitVec;

    public TargetEntityInfo(final Entity entity, final Vector hitVec) {
        this.entity = entity;
        this.hitVec = hitVec;
    }

    /**
     * Get the entity that is targeted.
     *
     * @return Targeted entity
     */
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Get the position the entity is targeted at.
     *
     * @return Targeted position
     */
    public Vector getHitVector() {
        return this.hitVec;
    }
}
