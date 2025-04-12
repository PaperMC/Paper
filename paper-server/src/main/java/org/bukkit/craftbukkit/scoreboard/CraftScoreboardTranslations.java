package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

public final class CraftScoreboardTranslations {

    private CraftScoreboardTranslations() {}

    public static DisplaySlot toBukkitSlot(net.minecraft.world.scores.DisplaySlot minecraft) {
        return DisplaySlot.NAMES.value(minecraft.getSerializedName()); // Paper
    }

    public static net.minecraft.world.scores.DisplaySlot fromBukkitSlot(DisplaySlot slot) {
        return net.minecraft.world.scores.DisplaySlot.CODEC.byName(slot.getId()); // Paper
    }

    static RenderType toBukkitRender(ObjectiveCriteria.RenderType display) {
        return RenderType.valueOf(display.name());
    }

    static ObjectiveCriteria.RenderType fromBukkitRender(RenderType render) {
        return ObjectiveCriteria.RenderType.valueOf(render.name());
    }
}
