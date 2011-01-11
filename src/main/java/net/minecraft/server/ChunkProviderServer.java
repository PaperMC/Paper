package net.minecraft.server;

import java.io.IOException;
import java.util.*;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event.Type;
import org.bukkit.event.world.ChunkLoadedEvent;
// CraftBukkit end

public class ChunkProviderServer implements IChunkProvider {

    private Set a;
    private Chunk b;
    private IChunkProvider c;
    private IChunkLoader d;
    private Map e;
    private List f;
    private WorldServer g;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider) {
        a = ((Set) (new HashSet()));
        e = ((Map) (new HashMap()));
        f = ((List) (new ArrayList()));
        b = new Chunk(((World) (worldserver)), new byte[32768], 0, 0);
        b.q = true;
        b.p = true;
        g = worldserver;
        d = ichunkloader;
        c = ichunkprovider;
    }

    public boolean a(int i, int j) {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(i, j);

        return e.containsKey(((chunkcoordinates)));
    }

    public void c(int i, int j) {
        int k = (i * 16 + 8) - g.m;
        int l = (j * 16 + 8) - g.o;
        char c1 = '\200';

        if (k < -c1 || k > c1 || l < -c1 || l > c1) {
            a.add(((new ChunkCoordinates(i, j))));
        }
    }

    public Chunk d(int i, int j) {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(i, j);

        a.remove(((new ChunkCoordinates(i, j))));
        Chunk chunk = (Chunk) e.get(((chunkcoordinates)));

        if (chunk == null) {
            chunk = e(i, j);
            if (chunk == null) {
                if (c == null) {
                    chunk = b;
                } else {
                    chunk = c.b(i, j);
                }
            }
            e.put(((chunkcoordinates)), ((chunk)));
            f.add(((chunk)));
            chunk.c();
            if (chunk != null) {
                chunk.d();
            }

            // CraftBukkit start
            CraftServer server = g.getServer();
            if (server != null) {
                /*
                 * If it's a new world, the first few chunks are generated inside
                 * the World constructor. We can't reliably alter that, so we have
                 * no way of creating a CraftWorld/CraftServer at that point.
                 */
                CraftWorld world = g.getWorld();
                CraftChunk cchunk = world.updateChunk(i, j);
                server.getPluginManager().callEvent(new ChunkLoadedEvent(Type.CHUNK_LOADED, cchunk));
            }
            // CraftBukkit end

            if (!chunk.n && a(i + 1, j + 1) && a(i, j + 1) && a(i + 1, j)) {
                a(((IChunkProvider) (this)), i, j);
            }
            if (a(i - 1, j) && !b(i - 1, j).n && a(i - 1, j + 1) && a(i, j + 1) && a(i - 1, j)) {
                a(((IChunkProvider) (this)), i - 1, j);
            }
            if (a(i, j - 1) && !b(i, j - 1).n && a(i + 1, j - 1) && a(i, j - 1) && a(i + 1, j)) {
                a(((IChunkProvider) (this)), i, j - 1);
            }
            if (a(i - 1, j - 1) && !b(i - 1, j - 1).n && a(i - 1, j - 1) && a(i, j - 1) && a(i - 1, j)) {
                a(((IChunkProvider) (this)), i - 1, j - 1);
            }
        }
        return chunk;
    }

    public Chunk b(int i, int j) {
        ChunkCoordinates chunkcoordinates = new ChunkCoordinates(i, j);
        Chunk chunk = (Chunk) e.get(((chunkcoordinates)));

        if (chunk == null) {
            if (g.x) {
                return d(i, j);
            } else {
                return b;
            }
        } else {
            return chunk;
        }
    }

    private Chunk e(int i, int j) {
        if (d == null) {
            return null;
        }
        try {
            Chunk chunk = d.a(((World) (g)), i, j);

            if (chunk != null) {
                chunk.s = g.e;
            }
            return chunk;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private void a(Chunk chunk) {
        if (d == null) {
            return;
        }
        try {
            d.b(((World) (g)), chunk);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void b(Chunk chunk) {
        if (d == null) {
            return;
        }
        try {
            chunk.s = g.e;
            d.a(((World) (g)), chunk);
        } catch (Throwable ioexception) { // CraftBukkit -- downcast to work
            ioexception.printStackTrace();
        }
    }

    public void a(IChunkProvider ichunkprovider, int i, int j) {
        Chunk chunk = b(i, j);

        if (!chunk.n) {
            chunk.n = true;
            if (c != null) {
                c.a(ichunkprovider, i, j);
                chunk.f();
            }
        }
    }

    public boolean a(boolean flag, IProgressUpdate iprogressupdate) {
        int i = 0;

        for (int j = 0; j < f.size(); j++) {
            Chunk chunk = (Chunk) f.get(j);

            if (flag && !chunk.p) {
                a(chunk);
            }
            if (!chunk.a(flag)) {
                continue;
            }
            b(chunk);
            chunk.o = false;
            if (++i == 24 && !flag) {
                return false;
            }
        }

        if (flag) {
            if (d == null) {
                return true;
            }
            d.b();
        }
        return true;
    }

    public boolean a() {
        if (!g.C) {
            for (int i = 0; i < 100; i++) {
                if (!a.isEmpty()) {
                    ChunkCoordinates chunkcoordinates = (ChunkCoordinates) a.iterator().next();
                    Chunk chunk = b(chunkcoordinates.a, chunkcoordinates.b);

                    chunk.e();
                    b(chunk);
                    a(chunk);
                    a.remove(((chunkcoordinates)));
                    e.remove(((chunkcoordinates)));
                    f.remove(((chunk)));
                }
            }

            if (d != null) {
                d.a();
            }
        }
        return c.a();
    }

    public boolean b() {
        return !g.C;
    }
}
