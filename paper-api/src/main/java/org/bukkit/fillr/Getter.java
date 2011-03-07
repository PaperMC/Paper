package org.bukkit.fillr;

import org.bukkit.entity.Player;
import java.io.File;
import java.util.logging.Level;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;

public class Getter {
    private Server server;
    private static String DIRECTORY = Fillr.DIRECTORY;

    public Getter(Server server) {
        this.server = server;
    }

    public void get(String string, CommandSender sender) {
        FillReader reader = new FillReader(string);
        sender.sendMessage("Downloading " + reader.getName() + " "
                + reader.getCurrVersion());
        try {
            Downloader.downloadJar(reader.getFile());
            if (reader.getNotes() != null && !reader.getNotes().equals("")) {
                sender.sendMessage("Notes: " + reader.getNotes());
            }
            sender.sendMessage("Finished Download!");
            enablePlugin(reader);
            sender.sendMessage("Loading " + reader.getName());
        } catch (Exception ex) {
            server.getLogger().log(Level.SEVERE, null, ex);
        }
    }

    private void enablePlugin(FillReader update) {
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
}
