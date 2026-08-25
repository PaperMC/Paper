package io.papermc.paper.event.world;

import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

public class PaperStructuresLocateEvent extends CraftWorldEvent implements StructuresLocateEvent {

    private final Location origin;
    private StructuresLocateEvent.@Nullable Result result;
    private List<Structure> structures;
    private int radius;
    private boolean findUnexplored;

    private boolean cancelled;

    public PaperStructuresLocateEvent(final World world, final Location origin, final List<Structure> structures, final int radius, final boolean findUnexplored) {
        super(world);
        this.origin = origin;
        this.structures = structures;
        this.radius = radius;
        this.findUnexplored = findUnexplored;
    }

    @Override
    public Location getOrigin() {
        return this.origin.clone();
    }

    @Override
    public StructuresLocateEvent.@Nullable Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(final StructuresLocateEvent.@Nullable Result result) {
        this.result = result;
    }

    @Override
    public @UnmodifiableView List<Structure> getStructures() {
        return Collections.unmodifiableList(this.structures);
    }

    @Override
    public void setStructures(final List<Structure> structures) {
        this.structures = structures;
    }

    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(final int radius) {
        this.radius = radius;
    }

    @Override
    public boolean shouldFindUnexplored() {
        return this.findUnexplored;
    }

    @Override
    public void setFindUnexplored(final boolean findUnexplored) {
        this.findUnexplored = findUnexplored;
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
        return StructuresLocateEvent.getHandlerList();
    }
}
