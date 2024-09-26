package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class GameEventTest {

    @Test
    public void toBukkit() {
        for (net.minecraft.world.level.gameevent.GameEvent nms : BuiltInRegistries.GAME_EVENT) {
            GameEvent bukkit = GameEvent.getByKey(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.GAME_EVENT.getKey(nms)));

            assertNotNull(bukkit, "Bukkit should not be null " + nms);
        }
    }
}
