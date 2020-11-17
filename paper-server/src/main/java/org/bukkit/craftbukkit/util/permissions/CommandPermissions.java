package org.bukkit.craftbukkit.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CommandPermissions {
    private static final String ROOT = "minecraft.command";
    private static final String PREFIX = ROOT + ".";

    private CommandPermissions() {}

    public static Permission registerPermissions(Permission parent) {
        Permission commands = DefaultPermissions.registerPermission(ROOT, "Gives the user the ability to use all vanilla minecraft commands", parent);

        DefaultPermissions.registerPermission(PREFIX + "kill", "Allows the user to commit suicide", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "me", "Allows the user to perform a chat action", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "msg", "Allows the user to privately message another player", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "help", "Allows the user to access Vanilla command help", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(PREFIX + "say", "Allows the user to talk as the console", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "give", "Allows the user to give items to players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "teleport", "Allows the user to teleport players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "kick", "Allows the user to kick players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "stop", "Allows the user to stop the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "list", "Allows the user to list all online players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "gamemode", "Allows the user to change the gamemode of another player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "xp", "Allows the user to give themselves or others arbitrary values of experience", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "toggledownfall", "Allows the user to toggle rain on/off for a given world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "defaultgamemode", "Allows the user to change the default gamemode of the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "seed", "Allows the user to view the seed of the world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "effect", "Allows the user to add/remove effects on players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "selector", "Allows the use of selectors", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(PREFIX + "trigger", "Allows the use of the trigger command", PermissionDefault.TRUE, commands);

        DefaultPermissions.registerPermission("minecraft.admin.command_feedback", "Receive command broadcasts when sendCommandFeedback is true", PermissionDefault.OP, commands);

        commands.recalculatePermissibles();
        return commands;
    }
}
