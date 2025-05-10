package org.bukkit.scoreboard;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ScoreHolder {
    String getScoreboardName();

    default Component getDisplayName() {
        return Component.text(getScoreboardName());
    }
}
