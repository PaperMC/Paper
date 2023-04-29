package org.bukkit;

import net.minecraft.server.level.PlayerChunk;
import org.junit.Assert;
import org.junit.Test;

public class ChunkLoadLevelTest {

    @Test
    public void testOrdinalsAndNames() {
        Chunk.LoadLevel[] bukkit = Chunk.LoadLevel.values();
        PlayerChunk.State[] nms = PlayerChunk.State.values();

        Assert.assertEquals("Enum length mismatch", bukkit.length - 1, nms.length); // Final entry is Bukkit-specific UNLOADED
        for (int i = 0; i < nms.length; i++) {
            Assert.assertEquals("Enum name mismatch, expected Bukkit " + bukkit[i] + " got NMS " + nms[i], bukkit[i].name(), nms[i].name());
        }

        // Check that Bukkit-specific UNLOADED is last
        Assert.assertEquals("Final Bukkit entry should be UNLOADED", Chunk.LoadLevel.UNLOADED, bukkit[bukkit.length - 1]);
    }
}
