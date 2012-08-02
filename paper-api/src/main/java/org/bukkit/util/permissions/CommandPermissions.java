package org.bukkit.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class CommandPermissions {
    private static final String ROOT = "bukkit.command";
    private static final String PREFIX = ROOT + ".";

    private CommandPermissions() {}

    private static Permission registerWhitelist(Permission parent) {
        Permission whitelist = DefaultPermissions.registerPermission(PREFIX + "whitelist", "Allows the user to modify the server whitelist", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "whitelist.add", "Allows the user to add a player to the server whitelist", whitelist);
        DefaultPermissions.registerPermission(PREFIX + "whitelist.remove", "Allows the user to remove a player from the server whitelist", whitelist);
        DefaultPermissions.registerPermission(PREFIX + "whitelist.reload", "Allows the user to reload the server whitelist", whitelist);
        DefaultPermissions.registerPermission(PREFIX + "whitelist.enable", "Allows the user to enable the server whitelist", whitelist);
        DefaultPermissions.registerPermission(PREFIX + "whitelist.disable", "Allows the user to disable the server whitelist", whitelist);
        DefaultPermissions.registerPermission(PREFIX + "whitelist.list", "Allows the user to list all the users on the server whitelist", whitelist);

        whitelist.recalculatePermissibles();

        return whitelist;
    }

    private static Permission registerBan(Permission parent) {
        Permission ban = DefaultPermissions.registerPermission(PREFIX + "ban", "Allows the user to ban people", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "ban.player", "Allows the user to ban players", ban);
        DefaultPermissions.registerPermission(PREFIX + "ban.ip", "Allows the user to ban IP addresses", ban);

        ban.recalculatePermissibles();

        return ban;
    }

    private static Permission registerUnban(Permission parent) {
        Permission unban = DefaultPermissions.registerPermission(PREFIX + "unban", "Allows the user to unban people", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "unban.player", "Allows the user to unban players", unban);
        DefaultPermissions.registerPermission(PREFIX + "unban.ip", "Allows the user to unban IP addresses", unban);

        unban.recalculatePermissibles();

        return unban;
    }

    private static Permission registerOp(Permission parent) {
        Permission op = DefaultPermissions.registerPermission(PREFIX + "op", "Allows the user to change operators", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "op.give", "Allows the user to give a player operator status", op);
        DefaultPermissions.registerPermission(PREFIX + "op.take", "Allows the user to take a players operator status", op);

        op.recalculatePermissibles();

        return op;
    }

    private static Permission registerSave(Permission parent) {
        Permission save = DefaultPermissions.registerPermission(PREFIX + "save", "Allows the user to save the worlds", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "save.enable", "Allows the user to enable automatic saving", save);
        DefaultPermissions.registerPermission(PREFIX + "save.disable", "Allows the user to disable automatic saving", save);
        DefaultPermissions.registerPermission(PREFIX + "save.perform", "Allows the user to perform a manual save", save);

        save.recalculatePermissibles();

        return save;
    }

    private static Permission registerTime(Permission parent) {
        Permission time = DefaultPermissions.registerPermission(PREFIX + "time", "Allows the user to alter the time", PermissionDefault.OP, parent);

        DefaultPermissions.registerPermission(PREFIX + "time.add", "Allows the user to fast-forward time", time);
        DefaultPermissions.registerPermission(PREFIX + "time.set", "Allows the user to change the time", time);

        time.recalculatePermissibles();

        return time;
    }

    public static Permission registerPermissions(Permission parent) {
        Permission commands = DefaultPermissions.registerPermission(ROOT, "Gives the user the ability to use all CraftBukkit commands", parent);

        registerWhitelist(commands);
        registerBan(commands);
        registerUnban(commands);
        registerOp(commands);
        registerSave(commands);
        registerTime(commands);

        DefaultPermissions.registerPermission(PREFIX + "kill", "Allows the user to commit suicide", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "me", "Allows the user to perform a chat action", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "tell", "Allows the user to privately message another player", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "say", "Allows the user to talk as the console", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "give", "Allows the user to give items to players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "teleport", "Allows the user to teleport players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "kick", "Allows the user to kick players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "stop", "Allows the user to stop the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "list", "Allows the user to list all online players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "help", "Allows the user to view the vanilla help menu", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "plugins", "Allows the user to view the list of plugins running on this server", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "reload", "Allows the user to reload the server settings", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "version", "Allows the user to view the version of the server", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "gamemode", "Allows the user to change the gamemode of another player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "xp", "Allows the user to give themselves or others arbitrary values of experience", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "toggledownfall", "Allows the user to toggle rain on/off for a given world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "defaultgamemode", "Allows the user to change the default gamemode of the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "seed", "Allows the user to view the seed of the world", PermissionDefault.OP, commands);


        commands.recalculatePermissibles();

        return commands;
    }
}
