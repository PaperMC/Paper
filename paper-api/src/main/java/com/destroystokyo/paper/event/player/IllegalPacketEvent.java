package com.destroystokyo.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.Nullable;

/**
 * @deprecated not called anymore
 */
@Deprecated(since = "1.16.4", forRemoval = true)
public interface IllegalPacketEvent extends PlayerEventNew {

    boolean isShouldKick();

    void setShouldKick(boolean shouldKick);

    @Nullable String getKickMessage();

    void setKickMessage(@Nullable String kickMessage);

    @Nullable String getType();

    @Nullable String getExceptionMessage();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
