package org.bukkit.plugin.messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.TestPlugin;
import java.util.Collection;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class StandardMessengerTest {
    public StandardMessenger getMessenger() {
        return new StandardMessenger();
    }

    private int count = 0;
    public TestPlugin getPlugin() {
        return new TestPlugin("" + count++);
    }

    @Test
    public void testIsReservedChannel() {
        Messenger messenger = getMessenger();

        assertTrue(messenger.isReservedChannel("minecraft:register"));
        assertFalse(messenger.isReservedChannel("test:register"));
        assertTrue(messenger.isReservedChannel("minecraft:unregister"));
        assertFalse(messenger.isReservedChannel("test:nregister"));
        assertTrue(messenger.isReservedChannel("minecraft:something"));
        assertFalse(messenger.isReservedChannel("minecraft:brand"));
    }

    @Test
    public void testRegisterAndUnregisterOutgoingPluginChannel() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        messenger.registerOutgoingPluginChannel(plugin, "test:foo");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:bar"));

        messenger.unregisterOutgoingPluginChannel(plugin, "test:foo");
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
    }

    @Test(expected = ReservedChannelException.class)
    public void testReservedOutgoingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin, "minecraft:register");
    }

    @Test
    public void testUnregisterOutgoingPluginChannel_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        messenger.registerOutgoingPluginChannel(plugin, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin, "test:bar");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "test:bar"));

        messenger.unregisterOutgoingPluginChannel(plugin);
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:bar"));
    }

    @Test
    public void testRegisterIncomingPluginChannel() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener);

        assertTrue(registration.isValid());
        assertTrue(messenger.isIncomingChannelRegistered(plugin, "test:foo"));
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        assertTrue(listener.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin, "test:foo", listener);
        listener.reset();

        assertFalse(registration.isValid());
        assertFalse(messenger.isIncomingChannelRegistered(plugin, "test:foo"));
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        assertFalse(listener.hasReceived());
    }

    @Test(expected = ReservedChannelException.class)
    public void testReservedIncomingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        messenger.registerIncomingPluginChannel(plugin, "minecraft:register", new TestMessageListener("test:foo", "test:bar".getBytes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIncomingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());

        messenger.registerIncomingPluginChannel(plugin, "test:baz", listener);
        messenger.registerIncomingPluginChannel(plugin, "test:baz", listener);
    }

    @Test
    public void testUnregisterIncomingPluginChannel_Plugin_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener1);
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "test:baz", listener2);

        assertTrue(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        messenger.dispatchIncomingMessage(player, "test:baz", "test:qux".getBytes());
        assertTrue(listener1.hasReceived());
        assertTrue(listener2.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin, "test:foo");
        listener1.reset();
        listener2.reset();

        assertFalse(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        messenger.dispatchIncomingMessage(player, "test:baz", "test:qux".getBytes());
        assertFalse(listener1.hasReceived());
        assertTrue(listener2.hasReceived());
    }

    @Test
    public void testUnregisterIncomingPluginChannel_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener1);
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "test:baz", listener2);

        assertTrue(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        messenger.dispatchIncomingMessage(player, "test:baz", "test:qux".getBytes());
        assertTrue(listener1.hasReceived());
        assertTrue(listener2.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin);
        listener1.reset();
        listener2.reset();

        assertFalse(registration1.isValid());
        assertFalse(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "test:foo", "test:bar".getBytes());
        messenger.dispatchIncomingMessage(player, "test:baz", "test:qux".getBytes());
        assertFalse(listener1.hasReceived());
        assertFalse(listener2.hasReceived());
    }

    @Test
    public void testGetOutgoingChannels() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();

        assertEquals(messenger.getOutgoingChannels());

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin1, "test:bar");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");

        assertEquals(messenger.getOutgoingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetOutgoingChannels_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin1, "test:bar");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");
        messenger.registerOutgoingPluginChannel(plugin2, "test:qux");

        assertEquals(messenger.getOutgoingChannels(plugin1), "test:foo", "test:bar");
        assertEquals(messenger.getOutgoingChannels(plugin2), "test:baz", "test:qux");
        assertEquals(messenger.getOutgoingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannels() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();

        assertEquals(messenger.getIncomingChannels());

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertEquals(messenger.getIncomingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetIncomingChannels_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertEquals(messenger.getIncomingChannels(plugin1), "test:foo", "test:bar");
        assertEquals(messenger.getIncomingChannels(plugin2), "test:baz", "test:qux");
        assertEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations(plugin1), registration1, registration2);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2), registration3, registration4);
        assertEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations("test:foo"), registration1, registration3);
        assertEquals(messenger.getIncomingChannelRegistrations("test:bar"), registration2, registration4);
        assertEquals(messenger.getIncomingChannelRegistrations("test:baz"));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration5 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration6 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:foo"), registration1, registration2);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:bar"), registration3);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:bar"), registration4);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:baz"), registration5, registration6);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:baz"));
        assertEquals(messenger.getIncomingChannelRegistrations(plugin3, "test:qux"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChannel() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin, "foo");
    }

    @Test
    public void testValidateAndCorrectChannel() {
        Assert.assertEquals("bungeecord:main", StandardMessenger.validateAndCorrectChannel("BungeeCord"));
        Assert.assertEquals("BungeeCord", StandardMessenger.validateAndCorrectChannel("bungeecord:main"));
    }

    private static <T> void assertEquals(Collection<T> actual, T... expected) {
        assertThat("Size of the array", actual.size(), is(expected.length));
        assertThat(actual, hasItems(expected));
    }
}
