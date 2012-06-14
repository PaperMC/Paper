package org.bukkit.plugin;

import static junit.framework.Assert.*;

import org.bukkit.Server;
import org.bukkit.TestServer;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.TestEvent;
import org.junit.Test;

public class PluginManagerTest {
    private class MutableObject {
        volatile Object value = null;
    }

    final Server server = TestServer.getInstance();
    final SimpleCommandMap commandMap = new SimpleCommandMap(server);
    final PluginManager pm = new SimplePluginManager(server, commandMap);
    final MutableObject store = new MutableObject();

    @Test
    public void testAsyncSameThread() {
        final Event event = new TestEvent(true);
        try {
            pm.callEvent(event);
        } catch (IllegalStateException ex) {
            assertEquals(event.getEventName() + " cannot be triggered asynchronously from primary server thread.", ex.getMessage());
            return;
        }
        throw new IllegalStateException("No exception thrown");
    }

    @Test
    public void testSyncSameThread() {
        final Event event = new TestEvent(false);
        pm.callEvent(event);
    }

    @Test
    public void testAsyncLocked() throws InterruptedException {
        final Event event = new TestEvent(true);
        Thread secondThread = new Thread(
            new Runnable() {
                public void run() {
                    try {
                        synchronized (pm) {
                            pm.callEvent(event);
                        }
                    } catch (Throwable ex) {
                        store.value = ex;
                    }
                }});
        secondThread.start();
        secondThread.join();
        assertTrue(store.value instanceof IllegalStateException);
        assertEquals(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.", ((Throwable) store.value).getMessage());
    }

    @Test
    public void testAsyncUnlocked() throws InterruptedException {
        final Event event = new TestEvent(true);
        Thread secondThread = new Thread(
            new Runnable() {
                public void run() {
                    try {
                        pm.callEvent(event);
                    } catch (Throwable ex) {
                        store.value = ex;
                    }
                }});
        secondThread.start();
        secondThread.join();
        if (store.value != null) {
            throw new RuntimeException((Throwable) store.value);
        }
    }

    @Test
    public void testSyncUnlocked() throws InterruptedException {
        final Event event = new TestEvent(false);
        Thread secondThread = new Thread(
            new Runnable() {
                public void run() {
                    try {
                        pm.callEvent(event);
                    } catch (Throwable ex) {
                        store.value = ex;
                    }
                }});
        secondThread.start();
        secondThread.join();
        if (store.value != null) {
            throw new RuntimeException((Throwable) store.value);
        }
    }

    @Test
    public void testSyncLocked() throws InterruptedException {
        final Event event = new TestEvent(false);
        Thread secondThread = new Thread(
            new Runnable() {
                public void run() {
                    try {
                        synchronized (pm) {
                            pm.callEvent(event);
                        }
                    } catch (Throwable ex) {
                        store.value = ex;
                    }
                }});
        secondThread.start();
        secondThread.join();
        if (store.value != null) {
            throw new RuntimeException((Throwable) store.value);
        }
    }
}
