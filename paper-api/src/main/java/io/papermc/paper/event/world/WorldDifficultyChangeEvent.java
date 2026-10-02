package io.papermc.paper.event.world;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// Called when a world's difficulty is changed, either by command or by api.
///
/// If the world is in [`hardcore`][World#isHardcore()]
/// the resulting difficulty will always be [Difficulty#HARD]
@NullMarked
public class WorldDifficultyChangeEvent extends WorldEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable CommandSourceStack commandSource;
    private final Difficulty difficulty;

    @ApiStatus.Internal
    public WorldDifficultyChangeEvent(final World world, final @Nullable CommandSourceStack commandSource, final Difficulty difficulty) {
        super(world);
        this.commandSource = commandSource;
        this.difficulty = difficulty;
    }

    /// Gets the command source associated with this event.
    ///
    /// @return `null` if the difficulty was changed via api, otherwise the [CommandSourceStack].
    public @Nullable CommandSourceStack getCommandSource() {
        return this.commandSource;
    }

    /// Gets the new difficulty of the world.
    ///
    /// @return the new difficulty.
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
