package org.bukkit.craftbukkit.event.world;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EntityTransformer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

@ApiStatus.Experimental
public class CraftAsyncStructureGenerateEvent extends CraftWorldEvent implements AsyncStructureGenerateEvent {

    private final Cause cause;

    private final Structure structure;
    private final BoundingBox boundingBox;

    private final int chunkX, chunkZ;

    private final Map<NamespacedKey, BlockTransformer> blockTransformers = new LinkedHashMap<>();
    private final Map<NamespacedKey, EntityTransformer> entityTransformers = new LinkedHashMap<>();

    public CraftAsyncStructureGenerateEvent(final World world, final boolean async, final Cause cause, final Structure structure, final BoundingBox boundingBox, final int chunkX, final int chunkZ) {
        super(world, async);
        this.cause = cause;
        this.structure = structure;
        this.boundingBox = boundingBox;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public Structure getStructure() {
        return this.structure;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox.clone();
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
    public @Nullable BlockTransformer getBlockTransformer(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        return this.blockTransformers.get(key);
    }

    @Override
    public void setBlockTransformer(final NamespacedKey key, final BlockTransformer transformer) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        Preconditions.checkArgument(transformer != null, "BlockTransformer cannot be null");
        this.blockTransformers.put(key, transformer);
    }

    @Override
    public void removeBlockTransformer(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        this.blockTransformers.remove(key);
    }

    @Override
    public void clearBlockTransformers() {
        this.blockTransformers.clear();
    }

    @Override
    public @Unmodifiable Map<NamespacedKey, BlockTransformer> getBlockTransformers() {
        return Collections.unmodifiableMap(this.blockTransformers);
    }

    @Override
    public @Nullable EntityTransformer getEntityTransformer(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        return this.entityTransformers.get(key);
    }

    @Override
    public void setEntityTransformer(final NamespacedKey key, final EntityTransformer transformer) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        Preconditions.checkArgument(transformer != null, "EntityTransformer cannot be null");
        this.entityTransformers.put(key, transformer);
    }

    @Override
    public void removeEntityTransformer(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        this.entityTransformers.remove(key);
    }

    @Override
    public void clearEntityTransformers() {
        this.entityTransformers.clear();
    }

    @Override
    public @Unmodifiable Map<NamespacedKey, EntityTransformer> getEntityTransformers() {
        return Collections.unmodifiableMap(this.entityTransformers);
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncStructureGenerateEvent.getHandlerList();
    }
}
