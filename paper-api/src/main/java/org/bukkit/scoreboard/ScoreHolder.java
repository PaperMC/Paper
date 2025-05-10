package org.bukkit.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Experimental
@NullMarked
public interface ScoreHolder {
    String getScoreboardName();

    default Component getDisplayName() {
        return Component.text(getScoreboardName());
    }
}
