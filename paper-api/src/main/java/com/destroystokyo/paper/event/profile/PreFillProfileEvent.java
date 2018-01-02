/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
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
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Collection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when the server is requesting to fill in properties of an incomplete profile, such as textures.
 * <p>
 * Allows plugins to pre-populate cached properties and avoid a call to the Mojang API
 */
@NullMarked
public class PreFillProfileEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerProfile profile;

    @ApiStatus.Internal
    public PreFillProfileEvent(final PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    /**
     * @return The profile that needs its properties filled
     */
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    /**
     * Sets the properties on the profile, avoiding the call to the Mojang API
     * Same as .getPlayerProfile().setProperties(properties);
     *
     * @param properties The properties to set/append
     * @see PlayerProfile#setProperties(Collection)
     */
    public void setProperties(final Collection<ProfileProperty> properties) {
        this.profile.setProperties(properties);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
