package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.event.player.*;

public class FillrListener extends PlayerListener {

	private final Server server;

	public FillrListener(Server server) {
		this.server = server;
	}

	public void onPlayerCommand(PlayerChatEvent event) {
		String[] split = event.getMessage().split(" ");
		Player player = event.getPlayer();

		if (split[0].equalsIgnoreCase("/check")) {
			new Checker().check(player);
			event.setCancelled(true);
		} else if (split[0].equalsIgnoreCase("/updateAll")) {
			new Updater(server).updateAll(player);
			event.setCancelled(true);
		} else if (split[0].equalsIgnoreCase("/update")) {
			if (split.length == 1) {
				player.sendMessage("Usage is /update <name>");
			} else {
				new Updater(server).update(split[1], player);
			}
			event.setCancelled(true);
		} else if (split[0].equalsIgnoreCase("/get")) {
			if (split.length == 1) {
				player.sendMessage("Usage is /get <name>");
			} else {
				try {
					new Getter(server).get(split[1], player);
				} catch (Exception e) {
					player.sendMessage("There was an error downloading "
							+ split[1]);
				}
			}
			event.setCancelled(true);
		}
	}
}
