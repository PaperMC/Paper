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
        assertEquals("TextComponent{text='', siblings=[TextComponent{text='https://spigotmc.org/test', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=OPEN_URL, value='https://spigotmc.org/test'}, hoverEvent=null, insertion=null}}, TextComponent{text=' Test Message', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}",
                components[0].toString());

        components = CraftChatMessage.fromString("123 " + ChatColor.GOLD + "https://spigotmc.org " + ChatColor.BOLD + "test");
        assertEquals("TextComponent{text='', siblings=[TextComponent{text='123 ', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='https://spigotmc.org', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=OPEN_URL, value='https://spigotmc.org'}, hoverEvent=null, insertion=null}}, TextComponent{text=' ', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='test', siblings=[], style=Style{hasParent=true, color=§6, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}",
                components[0].toString());

        components = CraftChatMessage.fromString("multiCase http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE");
        assertEquals("TextComponent{text='', siblings=[TextComponent{text='multiCase ', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=OPEN_URL, value='http://SpigotMC.ORg/SpOngeBobMeEMeGoESHeRE'}, hoverEvent=null, insertion=null}}], style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}",
                components[0].toString());
    }
}
