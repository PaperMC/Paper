package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TimingsCommand extends Command {
    public TimingsCommand(String name) {
        super(name);
        this.description = "Records timings for all plugin events";
        this.usageMessage = "/timings <function>";
        this.setPermission("bukkit.command.timings");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1) return false;

        boolean seperate = "seperate".equals(args[0]);
        if ("reset".equals(args[0])) {
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                plugin.resetTimings();
            }
            sender.sendMessage("Timings reset");
        }
        else if("merged".equals(args[0]) || seperate) {

            int index = 0;
            int pluginIdx = 0;
            File timingFolder = new File("timings");
            timingFolder.mkdirs();
            File timings = new File(timingFolder, "timings.txt");
            File names = null;
            while (timings.exists()) timings = new File(timingFolder, "timinigs" + (++index) + ".txt");
            try {
                PrintStream fileTimings = new PrintStream(timings);
                PrintStream fileNames = null;
                if (seperate) {
                    names = new File(timingFolder, "names" + index + ".txt");
                    fileNames = new PrintStream(names);
                }
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    pluginIdx++;
                    long totalTime = 0;
                    if (seperate) {
                        fileNames.println(pluginIdx + " " + plugin.getDescription().getFullName());
                        fileTimings.println("Plugin " + pluginIdx);
                    }
                    else fileTimings.println(plugin.getDescription().getFullName());
                    for (Event.Type type : Event.Type.values()) {
                        long time = plugin.getTiming(type);
                        totalTime += time;
                        if (time > 0) {
                            fileTimings.println("    " + type.name() + " " + time);
                        }
                    }
                    fileTimings.println("    Total time " + totalTime + " (" + totalTime / 1000000000 + "s)");
                }
                sender.sendMessage("Timings written to " + timings.getPath());
                if (seperate) sender.sendMessage("Names written to " + names.getPath());
            } catch (IOException e) {
            }
        }
        return true;
    }
}
