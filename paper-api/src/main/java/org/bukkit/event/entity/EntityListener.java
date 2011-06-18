package org.bukkit.event.entity;

import org.bukkit.event.Listener;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.painting.PaintingBreakEvent;

/**
 * Handles all events fired in relation to entities
 */
public class EntityListener implements Listener {
    public EntityListener() {}

    public void onCreatureSpawn(CreatureSpawnEvent event) {}

    public void onItemSpawn(ItemSpawnEvent event) {}

    public void onEntityCombust(EntityCombustEvent event) {}

    public void onEntityDamage(EntityDamageEvent event) {}

    public void onEntityExplode(EntityExplodeEvent event) {}

    public void onExplosionPrime(ExplosionPrimeEvent event) {}

    public void onEntityDeath(EntityDeathEvent event) {}

    public void onEntityTarget(EntityTargetEvent event) {}

    public void onEntityInteract(EntityInteractEvent event) {}

    public void onEntityPortalEnter(EntityPortalEnterEvent event) {}

    public void onPaintingPlace(PaintingPlaceEvent event) {}

    public void onPaintingBreak(PaintingBreakEvent event) {}

    public void onPigZap(PigZapEvent event) {}

    public void onCreeperPower(CreeperPowerEvent event) {}

    public void onEntityTame(EntityTameEvent event) {}

    public void onEntityRegainHealth(EntityRegainHealthEvent event) {}

    public void onProjectileHit(ProjectileHitEvent event) {}
}
