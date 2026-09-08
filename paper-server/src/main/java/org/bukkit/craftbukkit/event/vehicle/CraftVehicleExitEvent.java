package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class CraftVehicleExitEvent extends CraftVehicleEvent implements VehicleExitEvent {

    private final LivingEntity exited;
    private final boolean cancellable;

    private boolean cancelled;

    public CraftVehicleExitEvent(final Vehicle vehicle, final LivingEntity exited, final boolean cancellable) {
        super(vehicle);
        this.exited = exited;
        this.cancellable = cancellable;
    }

    public CraftVehicleExitEvent(final Vehicle vehicle, final net.minecraft.world.entity.LivingEntity exited, final boolean cancellable) {
        this(vehicle, exited.getBukkitEntity(), cancellable);
    }

    @Override
    public LivingEntity getExited() {
        return this.exited;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        if (cancel && !this.cancellable) {
            return;
        }
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancellable() {
        return this.cancellable;
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleExitEvent.getHandlerList();
    }
}
