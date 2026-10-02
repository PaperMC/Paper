package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
@Deprecated
public class GameRuleTest {

    @Test
    public void testBukkitRules() {
        for (GameRule<?> rule : Registry.GAME_RULE) {
            GameRule<?> registeredRule = GameRule.getByName(rule.getName());
            assertNotNull(registeredRule, "Null GameRule");
            assertEquals(rule, registeredRule, "Invalid GameRule equality");
        }
    }

    @Test
    public void nullGameRuleName() {
        assertThrows(IllegalArgumentException.class, () -> GameRule.getByName(null));
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
