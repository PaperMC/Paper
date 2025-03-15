package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

public class PaperPlayerConfigurationConnection extends PaperCommonConnection<ServerConfigurationPacketListenerImpl> implements PlayerConfigurationConnection {

    private final ConfiguringPlayer configuringPlayer;

    public PaperPlayerConfigurationConnection(final ServerConfigurationPacketListenerImpl packetListener) {
        super(packetListener);
        this.configuringPlayer = new ConfiguringPlayer(packetListener);
    }

    @Override
    public Audience getConfiguringPlayer() {
        return this.configuringPlayer;
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
            throw new IllegalStateException("This current connection is already attempting to complete configuration. (FOUND: " + task.type().id() + ")");
        }

        this.handle.returnToWorld();
    }

    @Override
    public <T> T getClientOption(final ClientOption<T> type) {
        ClientInformation information = this.handle.clientInformation;

        if (ClientOption.SKIN_PARTS == type) {
            return type.getType().cast(new PaperSkinParts(information.modelCustomisation()));
        } else if (ClientOption.CHAT_COLORS_ENABLED == type) {
            return type.getType().cast(information.chatColors());
        } else if (ClientOption.CHAT_VISIBILITY == type) {
            return type.getType().cast(ClientOption.ChatVisibility.valueOf(information.chatVisibility().name()));
        } else if (ClientOption.LOCALE == type) {
            return type.getType().cast(information.language());
        } else if (ClientOption.MAIN_HAND == type) {
            return type.getType().cast(information.mainHand());
        } else if (ClientOption.VIEW_DISTANCE == type) {
            return type.getType().cast(information.viewDistance());
        } else if (ClientOption.TEXT_FILTERING_ENABLED == type) {
            return type.getType().cast(information.textFilteringEnabled());
        } else if (ClientOption.ALLOW_SERVER_LISTINGS == type) {
            return type.getType().cast(information.allowsListing());
        } else if (ClientOption.PARTICLE_VISIBILITY == type) {
            return type.getType().cast(ClientOption.ParticleVisibility.valueOf(information.particleStatus().name()));
        }
        throw new RuntimeException("Unknown settings type");
    }
}
