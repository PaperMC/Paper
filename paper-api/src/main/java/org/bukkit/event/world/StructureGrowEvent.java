package org.bukkit.event.world;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that is called when an organic structure attempts to grow (Sapling {@literal ->}
 * Tree), (Mushroom {@literal ->} Huge Mushroom), naturally or using bonemeal.
 */
public class StructureGrowEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private final TreeType species;
    private final boolean bonemeal;
    private final Player player;
    private final List<BlockState> blocks;

    private boolean cancelled;

    @ApiStatus.Internal
    public StructureGrowEvent(@NotNull final Location location, @NotNull final TreeType species, final boolean bonemeal, @Nullable final Player player, @NotNull final List<BlockState> blocks) {
        super(location.getWorld());
        this.location = location;
        this.species = species;
        this.bonemeal = bonemeal;
        this.player = player;
        this.blocks = blocks;
    }

    /**
     * Gets the location of the structure.
     *
     * @return Location of the structure
     */
    @NotNull
    public Location getLocation() {
        return this.location.clone();
    }

    /**
     * Gets the species type (birch, normal, pine, red mushroom, brown
     * mushroom)
     *
     * @return Structure species
     */
    @NotNull
    public TreeType getSpecies() {
        return this.species;
    }

    /**
     * Checks if structure was grown using bonemeal.
     *
     * @return {@code true} if the structure was grown using bonemeal.
     */
    public boolean isFromBonemeal() {
        return this.bonemeal;
    }

    /**
     * Gets the player that created the structure.
     *
     * @return Player that created the structure, {@code null} if was not created
     *     manually
     */
    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets a list of all blocks associated with the structure.
     *
     * @return list of all blocks associated with the structure.
     */
    @NotNull
    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
