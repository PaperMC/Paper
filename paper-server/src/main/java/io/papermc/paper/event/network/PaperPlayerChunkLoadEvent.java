package io.papermc.paper.event.network;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.event.world.CraftChunkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerChunkLoadEvent extends CraftChunkEvent implements PlayerChunkLoadEvent {

    private final Player player;

    public PaperPlayerChunkLoadEvent(final Chunk chunk, final Player player) {
        super(chunk);
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChunkLoadEvent.getHandlerList();
    }
}
