package net.minecraft.network;

import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.EventLoop;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.server.players.PlayerList;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Normal
class ConnectionPacketLimiterIntegrationTest {

    @Test
    void channelReadBypassesGlobalAndPacketSpecificLimitersForAuthorizedPlayListeners() throws ReflectiveOperationException {
        final GlobalConfiguration.PacketLimiter packetLimiter = GlobalConfiguration.get().packetLimiter;
        final GlobalConfiguration.PacketLimiter.PacketLimit previousAllPackets = packetLimiter.allPackets;
        final Map<Class<? extends Packet<?>>, GlobalConfiguration.PacketLimiter.PacketLimit> previousOverrides = packetLimiter.overrides;
        packetLimiter.allPackets = new GlobalConfiguration.PacketLimiter.PacketLimit(
            1.0, 100.0, GlobalConfiguration.PacketLimiter.PacketLimit.ViolateAction.KICK
        );
        packetLimiter.overrides = Map.of(TestPacket.class, new GlobalConfiguration.PacketLimiter.PacketLimit(
            1.0, 0.5, GlobalConfiguration.PacketLimiter.PacketLimit.ViolateAction.DROP
        ));

        try {
            final TestPacket limitedPacket = new TestPacket();
            final Connection limitedConnection = openConnection(false, limitedPacket);
            limitedConnection.channelRead0(null, limitedPacket);

            assertFalse(limitedPacket.handled);
            assertNotNull(limitedConnection.allPacketCounts);
            assertEquals(1L, limitedConnection.allPacketCounts.getSum());
            assertTrue(limitedConnection.packetSpecificLimits.containsKey(TestPacket.class));

            final TestPacket bypassedPacket = new TestPacket();
            final Connection bypassedConnection = openConnection(true, bypassedPacket);
            bypassedConnection.channelRead0(null, bypassedPacket);

            assertTrue(bypassedPacket.handled);
            assertNotNull(bypassedConnection.allPacketCounts);
            assertEquals(0L, bypassedConnection.allPacketCounts.getSum());
            assertTrue(bypassedConnection.packetSpecificLimits.isEmpty());
        } finally {
            packetLimiter.allPackets = previousAllPackets;
            packetLimiter.overrides = previousOverrides;
        }
    }

    @Test
    void allPacketKickAppliesWithoutBypassAndIsSkippedWithBypass() throws ReflectiveOperationException {
        final GlobalConfiguration.PacketLimiter packetLimiter = GlobalConfiguration.get().packetLimiter;
        final GlobalConfiguration.PacketLimiter.PacketLimit previousAllPackets = packetLimiter.allPackets;
        final Map<Class<? extends Packet<?>>, GlobalConfiguration.PacketLimiter.PacketLimit> previousOverrides = packetLimiter.overrides;
        packetLimiter.allPackets = new GlobalConfiguration.PacketLimiter.PacketLimit(
            1.0, 0.5, GlobalConfiguration.PacketLimiter.PacketLimit.ViolateAction.KICK
        );
        packetLimiter.overrides = Map.of();

        try {
            final TestPacket limitedPacket = new TestPacket();
            final KickingConnectionContext limitedContext = openKickingConnection(limitedPacket);
            limitedContext.connection.channelRead0(null, limitedPacket);

            assertFalse(limitedPacket.handled);
            verify(limitedContext.channelConfig).setAutoRead(false);

            final TestPacket bypassedPacket = new TestPacket();
            final Connection bypassedConnection = openConnection(true, bypassedPacket);
            bypassedConnection.channelRead0(null, bypassedPacket);

            assertTrue(bypassedPacket.handled);
            assertNotNull(bypassedConnection.allPacketCounts);
            assertEquals(0L, bypassedConnection.allPacketCounts.getSum());
        } finally {
            packetLimiter.allPackets = previousAllPackets;
            packetLimiter.overrides = previousOverrides;
        }
    }

    @Test
    void packetSpecificKickAppliesWithoutBypassAndIsSkippedWithBypass() throws ReflectiveOperationException {
        final GlobalConfiguration.PacketLimiter packetLimiter = GlobalConfiguration.get().packetLimiter;
        final GlobalConfiguration.PacketLimiter.PacketLimit previousAllPackets = packetLimiter.allPackets;
        final Map<Class<? extends Packet<?>>, GlobalConfiguration.PacketLimiter.PacketLimit> previousOverrides = packetLimiter.overrides;
        packetLimiter.allPackets = new GlobalConfiguration.PacketLimiter.PacketLimit(
            1.0, 100.0, GlobalConfiguration.PacketLimiter.PacketLimit.ViolateAction.KICK
        );
        packetLimiter.overrides = Map.of(TestPacket.class, new GlobalConfiguration.PacketLimiter.PacketLimit(
            1.0, 0.5, GlobalConfiguration.PacketLimiter.PacketLimit.ViolateAction.KICK
        ));

        try {
            final TestPacket limitedPacket = new TestPacket();
            final KickingConnectionContext limitedContext = openKickingConnection(limitedPacket);
            limitedContext.connection.channelRead0(null, limitedPacket);

            assertFalse(limitedPacket.handled);
            assertTrue(limitedContext.connection.packetSpecificLimits.containsKey(TestPacket.class));
            verify(limitedContext.channelConfig).setAutoRead(false);

            final TestPacket bypassedPacket = new TestPacket();
            final Connection bypassedConnection = openConnection(true, bypassedPacket);
            bypassedConnection.channelRead0(null, bypassedPacket);

            assertTrue(bypassedPacket.handled);
            assertTrue(bypassedConnection.packetSpecificLimits.isEmpty());
        } finally {
            packetLimiter.allPackets = previousAllPackets;
            packetLimiter.overrides = previousOverrides;
        }
    }

    @Test
    void fullTickTracksPermissionGrantAndRevocationAndFailsClosedWhenDisconnected() {
        final ListenerContext context = createListenerContext();
        assertFalse(context.listener.shouldBypassPacketLimiter());

        when(context.bukkitPlayer.hasPermission(io.papermc.paper.permissions.PaperPermissions.PACKET_LIMITER_BYPASS)).thenReturn(true);
        context.listener.tick();
        assertTrue(context.listener.shouldBypassPacketLimiter());

        when(context.bukkitPlayer.hasPermission(io.papermc.paper.permissions.PaperPermissions.PACKET_LIMITER_BYPASS)).thenReturn(false);
        context.listener.tick();
        assertFalse(context.listener.shouldBypassPacketLimiter());

        when(context.bukkitPlayer.hasPermission(io.papermc.paper.permissions.PaperPermissions.PACKET_LIMITER_BYPASS)).thenReturn(true);
        when(context.connection.isConnected()).thenReturn(false);
        context.listener.tick();
        assertFalse(context.listener.shouldBypassPacketLimiter());
    }

    @Test
    void switchingToConfigurationPermanentlyDisablesTheCurrentPlayListener() throws ReflectiveOperationException {
        final ListenerContext context = createListenerContext();
        when(context.bukkitPlayer.hasPermission(io.papermc.paper.permissions.PaperPermissions.PACKET_LIMITER_BYPASS)).thenReturn(true);
        context.listener.tick();
        assertTrue(context.listener.shouldBypassPacketLimiter());

        context.listener.switchToConfig();

        assertFalse(context.listener.shouldBypassPacketLimiter());
        assertTrue(getBooleanField(context.listener, "packetLimiterBypassDisabled"));
        context.listener.tick();
        assertFalse(context.listener.shouldBypassPacketLimiter());
        verify(context.connection).setupOutboundProtocol(ConfigurationProtocols.CLIENTBOUND);
        verify(context.playerList).remove(context.serverPlayer);
    }

    @Test
    void disconnectingPermanentlyDisablesTheCurrentPlayListener() throws ReflectiveOperationException {
        final ListenerContext context = createListenerContext();
        when(context.bukkitPlayer.hasPermission(io.papermc.paper.permissions.PaperPermissions.PACKET_LIMITER_BYPASS)).thenReturn(true);
        context.listener.tick();
        assertTrue(context.listener.shouldBypassPacketLimiter());

        context.listener.onDisconnect(new DisconnectionDetails(Component.literal("test disconnect")));

        assertFalse(context.listener.shouldBypassPacketLimiter());
        assertTrue(getBooleanField(context.listener, "packetLimiterBypassDisabled"));
        context.listener.tick();
        assertFalse(context.listener.shouldBypassPacketLimiter());
        verify(context.playerList).remove(context.serverPlayer);
    }

    private static Connection openConnection(final boolean bypass, final Packet<?> packet) throws ReflectiveOperationException {
        final Connection connection = new Connection(PacketFlow.SERVERBOUND);
        final Channel channel = mock(Channel.class);
        when(channel.isOpen()).thenReturn(true);
        connection.channel = channel;

        final ServerGamePacketListenerImpl listener = mock(ServerGamePacketListenerImpl.class);
        when(listener.shouldBypassPacketLimiter()).thenReturn(bypass);
        when(listener.shouldHandleMessage(packet)).thenReturn(true);
        setPacketListener(connection, listener);
        return connection;
    }

    private static KickingConnectionContext openKickingConnection(final Packet<?> packet) throws ReflectiveOperationException {
        final Connection connection = new Connection(PacketFlow.SERVERBOUND);
        final Channel channel = mock(Channel.class);
        final ChannelConfig channelConfig = mock(ChannelConfig.class);
        final EventLoop eventLoop = mock(EventLoop.class);
        when(channel.isOpen()).thenReturn(true, false);
        when(channel.config()).thenReturn(channelConfig);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(true);
        connection.channel = channel;

        final ServerGamePacketListenerImpl listener = mock(ServerGamePacketListenerImpl.class);
        when(listener.shouldHandleMessage(packet)).thenReturn(true);
        when(listener.getOwner()).thenReturn(new GameProfile(UUID.randomUUID(), "PacketLimiterTest"));
        setPacketListener(connection, listener);
        return new KickingConnectionContext(connection, channelConfig);
    }

    private static ListenerContext createListenerContext() {
        final MinecraftServer server = mock(MinecraftServer.class);
        final Connection connection = mock(Connection.class);
        final ServerPlayer serverPlayer = mock(ServerPlayer.class);
        final CraftPlayer bukkitPlayer = mock(CraftPlayer.class);
        final PlayerList playerList = mock(PlayerList.class);
        final UUID playerId = UUID.randomUUID();
        final GameProfile gameProfile = new GameProfile(playerId, "PacketLimiterTest");

        when(connection.isConnected()).thenReturn(true);
        when(server.isPaused()).thenReturn(true);
        when(server.getPlayerList()).thenReturn(playerList);
        when(serverPlayer.getUUID()).thenReturn(playerId);
        when(serverPlayer.getGameProfile()).thenReturn(gameProfile);
        when(serverPlayer.getTextFilter()).thenReturn(TextFilter.DUMMY);
        when(serverPlayer.getBukkitEntity()).thenReturn(bukkitPlayer);

        final CommonListenerCookie cookie = CommonListenerCookie.createInitial(gameProfile, false);
        final ServerGamePacketListenerImpl listener = new ServerGamePacketListenerImpl(server, connection, serverPlayer, cookie);
        return new ListenerContext(listener, connection, serverPlayer, bukkitPlayer, playerList);
    }

    private static void setPacketListener(final Connection connection, final PacketListener packetListener) throws ReflectiveOperationException {
        final Field field = Connection.class.getDeclaredField("packetListener");
        assertTrue(field.trySetAccessible());
        field.set(connection, packetListener);
    }

    private static boolean getBooleanField(final Object target, final String name) throws ReflectiveOperationException {
        final Field field = ServerGamePacketListenerImpl.class.getDeclaredField(name);
        assertTrue(field.trySetAccessible());
        return field.getBoolean(target);
    }

    private record ListenerContext(
        ServerGamePacketListenerImpl listener,
        Connection connection,
        ServerPlayer serverPlayer,
        CraftPlayer bukkitPlayer,
        PlayerList playerList
    ) {
    }

    private record KickingConnectionContext(Connection connection, ChannelConfig channelConfig) {
    }

    private static final class TestPacket implements Packet<ServerGamePacketListenerImpl> {

        private boolean handled;

        @Override
        public PacketType<TestPacket> type() {
            return null;
        }

        @Override
        public void handle(final ServerGamePacketListenerImpl listener) {
            this.handled = true;
        }
    }
}
