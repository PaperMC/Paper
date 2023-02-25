package io.papermc.testplugin;

import io.papermc.paper.registry.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final Reference<Biome> biomeReference;

    public TestPlugin(Reference<Biome> biomeReference) {
        this.biomeReference = biomeReference;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Set the custom biome to the position where a player joins the server.
        Player player = e.getPlayer();
	Biome biome = player.getWorld().biome(player.getLocation());
        System.out.println("joined: " + biome);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();
        Biome biome = biomeReference.value();
        Location location = new Location(world, 1, 1, 1);
        world.loadChunk(location.getBlockX(), location.getBlockZ());
        System.out.println("set: " + biome);
        world.setBiome(location, biome);
        Biome biome1 = world.biome(location);
        System.out.println("got: " + biome1);
    }
}
