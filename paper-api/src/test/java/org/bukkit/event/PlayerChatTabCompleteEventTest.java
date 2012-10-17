package org.bukkit.event;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.messaging.TestPlayer;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class PlayerChatTabCompleteEventTest {

    @Test
    public void testGetLastToken() {
        assertThat(getToken("Hello everyone!"), is("everyone!"));
        assertThat(getToken(" welcome to the show..."), is("show..."));
        assertThat(getToken("The whitespace is here "), is(""));
        assertThat(getToken("Too much whitespace is here  "), is(""));
        assertThat(getToken("The_whitespace_is_missing"), is("The_whitespace_is_missing"));
        assertThat(getToken(""), is(""));
        assertThat(getToken(" "), is(""));
    }

    private String getToken(String message) {
        return new PlayerChatTabCompleteEvent(TestPlayer.getInstance(), message, ImmutableList.<String>of()).getLastToken();
    }
}
