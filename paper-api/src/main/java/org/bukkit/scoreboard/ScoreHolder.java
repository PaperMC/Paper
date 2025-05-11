package org.bukkit.scoreboard;

import io.papermc.paper.InternalAPIBridge;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents anything that can be used as an entry in a {@link Scoreboard}.
 * Includes entities, players, and text.
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
    default Component getScoreDisplayName() {
        return Component.text(getScoreboardName());
    }

    /**
     * Wraps a {@link String} into a {@link ScoreHolder}.
     * @param entry the text to warp
     * @return a {@link ScoreHolder} representing the given String
     */
    static ScoreHolder of(String entry) {
        return InternalAPIBridge.get().scoreHolderOf(entry);
    }

    /**
     * A simple {@link ScoreHolder} which holds a string entry
     */
    interface StringScoreHolder extends ScoreHolder {}
}
