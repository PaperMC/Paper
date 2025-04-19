package io.papermc.paper.connection;

import io.papermc.paper.adventure.PaperAdventure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
import net.minecraft.network.protocol.common.ClientboundTransferPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.bukkit.ServerLinks;
import org.bukkit.craftbukkit.CraftServerLinks;
import org.jetbrains.annotations.NotNull;

public abstract class PaperCommonConnection<T extends ServerCommonPacketListenerImpl> extends CommonCookieConnection implements PlayerCommonConnection {

    protected final T handle;

    public PaperCommonConnection(final T serverConfigurationPacketListenerImpl) {
        super(serverConfigurationPacketListenerImpl.connection);
        this.handle = serverConfigurationPacketListenerImpl;
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        final List<ClientboundResourcePackPushPacket> packs = new java.util.ArrayList<>(request.packs().size());
        if (request.replace()) {
            this.clearResourcePacks();
        }
        final net.minecraft.network.chat.Component prompt = io.papermc.paper.adventure.PaperAdventure.asVanilla(request.prompt());
        for (final java.util.Iterator<net.kyori.adventure.resource.ResourcePackInfo> iter = request.packs().iterator(); iter.hasNext();) {
            final net.kyori.adventure.resource.ResourcePackInfo pack = iter.next();
            packs.add(new ClientboundResourcePackPushPacket(pack.id(), pack.uri().toASCIIString(), pack.hash(), request.required(), iter.hasNext() ? Optional.empty() : Optional.ofNullable(prompt)));
            if (request.callback() != net.kyori.adventure.resource.ResourcePackCallback.noOp()) {
                this.handle.packCallbacks.put(pack.id(), request.callback()); // just override if there is a previously existing callback
            }
        }
        this.sendBundle(packs);
    }

    @Override
    public void removeResourcePacks(@NotNull UUID id, @NotNull UUID... others) {
        this.sendBundle(net.kyori.adventure.util.MonkeyBars.nonEmptyArrayToList(pack -> new ClientboundResourcePackPopPacket(Optional.of(pack)), id, others));
    }

    @Override
    public void clearResourcePacks() {
        this.handle.connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void sendBundle(final List<? extends net.minecraft.network.protocol.Packet<? extends net.minecraft.network.protocol.common.ClientCommonPacketListener>> packet) {
        this.handle.connection.send(new net.minecraft.network.protocol.game.ClientboundBundlePacket((List) packet));
    }

    @Override
    public void sendReportDetails(final Map<String, String> details) {
        this.handle.send(new ClientboundCustomReportDetailsPacket(details));
    }

    @Override
    public void sendLinks(final ServerLinks links) {
        this.handle.send(new ClientboundServerLinksPacket(((CraftServerLinks) links).getServerLinks().untrust()));
    }

    @Override
    public void transfer(final String host, final int port) {
        this.handle.send(new ClientboundTransferPacket(host, port));
    }

    @Override
    public void disconnect(final Component component) {
        this.handle.disconnect(PaperAdventure.asVanilla(component), DisconnectionReason.UNKNOWN);
    }

    @Override
    public boolean isTransferred() {
        return this.handle.isTransferred();
    }

    @Override
    public String getBrand() {
        return this.handle.playerBrand;
    }
}
