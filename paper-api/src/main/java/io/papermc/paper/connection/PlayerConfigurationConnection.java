package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.audience.Audience;

public interface PlayerConfigurationConnection extends PlayerCommonConnection {

    /**
     * Returns the audience representing the player in configuration mode.
     * This can be used to interact with the Adventure API during the configuration stage.
     * This is guaranteed to be an instance of {@link PlayerConfigurationConnection}
     *
     * @return the configuring player audience
     */
    Audience getAudience();

    /**
     * Gets the profile for this connection.
     *
     * @return profile
     */
    PlayerProfile getProfile();

    /**
     * Clears the players chat history and their local chat.
     */
    void clearChat();

    /**
     * Completes the configuration for this player, which will cause this player to reenter the game.
     * <p>
     * Note, this should be only be called if you are reconfiguring the player.
     */
    void completeReconfiguration();

}
