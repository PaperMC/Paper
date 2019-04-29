package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public final class TestWorld implements InvocationHandler {

    private static interface MethodHandler {

        Object handle(TestWorld server, Object[] args);
    }

    private static final Map<Method, MethodHandler> methods;
    public static final World INSTANCE;

    static {
        try {
            ImmutableMap.Builder<Method, MethodHandler> methodMap = ImmutableMap.builder();
            methodMap.put(
                    Object.class.getMethod("equals", Object.class),
                    new MethodHandler() {
                        @Override
                        public Object handle(TestWorld server, Object[] args) {
                            return this == args[0];
                        }
                    }
                );
            methodMap.put(
                    Object.class.getMethod("hashCode"),
                    new MethodHandler() {
                        @Override
                        public Object handle(TestWorld server, Object[] args) {
                            return this.hashCode();
                        }
                    }
                );
            methods = methodMap.build();

            TestWorld world = new TestWorld();
            INSTANCE = Proxy.getProxyClass(World.class.getClassLoader(), World.class).asSubclass(World.class).getConstructor(InvocationHandler.class).newInstance(world);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private TestWorld() {
    }

    public static Server getInstance() {
        return Bukkit.getServer();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method));
    }
}
