package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import org.bukkit.scoreboard.ScoreHolder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CraftScoreHolder extends ScoreHolder {

    @Override
    default String getScoreboardName() {
        return this.asNmsScoreHolder().getScoreboardName();
    }

    @Override
    default Component getScoreDisplayName() {
        return PaperAdventure.asAdventure(asNmsScoreHolder().getDisplayName());
    }

    net.minecraft.world.scores.ScoreHolder asNmsScoreHolder();

    static CraftScoreHolder fromNms(net.minecraft.world.scores.ScoreHolder nmsHolder) {
        if (nmsHolder instanceof Entity nmsEntity) {
            return nmsEntity.getBukkitEntity();
        }
        return new CraftStringScoreHolder(nmsHolder.getScoreboardName());
    }

    class CraftStringScoreHolder implements CraftScoreHolder, net.minecraft.world.scores.ScoreHolder {
        private final String name;

        public CraftStringScoreHolder(final String name) {
            Preconditions.checkArgument(name != null, "Name cannot be null");
            Preconditions.checkArgument(name.length() <= Short.MAX_VALUE, "The name '%s' is longer than the limit of 32767 characters (%s)", name, name.length());
            this.name = name;
        }

        @Override
        public String getScoreboardName() {
            return this.name;
        }

        @Override
        public net.minecraft.world.scores.ScoreHolder asNmsScoreHolder() {
            return this;
        }

        @Override
        public String toString() {
            return "CraftStringScoreHolder{" +
                "name='" + this.name + '\'' +
                '}';
        }
    }
}
