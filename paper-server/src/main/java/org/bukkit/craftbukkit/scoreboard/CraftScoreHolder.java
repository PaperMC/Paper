package org.bukkit.craftbukkit.scoreboard;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import org.bukkit.scoreboard.ScoreHolder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CraftScoreHolder extends ScoreHolder {

    @Override
    default String getScoreboardName() {
        return asNmsScoreHolder().getScoreboardName();
    }

    @Override
    default Component getScoreDisplayName() {
        return PaperAdventure.asAdventure(asNmsScoreHolder().getDisplayName());
    }

    net.minecraft.world.scores.ScoreHolder asNmsScoreHolder();

    static CraftScoreHolder fromNms(net.minecraft.world.scores.ScoreHolder nmsHolder) {
        return switch (nmsHolder) {
            case Entity nmsEntity -> nmsEntity.getBukkitEntity();
            default -> new CraftStringScoreHolder(nmsHolder.getScoreboardName());
        };
    }

    class CraftStringScoreHolder implements StringScoreHolder, CraftScoreHolder {
        private final String name;

        public CraftStringScoreHolder(final String name) {
            this.name = name;
        }

        @Override
        public net.minecraft.world.scores.ScoreHolder asNmsScoreHolder() {
            return () -> this.name;
        }
    }
}
