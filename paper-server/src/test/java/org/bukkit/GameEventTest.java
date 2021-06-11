package org.bukkit;

import net.minecraft.core.IRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class GameEventTest extends AbstractTestingBase {

    @Test
    public void toBukkit() {
        for (net.minecraft.world.level.gameevent.GameEvent nms : IRegistry.GAME_EVENT) {
            GameEvent bukkit = GameEvent.getByKey(CraftNamespacedKey.fromMinecraft(IRegistry.GAME_EVENT.getKey(nms)));

            Assert.assertNotNull("Bukkit should not be null " + nms, bukkit);
        }
    }
}
