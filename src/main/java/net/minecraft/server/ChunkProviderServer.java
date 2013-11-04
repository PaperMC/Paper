package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.util.Random;

import org.bukkit.Server;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.craftbukkit.util.LongObjectHashMap;
import org.bukkit.event.world.ChunkUnloadEvent;
// CraftBukkit end

public class ChunkProviderServer implements IChunkProvider {

    private static final Logger b = LogManager.getLogger();
    // CraftBukkit start - private -> public
    public LongHashSet unloadQueue = new LongHashSet(); // LongHashSet
    public Chunk emptyChunk;
    public IChunkProvider chunkProvider;
    private IChunkLoader f;
    public boolean forceChunkLoad = false; // true -> false
    public LongObjectHashMap<Chunk> chunks = new LongObjectHashMap<Chunk>();
    private List chunkList = new ArrayList();
    public WorldServer world;
    // CraftBukkit end

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider) {
        this.emptyChunk = new EmptyChunk(worldserver, 0, 0);
        this.world = worldserver;
        this.f = ichunkloader;
        this.chunkProvider = ichunkprovider;
    }

    public boolean isChunkLoaded(int i, int j) {
        return this.chunks.containsKey(LongHash.toLong(i, j)); // CraftBukkit
    }

    public void queueUnload(int i, int j) {
        if (this.world.worldProvider.e()) {
            ChunkCoordinates chunkcoordinates = this.world.getSpawn();
            int k = i * 16 + 8 - chunkcoordinates.x;
            int l = j * 16 + 8 - chunkcoordinates.z;
            short short1 = 128;

            // CraftBukkit start
            if (k < -short1 || k > short1 || l < -short1 || l > short1 || !(this.world.keepSpawnInMemory)) { // Added 'this.world.keepSpawnInMemory'
                this.unloadQueue.add(i, j);

                Chunk c = this.chunks.get(LongHash.toLong(i, j));
                if (c != null) {
                    c.mustSave = true;
                }
            }
            // CraftBukkit end
        } else {
            // CraftBukkit start
            this.unloadQueue.add(i, j);

            Chunk c = this.chunks.get(LongHash.toLong(i, j));
            if (c != null) {
                c.mustSave = true;
            }
            // CraftBukkit end
        }
    }

    public void a() {
        Iterator iterator = this.chunks.values().iterator(); // CraftBukkit

        while (iterator.hasNext()) {
            Chunk chunk = (Chunk) iterator.next();

            this.queueUnload(chunk.locX, chunk.locZ);
        }
    }

    // CraftBukkit start - Add async variant, provide compatibility
    public Chunk getChunkAt(int i, int j) {
        return getChunkAt(i, j, null);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable) {
        this.unloadQueue.remove(i, j);
        Chunk chunk = (Chunk) this.chunks.get(LongHash.toLong(i, j));
        boolean newChunk = false;
        ChunkRegionLoader loader = null;

        if (this.f instanceof ChunkRegionLoader) {
            loader = (ChunkRegionLoader) this.f;
        }

        // If the chunk exists but isn't loaded do it async
        if (chunk == null && runnable != null && loader != null && loader.chunkExists(this.world, i, j)) {
            org.bukkit.craftbukkit.chunkio.ChunkIOExecutor.queueChunkLoad(this.world, loader, this, i, j, runnable);
            return null;
        }
        // CraftBukkit end

        if (chunk == null) {
            chunk = this.loadChunk(i, j);
            if (chunk == null) {
                if (this.chunkProvider == null) {
                    chunk = this.emptyChunk;
                } else {
                    try {
                        chunk = this.chunkProvider.getOrCreateChunk(i, j);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");

                        crashreportsystemdetails.a("Location", String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j)}));
                        crashreportsystemdetails.a("Position hash", Long.valueOf(LongHash.toLong(i, j))); // CraftBukkit - Use LongHash
                        crashreportsystemdetails.a("Generator", this.chunkProvider.getName());
                        throw new ReportedException(crashreport);
                    }
                }
                newChunk = true; // CraftBukkit
            }

            this.chunks.put(LongHash.toLong(i, j), chunk); // CraftBukkit
            this.chunkList.add(chunk);
            chunk.addEntities();

            // CraftBukkit start
            Server server = this.world.getServer();
            if (server != null) {
                /*
                 * If it's a new world, the first few chunks are generated inside
                 * the World constructor. We can't reliably alter that, so we have
                 * no way of creating a CraftWorld/CraftServer at that point.
                 */
                server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(chunk.bukkitChunk, newChunk));
            }
            // CraftBukkit end
            chunk.a(this, this, i, j);
        }

        // CraftBukkit start - If we didn't need to load the chunk run the callback now
        if (runnable != null) {
            runnable.run();
        }
        // CraftBukkit end

        return chunk;
    }

    public Chunk getOrCreateChunk(int i, int j) {
        // CraftBukkit start
        Chunk chunk = (Chunk) this.chunks.get(LongHash.toLong(i, j));

        chunk = chunk == null ? (!this.world.isLoading && !this.forceChunkLoad ? this.emptyChunk : this.getChunkAt(i, j)) : chunk;
        if (chunk == this.emptyChunk) return chunk;
        if (i != chunk.locX || j != chunk.locZ) {
            b.error("Chunk (" + chunk.locX + ", " + chunk.locZ + ") stored at  (" + i + ", " + j + ") in world '" + world.getWorld().getName() + "'");
            b.error(chunk.getClass().getName());
            Throwable ex = new Throwable();
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
        return chunk;
        // CraftBukkit end
    }

    public Chunk loadChunk(int i, int j) { // CraftBukkit - private -> public
        if (this.f == null) {
            return null;
        } else {
            try {
                Chunk chunk = this.f.a(this.world, i, j);

                if (chunk != null) {
                    chunk.p = this.world.getTime();
                    if (this.chunkProvider != null) {
                        this.chunkProvider.recreateStructures(i, j);
                    }
                }

                return chunk;
            } catch (Exception exception) {
                b.error("Couldn\'t load chunk", exception);
                return null;
            }
        }
    }

    public void saveChunkNOP(Chunk chunk) { // CraftBukkit - private -> public
        if (this.f != null) {
            try {
                this.f.b(this.world, chunk);
            } catch (Exception exception) {
                b.error("Couldn\'t save entities", exception);
            }
        }
    }

    public void saveChunk(Chunk chunk) { // CraftBukkit - private -> public
        if (this.f != null) {
            try {
                chunk.p = this.world.getTime();
                this.f.a(this.world, chunk);
                // CraftBukkit start - IOException to Exception
            } catch (Exception ioexception) {
                b.error("Couldn\'t save chunk", ioexception);
                /* Remove extra exception
            } catch (ExceptionWorldConflict exceptionworldconflict) {
                b.error("Couldn\'t save chunk; already in use by another instance of Minecraft?", exceptionworldconflict);
                // CraftBukkit end */
            }
        }
    }

    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        Chunk chunk = this.getOrCreateChunk(i, j);

        if (!chunk.done) {
            chunk.p();
            if (this.chunkProvider != null) {
                this.chunkProvider.getChunkAt(ichunkprovider, i, j);

                // CraftBukkit start
                BlockSand.instaFall = true;
                Random random = new Random();
                random.setSeed(world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) i * xRand + (long) j * zRand ^ world.getSeed());

                org.bukkit.World world = this.world.getWorld();
                if (world != null) {
                    for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                        populator.populate(world, random, chunk.bukkitChunk);
                    }
                }
                BlockSand.instaFall = false;
                this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(chunk.bukkitChunk));
                // CraftBukkit end

                chunk.e();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        int i = 0;
        // CraftBukkit start
        Iterator iterator = this.chunks.values().iterator();

        while (iterator.hasNext()) {
            Chunk chunk = (Chunk) iterator.next();
            // CraftBukkit end

            if (flag) {
                this.saveChunkNOP(chunk);
            }

            if (chunk.a(flag)) {
                this.saveChunk(chunk);
                chunk.n = false;
                ++i;
                if (i == 24 && !flag) {
                    return false;
                }
            }
        }

        return true;
    }

    public void b() {
        if (this.f != null) {
            this.f.b();
        }
    }

    public boolean unloadChunks() {
        if (!this.world.savingDisabled) {
            // CraftBukkit start
            Server server = this.world.getServer();
            for (int i = 0; i < 100 && !this.unloadQueue.isEmpty(); i++) {
                long chunkcoordinates = this.unloadQueue.popFirst();
                Chunk chunk = this.chunks.get(chunkcoordinates);
                if (chunk == null) continue;

                ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk);
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    chunk.removeEntities();
                    this.saveChunk(chunk);
                    this.saveChunkNOP(chunk);
                    // this.unloadQueue.remove(olong);
                    // this.chunks.remove(olong.longValue());
                    this.chunks.remove(chunkcoordinates); // CraftBukkit
                    this.chunkList.remove(chunk);
                }
            }
            // CraftBukkit end

            if (this.f != null) {
                this.f.a();
            }
        }

        return this.chunkProvider.unloadChunks();
    }

    public boolean canSave() {
        return !this.world.savingDisabled;
    }

    public String getName() {
        // CraftBukkit - this.chunks.count() -> .values().size()
        return "ServerChunkCache: " + this.chunks.values().size() + " Drop: " + this.unloadQueue.size();
    }

    public List getMobsFor(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        return this.chunkProvider.getMobsFor(enumcreaturetype, i, j, k);
    }

    public ChunkPosition findNearestMapFeature(World world, String s, int i, int j, int k) {
        return this.chunkProvider.findNearestMapFeature(world, s, i, j, k);
    }

    public int getLoadedChunks() {
        // CraftBukkit - this.chunks.count() -> .values().size()
        return this.chunks.values().size();
    }

    public void recreateStructures(int i, int j) {}
}
