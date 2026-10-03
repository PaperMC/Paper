package net.minecraft.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.mockito.MockedStatic;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Normal
class ConnectionPacketLimiterTest {

    @Test
    void onlyPlayListenersCanBypassThePacketLimiter() {
        final ServerLoginPacketListenerImpl loginListener = mock(ServerLoginPacketListenerImpl.class);
        final ServerConfigurationPacketListenerImpl configurationListener = mock(ServerConfigurationPacketListenerImpl.class);

        assertFalse(Connection.shouldBypassPacketLimiter(null));
        assertFalse(Connection.shouldBypassPacketLimiter(loginListener));
        assertFalse(Connection.shouldBypassPacketLimiter(configurationListener));
    }

    @Test
    void playListenerBypassFollowsCachedPermissionState() {
        final ServerGamePacketListenerImpl playListener = mock(ServerGamePacketListenerImpl.class);
        assertFalse(Connection.shouldBypassPacketLimiter(playListener));

        when(playListener.shouldBypassPacketLimiter()).thenReturn(true);
        assertTrue(Connection.shouldBypassPacketLimiter(playListener));
    }

    @Test
    void skippedDecoderExceptionsStillTriggerVanillaRateLimitWhenBypassed() throws ReflectiveOperationException {
        final RateKickingConnection connection = new RateKickingConnection(0);
        final Channel channel = mock(Channel.class);
        final ChannelConfig channelConfig = mock(ChannelConfig.class);
        when(channel.isOpen()).thenReturn(false);
        when(channel.config()).thenReturn(channelConfig);
        connection.channel = channel;

        final ServerGamePacketListenerImpl playListener = mock(ServerGamePacketListenerImpl.class);
        when(playListener.shouldBypassPacketLimiter()).thenReturn(true);
        setPacketListener(connection, playListener);

        final MinecraftServer server = mock(MinecraftServer.class);
        when(server.isDebugging()).thenReturn(false);
        try (MockedStatic<MinecraftServer> ignored = mockStatic(MinecraftServer.class)) {
            ignored.when(MinecraftServer::getServer).thenReturn(server);
            connection.exceptionCaught(null, new SkipPacketDecoderException("skipped"));
        }
        connection.tickSecond();

        assertTrue(connection.getAverageReceivedPackets() > 0.0F);
        verify(channelConfig).setAutoRead(false);
    }

    @Test
    void disablingPacketLimiterBypassPermanentlyDisablesTheCurrentPlayListener() throws ReflectiveOperationException {
        final ServerGamePacketListenerImpl playListener = mock(ServerGamePacketListenerImpl.class, CALLS_REAL_METHODS);
        setBooleanField(playListener, "packetLimiterBypass", true);
        assertTrue(playListener.shouldBypassPacketLimiter());

        setBooleanField(playListener, "packetLimiterBypassDisabled", true);
        assertFalse(playListener.shouldBypassPacketLimiter());

        setBooleanField(playListener, "packetLimiterBypassDisabled", false);
        invokeDisablePacketLimiterBypass(playListener);

        assertFalse(playListener.shouldBypassPacketLimiter());
        assertTrue(getBooleanField(playListener, "packetLimiterBypassDisabled"));
    }

    private static void setPacketListener(final Connection connection, final PacketListener packetListener) throws ReflectiveOperationException {
        final Field field = Connection.class.getDeclaredField("packetListener");
        assertTrue(field.trySetAccessible());
        field.set(connection, packetListener);
    }

    private static void setBooleanField(final Object target, final String name, final boolean value) throws ReflectiveOperationException {
        final Field field = ServerGamePacketListenerImpl.class.getDeclaredField(name);
        assertTrue(field.trySetAccessible());
        field.setBoolean(target, value);
    }

    private static void invokeDisablePacketLimiterBypass(final ServerGamePacketListenerImpl listener) throws ReflectiveOperationException {
        final Method method = ServerGamePacketListenerImpl.class.getDeclaredMethod("disablePacketLimiterBypass");
        assertTrue(method.trySetAccessible());
        method.invoke(listener);
    }

    private static boolean getBooleanField(final Object target, final String name) throws ReflectiveOperationException {
        final Field field = ServerGamePacketListenerImpl.class.getDeclaredField(name);
        assertTrue(field.trySetAccessible());
        return field.getBoolean(target);
    }
}
