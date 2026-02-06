package com.destroystokyo.paper.event.player;

import com.destroystokyo.paper.ClientOption.ChatVisibility;
import com.destroystokyo.paper.ClientOption.ParticleVisibility;
import com.destroystokyo.paper.SkinParts;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.MainHand;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the player changes their client settings
 */
@NullMarked
public interface PlayerClientOptionsChangeEvent extends PlayerEventNew { // todo javadocs?

    String getLocale();

    boolean hasLocaleChanged();

    int getViewDistance();

    boolean hasViewDistanceChanged();

    ChatVisibility getChatVisibility();

    boolean hasChatVisibilityChanged();

    boolean hasChatColorsEnabled();

    boolean hasChatColorsEnabledChanged();

    SkinParts getSkinParts();

    boolean hasSkinPartsChanged();

    MainHand getMainHand();

    boolean hasMainHandChanged();

    boolean hasTextFilteringEnabled();

    boolean hasTextFilteringChanged();

    boolean allowsServerListings();

    boolean hasAllowServerListingsChanged();

    ParticleVisibility getParticleVisibility();

    boolean hasParticleVisibilityChanged();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
