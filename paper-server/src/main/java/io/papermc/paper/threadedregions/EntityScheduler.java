package io.papermc.paper.threadedregions;

import ca.spottedleaf.moonrise.common.list.ReferenceList;
import ca.spottedleaf.moonrise.common.util.TickThread;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;

/**
 * An entity can move between worlds with an arbitrary tick delay, be temporarily removed
 * for players (i.e end credits), be partially removed from world state (i.e inactive but not removed),
 * teleport between ticking regions, teleport between worlds (which will change the underlying Entity object
 * for non-players), and even be removed entirely from the server. The uncertainty of an entity's state can make
 * it difficult to schedule tasks without worrying about undefined behaviors resulting from any of the states listed
 * previously.
 *
 * <p>
 * This class is designed to eliminate those states by providing an interface to run tasks only when an entity
 * is contained in a world, on the owning thread for the region, and by providing the current Entity object.
 * The scheduler also allows a task to provide a callback, the "retired" callback, that will be invoked
 * if the entity is removed before a task that was scheduled could be executed. The scheduler is also
 * completely thread-safe, allowing tasks to be scheduled from any thread context. The scheduler also indicates
 * properly whether a task was scheduled successfully (i.e scheduler not retired), thus the code scheduling any task
 * knows whether the given callbacks will be invoked eventually or not - which may be critical for off-thread
 * contexts.
 * </p>
 */
public final class EntityScheduler {

    /**
     * The Entity. Note that it is the CraftEntity, since only that class properly tracks world transfers.
     */
    public final CraftEntity entity;

    private static final record ScheduledTask(Consumer<? extends Entity> run, Consumer<? extends Entity> retired) {}

    private long tickCount = 0L;
    private static final long RETIRED_TICK_COUNT = -1L;
    private final Object stateLock = new Object();
    private final Long2ObjectOpenHashMap<List<ScheduledTask>> oneTimeDelayed = new Long2ObjectOpenHashMap<>();
    private EntitySchedulerTickList scheduledList;
    private boolean insideScheduledList;

    private final ArrayDeque<ScheduledTask> currentlyExecuting = new ArrayDeque<>();

    public EntityScheduler(final CraftEntity entity) {
        this.entity = Objects.requireNonNull(entity);
    }

    // must own state lock
    private boolean hasTasks() {
        return !this.currentlyExecuting.isEmpty() || !this.oneTimeDelayed.isEmpty();
    }

    public void registerTo(final EntitySchedulerTickList newTickList) {
        synchronized (this.stateLock) {
            final EntitySchedulerTickList prevList = this.scheduledList;
            if (prevList == newTickList) {
                return;
            }
            this.scheduledList = newTickList;

            // make sure tasks scheduled before registration can be ticked
            if (prevList == null && this.hasTasks()) {
                this.insideScheduledList = true;
            }

            // transfer to new list
            if (this.insideScheduledList) {
                if (prevList != null) {
                    prevList.remove(this);
                }
                if (newTickList != null) {
                    newTickList.add(this);
                } else {
                    // retired
                    this.insideScheduledList = false;
                }
            }
        }
    }

    /**
     * Returns whether this scheduler is retired.
     *
     * <p>
     * Note: This should only be invoked on the owning thread for the entity.
     * </p>
     * @return whether this scheduler is retired.
     */
    public boolean isRetired() {
        return this.tickCount == RETIRED_TICK_COUNT;
    }

    /**
     * Retires the scheduler, preventing new tasks from being scheduled and invoking the retired callback
     * on all currently scheduled tasks.
     *
     * <p>
     * Note: This should only be invoked after synchronously removing the entity from the world.
     * </p>
     *
     * @throws IllegalStateException If the scheduler is already retired.
     */
    public void retire() {
        synchronized (this.stateLock) {
            if (this.tickCount == RETIRED_TICK_COUNT) {
                throw new IllegalStateException("Already retired");
            }
            this.tickCount = RETIRED_TICK_COUNT;
            this.registerTo(null);
        }

        final Entity thisEntity = this.entity.getHandleRaw();

        // correctly handle and order retiring while running executeTick
        for (int i = 0, len = this.currentlyExecuting.size(); i < len; ++i) {
            final ScheduledTask task = this.currentlyExecuting.pollFirst();
            final Consumer<Entity> retireTask = (Consumer<Entity>)task.retired;
            if (retireTask == null) {
                continue;
            }

            retireTask.accept(thisEntity);
        }

        for (final List<ScheduledTask> tasks : this.oneTimeDelayed.values()) {
            for (int i = 0, len = tasks.size(); i < len; ++i) {
                final ScheduledTask task = tasks.get(i);
                final Consumer<Entity> retireTask = (Consumer<Entity>)task.retired;
                if (retireTask == null) {
                    continue;
                }

                retireTask.accept(thisEntity);
            }
        }
    }

    /**
     * Schedules a task with the given delay. If the task failed to schedule because the scheduler is retired (entity
     * removed), then returns {@code false}. Otherwise, either the run callback will be invoked after the specified delay,
     * or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity, remove
     * other entities, load chunks, load worlds, modify ticket levels, etc.
     *
     * <p>
     * It is guaranteed that the run and retired callback are invoked on the region which owns the entity.
     * </p>
     * <p>
     * The run and retired callback take an Entity parameter representing the current object entity that the scheduler
     * is tied to. Since the scheduler is transferred when an entity changes dimensions, it is possible the entity parameter
     * is not the same when the task was first scheduled. Thus, <b>only</b> the parameter provided should be used.
     * </p>
     * @param run The callback to run after the specified delay, may not be null.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param delay The delay in ticks before the run callback is invoked. Any value less-than 1 is treated as 1.
     * @return {@code true} if the task was scheduled, which means that either the run function or the retired function
     *         will be invoked (but never both), or {@code false} indicating neither the run nor retired function will be invoked
     *         since the scheduler has been retired.
     */
    public boolean schedule(final Consumer<? extends Entity> run, final Consumer<? extends Entity> retired, final long delay) {
        Objects.requireNonNull(run, "Run task may not be null");

        final ScheduledTask task = new ScheduledTask(run, retired);
        synchronized (this.stateLock) {
            if (this.tickCount == RETIRED_TICK_COUNT) {
                return false;
            }
            this.oneTimeDelayed.computeIfAbsent(this.tickCount + Math.max(1L, delay), (final long keyInMap) -> {
                return new ArrayList<>();
            }).add(task);

            if (!this.insideScheduledList && this.scheduledList != null) {
                this.scheduledList.add(this);
                this.insideScheduledList = true;
            }
        }

        return true;
    }

    /**
     * Executes a tick for the scheduler.
     *
     * @throws IllegalStateException If the scheduler is retired.
     */
    public void executeTick() {
        final Entity thisEntity = this.entity.getHandleRaw();

        TickThread.ensureTickThread(thisEntity, "May not tick entity scheduler asynchronously");
        final List<ScheduledTask> toRun;
        synchronized (this.stateLock) {
            if (this.tickCount == RETIRED_TICK_COUNT) {
                throw new IllegalStateException("Ticking retired scheduler");
            }
            ++this.tickCount;

            if (this.scheduledList != null && !this.hasTasks()) {
                this.scheduledList.remove(this);
                this.insideScheduledList = false;
            }

            if (this.oneTimeDelayed.isEmpty()) {
                toRun = null;
            } else {
                toRun = this.oneTimeDelayed.remove(this.tickCount);
            }
        }

        if (toRun != null) {
            for (int i = 0, len = toRun.size(); i < len; ++i) {
                this.currentlyExecuting.addLast(toRun.get(i));
            }
        }

        // Note: It is allowed for the tasks executed to retire the entity in a given task.
        for (int i = 0, len = this.currentlyExecuting.size(); i < len; ++i) {
            if (!TickThread.isTickThreadFor(thisEntity)) {
                // tp has been queued sync by one of the tasks
                // in this case, we need to delay the tasks for next tick
                break;
            }
            final ScheduledTask task = this.currentlyExecuting.pollFirst();

            if (this.tickCount != RETIRED_TICK_COUNT) {
                ((Consumer<Entity>)task.run).accept(thisEntity);
            } else {
                // retired synchronously
                // note: here task is null
                break;
            }
        }
    }

    public static final class EntitySchedulerTickList {

        private static final EntityScheduler[] ENTITY_SCHEDULER_ARRAY = new EntityScheduler[0];

        private final ReferenceList<EntityScheduler> entitySchedulers = new ReferenceList<>(ENTITY_SCHEDULER_ARRAY);

        public boolean add(final EntityScheduler scheduler) {
            synchronized (this) {
                return this.entitySchedulers.add(scheduler);
            }
        }

        public void remove(final EntityScheduler scheduler) {
            synchronized (this) {
                this.entitySchedulers.remove(scheduler);
            }
        }

        public EntityScheduler[] getAllSchedulers() {
            EntityScheduler[] ret = new EntityScheduler[this.entitySchedulers.size()];
            synchronized (this) {
                if (ret.length != this.entitySchedulers.size()) {
                    ret = new EntityScheduler[this.entitySchedulers.size()];
                }
                System.arraycopy(this.entitySchedulers.getRawDataUnchecked(), 0, ret, 0, this.entitySchedulers.size());
                return ret;
            }
        }
    }
}
