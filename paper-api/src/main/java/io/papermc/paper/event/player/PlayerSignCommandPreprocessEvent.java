package io.papermc.paper.event.player;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link Player} clicks a side on a sign that causes a command to run.
 * <p>
 * This command is run with elevated permissions which allows players to access commands on signs they wouldn't
 * normally be able to run.
 */
@NullMarked
public interface PlayerSignCommandPreprocessEvent extends PlayerCommandPreprocessEvent {

    /**
     * Gets the sign that the command originated from.
     *
     * @return the sign
     */
    Sign getSign();

    /**
     * Gets the side of the sign that the command originated from.
     *
     * @return the sign side
     */
    Side getSide();
}
