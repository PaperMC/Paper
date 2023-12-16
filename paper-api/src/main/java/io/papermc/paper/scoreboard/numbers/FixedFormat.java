package io.papermc.paper.scoreboard.numbers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

/**
 * A scoreboard number format that replaces the score number with a chat component.
 */
@NullMarked
public interface FixedFormat extends NumberFormat, ComponentLike {

    /**
     * The component shown instead of the number for a score
     *
     * @return the chat component
     */
    Component component();

}
