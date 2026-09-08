package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.jspecify.annotations.Nullable;

public class CraftCreeperPowerEvent extends CraftEntityEvent implements CreeperPowerEvent {

    private final PowerCause cause;
    private final @Nullable LightningStrike bolt;

    private boolean cancelled;

    public CraftCreeperPowerEvent(final Creeper creeper, final @Nullable LightningStrike bolt, final PowerCause cause) {
        super(creeper);
        this.cause = cause;
        this.bolt = bolt;
    }

    public CraftCreeperPowerEvent(final net.minecraft.world.entity.monster.Creeper creeper, final @Nullable LightningBolt bolt, final PowerCause cause) {
        this((Creeper) creeper.getBukkitEntity(), (LightningStrike) Optionull.map(bolt, Entity::getBukkitEntity), cause);
    }

    @Override
    public Creeper getEntity() {
        return (Creeper) this.entity;
    }

    @Override
    public @Nullable LightningStrike getLightning() {
        return this.bolt;
    }

    @Override
    public PowerCause getCause() {
        return this.cause;
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
        return CreeperPowerEvent.getHandlerList();
    }
}
