package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.PaperDialog;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.Nullable;

public class PaperPlayerConfigurationConnection extends PaperCommonConnection<ServerConfigurationPacketListenerImpl> implements PlayerConfigurationConnection, Audience {

    private @Nullable Pointers adventurePointers;

    public PaperPlayerConfigurationConnection(final ServerConfigurationPacketListenerImpl packetListener) {
        super(packetListener);
    }

    @Override
    public ClientInformation getClientInformation() {
        return this.handle.clientInformation;
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
                this.handle.packCallbacks.put(pack.id(), request.callback()); // just override if there is a previously existing callback
            }
        }
        packs.forEach(this.handle::send);
    }

    @Override
    public void removeResourcePacks(UUID id, UUID... others) {
        net.kyori.adventure.util.MonkeyBars.nonEmptyArrayToList(pack -> new ClientboundResourcePackPopPacket(Optional.of(pack)), id, others).forEach(this.handle::send);
    }

    @Override
    public void clearResourcePacks() {
        this.handle.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }

    @Override
    public void showDialog(final DialogLike dialog) {
        this.handle.send(new ClientboundShowDialogPacket(PaperDialog.bukkitToMinecraftHolder((Dialog) dialog)));
    }

    @Override
    public Pointers pointers() {
        if (this.adventurePointers == null) {
            this.adventurePointers = Pointers.builder()
                    .withDynamic(Identity.NAME, () -> this.handle.getOwner().getName())
                    .withDynamic(Identity.UUID, () -> this.handle.getOwner().getId())
                    .build();
        }

        return this.adventurePointers;
    }

    @Override
    public Audience getAudience() {
        return this;
    }

    @Override
    public PlayerProfile getProfile() {
        return CraftPlayerProfile.asBukkitCopy(this.handle.getOwner());
    }

    @Override
    public void clearChat() {
        this.handle.send(ClientboundResetChatPacket.INSTANCE);
    }

    @Override
    public void completeReconfiguration() {
        ConfigurationTask task = this.handle.currentTask;
        if (task != null) {
            // This means that the player is going through the normal configuration process, or is already returning to the game phase.
            // Be safe and just ignore, as many plugins may call this.
            return;
        }

        this.handle.returnToWorld();
    }
}
