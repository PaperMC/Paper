package io.papermc.paper.event.player;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Runs when a player attempts to move, but is prevented from doing so by the server
 */
@NullMarked
public interface PlayerFailMoveEvent extends PlayerEventNew {

    /**
     * Gets the reason this movement was prevented by the server
     *
     * @return The reason the movement was prevented
     */
    FailReason getFailReason();

    /**
     * Gets the location this player moved from
     *
     * @return Location the player moved from
     */
    Location getFrom();

    /**
     * Gets the location this player tried to move to
     *
     * @return Location the player tried to move to
     */
    Location getTo();

    /**
     * Gets if the check should be bypassed, allowing the movement
     *
     * @return whether to bypass the check
     */
    boolean isAllowed();

    /**
     * Set if the check should be bypassed and the movement should be allowed
     *
     * @param allowed whether to bypass the check
     */
    void setAllowed(boolean allowed);

    /**
     * Gets if warnings will be printed to console. e.g. "Player123 moved too quickly!"
     *
     * @return whether to log warnings
     */
    boolean getLogWarning();

    /**
     * Set if a warning is printed to console. e.g. "Player123 moved too quickly!"
     *
     * @param logWarning whether to log warnings
     */
    void setLogWarning(boolean logWarning);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum FailReason {
        MOVED_INTO_UNLOADED_CHUNK, // Only fired if the world setting prevent-moving-into-unloaded-chunks is true
        MOVED_TOO_QUICKLY,
        MOVED_WRONGLY,
        CLIPPED_INTO_BLOCK
    }
}
