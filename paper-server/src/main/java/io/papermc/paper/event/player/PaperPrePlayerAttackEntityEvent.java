package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPrePlayerAttackEntityEvent extends CraftPlayerEvent implements PrePlayerAttackEntityEvent {

    private final Entity attacked;
    private final boolean willAttack;

    private boolean cancelled;

    public PaperPrePlayerAttackEntityEvent(final Player player, final Entity attacked, final boolean willAttack) {
        super(player);
        this.attacked = attacked;
        this.willAttack = willAttack;
        this.cancelled = !willAttack;
    }

    @Override
    public Entity getAttacked() {
        return this.attacked;
    }

    @Override
    public boolean willAttack() {
        return this.willAttack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        if (!this.willAttack) {
            return;
        }

        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PrePlayerAttackEntityEvent.getHandlerList();
    }
}
