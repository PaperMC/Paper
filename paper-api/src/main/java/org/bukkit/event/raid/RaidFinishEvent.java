package org.bukkit.event.raid;

import java.util.Collections;
import java.util.List;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * This event is called when a {@link Raid} was complete with a clear result.
 */
public class RaidFinishEvent extends RaidEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Player> winners;

    @ApiStatus.Internal
    public RaidFinishEvent(@NotNull Raid raid, @NotNull World world, @NotNull List<Player> winners) {
        super(raid, world);
        this.winners = winners;
    }

    /**
     * Returns an immutable list contains all winners.
     * <br>
     * <b>Note: Players who are considered as heroes but were not online at the
     * end would not be included in this list.</b>
     *
     * @return the winners
     */
    @NotNull
    public @Unmodifiable List<Player> getWinners() {
        return Collections.unmodifiableList(this.winners);
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
