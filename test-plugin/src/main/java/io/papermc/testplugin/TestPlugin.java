package io.papermc.testplugin;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import jdk.jfr.EventSettings;
import jdk.jfr.Recording;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class TestPlugin extends JavaPlugin implements Listener {

    private static final int WARMUP_TICKS = 200;
    private static final int MEASUREMENT_TICKS = 600;
    private static final int CALLS_PER_TICK = 20_000;

    private final List<Double> tickDurations = new ArrayList<>(MEASUREMENT_TICKS);
    private int workloadTicks;
    private boolean measuring;
    private boolean finishing;
    private Recording recording;
    private MethodHandle entityInside;
    private Object level;
    private Object blockPos;
    private Object entity;
    private Object blockState;
    private Object noOpEffectApplier;
    private Path output;
    private long cpuStartNanos;

    @Override
    public void onEnable() {
        this.output = Path.of(System.getenv().getOrDefault("PAPER14153_OUTPUT", "paper14153.jfr"));
        this.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTask(this, this::initialize);
    }

    private void initialize() {
        try {
            final World world = Bukkit.getWorlds().get(0);
            final int x = 0;
            final int y = world.getHighestBlockYAt(0, 0) + 1;
            final int z = 0;
            world.getBlockAt(x, y, z).setType(Material.CACTUS, false);

            final ArmorStand stand = world.spawn(new Location(world, x + 0.5, y, z + 0.5), ArmorStand.class);
            stand.setInvulnerable(true);
            stand.setGravity(false);
            stand.setInvisible(true);

            final Object serverLevel = world.getClass().getMethod("getHandle").invoke(world);
            final Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            final Constructor<?> blockPosConstructor = blockPosClass.getConstructor(int.class, int.class, int.class);
            this.blockPos = blockPosConstructor.newInstance(x, y, z);
            this.level = serverLevel;
            this.entity = stand.getClass().getMethod("getHandle").invoke(stand);
            this.blockState = serverLevel.getClass().getMethod("getBlockState", blockPosClass).invoke(serverLevel, this.blockPos);

            final Class<?> effectApplierClass = Class.forName("net.minecraft.world.entity.InsideBlockEffectApplier");
            final Field noOpField = effectApplierClass.getField("NOOP");
            this.noOpEffectApplier = noOpField.get(null);
            final Class<?> levelClass = Class.forName("net.minecraft.world.level.Level");
            final Class<?> entityClass = Class.forName("net.minecraft.world.entity.Entity");
            final Method entityInsideMethod = this.blockState.getClass().getMethod(
                "entityInside", levelClass, blockPosClass, entityClass, effectApplierClass, boolean.class
            );
            this.entityInside = MethodHandles.lookup().unreflect(entityInsideMethod).asType(
                MethodType.methodType(void.class, Object.class, Object.class, Object.class, Object.class, Object.class, boolean.class)
            );
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to initialize benchmark harness", exception);
        }

        Bukkit.getScheduler().runTaskTimer(this, this::runWorkload, 1L, 1L);
    }

    private void runWorkload() {
        if (this.workloadTicks == WARMUP_TICKS) {
            this.startRecording();
            this.cpuStartNanos = this.currentThreadCpuTime();
            this.measuring = true;
        }

        try {
            for (int i = 0; i < CALLS_PER_TICK; i++) {
                this.entityInside.invokeExact(this.blockState, this.level, this.blockPos, this.entity, this.noOpEffectApplier, true);
            }
        } catch (Throwable throwable) {
            throw new IllegalStateException("Benchmark workload failed", throwable);
        }

        this.workloadTicks++;
        if (this.workloadTicks >= WARMUP_TICKS + MEASUREMENT_TICKS) {
            this.finishing = true;
        }
    }

    @EventHandler
    public void onServerTickEnd(final ServerTickEndEvent event) {
        if (this.measuring && this.tickDurations.size() < MEASUREMENT_TICKS) {
            this.tickDurations.add(event.getTickDuration());
        }
        if (this.finishing && this.tickDurations.size() >= MEASUREMENT_TICKS) {
            this.finish();
        }
    }

    private void startRecording() {
        try {
            Files.createDirectories(this.output.toAbsolutePath().getParent());
            this.recording = new Recording();
            this.enable("jdk.ExecutionSample", settings -> settings.withPeriod(Duration.ofMillis(10)));
            this.enable("jdk.ObjectAllocationInNewTLAB", settings -> settings.withThreshold(Duration.ZERO));
            this.enable("jdk.ObjectAllocationOutsideTLAB", settings -> settings.withThreshold(Duration.ZERO));
            this.enable("jdk.GarbageCollection", settings -> settings.withThreshold(Duration.ZERO));
            this.enable("jdk.GCHeapSummary", settings -> settings.withThreshold(Duration.ZERO));
            this.enable("minecraft.ServerTickTime", settings -> settings.withThreshold(Duration.ZERO));
            this.recording.setToDisk(true);
            this.recording.setDestination(this.output);
            this.recording.start();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to start JFR recording", exception);
        }
    }

    private void enable(final String name, final java.util.function.Consumer<EventSettings> configuration) {
        try {
            configuration.accept(this.recording.enable(name));
        } catch (IllegalArgumentException ignored) {
            this.getLogger().warning("JFR event unavailable: " + name);
        }
    }

    private void finish() {
        final long cpuNanos = this.currentThreadCpuTime() - this.cpuStartNanos;
        if (this.recording != null) {
            this.recording.stop();
            this.recording.close();
        }
        final Path csv = Path.of(this.output.toString().replaceFirst("\\.jfr$", "") + ".ticks.csv");
        final Path metrics = Path.of(this.output.toString().replaceFirst("\\.jfr$", "") + ".metrics.txt");
        final StringBuilder values = new StringBuilder("tick,duration_ms\n");
        for (int i = 0; i < this.tickDurations.size(); i++) {
            values.append(i + 1).append(',').append(this.tickDurations.get(i)).append('\n');
        }
        try {
            Files.writeString(csv, values);
            Files.writeString(metrics, "calls=" + ((long) CALLS_PER_TICK * MEASUREMENT_TICKS)
                + "\nticks=" + this.tickDurations.size()
                + "\nmain_thread_cpu_ms=" + (cpuNanos / 1_000_000.0) + "\n");
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write benchmark measurements", exception);
        }
        this.getLogger().info("PAPER14153_BENCH calls=" + ((long) CALLS_PER_TICK * MEASUREMENT_TICKS)
            + " ticks=" + this.tickDurations.size() + " jfr=" + this.output + " csv=" + csv + " metrics=" + metrics);
        Bukkit.shutdown();
    }

    private long currentThreadCpuTime() {
        final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : -1L;
    }
}

