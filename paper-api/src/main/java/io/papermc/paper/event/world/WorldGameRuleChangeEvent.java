package io.papermc.paper.event.world;

import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jspecify.annotations.Nullable;

/**
 * Called when a world's gamerule is changed, either by command, world options menu, or by api.
 * @see <a href="https://minecraft.wiki/w/Game_rule#Modifying_game_rules">Modifying game rules - Minecraft wiki</a>
 */
public interface WorldGameRuleChangeEvent extends WorldEvent, Cancellable {

    /**
     * Gets the command sender associated with this event.
     *
     * @return {@code null} if the gamerule was changed via api, otherwise the {@link CommandSender}.
     */
    @Nullable CommandSender getCommandSender();

    /**
     * Gets the game rule associated with this event.
     *
     * @return the gamerule being changed.
     */
    GameRule<?> getGameRule();

    /**
     * Gets the new value of the gamerule.
     *
     * @return the new value of the gamerule.
     */
    String getValue();

    /**
     * Sets the new value of this gamerule.
     *
     * @param value the new value of the gamerule.
     */
    void setValue(final String value);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
