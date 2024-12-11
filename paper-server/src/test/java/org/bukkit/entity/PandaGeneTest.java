package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.craftbukkit.entity.CraftPanda;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class PandaGeneTest {

    @Test
    public void testBukkit() {
        for (Panda.Gene gene : Panda.Gene.values()) { // Paper - remap fix
            net.minecraft.world.entity.animal.Panda.Gene nms = CraftPanda.toNms(gene); // Paper - remap fix

            assertNotNull(nms, "NMS gene null for " + gene);
            assertEquals(gene.isRecessive(), nms.isRecessive(), "Recessive status did not match " + gene);
            assertEquals(gene, CraftPanda.fromNms(nms), "Gene did not convert back " + gene);
        }
    }

    @Test
    public void testNMS() {
        for (net.minecraft.world.entity.animal.Panda.Gene gene : net.minecraft.world.entity.animal.Panda.Gene.values()) { // Paper - remap fix
            org.bukkit.entity.Panda.Gene bukkit = CraftPanda.fromNms(gene);

            assertNotNull(bukkit, "Bukkit gene null for " + gene);
            assertEquals(gene.isRecessive(), bukkit.isRecessive(), "Recessive status did not match " + gene);
            assertEquals(gene, CraftPanda.toNms(bukkit), "Gene did not convert back " + gene);
        }
    }
}
