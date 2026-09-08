package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class PaperEntityPushedByEntityAttackEvent extends PaperEntityKnockbackEvent implements EntityPushedByEntityAttackEvent {

    private final Entity pushedBy;

    public PaperEntityPushedByEntityAttackEvent(final Entity entity, final Cause cause, final Entity pushedBy, final Vector knockback) {
        super(entity, cause, knockback);
        this.pushedBy = pushedBy;
    }

    public PaperEntityPushedByEntityAttackEvent(
        final net.minecraft.world.entity.Entity entity, final Cause cause, final net.minecraft.world.entity.Entity pushedBy, final Vector knockback
    ) {
        this(entity.getBukkitEntity(), cause, pushedBy.getBukkitEntity(), knockback);
    }

    @Override
    public Entity getPushedBy() {
        return this.pushedBy;
    }
}
