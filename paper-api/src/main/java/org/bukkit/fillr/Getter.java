package org.bukkit.fillr;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

public class Getter {
    private Server server;
    private static String directory = Fillr.directory;

    public Getter(Server server) {
        this.server = server;
    }

    public void get(String string, Player player) {
        FillReader reader = new FillReader(string);
        player.sendMessage("Downloading " + reader.getName() + " "
                + reader.getCurrVersion());
        try {
            Downloader.downloadJar(reader.getFile());
            if (reader.getNotes() != null && !reader.getNotes().equals("")) {
                player.sendMessage("Notes: " + reader.getNotes());
            }
            player.sendMessage("Finished Download!");
            enablePlugin(reader);
            player.sendMessage("Loading " + reader.getName());
        } catch (Exception ex) {
            Logger.getLogger(Getter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enablePlugin(FillReader update) {
        final String name = update.getName();
        //TODO again with the implicit jar support...
        File plugin = new File(directory, name + ".jar");
        try {
            server.getPluginManager().loadPlugin(plugin);
        } catch (InvalidPluginException ex) {
            Logger.getLogger(Getter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDescriptionException ex) {
            Logger.getLogger(Getter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
