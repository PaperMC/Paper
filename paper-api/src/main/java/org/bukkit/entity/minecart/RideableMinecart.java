package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;

/// Represents a minecart that can have certain
/// [`entities`][org.bukkit.entity.Entity] as passengers. Normal passengers
/// include all [`living entities`][org.bukkit.entity.LivingEntity] with
/// the exception of [`iron golems`][org.bukkit.entity.IronGolem].
/// Non-player entities that meet normal passenger criteria automatically
/// mount these minecarts when close enough.
public interface RideableMinecart extends Minecart {
}
