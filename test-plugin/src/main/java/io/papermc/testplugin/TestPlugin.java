package io.papermc.testplugin;

import io.papermc.paper.registry.Reference;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final String key;
    private final Reference<Biome> biomeReference;

    public TestPlugin(String key, Reference<Biome> biomeReference) {
        this.key = key;
        this.biomeReference = biomeReference;
    }

    @Override
    public void onLoad() {
        this.getLogger().log(Level.INFO, "HELLO FROM onLoad!");
        this.getLogger().log(Level.INFO, "Secret bootstrap value: " + this.key);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Set the custom biome to the position where a player joins the server.
        Player player = e.getPlayer();
        player.getWorld().setBiome(player.getLocation(), biomeReference.value());
    }
}
