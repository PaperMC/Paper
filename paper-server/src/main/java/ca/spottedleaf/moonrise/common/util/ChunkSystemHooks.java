package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.concurrentutil.util.Priority;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import java.util.List;
import java.util.function.Consumer;

public interface ChunkSystemHooks {

    public void scheduleChunkTask(final ServerLevel level, final int chunkX, final int chunkZ, final Runnable run);

    public void scheduleChunkTask(final ServerLevel level, final int chunkX, final int chunkZ, final Runnable run, final Priority priority);

    public void scheduleChunkLoad(final ServerLevel level, final int chunkX, final int chunkZ, final boolean gen,
                                  final ChunkStatus toStatus, final boolean addTicket, final Priority priority,
                                  final Consumer<ChunkAccess> onComplete);

    public void scheduleChunkLoad(final ServerLevel level, final int chunkX, final int chunkZ, final ChunkStatus toStatus,
                                  final boolean addTicket, final Priority priority, final Consumer<ChunkAccess> onComplete);

    public void scheduleTickingState(final ServerLevel level, final int chunkX, final int chunkZ,
                                     final FullChunkStatus toStatus, final boolean addTicket,
                                     final Priority priority, final Consumer<LevelChunk> onComplete);

    public List<ChunkHolder> getVisibleChunkHolders(final ServerLevel level);

    public List<ChunkHolder> getUpdatingChunkHolders(final ServerLevel level);

    public int getVisibleChunkHolderCount(final ServerLevel level);

    public int getUpdatingChunkHolderCount(final ServerLevel level);

    public boolean hasAnyChunkHolders(final ServerLevel level);

    public boolean screenEntity(final ServerLevel level, final Entity entity, final boolean fromDisk, final boolean event);

    public void onChunkHolderCreate(final ServerLevel level, final ChunkHolder holder);

    public void onChunkHolderDelete(final ServerLevel level, final ChunkHolder holder);

    public void onChunkPreBorder(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkBorder(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkNotBorder(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkPostNotBorder(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkTicking(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkNotTicking(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkEntityTicking(final LevelChunk chunk, final ChunkHolder holder);

    public void onChunkNotEntityTicking(final LevelChunk chunk, final ChunkHolder holder);

    public ChunkHolder getUnloadingChunkHolder(final ServerLevel level, final int chunkX, final int chunkZ);

    public int getSendViewDistance(final ServerPlayer player);

    public int getViewDistance(final ServerPlayer player);

    public int getTickViewDistance(final ServerPlayer player);

    public void addPlayerToDistanceMaps(final ServerLevel world, final ServerPlayer player);

    public void removePlayerFromDistanceMaps(final ServerLevel world, final ServerPlayer player);

    public void updateMaps(final ServerLevel world, final ServerPlayer player);

    public long[] getCounterTypesUncached(final TicketType type);
}
