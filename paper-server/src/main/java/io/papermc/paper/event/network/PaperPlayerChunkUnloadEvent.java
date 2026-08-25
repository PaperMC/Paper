package io.papermc.paper.event.network;

import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.event.world.CraftChunkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerChunkUnloadEvent extends CraftChunkEvent implements PlayerChunkUnloadEvent {

    private final Player player;

    public PaperPlayerChunkUnloadEvent(final Chunk chunk, final Player player) {
        super(chunk);
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChunkUnloadEvent.getHandlerList();
    }
}
