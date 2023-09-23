package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.entity.animal.EntityPanda;
import org.bukkit.craftbukkit.entity.CraftPanda;
import org.junit.jupiter.api.Test;

public class PandaGeneTest {

    @Test
    public void testBukkit() {
        for (Panda.Gene gene : Panda.Gene.values()) {
            EntityPanda.Gene nms = CraftPanda.toNms(gene);

            assertNotNull(nms, "NMS gene null for " + gene);
            assertEquals(gene.isRecessive(), nms.isRecessive(), "Recessive status did not match " + gene);
            assertEquals(gene, CraftPanda.fromNms(nms), "Gene did not convert back " + gene);
        }
    }

    @Test
    public void testNMS() {
        for (EntityPanda.Gene gene : EntityPanda.Gene.values()) {
            Panda.Gene bukkit = CraftPanda.fromNms(gene);

            assertNotNull(bukkit, "Bukkit gene null for " + gene);
            assertEquals(gene.isRecessive(), bukkit.isRecessive(), "Recessive status did not match " + gene);
            assertEquals(gene, CraftPanda.toNms(bukkit), "Gene did not convert back " + gene);
        }
    }
}
