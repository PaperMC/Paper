package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.World;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BoundingBox;

public class CraftAsyncStructureSpawnEvent extends CraftWorldEvent implements AsyncStructureSpawnEvent {

    private final Structure structure;
    private final BoundingBox box;

    private final int chunkX, chunkZ;

    private boolean cancelled;

    public CraftAsyncStructureSpawnEvent(final World world, final Structure structure, final BoundingBox box, final int chunkX, final int chunkZ) {
        super(world, true);
        this.structure = structure;
        this.box = box;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public CraftAsyncStructureSpawnEvent(
        final LevelAccessor level,
        final net.minecraft.world.level.levelgen.structure.Structure structure,
        final net.minecraft.world.level.levelgen.structure.BoundingBox box,
        final ChunkPos source
    ) {
        this(
            level.getMinecraftWorld().getWorld(),
            CraftStructure.minecraftToBukkit(structure),
            new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()),
            source.x(),
            source.z()
        );
    }

    @Override
    public Structure getStructure() {
        return this.structure;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.box.clone();
    }

    @Override
    public int getChunkX() {
        return this.chunkX;
    }

    @Override
    public int getChunkZ() {
        return this.chunkZ;
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
        return AsyncStructureSpawnEvent.getHandlerList();
    }
}
