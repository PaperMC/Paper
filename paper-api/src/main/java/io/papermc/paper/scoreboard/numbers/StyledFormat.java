package io.papermc.paper.scoreboard.numbers;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jspecify.annotations.NullMarked;

/**
 * A scoreboard number format that applies a custom formatting to the score number.
 */
@NullMarked
public interface StyledFormat extends NumberFormat, StyleBuilderApplicable {

    /**
     * The style that is being applied to the number in the score
     *
     * @return the style to apply
     */
    Style style();

}
