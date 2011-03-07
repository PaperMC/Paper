package org.bukkit.fillr;

import org.bukkit.entity.Player;
import org.bukkit.*;
import org.bukkit.plugin.*;

import java.io.File;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;

public class Updater {
    public static String DIRECTORY = Fillr.DIRECTORY;
    private final Server server;

    Updater(Server server) {
        this.server = server;
    }

    /**
     * Checks and updates the plugins
     *
     * @param sender
     *            The player to send info to
     */
    void updateAll(CommandSender sender) {
        File folder = new File(DIRECTORY);
        File[] files = folder.listFiles(new PluginFilter());
        if (files.length == 0) {
            sender.sendMessage("No plugins to update.");
        } else {
            sender.sendMessage("Updating "
                    + files.length + " plugins:");
            for (File file : files) {
                PluginDescriptionFile pdfFile = Checker.getPDF(file);
                if (pdfFile == null) {
                    continue;
                }
                FillReader reader = Checker.needsUpdate(pdfFile);
                if (reader != null) {
                    update(reader, sender);
                }
            }
        }
    }

    /**
     * Checks if a given plugin needs an update, if it does, it updates it
     *
     * @param string
     *            The name of the plugin
     * @param player
     *            The player to send info to
     */
    void update(String string, CommandSender player) {
        //TODO so much .jars
        File file = new File(DIRECTORY, string + ".jar");
        if (file.exists()) {
            PluginDescriptionFile pdfFile = Checker.getPDF(file);
            FillReader reader = Checker.needsUpdate(pdfFile);
            if (reader != null) {
                update(reader, player);
            } else {
                player.sendMessage(string + " is up to date");
            }
        } else {
            player.sendMessage("Can't find " + string);
        }
    }

    /**
     * Downloads the plugin specified by the URLReader
     *
     * @param update
     *            The FillReader with all the plugin info
     * @param sender The player to send info to
     */
    private void update(FillReader update, CommandSender sender) {
        disablePlugin(update);
        sender.sendMessage("Disabling " + update.getName() + " for update");
        sender.sendMessage("Downloading " + update.getName() + " "
                + update.getCurrVersion());
        try {
            Downloader.downloadJar(update.getFile());
            if (update.getNotes() != null && !update.getNotes().equals("")) {
                sender.sendMessage("Notes: " + update.getNotes());
            }
            sender.sendMessage("Finished Download!");
            enablePlugin(update);
            sender.sendMessage("Loading " + update.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void enablePlugin(FillReader update) {
        final String name = update.getName();
        //TODO again with the implicit jar support...
        File plugin = new File(DIRECTORY, name + ".jar");
        try {
            server.getPluginManager().loadPlugin(plugin);
        } catch (UnknownDependencyException ex) {
            server.getLogger().log(Level.SEVERE, null, ex);
        } catch (InvalidPluginException ex) {
            server.getLogger().log(Level.SEVERE, null, ex);
        } catch (InvalidDescriptionException ex) {
            server.getLogger().log(Level.SEVERE, null, ex);
        }
    }

    private void disablePlugin(FillReader update) {
        String name = update.getName();
        Plugin plugin = server.getPluginManager().getPlugin(name);
        server.getPluginManager().disablePlugin(plugin);
    }
}
