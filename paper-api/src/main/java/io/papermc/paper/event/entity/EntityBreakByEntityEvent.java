package io.papermc.paper.event.entity;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EntityBreakByEntityEvent extends EntityBreakEvent {

    private final Entity remover;
    private final DamageSource damageSource;

    @ApiStatus.Internal
    public EntityBreakByEntityEvent(final Entity entity, final Entity remover, final DamageSource damageSource) {
        this(entity, remover, damageSource, RemoveCause.ENTITY);
    }

    public EntityBreakByEntityEvent(final Entity entity, final Entity remover, final DamageSource damageSource, final RemoveCause cause) {
        super(entity, cause);
        this.remover = remover;
        this.damageSource = damageSource;
    }

    /**
     * Gets the entity that removed the entity.
     *
     * @return the entity that removed the entity
     */
    public Entity getRemover() {
        return this.remover;
    }

    /**
     * Gets the {@link DamageSource} that caused the entity to be removed.
     *
     * @return the damage source
     */
    public DamageSource getDamageSource() {
        return this.damageSource;
    }
}
