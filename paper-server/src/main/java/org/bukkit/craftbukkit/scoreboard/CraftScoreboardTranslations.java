package org.bukkit.craftbukkit.scoreboard;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

final class CraftScoreboardTranslations {
    static final int MAX_DISPLAY_SLOT = 19;
    static final ImmutableBiMap<DisplaySlot, String> SLOTS = ImmutableBiMap.<DisplaySlot, String>builder()
            .put(DisplaySlot.BELOW_NAME, "belowName")
            .put(DisplaySlot.PLAYER_LIST, "list")
            .put(DisplaySlot.SIDEBAR, "sidebar")
            .put(DisplaySlot.SIDEBAR_BLACK, "sidebar.team.black")
            .put(DisplaySlot.SIDEBAR_DARK_BLUE, "sidebar.team.dark_blue")
            .put(DisplaySlot.SIDEBAR_DARK_GREEN, "sidebar.team.dark_green")
            .put(DisplaySlot.SIDEBAR_DARK_AQUA, "sidebar.team.dark_aqua")
            .put(DisplaySlot.SIDEBAR_DARK_RED, "sidebar.team.dark_red")
            .put(DisplaySlot.SIDEBAR_DARK_PURPLE, "sidebar.team.dark_purple")
            .put(DisplaySlot.SIDEBAR_GOLD, "sidebar.team.gold")
            .put(DisplaySlot.SIDEBAR_GRAY, "sidebar.team.gray")
            .put(DisplaySlot.SIDEBAR_DARK_GRAY, "sidebar.team.dark_gray")
            .put(DisplaySlot.SIDEBAR_BLUE, "sidebar.team.blue")
            .put(DisplaySlot.SIDEBAR_GREEN, "sidebar.team.green")
            .put(DisplaySlot.SIDEBAR_AQUA, "sidebar.team.aqua")
            .put(DisplaySlot.SIDEBAR_RED, "sidebar.team.red")
            .put(DisplaySlot.SIDEBAR_LIGHT_PURPLE, "sidebar.team.light_purple")
            .put(DisplaySlot.SIDEBAR_YELLOW, "sidebar.team.yellow")
            .put(DisplaySlot.SIDEBAR_WHITE, "sidebar.team.white")
            .buildOrThrow();

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
