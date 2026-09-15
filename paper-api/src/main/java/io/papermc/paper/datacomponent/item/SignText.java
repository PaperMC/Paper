package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Represents the lines of text on a sign side.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#SIGN_TEXT_FRONT
 * @see io.papermc.paper.datacomponent.DataComponentTypes#SIGN_TEXT_BACK
 */
@ApiStatus.NonExtendable
public interface SignText extends BuildableDataComponent<SignText, SignText.Builder> {

    @Contract(value = "_ -> new", pure = true)
    static SignText.Builder signText(final List<? extends ComponentLike> messages) {
        return ItemComponentTypesBridge.bridge().signText(messages);
    }

    @Contract(value = "-> new", pure = true)
    static SignText.Builder signText() {
        return ItemComponentTypesBridge.bridge().signText();
    }

    /**
     * Gets the lines on this sign side.
     *
     * @return the list of messages
     */
    @Contract(pure = true)
    List<Component> lines();

    /**
     * Gets the lines on this sign side, filtered for the player.
     *
     * @return the list of messages
     */
    @Contract(pure = true)
    List<Component> filteredLines();

    /**
     * Gets the color of the text on this sign side.
     *
     * @return the color of the text
     */
    @Contract(pure = true)
    DyeColor color();

    /**
     * Checks if the text on this sign side has a glowing effect.
     *
     * @return true if the text has a glowing effect, false otherwise
     */
    @Contract(pure = true)
    boolean hasGlowingText();

    /**
     * Builder for {@link SignText}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<SignText> {

        /**
         * Sets the lines on this sign side.
         *
         * @param messages the list of messages
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder lines(List<? extends ComponentLike> messages);

        /**
         * Sets a line at the specified index on this sign side.
         *
         * @param index the line index
         * @param message the message
         * @return the builder for chaining
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        Builder line(int index, ComponentLike message);

        /**
         * Adds a line on this sign side.
         *
         * @param message the message
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addLine(ComponentLike message);

        /**
         * Sets the color of the text on this sign side.
         *
         * @param color the color of the text
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder color(DyeColor color);

        /**
         * Sets whether the text on this sign side has a glowing effect.
         *
         * @param hasGlowingText {@code true} if the text has a glowing effect
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hasGlowingText(boolean hasGlowingText);
    }
}
