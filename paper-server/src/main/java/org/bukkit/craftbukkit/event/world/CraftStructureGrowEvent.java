package org.bukkit.craftbukkit.event.world;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.StructureGrowEvent;
import org.jspecify.annotations.Nullable;

public class CraftStructureGrowEvent extends CraftWorldEvent implements StructureGrowEvent {

    private final Location location;
    private final TreeType species;
    private final boolean bonemeal;
    private final Player player;
    private final List<BlockState> blocks;

    private boolean cancelled;

    public CraftStructureGrowEvent(final Location location, final TreeType species, final boolean bonemeal, final @Nullable Player player, final List<BlockState> blocks) {
        super(location.getWorld());
        this.location = location;
        this.species = species;
        this.bonemeal = bonemeal;
        this.player = player;
        this.blocks = blocks;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public TreeType getSpecies() {
        return this.species;
    }

    @Override
    public boolean isFromBonemeal() {
        return this.bonemeal;
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public List<BlockState> getBlocks() {
        return this.blocks;
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
        return StructureGrowEvent.getHandlerList();
    }
}
