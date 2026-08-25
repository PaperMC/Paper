package io.papermc.paper.event.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.network.StatusClient;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.network.PaperStatusClient;
import io.papermc.paper.util.TransformingRandomAccessList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import org.bukkit.craftbukkit.event.server.CraftServerListPingEvent;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.CachedServerIcon;
import org.jspecify.annotations.Nullable;

public class PaperServerListPingEventImpl extends CraftServerListPingEvent implements PaperServerListPingEvent {

    protected final MinecraftServer server;
    private final StatusClient client;

    private int numPlayers;
    private boolean hidePlayers;
    private final List<ListedPlayerInfo> listedPlayers = new ArrayList<>();
    private final TransformingRandomAccessList<ListedPlayerInfo, PlayerProfile> playerSample = new TransformingRandomAccessList<>(
        listedPlayers,
        info -> new UncheckedPlayerProfile(info.name(), info.id()),
        profile -> new ListedPlayerInfo(profile.getName(), profile.getId())
    );

    private String version;
    private int protocolVersion;

    private @Nullable CachedServerIcon favicon;

    private boolean cancelled;

    private boolean originalPlayerCount = true;
    private @Nullable ServerPlayer[] players;

    public PaperServerListPingEventImpl(final MinecraftServer server, final StatusClient client, final int protocolVersion, final @Nullable CachedServerIcon icon) {
        super("", client.address().getAddress(), server.motd(), server.getPlayerCount(), server.getMaxPlayers());
        this.client = client;
        this.version = server.getServerModName() + ' ' + server.getServerVersion();
        this.protocolVersion = protocolVersion;
        this.setServerIcon(icon);
        this.server = server;
    }

    @Override
    public StatusClient getClient() {
        return this.client;
    }

    @Override
    public int getNumPlayers() {
        if (this.hidePlayers) {
            return -1;
        }

        return this.numPlayers;
    }

    @Override
    public void setNumPlayers(final int numPlayers) {
        if (this.numPlayers != numPlayers) {
            this.numPlayers = numPlayers;
            this.originalPlayerCount = false;
        }
    }

    @Override
    public int getMaxPlayers() {
        if (this.hidePlayers) {
            return -1;
        }

        return super.getMaxPlayers();
    }

    @Override
    public boolean shouldHidePlayers() {
        return this.hidePlayers;
    }

    @Override
    public void setHidePlayers(final boolean hidePlayers) {
        this.hidePlayers = hidePlayers;
    }

    @Override
    public List<ListedPlayerInfo> getListedPlayers() {
        return this.listedPlayers;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    public List<PlayerProfile> getPlayerSample() {
        return this.playerSample;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(final String version) {
        Preconditions.checkArgument(version != null, "version cannot be null");
        this.version = version;
    }

    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public @Nullable CachedServerIcon getServerIcon() {
        return this.favicon;
    }

    @Override
    public void setServerIcon(@Nullable CachedServerIcon icon) {
        if (icon != null && icon.isEmpty()) {
            // Represent empty icons as null
            icon = null;
        }

        this.favicon = icon;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    public Iterator<Player> iterator() {
        if (this.players == null) {
            this.players = this.server.getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
        }

        return new PlayerIterator();
    }

    private final class PlayerIterator implements Iterator<Player> {

        private int next;
        private int current;
        private @Nullable Player player;

        @Override
        public boolean hasNext() {
            for (; this.next < players.length; this.next++) {
                if (PaperServerListPingEventImpl.this.players[this.next] != null) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Player next() {
            if (!hasNext()) {
                this.player = null;
                throw new NoSuchElementException();
            }

            this.current = this.next++;
            return this.player = PaperServerListPingEventImpl.this.players[this.current].getBukkitEntity();
        }

        @Override
        public void remove() {
            if (this.player == null) {
                throw new IllegalStateException();
            }

            final UUID uniqueId = this.player.getUniqueId();
            this.player = null;

            // Remove player from iterator
            PaperServerListPingEventImpl.this.players[this.current] = null;

            // Remove player from sample
            PaperServerListPingEventImpl.this.getPlayerSample().removeIf(p -> uniqueId.equals(p.getId()));

            // Decrement player count
            if (PaperServerListPingEventImpl.this.originalPlayerCount) {
                PaperServerListPingEventImpl.this.numPlayers--;
            }
        }
    }

    private static final class UncheckedPlayerProfile implements PlayerProfile {

        private String name;
        private UUID uuid;

        public UncheckedPlayerProfile(final String name, final UUID uuid) {
            Preconditions.checkNotNull(name, "name cannot be null");
            Preconditions.checkNotNull(uuid, "uuid cannot be null");
            this.name = name;
            this.uuid = uuid;
        }

        @Override
        public @Nullable UUID getUniqueId() {
            return this.uuid;
        }

        @Override
        public @Nullable String getName() {
            return this.name;
        }

        @Override
        public String setName(final @Nullable String name) {
            Preconditions.checkArgument(name != null, "name cannot be null");
            return this.name = name;
        }

        @Override
        public @Nullable UUID getId() {
            return this.uuid;
        }

        @Override
        public @Nullable UUID setId(final @Nullable UUID uuid) {
            Preconditions.checkArgument(uuid != null, "uuid cannot be null");
            return this.uuid = uuid;
        }

        @Override
        public PlayerTextures getTextures() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setTextures(final @Nullable PlayerTextures textures) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<ProfileProperty> getProperties() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasProperty(final @Nullable String property) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setProperty(final ProfileProperty property) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setProperties(final Collection<ProfileProperty> properties) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeProperty(final @Nullable String property) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clearProperties() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean completeFromCache() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean completeFromCache(final boolean onlineMode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean completeFromCache(final boolean lookupUUID, final boolean onlineMode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean complete(final boolean textures) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean complete(final boolean textures, final boolean onlineMode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public CompletableFuture<PlayerProfile> update() {
            throw new UnsupportedOperationException();
        }

        @Override
        public PlayerProfile clone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Object> serialize() {
            throw new UnsupportedOperationException();
        }
    }

    public static class Standard extends PaperServerListPingEventImpl {

        private @Nullable List<NameAndId> originalSample;

        public Standard(final MinecraftServer server, final Connection connection, final ServerStatus ping) {
            super(server, new PaperStatusClient(connection), ping.version().map(ServerStatus.Version::protocol).orElse(-1), server.server.getServerIcon());
            this.originalSample = ping.players().map(ServerStatus.Players::sample).orElse(null); // GH-1473 - pre-tick race condition NPE
        }

        @Override
        public List<ListedPlayerInfo> getListedPlayers() {
            final List<ListedPlayerInfo> sample = super.getListedPlayers();

            if (this.originalSample != null) {
                for (final NameAndId profile : this.originalSample) {
                    sample.add(new ListedPlayerInfo(profile.name(), profile.id()));
                }
                this.originalSample = null;
            }

            return sample;
        }

        @Override
        public List<PlayerProfile> getPlayerSample() {
            this.getListedPlayers(); // Populate the backing list for the transforming view, and null out originalSample (see getListedPlayers and processRequest)
            return super.getPlayerSample();
        }

        private List<NameAndId> getPlayerSampleHandle() {
            if (this.originalSample != null) {
                return this.originalSample;
            }

            final List<ListedPlayerInfo> players = super.getListedPlayers();
            if (players.isEmpty()) {
                return Collections.emptyList();
            }

            final List<NameAndId> profiles = new ArrayList<>(players.size());
            for (final ListedPlayerInfo player : players) {
                profiles.add(new NameAndId(player.id(), player.name()));
            }
            return profiles;
        }

        // Setup response
        public ServerStatus packStatus() {
            // Description
            final Component description = PaperAdventure.asVanilla(this.motd());

            // Players
            final Optional<ServerStatus.Players> players;
            if (!this.shouldHidePlayers()) {
                players = Optional.of(new ServerStatus.Players(this.getMaxPlayers(), this.getNumPlayers(), this.getPlayerSampleHandle()));
            } else {
                players = Optional.empty();
            }

            // Version
            final ServerStatus.Version version = new ServerStatus.Version(this.getVersion(), this.getProtocolVersion());

            // Favicon
            final Optional<ServerStatus.Favicon> favicon;
            if (this.getServerIcon() != null) {
                favicon = Optional.of(new ServerStatus.Favicon(((CraftIconCache) this.getServerIcon()).value));
            } else {
                favicon = Optional.empty();
            }

            return new ServerStatus(
                description,
                players,
                Optional.of(version),
                favicon,
                this.server.enforceSecureProfile()
            );
        }
    }
}
