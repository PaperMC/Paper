package com.destroystokyo.paper.event.entity;

/**
 * Fired when a Slime decides to start jumping while swimming in water/lava.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start jumping.
 */
public interface SlimeSwimEvent extends SlimeWanderEvent {
}
