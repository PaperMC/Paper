package com.destroystokyo.paper.network;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketStatusOutServerInfo;
import net.minecraft.server.ServerPing;
import org.bukkit.craftbukkit.util.CraftChatMessage;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

public final class StandardPaperServerListPingEventImpl extends PaperServerListPingEventImpl {

    private static final GameProfile[] EMPTY_PROFILES = new GameProfile[0];
    private static final UUID FAKE_UUID = new UUID(0, 0);

    private GameProfile[] originalSample;

    private StandardPaperServerListPingEventImpl(MinecraftServer server, NetworkManager networkManager, ServerPing ping) {
        super(server, new PaperStatusClient(networkManager), ping.getServerData() != null ? ping.getServerData().getProtocolVersion() : -1, server.server.getServerIcon());
        this.originalSample = ping.getPlayers() == null ? null : ping.getPlayers().getSample(); // GH-1473 - pre-tick race condition NPE
    }

    @Nonnull
    @Override
    public List<PlayerProfile> getPlayerSample() {
        List<PlayerProfile> sample = super.getPlayerSample();

        if (this.originalSample != null) {
            for (GameProfile profile : this.originalSample) {
                sample.add(CraftPlayerProfile.asBukkitCopy(profile));
            }
            this.originalSample = null;
        }

        return sample;
    }

    private GameProfile[] getPlayerSampleHandle() {
        if (this.originalSample != null) {
            return this.originalSample;
        }

        List<PlayerProfile> entries = super.getPlayerSample();
        if (entries.isEmpty()) {
            return EMPTY_PROFILES;
        }

        GameProfile[] profiles = new GameProfile[entries.size()];
        for (int i = 0; i < profiles.length; i++) {
            /*
             * Avoid null UUIDs/names since that will make the response invalid
             * on the client.
             * Instead, fall back to a fake/empty UUID and an empty string as name.
             * This can be used to create custom lines in the player list that do not
             * refer to a specific player.
             */

            PlayerProfile profile = entries.get(i);
            if (profile.getId() != null && profile.getName() != null) {
                profiles[i] = CraftPlayerProfile.asAuthlib(profile);
            } else {
                profiles[i] = new GameProfile(MoreObjects.firstNonNull(profile.getId(), FAKE_UUID), Strings.nullToEmpty(profile.getName()));
            }
        }

        return profiles;
    }

    @SuppressWarnings("deprecation")
    public static void processRequest(MinecraftServer server, NetworkManager networkManager) {
        StandardPaperServerListPingEventImpl event = new StandardPaperServerListPingEventImpl(server, networkManager, server.getServerPing());
        server.server.getPluginManager().callEvent(event);

        // Close connection immediately if event is cancelled
        if (event.isCancelled()) {
            networkManager.close(null);
            return;
        }

        // Setup response
        ServerPing ping = new ServerPing();

        // Description
        ping.setMOTD(CraftChatMessage.fromString(event.getMotd(), true)[0]);

        // Players
        if (!event.shouldHidePlayers()) {
            ping.setPlayerSample(new ServerPing.ServerPingPlayerSample(event.getMaxPlayers(), event.getNumPlayers()));
            ping.getPlayers().setSample(event.getPlayerSampleHandle());
        }

        // Version
        ping.setServerInfo(new ServerPing.ServerData(event.getVersion(), event.getProtocolVersion()));

        // Favicon
        if (event.getServerIcon() != null) {
            ping.setFavicon(event.getServerIcon().getData());
        }

        // Send response
        networkManager.sendPacket(new PacketStatusOutServerInfo(ping));
    }

}
