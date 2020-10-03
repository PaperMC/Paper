package net.minecraft.server;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.Hash.Strategy;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemUtils {

    private static final AtomicInteger c = new AtomicInteger(1);
    private static final ExecutorService d = a("Bootstrap");
    private static final ExecutorService e = a("Main");
    private static final ExecutorService f = n();
    public static LongSupplier a = System::nanoTime;
    public static final UUID b = new UUID(0L, 0L);
    private static final Logger LOGGER = LogManager.getLogger();

    public static <K, V> Collector<Entry<? extends K, ? extends V>, ?, Map<K, V>> a() {
        return Collectors.toMap(Entry::getKey, Entry::getValue);
    }

    public static <T extends Comparable<T>> String a(IBlockState<T> iblockstate, Object object) {
        return iblockstate.a((Comparable) object);
    }

    public static String a(String s, @Nullable MinecraftKey minecraftkey) {
        return minecraftkey == null ? s + ".unregistered_sadface" : s + '.' + minecraftkey.getNamespace() + '.' + minecraftkey.getKey().replace('/', '.');
    }

    public static long getMonotonicMillis() {
        return getMonotonicNanos() / 1000000L;
    }

    public static long getMonotonicNanos() {
        return SystemUtils.a.getAsLong();
    }

    public static long getTimeMillis() {
        return Instant.now().toEpochMilli();
    }

    private static ExecutorService a(String s) {
        int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 7);
        Object object;

        if (i <= 0) {
            object = MoreExecutors.newDirectExecutorService();
        } else {
            object = new ForkJoinPool(i, (forkjoinpool) -> {
                ForkJoinWorkerThread forkjoinworkerthread = new ForkJoinWorkerThread(forkjoinpool) {
                    protected void onTermination(Throwable throwable) {
                        if (throwable != null) {
                            SystemUtils.LOGGER.warn("{} died", this.getName(), throwable);
                        } else {
                            SystemUtils.LOGGER.debug("{} shutdown", this.getName());
                        }

                        super.onTermination(throwable);
                    }
                };

                forkjoinworkerthread.setName("Worker-" + s + "-" + SystemUtils.c.getAndIncrement());
                return forkjoinworkerthread;
            }, SystemUtils::a, true);
        }

        return (ExecutorService) object;
    }

    public static Executor e() {
        return SystemUtils.d;
    }

    public static Executor f() {
        return SystemUtils.e;
    }

    public static Executor g() {
        return SystemUtils.f;
    }

    public static void h() {
        a(SystemUtils.e);
        a(SystemUtils.f);
    }

    private static void a(ExecutorService executorservice) {
        executorservice.shutdown();

        boolean flag;

        try {
            flag = executorservice.awaitTermination(3L, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedexception) {
            flag = false;
        }

        if (!flag) {
            executorservice.shutdownNow();
        }

    }

    private static ExecutorService n() {
        return Executors.newCachedThreadPool((runnable) -> {
            Thread thread = new Thread(runnable);

            thread.setName("IO-Worker-" + SystemUtils.c.getAndIncrement());
            thread.setUncaughtExceptionHandler(SystemUtils::a);
            return thread;
        });
    }

    private static void a(Thread thread, Throwable throwable) {
        c(throwable);
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

        if (throwable instanceof ReportedException) {
            DispenserRegistry.a(((ReportedException) throwable).a().e());
            System.exit(-1);
        }

        SystemUtils.LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
    }

    @Nullable
    public static Type<?> a(TypeReference typereference, String s) {
        return !SharedConstants.c ? null : b(typereference, s);
    }

    @Nullable
    private static Type<?> b(TypeReference typereference, String s) {
        Type type = null;

        try {
            type = DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())).getChoiceType(typereference, s);
        } catch (IllegalArgumentException illegalargumentexception) {
            SystemUtils.LOGGER.error("No data fixer registered for {}", s);
            if (SharedConstants.d) {
                throw illegalargumentexception;
            }
        }

        return type;
    }

    public static SystemUtils.OS i() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        return s.contains("win") ? SystemUtils.OS.WINDOWS : (s.contains("mac") ? SystemUtils.OS.OSX : (s.contains("solaris") ? SystemUtils.OS.SOLARIS : (s.contains("sunos") ? SystemUtils.OS.SOLARIS : (s.contains("linux") ? SystemUtils.OS.LINUX : (s.contains("unix") ? SystemUtils.OS.LINUX : SystemUtils.OS.UNKNOWN)))));
    }

    public static Stream<String> j() {
        RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();

        return runtimemxbean.getInputArguments().stream().filter((s) -> {
            return s.startsWith("-X");
        });
    }

    public static <T> T a(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> T a(Iterable<T> iterable, @Nullable T t0) {
        Iterator<T> iterator = iterable.iterator();
        T t1 = iterator.next();

        if (t0 != null) {
            Object object = t1;

            while (object != t0) {
                if (iterator.hasNext()) {
                    object = iterator.next();
                }
            }

            if (iterator.hasNext()) {
                return iterator.next();
            }
        }

        return t1;
    }

    public static <T> T b(Iterable<T> iterable, @Nullable T t0) {
        Iterator<T> iterator = iterable.iterator();

        Object object;
        Object object1;

        for (object1 = null; iterator.hasNext(); object1 = object) {
            object = iterator.next();
            if (object == t0) {
                if (object1 == null) {
                    object1 = iterator.hasNext() ? Iterators.getLast(iterator) : t0;
                }
                break;
            }
        }

        return object1;
    }

    public static <T> T a(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> T a(T t0, Consumer<T> consumer) {
        consumer.accept(t0);
        return t0;
    }

    public static <K> Strategy<K> k() {
        return SystemUtils.IdentityHashingStrategy.INSTANCE;
    }

    public static <V> CompletableFuture<List<V>> b(List<? extends CompletableFuture<? extends V>> list) {
        List<V> list1 = Lists.newArrayListWithCapacity(list.size());
        CompletableFuture<?>[] acompletablefuture = new CompletableFuture[list.size()];
        CompletableFuture<Void> completablefuture = new CompletableFuture();

        list.forEach((completablefuture1) -> {
            int i = list1.size();

            list1.add((Object) null);
            acompletablefuture[i] = completablefuture1.whenComplete((object, throwable) -> {
                if (throwable != null) {
                    completablefuture.completeExceptionally(throwable);
                } else {
                    list1.set(i, object);
                }

            });
        });
        return CompletableFuture.allOf(acompletablefuture).applyToEither(completablefuture, (ovoid) -> {
            return list1;
        });
    }

    public static <T> Stream<T> a(Optional<? extends T> optional) {
        return (Stream) DataFixUtils.orElseGet(optional.map(Stream::of), Stream::empty);
    }

    public static <T> Optional<T> a(Optional<T> optional, Consumer<T> consumer, Runnable runnable) {
        if (optional.isPresent()) {
            consumer.accept(optional.get());
        } else {
            runnable.run();
        }

        return optional;
    }

    public static Runnable a(Runnable runnable, Supplier<String> supplier) {
        return runnable;
    }

    public static <T extends Throwable> T c(T t0) {
        if (SharedConstants.d) {
            SystemUtils.LOGGER.error("Trying to throw a fatal exception, pausing in IDE", t0);

            while (true) {
                try {
                    Thread.sleep(1000L);
                    SystemUtils.LOGGER.error("paused");
                } catch (InterruptedException interruptedexception) {
                    return t0;
                }
            }
        } else {
            return t0;
        }
    }

    public static String d(Throwable throwable) {
        return throwable.getCause() != null ? d(throwable.getCause()) : (throwable.getMessage() != null ? throwable.getMessage() : throwable.toString());
    }

    public static <T> T a(T[] at, Random random) {
        return at[random.nextInt(at.length)];
    }

    public static int a(int[] aint, Random random) {
        return aint[random.nextInt(aint.length)];
    }

    private static BooleanSupplier a(final java.nio.file.Path java_nio_file_path, final java.nio.file.Path java_nio_file_path1) {
        return new BooleanSupplier() {
            public boolean getAsBoolean() {
                try {
                    Files.move(java_nio_file_path, java_nio_file_path1);
                    return true;
                } catch (IOException ioexception) {
                    SystemUtils.LOGGER.error("Failed to rename", ioexception);
                    return false;
                }
            }

            public String toString() {
                return "rename " + java_nio_file_path + " to " + java_nio_file_path1;
            }
        };
    }

    private static BooleanSupplier a(final java.nio.file.Path java_nio_file_path) {
        return new BooleanSupplier() {
            public boolean getAsBoolean() {
                try {
                    Files.deleteIfExists(java_nio_file_path);
                    return true;
                } catch (IOException ioexception) {
                    SystemUtils.LOGGER.warn("Failed to delete", ioexception);
                    return false;
                }
            }

            public String toString() {
                return "delete old " + java_nio_file_path;
            }
        };
    }

    private static BooleanSupplier b(final java.nio.file.Path java_nio_file_path) {
        return new BooleanSupplier() {
            public boolean getAsBoolean() {
                return !Files.exists(java_nio_file_path, new LinkOption[0]);
            }

            public String toString() {
                return "verify that " + java_nio_file_path + " is deleted";
            }
        };
    }

    private static BooleanSupplier c(final java.nio.file.Path java_nio_file_path) {
        return new BooleanSupplier() {
            public boolean getAsBoolean() {
                return Files.isRegularFile(java_nio_file_path, new LinkOption[0]);
            }

            public String toString() {
                return "verify that " + java_nio_file_path + " is present";
            }
        };
    }

    private static boolean a(BooleanSupplier... abooleansupplier) {
        BooleanSupplier[] abooleansupplier1 = abooleansupplier;
        int i = abooleansupplier.length;

        for (int j = 0; j < i; ++j) {
            BooleanSupplier booleansupplier = abooleansupplier1[j];

            if (!booleansupplier.getAsBoolean()) {
                SystemUtils.LOGGER.warn("Failed to execute {}", booleansupplier);
                return false;
            }
        }

        return true;
    }

    private static boolean a(int i, String s, BooleanSupplier... abooleansupplier) {
        for (int j = 0; j < i; ++j) {
            if (a(abooleansupplier)) {
                return true;
            }

            SystemUtils.LOGGER.error("Failed to {}, retrying {}/{}", s, j, i);
        }

        SystemUtils.LOGGER.error("Failed to {}, aborting, progress might be lost", s);
        return false;
    }

    public static void a(File file, File file1, File file2) {
        a(file.toPath(), file1.toPath(), file2.toPath());
    }

    public static void a(java.nio.file.Path java_nio_file_path, java.nio.file.Path java_nio_file_path1, java.nio.file.Path java_nio_file_path2) {
        boolean flag = true;

        if (!Files.exists(java_nio_file_path, new LinkOption[0]) || a(10, "create backup " + java_nio_file_path2, a(java_nio_file_path2), a(java_nio_file_path, java_nio_file_path2), c(java_nio_file_path2))) {
            if (a(10, "remove old " + java_nio_file_path, a(java_nio_file_path), b(java_nio_file_path))) {
                if (!a(10, "replace " + java_nio_file_path + " with " + java_nio_file_path1, a(java_nio_file_path1, java_nio_file_path), c(java_nio_file_path))) {
                    a(10, "restore " + java_nio_file_path + " from " + java_nio_file_path2, a(java_nio_file_path2, java_nio_file_path), c(java_nio_file_path));
                }

            }
        }
    }

    public static Consumer<String> a(String s, Consumer<String> consumer) {
        return (s1) -> {
            consumer.accept(s + s1);
        };
    }

    public static DataResult<int[]> a(IntStream intstream, int i) {
        int[] aint = intstream.limit((long) (i + 1)).toArray();

        if (aint.length != i) {
            String s = "Input is not a list of " + i + " ints";

            return aint.length >= i ? DataResult.error(s, Arrays.copyOf(aint, i)) : DataResult.error(s);
        } else {
            return DataResult.success(aint);
        }
    }

    public static void l() {
        Thread thread = new Thread("Timer hack thread") {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2147483647L);
                    } catch (InterruptedException interruptedexception) {
                        SystemUtils.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
                        return;
                    }
                }
            }
        };

        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(SystemUtils.LOGGER));
        thread.start();
    }

    static enum IdentityHashingStrategy implements Strategy<Object> {

        INSTANCE;

        private IdentityHashingStrategy() {}

        public int hashCode(Object object) {
            return System.identityHashCode(object);
        }

        public boolean equals(Object object, Object object1) {
            return object == object1;
        }
    }

    public static enum OS {

        LINUX, SOLARIS, WINDOWS {
        },
        OSX {
        },
        UNKNOWN;

        private OS() {}
    }
}
