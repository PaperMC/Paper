package org.bukkit.advancement;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information about how the advancement is displayed by the game.
 */
public interface AdvancementDisplay {

    /**
     * Gets the title of the advancement.
     *
     * @return The advancement title without colour codes.
     */
    @NotNull
    String getTitle();

    /**
     * Gets the visible description of the advancement.
     *
     * @return The advancement description without colour codes.
     */
    @NotNull
    String getDescription();

    /**
     * The icon that is used for this advancement.
     *
     * @return an ItemStack that represents the advancement.
     */
    @NotNull
    ItemStack getIcon();

    /**
     * Whether to show a toast to the player when this advancement has been
     * completed.
     *
     * @return true if a toast is shown.
     */
    boolean shouldShowToast();

    /**
     * Whether to announce in the chat when this advancement has been completed.
     *
     * @return true if announced in chat.
     */
    boolean shouldAnnounceChat();

    /**
     * Whether to hide this advancement and all its children from the
     * advancement screen until this advancement have been completed.
     *
     * Has no effect on root advancements themselves, but still affects all
     * their children.
     *
     * @return true if hidden.
     */
    boolean isHidden();

    /**
     * The X position of the advancement in the advancement screen.
     *
     * @return the X coordinate as float
     */
    float getX();

    /**
     * The Y position of the advancement in the advancement screen.
     *
     * @return the Y coordinate as float
     */
    float getY();

    /**
     * The display type of this advancement.
     *
     * @return an enum representing the type.
     */
    @NotNull
    AdvancementDisplayType getType();
}
