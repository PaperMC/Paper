package io.papermc.paper.event.world;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperWorldDifficultyChangeEvent extends CraftWorldEvent implements WorldDifficultyChangeEvent {

    private final @Nullable CommandSourceStack commandSource;
    private final Difficulty difficulty;

    public PaperWorldDifficultyChangeEvent(final World world, final @Nullable CommandSourceStack commandSource, final Difficulty difficulty) {
        super(world);
        this.commandSource = commandSource;
        this.difficulty = difficulty;
    }

    @Override
    public @Nullable CommandSourceStack getCommandSource() {
        return this.commandSource;
    }

    @Override
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    @Override
    public HandlerList getHandlers() {
        return WorldDifficultyChangeEvent.getHandlerList();
    }
}
