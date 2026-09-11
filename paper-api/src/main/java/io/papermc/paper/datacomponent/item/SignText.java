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
 * Represents the text on a sign side.
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
     * Gets the list of messages on this sign side.
     *
     * @return the list of messages
     */
    @Contract(pure = true)
    List<Component> messages();

    /**
     * Gets the list of messages on this sign side, filtered for the player.
     *
     * @return the list of messages
     */
    @Contract(pure = true)
    List<Component> filteredMessages();

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
         * Sets the list of messages on this sign side.
         *
         * @param messages the list of messages
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder messages(List<? extends ComponentLike> messages);

        /**
         * Adds a message to the list of messages on this sign side.
         *
         * @param message the message to add
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addMessage(ComponentLike message);

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
         * @param hasGlowingText true if the text has a glowing effect, false otherwise
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hasGlowingText(boolean hasGlowingText);
    }
}
