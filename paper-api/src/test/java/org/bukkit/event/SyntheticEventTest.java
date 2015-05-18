package org.bukkit.event;

import org.bukkit.TestServer;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.TestPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.Assert;
import org.junit.Test;

public class SyntheticEventTest {
    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        final JavaPluginLoader loader = new JavaPluginLoader(TestServer.getInstance());
        TestPlugin plugin = new TestPlugin(getClass().getName()) {
            @Override
            public PluginLoader getPluginLoader() {
                return loader;
            }
        };
        SimplePluginManager pluginManager = new SimplePluginManager(TestServer.getInstance(), null);

        TestEvent event = new TestEvent(false);
        Impl impl = new Impl();

        pluginManager.registerEvents(impl, plugin);
        pluginManager.callEvent(event);

        Assert.assertEquals(1, impl.callCount);
    }

    public static abstract class Base<E extends Event> implements Listener {
        int callCount = 0;

        public void accept(E evt) {
            System.out.println("Invk " + evt);
            callCount++;
        }
    }

    public static class Impl extends Base<TestEvent> {
        @Override
        @EventHandler
        public void accept(TestEvent evt) {
            super.accept(evt);
        }
    }
}
