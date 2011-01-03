package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.event.*;

import java.io.File;
import java.util.logging.Logger;

public class Fillr extends JavaPlugin {
	private Logger log;
	private FillrListener listener;
	public static String name = "Fillr";
	public static String version = "1.0";
	public static String directory = "plugins";
	
	public Fillr(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
		super(pluginLoader, instance, desc, plugin, cLoader);
		registerEvents();
	}
	
	public void onDisable() {}
	public void onEnable() {}
	
	public void registerEvents() {
		listener = new FillrListener(this.getServer());
	    getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, listener, Event.Priority.Normal, this);
	}

}
