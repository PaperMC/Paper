package org.bukkit.fillr;

import org.bukkit.entity.Player;
import org.bukkit.*;
import org.bukkit.plugin.*;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater {
    public static String DIRECTORY = Fillr.DIRECTORY;
    private final Server server;

    Updater(Server server) {
        this.server = server;
    }

    /**
     * Checks and updates the plugins
     *
     * @param player
     *            The player to send info to
     */
    void updateAll(Player player) {
        File folder = new File(DIRECTORY);
        File[] files = folder.listFiles(new PluginFilter());
        if (files.length == 0) {
            player.sendMessage("No plugins to update.");
        } else {
            player.sendMessage("Updating "
                    + files.length + " plugins:");
            for (File file : files) {
                PluginDescriptionFile pdfFile = Checker.getPDF(file);
                if (pdfFile == null) {
                    continue;
                }
                FillReader reader = Checker.needsUpdate(pdfFile);
                if (reader != null) {
                    update(reader, player);
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
    void update(String string, Player player) {
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
     * @param player The player to send info to
     */
    private void update(FillReader update, Player player) {
        disablePlugin(update);
        player.sendMessage("Disabling " + update.getName() + " for update");
        player.sendMessage("Downloading " + update.getName() + " "
                + update.getCurrVersion());
        try {
            Downloader.downloadJar(update.getFile());
            if (update.getNotes() != null && !update.getNotes().equals("")) {
                player.sendMessage("Notes: " + update.getNotes());
            }
            player.sendMessage("Finished Download!");
            enablePlugin(update);
            player.sendMessage("Loading " + update.getName());
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
        } catch (InvalidPluginException ex) {
            Logger.getLogger(Getter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDescriptionException ex) {
            Logger.getLogger(Getter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void disablePlugin(FillReader update) {
        String name = update.getName();
        Plugin plugin = server.getPluginManager().getPlugin(name);
        server.getPluginManager().disablePlugin(plugin);
    }
}
