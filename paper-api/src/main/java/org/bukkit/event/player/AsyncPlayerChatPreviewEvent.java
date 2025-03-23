package org.bukkit.event.player;

import java.util.Set;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Used to format chat for chat preview. If this event is used, then the result
 * of the corresponding {@link AsyncPlayerChatEvent} <b>must</b> be formatted in
 * the same way.
 *
 * @deprecated chat previews have been removed
 */
@Deprecated(since = "1.19.1")
@Warning
public class AsyncPlayerChatPreviewEvent extends AsyncPlayerChatEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public AsyncPlayerChatPreviewEvent(final boolean async, @NotNull final Player player, @NotNull final String message, @NotNull final Set<Player> players) {
        super(async, player, message, players);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
