package io.papermc.paper.event.network;

import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.event.world.CraftChunkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerChunkUnloadEvent extends CraftChunkEvent implements PlayerChunkUnloadEvent {

    private final Player player;

    public PaperPlayerChunkUnloadEvent(final Chunk chunk, final Player player) {
        super(chunk);
        this.player = player;
    }

    public PaperPlayerChunkUnloadEvent(final ServerLevel level, final ChunkPos pos, final ServerPlayer player) {
        this(new CraftChunk(level, pos), player.getBukkitEntity());
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
