package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Slime;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a Slime decides to start jumping while swimming in water/lava.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start jumping.
 */
@NullMarked
public class SlimeSwimEvent extends SlimeWanderEvent {

    @ApiStatus.Internal
    public SlimeSwimEvent(final Slime slime) {
        super(slime);
    }
}
