package io.papermc.paper.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public final class EventBootstrap {
    private static final MethodHandle RELINK_TO_GENERIC;
    private static final MethodHandle RECEIVER_HAS_EXACT_CLASS;
    private static final MethodHandle RELINK_TO_INITIAL;
    private static final MethodHandle RELINK_TO_TRUE;
    private static final MethodHandle IS_SAME_CLASS;
    private static final MethodType CALL_EVENT_TYPE = MethodType.methodType(boolean.class);
    private static final MethodHandle TRUE = MethodHandles.constant(boolean.class, true);

    static {
        try {
            RELINK_TO_GENERIC = MethodHandles.lookup().findStatic(
                EventBootstrap.class,
                "relinkToGeneric",
                MethodType.methodType(boolean.class, MutableCallSite.class, MethodHandle.class, Event.class)
            );
            RECEIVER_HAS_EXACT_CLASS = MethodHandles.lookup().findStatic(
                EventBootstrap.class,
                "receiverHasExactClass",
                MethodType.methodType(boolean.class, Event.class, Class.class)
            );
            RELINK_TO_INITIAL = MethodHandles.lookup().findStatic(
                EventBootstrap.class,
                "relinkToInitial",
                MethodType.methodType(boolean.class, MutableCallSite.class, Class.class)
            );
            RELINK_TO_TRUE = MethodHandles.lookup().findStatic(
                EventBootstrap.class,
                "relinkToTrue",
                MethodType.methodType(boolean.class, MutableCallSite.class)
            );
            IS_SAME_CLASS = MethodHandles.lookup().findStatic(
                EventBootstrap.class,
                "isSameClass",
                MethodType.methodType(boolean.class, Class.class, Class.class)
            );
        } catch (final NoSuchMethodException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private EventBootstrap() {
    }

    public static CallSite createForCallEvent(
        final MethodHandles.Lookup lookup,
        final String invocationName,
        final MethodType invocationType,
        final Class<?> staticEventClass
    ) {
        try {
            final Class<? extends Event> typedEventClass = staticEventClass.asSubclass(Event.class);
            final MutableCallSite callSite = new MutableCallSite(invocationType);
            final MethodHandle genericTarget = lookup.findVirtual(typedEventClass, invocationName, CALL_EVENT_TYPE).asType(invocationType);
            final MethodHandle specializedTarget = EventBootstrap.targetForStaticType(lookup, typedEventClass, invocationName, invocationType);
            final MethodHandle receiverCheck = MethodHandles.insertArguments(RECEIVER_HAS_EXACT_CLASS, 1, staticEventClass)
                .asType(MethodType.methodType(boolean.class, invocationType.parameterType(0)));
            final MethodHandle fallbackTarget = MethodHandles.insertArguments(RELINK_TO_GENERIC, 0, callSite, genericTarget)
                .asType(invocationType);

            callSite.setTarget(MethodHandles.guardWithTest(receiverCheck, specializedTarget, fallbackTarget));
            return callSite;
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException("Failed to resolve callEvent target for " + staticEventClass.getName(), ex);
        }
    }

    public static CallSite createForHasListeners(MethodHandles.Lookup lookup, String invocationName, MethodType invocationType) {
        // A hasListeners call site is described by a state machine with three states:
        // 1. Unitialized (never called)
        // 2. Single Class Initialized (called at least once, each call provided the same Class argument)
        // 3. Multi Class Initialized (called at least twice, two calls provided different Class arguments)
        // At 1., we take the passed Class argument (C) and relink the call site to the following pseudocode:
        // boolean hasListeners(Class C') {
        //   if C == C' then C.hasListeners() else relinkToTrue() }
        // When encountering C != C', we relink the call site to always return true.
        final MutableCallSite callSite = new MutableCallSite(MethodType.methodType(boolean.class, Class.class));
        final MethodHandle mh = MethodHandles.insertArguments(RELINK_TO_INITIAL, 0, callSite);
        callSite.setTarget(mh);
        return callSite;
    }

    private static boolean relinkToInitial(final MutableCallSite callSite, Class<?> clazz) throws Throwable {
        final Class<? extends Event> initial = clazz.asSubclass(Event.class);
        MethodHandle invoker = HANDLER_LIST_VALUE.get(initial).hasListeners.dynamicInvoker();
        invoker = MethodHandles.dropArguments(invoker, 0, Class.class);
        final MethodHandle guard = MethodHandles.insertArguments(IS_SAME_CLASS, 0, clazz);
        MethodHandle fallback = MethodHandles.insertArguments(RELINK_TO_TRUE, 0, callSite);
        fallback = MethodHandles.dropArguments(fallback, 0, Class.class);
        callSite.setTarget(MethodHandles.guardWithTest(guard, invoker, fallback));
        MutableCallSite.syncAll(new MutableCallSite[]{callSite});
        return (boolean) invoker.invokeExact(clazz);
    }

    private static boolean relinkToTrue(final MutableCallSite callSite) {
        // Fallback: just always report hasListeners = true for performance reasons
        final MethodHandle alwaysTrue = MethodHandles.dropArguments(TRUE, 0, Class.class);
        MutableCallSite.syncAll(new MutableCallSite[]{callSite});
        callSite.setTarget(alwaysTrue);
        return true;
    }

    private static boolean isSameClass(Class<?> a, Class<?> b) {
        return a == b;
    }

    private static boolean receiverHasExactClass(final Event event, final Class<?> staticEventClass) {
        return event.getClass() == staticEventClass;
    }

    private static boolean relinkToGeneric(final MutableCallSite callSite, final MethodHandle genericTarget, final Event event) throws Throwable {
        System.out.println("relinking due to " + event + " " + genericTarget + " " + callSite.toString());
        new Exception().printStackTrace();
        callSite.setTarget(genericTarget.asType(callSite.type()));
        MutableCallSite.syncAll(new MutableCallSite[]{callSite});
        // the genericTarget expects the static event type of the call site, so invokeExact does not work
        return (boolean) genericTarget.invoke(event);
    }

    private static MethodHandle targetForStaticType(
        final MethodHandles.Lookup lookup,
        final Class<? extends Event> staticEventClass,
        final String invocationName,
        final MethodType invocationType
    ) throws ReflectiveOperationException {
        if (staticEventClass.getMethod(invocationName).getDeclaringClass() != Event.class) {
            System.out.println("static type with override " + staticEventClass);
            new Exception().printStackTrace();
            return lookup.findVirtual(staticEventClass, invocationName, CALL_EVENT_TYPE).asType(invocationType);
        }

        if (staticEventClass == Event.class) {
            System.out.println("static type is event");
            new Exception().printStackTrace();
            return lookup.findVirtual(Event.class, invocationName, CALL_EVENT_TYPE).asType(invocationType);
        }

        return HANDLER_LIST_VALUE.get(staticEventClass).callEvent.dynamicInvoker().asType(invocationType);
    }

    private static final ClassValue<HandlerList> HANDLER_LIST_VALUE = new ClassValue<>() {

        @Override
        protected HandlerList computeValue(@NotNull final Class<?> type) {
            try {
                return findHandlerList(type.asSubclass(Event.class));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        private static HandlerList findHandlerList(final Class<? extends Event> eventClass) throws ReflectiveOperationException {
            for (Class<?> current = eventClass; Event.class.isAssignableFrom(current); current = current.getSuperclass()) {
                try {
                    return (HandlerList) current.getDeclaredMethod("getHandlerList").invoke(null);
                } catch (final NoSuchMethodException ignored) {
                    // continue walking up the event hierarchy
                }
            }
            throw new NoSuchMethodException("No static getHandlerList() found for " + eventClass.getName());
        }
    };
}
