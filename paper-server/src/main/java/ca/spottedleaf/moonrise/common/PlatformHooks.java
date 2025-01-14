package ca.spottedleaf.moonrise.common;

import ca.spottedleaf.moonrise.common.util.ChunkSystemHooks;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Predicate;

public interface PlatformHooks extends ChunkSystemHooks {
    static PlatformHooks get() {
        return Holder.INSTANCE;
    }

    String getBrand();

    int getLightEmission(final BlockState blockState, final BlockGetter world, final BlockPos pos);

    Predicate<BlockState> maybeHasLightEmission();

    boolean hasCurrentlyLoadingChunk();

    LevelChunk getCurrentlyLoadingChunk(final GenerationChunkHolder holder);

    void setCurrentlyLoading(final GenerationChunkHolder holder, final LevelChunk levelChunk);

    void chunkFullStatusComplete(final LevelChunk newChunk, final ProtoChunk original);

    boolean allowAsyncTicketUpdates();

    void onChunkHolderTicketChange(final ServerLevel world, final ChunkHolder holder, final int oldLevel, final int newLevel);

    void chunkUnloadFromWorld(final LevelChunk chunk);

    void chunkSyncSave(final ServerLevel world, final ChunkAccess chunk, final SerializableChunkData data);

    void onChunkWatch(final ServerLevel world, final LevelChunk chunk, final ServerPlayer player);

    void onChunkUnWatch(final ServerLevel world, final ChunkPos chunk, final ServerPlayer player);

    void addToGetEntities(final Level world, final Entity entity, final AABB boundingBox, final Predicate<? super Entity> predicate,
                                 final List<Entity> into);

    <T extends Entity> void addToGetEntities(final Level world, final EntityTypeTest<Entity, T> entityTypeTest,
                                                    final AABB boundingBox, final Predicate<? super T> predicate,
                                                    final List<? super T> into, final int maxCount);

    void entityMove(final Entity entity, final long oldSection, final long newSection);

    boolean configFixMC224294();

    boolean configAutoConfigSendDistance();

    double configPlayerMaxLoadRate();

    double configPlayerMaxGenRate();

    double configPlayerMaxSendRate();

    int configPlayerMaxConcurrentLoads();

    int configPlayerMaxConcurrentGens();

    long configAutoSaveInterval(final ServerLevel world);

    int configMaxAutoSavePerTick(final ServerLevel world);

    boolean configFixMC159283();

    // support for CB chunk mustNotSave
    boolean forceNoSave(final ChunkAccess chunk);

    CompoundTag convertNBT(final DSL.TypeReference type, final DataFixer dataFixer, final CompoundTag nbt,
                                  final int fromVersion, final int toVersion);

    boolean hasMainChunkLoadHook();

    void mainChunkLoad(final ChunkAccess chunk, final SerializableChunkData chunkData);

    List<Entity> modifySavedEntities(final ServerLevel world, final int chunkX, final int chunkZ, final List<Entity> entities);

    void unloadEntity(final Entity entity);

    void postLoadProtoChunk(final ServerLevel world, final ProtoChunk chunk);

    int modifyEntityTrackingRange(final Entity entity, final int currentRange);

    final class Holder {
        private Holder() {
        }

        private static final PlatformHooks INSTANCE;

        static {
            INSTANCE = ServiceLoader.load(PlatformHooks.class, PlatformHooks.class.getClassLoader()).findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to locate PlatformHooks"));
        }
    }
}
