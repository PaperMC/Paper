package org.bukkit.event;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.junit.jupiter.api.Test;

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
        return new PlayerChatTabCompleteEvent(mock(), message, ImmutableList.<String>of()).getLastToken();
    }
}
