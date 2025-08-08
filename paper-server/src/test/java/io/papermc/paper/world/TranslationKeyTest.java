package io.papermc.paper.world;

import com.destroystokyo.paper.ClientOption;
import java.util.Map;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@AllFeatures
public class TranslationKeyTest {

    @Test
    public void testChatVisibilityKeys() {
        for (ClientOption.ChatVisibility chatVisibility : ClientOption.ChatVisibility.values()) {
            if (chatVisibility == ClientOption.ChatVisibility.UNKNOWN) continue;
            Assertions.assertEquals(ChatVisiblity.valueOf(chatVisibility.name()).getKey(), chatVisibility.translationKey(), chatVisibility + "'s translation key doesn't match");
        }
    }

    @Test
    public void testParticleVisibilityKeys() {
        for (ClientOption.ParticleVisibility particleVisibility : ClientOption.ParticleVisibility.values()) {
            Assertions.assertEquals(ParticleStatus.valueOf(particleVisibility.name()).getKey(), particleVisibility.translationKey(), particleVisibility + "'s translation key doesn't match");
        }
    }

    @Test
    public void testDifficultyKeys() {
        for (Difficulty bukkitDifficulty : Difficulty.values()) {
            Assertions.assertEquals(((TranslatableContents) net.minecraft.world.Difficulty.byId(bukkitDifficulty.ordinal()).getDisplayName().getContents()).getKey(), bukkitDifficulty.translationKey(), bukkitDifficulty + "'s translation key doesn't match");
        }
    }

    @Test
    public void testGameruleKeys() {
        final Map<String, GameRules.Key<?>> gameRules = CraftWorld.getGameRulesNMS(new GameRules(FeatureFlags.REGISTRY.allFlags()));
        for (GameRule<?> rule : GameRule.values()) {
            Assertions.assertEquals(gameRules.get(rule.getName()).getDescriptionId(), rule.translationKey(), rule.getName() + "'s translation doesn't match");
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
