package io.papermc.paper.event.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Tameable;
import org.bukkit.event.HandlerList;

public class PaperTameableDeathMessageEvent extends CraftEntityEvent implements TameableDeathMessageEvent {

    private Component deathMessage;
    private boolean cancelled;

    public PaperTameableDeathMessageEvent(final Tameable tameable, final Component deathMessage) {
        super(tameable);
        this.deathMessage = deathMessage;
    }

    @Override
    public Component deathMessage() {
        return this.deathMessage;
    }

    @Override
    public void deathMessage(final Component deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    public Tameable getEntity() {
        return (Tameable) this.entity;
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
        return TameableDeathMessageEvent.getHandlerList();
    }
}
