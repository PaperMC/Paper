package org.bukkit.event;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.TestPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class SyntheticEventTest extends AbstractTestingBase {
    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        final JavaPluginLoader loader = new JavaPluginLoader(Bukkit.getServer());
        TestPlugin plugin = new TestPlugin(getClass().getName()) {
            @Override
            public PluginLoader getPluginLoader() {
                return loader;
            }
        };
        SimplePluginManager pluginManager = new SimplePluginManager(Bukkit.getServer(), null);

        TestEvent event = new TestEvent(false);
        Impl impl = new Impl();

        pluginManager.registerEvents(impl, plugin);
        pluginManager.callEvent(event);

        assertEquals(1, impl.callCount);
    }

    public abstract static class Base<E extends Event> implements Listener {
        int callCount = 0;

        public void accept(E evt) {
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
