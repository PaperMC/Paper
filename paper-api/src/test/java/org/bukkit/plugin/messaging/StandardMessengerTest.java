package org.bukkit.plugin.messaging;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.BukkitTestPlugin;
import org.junit.jupiter.api.Test;

public class StandardMessengerTest {
    public StandardMessenger getMessenger() {
        return new StandardMessenger();
    }

    private int count = 0;
    public BukkitTestPlugin getPlugin() {
        return new BukkitTestPlugin("" + count++);
    }

    @Test
    public void testIsReservedChannel() {
        Messenger messenger = getMessenger();

        assertTrue(messenger.isReservedChannel("minecraft:register"));
        assertFalse(messenger.isReservedChannel("test:register"));
        assertTrue(messenger.isReservedChannel("minecraft:unregister"));
        assertFalse(messenger.isReservedChannel("test:unregister")); // Paper - fix typo
        assertFalse(messenger.isReservedChannel("minecraft:something")); // Paper - now less strict
        assertFalse(messenger.isReservedChannel("minecraft:brand"));
    }

    @Test
    public void testRegisterAndUnregisterOutgoingPluginChannel() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        messenger.registerOutgoingPluginChannel(plugin, "test:foo");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:bar"));

        messenger.unregisterOutgoingPluginChannel(plugin, "test:foo");
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
    }

    @Test
    public void testReservedOutgoingRegistration() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();

        assertThrows(ReservedChannelException.class, () -> messenger.registerOutgoingPluginChannel(plugin, "minecraft:register"));
    }

    @Test
    public void testUnregisterOutgoingPluginChannel_Plugin() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();

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
        BukkitTestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());
        Player player = mock();
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

    @Test
    public void testReservedIncomingRegistration() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();

        assertThrows(ReservedChannelException.class, () -> messenger.registerIncomingPluginChannel(plugin, "minecraft:register", new TestMessageListener("test:foo", "test:bar".getBytes())));
    }

    @Test
    public void testDuplicateIncomingRegistration() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();
        TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());

        messenger.registerIncomingPluginChannel(plugin, "test:baz", listener);
        assertThrows(IllegalArgumentException.class, () -> messenger.registerIncomingPluginChannel(plugin, "test:baz", listener));
    }

    @Test
    public void testUnregisterIncomingPluginChannel_Plugin_String() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        Player player = mock();
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
        BukkitTestPlugin plugin = getPlugin();
        TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        Player player = mock();
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
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();

        assertCollectionEquals(messenger.getOutgoingChannels());

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin1, "test:bar");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");

        assertCollectionEquals(messenger.getOutgoingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetOutgoingChannels_Plugin() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();
        BukkitTestPlugin plugin3 = getPlugin();

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin1, "test:bar");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");
        messenger.registerOutgoingPluginChannel(plugin2, "test:qux");

        assertCollectionEquals(messenger.getOutgoingChannels(plugin1), "test:foo", "test:bar");
        assertCollectionEquals(messenger.getOutgoingChannels(plugin2), "test:baz", "test:qux");
        assertCollectionEquals(messenger.getOutgoingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannels() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();

        assertCollectionEquals(messenger.getIncomingChannels());

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetIncomingChannels_Plugin() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();
        BukkitTestPlugin plugin3 = getPlugin();

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannels(plugin1), "test:foo", "test:bar");
        assertCollectionEquals(messenger.getIncomingChannels(plugin2), "test:baz", "test:qux");
        assertCollectionEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();
        BukkitTestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1), registration1, registration2);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2), registration3, registration4);
        assertCollectionEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrations_String() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:foo"), registration1, registration3);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:bar"), registration2, registration4);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:baz"));
    }

    @Test
    public void testGetIncomingChannelRegistrations_Plugin_String() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin1 = getPlugin();
        BukkitTestPlugin plugin2 = getPlugin();
        BukkitTestPlugin plugin3 = getPlugin();
        PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration5 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        PluginMessageListenerRegistration registration6 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:foo"), registration1, registration2);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:bar"), registration3);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:bar"), registration4);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:baz"), registration5, registration6);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:baz"));
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin3, "test:qux"));
    }

    @Test
    public void testInvalidChannel() {
        Messenger messenger = getMessenger();
        BukkitTestPlugin plugin = getPlugin();

        assertThrows(IllegalArgumentException.class, () -> messenger.registerOutgoingPluginChannel(plugin, "foo"));
    }

    @Test
    public void testValidateAndCorrectChannel() {
        assertEquals("bungeecord:main", StandardMessenger.validateAndCorrectChannel("BungeeCord"));
        assertEquals("BungeeCord", StandardMessenger.validateAndCorrectChannel("bungeecord:main"));
    }

    private static <T> void assertCollectionEquals(Collection<T> actual, T... expected) {
        assertThat(actual.size(), is(expected.length), "Size of the array");
        assertThat(actual, hasItems(expected));
    }
}
