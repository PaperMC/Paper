package io.papermc.paper.util;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.scoreboard.numbers.NumberFormat;

public final class PaperScoreboardFormat {

    private PaperScoreboardFormat() {
    }

    public static net.minecraft.network.chat.numbers.NumberFormat asVanilla(final NumberFormat format) {
        final net.minecraft.network.chat.numbers.NumberFormat vanilla;
        if (format instanceof final io.papermc.paper.scoreboard.numbers.StyledFormat styled) {
            vanilla = new net.minecraft.network.chat.numbers.StyledFormat(PaperAdventure.asVanilla(styled.style()));
        } else if (format instanceof final io.papermc.paper.scoreboard.numbers.FixedFormat fixed) {
            vanilla = new net.minecraft.network.chat.numbers.FixedFormat(io.papermc.paper.adventure.PaperAdventure
                .asVanilla(fixed.component()));
        } else if (format.equals(NumberFormat.blank())) {
            vanilla = net.minecraft.network.chat.numbers.BlankFormat.INSTANCE;
        } else {
            throw new IllegalArgumentException("Unknown format type " + format.getClass());
        }

        return vanilla;
    }

    public static NumberFormat asPaper(final net.minecraft.network.chat.numbers.NumberFormat vanilla) {
        if (vanilla instanceof final net.minecraft.network.chat.numbers.StyledFormat styled) {
            return NumberFormat.styled(PaperAdventure.asAdventure(styled.style));
        } else if (vanilla instanceof final net.minecraft.network.chat.numbers.FixedFormat fixed) {
            return NumberFormat.fixed(io.papermc.paper.adventure.PaperAdventure.asAdventure(fixed.value));
        } else if (vanilla instanceof net.minecraft.network.chat.numbers.BlankFormat) {
            return NumberFormat.blank();
        }

        throw new IllegalArgumentException("Unknown format type " + vanilla.getClass());
    }
}
