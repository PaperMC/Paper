package org.bukkit;

import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IChatBaseComponent;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testColorConversion() {
        String test = String.format("%1$sA%1$sa%1$sB%1$sb%1$sC%1$sc%1$sD%1$sd%1$sE%1$se%1$sZ%1$sz%1$s", ChatColor.COLOR_CHAR);
        IChatBaseComponent name = CraftChatMessage.fromStringOrNull(test);
        assertEquals(test.replace(String.valueOf(ChatColor.COLOR_CHAR), ""),
                CraftChatMessage.fromComponent(name).replace(String.valueOf(ChatColor.COLOR_CHAR), ""));
    }

    @Test
    public void testURLJsonConversion() {
        IChatBaseComponent[] components;
        components = CraftChatMessage.fromString("https://spigotmc.org/test Test Message");
        assertEquals("{\"extra\":[{\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://spigotmc.org/test\"},\"text\":\"https://spigotmc.org/test\"},{\"text\":\" Test Message\"}],\"text\":\"\"}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("123 " + ChatColor.GOLD + "https://spigotmc.org " + ChatColor.BOLD + "test");
        assertEquals("{\"extra\":[{\"text\":\"123 \"},{\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://spigotmc.org\"},\"text\":\"https://spigotmc.org\"},{\"color\":\"gold\",\"text\":\" \"},{\"bold\":true,\"color\":\"gold\",\"text\":\"test\"}],\"text\":\"\"}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("multiCase http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE");
        assertEquals("{\"extra\":[{\"text\":\"multiCase \"},{\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\"},\"text\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\"}],\"text\":\"\"}",
                CraftChatMessage.toJSON(components[0]));
    }
}
