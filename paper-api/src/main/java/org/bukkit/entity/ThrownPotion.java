package org.bukkit.entity;

import java.util.Collection;

import org.bukkit.potion.PotionEffect;

/**
 * Represents a thrown potion bottle
 */
public interface ThrownPotion extends Projectile {
    /**
     * Returns the effects that are applied by this potion.
     * @return The potion effects
     */
    public Collection<PotionEffect> getEffects();
}
