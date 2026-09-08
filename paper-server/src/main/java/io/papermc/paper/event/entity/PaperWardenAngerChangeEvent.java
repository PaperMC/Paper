package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Warden;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

import static io.papermc.paper.util.BoundChecker.requireRange;

public class PaperWardenAngerChangeEvent extends CraftEntityEvent implements WardenAngerChangeEvent {

    private static final int MAX_ANGER = 150; // AngerManagement.MAX_ANGER

    private final Entity target;
    private final int oldAnger;
    private int newAnger;

    private boolean cancelled;

    public PaperWardenAngerChangeEvent(final Warden warden, final Entity target, final int oldAnger, final int newAnger) {
        super(warden);
        this.target = target;
        this.oldAnger = oldAnger;
        this.newAnger = newAnger;
    }

    public PaperWardenAngerChangeEvent(
        final net.minecraft.world.entity.monster.warden.Warden warden,
        final net.minecraft.world.entity.Entity target,
        final int oldAnger,
        final int newAnger
    ) {
        this((Warden) warden.getBukkitEntity(), target.getBukkitEntity(), oldAnger, newAnger);
    }

    @Override
    public Entity getTarget() {
        return this.target;
    }

    @Override
    public @IntRange(from = 0, to = MAX_ANGER) int getOldAnger() {
        return this.oldAnger;
    }

    @Override
    public @IntRange(from = 0, to = MAX_ANGER) int getNewAnger() {
        return this.newAnger;
    }

    @Override
    public void setNewAnger(final @IntRange(from = 0, to = MAX_ANGER) int newAnger) {
        this.newAnger = requireRange(newAnger, "newAnger", 0, MAX_ANGER);
    }

    @Override
    public Warden getEntity() {
        return (Warden) this.entity;
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
        return WardenAngerChangeEvent.getHandlerList();
    }
}
