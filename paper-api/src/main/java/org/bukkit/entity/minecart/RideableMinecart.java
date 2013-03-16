package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;

/**
 * Represents a minecart that can have certain {@link
 * org.bukkit.entity.Entity entities} as passengers. Normal passengers
 * include all {@link org.bukkit.entity.LivingEntity living entities} with
 * the exception of {@link org.bukkit.entity.IronGolem iron golems}.
 * Non-player entities that meet normal passenger criteria automatically
 * mount these minecarts when close enough.
 */
public interface RideableMinecart extends Minecart {
}
