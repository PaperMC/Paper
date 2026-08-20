package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.event.HandlerList;

/**
 * Used to format chat for chat preview. If this event is used, then the result
 * of the corresponding {@link AsyncPlayerChatEvent} <b>must</b> be formatted in
 * the same way.
 *
 * @deprecated chat previews have been removed
 */
@Warning
@Deprecated(since = "1.19.1")
public interface AsyncPlayerChatPreviewEvent extends AsyncPlayerChatEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
