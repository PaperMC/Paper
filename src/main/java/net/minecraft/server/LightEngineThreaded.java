package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LightEngineThreaded extends LightEngine implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ThreadedMailbox<Runnable> b;
    private final ObjectList<Pair<LightEngineThreaded.Update, Runnable>> c = new ObjectArrayList();
    private final PlayerChunkMap d;
    private final Mailbox<ChunkTaskQueueSorter.a<Runnable>> e;
    private volatile int f = 5;
    private final AtomicBoolean g = new AtomicBoolean();

    public LightEngineThreaded(ILightAccess ilightaccess, PlayerChunkMap playerchunkmap, boolean flag, ThreadedMailbox<Runnable> threadedmailbox, Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailbox) {
        super(ilightaccess, true, flag);
        this.d = playerchunkmap;
        this.e = mailbox;
        this.b = threadedmailbox;
    }

    public void close() {}

    @Override
    public int a(int i, boolean flag, boolean flag1) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition, int i) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.immutableCopy();

        this.a(blockposition.getX() >> 4, blockposition.getZ() >> 4, LightEngineThreaded.Update.POST_UPDATE, SystemUtils.a(() -> {
            super.a(blockposition1);
        }, () -> {
            return "checkBlock " + blockposition1;
        }));
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, false);
            super.a(chunkcoordintpair, false);

            int i;

            for (i = -1; i < 17; ++i) {
                super.a(EnumSkyBlock.BLOCK, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
                super.a(EnumSkyBlock.SKY, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
            }

            for (i = 0; i < 16; ++i) {
                super.a(SectionPosition.a(chunkcoordintpair, i), true);
            }

        }, () -> {
            return "updateChunkStatus " + chunkcoordintpair + " " + true;
        }));
    }

    @Override
    public void a(SectionPosition sectionposition, boolean flag) {
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(sectionposition, flag);
        }, () -> {
            return "updateSectionStatus " + sectionposition + " " + flag;
        }));
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(chunkcoordintpair, flag);
        }, () -> {
            return "enableLight " + chunkcoordintpair + " " + flag;
        }));
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, SectionPosition sectionposition, @Nullable NibbleArray nibblearray, boolean flag) {
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(enumskyblock, sectionposition, nibblearray, flag);
        }, () -> {
            return "queueData " + sectionposition;
        }));
    }

    private void a(int i, int j, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        this.a(i, j, this.d.c(ChunkCoordIntPair.pair(i, j)), lightenginethreaded_update, runnable);
    }

    private void a(int i, int j, IntSupplier intsupplier, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        this.e.a(ChunkTaskQueueSorter.a(() -> {
            this.c.add(Pair.of(lightenginethreaded_update, runnable));
            if (this.c.size() >= this.f) {
                this.b();
            }

        }, ChunkCoordIntPair.pair(i, j), intsupplier));
    }

    @Override
    public void b(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, flag);
        }, () -> {
            return "retainData " + chunkcoordintpair;
        }));
    }

    public CompletableFuture<IChunkAccess> a(IChunkAccess ichunkaccess, boolean flag) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();

        ichunkaccess.b(false);
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            ChunkSection[] achunksection = ichunkaccess.getSections();

            for (int i = 0; i < 16; ++i) {
                ChunkSection chunksection = achunksection[i];

                if (!ChunkSection.a(chunksection)) {
                    super.a(SectionPosition.a(chunkcoordintpair, i), false);
                }
            }

            super.a(chunkcoordintpair, true);
            if (!flag) {
                ichunkaccess.m().forEach((blockposition) -> {
                    super.a(blockposition, ichunkaccess.g(blockposition));
                });
            }

            this.d.c(chunkcoordintpair);
        }, () -> {
            return "lightChunk " + chunkcoordintpair + " " + flag;
        }));
        return CompletableFuture.supplyAsync(() -> {
            ichunkaccess.b(true);
            super.b(chunkcoordintpair, false);
            return ichunkaccess;
        }, (runnable) -> {
            this.a(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.POST_UPDATE, runnable);
        });
    }

    public void queueUpdate() {
        if ((!this.c.isEmpty() || super.a()) && this.g.compareAndSet(false, true)) {
            this.b.a((Object) (() -> {
                this.b();
                this.g.set(false);
            }));
        }

    }

    private void b() {
        int i = Math.min(this.c.size(), this.f);
        ObjectListIterator<Pair<LightEngineThreaded.Update, Runnable>> objectlistiterator = this.c.iterator();

        Pair pair;
        int j;

        for (j = 0; objectlistiterator.hasNext() && j < i; ++j) {
            pair = (Pair) objectlistiterator.next();
            if (pair.getFirst() == LightEngineThreaded.Update.PRE_UPDATE) {
                ((Runnable) pair.getSecond()).run();
            }
        }

        objectlistiterator.back(j);
        super.a(Integer.MAX_VALUE, true, true);

        for (j = 0; objectlistiterator.hasNext() && j < i; ++j) {
            pair = (Pair) objectlistiterator.next();
            if (pair.getFirst() == LightEngineThreaded.Update.POST_UPDATE) {
                ((Runnable) pair.getSecond()).run();
            }

            objectlistiterator.remove();
        }

    }

    public void a(int i) {
        this.f = i;
    }

    static enum Update {

        PRE_UPDATE, POST_UPDATE;

        private Update() {}
    }
}
