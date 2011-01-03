package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.plugin.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

public class Updater {
	public static String directory = Fillr.directory;

	/**
	 * Checks and updates the plugins with updatr files
	 * 
	 * @param player
	 *            The player to send info to
	 */
	public void updateAll(Player player) { 
		File folder = new File(directory);
		File[] files = folder.listFiles(new PluginFilter());
		if (files.length == 0) {
			player.sendMessage("No plugins to update.");
		} else {
			player.sendMessage("Updating "
					+ files.length + " plugins:");
			for (File file : files) {
				PluginDescriptionFile pdfFile = Checker.getPDF(file);
				if(pdfFile == null) continue;
				FillReader reader = Checker.needsUpdate(pdfFile);
				if (reader != null)
					update(reader, player);
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
	public void update(String string, Player player) {
		//TODO so much .jars
		File file = new File(directory, string + ".jar");
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
	 *            The URLReader representing the online .updatr file
	 */
	public void update(FillReader update, Player player) {
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

	private void enablePlugin(FillReader update) {
		final String name = update.getName();
		//TODO again with the implicit jar support...
		File plugin = new File(directory, name + ".jar");
		try {
			Fillr.getInstance().server.getPluginManager().loadPlugin(plugin);
		} catch (InvalidPluginException e) {
			e.printStackTrace();
		}
	}

	private void disablePlugin(FillReader update) {
		String name = update.getName();
		Plugin plugin = Fillr.getInstance().server.getPluginManager().getPlugin(name);
		Fillr.getInstance().server.getPluginManager().disablePlugin(plugin);
	}
}