package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ChatColorTest {

    @Test
    public void getByChar() {
        for (ChatColor color : ChatColor.values()) {
            assertThat(ChatColor.getByChar(color.getChar()), is(color));
        }
    }

    @Test
    public void getByStringWithNull() {
        assertThrows(IllegalArgumentException.class, () -> ChatColor.getByChar((String) null));
    }

    @Test
    public void getByStringWithEmpty() {
        assertThrows(IllegalArgumentException.class, () -> ChatColor.getByChar(""));
    }

    @Test
    public void getByNull() {
        assertThat(ChatColor.stripColor(null), is(nullValue()));
    }

    @Test
    public void getByString() {
        for (ChatColor color : ChatColor.values()) {
            assertThat(ChatColor.getByChar(String.valueOf(color.getChar())), is(color));
        }
    }

    @Test
    public void stripColorOnNullString() {
        assertThat(ChatColor.stripColor(null), is(nullValue()));
    }

    @Test
    public void stripColor() {
        StringBuilder subject = new StringBuilder();
        StringBuilder expected = new StringBuilder();

        final String filler = "test";
        for (ChatColor color : ChatColor.values()) {
            subject.append(color).append(filler);
            expected.append(filler);
        }

        assertThat(ChatColor.stripColor(subject.toString()), is(expected.toString()));
    }

    @Test
    public void toStringWorks() {
        for (ChatColor color : ChatColor.values()) {
            assertThat(String.format("%c%c", ChatColor.COLOR_CHAR, color.getChar()), is(color.toString()));
        }
    }

    @Test
    public void translateAlternateColorCodes() {
        String s = "&0&1&2&3&4&5&6&7&8&9&A&a&B&b&C&c&D&d&E&e&F&f&K&k & more";
        String t = ChatColor.translateAlternateColorCodes('&', s);
        String u = ChatColor.BLACK.toString() + ChatColor.DARK_BLUE + ChatColor.DARK_GREEN + ChatColor.DARK_AQUA + ChatColor.DARK_RED + ChatColor.DARK_PURPLE + ChatColor.GOLD + ChatColor.GRAY + ChatColor.DARK_GRAY + ChatColor.BLUE + ChatColor.GREEN + ChatColor.GREEN + ChatColor.AQUA + ChatColor.AQUA + ChatColor.RED + ChatColor.RED + ChatColor.LIGHT_PURPLE + ChatColor.LIGHT_PURPLE + ChatColor.YELLOW + ChatColor.YELLOW + ChatColor.WHITE + ChatColor.WHITE + ChatColor.MAGIC + ChatColor.MAGIC + " & more";
        assertThat(t, is(u));
    }
}
