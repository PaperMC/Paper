package io.papermc.paper.connection;

import java.util.Optional;
import org.bukkit.event.player.PlayerKickEvent;

// This class is used for when there may be a want for a common disconnect event for the configuration stage.
public interface DisconnectionReason {

    DisconnectionReason UNKNOWN = game(PlayerKickEvent.Cause.UNKNOWN);
    DisconnectionReason TIMEOUT = game(PlayerKickEvent.Cause.TIMEOUT);
    DisconnectionReason RESOURCE_PACK_REJECTION = game(PlayerKickEvent.Cause.RESOURCE_PACK_REJECTION);
    DisconnectionReason INVALID_COOKIE = game(PlayerKickEvent.Cause.INVALID_COOKIE);
    DisconnectionReason DUPLICATE_LOGIN_MESSAGE = game(PlayerKickEvent.Cause.DUPLICATE_LOGIN);
    DisconnectionReason INVALID_PAYLOAD = game(PlayerKickEvent.Cause.INVALID_PAYLOAD);

    Optional<PlayerKickEvent.Cause> game();

    static DisconnectionReason game(PlayerKickEvent.Cause cause) {
        return new GameEntry(cause);
    }

    record GameEntry(PlayerKickEvent.Cause gameQuitEvent) implements DisconnectionReason {

        @Override
        public Optional<PlayerKickEvent.Cause> game() {
            return Optional.ofNullable(this.gameQuitEvent);
        }
    }
}
