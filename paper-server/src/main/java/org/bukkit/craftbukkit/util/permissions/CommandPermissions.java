package org.bukkit.craftbukkit.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CommandPermissions {
    private static final String ROOT = "minecraft.command";
    private static final String PREFIX = CommandPermissions.ROOT + ".";

    private CommandPermissions() {}

    public static Permission registerPermissions(Permission parent) {
        Permission commands = DefaultPermissions.registerPermission(CommandPermissions.ROOT, "Gives the user the ability to use all vanilla minecraft commands", parent);

        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "kill", "Allows the user to commit suicide", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "me", "Allows the user to perform a chat action", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "msg", "Allows the user to privately message another player", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "help", "Allows the user to access Vanilla command help", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "say", "Allows the user to talk as the console", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "give", "Allows the user to give items to players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "teleport", "Allows the user to teleport players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "kick", "Allows the user to kick players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "stop", "Allows the user to stop the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "list", "Allows the user to list all online players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "gamemode", "Allows the user to change the gamemode of another player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "experience", "Allows the user to give themselves or others arbitrary values of experience", PermissionDefault.OP, commands); // Paper - wrong permission; redirects are de-redirected and the root literal name is used, so xp -> experience
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "defaultgamemode", "Allows the user to change the default gamemode of the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "seed", "Allows the user to view the seed of the world", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "effect", "Allows the user to add/remove effects on players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "selector", "Allows the use of selectors", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "trigger", "Allows the use of the trigger command", PermissionDefault.TRUE, commands);
        // Paper start
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "advancement", "Allows the user to give, remove, or check player advancements", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "attribute", "Allows the user to query, add, remove or set an entity attribute", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "ban", "Allows the user to add players to the ban list", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "ban-ip", "Allows the user to add ip address to the ban list", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "banlist", "Allows the user to display the ban list", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "bossbar", "Allows the user to create and modify bossbars", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "clear", "Allows the user to clear items from player inventory", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "clone", "Allows the user to copy blocks from one place to another", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "damage", "Allows the user to use the damage command to damage entities", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "data", "Allows the user to get, merge, modify, and remove block entity and entity NBT data", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "datapack", "Allows the user to control loaded data packs", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "debug", "Allows the user to start or stop a debugging session", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "deop", "Allows the user to revoke operator status from a player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "dialog", "Allows the user to show dialogs", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "difficulty", "Allows the user to set the difficulty level", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "enchant", "Allows the user to enchant a player item", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "execute", "Allows the user to execute another command", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "fetchprofile", "Allows the user to fetch a player profile via name or id", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "fill", "Allows the user to fill a region with a specific block", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "fillbiome", "Allows the user to fill a region with a specific biome", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "forceload", "Allows the user to force chunks to be constantly loaded or not", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "function", "Allows the user to run a function", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "gamerule", "Allows a user to set or query a game rule value", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "item", "Allows the user to replace items in inventories", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "jfr", "Allows a user to use the vanilla Java FlightRecorder profiling system", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "locate", "Allows the user to locate the closest structure", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "loot", "Allows the user to drop items from an inventory slot onto the ground", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "op", "Allows the user to grant operator status to a player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "pardon", "Allows the user to remove entries from the player ban list", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "pardon-ip", "Allows the user to remove entries from the ip address ban list", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "particle", "Allows the user to create particles", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "perf", "Allows the user to start/stop the vanilla performance metrics capture", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "place", "Allows the user to place features and structures", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "playsound", "Allows the user to play a sound", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "random", "Allows the user to generate a random number", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "recipe", "Allows the user to give or take recipes", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "reload", "Allows the user to reload loot tables, advancements, and functions from disk", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "return", "Allows the user to use the /return command", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "ride", "Allows the user to use the /ride command to control passengers", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "rotate", "Allows the user to change the rotation of entities", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "save-all", "Allows the user to save the server to disk", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "save-off", "Allows the user disable automatic server saves", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "save-on", "Allows the user enable automatic server saves", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "schedule", "Allows the user to delay the execution of a function", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "scoreboard", "Allows the user manage scoreboard objectives and players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "setblock", "Allows the user to change a block to another block", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "setidletimeout", "Allows the user to set the time before idle players are kicked", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "setworldspawn", "Allows the user to set the world spawn", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "spawnpoint", "Allows the user to set the spawn point for a player", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "spectate", "Allows the user to make one player in spectator mode spectate an entity", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "spreadplayers", "Allows the user to teleport entities to random locations", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "stopsound", "Allows the user to stop a sound", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "summon", "Allows the user to summon an entity", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "tag", "Allows the user to control entity tags", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "team", "Allows the user to control teams", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "teammsg", "Allows the user to specify the message to send to team", PermissionDefault.TRUE, commands); // defaults to all players
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "tellraw", "Allows the user to display a JSON message to players", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "test", "Allows the user to manage and execute GameTests", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "tick", "Allows the user to control the tick rate of the server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "time", "Allows the user to change or query the world's game time", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "title", "Allows the user to manage screen titles", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "transfer", "Allows the user to transfer to another server", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "version", "Shows info related to the server version", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "waypoint", "Allows the management of a waypoints on the server/locator bar", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "weather", "Allows the user to set the weather", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "whitelist", "Allows the user to manage the server whitelist", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission(CommandPermissions.PREFIX + "worldborder", "Allows the user to manage the world border", PermissionDefault.OP, commands);
        // Paper end

        DefaultPermissions.registerPermission("minecraft.admin.command_feedback", "Receive command broadcasts when sendCommandFeedback is true", PermissionDefault.OP, commands);

        commands.recalculatePermissibles();
        return commands;
    }
}
