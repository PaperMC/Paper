package io.papermc.paper.plugin;

import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Normal
public class SyntheticEventTest {

    @Test
    public void test() {
        PaperTestPlugin paperTestPlugin = new PaperTestPlugin("synthetictest");
        PaperPluginManagerImpl paperPluginManager = new PaperPluginManagerImpl(Bukkit.getServer(), null, null);

        TestEvent event = new TestEvent(false);
        Impl impl = new Impl();

        paperPluginManager.registerEvents(impl, paperTestPlugin);
        paperPluginManager.callEvent(event);

        Assertions.assertEquals(1, impl.callCount);
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
