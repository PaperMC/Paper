package org.bukkit.craftbukkit.event.world.weather;

import net.minecraft.world.entity.LightningBolt;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.HandlerList;
import org.bukkit.event.weather.LightningStrikeEvent;

public class CraftLightningStrikeEvent extends CraftWeatherEvent implements LightningStrikeEvent {

    private final LightningStrike bolt;
    private final Cause cause;

    private boolean cancelled;

    public CraftLightningStrikeEvent(final World world, final LightningStrike bolt, final Cause cause) {
        super(world);
        this.bolt = bolt;
        this.cause = cause;
    }

    public CraftLightningStrikeEvent(final LightningBolt bolt, final Cause cause) {
        this(bolt.level().getWorld(), (LightningStrike) bolt.getBukkitEntity(), cause);
    }

    @Override
    public LightningStrike getEntity() {
        return this.bolt;
    }

    @Override
    public Cause getCause() {
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
        return LightningStrikeEvent.getHandlerList();
    }
}
