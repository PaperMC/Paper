package org.bukkit.plugin.messaging;

import org.bukkit.entity.Player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMessageListener implements PluginMessageListener {
    private final String channel;
    private final byte[] message;
    private boolean received = false;

    public TestMessageListener(final String channel, final byte[] message) {
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        assertEquals(this.channel, channel);
        assertArrayEquals(this.message, message);
        this.received = true;
    }

    public boolean hasReceived() {
        return this.received;
    }

    public void reset() {
        this.received = false;
    }
}
