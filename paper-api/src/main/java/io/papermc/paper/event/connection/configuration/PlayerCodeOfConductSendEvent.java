package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.event.connection.ConnectionEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * This event is called when the code of conduct is potentially sent to the player.
 */
public interface PlayerCodeOfConductSendEvent extends ConnectionEvent {

    @Override
    PlayerCommonConnection getConnection();

    /**
     * Gets the code of conduct to be sent.
     *
     * @return the code of conduct or null if none will be sent
     */
    @Nullable String getCodeOfConduct();

    /**
     * Sets the code of conduct to be sent.
     *
     * @param codeOfConduct the code of conduct or null to not send one
     */
    void setCodeOfConduct(@Nullable String codeOfConduct);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
