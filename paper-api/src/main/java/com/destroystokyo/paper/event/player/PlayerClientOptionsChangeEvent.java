package com.destroystokyo.paper.event.player;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.ClientOption.ChatVisibility;
import com.destroystokyo.paper.ClientOption.ParticleVisibility;
import com.destroystokyo.paper.SkinParts;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.MainHand;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the player changes their client settings
 */
@NullMarked
public class PlayerClientOptionsChangeEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String locale;
    private final int viewDistance;
    private final ChatVisibility chatVisibility;
    private final boolean chatColors;
    private final SkinParts skinparts;
    private final MainHand mainHand;
    private final boolean allowsServerListings;
    private final boolean textFilteringEnabled;
    private final ParticleVisibility particleVisibility;

    @ApiStatus.Internal
    public PlayerClientOptionsChangeEvent(final Player player, final Map<ClientOption<?>, ?> options) {
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

    public String getLocale() {
        return this.locale;
    }

    public boolean hasLocaleChanged() {
        return !this.locale.equals(this.player.getClientOption(ClientOption.LOCALE));
    }

    public int getViewDistance() {
        return this.viewDistance;
    }

    public boolean hasViewDistanceChanged() {
        return this.viewDistance != this.player.getClientOption(ClientOption.VIEW_DISTANCE);
    }

    public ChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public boolean hasChatVisibilityChanged() {
        return this.chatVisibility != this.player.getClientOption(ClientOption.CHAT_VISIBILITY);
    }

    public boolean hasChatColorsEnabled() {
        return this.chatColors;
    }

    public boolean hasChatColorsEnabledChanged() {
        return this.chatColors != this.player.getClientOption(ClientOption.CHAT_COLORS_ENABLED);
    }

    public SkinParts getSkinParts() {
        return this.skinparts;
    }

    public boolean hasSkinPartsChanged() {
        return this.skinparts.getRaw() != this.player.getClientOption(ClientOption.SKIN_PARTS).getRaw();
    }

    public MainHand getMainHand() {
        return this.mainHand;
    }

    public boolean hasMainHandChanged() {
        return this.mainHand != this.player.getClientOption(ClientOption.MAIN_HAND);
    }

    public boolean hasTextFilteringEnabled() {
        return this.textFilteringEnabled;
    }

    public boolean hasTextFilteringChanged() {
        return this.textFilteringEnabled != this.player.getClientOption(ClientOption.TEXT_FILTERING_ENABLED);
    }

    public boolean allowsServerListings() {
        return this.allowsServerListings;
    }

    public boolean hasAllowServerListingsChanged() {
        return this.allowsServerListings != this.player.getClientOption(ClientOption.ALLOW_SERVER_LISTINGS);
    }

    public ParticleVisibility getParticleVisibility() {
        return this.particleVisibility;
    }

    public boolean hasParticleVisibilityChanged() {
        return this.particleVisibility != this.player.getClientOption(ClientOption.PARTICLE_VISIBILITY);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
