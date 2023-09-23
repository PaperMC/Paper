package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import net.minecraft.world.level.GameRules;
import org.bukkit.craftbukkit.CraftWorld;
import org.junit.jupiter.api.Test;

public class GameRuleTest {

    @Test
    public void testBukkitRules() {
        GameRule<?>[] rules = GameRule.values();

        for (GameRule<?> rule : rules) {
            GameRule<?> registeredRule = GameRule.getByName(rule.getName());
            assertNotNull(registeredRule, "Null GameRule");
            assertEquals(rule, registeredRule, "Invalid GameRule equality");
        }
    }

    @Test
    public void testMinecraftRules() {
        Map<String, GameRules.GameRuleKey<?>> minecraftRules = CraftWorld.getGameRulesNMS();

        for (Map.Entry<String, GameRules.GameRuleKey<?>> entry : minecraftRules.entrySet()) {
            GameRule<?> bukkitRule = GameRule.getByName(entry.getKey());

            assertNotNull(bukkitRule, "Missing " + entry.getKey());
            assertEquals(bukkitRule.getName(), entry.getKey(), "Invalid GameRule Name");
        }
    }

    @Test
    public void nullGameRuleName() {
        assertThrows(NullPointerException.class, () -> GameRule.getByName(null));
    }

    @Test
    public void emptyGameRuleName() {
        assertNull(GameRule.getByName(""));
    }

    @Test
    public void incorrectGameRuleName() {
        assertNull(GameRule.getByName("doAnnounceAdvancements"));
        assertNull(GameRule.getByName("sendCommandBlockFeedback"));
    }

    @Test
    public void invalidCasing() {
        assertNull(GameRule.getByName("CommandBlockOutput"));
        assertNull(GameRule.getByName("spAwnRadius"));
        assertNull(GameRule.getByName("rand0mTickSpeEd"));
    }
}
