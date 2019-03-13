package org.bukkit.entity;

import org.jetbrains.annotations.Nullable;

public interface ShulkerBullet extends Projectile {

    /**
     * Retrieve the target of this bullet.
     *
     * @return the targeted entity
     */
    @Nullable
    Entity getTarget();

    /**
     * Sets the target of this bullet
     *
     * @param target the entity to target
     */
    void setTarget(@Nullable Entity target);
}
