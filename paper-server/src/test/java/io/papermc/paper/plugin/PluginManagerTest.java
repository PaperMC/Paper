package io.papermc.paper.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@Normal
public class PluginManagerTest {

    private static final PluginManager pm = Bukkit.getPluginManager();

    @Test
    public void testSyncSameThread() {
        final Event event = new TestEvent(false);
        pm.callEvent(event);
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

    @Test
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

    @AfterEach
    public void tearDown() {
        pm.clearPlugins();
        assertThat(pm.getPermissions(), is(empty()));
    }
}
