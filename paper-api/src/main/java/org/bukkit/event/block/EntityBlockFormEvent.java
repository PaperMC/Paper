package org.bukkit.event.block;

import org.bukkit.event.entity.EntityEvent;

/**
 * Called when a block is formed by entities.
 * <p>
 * Examples:
 * <ul>
 * <li>Snow formed by a {@link org.bukkit.entity.Snowman}.
 * <li>Frosted Ice formed by the Frost Walker enchantment.
 * </ul>
 */
public interface EntityBlockFormEvent extends BlockFormEvent, EntityEvent {
}
