package io.papermc.paper.block.bed;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
record BedEnterProblemImpl(
    Player.BedSleepingProblem vanillaProblem
) implements BedEnterProblem {
}
