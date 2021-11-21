package org.bukkit.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Versioning;

public final class DummyServer implements InvocationHandler {
    private static interface MethodHandler {
        Object handle(DummyServer server, Object[] args);
    }
    private static final HashMap<Method, MethodHandler> methods = new HashMap<Method, MethodHandler>();
    static {
        try {
            methods.put(
                    Server.class.getMethod("getItemFactory"),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return CraftItemFactory.instance();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getName"),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return DummyServer.class.getName();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getVersion"),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return DummyServer.class.getPackage().getImplementationVersion();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getBukkitVersion"),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return Versioning.getBukkitVersion();
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getLogger"),
                    new MethodHandler() {
                        final Logger logger = Logger.getLogger(DummyServer.class.getCanonicalName());
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return logger;
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("getUnsafe"),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return CraftMagicNumbers.INSTANCE;
                        }
                    }
                );
            methods.put(
                    Server.class.getMethod("createBlockData", Material.class),
                    new MethodHandler() {
                        final Logger logger = Logger.getLogger(DummyServer.class.getCanonicalName());
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            return CraftBlockData.newData((Material) args[0], null);
                        }
                    }
                );
            methods.put(Server.class.getMethod("getLootTable", NamespacedKey.class),
                    new MethodHandler() {
                        @Override
                        public Object handle(DummyServer server, Object[] args) {
                            NamespacedKey key = (NamespacedKey) args[0];
                            return new CraftLootTable(key, AbstractTestingBase.LOOT_TABLE_REGISTRY.get(CraftNamespacedKey.toMinecraft(key)));
                        }
                    }
                );
            Bukkit.setServer(Proxy.getProxyClass(Server.class.getClassLoader(), Server.class).asSubclass(Server.class).getConstructor(InvocationHandler.class).newInstance(new DummyServer()));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {}

    private DummyServer() {};

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        MethodHandler handler = methods.get(method);
        if (handler != null) {
            return handler.handle(this, args);
        }
        throw new UnsupportedOperationException(String.valueOf(method));
    }
}
