package io.papermc.testplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register the plugin to listen for events
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    // Event handler for player join event
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Your custom logic when a player joins
        event.getPlayer().sendMessage("Welcome to the server, " + event.getPlayer().getName() + "!");
    }
}
