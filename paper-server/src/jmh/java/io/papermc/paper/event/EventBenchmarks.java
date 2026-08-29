package io.papermc.paper.event;

import io.papermc.paper.plugin.TestEvent;
import io.papermc.paper.plugin.TestEvent2;
import io.papermc.paper.plugin.TestEvent3;
import io.papermc.paper.plugin.manager.PaperEventManager;
import net.minecraft.SharedConstants;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(2)
public class EventBenchmarks {

    @Param({"true", "false"})
    private boolean hasListener;

    private static final MutableCallSite PEM = new MutableCallSite(MethodType.methodType(PaperEventManager.class));

    @Setup
    public void setup() {
        SharedConstants.tryDetectVersion();
        Server server = CallEventBenchmarkServer.setup();
        Bukkit.setServer(server);
        // var pem = new PaperEventManager(server);
        // PEM.setTarget(MethodHandles.constant(PaperEventManager.class, pem));
        // MutableCallSite.syncAll(new MutableCallSite[] { PEM });
        if (hasListener) {
            JavaPlugin plugin = new JavaPlugin() {
            };
            plugin.setEnabled(true);
            RegisteredListener listener = new RegisteredListener(new Listener() {
            }, new EventExecutor() {
                @Override
                public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

                }
            }, EventPriority.NORMAL, plugin, false);
            TestEvent.getHandlerList().register(listener);
            TestEvent2.getHandlerList().register(listener);
            TestEvent3.getHandlerList().register(listener);
        }
    }

    @Benchmark
    @OperationsPerInvocation(3)
    public void callNormal() {
        new TestEvent(false).callEvent();
        new TestEvent2(false).callEvent();
        new TestEvent3(false).callEvent();
    }

    // @Benchmark
    @OperationsPerInvocation(3)
    public void callSkipIndirections() throws Throwable {
        ((PaperEventManager) PEM.getTarget().invokeExact()).callEvent(new TestEvent(false));
        ((PaperEventManager) PEM.getTarget().invokeExact()).callEvent(new TestEvent2(false));
        ((PaperEventManager) PEM.getTarget().invokeExact()).callEvent(new TestEvent3(false));
    }

    @Benchmark
    public boolean registeredListenersLength() {
        return TestEvent.getHandlerList().getRegisteredListeners().length != 0;
    }

    @Benchmark
    public boolean hasListenersIndy() {
        return EventGuard.hasListeners(TestEvent.class);
    }

    // @Benchmark
    @OperationsPerInvocation(3)
    public int registeredListenersLengthMulti() {
        return
            TestEvent.getHandlerList().getRegisteredListeners().length +
            TestEvent2.getHandlerList().getRegisteredListeners().length +
            TestEvent3.getHandlerList().getRegisteredListeners().length;
    }
}
