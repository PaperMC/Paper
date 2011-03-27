package org.bukkit.event.entity;

import java.util.Random;

import org.bukkit.entity.Entity;

public class EntityDamageByProjectileEvent extends EntityDamageByEntityEvent {

    private Entity projectile;
    private boolean bounce;

    public EntityDamageByProjectileEvent(Entity damager, Entity damagee, Entity projectile, DamageCause cause, int damage) {
        super(damager, damagee, cause, damage);
        this.projectile = projectile;
        Random random = new Random();
        this.bounce = random.nextBoolean();
    }

    /**
     * The projectile used to cause the event
     * @return the projectile
     */
    public Entity getProjectile() {
        return projectile;
    }

    public void setBounce(boolean bounce){
        this.bounce = bounce;
    }

    public boolean getBounce(){
        return bounce;
    }

}
