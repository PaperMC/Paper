package io.papermc.paper.threadedregions.scheduler;

import ca.spottedleaf.concurrentutil.util.ConcurrentUtil;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import java.lang.invoke.VarHandle;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class FoliaEntityScheduler implements EntityScheduler {

    private final CraftEntity entity;

    public FoliaEntityScheduler(final CraftEntity entity) {
        this.entity = entity;
    }

    private static Consumer<? extends Entity> wrap(final Plugin plugin, final Runnable runnable) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(runnable, "Runnable may not be null");

        return (final Entity nmsEntity) -> {
            if (!plugin.isEnabled()) {
                // don't execute if the plugin is disabled
                return;
            }
            try {
                runnable.run();
            } catch (final Throwable throwable) {
                plugin.getLogger().log(Level.WARNING, "Entity task for " + plugin.getDescription().getFullName() + " generated an exception", throwable);
            }
        };
    }

    @Override
    public boolean execute(final Plugin plugin, final Runnable run, final Runnable retired,
                           final long delay) {
        final Consumer<? extends Entity> runNMS = wrap(plugin, run);
        final Consumer<? extends Entity> runRetired = retired == null ? null : wrap(plugin, retired);

        return this.entity.taskScheduler.schedule(runNMS, runRetired, delay);
    }

    @Override
    public @Nullable ScheduledTask run(final Plugin plugin, final Consumer<ScheduledTask> task, final Runnable retired) {
        return this.runDelayed(plugin, task, retired, 1);
    }

    @Override
    public @Nullable ScheduledTask runDelayed(final Plugin plugin, final Consumer<ScheduledTask> task, final Runnable retired,
                                              final long delayTicks) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(task, "Task may not be null");
        if (delayTicks <= 0) {
            throw new IllegalArgumentException("Delay ticks may not be <= 0");
        }

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }

        final EntityScheduledTask ret = new EntityScheduledTask(plugin, -1, task, retired);

        if (!this.scheduleInternal(ret, delayTicks)) {
            return null;
        }

        if (!plugin.isEnabled()) {
            // handle race condition where plugin is disabled asynchronously
            ret.cancel();
        }

        return ret;
    }

    @Override
    public @Nullable ScheduledTask runAtFixedRate(final Plugin plugin, final Consumer<ScheduledTask> task,
                                                  final Runnable retired, final long initialDelayTicks, final long periodTicks) {
        Objects.requireNonNull(plugin, "Plugin may not be null");
        Objects.requireNonNull(task, "Task may not be null");
        if (initialDelayTicks <= 0) {
            throw new IllegalArgumentException("Initial delay ticks may not be <= 0");
        }
        if (periodTicks <= 0) {
            throw new IllegalArgumentException("Period ticks may not be <= 0");
        }

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }

        final EntityScheduledTask ret = new EntityScheduledTask(plugin, periodTicks, task, retired);

        if (!this.scheduleInternal(ret, initialDelayTicks)) {
            return null;
        }

        if (!plugin.isEnabled()) {
            // handle race condition where plugin is disabled asynchronously
            ret.cancel();
        }

        return ret;
    }

    private boolean scheduleInternal(final EntityScheduledTask ret, final long delay) {
        return this.entity.taskScheduler.schedule(ret, ret, delay);
    }

    private final class EntityScheduledTask implements ScheduledTask, Consumer<Entity> {

        private static final int STATE_IDLE                = 0;
        private static final int STATE_EXECUTING           = 1;
        private static final int STATE_EXECUTING_CANCELLED = 2;
        private static final int STATE_FINISHED            = 3;
        private static final int STATE_CANCELLED           = 4;

        private final Plugin plugin;
        private final long repeatDelay; // in ticks
        private Consumer<ScheduledTask> run;
        private Runnable retired;
        private volatile int state;

        private static final VarHandle STATE_HANDLE = ConcurrentUtil.getVarHandle(EntityScheduledTask.class, "state", int.class);

        private EntityScheduledTask(final Plugin plugin, final long repeatDelay, final Consumer<ScheduledTask> run, final Runnable retired) {
            this.plugin = plugin;
            this.repeatDelay = repeatDelay;
            this.run = run;
            this.retired = retired;
        }

        private final int getStateVolatile() {
            return (int)STATE_HANDLE.get(this);
        }

        private final int compareAndExchangeStateVolatile(final int expect, final int update) {
            return (int)STATE_HANDLE.compareAndExchange(this, expect, update);
        }

        private final void setStateVolatile(final int value) {
            STATE_HANDLE.setVolatile(this, value);
        }

        @Override
        public void accept(final Entity entity) {
            if (!this.plugin.isEnabled()) {
                // don't execute if the plugin is disabled
                this.setStateVolatile(STATE_CANCELLED);
                return;
            }

            final boolean repeating = this.isRepeatingTask();
            if (STATE_IDLE != this.compareAndExchangeStateVolatile(STATE_IDLE, STATE_EXECUTING)) {
                // cancelled
                return;
            }

            final boolean retired = entity.isRemoved();

            try {
                if (!retired) {
                    this.run.accept(this);
                } else {
                    if (this.retired != null) {
                        this.retired.run();
                    }
                }
            } catch (final Throwable throwable) {
                this.plugin.getLogger().log(Level.WARNING, "Entity task for " + this.plugin.getDescription().getFullName() + " generated an exception", throwable);
            } finally {
                boolean reschedule = false;
                 if (!repeating && !retired) {
                    this.setStateVolatile(STATE_FINISHED);
                } else if (retired || !this.plugin.isEnabled()) {
                     this.setStateVolatile(STATE_CANCELLED);
                } else if (STATE_EXECUTING == this.compareAndExchangeStateVolatile(STATE_EXECUTING, STATE_IDLE)) {
                    reschedule = true;
                } // else: cancelled repeating task

                if (!reschedule) {
                    this.run = null;
                    this.retired = null;
                } else {
                    if (!FoliaEntityScheduler.this.scheduleInternal(this, this.repeatDelay)) {
                        // the task itself must have removed the entity, so in this case we need to mark as cancelled
                        this.setStateVolatile(STATE_CANCELLED);
                    }
                }
            }
        }

        @Override
        public Plugin getOwningPlugin() {
            return this.plugin;
        }

        @Override
        public boolean isRepeatingTask() {
            return this.repeatDelay > 0;
        }

        @Override
        public CancelledState cancel() {
            for (int curr = this.getStateVolatile();;) {
                switch (curr) {
                    case STATE_IDLE: {
                        if (STATE_IDLE == (curr = this.compareAndExchangeStateVolatile(STATE_IDLE, STATE_CANCELLED))) {
                            this.state = STATE_CANCELLED;
                            this.run = null;
                            this.retired = null;
                            return CancelledState.CANCELLED_BY_CALLER;
                        }
                        // try again
                        continue;
                    }
                    case STATE_EXECUTING: {
                        if (!this.isRepeatingTask()) {
                            return CancelledState.RUNNING;
                        }
                        if (STATE_EXECUTING == (curr = this.compareAndExchangeStateVolatile(STATE_EXECUTING, STATE_EXECUTING_CANCELLED))) {
                            return CancelledState.NEXT_RUNS_CANCELLED;
                        }
                        // try again
                        continue;
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
                        throw new IllegalStateException("Unknown state: " + curr);
                    }
                }
            }
        }

        @Override
        public ExecutionState getExecutionState() {
            final int state = this.getStateVolatile();
            switch (state) {
                case STATE_IDLE:
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
                    throw new IllegalStateException("Unknown state: " + state);
                }
            }
        }
    }
}
