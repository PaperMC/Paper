package org.bukkit.command.defaults;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class TimingsCommand extends BukkitCommand {
    private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("merged", "reset", "separate");

    public TimingsCommand(@NotNull String name) {
        super(name);
        this.description = "Records timings for all plugin events";
        this.usageMessage = "/timings <reset|merged|separate>";
        this.setPermission("bukkit.command.timings");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        if (!sender.getServer().getPluginManager().useTimings()) {
            sender.sendMessage("Please enable timings by setting \"settings.plugin-profiling\" to true in bukkit.yml");
            return true;
        }

        boolean separate = "separate".equalsIgnoreCase(args[0]);
        if ("reset".equalsIgnoreCase(args[0])) {
            for (HandlerList handlerList : HandlerList.getHandlerLists()) {
                for (RegisteredListener listener : handlerList.getRegisteredListeners()) {
                    if (listener instanceof TimedRegisteredListener) {
                        ((TimedRegisteredListener) listener).reset();
                    }
                }
            }
            sender.sendMessage("Timings reset");
        } else if ("merged".equalsIgnoreCase(args[0]) || separate) {

            int index = 0;
            int pluginIdx = 0;
            File timingFolder = new File("timings");
            timingFolder.mkdirs();
            File timings = new File(timingFolder, "timings.txt");
            File names = null;
            while (timings.exists()) timings = new File(timingFolder, "timings" + (++index) + ".txt");
            PrintStream fileTimings = null;
            PrintStream fileNames = null;
            try {
                fileTimings = new PrintStream(timings);
                if (separate) {
                    names = new File(timingFolder, "names" + index + ".txt");
                    fileNames = new PrintStream(names);
                }
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    pluginIdx++;
                    long totalTime = 0;
                    if (separate) {
                        fileNames.println(pluginIdx + " " + plugin.getDescription().getFullName());
                        fileTimings.println("Plugin " + pluginIdx);
                    } else {
                        fileTimings.println(plugin.getDescription().getFullName());
                    }
                    for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
                        if (listener instanceof TimedRegisteredListener) {
                            TimedRegisteredListener trl = (TimedRegisteredListener) listener;
                            long time = trl.getTotalTime();
                            int count = trl.getCount();
                            if (count == 0) continue;
                            long avg = time / count;
                            totalTime += time;
                            Class<? extends Event> eventClass = trl.getEventClass();
                            if (count > 0 && eventClass != null) {
                                fileTimings.println("    " + eventClass.getSimpleName() + (trl.hasMultiple() ? " (and sub-classes)" : "") + " Time: " + time + " Count: " + count + " Avg: " + avg);
                            }
                        }
                    }
                    fileTimings.println("    Total time " + totalTime + " (" + totalTime / 1000000000 + "s)");
                }
                sender.sendMessage("Timings written to " + timings.getPath());
                if (separate) sender.sendMessage("Names written to " + names.getPath());
            } catch (IOException e) {
            } finally {
                if (fileTimings != null) {
                    fileTimings.close();
                }
                if (fileNames != null) {
                    fileNames.close();
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        Preconditions.checkArgument(args != null, "Arguments cannot be null");
        Preconditions.checkArgument(alias != null, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], TIMINGS_SUBCOMMANDS, new ArrayList<String>(TIMINGS_SUBCOMMANDS.size()));
        }
        return ImmutableList.of();
    }
}
