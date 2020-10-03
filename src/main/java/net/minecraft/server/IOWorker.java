package net.minecraft.server;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IOWorker implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final AtomicBoolean b = new AtomicBoolean();
    private final ThreadedMailbox<PairedQueue.b> c;
    private final RegionFileCache d;
    private final Map<ChunkCoordIntPair, IOWorker.a> e = Maps.newLinkedHashMap();

    protected IOWorker(File file, boolean flag, String s) {
        this.d = new RegionFileCache(file, flag);
        this.c = new ThreadedMailbox<>(new PairedQueue.a(IOWorker.Priority.values().length), SystemUtils.g(), "IOWorker-" + s);
    }

    public CompletableFuture<Void> a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
        return this.a(() -> {
            IOWorker.a ioworker_a = (IOWorker.a) this.e.computeIfAbsent(chunkcoordintpair, (chunkcoordintpair1) -> {
                return new IOWorker.a(nbttagcompound);
            });

            ioworker_a.a = nbttagcompound;
            return Either.left(ioworker_a.b);
        }).thenCompose(Function.identity());
    }

    @Nullable
    public NBTTagCompound a(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        CompletableFuture completablefuture = this.a(() -> {
            IOWorker.a ioworker_a = (IOWorker.a) this.e.get(chunkcoordintpair);

            if (ioworker_a != null) {
                return Either.left(ioworker_a.a);
            } else {
                try {
                    NBTTagCompound nbttagcompound = this.d.read(chunkcoordintpair);

                    return Either.left(nbttagcompound);
                } catch (Exception exception) {
                    IOWorker.LOGGER.warn("Failed to read chunk {}", chunkcoordintpair, exception);
                    return Either.right(exception);
                }
            }
        });

        try {
            return (NBTTagCompound) completablefuture.join();
        } catch (CompletionException completionexception) {
            if (completionexception.getCause() instanceof IOException) {
                throw (IOException) completionexception.getCause();
            } else {
                throw completionexception;
            }
        }
    }

    public CompletableFuture<Void> a() {
        CompletableFuture<Void> completablefuture = this.a(() -> {
            return Either.left(CompletableFuture.allOf((CompletableFuture[]) this.e.values().stream().map((ioworker_a) -> {
                return ioworker_a.b;
            }).toArray((i) -> {
                return new CompletableFuture[i];
            })));
        }).thenCompose(Function.identity());

        return completablefuture.thenCompose((ovoid) -> {
            return this.a(() -> {
                try {
                    this.d.a();
                    return Either.left((Object) null);
                } catch (Exception exception) {
                    IOWorker.LOGGER.warn("Failed to synchronized chunks", exception);
                    return Either.right(exception);
                }
            });
        });
    }

    private <T> CompletableFuture<T> a(Supplier<Either<T, Exception>> supplier) {
        return this.c.c((mailbox) -> {
            return new PairedQueue.b(IOWorker.Priority.HIGH.ordinal(), () -> {
                if (!this.b.get()) {
                    mailbox.a(supplier.get());
                }

                this.c();
            });
        });
    }

    private void b() {
        Iterator<Entry<ChunkCoordIntPair, IOWorker.a>> iterator = this.e.entrySet().iterator();

        if (iterator.hasNext()) {
            Entry<ChunkCoordIntPair, IOWorker.a> entry = (Entry) iterator.next();

            iterator.remove();
            this.a((ChunkCoordIntPair) entry.getKey(), (IOWorker.a) entry.getValue());
            this.c();
        }
    }

    private void c() {
        this.c.a((Object) (new PairedQueue.b(IOWorker.Priority.LOW.ordinal(), this::b)));
    }

    private void a(ChunkCoordIntPair chunkcoordintpair, IOWorker.a ioworker_a) {
        try {
            this.d.write(chunkcoordintpair, ioworker_a.a);
            ioworker_a.b.complete((Object) null);
        } catch (Exception exception) {
            IOWorker.LOGGER.error("Failed to store chunk {}", chunkcoordintpair, exception);
            ioworker_a.b.completeExceptionally(exception);
        }

    }

    public void close() throws IOException {
        if (this.b.compareAndSet(false, true)) {
            CompletableFuture completablefuture = this.c.b((mailbox) -> {
                return new PairedQueue.b(IOWorker.Priority.HIGH.ordinal(), () -> {
                    mailbox.a(Unit.INSTANCE);
                });
            });

            try {
                completablefuture.join();
            } catch (CompletionException completionexception) {
                if (completionexception.getCause() instanceof IOException) {
                    throw (IOException) completionexception.getCause();
                }

                throw completionexception;
            }

            this.c.close();
            this.e.forEach(this::a);
            this.e.clear();

            try {
                this.d.close();
            } catch (Exception exception) {
                IOWorker.LOGGER.error("Failed to close storage", exception);
            }

        }
    }

    static class a {

        private NBTTagCompound a;
        private final CompletableFuture<Void> b = new CompletableFuture();

        public a(NBTTagCompound nbttagcompound) {
            this.a = nbttagcompound;
        }
    }

    static enum Priority {

        HIGH, LOW;

        private Priority() {}
    }
}
