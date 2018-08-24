package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Slime;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a Slime decides to start wandering.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start moving.
 */
@NullMarked
public class SlimeWanderEvent extends SlimePathfindEvent {

    @ApiStatus.Internal
    public SlimeWanderEvent(final Slime slime) {
        super(slime);
    }
}
