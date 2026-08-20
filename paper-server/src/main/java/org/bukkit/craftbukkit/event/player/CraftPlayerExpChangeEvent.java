package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerExpChangeEvent extends CraftPlayerEvent implements PlayerExpChangeEvent {

    private final @Nullable Entity source;
    private int amount;

    public CraftPlayerExpChangeEvent(final Player player, final @Nullable Entity source, final int amount) {
        super(player);
        this.source = source;
        this.amount = amount;
    }

    @Override
    public @Nullable Entity getSource() {
        return this.source;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerExpChangeEvent.getHandlerList();
    }
}
