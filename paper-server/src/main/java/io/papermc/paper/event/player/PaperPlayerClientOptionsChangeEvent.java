package io.papermc.paper.event.player;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.SkinParts;
import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;

public class PaperPlayerClientOptionsChangeEvent extends CraftPlayerEvent implements PlayerClientOptionsChangeEvent {

    private final String locale;
    private final int viewDistance;
    private final ClientOption.ChatVisibility chatVisibility;
    private final boolean chatColors;
    private final SkinParts skinparts;
    private final MainHand mainHand;
    private final boolean allowsServerListings;
    private final boolean textFilteringEnabled;
    private final ClientOption.ParticleVisibility particleVisibility;

    public PaperPlayerClientOptionsChangeEvent(final ServerPlayer player, final ClientInformation information) {
        super(player.getBukkitEntity());

        this.locale = information.language();
        this.viewDistance = information.viewDistance();
        this.chatVisibility = ClientOption.ChatVisibility.valueOf(information.chatVisibility().name());
        this.chatColors = information.chatColors();
        this.skinparts = new PaperSkinParts(information.modelCustomisation());
        this.mainHand = information.mainHand() == HumanoidArm.LEFT ? MainHand.LEFT : MainHand.RIGHT;
        this.allowsServerListings = information.allowsListing();
        this.textFilteringEnabled = information.textFilteringEnabled();
        this.particleVisibility = ClientOption.ParticleVisibility.valueOf(information.particleStatus().name());
    }

    @Override
    public String getLocale() {
        return this.locale;
    }

    @Override
    public boolean hasLocaleChanged() {
        return !this.locale.equals(this.player.getClientOption(ClientOption.LOCALE));
    }

    @Override
    public int getViewDistance() {
        return this.viewDistance;
    }

    @Override
    public boolean hasViewDistanceChanged() {
        return this.viewDistance != this.player.getClientOption(ClientOption.VIEW_DISTANCE);
    }

    @Override
    public ClientOption.ChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    @Override
    public boolean hasChatVisibilityChanged() {
        return this.chatVisibility != this.player.getClientOption(ClientOption.CHAT_VISIBILITY);
    }

    @Override
    public boolean hasChatColorsEnabled() {
        return this.chatColors;
    }

    @Override
    public boolean hasChatColorsEnabledChanged() {
        return this.chatColors != this.player.getClientOption(ClientOption.CHAT_COLORS_ENABLED);
    }

    @Override
    public SkinParts getSkinParts() {
        return this.skinparts;
    }

    @Override
    public boolean hasSkinPartsChanged() {
        return this.skinparts.getRaw() != this.player.getClientOption(ClientOption.SKIN_PARTS).getRaw();
    }

    @Override
    public MainHand getMainHand() {
        return this.mainHand;
    }

    @Override
    public boolean hasMainHandChanged() {
        return this.mainHand != this.player.getClientOption(ClientOption.MAIN_HAND);
    }

    @Override
    public boolean hasTextFilteringEnabled() {
        return this.textFilteringEnabled;
    }

    @Override
    public boolean hasTextFilteringChanged() {
        return this.textFilteringEnabled != this.player.getClientOption(ClientOption.TEXT_FILTERING_ENABLED);
    }

    @Override
    public boolean allowsServerListings() {
        return this.allowsServerListings;
    }

    @Override
    public boolean hasAllowServerListingsChanged() {
        return this.allowsServerListings != this.player.getClientOption(ClientOption.ALLOW_SERVER_LISTINGS);
    }

    @Override
    public ClientOption.ParticleVisibility getParticleVisibility() {
        return this.particleVisibility;
    }

    @Override
    public boolean hasParticleVisibilityChanged() {
        return this.particleVisibility != this.player.getClientOption(ClientOption.PARTICLE_VISIBILITY);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerClientOptionsChangeEvent.getHandlerList();
    }
}
