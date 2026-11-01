package org.bukkit.event;

import com.google.common.collect.ImmutableList;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@NullMarked
public class PlayerChatTabCompleteEventTest {

    @Test
    public void testGetLastToken() {
        assertThat(this.getToken("Hello everyone!"), is("everyone!"));
        assertThat(this.getToken(" welcome to the show..."), is("show..."));
        assertThat(this.getToken("The whitespace is here "), is(""));
        assertThat(this.getToken("Too much whitespace is here  "), is(""));
        assertThat(this.getToken("The_whitespace_is_missing"), is("The_whitespace_is_missing"));
        assertThat(this.getToken(""), is(""));
        assertThat(this.getToken(" "), is(""));
    }

    private String getToken(final String message) {
        return new PlayerChatTabCompleteEvent(mock(), message, ImmutableList.of()).getLastToken();
    }
}
