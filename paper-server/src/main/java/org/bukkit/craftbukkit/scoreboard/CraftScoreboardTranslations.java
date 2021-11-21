package org.bukkit.craftbukkit.scoreboard;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

final class CraftScoreboardTranslations {
    static final int MAX_DISPLAY_SLOT = 3;
    static ImmutableBiMap<DisplaySlot, String> SLOTS = ImmutableBiMap.of(
            DisplaySlot.BELOW_NAME, "belowName",
            DisplaySlot.PLAYER_LIST, "list",
            DisplaySlot.SIDEBAR, "sidebar");

    private CraftScoreboardTranslations() {}

    static DisplaySlot toBukkitSlot(int i) {
        return SLOTS.inverse().get(Scoreboard.getDisplaySlotName(i));
    }

    static int fromBukkitSlot(DisplaySlot slot) {
        return Scoreboard.getDisplaySlotByName(SLOTS.get(slot));
    }

    static RenderType toBukkitRender(IScoreboardCriteria.EnumScoreboardHealthDisplay display) {
        return RenderType.valueOf(display.name());
    }

    static IScoreboardCriteria.EnumScoreboardHealthDisplay fromBukkitRender(RenderType render) {
        return IScoreboardCriteria.EnumScoreboardHealthDisplay.valueOf(render.name());
    }
}
