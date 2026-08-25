package org.bukkit.craftbukkit.event.server;

import java.net.InetAddress;
import java.util.Iterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.UndefinedNullability;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.Contract;

public class CraftServerListPingEvent extends CraftServerEvent implements ServerListPingEvent {

    private static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;

    private final String hostname;
    private final InetAddress address;
    private final int numPlayers;
    private Component motd;
    private int maxPlayers;

    public CraftServerListPingEvent(final String hostname, final InetAddress address, final Component motd, final int numPlayers, final int maxPlayers) {
        super(true);
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    // todo never used?
    /*
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     */
    protected CraftServerListPingEvent(final String hostname, final InetAddress address, final Component motd, final int maxPlayers) {
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public InetAddress getAddress() {
        return this.address;
    }

    @Override
    public Component motd() {
        return this.motd;
    }

    @Override
    public void motd(final Component motd) {
        this.motd = motd;
    }

    @Override
    @Deprecated
    public String getMotd() {
        return LegacyComponentSerializer.legacySection().serialize(this.motd);
    }

    @Override
    @Deprecated
    public void setMotd(final String motd) {
        this.motd = LegacyComponentSerializer.legacySection().deserialize(motd);
    }

    @Override
    public int getNumPlayers() {
        int numPlayers = this.numPlayers;
        if (numPlayers == MAGIC_PLAYER_COUNT) {
            numPlayers = 0;
            for (@SuppressWarnings("unused") final Player player : this) {
                numPlayers++;
            }
        }
        return numPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    @Contract("-> false")
    @Deprecated(since = "1.19.3", forRemoval = true)
    public boolean shouldSendChatPreviews() {
        return false;
    }

    @Override
    public void setServerIcon(@UndefinedNullability("implementation dependent") final CachedServerIcon icon) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    public Iterator<Player> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HandlerList getHandlers() {
        return ServerListPingEvent.getHandlerList();
    }
}
