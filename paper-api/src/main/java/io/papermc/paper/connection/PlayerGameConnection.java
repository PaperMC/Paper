package io.papermc.paper.connection;

import org.bukkit.entity.Player;

public interface PlayerGameConnection extends PlayerCommonConnection {

    /**
     * Bumps the player to the configuration stage.
     * <p>
     * This will, by default, cause the player to stay until their connection is released by
     * {@link PlayerConfigurationConnection#completeConfiguration()}
     */
    void enterConfiguration();

    /**
     * Gets the player that is currently associated with this game connection.
     *
     * @return player
     */
    Player getPlayer();
}
