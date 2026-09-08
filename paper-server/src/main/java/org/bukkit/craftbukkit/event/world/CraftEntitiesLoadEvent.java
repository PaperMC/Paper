package org.bukkit.craftbukkit.event.world;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftEntitiesLoadEvent extends CraftChunkEvent implements EntitiesLoadEvent {

    private final List<Entity> entities;

    public CraftEntitiesLoadEvent(final Chunk chunk, final List<Entity> entities) {
        super(chunk);
        this.entities = Collections.unmodifiableList(entities);
    }

    public CraftEntitiesLoadEvent(final ServerLevel level, final ChunkPos pos, final List<net.minecraft.world.entity.Entity> entities) {
        this(new CraftChunk(level, pos), Lists.transform(entities, net.minecraft.world.entity.Entity::getBukkitEntity));
    }

    @Override
    public @Unmodifiable List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public HandlerList getHandlers() {
        return EntitiesLoadEvent.getHandlerList();
    }
}
