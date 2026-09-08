package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

public class PaperEnderDragonFireballHitEvent extends CraftEntityEvent implements EnderDragonFireballHitEvent {

    private final Collection<LivingEntity> targets;
    private final AreaEffectCloud areaEffectCloud;

    private boolean cancelled;

    public PaperEnderDragonFireballHitEvent(final DragonFireball fireball, final Collection<LivingEntity> targets, final AreaEffectCloud areaEffectCloud) {
        super(fireball);
        this.targets = Collections.unmodifiableCollection(targets);
        this.areaEffectCloud = areaEffectCloud;
    }

    public PaperEnderDragonFireballHitEvent(
        final net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball fireball,
        final List<net.minecraft.world.entity.LivingEntity> targets,
        final net.minecraft.world.entity.AreaEffectCloud areaEffectCloud
    ) {
        this(
            (DragonFireball) fireball.getBukkitEntity(),
            Lists.transform(targets, net.minecraft.world.entity.LivingEntity::getBukkitEntity),
            (AreaEffectCloud) areaEffectCloud.getBukkitEntity()
        );
    }

    @Override
    public DragonFireball getEntity() {
        return (DragonFireball) this.entity;
    }

    @Override
    public @Unmodifiable Collection<LivingEntity> getTargets() {
        return this.targets;
    }

    @Override
    public AreaEffectCloud getAreaEffectCloud() {
        return this.areaEffectCloud;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EnderDragonFireballHitEvent.getHandlerList();
    }
}
