/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.bukkit.craftbukkit.scheduler;

import com.destroystokyo.paper.ServerSchedulerReportingWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CraftAsyncScheduler extends CraftScheduler {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4, Integer.MAX_VALUE,30L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %1$d").build());
    private final Executor management = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
            .setNameFormat("Craft Async Scheduler Management Thread").build());

    CraftAsyncScheduler() {
        super(true);
        this.executor.allowCoreThreadTimeOut(true);
        this.executor.prestartAllCoreThreads();
    }


    @Override
    public void mainThreadHeartbeat() {
        final int tick = now();
        this.management.execute(() -> this.runTasks(tick));
    }

    private void runTasks(final int currentTick) {
        this.parsePending();
        this.pending.dropAll(task -> {
            if (this.executeTask(task)) {
                final long period = task.getPeriod();
                if (period > 0) {
                    task.setNextRun(currentTick + period);
                    this.external.push(task);
                }
            }
        });
    }

    private boolean executeTask(final CraftTask task) {
        if (task.isInternal()) {
            task.run();
        } else if (task.getState() >= CraftTask.NO_REPEATING) {
            this.executor.execute(new ServerSchedulerReportingWrapper(task));
            return true;
        }
        return false;
    }
}
