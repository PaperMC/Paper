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

// Spigot start
// CHECKSTYLE:OFF
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.plugin.SimplePluginManager;
import org.spigotmc.CustomTimingsHandler;
// CHECKSTYLE:ON
// Spigot end

public class TimingsCommand extends BukkitCommand {
    private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("report", "reset", "on", "off", "paste"); // Spigot
    public static long timingStart = 0; // Spigot

    public TimingsCommand(@NotNull String name) {
        super(name);
        this.description = "Manages Spigot Timings data to see performance of the server."; // Spigot
        this.usageMessage = "/timings <reset|report|on|off|paste>"; // Spigot
        this.setPermission("bukkit.command.timings");
    }

    // Spigot start - redesigned Timings Command
    public void executeSpigotTimings(@NotNull CommandSender sender, @NotNull String[] args) {
        if ("on".equals(args[0])) {
            ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(true);
            CustomTimingsHandler.reload();
            sender.sendMessage("Enabled Timings & Reset");
            return;
        } else if ("off".equals(args[0])) {
            ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(false);
            sender.sendMessage("Disabled Timings");
            return;
        }

        if (!Bukkit.getPluginManager().useTimings()) {
            sender.sendMessage("Please enable timings by typing /timings on");
            return;
        }

        boolean paste = "paste".equals(args[0]);
        if ("reset".equals(args[0])) {
            CustomTimingsHandler.reload();
            sender.sendMessage("Timings reset");
        } else if ("merged".equals(args[0]) || "report".equals(args[0]) || paste) {
            long sampleTime = System.nanoTime() - timingStart;
            int index = 0;
            File timingFolder = new File("timings");
            timingFolder.mkdirs();
            File timings = new File(timingFolder, "timings.txt");
            ByteArrayOutputStream bout = (paste) ? new ByteArrayOutputStream() : null;
            while (timings.exists()) timings = new File(timingFolder, "timings" + (++index) + ".txt");
            PrintStream fileTimings = null;
            try {
                fileTimings = (paste) ? new PrintStream(bout) : new PrintStream(timings);

                CustomTimingsHandler.printTimings(fileTimings);
                fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1E9 + "s)");

                fileTimings.println("<spigotConfig>");
                fileTimings.println(Bukkit.spigot().getConfig().saveToString());
                fileTimings.println("</spigotConfig>");

                if (paste) {
                    new PasteThread(sender, bout).start();
                    return;
                }

                sender.sendMessage("Timings written to " + timings.getPath());
                sender.sendMessage("Paste contents of file into form at http://www.spigotmc.org/go/timings to read results.");

            } catch (IOException e) {
            } finally {
                if (fileTimings != null) {
                    fileTimings.close();
                }
            }
        }
    }
    // Spigot end

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1) { // Spigot
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        // Spigot start
        if (true) {
            executeSpigotTimings(sender, args);
            return true;
        }
        // Spigot end
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

    // Spigot start
    private static class PasteThread extends Thread {

        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(@NotNull CommandSender sender, @NotNull ByteArrayOutputStream bout) {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }

        @Override
        public synchronized void start() {
            if (sender instanceof RemoteConsoleCommandSender) {
                run();
            } else {
                super.start();
            }
        }

        @Override
        public void run() {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("https://timings.spigotmc.org/paste").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setInstanceFollowRedirects(false);

                OutputStream out = con.getOutputStream();
                out.write(bout.toByteArray());
                out.close();

                com.google.gson.JsonObject location = new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(con.getInputStream()), com.google.gson.JsonObject.class);
                con.getInputStream().close();

                String pasteID = location.get("key").getAsString();
                sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at https://www.spigotmc.org/go/timings?url=" + pasteID);
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
            }
        }
    }
    // Spigot end
}
