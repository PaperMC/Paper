package com.destroystokyo.paper.event.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired if server is getting queried over GS4 Query protocol.
 * <br>
 * Adapted from Velocity's ProxyQueryEvent
 *
 * @author Mark Vainomaa
 */
public interface GS4QueryEvent extends Event { // todo javadocs?

    /**
     * Get query type
     *
     * @return query type
     */
    QueryType getQueryType();

    /**
     * Get querier address
     *
     * @return querier address
     */
    InetAddress getQuerierAddress();

    /**
     * Get query response
     *
     * @return query response
     */
    QueryResponse getResponse();

    /**
     * Set query response
     *
     * @param response query response
     */
    void setResponse(QueryResponse response);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * The type of query
     */
    enum QueryType {
        /**
         * Basic query asks only a subset of information, such as motd, game type (hardcoded to <pre>MINECRAFT</pre>), map,
         * current players, max players, server port and server motd
         */
        BASIC,

        /**
         * Full query asks pretty much everything present on this event (only hardcoded values cannot be modified here).
         */
        FULL
    }

    final class QueryResponse {

        private final String motd;
        private final String gameVersion;
        private final String map;
        private final int currentPlayers;
        private final int maxPlayers;
        private final String hostname;
        private final int port;
        private final Collection<String> players;
        private final String serverVersion;
        private final Collection<PluginInformation> plugins;

        private QueryResponse(
            final String motd,
            final String gameVersion,
            final String map,
            final int currentPlayers,
            final int maxPlayers,
            final String hostname,
            final int port,
            final Collection<String> players,
            final String serverVersion,
            final Collection<PluginInformation> plugins
        ) {
            this.motd = motd;
            this.gameVersion = gameVersion;
            this.map = map;
            this.currentPlayers = currentPlayers;
            this.maxPlayers = maxPlayers;
            this.hostname = hostname;
            this.port = port;
            this.players = players;
            this.serverVersion = serverVersion;
            this.plugins = plugins;
        }

        /**
         * Get motd which will be used to reply to the query. By default, it is {@link Server#getMotd()}.
         *
         * @return motd
         */
        public String getMotd() {
            return this.motd;
        }

        /**
         * Get game version which will be used to reply to the query. By default, supported Minecraft versions range is sent.
         *
         * @return game version
         */
        public String getGameVersion() {
            return this.gameVersion;
        }

        /**
         * Get map name which will be used to reply to the query. By default {@code world} is sent.
         *
         * @return map name
         */
        public String getMap() {
            return this.map;
        }

        /**
         * Get current online player count which will be used to reply to the query.
         *
         * @return online player count
         */
        public int getCurrentPlayers() {
            return this.currentPlayers;
        }

        /**
         * Get max player count which will be used to reply to the query.
         *
         * @return max player count
         */
        public int getMaxPlayers() {
            return this.maxPlayers;
        }

        /**
         * Get server (public facing) hostname.
         *
         * @return server hostname
         */
        public String getHostname() {
            return this.hostname;
        }

        /**
         * Get server (public facing) port.
         *
         * @return server port
         */
        public int getPort() {
            return this.port;
        }

        /**
         * Get collection of players which will be used to reply to the query.
         *
         * @return collection of players
         */
        public Collection<String> getPlayers() {
            return this.players;
        }

        /**
         * Get server software (name and version) which will be used to reply to the query.
         *
         * @return server software
         */
        public String getServerVersion() {
            return this.serverVersion;
        }

        /**
         * Get list of plugins which will be used to reply to the query.
         *
         * @return collection of plugins
         */
        public Collection<PluginInformation> getPlugins() {
            return this.plugins;
        }

        /**
         * Creates a new {@link Builder} instance from data represented by this response.
         *
         * @return {@link QueryResponse} builder
         */
        public Builder toBuilder() {
            return QueryResponse.builder()
                .motd(this.getMotd())
                .gameVersion(this.getGameVersion())
                .map(this.getMap())
                .currentPlayers(this.getCurrentPlayers())
                .maxPlayers(this.getMaxPlayers())
                .hostname(this.getHostname())
                .port(this.getPort())
                .players(this.getPlayers())
                .serverVersion(this.getServerVersion())
                .plugins(this.getPlugins());
        }

        /**
         * Creates a new {@link Builder} instance.
         *
         * @return {@link QueryResponse} builder
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * A builder for {@link QueryResponse} objects.
         */
        public static final class Builder {

            private @Nullable String motd;
            private @Nullable String gameVersion;
            private @Nullable String map;
            private @Nullable String hostname;
            private @Nullable String serverVersion;

            private int currentPlayers;
            private int maxPlayers;
            private int port;

            private final List<String> players = new ArrayList<>();
            private final List<PluginInformation> plugins = new ArrayList<>();

            private Builder() {
            }

            public Builder motd(final String motd) {
                Preconditions.checkArgument(motd != null, "motd cannot be null");
                this.motd = motd;
                return this;
            }

            public Builder gameVersion(final String gameVersion) {
                Preconditions.checkArgument(gameVersion != null, "gameVersion cannot be null");
                this.gameVersion = gameVersion;
                return this;
            }

            public Builder map(final String map) {
                Preconditions.checkArgument(map != null, "map cannot be null");
                this.map = map;
                return this;
            }

            public Builder currentPlayers(final int currentPlayers) {
                Preconditions.checkArgument(currentPlayers >= 0, "currentPlayers cannot be negative");
                this.currentPlayers = currentPlayers;
                return this;
            }

            public Builder maxPlayers(final int maxPlayers) {
                Preconditions.checkArgument(maxPlayers >= 0, "maxPlayers cannot be negative");
                this.maxPlayers = maxPlayers;
                return this;
            }

            public Builder hostname(final String hostname) {
                Preconditions.checkArgument(hostname != null, "hostname cannot be null");
                this.hostname = hostname;
                return this;
            }

            public Builder port(final int port) {
                Preconditions.checkArgument(port >= 1 && port <= 65535, "port must be between 1-65535");
                this.port = port;
                return this;
            }

            public Builder players(final Collection<String> players) {
                Preconditions.checkArgument(players != null, "players cannot be null");
                this.players.addAll(players);
                return this;
            }

            public Builder players(final String... players) {
                Preconditions.checkArgument(players != null, "players cannot be null");
                this.players.addAll(Arrays.asList(players));
                return this;
            }

            public Builder clearPlayers() {
                this.players.clear();
                return this;
            }

            public Builder serverVersion(final String serverVersion) {
                Preconditions.checkArgument(serverVersion != null, "serverVersion cannot be null");
                this.serverVersion = serverVersion;
                return this;
            }

            public Builder plugins(final Collection<PluginInformation> plugins) {
                Preconditions.checkArgument(plugins != null, "plugins cannot be null");
                this.plugins.addAll(plugins);
                return this;
            }

            public Builder plugins(final PluginInformation... plugins) {
                Preconditions.checkArgument(plugins != null, "plugins cannot be null");
                this.plugins.addAll(Arrays.asList(plugins));
                return this;
            }

            public Builder clearPlugins() {
                this.plugins.clear();
                return this;
            }

            /**
             * Builds new {@link QueryResponse} with supplied data.
             *
             * @return response
             */
            public QueryResponse build() {
                Preconditions.checkState(this.motd != null, "motd is required");
                Preconditions.checkState(this.gameVersion != null, "gameVersion is required");
                Preconditions.checkState(this.map != null, "map is required");
                Preconditions.checkState(this.hostname != null, "hostname is required");
                Preconditions.checkState(this.serverVersion != null, "serverVersion is required");
                return new QueryResponse(
                    this.motd,
                    this.gameVersion,
                    this.map,
                    this.currentPlayers,
                    this.maxPlayers,
                    this.hostname,
                    this.port,
                    ImmutableList.copyOf(this.players),
                    this.serverVersion,
                    ImmutableList.copyOf(this.plugins)
                );
            }
        }

        /**
         * Plugin information
         */
        public interface PluginInformation {

            String name();

            String version();

            PluginInformation withName(String name);

            PluginInformation withVersion(String version);

            /**
             * @deprecated use {@link #name()}
             */
            @Deprecated(forRemoval = true)
            default String getName() {
                return this.name();
            }

            /**
             * @deprecated use {@link #withName(String)}
             */
            @Deprecated(forRemoval = true)
            default void setName(String name) {
                this.withName(name); // no op
            }

            /**
             * @deprecated use {@link #version()}
             */
            @Deprecated(forRemoval = true)
            default String getVersion() {
                return this.version();
            }

            /**
             * @deprecated use {@link #withVersion(String)}
             */
            @Deprecated(forRemoval = true)
            default void setVersion(String version) {
                this.withVersion(version); // no op
            }

            static PluginInformation of(final String name, final String version) {
                record PluginInformationImpl(String name, String version) implements PluginInformation {
                    PluginInformationImpl {
                        Preconditions.checkArgument(name != null, "name cannot be null");
                        Preconditions.checkArgument(version != null, "version cannot be null");
                    }

                    @Override
                    public PluginInformation withName(final String name) {
                        return new PluginInformationImpl(name, this.version);
                    }

                    @Override
                    public PluginInformation withVersion(final String version) {
                        return new PluginInformationImpl(this.name, version);
                    }
                }
                return new PluginInformationImpl(name, version);
            }
        }
    }
}
