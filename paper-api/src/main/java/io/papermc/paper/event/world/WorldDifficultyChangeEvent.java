package io.papermc.paper.event.world;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jspecify.annotations.Nullable;

/**
 * Called when a world's difficulty is changed, either by command or by api.
 * <p>
 * If the world is in {@link World#isHardcore() hardcore}
 * the resulting difficulty will always be {@link Difficulty#HARD}
 */
public interface WorldDifficultyChangeEvent extends WorldEvent {

    /**
     * Gets the command source associated with this event.
     *
     * @return {@code null} if the difficulty was changed via api, otherwise the {@link CommandSourceStack}.
     */
    @Nullable CommandSourceStack getCommandSource();

    /**
     * Gets the new difficulty of the world.
     *
     * @return the new difficulty.
     */
    Difficulty getDifficulty();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
