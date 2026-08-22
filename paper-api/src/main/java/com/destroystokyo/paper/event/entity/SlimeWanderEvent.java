package com.destroystokyo.paper.event.entity;

/**
 * Fired when a Slime decides to start wandering.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start moving.
 */
public interface SlimeWanderEvent extends SlimePathfindEvent {
}
