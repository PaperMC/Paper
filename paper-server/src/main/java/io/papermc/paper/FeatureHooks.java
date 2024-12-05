package io.papermc.paper;

import io.papermc.paper.command.PaperSubcommand;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.bukkit.Chunk;
import org.bukkit.World;

public final class FeatureHooks {

    public static void initChunkTaskScheduler(final boolean useParallelGen) {
    }

    public static void registerPaperCommands(final Map<Set<String>, PaperSubcommand> commands) {
    }

    public static LevelChunkSection createSection(final Registry<Biome> biomeRegistry, final Level level, final ChunkPos chunkPos, final int chunkSection) {
        return new LevelChunkSection(biomeRegistry);
    }

    public static void sendChunkRefreshPackets(final List<ServerPlayer> playersInRange, final LevelChunk chunk) {
        final ClientboundLevelChunkWithLightPacket refreshPacket = new ClientboundLevelChunkWithLightPacket(chunk, chunk.level.getLightEngine(), null, null);
        for (final ServerPlayer player : playersInRange) {
            if (player.connection == null) continue;

            player.connection.send(refreshPacket);
        }
    }

    public static PalettedContainer<BlockState> emptyPalettedBlockContainer() {
        return new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
    }

    public static Set<Long> getSentChunkKeys(final ServerPlayer player) {
        final LongSet keys = new LongOpenHashSet();
        player.getChunkTrackingView().forEach(pos -> keys.add(pos.longKey));
        return LongSets.unmodifiable(keys);
    }

    public static Set<Chunk> getSentChunks(final ServerPlayer player) {
        final ObjectSet<Chunk> chunks = new ObjectOpenHashSet<>();
        final World world = player.serverLevel().getWorld();
        player.getChunkTrackingView().forEach(pos -> {
            final org.bukkit.Chunk chunk = world.getChunkAt(pos.longKey);
            chunks.add(chunk);
        });
        return ObjectSets.unmodifiable(chunks);
    }

    public static boolean isChunkSent(final ServerPlayer player, final long chunkKey) {
        return player.getChunkTrackingView().contains(new ChunkPos(chunkKey));
    }
}
