package net.minecraft.server;

import java.net.InetSocketAddress;

// CraftBukkit start
import java.util.Iterator;

import org.bukkit.craftbukkit.util.CraftIconCache;
import org.bukkit.entity.Player;

import net.minecraft.util.com.mojang.authlib.GameProfile;
// CraftBukkit end

import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

public class PacketStatusListener implements PacketStatusInListener {

    private final MinecraftServer minecraftServer;
    private final NetworkManager networkManager;

    public PacketStatusListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
    }

    public void a(IChatBaseComponent ichatbasecomponent) {}

    public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1) {
        if (enumprotocol1 != EnumProtocol.STATUS) {
            throw new UnsupportedOperationException("Unexpected change in protocol to " + enumprotocol1);
        }
    }

    public void a() {}

    public void a(PacketStatusInStart packetstatusinstart) {
        // CraftBukkit start - fire ping event
        final Object[] players = minecraftServer.getPlayerList().players.toArray();
        class ServerListPingEvent extends org.bukkit.event.server.ServerListPingEvent {
            CraftIconCache icon = minecraftServer.server.getServerIcon();

            ServerListPingEvent() {
                super(((InetSocketAddress) networkManager.getSocketAddress()).getAddress(), minecraftServer.getMotd(), minecraftServer.getPlayerList().getMaxPlayers());
            }

            @Override
            public void setServerIcon(org.bukkit.util.CachedServerIcon icon) {
                if (!(icon instanceof CraftIconCache)) {
                    throw new IllegalArgumentException(icon + " was not created by " + org.bukkit.craftbukkit.CraftServer.class);
                }
                this.icon = (CraftIconCache) icon;
            }

            @Override
            public Iterator<Player> iterator() throws UnsupportedOperationException {
                return new Iterator<Player>() {
                    int i;
                    int ret = Integer.MIN_VALUE;
                    EntityPlayer player;

                    @Override
                    public boolean hasNext() {
                        if (player != null) {
                            return true;
                        }
                        final Object[] currentPlayers = players;
                        for (int length = currentPlayers.length, i = this.i; i < length; i++) {
                            final EntityPlayer player = (EntityPlayer) currentPlayers[i];
                            if (player != null) {
                                this.i = i + 1;
                                this.player = player;
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public Player next() {
                        if (!hasNext()) {
                            throw new java.util.NoSuchElementException();
                        }
                        final EntityPlayer player = this.player;
                        this.player = null;
                        this.ret = this.i - 1;
                        return player.getBukkitEntity();
                    }

                    @Override
                    public void remove() {
                        final Object[] currentPlayers = players;
                        final int i = this.ret;
                        if (i < 0 || currentPlayers[i] == null) {
                            throw new IllegalStateException();
                        }
                        currentPlayers[i] = null;
                    }
                };
            }
        }

        ServerListPingEvent event = new ServerListPingEvent();
        this.minecraftServer.server.getPluginManager().callEvent(event);

        java.util.List<GameProfile> profiles = new java.util.ArrayList<GameProfile>(players.length);
        for (Object player : players) {
            if (player != null) {
                profiles.add(((EntityPlayer) player).getProfile());
            }
        }

        ServerPingPlayerSample playerSample = new ServerPingPlayerSample(event.getMaxPlayers(), profiles.size());
        playerSample.a(profiles.toArray(new GameProfile[profiles.size()]));

        ServerPing ping = new ServerPing();
        ping.setFavicon(event.icon.value);
        ping.setMOTD(new ChatComponentText(event.getMotd()));
        ping.setPlayerSample(playerSample);
        ping.setServerInfo(new ServerPingServerData(minecraftServer.getServerModName() + " " + minecraftServer.getVersion(), 4)); // TODO: Update when protocol changes

        this.networkManager.handle(new PacketStatusOutServerInfo(ping), new GenericFutureListener[0]);
        // CraftBukkit end
    }

    public void a(PacketStatusInPing packetstatusinping) {
        this.networkManager.handle(new PacketStatusOutPong(packetstatusinping.c()), new GenericFutureListener[0]);
    }
}
