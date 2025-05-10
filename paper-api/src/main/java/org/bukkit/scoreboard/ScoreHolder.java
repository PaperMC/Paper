package org.bukkit.scoreboard;

import io.papermc.paper.InternalAPIBridge;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Experimental
@NullMarked
public interface ScoreHolder {
    String getScoreboardName();

    default Component getDisplayName() {
        return Component.text(getScoreboardName());
    }
    
    static ScoreHolder of(OfflinePlayer offlinePlayer) {
        return InternalAPIBridge.get().scoreHolderOf(offlinePlayer);
    }
    
    static ScoreHolder of(String entry) {
        return InternalAPIBridge.get().scoreHolderOf(entry);
    }
}
