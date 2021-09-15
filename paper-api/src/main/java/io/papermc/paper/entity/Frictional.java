package io.papermc.paper.entity;

import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an {@link Entity} that can experience friction with the air and ground.
 */
@NullMarked
public interface Frictional {

    /**
     * Gets the friction state of this entity.
     * When set to {@link TriState#TRUE}, the entity will always experience friction.
     * When set to {@link TriState#FALSE}, the entity will never experience friction.
     * When set to {@link TriState#NOT_SET}, the entity will fall back to Minecraft's default behaviour.
     *
     * @return the entity's friction state
     */
    TriState getFrictionState();

    /**
     * Sets the friction state of this entity.
     * When set to {@link TriState#TRUE}, the entity will always experience friction.
     * When set to {@link TriState#FALSE}, the entity will never experience friction.
     * When set to {@link TriState#NOT_SET}, the entity will fall back to Minecraft's default behaviour.
     * <p>
     * Please note that changing this value will do nothing for a player.
     *
     * @param state the new friction state to set for the entity
     */
    void setFrictionState(TriState state);

}
