package org.bukkit.scoreboard;

import io.papermc.paper.InternalAPIBridge;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents anything that can be used as an entry in a {@link Scoreboard}. Includes all entities,
 * players, and text.
 */
@ApiStatus.Experimental
@NullMarked
public interface ScoreHolder {

    /**
     * The name used to access this {@link ScoreHolder} on a scoreboard.
     * @return the name
     */
    String getScoreboardName();

    /**
     * The display name of this {@link ScoreHolder}.
     * @return the display name
     */
    default Component getDisplayName() {
        return Component.text(getScoreboardName());
    }

    /**
     * Wraps an {@link OfflinePlayer} into a {@link ScoreHolder}.
     * @param offlinePlayer the player to wrap
     * @return a {@link ScoreHolder} representing the given player
     */
    static ScoreHolder of(OfflinePlayer offlinePlayer) {
        return InternalAPIBridge.get().scoreHolderOf(offlinePlayer);
    }

    /**
     * Wraps a {@link String} into a {@link ScoreHolder}.
     * @param entry the text to warp
     * @return a {@link ScoreHolder} representing the given String
     */
    static ScoreHolder of(String entry) {
        return InternalAPIBridge.get().scoreHolderOf(entry);
    }
}
