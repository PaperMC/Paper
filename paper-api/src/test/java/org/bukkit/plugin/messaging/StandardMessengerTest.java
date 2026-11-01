package org.bukkit.plugin.messaging;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.BukkitTestPlugin;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@NullMarked
public class StandardMessengerTest {
    private int count = 0;

    public StandardMessenger getMessenger() {
        return new StandardMessenger();
    }

    public BukkitTestPlugin getPlugin() {
        return new BukkitTestPlugin("" + this.count++);
    }

    @Test
    public void testIsReservedChannel() {
        final Messenger messenger = this.getMessenger();

        assertTrue(messenger.isReservedChannel("minecraft:register"));
        assertFalse(messenger.isReservedChannel("test:register"));
        assertTrue(messenger.isReservedChannel("minecraft:unregister"));
        assertFalse(messenger.isReservedChannel("test:unregister")); // Paper - fix typo
        assertFalse(messenger.isReservedChannel("minecraft:something")); // Paper - now less strict
        assertFalse(messenger.isReservedChannel("minecraft:brand"));
    }

    @Test
    public void testRegisterAndUnregisterOutgoingPluginChannel() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();

        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        messenger.registerOutgoingPluginChannel(plugin, "test:foo");
        assertTrue(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:bar"));

        messenger.unregisterOutgoingPluginChannel(plugin, "test:foo");
        assertFalse(messenger.isOutgoingChannelRegistered(plugin, "test:foo"));
    }

    @Test
    public void testReservedOutgoingRegistration() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();

        assertThrows(ReservedChannelException.class, () -> messenger.registerOutgoingPluginChannel(plugin, "minecraft:register"));
    }

    @Test
    public void testUnregisterOutgoingPluginChannelPlugin() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();

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
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();
        final TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());
        final Player player = mock();
        final PluginMessageListenerRegistration registration = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener);

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
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();

        assertThrows(ReservedChannelException.class, () -> messenger.registerIncomingPluginChannel(plugin, "minecraft:register", new TestMessageListener("test:foo", "test:bar".getBytes())));
    }

    @Test
    public void testDuplicateIncomingRegistration() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();
        final TestMessageListener listener = new TestMessageListener("test:foo", "test:bar".getBytes());

        messenger.registerIncomingPluginChannel(plugin, "test:baz", listener);
        assertThrows(IllegalArgumentException.class, () -> messenger.registerIncomingPluginChannel(plugin, "test:baz", listener));
    }

    @Test
    public void testUnregisterIncomingPluginChannelPluginString() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();
        final TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        final TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        final Player player = mock();
        final PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener1);
        final PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "test:baz", listener2);

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
    public void testUnregisterOutgoingPluginChannelSharedChannel() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin2, "test:foo");

        messenger.unregisterOutgoingPluginChannel(plugin1, "test:foo");

        assertTrue(messenger.getOutgoingChannels().contains("test:foo"), "Channel should still be present because plugin2 is still registered");

        assertTrue(messenger.isOutgoingChannelRegistered(plugin2, "test:foo"), "Second plugin should still be registered");
        assertFalse(messenger.isOutgoingChannelRegistered(plugin1, "test:foo"), "First plugin should no longer be registered");
    }

    @Test
    public void testUnregisterIncomingPluginChannelPlugin() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();
        final TestMessageListener listener1 = new TestMessageListener("test:foo", "test:bar".getBytes());
        final TestMessageListener listener2 = new TestMessageListener("test:baz", "test:qux".getBytes());
        final Player player = mock();
        final PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin, "test:foo", listener1);
        final PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin, "test:baz", listener2);

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
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();

        assertCollectionEquals(messenger.getOutgoingChannels());

        messenger.registerOutgoingPluginChannel(plugin1, "test:foo");
        messenger.registerOutgoingPluginChannel(plugin1, "test:bar");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");
        messenger.registerOutgoingPluginChannel(plugin2, "test:baz");

        assertCollectionEquals(messenger.getOutgoingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetOutgoingChannelsPlugin() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();
        final BukkitTestPlugin plugin3 = this.getPlugin();

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
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();

        assertCollectionEquals(messenger.getIncomingChannels());

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannels(), "test:foo", "test:bar", "test:baz");
    }

    @Test
    public void testGetIncomingChannelsPlugin() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();
        final BukkitTestPlugin plugin3 = this.getPlugin();

        messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannels(plugin1), "test:foo", "test:bar");
        assertCollectionEquals(messenger.getIncomingChannels(plugin2), "test:baz", "test:qux");
        assertCollectionEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrationsPlugin() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();
        final BukkitTestPlugin plugin3 = this.getPlugin();
        final PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:qux", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1), registration1, registration2);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2), registration3, registration4);
        assertCollectionEquals(messenger.getIncomingChannels(plugin3));
    }

    @Test
    public void testGetIncomingChannelRegistrationsString() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();
        final PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin2, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:foo"), registration1, registration3);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:bar"), registration2, registration4);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations("test:baz"));
    }

    @Test
    public void testGetIncomingChannelRegistrationsPluginString() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin1 = this.getPlugin();
        final BukkitTestPlugin plugin2 = this.getPlugin();
        final BukkitTestPlugin plugin3 = this.getPlugin();
        final PluginMessageListenerRegistration registration1 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration2 = messenger.registerIncomingPluginChannel(plugin1, "test:foo", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration3 = messenger.registerIncomingPluginChannel(plugin1, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration4 = messenger.registerIncomingPluginChannel(plugin2, "test:bar", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration5 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));
        final PluginMessageListenerRegistration registration6 = messenger.registerIncomingPluginChannel(plugin2, "test:baz", new TestMessageListener("test:foo", "test:bar".getBytes()));

        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:foo"), registration1, registration2);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:bar"), registration3);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:bar"), registration4);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin2, "test:baz"), registration5, registration6);
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin1, "test:baz"));
        assertCollectionEquals(messenger.getIncomingChannelRegistrations(plugin3, "test:qux"));
    }

    @Test
    public void testInvalidChannel() {
        final Messenger messenger = this.getMessenger();
        final BukkitTestPlugin plugin = this.getPlugin();

        assertThrows(IllegalArgumentException.class, () -> messenger.registerOutgoingPluginChannel(plugin, "foo"));
    }

    @Test
    public void testValidateAndCorrectChannel() {
        assertEquals("bungeecord:main", StandardMessenger.validateAndCorrectChannel("BungeeCord"));
        assertEquals("BungeeCord", StandardMessenger.validateAndCorrectChannel("bungeecord:main"));
    }

    @SafeVarargs
    private static <T> void assertCollectionEquals(final Collection<T> actual, final T... expected) {
        assertThat(actual.size(), is(expected.length), "Size of the array");
        assertThat(actual, hasItems(expected));
    }
}
