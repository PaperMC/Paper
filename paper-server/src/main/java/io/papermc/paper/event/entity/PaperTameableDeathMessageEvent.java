package io.papermc.paper.event.entity;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.TamableAnimal;
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

    public PaperTameableDeathMessageEvent(final TamableAnimal animal, final net.minecraft.network.chat.Component deathMessage) {
        this((Tameable) animal.getBukkitEntity(), PaperAdventure.asAdventure(deathMessage));
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
