package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerNaturallySpawnCreaturesEvent extends CraftPlayerEvent implements PlayerNaturallySpawnCreaturesEvent {

    private byte radius;
    private boolean cancelled;

    public PaperPlayerNaturallySpawnCreaturesEvent(final Player player, final byte radius) {
        super(player);
        this.radius = radius;
    }

    @Override
    public byte getSpawnRadius() {
        return this.radius;
    }

    @Override
    public void setSpawnRadius(final byte radius) {
        this.radius = radius;
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
        return PlayerNaturallySpawnCreaturesEvent.getHandlerList();
    }
}
