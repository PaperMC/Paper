package org.bukkit.fillr;

import java.io.*;
import java.util.jar.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;

public class Checker {
	private static String DIRECTORY = Fillr.DIRECTORY;

	/**
	 * Checks all the plugins in plugins/ for updates
	 * 
	 * @param sender
	 *            The player to send info to
	 */
	void check(CommandSender sender) {
		File folder = new File(DIRECTORY);
		File[] files = folder.listFiles(new PluginFilter());
		if (files.length == 0) {
			sender.sendMessage("No plugins to update.");
		} else {
			sender.sendMessage("Status for " + files.length + " plugins:");
			for (File file : files) {
				PluginDescriptionFile pdfFile = Checker.getPDF(file);
				if (pdfFile == null) {
					continue;
				}
				checkForUpdate(file, sender);
			}
		}
	}

	/**
	 * Checks for an update for a given plugin
	 * 
	 * @param file
	 *            The plugin file to check for an update
	 * @param sender
	 *            The player to send info to
	 */
	private void checkForUpdate(File file, CommandSender sender) {
		PluginDescriptionFile pdfFile = Checker.getPDF(file);
		FillReader reader = needsUpdate(pdfFile);
		if (reader != null) {
			sender.sendMessage(ChatColor.RED + reader.getName() + " " + pdfFile.getVersion() + " has an update to " + reader.getCurrVersion());
		} else {
			sender.sendMessage(pdfFile.getName() + " " + pdfFile.getVersion() + " is up to date!");
		}
	}

	/**
	 * Checks if a given plugin needs an update
	 * 
	 * @param file
	 *            The .yml file to check
	 * @return The FillReader for the online repo info on the plugin if the plugin needs an update
	 * 			Returns null if no update is needed.
	 */
	static FillReader needsUpdate(PluginDescriptionFile file) {
		FillReader reader = new FillReader(file.getName());
		String version = file.getVersion();
		String currVersion = reader.getCurrVersion();
		String name = reader.getName();
		if (currVersion.equalsIgnoreCase(version) && new File(DIRECTORY, name + ".jar").exists()) {
			return null;
		} else {
			return reader;
		}
	}

	/**
	 * Will grab the plugin's .yml file from the give file (hopefully a plugin).
	 * It'll throw it into a PluginDescriptionFile
	 * 
	 * @param file
	 *            The plugin (jar) file
	 * @return The PluginDescriptionFile representing the .yml
	 */
	static PluginDescriptionFile getPDF(File file) {
		// TODO supports only jar files for now. how will yml's be stored in
		// different languages?
		if (file.getName().endsWith(".jar")) {
			JarFile jarFile;
			try {
				jarFile = new JarFile(file);
				JarEntry entry = jarFile.getJarEntry("plugin.yml");
				InputStream input = jarFile.getInputStream(entry);
				return new PluginDescriptionFile(input);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (InvalidDescriptionException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
}
