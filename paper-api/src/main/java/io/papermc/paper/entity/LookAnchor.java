package io.papermc.paper.entity;

import io.papermc.paper.math.Position;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Represents what part of the entity should be used when determining where to face a position/entity.
 *
 * @see org.bukkit.entity.Player#lookAt(Position, LookAnchor)
 * @see org.bukkit.entity.Player#lookAt(Entity, LookAnchor, LookAnchor)
 */
public enum LookAnchor {
    /**
     * Represents the entity's feet.
     * @see LivingEntity#getLocation()
     */
    FEET,
    /**
     * Represents the entity's eyes.
     * @see LivingEntity#getEyeLocation()
     */
    EYES;
}
