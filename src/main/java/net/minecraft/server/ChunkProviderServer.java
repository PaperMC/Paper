package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// CraftBukkit start
import java.util.Random;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.LongHashset;
import org.bukkit.craftbukkit.util.LongHashtable;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.generator.BlockPopulator;
// CraftBukkit end

public class ChunkProviderServer implements IChunkProvider {

    // CraftBukkit start
    public LongHashset unloadQueue = new LongHashset();
    public Chunk emptyChunk;
    public IChunkProvider chunkProvider; // CraftBukkit
    private IChunkLoader e;
    public boolean a = false;
    public LongHashtable<Chunk> chunks = new LongHashtable<Chunk>();
    public List chunkList = new ArrayList();
    public WorldServer world;
    // CraftBukkit end

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider) {
        this.emptyChunk = new EmptyChunk(worldserver, new byte['\u8000'], 0, 0);
        this.world = worldserver;
        this.e = ichunkloader;
        this.chunkProvider = ichunkprovider;
    }

    public boolean isChunkLoaded(int i, int j) {
        return this.chunks.containsKey(i, j); // CraftBukkit
    }

    public void queueUnload(int i, int j) {
        ChunkCoordinates chunkcoordinates = this.world.getSpawn();
        int k = i * 16 + 8 - chunkcoordinates.x;
        int l = j * 16 + 8 - chunkcoordinates.z;
        short short1 = 128;

        if (k < -short1 || k > short1 || l < -short1 || l > short1) {
            this.unloadQueue.add(i, j); // CraftBukkit
        }
    }

    public Chunk getChunkAt(int i, int j) {
        // CraftBukkit start
        this.unloadQueue.remove(i, j);
        Chunk chunk = (Chunk) this.chunks.get(i, j);
        // CraftBukkit end

        if (chunk == null) {
            chunk = this.loadChunk(i, j);
            if (chunk == null) {
                if (this.chunkProvider == null) {
                    chunk = this.emptyChunk;
                } else {
                    chunk = this.chunkProvider.getOrCreateChunk(i, j);
                }
            }

            this.chunks.put(i, j, chunk); // CraftBukkit
            this.chunkList.add(chunk);
            if (chunk != null) {
                chunk.loadNOP();
                chunk.addEntities();
            }

            // CraftBukkit start
            CraftServer server = this.world.getServer();
            if (server != null) {
                /*
                 * If it's a new world, the first few chunks are generated inside
                 * the World constructor. We can't reliably alter that, so we have
                 * no way of creating a CraftWorld/CraftServer at that point.
                 */
                server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk));
            }
            // CraftBukkit end

            if (!chunk.done && this.isChunkLoaded(i + 1, j + 1) && this.isChunkLoaded(i, j + 1) && this.isChunkLoaded(i + 1, j)) {
                this.getChunkAt(this, i, j);
            }

            if (this.isChunkLoaded(i - 1, j) && !this.getOrCreateChunk(i - 1, j).done && this.isChunkLoaded(i - 1, j + 1) && this.isChunkLoaded(i, j + 1) && this.isChunkLoaded(i - 1, j)) {
                this.getChunkAt(this, i - 1, j);
            }

            if (this.isChunkLoaded(i, j - 1) && !this.getOrCreateChunk(i, j - 1).done && this.isChunkLoaded(i + 1, j - 1) && this.isChunkLoaded(i, j - 1) && this.isChunkLoaded(i + 1, j)) {
                this.getChunkAt(this, i, j - 1);
            }

            if (this.isChunkLoaded(i - 1, j - 1) && !this.getOrCreateChunk(i - 1, j - 1).done && this.isChunkLoaded(i - 1, j - 1) && this.isChunkLoaded(i, j - 1) && this.isChunkLoaded(i - 1, j)) {
                this.getChunkAt(this, i - 1, j - 1);
            }
        }

        return chunk;
    }

    public Chunk getOrCreateChunk(int i, int j) {
        // CraftBukkit start
        Chunk chunk = (Chunk) this.chunks.get(i, j);

        chunk = chunk == null ? (!this.world.isLoading && !this.a ? this.emptyChunk : this.getChunkAt(i, j)) : chunk;
        if (chunk == this.emptyChunk) return chunk;
        if (i != chunk.x || j != chunk.z) {
            MinecraftServer.log.info("Chunk (" + chunk.x + ", " + chunk.z + ") stored at  (" + i + ", " + j + ")");
            MinecraftServer.log.info(chunk.getClass().getName());
            Throwable ex = new Throwable();
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
        return chunk;
        // CraftBukkit end
    }

    public Chunk loadChunk(int i, int j) { // CraftBukkit - private -> public
        if (this.e == null) {
            return null;
        } else {
            try {
                Chunk chunk = this.e.a(this.world, i, j);

                if (chunk != null) {
                    chunk.r = this.world.getTime();
                }

                return chunk;
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    public void saveChunkNOP(Chunk chunk) { // CraftBukkit - private -> public
        if (this.e != null) {
            try {
                this.e.b(this.world, chunk);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void saveChunk(Chunk chunk) { // CraftBukkit - private -> public
        if (this.e != null) {
            try {
                chunk.r = this.world.getTime();
                this.e.a(this.world, chunk);
            } catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
                ioexception.printStackTrace();
            }
        }
    }

    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        Chunk chunk = this.getOrCreateChunk(i, j);

        if (!chunk.done) {
            chunk.done = true;
            if (this.chunkProvider != null) {
                this.chunkProvider.getChunkAt(ichunkprovider, i, j);

                // CraftBukkit start
                BlockSand.a = true;
                Random random = new Random();
                random.setSeed(world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) i * xRand + (long) j * zRand ^ world.getSeed());
                for (BlockPopulator populator : world.getWorld().getPopulators()) {
                    populator.populate(world.getWorld(), random, chunk.bukkitChunk);
                }
                BlockSand.a = false;
                // CraftBukkit end

                chunk.f();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        int i = 0;

        for (int j = 0; j < this.chunkList.size(); ++j) {
            Chunk chunk = (Chunk) this.chunkList.get(j);

            if (flag && !chunk.p) {
                this.saveChunkNOP(chunk);
            }

            if (chunk.a(flag)) {
                this.saveChunk(chunk);
                chunk.o = false;
                ++i;
                if (i == 24 && !flag) {
                    return false;
                }
            }
        }

        if (flag) {
            if (this.e == null) {
                return true;
            }

            this.e.b();
        }

        return true;
    }

    public boolean unloadChunks() {
        if (!this.world.E) {
            // CraftBukkit start
            Server server = this.world.getServer();
            for (int i = 0; i < 50 && !this.unloadQueue.isEmpty(); i++) {
                long chunkcoordinates = this.unloadQueue.popFirst();
                Chunk chunk = this.chunks.get(chunkcoordinates);
                if (chunk == null) continue;

                ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk);
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.world.getWorld().preserveChunk((CraftChunk) chunk.bukkitChunk);

                    chunk.removeEntities();
                    this.saveChunk(chunk);
                    this.saveChunkNOP(chunk);
                    // this.unloadQueue.remove(integer);
                    this.chunks.remove(chunkcoordinates); // CraftBukkit
                    this.chunkList.remove(chunk);
                }
            }
            // CraftBukkit end

            if (this.e != null) {
                this.e.a();
            }
        }

        return this.chunkProvider.unloadChunks();
    }

    public boolean b() {
        return !this.world.E;
    }
}
