package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.net.InetAddress;

public interface PlayerConfigurationConnection extends PlayerCommonConnection {

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
    void completeConfiguration();

    /**
     * @param type client option
     * @return the client option value of the player
     */
    <T> T getClientOption(ClientOption<T> type);

}
