package io.papermc.paper.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event is called when the code of conduct is potentially sent to the player.
 */
@ApiStatus.Experimental
@NullMarked
public class PlayerCodeOfConductSendEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private @Nullable String codeOfConduct;
    private final PlayerCommonConnection connection;

    @ApiStatus.Internal
    public PlayerCodeOfConductSendEvent(final PlayerConfigurationConnection connection, final @Nullable String codeOfConduct) {
        this.connection = connection;
        this.codeOfConduct = codeOfConduct;
    }

    /**
     * Gets the connection that will receive the code of conduct.
     *
     * @return connection
     */
    public PlayerCommonConnection getConnection() {
        return connection;
    }

    /**
     * Gets the code of conduct to be sent.
     *
     * @return the code of conduct or null if none will be sent
     */
    public @Nullable String getCodeOfConduct() {
        return this.codeOfConduct;
    }

    /**
     * Sets the code of conduct to be sent.
     *
     * @param codeOfConduct the code of conduct or null to not send one
     */
    public void setCodeOfConduct(final @Nullable String codeOfConduct) {
        this.codeOfConduct = codeOfConduct;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
