/*
 * Copyright (c) 2017 - Daniel Ennis (Aikar) - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Fires when the server needs to verify if a player is whitelisted.
 * <p>
 * Plugins may override/control the servers whitelist with this event,
 * and dynamically change the kick message.
 */
public interface ProfileWhitelistVerifyEvent extends Event {

    /**
     * @return the currently planned message to send to the user if they are not whitelisted
     * @deprecated use {@link #kickMessage()}
     */
    @Deprecated
    @Nullable String getKickMessage();

    /**
     * @param kickMessage The message to send to the player on kick if not whitelisted. May set to {@code null} to use the server configured default
     * @deprecated Use {@link #kickMessage(Component)}
     */
    @Deprecated
    void setKickMessage(@Nullable String kickMessage);

    /**
     * @return the currently planned message to send to the user if they are not whitelisted
     */
    @Contract(pure = true)
    @Nullable Component kickMessage();

    /**
     * @param kickMessage The message to send to the player on kick if not whitelisted. May set to {@code null} to use the server configured default
     */
    void kickMessage(@Nullable Component kickMessage);

    /**
     * @return The profile of the player trying to connect
     */
    PlayerProfile getPlayerProfile();

    /**
     * @return Whether the player is whitelisted to play on this server (whitelist may be off is why it's true)
     */
    boolean isWhitelisted();

    /**
     * Changes the players whitelisted state. {@code false} will deny the login
     *
     * @param whitelisted The new whitelisted state
     */
    void setWhitelisted(boolean whitelisted);

    /**
     * @return if the player obtained whitelist status by having op
     */
    boolean isOp();

    /**
     * @return if the server even has whitelist on
     */
    boolean isWhitelistEnabled();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
