package org.bukkit.craftbukkit.util.permissions;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public final class DefaultPermissions {
    private static final String ROOT = "craftbukkit";
    private static final String PREFIX = ROOT + ".";

    private DefaultPermissions() {}

    public static Permission registerPermission(Permission perm) {
        try {
            Bukkit.getPluginManager().addPermission(perm);
            return perm;
        } catch (IllegalArgumentException ex) {
            return Bukkit.getPluginManager().getPermission(perm.getName());
        }
    }

    public static Permission registerPermission(Permission perm, Permission parent) {
        parent.getChildren().put(perm.getName(), true);
        return registerPermission(perm);
    }

    public static Permission registerPermission(String name, String desc) {
        Permission perm = registerPermission(new Permission(name, desc));
        return perm;
    }

    public static Permission registerPermission(String name, String desc, Permission parent) {
        Permission perm = registerPermission(name, desc);
        parent.getChildren().put(perm.getName(), true);
        return perm;
    }

    public static Permission registerPermission(String name, String desc, PermissionDefault def) {
        Permission perm = registerPermission(new Permission(name, desc, def));
        return perm;
    }

    public static Permission registerPermission(String name, String desc, PermissionDefault def, Permission parent) {
        Permission perm = registerPermission(name, desc, def);
        parent.getChildren().put(perm.getName(), true);
        return perm;
    }

    public static Permission registerPermission(String name, String desc, PermissionDefault def, Map<String, Boolean> children) {
        Permission perm = registerPermission(new Permission(name, desc, def, children));
        return perm;
    }

    public static Permission registerPermission(String name, String desc, PermissionDefault def, Map<String, Boolean> children, Permission parent) {
        Permission perm = registerPermission(name, desc, def, children);
        parent.getChildren().put(perm.getName(), true);
        return perm;
    }

    public static void registerCorePermissions() {
        Permission parent = registerPermission(ROOT, "Gives the user the ability to use all Craftbukkit utilities and commands");

        CommandPermissions.registerPermissions(parent);

        parent.recalculatePermissibles();
    }
}
