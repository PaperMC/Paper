package io.papermc.paper.event.player;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.ClientOption.ChatVisibility;
import com.destroystokyo.paper.ClientOption.ParticleVisibility;
import com.destroystokyo.paper.SkinParts;
import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import java.util.Map;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;

public class PaperPlayerClientOptionsChangeEvent extends CraftPlayerEvent implements PlayerClientOptionsChangeEvent {

    private final String locale;
    private final int viewDistance;
    private final ChatVisibility chatVisibility;
    private final boolean chatColors;
    private final SkinParts skinparts;
    private final MainHand mainHand;
    private final boolean allowsServerListings;
    private final boolean textFilteringEnabled;
    private final ParticleVisibility particleVisibility;

    public PaperPlayerClientOptionsChangeEvent(final Player player, final Map<ClientOption<?>, ?> options) {
        super(player);

        this.locale = (String) options.get(ClientOption.LOCALE);
        this.viewDistance = (int) options.get(ClientOption.VIEW_DISTANCE);
        this.chatVisibility = (ChatVisibility) options.get(ClientOption.CHAT_VISIBILITY);
        this.chatColors = (boolean) options.get(ClientOption.CHAT_COLORS_ENABLED);
        this.skinparts = (SkinParts) options.get(ClientOption.SKIN_PARTS);
        this.mainHand = (MainHand) options.get(ClientOption.MAIN_HAND);
        this.allowsServerListings = (boolean) options.get(ClientOption.ALLOW_SERVER_LISTINGS);
        this.textFilteringEnabled = (boolean) options.get(ClientOption.TEXT_FILTERING_ENABLED);
        this.particleVisibility = (ParticleVisibility) options.get(ClientOption.PARTICLE_VISIBILITY);
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
    public ChatVisibility getChatVisibility() {
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
    public ParticleVisibility getParticleVisibility() {
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
