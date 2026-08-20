package io.papermc.paper.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;

/**
 * Fires when computing if a server is currently considered full for a player.
 */
public interface PlayerServerFullCheckEvent extends Event {

    /**
     * @return the profile of the player trying to connect
     */
    PlayerProfile getPlayerProfile();

    /**
     * @return the currently planned message to send to the user if they are unable to join the server
     */
    @Contract(pure = true)
    Component kickMessage();

    /**
     * Denies the player access to join this server.
     *
     * @param kickMessage The message to send to the player on kick if not able to join.
     */
    void deny(final Component kickMessage);

    /**
     * Sets whether the player is able to join this server.
     *
     * @param allow can join the server
     */
    void allow(boolean allow);

    /**
     * Gets if the player is currently able to join the server.
     *
     * @return can join the server, or {@code false} if the server should be considered full
     */
    boolean isAllowed();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
