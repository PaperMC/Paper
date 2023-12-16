package io.papermc.paper.scoreboard.numbers;

import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.NullMarked;

@NullMarked
record StyledFormatImpl(Style style) implements StyledFormat {
    static final StyledFormat NO_STYLE = new StyledFormatImpl(Style.empty());

    @Override
    public void styleApply(final Style.Builder style) {
        style.merge(this.style);
    }
}
