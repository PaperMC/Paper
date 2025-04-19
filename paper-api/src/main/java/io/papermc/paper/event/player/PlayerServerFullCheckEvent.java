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

package io.papermc.paper.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Fires when a server is currently considered "full", and is executed for each player
 * trying to join the server while it is in this state.
 */
@NullMarked
public class PlayerServerFullCheckEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerProfile profile;
    private Component kickMessage;
    private boolean canJoin = false;

    @ApiStatus.Internal
    public PlayerServerFullCheckEvent(final PlayerProfile profile, final Component kickMessage) {
        this.profile = profile;
        this.kickMessage = kickMessage;
    }

    /**
     * @return the currently planned message to send to the user if they are not whitelisted
     */
    @Contract(pure = true)
    public Component kickMessage() {
        return this.kickMessage;
    }

    /**
     * @param kickMessage The message to send to the player on kick if not able to join.
     */
    public void kickMessage(final Component kickMessage) {
        this.kickMessage = kickMessage;
    }

    /**
     * @return The profile of the player trying to connect
     */
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    /**
     * Forcibly allows you to override if a player is able to join the server despite the fullness check.
     * @param canJoin can join the server
     */
    public void setCanJoin(final boolean canJoin) {
        this.canJoin = canJoin;
    }

    /**
     * Gets if the player is currently able to join the server despite the fullness check.
     *
     * @return can skip
     */
    public boolean canJoin() {
        return this.canJoin;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
