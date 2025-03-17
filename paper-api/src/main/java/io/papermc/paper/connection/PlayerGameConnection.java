package io.papermc.paper.connection;

import org.bukkit.entity.Player;

public interface PlayerGameConnection extends CookieConnection {


    /**
     * Bumps the player to the configuration stage.
     * <p>
     * This will, by default, cause the player to stay until their connection is released by
     * {@link PlayerConfigurationConnection#completeConfiguration()}
     */
    void configurate();

    Player getPlayer();
}
