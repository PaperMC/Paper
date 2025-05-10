package org.bukkit.craftbukkit.scoreboard;

import io.papermc.paper.adventure.PaperAdventure;
import org.bukkit.scoreboard.ScoreHolder;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftScoreHolder implements ScoreHolder {

    private final net.minecraft.world.scores.ScoreHolder handle;

    public CraftScoreHolder(net.minecraft.world.scores.ScoreHolder nmsScoreHolder) {
        this.handle = nmsScoreHolder;
    }

    @Override
    public String getScoreboardName() {
        return handle.getScoreboardName();
    }

    @Override
    public Component getDisplayName() {
        return PaperAdventure.asAdventure(handle.getDisplayName());
    }

    public net.minecraft.world.scores.ScoreHolder getHandle() {
        return handle;
    }
}
