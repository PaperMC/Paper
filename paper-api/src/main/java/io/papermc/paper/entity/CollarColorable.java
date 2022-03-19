package io.papermc.paper.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

/**
 * Entities that can have their collars colored.
 */
@NullMarked
public interface CollarColorable extends LivingEntity {

    /**
     * Get the collar color of this entity
     *
     * @return the color of the collar
     */
    DyeColor getCollarColor();

    /**
     * Set the collar color of this entity
     *
     * @param color the color to apply
     */
    void setCollarColor(DyeColor color);
}
