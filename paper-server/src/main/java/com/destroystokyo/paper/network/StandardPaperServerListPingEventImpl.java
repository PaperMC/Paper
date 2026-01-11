package com.destroystokyo.paper.network;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.adventure.AdventureComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.jetbrains.annotations.NotNull;

public final class StandardPaperServerListPingEventImpl extends PaperServerListPingEventImpl {

    private List<NameAndId> originalSample;

    private StandardPaperServerListPingEventImpl(MinecraftServer server, Connection networkManager, ServerStatus ping) {
        super(server, new PaperStatusClient(networkManager), ping.version().map(ServerStatus.Version::protocol).orElse(-1), server.server.getServerIcon());
        this.originalSample = ping.players().map(ServerStatus.Players::sample).orElse(null); // GH-1473 - pre-tick race condition NPE
    }

    @Nonnull
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

    @Override
    public @NotNull List<PlayerProfile> getPlayerSample() {
        this.getListedPlayers(); // Populate the backing list for the transforming view, and null out originalSample (see getListedPlayers and processRequest)
        return super.getPlayerSample();
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

    public static void processRequest(MinecraftServer server, Connection networkManager) {
        StandardPaperServerListPingEventImpl event = new StandardPaperServerListPingEventImpl(server, networkManager, server.getStatus());
        server.server.getPluginManager().callEvent(event);

        // Close connection immediately if event is cancelled
        if (event.isCancelled()) {
            networkManager.disconnect((Component) null);
            return;
        }

        // Setup response

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

        // Send response
        networkManager.send(new ClientboundStatusResponsePacket(ping));
    }

}
