package org.bukkit;

import net.minecraft.server.EnumChatFormat;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.junit.Assert;
import org.junit.Test;

public class ChatTest {

    @Test
    public void testColors() {
        for (ChatColor color : ChatColor.values()) {
            Assert.assertNotNull(CraftChatMessage.getColor(color));
            Assert.assertEquals(color, CraftChatMessage.getColor(CraftChatMessage.getColor(color)));
        }

        for (EnumChatFormat format : EnumChatFormat.values()) {
            Assert.assertNotNull(CraftChatMessage.getColor(format));
            Assert.assertEquals(format, CraftChatMessage.getColor(CraftChatMessage.getColor(format)));
        }
    }
}
