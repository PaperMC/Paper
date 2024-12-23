/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
public class TimedEventExecutor implements EventExecutor {

    private final EventExecutor executor;
    private final Timing timings;

    /**
     * Wraps an event executor and associates a timing handler to it.
     *
     * @param executor Executor to wrap
     * @param plugin Owning plugin
     * @param method EventHandler method
     * @param eventClass Owning class
     */
    public TimedEventExecutor(@NotNull EventExecutor executor, @NotNull Plugin plugin, @Nullable Method method, @NotNull Class<? extends Event> eventClass) {
        this.executor = executor;
        String id;

        if (method == null) {
            if (executor.getClass().getEnclosingClass() != null) { // Oh Skript, how we love you
                method = executor.getClass().getEnclosingMethod();
            }
        }

        if (method != null) {
            id = method.getDeclaringClass().getName();
        } else {
            id = executor.getClass().getName();
        }


        final String eventName = eventClass.getSimpleName();
        boolean verbose = "BlockPhysicsEvent".equals(eventName);
        this.timings = Timings.ofSafe(plugin, (verbose ? "## " : "") +
            "Event: " + id + " (" + eventName + ")");
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if (event.isAsynchronous() || !Timings.timingsEnabled || !Bukkit.isPrimaryThread()) {
            executor.execute(listener, event);
            return;
        }
        try (Timing ignored = timings.startTiming()){
            executor.execute(listener, event);
        }
    }

    @Override
    @NotNull
    public String toString() {
        return "TimedEventExecutor['" + this.executor.toString() + "']";
    }
}
