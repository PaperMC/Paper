package io.papermc.paper.threadedregions.scheduler;

import com.mojang.logging.LogUtils;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class FoliaAsyncScheduler implements AsyncScheduler {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final Executor executors = new ThreadPoolExecutor(Math.max(4, Runtime.getRuntime().availableProcessors() / 2), Integer.MAX_VALUE,
        30L, TimeUnit.SECONDS, new SynchronousQueue<>(),
        new ThreadFactory() {
            private final AtomicInteger idGenerator = new AtomicInteger();

            @Override
            public Thread newThread(final Runnable run) {
                final Thread ret = new Thread(run);

                ret.setName("Folia Async Scheduler Thread #" + this.idGenerator.getAndIncrement());
                ret.setPriority(Thread.NORM_PRIORITY - 1);
                ret.setUncaughtExceptionHandler((final Thread thread, final Throwable thr) -> {
                    LOGGER.error("Uncaught exception in thread: " + thread.getName(), thr);
                });

                return ret;
            }
        }
    );

    private final ScheduledExecutorService timerThread = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable run) {
            final Thread ret = new Thread(run);

            ret.setName("Folia Async Scheduler Thread Timer");
            ret.setPriority(Thread.NORM_PRIORITY + 1);
            ret.setUncaughtExceptionHandler((final Thread thread, final Throwable thr) -> {
                LOGGER.error("Uncaught exception in thread: " + thread.getName(), thr);
            });

            return ret;
        }
    });

    private final Set<AsyncScheduledTask> tasks = ConcurrentHashMap.newKeySet();

    @Override
    public ScheduledTask runNow(final Plugin plugin, final Consumer<ScheduledTask> task) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(task, "Task may not be null");

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }

        final AsyncScheduledTask ret = new AsyncScheduledTask(plugin, -1L, task, null, -1L);

        this.tasks.add(ret);
        this.executors.execute(ret);

        if (!plugin.isEnabled()) {
            // handle race condition where plugin is disabled asynchronously
            ret.cancel();
        }

        return ret;
    }

    @Override
    public ScheduledTask runDelayed(final Plugin plugin, final Consumer<ScheduledTask> task, final long delay,
                                    final TimeUnit unit) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(task, "Task may not be null");
        Objects.requireNonNull(unit, "Time unit may not be null");
        if (delay < 0L) {
            throw new IllegalArgumentException("Delay may not be < 0");
        }

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }

        return this.scheduleTimerTask(plugin, task, delay, -1L, unit);
    }

    @Override
    public ScheduledTask runAtFixedRate(final Plugin plugin, final Consumer<ScheduledTask> task, final long initialDelay,
                                        final long period, final TimeUnit unit) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(task, "Task may not be null");
        Objects.requireNonNull(unit, "Time unit may not be null");
        if (initialDelay < 0L) {
            throw new IllegalArgumentException("Initial delay may not be < 0");
        }
        if (period <= 0L) {
            throw new IllegalArgumentException("Period may not be <= 0");
        }

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }

        return this.scheduleTimerTask(plugin, task, initialDelay, period, unit);
    }

    private AsyncScheduledTask scheduleTimerTask(final Plugin plugin, final Consumer<ScheduledTask> task, final long initialDelay,
                                                 final long period, final TimeUnit unit) {
        final AsyncScheduledTask ret = new AsyncScheduledTask(
            plugin, period <= 0 ? period : unit.toNanos(period), task, null,
            System.nanoTime() + unit.toNanos(initialDelay)
        );

        synchronized (ret) {
            // even though ret is not published, we need to synchronise while scheduling to avoid a race condition
            // for when a scheduled task immediately executes before we update the delay field and state field
            ret.setDelay(this.timerThread.schedule(ret, initialDelay, unit));
            this.tasks.add(ret);
        }

        if (!plugin.isEnabled()) {
            // handle race condition where plugin is disabled asynchronously
            ret.cancel();
        }

        return ret;
    }

    @Override
    public void cancelTasks(final Plugin plugin) {
        Objects.requireNonNull(plugin, "Plugin may not be null");

        for (final AsyncScheduledTask task : this.tasks) {
            if (task.plugin == plugin) {
                task.cancel();
            }
        }
    }

    private final class AsyncScheduledTask implements ScheduledTask, Runnable {

        private static final int STATE_ON_TIMER            = 0;
        private static final int STATE_SCHEDULED_EXECUTOR  = 1;
        private static final int STATE_EXECUTING           = 2;
        private static final int STATE_EXECUTING_CANCELLED = 3;
        private static final int STATE_FINISHED            = 4;
        private static final int STATE_CANCELLED           = 5;

        private final Plugin plugin;
        private final long repeatDelay; // in ns
        private Consumer<ScheduledTask> run;
        private ScheduledFuture<?> delay;
        private int state;
        private long scheduleTarget;

        public AsyncScheduledTask(final Plugin plugin, final long repeatDelay, final Consumer<ScheduledTask> run,
                                  final ScheduledFuture<?> delay, final long firstTarget) {
            this.plugin = plugin;
            this.repeatDelay = repeatDelay;
            this.run = run;
            this.delay = delay;
            this.state = delay == null ? STATE_SCHEDULED_EXECUTOR : STATE_ON_TIMER;
            this.scheduleTarget = firstTarget;
        }

        private void setDelay(final ScheduledFuture<?> delay) {
            this.delay = delay;
            this.state = delay == null ? STATE_SCHEDULED_EXECUTOR : STATE_ON_TIMER;
        }

        @Override
        public void run() {
            final boolean repeating = this.isRepeatingTask();
            // try to advance state
            final boolean timer;
            synchronized (this) {
                if (this.state == STATE_ON_TIMER) {
                    timer = true;
                    this.delay = null;
                    this.state = STATE_SCHEDULED_EXECUTOR;
                } else if (this.state != STATE_SCHEDULED_EXECUTOR) {
                    // cancelled
                    if (this.state != STATE_CANCELLED) {
                        throw new IllegalStateException("Wrong state: " + this.state);
                    }
                    return;
                } else {
                    timer = false;
                    this.state = STATE_EXECUTING;
                }
            }

            if (timer) {
                // the scheduled executor is single thread, and unfortunately not expandable with threads
                // so we just schedule onto the executor
                FoliaAsyncScheduler.this.executors.execute(this);
                return;
            }

            try {
                this.run.accept(this);
            } catch (final Throwable throwable) {
                this.plugin.getLogger().log(Level.WARNING, "Async task for " + this.plugin.getDescription().getFullName() + " generated an exception", throwable);
            } finally {
                boolean removeFromTasks = false;
                synchronized (this) {
                    if (!repeating) {
                        // only want to execute once, so we're done
                        removeFromTasks = true;
                        this.state = STATE_FINISHED;
                    } else if (this.state != STATE_EXECUTING_CANCELLED) {
                        this.state = STATE_ON_TIMER;
                        // account for any delays, whether it be by task exec. or scheduler issues so that we keep
                        // the fixed schedule
                        final long currTime = System.nanoTime();
                        final long delay = Math.max(0L, this.scheduleTarget + this.repeatDelay - currTime);
                        this.scheduleTarget = currTime + delay;
                        this.delay = FoliaAsyncScheduler.this.timerThread.schedule(this, delay, TimeUnit.NANOSECONDS);
                    } else {
                        // cancelled repeating task
                        removeFromTasks = true;
                    }
                }

                if (removeFromTasks) {
                    this.run = null;
                    FoliaAsyncScheduler.this.tasks.remove(this);
                }
            }
        }

        @Override
        public Plugin getOwningPlugin() {
            return this.plugin;
        }

        @Override
        public boolean isRepeatingTask() {
            return this.repeatDelay > 0L;
        }

        @Override
        public CancelledState cancel() {
            ScheduledFuture<?> delay = null;
            CancelledState ret;
            synchronized (this) {
                switch (this.state) {
                    case STATE_ON_TIMER: {
                        delay = this.delay;
                        this.delay = null;
                        this.state = STATE_CANCELLED;
                        ret = CancelledState.CANCELLED_BY_CALLER;
                        break;
                    }
                    case STATE_SCHEDULED_EXECUTOR: {
                        this.state = STATE_CANCELLED;
                        ret = CancelledState.CANCELLED_BY_CALLER;
                        break;
                    }
                    case STATE_EXECUTING: {
                        if (!this.isRepeatingTask()) {
                            return CancelledState.RUNNING;
                        }
                        this.state = STATE_EXECUTING_CANCELLED;
                        return CancelledState.NEXT_RUNS_CANCELLED;
                    }
                    case STATE_EXECUTING_CANCELLED: {
                        return CancelledState.NEXT_RUNS_CANCELLED_ALREADY;
                    }
                    case STATE_FINISHED: {
                        return CancelledState.ALREADY_EXECUTED;
                    }
                    case STATE_CANCELLED: {
                        return CancelledState.CANCELLED_ALREADY;
                    }
                    default: {
                        throw new IllegalStateException("Unknown state: " + this.state);
                    }
                }
            }

            if (delay != null) {
                delay.cancel(false);
            }
            this.run = null;
            FoliaAsyncScheduler.this.tasks.remove(this);
            return ret;
        }

        @Override
        public ExecutionState getExecutionState() {
            synchronized (this) {
                switch (this.state) {
                    case STATE_ON_TIMER:
                    case STATE_SCHEDULED_EXECUTOR:
                        return ExecutionState.IDLE;
                    case STATE_EXECUTING:
                        return ExecutionState.RUNNING;
                    case STATE_EXECUTING_CANCELLED:
                        return ExecutionState.CANCELLED_RUNNING;
                    case STATE_FINISHED:
                        return ExecutionState.FINISHED;
                    case STATE_CANCELLED:
                        return ExecutionState.CANCELLED;
                    default: {
                        throw new IllegalStateException("Unknown state: " + this.state);
                    }
                }
            }
        }
    }
}
