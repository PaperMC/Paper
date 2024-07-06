package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.level.Explosion;
import org.bukkit.craftbukkit.CraftExplosionResult;
import org.junit.jupiter.api.Test;

public class ExplosionResultTest {

    @Test
    public void testMatchingEnum() {
        for (ExplosionResult result : ExplosionResult.values()) {
            assertNotNull(Explosion.Effect.valueOf(result.name()), "No NMS enum for Bukkit result " + result);
        }
    }

    @Test
    public void testToBukkit() {
        for (Explosion.Effect effect : Explosion.Effect.values()) {
            assertNotNull(CraftExplosionResult.toBukkit(effect), "No Bukkit enum for NMS explosion effect " + effect);
        }
    }
}
