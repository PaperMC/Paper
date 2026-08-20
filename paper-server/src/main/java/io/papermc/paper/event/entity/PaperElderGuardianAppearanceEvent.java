package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperElderGuardianAppearanceEvent extends CraftEntityEvent implements ElderGuardianAppearanceEvent {

    private final Player affectedPlayer;
    private boolean cancelled;

    public PaperElderGuardianAppearanceEvent(final ElderGuardian guardian, final Player affectedPlayer) {
        super(guardian);
        this.affectedPlayer = affectedPlayer;
    }

    @Override
    public Player getAffectedPlayer() {
        return this.affectedPlayer;
    }

    @Override
    public ElderGuardian getEntity() {
        return (ElderGuardian) this.entity;
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
        return ElderGuardianAppearanceEvent.getHandlerList();
    }
}
