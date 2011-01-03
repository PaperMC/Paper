package org.bukkit.fillr;
import org.bukkit.*;

import java.util.logging.Logger;

public class Fillr {
	private Logger log;
	private String name = "Fillr";
	public static String version = "1.0";
	public Server server;
	private static Fillr singleton;
	public static String directory = "plugins";
	
	private Fillr(Server server) {
		this.server = server;
	}
	
	public Fillr createInstance(Server server) {
		singleton = new Fillr(server);
		return singleton;
	}
	
	public static Fillr getInstance() {
		return singleton;
	}
	
	//TODO Get command hooks in somehow...
	/*public class UpdatrListener extends PluginListener {

		public boolean onCommand(Player player, String[] split) {
			if (split[0].equalsIgnoreCase("/check")
					&& player.canUseCommand("/check")) {
				new Checker().check(player);
				return true;
			}
			if (split[0].equalsIgnoreCase("/updateAll")
					&& player.canUseCommand("/updateAll")) {
				new Updater().updateAll(player);
				return true;
			}
			if (split[0].equalsIgnoreCase("/update")
					&& player.canUseCommand("/update")) {
				if (split.length == 1) {
					player.sendMessage(premessage + "Usage is /update <name>");
				} else {
					new Updater().update(split[1], player);
				}
				return true;
			}
			if (split[0].equalsIgnoreCase("/download")
					&& player.canUseCommand("/download")) {
				if (split.length == 1) {
					player.sendMessage(premessage + "Usage is /download <name>");
				} else {
					try {
					new Downloader().downloadFile(split[1], player);
					} catch(Exception e) {
						player.sendMessage("There was an error downloading "+ split[1]);
					}
				}
				return true;
			}

			return false;
		}
	}*/
}
