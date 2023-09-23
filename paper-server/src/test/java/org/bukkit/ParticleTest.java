package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class ParticleTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        for (Particle bukkit : Particle.values()) {
            Object data = null;
            if (bukkit.getDataType().equals(ItemStack.class)) {
                data = new ItemStack(Material.STONE);
            } else if (bukkit.getDataType() == MaterialData.class) {
                data = new MaterialData(Material.LEGACY_STONE);
            } else if (bukkit.getDataType() == Particle.DustOptions.class) {
                data = new Particle.DustOptions(Color.BLACK, 0);
            } else if (bukkit.getDataType() == Particle.DustTransition.class) {
                data = new Particle.DustTransition(Color.BLACK, Color.WHITE, 0);
            } else if (bukkit.getDataType() == Vibration.class) {
                data = new Vibration(new Location(null, 0, 0, 0), new Vibration.Destination.BlockDestination(new Location(null, 0, 0, 0)), 0);
            } else if (bukkit.getDataType() == BlockData.class) {
                data = CraftBlockData.newData(Material.STONE, "");
            } else if (bukkit.getDataType() == Float.class) {
                data = 1.0F;
            } else if (bukkit.getDataType() == Integer.class) {
                data = 0;
            }

            assertNotNull(CraftParticle.toNMS(bukkit, data), "Missing Bukkit->NMS particle mapping for " + bukkit);
        }
        for (net.minecraft.core.particles.Particle nms : BuiltInRegistries.PARTICLE_TYPE) {
            assertNotNull(CraftParticle.minecraftToBukkit(nms), "Missing NMS->Bukkit particle mapping for " + BuiltInRegistries.PARTICLE_TYPE.getKey(nms));
        }
    }
}
