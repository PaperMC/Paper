package com.destroystokyo.paper.event.executor;

import com.destroystokyo.paper.util.SneakyThrow;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
@NullMarked
public class MethodHandleEventExecutor implements EventExecutor {

    private final Class<? extends Event> eventClass;
    private final MethodHandle handle;

    public MethodHandleEventExecutor(final Class<? extends Event> eventClass, final MethodHandle handle) {
        this.eventClass = eventClass;
        this.handle = handle;
    }

    public MethodHandleEventExecutor(final Class<? extends Event> eventClass, final Method m) {
        this.eventClass = eventClass;
        try {
            m.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(m);
        } catch (final IllegalAccessException e) {
            throw new AssertionError("Unable to set accessible", e);
        }
    }

    @Override
    public void execute(final Listener listener, final Event event) throws EventException {
        if (!this.eventClass.isInstance(event)) return;
        try {
            this.handle.invoke(listener, event);
        } catch (final Throwable t) {
            SneakyThrow.sneaky(t);
        }
    }
}
