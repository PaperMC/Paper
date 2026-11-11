package io.papermc.paper.connection;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.jsonrpc.JsonRpcLogger;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.internalapi.MinecraftPlayerListServiceImpl;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.Message;
import net.minecraft.server.jsonrpc.methods.PlayerService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.notifications.NotificationManager;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@NullMarked
@Normal
class ManagementServerKickTest {

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void testKickDefersPlayerRemoval(final boolean customMessage) {
        final NotificationManager notificationManager = Mockito.mock(NotificationManager.class);
        final DedicatedServer server = Mockito.mock(DedicatedServer.class);
        final DedicatedPlayerList playerList = Mockito.mock(DedicatedPlayerList.class);
        final JsonRpcLogger logger = Mockito.mock(JsonRpcLogger.class);
        final MinecraftApi minecraftApi = Mockito.mock(MinecraftApi.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);
        player.connection = Mockito.mock(ServerGamePacketListenerImpl.class);

        Mockito.when(notificationManager.server()).thenReturn(server);
        Mockito.when(server.getPlayerList()).thenReturn(playerList);
        Mockito.when(playerList.getPlayerByName("TestPlayer")).thenReturn(player);
        Mockito.when(player.getPlainTextName()).thenReturn("TestPlayer");
        Mockito.when(minecraftApi.playerListService()).thenReturn(new MinecraftPlayerListServiceImpl(notificationManager, logger));

        final PlayerDto playerDto = new PlayerDto(Optional.empty(), Optional.of("TestPlayer"));
        final Optional<Message> message = customMessage
            ? Optional.of(new Message(Optional.of("The kick reason"), Optional.empty(), Optional.empty()))
            : Optional.empty();
        final Component expectedMessage = customMessage
            ? Component.literal("The kick reason")
            : Component.translatable("multiplayer.disconnect.kicked");
        final ClientInfo clientInfo = ClientInfo.of(1);

        final List<PlayerDto> kicked = PlayerService.kick(minecraftApi, List.of(new PlayerService.KickDto(playerDto, message)), clientInfo);

        Assertions.assertEquals(List.of(playerDto), kicked);
        Mockito.verify(player.connection).disconnect(expectedMessage, PlayerKickEvent.Cause.KICKED);
        Mockito.verify(playerList, Mockito.never()).remove(player);
        Mockito.verify(logger).log(clientInfo, "Remove player '{}'", "TestPlayer");
    }
}
