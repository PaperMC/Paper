package org.bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;


public class TestServer implements InvocationHandler {
    private static interface MethodHandler {
        Object handle(TestServer server, Object[] args);
    }
    private static final Constructor<? extends Server> constructor;
    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();
    static {
        try {
            methods.put(Server.class.getMethod("isPrimaryThread"),
                new MethodHandler() {
                    public Object handle(TestServer server, Object[] args) {
                        return Thread.currentThread().equals(server.creatingThread);
                    }
                });
            constructor = Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private Thread creatingThread = Thread.currentThread();
    private TestServer() {};

    public static Server getInstance() {
        try {
            return constructor.newInstance(new TestServer());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method));
    }
}
