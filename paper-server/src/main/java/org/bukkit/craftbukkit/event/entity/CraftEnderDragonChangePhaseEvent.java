package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Preconditions;
import net.minecraft.Optionull;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jspecify.annotations.Nullable;

public class CraftEnderDragonChangePhaseEvent extends CraftEntityEvent implements EnderDragonChangePhaseEvent {

    private final EnderDragon.@Nullable Phase currentPhase;
    private EnderDragon.Phase newPhase;

    private boolean cancelled;

    public CraftEnderDragonChangePhaseEvent(final EnderDragon dragon, final EnderDragon.@Nullable Phase currentPhase, final EnderDragon.Phase newPhase) {
        super(dragon);
        this.currentPhase = currentPhase;
        this.newPhase = newPhase;
    }

    public CraftEnderDragonChangePhaseEvent(
        final net.minecraft.world.entity.boss.enderdragon.EnderDragon dragon,
        final @Nullable DragonPhaseInstance currentPhase,
        final EnderDragonPhase<?> newPhase
    ) {
        this(
            (EnderDragon) dragon.getBukkitEntity(),
            Optionull.map(currentPhase, i -> CraftEnderDragon.getBukkitPhase(i.getPhase())),
            CraftEnderDragon.getBukkitPhase(newPhase)
        );
    }

    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) this.entity;
    }

    @Override
    public EnderDragon.@Nullable Phase getCurrentPhase() {
        return this.currentPhase;
    }

    @Override
    public EnderDragon.Phase getNewPhase() {
        return this.newPhase;
    }

    @Override
    public void setNewPhase(final EnderDragon.Phase newPhase) {
        Preconditions.checkArgument(newPhase != null, "New dragon phase cannot be null");
        this.newPhase = newPhase;
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
        return EnderDragonChangePhaseEvent.getHandlerList();
    }
}
