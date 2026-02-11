package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class PaperEntityKnockbackByEntityEvent extends PaperEntityPushedByEntityAttackEvent implements EntityKnockbackByEntityEvent {

    private final float knockbackStrength;

    public PaperEntityKnockbackByEntityEvent(final LivingEntity entity, final Entity hitBy, final Cause cause, final float knockbackStrength, final Vector knockback) {
        super(entity, cause, hitBy, knockback);
        this.knockbackStrength = knockbackStrength;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    @Override
    public float getKnockbackStrength() {
        return this.knockbackStrength;
    }

    @Override
    public Entity getHitBy() {
        return super.getPushedBy();
    }
}
