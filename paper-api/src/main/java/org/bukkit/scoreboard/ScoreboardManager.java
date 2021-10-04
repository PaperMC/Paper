package org.bukkit.scoreboard;

import java.lang.ref.WeakReference;
import org.jetbrains.annotations.NotNull;

/**
 * Manager of Scoreboards
 */
public interface ScoreboardManager {

    /**
     * Gets the primary Scoreboard controlled by the server.
     * <p>
     * This Scoreboard is saved by the server, is affected by the /scoreboard
     * command, and is the scoreboard shown by default to players.
     *
     * @return the default server scoreboard
     */
    @NotNull
    Scoreboard getMainScoreboard();

    /**
     * Gets a new Scoreboard to be tracked by the server. This scoreboard will
     * be tracked as long as a reference is kept, either by a player or by a
     * plugin.
     *
     * @return the registered Scoreboard
     * @see WeakReference
     */
    @NotNull
    Scoreboard getNewScoreboard();
}
