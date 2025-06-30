package io.papermc.paper.connection;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.jspecify.annotations.Nullable;

public interface PlayerLoginConnection extends ReadablePlayerCookieConnection {

    /**
     * Gets the authenticated profile for this connection.
     * This may return null depending on what stage this connection is at.
     *
     * @return authenticated profile, or null if not present
     */
    @Nullable PlayerProfile getAuthenticatedProfile();

    /**
     * Gets the player profile that this connection is requesting to authenticate as.
     *
     * @return the unsafe unauthenticated profile, or null if not sent
     */
    @Nullable
    PlayerProfile getUnsafeProfile();
}
