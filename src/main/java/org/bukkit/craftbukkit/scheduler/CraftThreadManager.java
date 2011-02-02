package org.bukkit.craftbukkit.scheduler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;

import org.bukkit.plugin.Plugin;

public class CraftThreadManager {

    final HashSet<CraftWorker> workers = new HashSet<CraftWorker>();

    void executeTask(Runnable task, Plugin owner, int taskId) {

        CraftWorker craftWorker = new CraftWorker(this, task, owner, taskId);
        synchronized (workers) {
            workers.add(craftWorker);
        }

    }

    void interruptTask(int taskId) {
        synchronized (workers) {
            Iterator<CraftWorker> itr = workers.iterator();
            while (itr.hasNext()) {
                CraftWorker craftWorker = itr.next();
                if (craftWorker.getTaskId() == taskId) {
                    craftWorker.interrupt();
                }
            }
        }
    }

    void interruptTask(Plugin owner) {
        synchronized (workers) {
            Iterator<CraftWorker> itr = workers.iterator();
            while (itr.hasNext()) {
                CraftWorker craftWorker = itr.next();
                if (craftWorker.getOwner().equals(owner)) {
                    craftWorker.interrupt();
                }
            }
        }
    }

    void interruptAllTasks() {
        synchronized (workers) {
            Iterator<CraftWorker> itr = workers.iterator();
            while (itr.hasNext()) {
                CraftWorker craftWorker = itr.next();
                craftWorker.interrupt();
            }
        }
    }
}
