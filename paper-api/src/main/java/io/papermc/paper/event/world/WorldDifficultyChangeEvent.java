package io.papermc.paper.event.world;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a world's difficulty is changed, either by command or by api.
 * <p>
 * If the world is in {@link World#isHardcore() hardcore}
 * the resulting difficulty will always be {@link Difficulty#HARD}
 */
@NullMarked
public class WorldDifficultyChangeEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable CommandSender commandSender;
    private Difficulty difficulty;
    private boolean cancelled;

    public WorldDifficultyChangeEvent(final World world, final @Nullable CommandSender commandSender, final Difficulty difficulty) {
        super(world);
        this.commandSender = commandSender;
        this.difficulty = difficulty;
    }

    /**
     * Gets the command sender associated with this event.
     *
     * @return {@code null} if the difficulty was changed via api, otherwise the {@link CommandSender}.
     */
    public @Nullable CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Gets the new difficulty of the world.
     *
     * @return the new difficulty.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Set the new difficulty of the world.
     *
     * @param difficulty the new difficulty.
     */
    public void setDifficulty(final Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
