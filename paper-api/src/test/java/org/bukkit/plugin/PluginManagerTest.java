package org.bukkit.plugin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.bukkit.TestServer;
import org.bukkit.event.Event;
import org.bukkit.event.TestEvent;
import org.bukkit.permissions.Permission;

import org.junit.After;
import org.junit.Test;

public class PluginManagerTest {
    private class MutableObject {
        volatile Object value = null;
    }

    private static final PluginManager pm = TestServer.getInstance().getPluginManager();

    private final MutableObject store = new MutableObject();

    @Test
    public void testAsyncSameThread() {
        final Event event = new TestEvent(true);
        try {
            pm.callEvent(event);
        } catch (IllegalStateException ex) {
            assertThat(event.getEventName() + " cannot be triggered asynchronously from primary server thread.", is(ex.getMessage()));
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
                }
            }
        );
        secondThread.start();
        secondThread.join();
        assertThat(store.value, is(instanceOf(IllegalStateException.class)));
        assertThat(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.", is(((Throwable) store.value).getMessage()));
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
                }
            }
        );
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
                }
            }
        );
        secondThread.start();
        secondThread.join();
        if (store.value != null) {
            throw new RuntimeException((Throwable) store.value);
        }
    }

    @Test
    public void testRemovePermissionByNameLower() {
        this.testRemovePermissionByName("lower");
    }

    @Test
    public void testRemovePermissionByNameUpper() {
        this.testRemovePermissionByName("UPPER");
    }

    @Test
    public void testRemovePermissionByNameCamel() {
        this.testRemovePermissionByName("CaMeL");
    }

    public void testRemovePermissionByPermissionLower() {
        this.testRemovePermissionByPermission("lower");
    }

    @Test
    public void testRemovePermissionByPermissionUpper() {
        this.testRemovePermissionByPermission("UPPER");
    }

    @Test
    public void testRemovePermissionByPermissionCamel() {
        this.testRemovePermissionByPermission("CaMeL");
    }

    private void testRemovePermissionByName(final String name) {
        final Permission perm = new Permission(name);
        pm.addPermission(perm);
        assertThat("Permission \"" + name + "\" was not added", pm.getPermission(name), is(perm));
        pm.removePermission(name);
        assertThat("Permission \"" + name + "\" was not removed", pm.getPermission(name), is(nullValue()));
    }

    private void testRemovePermissionByPermission(final String name) {
        final Permission perm = new Permission(name);
        pm.addPermission(perm);
        assertThat("Permission \"" + name + "\" was not added", pm.getPermission(name), is(perm));
        pm.removePermission(perm);
        assertThat("Permission \"" + name + "\" was not removed", pm.getPermission(name), is(nullValue()));
    }

    @After
    public void tearDown() {
        pm.clearPlugins();
        assertThat(pm.getPermissions(), is(empty()));
    }
}
