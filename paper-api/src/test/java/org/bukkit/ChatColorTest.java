package org.bukkit;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.CoreMatchers.*;

public class ChatColorTest {
    @Test
    public void testGetCode() {
        ChatColor color = ChatColor.DARK_RED;
        assertThat(color.getCode(), equalTo(4));
    }

    @Test
    public void testGetChar() {
        ChatColor color = ChatColor.MAGIC;
        assertThat(color.getChar(), equalTo('k'));
    }

    @Test
    public void testToString() {
        ChatColor color = ChatColor.LIGHT_PURPLE;
        assertThat(color.toString(), equalTo("\u00A7d"));
    }

    @Test
    public void testGetByCode() {
        ChatColor color = ChatColor.AQUA;
        assertThat(ChatColor.getByCode(color.getCode()), equalTo(color));
    }

    @Test
    public void testGetByChar_char() {
        ChatColor color = ChatColor.GOLD;
        assertThat(ChatColor.getByChar(color.getChar()), equalTo(color));
    }

    @Test
    public void testGetByChar_String() {
        ChatColor color = ChatColor.BLUE;
        assertThat(ChatColor.getByChar(((Character)color.getChar()).toString()), equalTo(color));
    }

    @Test
    public void testStripColor() {
        String string = "";
        String expected = "";

        for (ChatColor color : ChatColor.values()) {
            string += color + "test";
            expected += "test";
        }

        assertThat(ChatColor.stripColor(string), equalTo(expected));
    }
}
