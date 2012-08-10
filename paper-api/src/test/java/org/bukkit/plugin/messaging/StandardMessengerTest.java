package org.bukkit.plugin.messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.TestPlugin;
import java.util.Collection;
import org.junit.Test;
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

        assertTrue(messenger.isReservedChannel("REGISTER"));
        assertFalse(messenger.isReservedChannel("register"));
        assertTrue(messenger.isReservedChannel("UNREGISTER"));
        assertFalse(messenger.isReservedChannel("unregister"));
        assertFalse(messenger.isReservedChannel("notReserved"));
    }

    @Test
    public void testRegisterAndUnregisterOutgoingPluginChannel() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "foo"));
        messenger.registerOutgoingPluginChannel(plugin, "foo");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "bar"));

        messenger.unregisterOutgoingPluginChannel(plugin, "foo");
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "foo"));
    }

    @Test(expected = ReservedChannelException.class)
    public void testReservedOutgoingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin, "REGISTER");
    }

    @Test
    public void testUnregisterOutgoingPluginChannel_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "foo"));
        messenger.registerOutgoingPluginChannel(plugin, "foo");
        messenger.registerOutgoingPluginChannel(plugin, "bar");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "foo"));
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "bar"));

        messenger.unregisterOutgoingPluginChannel(plugin);
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "bar"));
    }

    @Test
    public void testRegisterIncomingPluginChannel() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("foo", "bar".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration = messenger.registerIncomingPluginChannel(plugin, "foo", listener);

        assertTrue(registration.isValid());
        assertTrue(messenger.isIncomingChannelRegistered(plugin, "foo"));
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        assertTrue(listener.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin, "foo", listener);
        listener.reset();

        assertFalse(registration.isValid());
        assertFalse(messenger.isIncomingChannelRegistered(plugin, "foo"));
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        assertFalse(listener.hasReceived());
    }

    @Test(expected = ReservedChannelException.class)
    public void testReservedIncomingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();

        messenger.registerIncomingPluginChannel(plugin, "REGISTER", new TestMessageListener("foo", "bar".getBytes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIncomingRegistration() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("foo", "bar".getBytes());

        messenger.registerIncomingPluginChannel(plugin, "baz", listener);
        messenger.registerIncomingPluginChannel(plugin, "baz", listener);
    }

    @Test
    public void testUnregisterIncomingPluginChannel_Plugin_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("foo", "bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("baz", "qux".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "foo", listener1);
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "baz", listener2);

        assertTrue(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        messenger.dispatchIncomingMessage(player, "baz", "qux".getBytes());
        assertTrue(listener1.hasReceived());
        assertTrue(listener2.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin, "foo");
        listener1.reset();
        listener2.reset();

        assertFalse(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        messenger.dispatchIncomingMessage(player, "baz", "qux".getBytes());
        assertFalse(listener1.hasReceived());
        assertTrue(listener2.hasReceived());
    }

    @Test
    public void testUnregisterIncomingPluginChannel_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("foo", "bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("baz", "qux".getBytes());
        Player player = TestPlayer.getInstance();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "foo", listener1);
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "baz", listener2);

        assertTrue(registration1.isValid());
        assertTrue(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        messenger.dispatchIncomingMessage(player, "baz", "qux".getBytes());
        assertTrue(listener1.hasReceived());
        assertTrue(listener2.hasReceived());

        messenger.unregisterIncomingPluginChannel(plugin);
        listener1.reset();
        listener2.reset();

        assertFalse(registration1.isValid());
        assertFalse(registration2.isValid());
        messenger.dispatchIncomingMessage(player, "foo", "bar".getBytes());
        messenger.dispatchIncomingMessage(player, "baz", "qux".getBytes());
        assertFalse(listener1.hasReceived());
        assertFalse(listener2.hasReceived());
    }

    @Test
    public void testGetOutgoingChannels() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();

        assertEquals(messenger.getOutgoingChannels());

        messenger.registerOutgoingPluginChannel(plugin1, "foo");
        messenger.registerOutgoingPluginChannel(plugin1, "bar");
        messenger.registerOutgoingPluginChannel(plugin2, "baz");
        messenger.registerOutgoingPluginChannel(plugin2, "baz");

        assertEquals(messenger.getOutgoingChannels(), "foo", "bar", "baz");
    }

    @Test
    public void testGetOutgoingChannels_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin1, "foo");
        messenger.registerOutgoingPluginChannel(plugin1, "bar");
        messenger.registerOutgoingPluginChannel(plugin2, "baz");
        messenger.registerOutgoingPluginChannel(plugin2, "qux");

        assertEquals(messenger.getOutgoingChannels(plugin1), "foo", "bar");
        assertEquals(messenger.getOutgoingChannels(plugin2), "baz", "qux");
        assertEquals(messenger.getOutgoingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannels() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();

        assertEquals(messenger.getIncomingChannels());

        messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "bar", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));

        assertEquals(messenger.getIncomingChannels(), "foo", "bar", "baz");
    }

    @Test
    public void testGetIncomingChannels_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();

        messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "bar", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "qux", new TestMessageListener("foo", "bar".getBytes()));

        assertEquals(messenger.getIncomingChannels(plugin1), "foo", "bar");
        assertEquals(messenger.getIncomingChannels(plugin2), "baz", "qux");
        assertEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "bar", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "qux", new TestMessageListener("foo", "bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations(plugin1), registration1, registration2);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2), registration3, registration4);
        assertEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "bar", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "foo", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "bar", new TestMessageListener("foo", "bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations("foo"), registration1, registration3);
        assertEquals(messenger.getIncomingChannelRegistrations("bar"), registration2, registration4);
        assertEquals(messenger.getIncomingChannelRegistrations("baz"));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin_String() {
        Messenger messenger = getMessenger();
        TestPlugin plugin1 = getPlugin();
        TestPlugin plugin2 = getPlugin();
        TestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "foo", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin1, "bar", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "bar", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration5 = messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));
        PluginMessageListenerRegistration registration6 = messenger.registerIncomingPluginChannel(plugin2, "baz", new TestMessageListener("foo", "bar".getBytes()));

        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "foo"), registration1, registration2);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "bar"), registration3);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2, "bar"), registration4);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin2, "baz"), registration5, registration6);
        assertEquals(messenger.getIncomingChannelRegistrations(plugin1, "baz"));
        assertEquals(messenger.getIncomingChannelRegistrations(plugin3, "qux"));
    }

    private static <T> void assertEquals(Collection<T> actual, T... expected) {
        assertThat("Size of the array", actual.size(), is(expected.length));
        assertThat(actual, hasItems(expected));
    }
}
