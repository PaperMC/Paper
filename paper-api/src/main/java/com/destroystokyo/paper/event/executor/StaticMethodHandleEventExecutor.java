package com.destroystokyo.paper.event.executor;

import com.destroystokyo.paper.util.SneakyThrow;
import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public class StaticMethodHandleEventExecutor implements EventExecutor {

    private final Class<? extends Event> eventClass;
    private final MethodHandle handle;
    private final Method method;

    public StaticMethodHandleEventExecutor(final Class<? extends Event> eventClass, final Method m) {
        Preconditions.checkArgument(Modifier.isStatic(m.getModifiers()), "Not a static method: %s", m);
        Preconditions.checkArgument(eventClass != null, "eventClass is null");
        this.eventClass = eventClass;
        try {
            m.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(m);
        } catch (final IllegalAccessException e) {
            throw new AssertionError("Unable to set accessible", e);
        }
        this.method = m;
    }

    @Override
    public void execute(final Listener listener, final Event event) throws EventException {
        if (!this.eventClass.isInstance(event)) return;
        try {
            this.handle.invoke(event);
        } catch (final Throwable throwable) {
            SneakyThrow.sneaky(throwable);
        }
    }

    @Override
    public String toString() {
        return "StaticMethodHandleEventExecutor['" + this.method + "']";
    }
}
