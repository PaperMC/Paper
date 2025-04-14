package io.papermc.paper.event.executor;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.io.InputStream;
import java.lang.constant.ConstantDescs;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
@NullMarked
public final class EventExecutorFactory {
    private static final byte[] TEMPLATE_CLASS_BYTES;

    static {
        try (final InputStream is = EventExecutorFactory.class.getResourceAsStream("MethodHandleEventExecutorTemplate.class")) {
            TEMPLATE_CLASS_BYTES = Objects.requireNonNull(is, "template class is missing").readAllBytes();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private EventExecutorFactory() {

    }

    /**
     * {@return an {@link EventExecutor} implemented by a hidden class calling a method handle}
     *
     * @param method     the method to be invoked by the created event executor
     * @param eventClass the class of the event to handle
     */
    public static EventExecutor create(final Method method, final Class<? extends Event> eventClass) {
        final List<?> classData = List.of(method, eventClass);
        try {
            final MethodHandles.Lookup newClass = MethodHandles.lookup().defineHiddenClassWithClassData(TEMPLATE_CLASS_BYTES, classData, true);
            return newClass.lookupClass().asSubclass(EventExecutor.class).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    record ClassData(Method method, MethodHandle methodHandle, Class<? extends Event> eventClass) {

    }

    /**
     * Extracts the class data and creates an adjusted MethodHandle directly usable by the lookup class.
     * The logic is kept here to minimize memory usage per created class.
     */
    static ClassData classData(final MethodHandles.Lookup lookup) {
        try {
            final Method method = MethodHandles.classDataAt(lookup, ConstantDescs.DEFAULT_NAME, Method.class, 0);
            MethodHandle mh = lookup.unreflect(method);
            if (Modifier.isStatic(method.getModifiers())) {
                mh = MethodHandles.dropArguments(mh, 0, Listener.class);
            }
            mh = mh.asType(MethodType.methodType(void.class, Listener.class, Event.class));
            final Class<?> eventClass = MethodHandles.classDataAt(lookup, ConstantDescs.DEFAULT_NAME, Class.class, 1);
            return new ClassData(method, mh, eventClass.asSubclass(Event.class));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
