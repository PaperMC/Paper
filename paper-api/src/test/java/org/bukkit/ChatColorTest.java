package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ChatColorTest {

    @Test
    public void getByChar() {
        for (ChatColor color : ChatColor.values()) {
            assertThat(ChatColor.getByChar(color.getChar()), is(color));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByStringWithNull() {
        ChatColor.getByChar((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByStringWithEmpty() {
        ChatColor.getByChar("");
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
}
