package io.papermc.paper.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.papermc.paper.adventure.AdventureComponent;
import io.papermc.paper.event.server.ServerListPlayerPingEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListPlayerPingEventImpl extends ServerListPlayerPingEvent {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
        .setNameFormat("Paper Async Status Handler Thread")
        .setUncaughtExceptionHandler(new net.minecraft.DefaultUncaughtExceptionHandlerWithName(MinecraftServer.LOGGER))
        .build()
    );

    private List<NameAndId> originalSample;

    private ServerListPlayerPingEventImpl(MinecraftServer server, ServerPlayer player, ServerStatus ping) {
        super(player.getBukkitEntity(), server.motd(), server.getPlayerCount(), server.getMaxPlayers(),
            server.getServerModName() + ' ' + server.getServerVersion(), ping.version().map(ServerStatus.Version::protocol).orElse(-1),
            server.server.getServerIcon());
        this.originalSample = ping.players().map(ServerStatus.Players::sample).orElse(null); // GH-1473 - pre-tick race condition NPE
    }

    @NonNull
    @Override
    public List<ListedPlayerInfo> getListedPlayers() {
        List<ListedPlayerInfo> sample = super.getListedPlayers();

        if (this.originalSample != null) {
            for (NameAndId profile : this.originalSample) {
                sample.add(new ListedPlayerInfo(profile.name(), profile.id()));
            }
            this.originalSample = null;
        }

        return sample;
    }

    private List<NameAndId> getPlayerSampleHandle() {
        if (this.originalSample != null) {
            return this.originalSample;
        }

        List<ListedPlayerInfo> entries = super.getListedPlayers();
        if (entries.isEmpty()) {
            return Collections.emptyList();
        }

        final List<NameAndId> profiles = new ArrayList<>();
        for (ListedPlayerInfo playerInfo : entries) {
            profiles.add(new NameAndId(playerInfo.id(), playerInfo.name()));
        }
        return profiles;
    }

    public static void processRequest(MinecraftServer server, ServerPlayer player) {
        EXECUTOR.execute(() -> {
            ServerListPlayerPingEventImpl event = new ServerListPlayerPingEventImpl(server, player, server.getStatus());
            server.server.getPluginManager().callEvent(event);

            // Do not send packet if cancelled
            if (event.isCancelled()) {
                return;
            }

            // Setup status

            // Description
            final Component description = new AdventureComponent(event.motd());

            // Players
            final Optional<ServerStatus.Players> players;
            if (!event.shouldHidePlayers()) {
                players = Optional.of(new ServerStatus.Players(event.getMaxPlayers(), event.getNumPlayers(), event.getPlayerSampleHandle()));
            } else {
                players = Optional.empty();
            }

            // Version
            final ServerStatus.Version version = new ServerStatus.Version(event.getVersion(), event.getProtocolVersion());

            // Favicon
            final Optional<ServerStatus.Favicon> favicon;
            if (event.getServerIcon() != null) {
                favicon = Optional.of(new ServerStatus.Favicon(((CraftIconCache) event.getServerIcon()).value));
            } else {
                favicon = Optional.empty();
            }
            final ServerStatus ping = new ServerStatus(description, players, Optional.of(version), favicon, server.enforceSecureProfile());

            // Send status
            player.sendServerStatus(ping);
        });
    }

}
