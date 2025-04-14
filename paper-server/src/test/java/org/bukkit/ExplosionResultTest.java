package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.level.Explosion;
import org.bukkit.craftbukkit.CraftExplosionResult;
import org.junit.jupiter.api.Test;

@org.bukkit.support.environment.Normal // Paper - test changes - missing test suite annotation
public class ExplosionResultTest {

    @Test
    public void testMatchingEnum() {
        for (ExplosionResult result : ExplosionResult.values()) {
            assertNotNull(Explosion.BlockInteraction.valueOf(result.name()), "No NMS enum for Bukkit result " + result);
        }
    }

    @Test
    public void testToBukkit() {
        for (Explosion.BlockInteraction effect : Explosion.BlockInteraction.values()) {
            assertNotNull(CraftExplosionResult.toExplosionResult(effect), "No Bukkit enum for NMS explosion effect " + effect);
        }
    }
}
