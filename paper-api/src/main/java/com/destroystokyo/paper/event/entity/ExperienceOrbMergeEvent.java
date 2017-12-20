/*
 * Copyright (c) 2017 Daniel Ennis (Aikar) MIT License
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

package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired anytime the server is about to merge 2 experience orbs into one
 */
@NullMarked
public class ExperienceOrbMergeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ExperienceOrb mergeTarget;
    private final ExperienceOrb mergeSource;

    private boolean cancelled;

    @ApiStatus.Internal
    public ExperienceOrbMergeEvent(final ExperienceOrb mergeTarget, final ExperienceOrb mergeSource) {
        super(mergeTarget);
        this.mergeTarget = mergeTarget;
        this.mergeSource = mergeSource;
    }

    /**
     * @return The orb that will absorb the other experience orb
     */
    public ExperienceOrb getMergeTarget() {
        return this.mergeTarget;
    }

    /**
     * @return The orb that is subject to being removed and merged into the target orb
     */
    public ExperienceOrb getMergeSource() {
        return this.mergeSource;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @param cancel {@code true} if you wish to cancel this event, and prevent the orbs from merging
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
