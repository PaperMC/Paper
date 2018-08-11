package org.bukkit;

import java.util.Map;
import java.util.TreeMap;
import net.minecraft.server.GameRules;
import org.junit.Assert;
import org.junit.Test;

public class GameRuleTest {

    @Test
    public void testBukkitRules() {
        GameRule<?>[] rules = GameRule.values();

        for (GameRule<?> rule : rules) {
            GameRule<?> registeredRule = GameRule.getByName(rule.getName());
            Assert.assertNotNull("Null GameRule", registeredRule);
            Assert.assertEquals("Invalid GameRule equality", rule, registeredRule);
        }
    }

    @Test
    public void testMinecraftRules() {
        TreeMap<String, GameRules.b> minecraftRules = GameRules.getGameRules();

        for (Map.Entry<String, GameRules.b> entry : minecraftRules.entrySet()) {
            GameRule<?> bukkitRule = GameRule.getByName(entry.getKey());

            Assert.assertNotNull(bukkitRule);
            Assert.assertEquals("Invalid GameRule Name", bukkitRule.getName(), entry.getKey());
        }
    }

    @Test(expected = NullPointerException.class)
    public void nullGameRuleName() {
        GameRule.getByName(null);
    }

    @Test
    public void emptyGameRuleName() {
        Assert.assertNull(GameRule.getByName(""));
    }

    @Test
    public void incorrectGameRuleName() {
        Assert.assertNull(GameRule.getByName("doAnnounceAdvancements"));
        Assert.assertNull(GameRule.getByName("sendCommandBlockFeedback"));
    }

    @Test
    public void invalidCasing() {
        Assert.assertNull(GameRule.getByName("CommandBlockOutput"));
        Assert.assertNull(GameRule.getByName("spAwnRadius"));
        Assert.assertNull(GameRule.getByName("rand0mTickSpeEd"));
    }
}
