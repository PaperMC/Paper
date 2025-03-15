package io.papermc.paper.connection;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ConfiguringPlayer implements Audience {

    private final ServerConfigurationPacketListenerImpl packetListener;
    private @Nullable Pointers adventurePointers;

    public ConfiguringPlayer(final ServerConfigurationPacketListenerImpl packetListener) {
        this.packetListener = packetListener;
    }

    @Override
    public void sendResourcePacks(ResourcePackRequest request) {
        final List<ClientboundResourcePackPushPacket> packs = new java.util.ArrayList<>(request.packs().size());
        if (request.replace()) {
            this.clearResourcePacks();
        }
        final net.minecraft.network.chat.Component prompt = PaperAdventure.asVanilla(request.prompt());
        for (final java.util.Iterator<ResourcePackInfo> iter = request.packs().iterator(); iter.hasNext(); ) {
            final ResourcePackInfo pack = iter.next();
            packs.add(new ClientboundResourcePackPushPacket(pack.id(), pack.uri().toASCIIString(), pack.hash(), request.required(), iter.hasNext() ? Optional.empty() : Optional.ofNullable(prompt)));
            if (request.callback() != ResourcePackCallback.noOp()) {
                this.packetListener.packCallbacks.put(pack.id(), request.callback()); // just override if there is a previously existing callback
            }
        }
        packs.forEach(this.packetListener::send);
    }

    @Override
    public void removeResourcePacks(UUID id, UUID... others) {
        net.kyori.adventure.util.MonkeyBars.nonEmptyArrayToList(pack -> new ClientboundResourcePackPopPacket(Optional.of(pack)), id, others).forEach(this.packetListener::send);
    }

    @Override
    public void clearResourcePacks() {
        this.packetListener.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }

    @Override
    public Pointers pointers() {
        if (this.adventurePointers == null) {
            this.adventurePointers = Pointers.builder()
                .withDynamic(Identity.NAME, () -> this.packetListener.getOwner().getName())
                .withDynamic(Identity.UUID, () -> this.packetListener.getOwner().getId())
                .build();
        }

        return this.adventurePointers;
    }
}
