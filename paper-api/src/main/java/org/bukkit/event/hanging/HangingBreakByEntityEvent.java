package org.bukkit.event.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when a hanging entity is removed by an entity
 */
public class HangingBreakByEntityEvent extends HangingBreakEvent {

    private final Entity remover;
    private final Entity directRemover; // Paper

    @ApiStatus.Internal
    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover) {
        this(hanging, remover, null, HangingBreakEvent.RemoveCause.ENTITY); // Paper
    }

    @ApiStatus.Internal
    public HangingBreakByEntityEvent(@NotNull final Hanging hanging, @NotNull final Entity remover, @Nullable Entity directRemover, @NotNull final HangingBreakEvent.RemoveCause cause) { // Slice
        super(hanging, cause);
        this.remover = remover;
        this.directRemover = directRemover; // Paper
    }

    /**
     * Gets the entity that removed the hanging entity.
     *
     * @return the entity that removed the hanging entity
     */
    @NotNull
    public Entity getRemover() {
        return this.remover;
    }

    // Paper start
    /**
     * Gets the {@link org.bukkit.damage.DamageSource#getDirectEntity()} entity that removed the hanging entity.
     * <p>
     * A good example of this is when a Player throws an Ender Pearl at an Item Frame. {@link #getRemover()}
     * will return the Player that threw the Ender Pearl, whereas {@link #getDirectRemover()} will return the Pearl.
     *
     * @return the entity that removed the hanging entity
     */
    @Nullable
    public Entity getDirectRemover() {
        return this.directRemover;
    }
    // Paper end
}
