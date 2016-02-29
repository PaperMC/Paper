package org.bukkit;

import net.minecraft.server.EnumParticle;
import org.bukkit.craftbukkit.CraftParticle;
import org.junit.Assert;
import org.junit.Test;

public class ParticleTest {

    @Test
    public void verifyMapping() {
        for (Particle bukkit : Particle.values()) {
            Assert.assertNotNull("Missing Bukkit->NMS particle mapping", CraftParticle.toNMS(bukkit));
        }
        for (EnumParticle nms : EnumParticle.values()) {
            Assert.assertNotNull("Missing NMS->Bukkit particle mapping", CraftParticle.toBukkit(nms));
        }
    }
}
