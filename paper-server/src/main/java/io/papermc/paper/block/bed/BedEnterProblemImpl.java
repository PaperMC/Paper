package io.papermc.paper.block.bed;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.Optionull;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
record BedEnterProblemImpl(Player.BedSleepingProblem sleepingProblem, @Nullable Component errorMessage) implements BedEnterProblem {

    BedEnterProblemImpl(final Player.BedSleepingProblem sleepingProblem) {
        this(
            sleepingProblem,
            Optionull.map(sleepingProblem.message(), PaperAdventure::asAdventure)
        );
    }
}
