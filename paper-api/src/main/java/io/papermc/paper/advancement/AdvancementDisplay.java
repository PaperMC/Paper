package io.papermc.paper.advancement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.util.Index;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Describes the display of an advancement.
 * <p>
 * The display is used in the chat, in the toast messages and the advancements
 * screen.
 */
@NullMarked
public interface AdvancementDisplay {

    /**
     * Gets the {@link Frame}.
     * <p>
     * This defines the appearance of the tile in the advancements screen and
     * the text when it's completed.
     *
     * @return the frame type
     */
    Frame frame();

    /**
     * Gets the advancement title.
     *
     * @return the title
     */
    Component title();

    /**
     * Gets the description.
     *
     * @return the description
     */
    Component description();

    /**
     * Gets the icon shown in the frame in the advancements screen.
     *
     * @return a copy of the icon
     */
    ItemStack icon();

    /**
     * Gets whether a toast should be displayed.
     * <p>
     * A toast is a notification that will be displayed in the top right corner
     * of the screen.
     *
     * @return {@code true} if a toast should be shown
     */
    boolean doesShowToast();

    /**
     * Gets whether a message should be sent in the chat.
     *
     * @return {@code true} if a message should be sent
     * @see org.bukkit.event.player.PlayerAdvancementDoneEvent#message() to edit
     * the message
     */
    boolean doesAnnounceToChat();

    /**
     * Gets whether this advancement is hidden.
     * <p>
     * Hidden advancements cannot be viewed by the player until they have been
     * unlocked.
     *
     * @return {@code true} if hidden
     */
    boolean isHidden();

    /**
     * Gets the texture displayed behind the advancement tree when selected.
     * <p>
     * This only affects root advancements without any parent. If the background
     * is not specified or doesn't exist, the tab background will be the missing
     * texture.
     *
     * @return the background texture path
     */
    @Nullable NamespacedKey backgroundPath();

    /**
     * Gets the formatted display name for this display. This
     * is a part of the component that would be shown in chat when a player
     * completes the advancement.
     *
     * @return the display name
     * @see org.bukkit.advancement.Advancement#displayName()
     */
    Component displayName();

    /**
     * Defines how the {@link #icon()} appears in the advancements screen and
     * the color used with the {@link #title() advancement name}.
     */
    enum Frame implements Translatable {

        /**
         * "Challenge complete" advancement.
         * <p>
         * The client will play the {@code ui.toast.challenge_complete} sound
         * when the challenge is completed and the toast is shown.
         */
        CHALLENGE("challenge", NamedTextColor.DARK_PURPLE),

        /**
         * "Goal reached" advancement.
         */
        GOAL("goal", NamedTextColor.GREEN),

        /**
         * "Advancement made" advancement.
         */
        TASK("task", NamedTextColor.GREEN);

        /**
         * The name map.
         */
        public static final Index<String, Frame> NAMES = Index.create(Frame.class, frame -> frame.name);
        private final String name;
        private final TextColor color;

        Frame(final String name, final TextColor color) {
            this.name = name;
            this.color = color;
        }

        /**
         * Gets the {@link TextColor} used for the advancement name.
         *
         * @return the text color
         */
        public TextColor color() {
            return this.color;
        }

        /**
         * Gets the translation key used when an advancement is completed.
         * <p>
         * This is the first line of the toast displayed by the client.
         *
         * @return the toast message key
         */
        @Override
        public String translationKey() {
            return "advancements.toast." + this.name;
        }
    }
}
