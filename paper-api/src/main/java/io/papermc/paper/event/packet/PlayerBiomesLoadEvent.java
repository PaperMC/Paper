package io.papermc.paper.event.packet;

import org.bukkit.BiomesSnapshot;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * Is called when a {@link Player} receives {@link org.bukkit.block.Biome Biomes}
 * <p>
 * Can for example be used for replacing Biomes when the player receives the Biome list.
 * <p>
 * Should only be used for packet/clientside related stuff.
 * Not intended for modifying server side state.
 */
@NullMarked
public class PlayerBiomesLoadEvent extends ChunkEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private @Nullable BiomesSnapshot biomesSnapshot;

    @ApiStatus.Internal
    public PlayerBiomesLoadEvent(final Player player, Chunk chunk) {
        super(chunk);
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Returns the player that is receiving the biomes
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns a biomes snapshot for the given chunk, by default it won't be set.
     *
     * @return biome snapshot if one is set
     */
    public @Nullable BiomesSnapshot getBiomeSnapshot() {
        return biomesSnapshot;
    }

    /**
     * Sets the biome snapshot for the given chunk that will be sent as an override to the player
     *
     * @param biomesSnapshot the biome override
     */
    public void setBiomeSnapshot(@Nullable final BiomesSnapshot biomesSnapshot) {
        this.biomesSnapshot = biomesSnapshot;
    }

    /**
     * Returns if chunk biomes were overridden
     *
     * @return true if override was made, else false
     */
    public boolean hasOverrides() {
        return biomesSnapshot != null;
    }
}
