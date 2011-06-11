package org.bukkit.craftbukkit.scheduler;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.scheduler.BukkitTask;

import org.bukkit.plugin.Plugin;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.scheduler.CraftTask;

public class CraftScheduler implements BukkitScheduler, Runnable {

    private static final Logger logger = Logger.getLogger("Minecraft");

    private final CraftServer server;

    private final CraftThreadManager craftThreadManager = new CraftThreadManager();

    private final LinkedList<CraftTask> mainThreadQueue = new LinkedList<CraftTask>();
    private final LinkedList<CraftTask> syncedTasks = new LinkedList<CraftTask>();

    private final TreeMap<CraftTask, Boolean> schedulerQueue = new TreeMap<CraftTask, Boolean>();

    private final Object currentTickSync = new Object();
    private Long currentTick = 0L;

    // This lock locks the mainThreadQueue and the currentTick value
    private final Lock mainThreadLock = new ReentrantLock();
    private final Lock syncedTasksLock = new ReentrantLock();

    public void run() {

        while (true) {
            boolean stop = false;
            long firstTick = -1;
            long currentTick = -1;
            CraftTask first = null;
            do {
                synchronized (schedulerQueue) {
                    first = null;
                    if (!schedulerQueue.isEmpty()) {
                        first = schedulerQueue.firstKey();
                        if (first != null) {
                            currentTick = getCurrentTick();

                            firstTick = first.getExecutionTick();

                            if (currentTick >= firstTick) {
                                schedulerQueue.remove(first);
                                processTask(first);
                                if (first.getPeriod() >= 0) {
                                    first.updateExecution();
                                    schedulerQueue.put(first, first.isSync());
                                }
                            } else {
                                stop = true;
                            }
                        } else {
                            stop = true;
                        }
                    } else {
                        stop = true;
                    }
                }
            } while (!stop);

            long sleepTime = 0;
            if (first == null) {
                sleepTime = 60000L;
            } else {
                currentTick = getCurrentTick();
                sleepTime = (firstTick - currentTick) * 50 + 25;
            }

            if (sleepTime < 50L) {
                sleepTime = 50L;
            } else if (sleepTime > 60000L) {
                sleepTime = 60000L;
            }

            synchronized (schedulerQueue) {
                try {
                    schedulerQueue.wait(sleepTime);
                } catch (InterruptedException ie) {}
            }
        }
    }

    void processTask(CraftTask task) {
        if (task.isSync()) {
            addToMainThreadQueue(task);
        } else {
            craftThreadManager.executeTask(task.getTask(), task.getOwner(), task.getIdNumber());
        }
    }

    public CraftScheduler(CraftServer server) {
        this.server = server;

        Thread t = new Thread(this);
        t.start();

    }

    // If the main thread cannot obtain the lock, it doesn't wait
    public void mainThreadHeartbeat(long currentTick) {
        if (syncedTasksLock.tryLock()) {
            try {
                if (mainThreadLock.tryLock()) {
                    try {
                        this.currentTick = currentTick;
                        while (!mainThreadQueue.isEmpty()) {
                            syncedTasks.addLast(mainThreadQueue.removeFirst());
                        }
                    } finally {
                        mainThreadLock.unlock();
                    }
                }
                long breakTime = System.currentTimeMillis() + 35; // max time spent in loop = 35ms
                while (!syncedTasks.isEmpty() && System.currentTimeMillis() <= breakTime) {
                    CraftTask task = syncedTasks.removeFirst();
                    try {
                        task.getTask().run();
                    } catch (Throwable t) {
                        // Bad plugin!
                        logger.log(Level.WARNING, "Task of '" + task.getOwner().getDescription().getName() + "' generated an exception", t);
                        synchronized (schedulerQueue) {
                            schedulerQueue.remove(task);
                        }
                    }
                }
            } finally {
                syncedTasksLock.unlock();
            }
        }
    }

    long getCurrentTick() {
        mainThreadLock.lock();
        long tempTick = 0;
        try {
            tempTick = currentTick;
        } finally {
            mainThreadLock.unlock();
        }
        return tempTick;
    }

    void addToMainThreadQueue(CraftTask task) {
        mainThreadLock.lock();
        try {
            mainThreadQueue.addLast(task);
        } finally {
            mainThreadLock.unlock();
        }
    }

    void wipeSyncedTasks() {
        syncedTasksLock.lock();
        try {
            syncedTasks.clear();
        } finally {
            syncedTasksLock.unlock();
        }
    }

    void wipeMainThreadQueue() {
        mainThreadLock.lock();
        try {
            mainThreadQueue.clear();
        } finally {
            mainThreadLock.unlock();
        }
    }

    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return scheduleSyncRepeatingTask(plugin, task, delay, -1);
    }

    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return scheduleSyncDelayedTask(plugin, task, 0L);
    }

    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be less than 0");
        }

        CraftTask newTask = new CraftTask(plugin, task, true, getCurrentTick() + delay, period);

        synchronized (schedulerQueue) {
            schedulerQueue.put(newTask, true);
            schedulerQueue.notify();
        }
        return newTask.getIdNumber();
    }

    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return scheduleAsyncRepeatingTask(plugin, task, delay, -1);
    }

    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return scheduleAsyncDelayedTask(plugin, task, 0L);
    }

    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be less than 0");
        }

        CraftTask newTask = new CraftTask(plugin, task, false, getCurrentTick() + delay, period);

        synchronized (schedulerQueue) {
            schedulerQueue.put(newTask, false);
            schedulerQueue.notify();
        }
        return newTask.getIdNumber();
    }

    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        CraftFuture<T> craftFuture = new CraftFuture<T>(this, task);
        synchronized (craftFuture) {
            int taskId = scheduleSyncDelayedTask(plugin, craftFuture);
            craftFuture.setTaskId(taskId);
        }
        return craftFuture;
    }

    public void cancelTask(int taskId) {
        synchronized (schedulerQueue) {
            Iterator<CraftTask> itr = schedulerQueue.keySet().iterator();
            while (itr.hasNext()) {
                CraftTask current = itr.next();
                if (current.getIdNumber() == taskId) {
                    itr.remove();
                }
            }
        }
        craftThreadManager.interruptTask(taskId);
    }

    public void cancelTasks(Plugin plugin) {
        syncedTasksLock.lock();
        try {
            synchronized (schedulerQueue) {
                mainThreadLock.lock();
                try {
                    Iterator<CraftTask> itr = schedulerQueue.keySet().iterator();
                    while (itr.hasNext()) {
                        CraftTask current = itr.next();
                        if (current.getOwner().equals(plugin)) {
                            itr.remove();
                        }
                    }
                    itr = mainThreadQueue.iterator();
                    while (itr.hasNext()) {
                        CraftTask current = itr.next();
                        if (current.getOwner().equals(plugin)) {
                            itr.remove();
                        }
                    }
                    itr = syncedTasks.iterator();
                    while (itr.hasNext()) {
                        CraftTask current = itr.next();
                        if (current.getOwner().equals(plugin)) {
                            itr.remove();
                        }
                    }
                } finally {
                    mainThreadLock.unlock();
                }
            }
        } finally {
            syncedTasksLock.unlock();
        }

        craftThreadManager.interruptTasks(plugin);
    }

    public void cancelAllTasks() {
        synchronized (schedulerQueue) {
            schedulerQueue.clear();
        }
        wipeMainThreadQueue();
        wipeSyncedTasks();

        craftThreadManager.interruptAllTasks();
    }

    public boolean isCurrentlyRunning(int taskId) {
        return craftThreadManager.isAlive(taskId);
    }

    public boolean isQueued(int taskId) {
        synchronized (schedulerQueue) {
            Iterator<CraftTask> itr = schedulerQueue.keySet().iterator();
            while (itr.hasNext()) {
                CraftTask current = itr.next();
                if (current.getIdNumber() == taskId) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<BukkitWorker> getActiveWorkers() {
        synchronized (craftThreadManager.workers) {
            List<BukkitWorker> workerList = new ArrayList<BukkitWorker>(craftThreadManager.workers.size());
            Iterator<CraftWorker> itr = craftThreadManager.workers.iterator();

            while (itr.hasNext()) {
                workerList.add((BukkitWorker) itr.next());
            }
            return workerList;
        }
    }

    public List<BukkitTask> getPendingTasks() {
        List<CraftTask> taskList = null;
        syncedTasksLock.lock();
        try {
            synchronized (schedulerQueue) {
                mainThreadLock.lock();
                try {
                    taskList = new ArrayList<CraftTask>(mainThreadQueue.size() + syncedTasks.size() + schedulerQueue.size());
                    taskList.addAll(mainThreadQueue);
                    taskList.addAll(syncedTasks);
                    taskList.addAll(schedulerQueue.keySet());
                } finally {
                    mainThreadLock.unlock();
                }
            }
        } finally {
            syncedTasksLock.unlock();
        }
        List<BukkitTask> newTaskList = new ArrayList<BukkitTask>(taskList.size());

        for (CraftTask craftTask : taskList) {
            newTaskList.add((BukkitTask) craftTask);
        }
        return newTaskList;
    }

}
