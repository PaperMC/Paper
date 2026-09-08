package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Shulker;
import org.bukkit.event.HandlerList;

public class PaperShulkerDuplicateEvent extends CraftEntityEvent implements ShulkerDuplicateEvent {

    private final Shulker parent;
    private boolean cancelled;

    public PaperShulkerDuplicateEvent(final Shulker baby, final Shulker parent) {
        super(baby);
        this.parent = parent;
    }

    public PaperShulkerDuplicateEvent(final net.minecraft.world.entity.monster.Shulker baby, final net.minecraft.world.entity.monster.Shulker parent) {
        this((Shulker) baby.getBukkitEntity(), (Shulker) parent.getBukkitEntity());
    }

    @Override
    public Shulker getEntity() {
        return (Shulker) this.entity;
    }

    @Override
    public Shulker getParent() {
        return this.parent;
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
        return ShulkerDuplicateEvent.getHandlerList();
    }
}
