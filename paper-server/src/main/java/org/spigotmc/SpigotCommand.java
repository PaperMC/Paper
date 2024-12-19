package org.spigotmc;

import java.io.File;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;

public class SpigotCommand extends Command {

    public SpigotCommand(String name) {
        super(name);
        this.description = "Spigot related commands";
        this.usageMessage = "/spigot reload";
        this.setPermission("bukkit.command.spigot");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) return true;

        if (args.length != 1) {
            sender.sendMessage(text("Usage: " + this.usageMessage, NamedTextColor.RED));
            return false;
        }

        if (args[0].equals("reload")) {
            Command.broadcastCommandMessage(sender, text().color(NamedTextColor.RED)
                .append(text("Please note that this command is not supported and may cause issues."))
                .appendNewline()
                .append(text("If you encounter any issues please use the /stop command to restart your server."))
                .build()
            );

            MinecraftServer console = MinecraftServer.getServer();
            org.spigotmc.SpigotConfig.init((File) console.options.valueOf("spigot-settings"));
            for (ServerLevel world : console.getAllLevels()) {
                world.spigotConfig.init();
            }
            console.server.reloadCount++;

            Command.broadcastCommandMessage(sender, text("Reload complete.", NamedTextColor.GREEN));
        }

        return true;
    }
}
