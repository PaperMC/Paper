package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.audience.Audience;

public interface PlayerConfigurationConnection extends PlayerCommonConnection {

    /**
     * Returns the audience representing the player in configuration mode.
     * This can be used to interact with the Adventure API during the configuration stage.
     * <p>
     * To access identifying information about this player, use {@link net.kyori.adventure.pointer.Pointered}
     * with the following pointers:
     * <ul>
     *   <li>{@link net.kyori.adventure.identity.Identity#UUID}</li>
     *   <li>{@link net.kyori.adventure.identity.Identity#NAME}</li>
     * </ul>
     *
     * @return the configuring player audience
     */
    Audience getConfiguringPlayer();

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

    /**
     * @param type client option
     * @return the client option value of the player
     */
    <T> T getClientOption(ClientOption<T> type);
}
