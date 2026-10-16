package io.papermc.paper.connection;

import com.mojang.authlib.GameProfile;
import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.event.connection.configuration.PlayerConnectionInitialConfigureEvent;
import io.papermc.paper.util.KeepAlive;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ServerLinks;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.config.SynchronizeRegistriesTask;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@Normal
class WorldListReconfigurationTest {

    private boolean previousSetting;
    private MinecraftServer server;
    private List<ServerPlayer> players;
    private WorldListReconfiguration refresh;

    @BeforeEach
    void setUp() {
        this.previousSetting = GlobalConfiguration.get().misc.reconfigureOnWorldChanges;
        GlobalConfiguration.get().misc.reconfigureOnWorldChanges = true;
        this.server = mock(MinecraftServer.class);
        PlayerList playerList = mock(PlayerList.class);
        this.players = new ArrayList<>();
        when(this.server.getPlayerList()).thenReturn(playerList);
        when(playerList.getPlayers()).thenReturn(this.players);
        when(this.server.levelKeys()).thenReturn(Set.of(Level.OVERWORLD));
        this.refresh = new WorldListReconfiguration(this.server);
    }

    @AfterEach
    void restoreSetting() {
        GlobalConfiguration.get().misc.reconfigureOnWorldChanges = this.previousSetting;
    }

    @Test
    void doesNothingWithoutWorldChanges() {
        ServerGamePacketListenerImpl listener = this.addPlayer();
        this.refresh.tick();
        verifyNoInteractions(listener);
    }

    @Test
    void doesNotReconfigureWhenDisabled() {
        GlobalConfiguration.get().misc.reconfigureOnWorldChanges = false;
        ServerGamePacketListenerImpl listener = this.addPlayer();
        this.refresh.requestRefresh();
        this.refresh.tick();
        verifyNoInteractions(listener);
    }

    @Test
    void coalescesChangesUntilTheEndOfTheTick() {
        ServerGamePacketListenerImpl listener = this.addPlayer();
        this.refresh.requestRefresh();
        this.refresh.requestRefresh();
        verifyNoInteractions(listener);
        this.refresh.tick();
        this.refresh.tick();
        verify(listener).reconfigureForWorldList(Set.of(Level.OVERWORLD));
    }

    @Test
    void snapshotsPlayersBeforeReconfigurationRemovesThem() {
        ServerGamePacketListenerImpl first = this.addPlayer();
        ServerGamePacketListenerImpl second = this.addPlayer();
        when(first.reconfigureForWorldList(any())).thenAnswer(invocation -> {
            this.players.clear();
            return WorldListReconfiguration.Result.STARTED;
        });
        this.refresh.requestRefresh();
        this.refresh.tick();
        verify(second).reconfigureForWorldList(Set.of(Level.OVERWORLD));
    }

    @Test
    void retainsWorldChangesRaisedDuringReconfiguration() {
        ServerGamePacketListenerImpl listener = this.addPlayer();
        when(listener.reconfigureForWorldList(any())).thenAnswer(invocation -> {
            this.refresh.requestRefresh();
            return WorldListReconfiguration.Result.STARTED;
        });
        this.refresh.requestRefresh();
        this.refresh.tick();
        verify(listener).reconfigureForWorldList(any());
        this.refresh.tick();
        verify(listener, times(2)).reconfigureForWorldList(any());
    }

    @Test
    void startsOnlyOneCycleForAnAddedWorld() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        Set<ResourceKey<Level>> worlds = Set.of(Level.OVERWORLD, addedWorld());
        assertEquals(WorldListReconfiguration.Result.STARTED, listener.reconfigureForWorldList(worlds));
        assertEquals(WorldListReconfiguration.Result.BUSY, listener.reconfigureForWorldList(worlds));
        verify(listener).switchToConfig();
    }

    @Test
    void refreshesAfterAWorldIsUnloaded() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        listener.setKnownWorlds(Set.of(Level.OVERWORLD, addedWorld()));
        assertEquals(WorldListReconfiguration.Result.STARTED, listener.reconfigureForWorldList(Set.of(Level.OVERWORLD)));
        verify(listener).switchToConfig();
    }

    @Test
    void skipsPlayersWhoAlreadyReceivedTheCurrentWorldList() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        assertEquals(WorldListReconfiguration.Result.UNCHANGED, listener.reconfigureForWorldList(Set.of(Level.OVERWORLD)));
        verify(listener, never()).switchToConfig();
    }

    @Test
    void skipsDisconnectedPlayers() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        when(listener.connection.isConnected()).thenReturn(false);
        assertEquals(WorldListReconfiguration.Result.BUSY, listener.reconfigureForWorldList(Set.of(Level.OVERWORLD, addedWorld())));
        verify(listener, never()).switchToConfig();
    }

    @Test
    void doesNotTakeOverPluginReconfiguration() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        setField(ServerGamePacketListenerImpl.class, listener, "waitingForSwitchToConfig", true);
        assertEquals(WorldListReconfiguration.Result.BUSY, listener.reconfigureForWorldList(Set.of(Level.OVERWORLD, addedWorld())));
        verify(listener, never()).switchToConfig();
    }

    @Test
    void skipsLegacyLoginConnections() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        listener.connection.handledLegacyLoginEvent = true;
        assertEquals(WorldListReconfiguration.Result.LEGACY_LOGIN, listener.reconfigureForWorldList(Set.of(Level.OVERWORLD, addedWorld())));
        verify(listener, never()).switchToConfig();
    }

    @Test
    void keepsAnImmutableSnapshotOfTheLoginWorlds() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        Set<ResourceKey<Level>> worlds = new HashSet<>(Set.of(Level.OVERWORLD));
        listener.setKnownWorlds(worlds);
        worlds.add(addedWorld());
        assertEquals(WorldListReconfiguration.Result.STARTED, listener.reconfigureForWorldList(worlds));
    }

    @Test
    void automaticAcknowledgementStartsRegistrySynchronization() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        listener.reconfigureForWorldList(Set.of(Level.OVERWORLD, addedWorld()));
        setField(ServerGamePacketListenerImpl.class, listener, "waitingForSwitchToConfig", true);
        try (MockedConstruction<ServerConfigurationPacketListenerImpl> construction = configurationListeners()) {
            listener.handleConfigurationAcknowledged(mock(ServerboundConfigurationAcknowledgedPacket.class));
            assertEquals(1, construction.constructed().size());
            verify(construction.constructed().getFirst()).startConfiguration(false);
        }
    }

    @Test
    void manualAcknowledgementStillWaitsForThePlugin() throws Exception {
        ServerGamePacketListenerImpl listener = this.gameListener();
        setField(ServerGamePacketListenerImpl.class, listener, "waitingForSwitchToConfig", true);
        try (MockedConstruction<ServerConfigurationPacketListenerImpl> construction = configurationListeners()) {
            listener.handleConfigurationAcknowledged(mock(ServerboundConfigurationAcknowledgedPacket.class));
            assertEquals(1, construction.constructed().size());
            verify(construction.constructed().getFirst(), never()).startConfiguration(anyBoolean());
        }
    }

    @Test
    void automaticConfigurationQueuesRegistriesWithoutAnInitialLoginEvent() throws Exception {
        this.checkConfigurationTasks(false, 0);
    }

    @Test
    void initialConfigurationKeepsItsLoginEvent() throws Exception {
        this.checkConfigurationTasks(true, 1);
    }

    private ServerGamePacketListenerImpl addPlayer() {
        ServerPlayer player = mock(ServerPlayer.class);
        ServerGamePacketListenerImpl listener = mock(ServerGamePacketListenerImpl.class);
        player.connection = listener;
        this.players.add(player);
        return listener;
    }

    private ServerGamePacketListenerImpl gameListener() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isConnected()).thenReturn(true);
        ServerGamePacketListenerImpl listener = mock(ServerGamePacketListenerImpl.class, CALLS_REAL_METHODS);
        setField(ServerCommonPacketListenerImpl.class, listener, "server", this.server);
        setField(ServerCommonPacketListenerImpl.class, listener, "connection", connection);
        setField(ServerCommonPacketListenerImpl.class, listener, "keepAlive", new KeepAlive());
        listener.player = mock(ServerPlayer.class);
        when(listener.player.getGameProfile()).thenReturn(new GameProfile(UUID.randomUUID(), "test"));
        when(listener.player.clientInformation()).thenReturn(ClientInformation.createDefault());
        doNothing().when(listener).switchToConfig();
        listener.setKnownWorlds(Set.of(Level.OVERWORLD));
        return listener;
    }

    private static MockedConstruction<ServerConfigurationPacketListenerImpl> configurationListeners() {
        return mockConstruction(ServerConfigurationPacketListenerImpl.class, (listener, context) ->
            listener.paperConnection = mock(PaperPlayerConfigurationConnection.class));
    }

    private void checkConfigurationTasks(boolean initial, int expectedInitialEvents) throws Exception {
        MinecraftServer server = mock(MinecraftServer.class, RETURNS_DEEP_STUBS);
        when(server.getServerModName()).thenReturn("Paper");
        when(server.serverLinks()).thenReturn(ServerLinks.EMPTY);
        when(server.registries()).thenReturn(RegistryLayer.createRegistryAccess());
        when(server.getResourceManager().listPacks()).thenAnswer(invocation -> Stream.empty());
        when(server.getWorldData().enabledFeatures()).thenReturn(FeatureFlags.VANILLA_SET);
        when(server.getCodeOfConducts()).thenReturn(Map.of());
        when(server.getServerResourcePack()).thenReturn(Optional.empty());
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "test"), false);
        ServerConfigurationPacketListenerImpl listener = spy(new ServerConfigurationPacketListenerImpl(server, mock(Connection.class), cookie));
        doNothing().when(listener).send(any(Packet.class));
        doNothing().when(listener).returnToWorld();
        AtomicInteger initialEvents = new AtomicInteger();
        Plugin plugin = mock(Plugin.class);
        when(plugin.isEnabled()).thenReturn(true);
        RegisteredListener registration = new RegisteredListener(new Listener() {},
            (ignored, event) -> initialEvents.incrementAndGet(), EventPriority.NORMAL, plugin, false);
        PlayerConnectionInitialConfigureEvent.getHandlerList().register(registration);
        try {
            listener.startConfiguration(initial);
            Field tasksField = ServerConfigurationPacketListenerImpl.class.getDeclaredField("configurationTasks");
            tasksField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Queue<ConfigurationTask> tasks = (Queue<ConfigurationTask>) tasksField.get(listener);
            assertTrue(tasks.stream().anyMatch(SynchronizeRegistriesTask.class::isInstance));
            assertTrue(tasks.stream().anyMatch(PaperConfigurationTask.class::isInstance));
            assertEquals(expectedInitialEvents, initialEvents.get());
            verify(listener).returnToWorld();
        } finally {
            PlayerConnectionInitialConfigureEvent.getHandlerList().unregister(registration);
        }
    }

    private static ResourceKey<Level> addedWorld() {
        return ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("test", "added_world"));
    }

    private static void setField(Class<?> owner, Object target, String name, Object value) throws Exception {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
