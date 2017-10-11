package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;
import org.bukkit.util.CachedServerIcon;

import javax.annotation.Nullable;

class PaperServerListPingEventImpl extends PaperServerListPingEvent {

    private final MinecraftServer server;

    PaperServerListPingEventImpl(MinecraftServer server, StatusClient client, int protocolVersion, @Nullable CachedServerIcon icon) {
        super(client, server.getMotd(), server.getPlayerCount(), server.getMaxPlayers(),
                server.getServerModName() + ' ' + server.getVersion(), protocolVersion, icon);
        this.server = server;
    }

    @Override
    protected final Object[] getOnlinePlayers() {
        return this.server.getPlayerList().players.toArray();
    }

    @Override
    protected final Player getBukkitPlayer(Object player) {
        return ((EntityPlayer) player).getBukkitEntity();
    }

}
