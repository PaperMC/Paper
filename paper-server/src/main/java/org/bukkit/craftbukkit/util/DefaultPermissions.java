
package org.bukkit.craftbukkit.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public final class DefaultPermissions {
    private DefaultPermissions() {}

    private static void registerPermission(PluginManager manager, Permission perm) {
        try {
            manager.addPermission(perm);
        } catch (IllegalArgumentException ex) {}
    }

    private static void registerWhitelist(PluginManager manager) {
        Map<String, Boolean> whitelist = new HashMap<String, Boolean>();
        whitelist.put("craftbukkit.command.whitelist.remove", true);
        whitelist.put("craftbukkit.command.whitelist.reload", true);
        whitelist.put("craftbukkit.command.whitelist.enable", true);
        whitelist.put("craftbukkit.command.whitelist.disable", true);
        whitelist.put("craftbukkit.command.whitelist.list", true);

        registerPermission(manager, new Permission("craftbukkit.command.whitelist.add", "Allows the user to add a player to the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist.remove", "Allows the user to remove a player from the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist.reload", "Allows the user to reload the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist.enable", "Allows the user to enable the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist.disable", "Allows the user to disable the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist.list", "Allows the user to list all the users on the server whitelist"));
        registerPermission(manager, new Permission("craftbukkit.command.whitelist", "Allows the user to modify the server whitelist", PermissionDefault.OP, whitelist));
    }

    private static void registerBan(PluginManager manager) {
        Map<String, Boolean> ban = new HashMap<String, Boolean>();
        ban.put("craftbukkit.command.ban.player", true);
        ban.put("craftbukkit.command.ban.ip", true);

        registerPermission(manager, new Permission("craftbukkit.command.ban.player", "Allows the user to ban players"));
        registerPermission(manager, new Permission("craftbukkit.command.ban.ip", "Allows the user to ban IP addresses"));
        registerPermission(manager, new Permission("craftbukkit.command.ban", "Allows the user to ban people", PermissionDefault.OP, ban));
    }

    private static void registerUnban(PluginManager manager) {
        Map<String, Boolean> unban = new HashMap<String, Boolean>();
        unban.put("craftbukkit.command.unban.player", true);
        unban.put("craftbukkit.command.unban.ip", true);
        
        registerPermission(manager, new Permission("craftbukkit.command.unban.player", "Allows the user to unban players"));
        registerPermission(manager, new Permission("craftbukkit.command.unban.ip", "Allows the user to unban IP addresses"));
        registerPermission(manager, new Permission("craftbukkit.command.unban", "Allows the user to unban people", PermissionDefault.OP, unban));
    }

    private static void registerOp(PluginManager manager) {
        Map<String, Boolean> op = new HashMap<String, Boolean>();
        op.put("craftbukkit.command.op.give", true);
        op.put("craftbukkit.command.op.take", true);
        
        registerPermission(manager, new Permission("craftbukkit.command.op.give", "Allows the user to give a player operator status"));
        registerPermission(manager, new Permission("craftbukkit.command.op.take", "Allows the user to take a players operator status"));
        registerPermission(manager, new Permission("craftbukkit.command.op", "Allows the user to change operators", PermissionDefault.OP, op));
    }

    private static void registerSave(PluginManager manager) {
        Map<String, Boolean> save = new HashMap<String, Boolean>();
        save.put("craftbukkit.command.save.enable", true);
        save.put("craftbukkit.command.save.disable", true);
        save.put("craftbukkit.command.save.perform", true);

        registerPermission(manager, new Permission("craftbukkit.command.save.enable", "Allows the user to enable automatic saving"));
        registerPermission(manager, new Permission("craftbukkit.command.save.disable", "Allows the user to disable automatic saving"));
        registerPermission(manager, new Permission("craftbukkit.command.save.perform", "Allows the user to perform a manual save"));
        registerPermission(manager, new Permission("craftbukkit.command.save", "Allows the user to save the worlds", PermissionDefault.OP, save));
    }

    private static void registerTime(PluginManager manager) {
        Map<String, Boolean> time = new HashMap<String, Boolean>();
        time.put("craftbukkit.command.time.add", true);
        time.put("craftbukkit.command.time.set", true);

        registerPermission(manager, new Permission("craftbukkit.command.time.add", "Allows the user to fast-forward time"));
        registerPermission(manager, new Permission("craftbukkit.command.time.set", "Allows the user to change the time"));
        registerPermission(manager, new Permission("craftbukkit.command.time", "Allows the user to alter the time", PermissionDefault.OP, time));
    }

    public static void registerCorePermissions(PluginManager manager) {
        registerWhitelist(manager);
        registerBan(manager);
        registerUnban(manager);
        registerOp(manager);
        registerSave(manager);
        registerTime(manager);

        registerPermission(manager, new Permission("craftbukkit.command.kill", "Allows the user to commit suicide", PermissionDefault.TRUE));
        registerPermission(manager, new Permission("craftbukkit.command.me", "Allows the user to perform a chat action", PermissionDefault.TRUE));
        registerPermission(manager, new Permission("craftbukkit.command.tell", "Allows the user to privately message another player", PermissionDefault.TRUE));
        registerPermission(manager, new Permission("craftbukkit.command.say", "Allows the user to talk as the console", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.give", "Allows the user to give items to players", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.teleport", "Allows the user to teleport players", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.kick", "Allows the user to kick players", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.stop", "Allows the user to stop the server", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.list", "Allows the user to list all online players", PermissionDefault.OP));
        registerPermission(manager, new Permission("craftbukkit.command.help", "Allows the user to view the vanilla help menu", PermissionDefault.OP));
    }

}
