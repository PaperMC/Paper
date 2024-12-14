package org.bukkit.advancement;

import java.util.Collection;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an advancement that may be awarded to a player. This class is not
 * reference safe as the underlying advancement may be reloaded.
 *
 * @since 1.12
 */
public interface Advancement extends Keyed {

    /**
     * Get all the criteria present in this advancement.
     *
     * @return a unmodifiable copy of all criteria
     */
    @NotNull
    Collection<String> getCriteria();

    /**
     * Returns the requirements for this advancement.
     *
     * @return an AdvancementRequirements object.
     * @since 1.21.3
     */
    @NotNull
    AdvancementRequirements getRequirements();

    // Paper start
    /**
     * Get the display info of this advancement.
     * <p>
     * Will be {@code null} when totally hidden, for example with crafting
     * recipes.
     *
     * @return the display info
     * @since 1.17.1
     */
    @Nullable
    io.papermc.paper.advancement.AdvancementDisplay getDisplay();

    /**
     * Gets the formatted display name for this display. This
     * is part of the component that would be shown in chat when a player
     * completes the advancement. Will return the same as
     * {@link io.papermc.paper.advancement.AdvancementDisplay#displayName()} when an
     * {@link io.papermc.paper.advancement.AdvancementDisplay} is present.
     *
     * @return the display name
     * @see io.papermc.paper.advancement.AdvancementDisplay#displayName()
     * @since 1.19.2
     */
    @NotNull net.kyori.adventure.text.Component displayName();

    /**
     * Gets the parent advancement, if any.
     *
     * @return the parent advancement
     * @since 1.17.1
     */
    @Nullable
    Advancement getParent();

    /**
     * Gets all the direct children advancements.
     *
     * @return the children advancements
     * @since 1.17.1
     */
    @NotNull
    @org.jetbrains.annotations.Unmodifiable
    Collection<Advancement> getChildren();

    /**
     * Gets the root advancement of the tree this is located in.
     *
     * @return the root advancement
     * @since 1.17.1
     */
    @NotNull
    Advancement getRoot();
    // Paper end
}
