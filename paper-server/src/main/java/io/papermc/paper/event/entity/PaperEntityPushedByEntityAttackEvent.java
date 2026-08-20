package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class PaperEntityPushedByEntityAttackEvent extends PaperEntityKnockbackEvent implements EntityPushedByEntityAttackEvent {

    private final Entity pushedBy;

    public PaperEntityPushedByEntityAttackEvent(final Entity entity, final Cause cause, final Entity pushedBy, final Vector knockback) {
        super(entity, cause, knockback);
        this.pushedBy = pushedBy;
    }

    @Override
    public Entity getPushedBy() {
        return this.pushedBy;
    }
}
