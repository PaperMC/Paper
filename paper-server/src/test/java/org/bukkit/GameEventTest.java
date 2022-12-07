package org.bukkit;

import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class GameEventTest extends AbstractTestingBase {

    @Test
    public void toBukkit() {
        for (net.minecraft.world.level.gameevent.GameEvent nms : BuiltInRegistries.GAME_EVENT) {
            GameEvent bukkit = GameEvent.getByKey(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.GAME_EVENT.getKey(nms)));

            Assert.assertNotNull("Bukkit should not be null " + nms, bukkit);
        }
    }
}
