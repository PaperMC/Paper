package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class ChatTest extends AbstractTestingBase {

    @Test
    public void testColors() {
        for (ChatColor color : ChatColor.values()) {
            assertNotNull(CraftChatMessage.getColor(color));
            assertEquals(color, CraftChatMessage.getColor(CraftChatMessage.getColor(color)));
        }

        for (EnumChatFormat format : EnumChatFormat.values()) {
            assertNotNull(CraftChatMessage.getColor(format));
            assertEquals(format, CraftChatMessage.getColor(CraftChatMessage.getColor(format)));
        }
    }

    @Test
    public void testURLJsonConversion() {
        IChatBaseComponent[] components;
        components = CraftChatMessage.fromString("https://spigotmc.org/test Test Message");
        assertEquals("{\"text\":\"\",\"extra\":[{\"text\":\"https://spigotmc.org/test\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://spigotmc.org/test\"}},\" Test Message\"]}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("123 " + ChatColor.GOLD + "https://spigotmc.org " + ChatColor.BOLD + "test");
        assertEquals("{\"text\":\"\",\"extra\":[\"123 \",{\"text\":\"https://spigotmc.org\",\"obfuscated\":false,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://spigotmc.org\"},\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"color\":\"gold\",\"bold\":false},{\"text\":\" \",\"obfuscated\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"color\":\"gold\",\"bold\":false},{\"text\":\"test\",\"obfuscated\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"color\":\"gold\",\"bold\":true}]}",
                CraftChatMessage.toJSON(components[0]));

        components = CraftChatMessage.fromString("multiCase http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE");
        assertEquals("{\"text\":\"\",\"extra\":[\"multiCase \",{\"text\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE\"}}]}",
                CraftChatMessage.toJSON(components[0]));
    }
}
