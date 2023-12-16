package io.papermc.paper.scoreboard.numbers;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jspecify.annotations.NullMarked;

/**
 * Describes a scoreboard number format that applies custom formatting to scoreboard scores.
 */
@NullMarked
public interface NumberFormat {

    /**
     * Creates a blank scoreboard number format that removes the score number entirely.
     *
     * @return a blank number format
     */
    static NumberFormat blank() {
        return BlankFormatImpl.INSTANCE;
    }

    /**
     * Gets an un-styled number format.
     *
     * @return an un-styled number format
     */
    static StyledFormat noStyle() {
        return StyledFormatImpl.NO_STYLE;
    }

    /**
     * Creates a scoreboard number format that applies a custom formatting to the score number.
     *
     * @param style the style to apply on the number
     * @return a styled number format
     */
    static StyledFormat styled(final Style style) {
        return new StyledFormatImpl(style);
    }

    /**
     * Creates a scoreboard number format that applies a custom formatting to the score number.
     *
     * @param styleBuilderApplicables the style to apply on the number
     * @return a styled number format
     */
    static StyledFormat styled(final StyleBuilderApplicable... styleBuilderApplicables) {
        return styled(Style.style(styleBuilderApplicables));
    }

    /**
     * Creates a scoreboard number format that replaces the score number with a chat component.
     *
     * @param component the component to replace the number with
     * @return a fixed number format
     */
    static FixedFormat fixed(final ComponentLike component) {
        return new FixedFormatImpl(component.asComponent());
    }
}
