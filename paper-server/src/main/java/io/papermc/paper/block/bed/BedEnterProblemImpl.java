package io.papermc.paper.block.bed;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
record BedEnterProblemImpl(
    Player.BedSleepingProblem vanillaProblem,
    @Nullable Component errorMessage
) implements BedEnterProblem {

    BedEnterProblemImpl(Player.BedSleepingProblem vanillaProblem) {
        this(
            vanillaProblem,
            vanillaProblem.message() == null ? null : PaperAdventure.asAdventure(vanillaProblem.message())
        );
    }
}
