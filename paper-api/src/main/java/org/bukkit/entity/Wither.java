package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Wither boss
 */
public interface Wither extends Monster, Boss {

    /**
     * {@inheritDoc}
     * <p>
     * This method will set the target of the {@link Head#CENTER center head} of
     * the wither.
     *
     * @see #setTarget(org.bukkit.entity.Wither.Head, org.bukkit.entity.LivingEntity)
     */
    @Override
    void setTarget(@Nullable LivingEntity target);

    /**
     * This method will set the target of individual heads {@link Head} of the
     * wither.
     *
     * @param head the individual head
     * @param target the entity that should be targeted
     */
    void setTarget(@NotNull Head head, @Nullable LivingEntity target);

    /**
     * This method will get the target of individual heads {@link Head} of the
     * wither.
     *
     * @param head the individual head
     * @return the entity targeted by the given head, or null if none is
     * targeted
     */
    @Nullable
    LivingEntity getTarget(@NotNull Head head);

    /**
     * Represents one of the Wither's heads.
     */
    enum Head {

        CENTER,
        LEFT,
        RIGHT
    }
}
