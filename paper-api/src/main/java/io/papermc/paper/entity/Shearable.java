package io.papermc.paper.entity;

import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an entity that can be sheared.
 */
@NullMarked
public interface Shearable extends Entity {

    /**
     * Forces the entity to be sheared and then play the effect as if it were sheared by a player.
     * This will cause the entity to be sheared, even if {@link Shearable#readyToBeSheared()} is false.
     * <p>
     * Some shearing behavior may cause the entity to no longer be valid
     * due to it being replaced by a different entity.
     */
    default void shear() {
        this.shear(Sound.Source.PLAYER);
    }

    /**
     * Forces the entity to be sheared and then play the effect as if it were sheared by the provided source.
     * This will cause the entity to be sheared, even if {@link Shearable#readyToBeSheared()} is false.
     * <p>
     * Some shearing behavior may cause the entity to no longer be valid
     * due to it being replaced by a different entity.
     * <p>
     * This simulates the behavior of an actual shearing, which may cause events like EntityTransformEvent to be called
     * for mooshrooms, and EntityDropItemEvent to be called for sheep.
     *
     * @param source Sound source to play any sound effects on
     */
    void shear(Sound.Source source);

    /**
     * Gets if the entity would be able to be sheared or not naturally using shears.
     *
     * @return if the entity can be sheared
     */
    boolean readyToBeSheared();
}
