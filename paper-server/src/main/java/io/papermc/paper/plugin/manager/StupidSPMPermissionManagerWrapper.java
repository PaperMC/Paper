package io.papermc.paper.plugin.manager;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.SimplePluginManager;

import java.util.Map;
import java.util.Set;

/*
This is actually so cursed I hate it.
We need to wrap these in fields as people override the fields, so we need to access them lazily at all times.
// TODO: When SimplePluginManager is GONE remove this and cleanup the PaperPermissionManager to use actual fields.
 */
class StupidSPMPermissionManagerWrapper extends PaperPermissionManager {

    private final SimplePluginManager simplePluginManager;

    public StupidSPMPermissionManagerWrapper(SimplePluginManager simplePluginManager) {
        this.simplePluginManager = simplePluginManager;
    }

    @Override
    public Map<String, Permission> permissions() {
        return this.simplePluginManager.permissions;
    }

    @Override
    public Map<Boolean, Set<Permission>> defaultPerms() {
        return this.simplePluginManager.defaultPerms;
    }

    @Override
    public Map<String, Map<Permissible, Boolean>> permSubs() {
        return this.simplePluginManager.permSubs;
    }

    @Override
    public Map<Boolean, Map<Permissible, Boolean>> defSubs() {
        return this.simplePluginManager.defSubs;
    }
}
