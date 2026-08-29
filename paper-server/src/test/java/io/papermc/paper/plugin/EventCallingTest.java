package io.papermc.paper.plugin;

import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Normal
public class EventCallingTest {

    @Test
    void testZeroThenOneListener() {
        PaperTestPlugin paperTestPlugin = new PaperTestPlugin("listener");
        PaperPluginManagerImpl paperPluginManager = new PaperPluginManagerImpl(Bukkit.getServer(), null, null);
        TestEvent event = new TestEvent(false);
        Listening listening = new Listening();
        assertTrue(event.callEvent());
        assertEquals(0, listening.called);
        paperPluginManager.registerEvents(listening, paperTestPlugin);
        assertTrue(event.callEvent());
        assertEquals(1, listening.called);
    }

    public static class Listening implements Listener {
        private int called = 0;
        @EventHandler
        public void onTest(TestEvent event) {
            called++;
        }
    }
}
