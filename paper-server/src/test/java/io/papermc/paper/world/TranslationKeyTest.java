package io.papermc.paper.world;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.GameType;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Normal
public class TranslationKeyTest {

    @Test
    public void testDifficultyKeys() {
        for (Difficulty bukkitDifficulty : Difficulty.values()) {
            Assertions.assertEquals(((TranslatableContents) net.minecraft.world.Difficulty.byId(bukkitDifficulty.ordinal()).getDisplayName().getContents()).getKey(), bukkitDifficulty.translationKey(), bukkitDifficulty + "'s translation key doesn't match");
        }
    }

    @Test
    public void testFireworkEffectType() {
        for (final FireworkEffect.Type type : FireworkEffect.Type.values()) {
            final net.minecraft.world.item.component.FireworkExplosion.Shape nmsType = org.bukkit.craftbukkit.inventory.CraftMetaFirework.getNBT(type);
            Assertions.assertTrue(nmsType.getName().getContents() instanceof TranslatableContents, "contents aren't translatable");
            Assertions.assertEquals(((TranslatableContents) nmsType.getName().getContents()).getKey(), type.translationKey(), "translation key mismatch for " + type);
        }
    }

    @Test
    public void testGameMode() {
        for (GameType nms : GameType.values()) {
            GameMode bukkit = GameMode.getByValue(nms.getId());
            Assertions.assertNotNull(bukkit);
            Assertions.assertEquals(((TranslatableContents) nms.getLongDisplayName().getContents()).getKey(), bukkit.translationKey(), "translation key mismatch for " + bukkit);
        }
    }
}
