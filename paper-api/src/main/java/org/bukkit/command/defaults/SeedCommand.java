package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeedCommand extends VanillaCommand {
    public SeedCommand() {
        super("seed");
        this.description = "Shows the world seed";
        this.usageMessage = "/seed";
        this.setPermission("bukkit.command.seed");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        long seed;
        if (sender instanceof Player) {
            seed = ((Player) sender).getWorld().getSeed();
        } else {
            seed = Bukkit.getWorlds().get(0).getSeed();
        }
        sender.sendMessage("Seed: " + seed);
        return true;
    }
}
