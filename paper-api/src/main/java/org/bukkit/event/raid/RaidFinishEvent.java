package org.bukkit.event.raid;

import java.util.List;
import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

/**
 * This event is called when a {@link Raid} was complete with a clear result.
 */
public interface RaidFinishEvent extends RaidEvent {

    /**
     * Returns an immutable list contains all winners.
     * <br>
     * <b>Note: Players who are considered as heroes but were not online at the
     * end would not be included in this list.</b>
     *
     * @return the winners
     */
    @Unmodifiable List<Player> getWinners();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
