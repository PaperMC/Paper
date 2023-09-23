package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class GameEventTest extends AbstractTestingBase {

    @Test
    public void toBukkit() {
        for (net.minecraft.world.level.gameevent.GameEvent nms : BuiltInRegistries.GAME_EVENT) {
            GameEvent bukkit = GameEvent.getByKey(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.GAME_EVENT.getKey(nms)));

            assertNotNull(bukkit, "Bukkit should not be null " + nms);
        }
    }
}
