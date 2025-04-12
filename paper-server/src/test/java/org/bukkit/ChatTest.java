package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
public class ChatTest {

    @Test
    public void testColors() {
        for (ChatColor color : ChatColor.values()) {
            assertNotNull(CraftChatMessage.getColor(color));
            assertEquals(color, CraftChatMessage.getColor(CraftChatMessage.getColor(color)));
        }

        for (ChatFormatting format : ChatFormatting.values()) {
            assertNotNull(CraftChatMessage.getColor(format));
            assertEquals(format, CraftChatMessage.getColor(CraftChatMessage.getColor(format)));
        }
    }

    @Test
    public void testURLJsonConversion() {
        Component[] components;
        components = CraftChatMessage.fromString("https://spigotmc.org/test Test Message");
        assertEquals("{\"text\":\"\",\"extra\":[{\"text\":\"https://spigotmc.org/test\",\"click_event\":{\"url\":\"https://spigotmc.org/test\",\"action\":\"open_url\"}},\" Test Message\"]}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("123 " + ChatColor.GOLD + "https://spigotmc.org " + ChatColor.BOLD + "test");
        assertEquals("{\"text\":\"\",\"extra\":[\"123 \",{\"text\":\"https://spigotmc.org\",\"strikethrough\":false,\"obfuscated\":false,\"click_event\":{\"url\":\"https://spigotmc.org\",\"action\":\"open_url\"},\"bold\":false,\"italic\":false,\"underlined\":false,\"color\":\"gold\"},{\"text\":\" \",\"strikethrough\":false,\"obfuscated\":false,\"bold\":false,\"italic\":false,\"underlined\":false,\"color\":\"gold\"},{\"text\":\"test\",\"strikethrough\":false,\"obfuscated\":false,\"bold\":true,\"italic\":false,\"underlined\":false,\"color\":\"gold\"}]}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("multiCase http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE");
        assertEquals("{\"text\":\"\",\"extra\":[\"multiCase \",{\"text\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\",\"click_event\":{\"url\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\",\"action\":\"open_url\"}}]}",
                CraftChatMessage.toJSON(components[0]));
    }
}
