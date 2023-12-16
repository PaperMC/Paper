package io.papermc.paper.scoreboard.numbers;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
record FixedFormatImpl(Component component) implements FixedFormat {

    @Override
    public Component asComponent() {
        return this.component();
    }
}
