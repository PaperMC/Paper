package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DefaultGameModeCommand extends VanillaCommand {
    public DefaultGameModeCommand() {
        super("defaultgamemode");
        this.description = "Set the default gamemode";
        this.usageMessage = "/defaultgamemode <mode>";
        this.setPermission("bukkit.command.defaultgamemode");
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase(this.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0) {
            sender.sendMessage("Usage: " + usageMessage);
            return false;
        }

        String modeArg = args[0];
        int value = -1;

        try {
            value = Integer.parseInt(modeArg);
        } catch (NumberFormatException ex) {}

        GameMode mode = GameMode.getByValue(value);

        if (mode == null) {
            if (modeArg.equalsIgnoreCase("creative") || modeArg.equalsIgnoreCase("c")) {
                mode = GameMode.CREATIVE;
            } else if (modeArg.equalsIgnoreCase("adventure") || modeArg.equalsIgnoreCase("a")) {
                mode = GameMode.ADVENTURE;
            } else {
                mode = GameMode.SURVIVAL;
            }
        }

        Bukkit.getServer().setDefaultGameMode(mode);
        Command.broadcastCommandMessage(sender, "Default game mode set to " + mode.toString().toLowerCase());

        return true;
    }

}
