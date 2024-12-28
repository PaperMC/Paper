package io.papermc.paper.event.executor;

import com.destroystokyo.paper.util.SneakyThrow;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * This class is designed to be used as hidden class template.
 * Initializing the class directly will fail due to missing {@code classData}.
 * Instead, {@link java.lang.invoke.MethodHandles.Lookup#defineHiddenClassWithClassData(byte[], Object, boolean, MethodHandles.Lookup.ClassOption...)}
 * must be used, with the {@code classData} object being a list consisting of two elements:
 * <ol>
 *     <li>A {@link Method} representing the event handler method</li>
 *     <li>A {@link Class} representing the event type</li>
 * </ol>
 * The method must take {@link Event} or a subtype of it as its single parameter.
 * If the method is non-static, it also needs to reside in a class implementing {@link Listener}.
 */
@SuppressWarnings("unused")
@ApiStatus.Internal
@NullMarked
class MethodHandleEventExecutorTemplate implements EventExecutor {
    private static final Method METHOD;
    private static final MethodHandle HANDLE;
    private static final Class<? extends Event> EVENT_CLASS;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        final EventExecutorFactory.ClassData classData = EventExecutorFactory.classData(lookup);
        METHOD = classData.method();
        HANDLE = classData.methodHandle();
        EVENT_CLASS = classData.eventClass();
    }

    @Override
    public void execute(final Listener listener, final Event event) throws EventException {
        if (!EVENT_CLASS.isInstance(event)) return;
        try {
            HANDLE.invokeExact(listener, event);
        } catch (Throwable t) {
            SneakyThrow.sneaky(t);
        }
    }

    @Override
    public String toString() {
        return "MethodHandleEventExecutorTemplate['" + METHOD + "']";
    }
}
