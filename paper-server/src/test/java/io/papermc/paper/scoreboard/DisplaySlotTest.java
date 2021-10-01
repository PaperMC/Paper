package io.papermc.paper.scoreboard;

import org.bukkit.craftbukkit.scoreboard.CraftScoreboardTranslations;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Normal
public class DisplaySlotTest {

    @Test
    public void testBukkitToMinecraftDisplaySlots() {
        for (DisplaySlot bukkitSlot : DisplaySlot.values()) {
            assertNotNull(CraftScoreboardTranslations.fromBukkitSlot(bukkitSlot));
        }
    }

    @Test
    public void testMinecraftToBukkitDisplaySlots() {
        for (net.minecraft.world.scores.DisplaySlot nmsSlot : net.minecraft.world.scores.DisplaySlot.values()) {
            assertNotNull(CraftScoreboardTranslations.toBukkitSlot(nmsSlot));
        }
    }
}
