package io.papermc.paper.testplugin;

import io.papermc.paper.world.poi.PoiManager;
import io.papermc.paper.world.poi.RegisteredPoi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        new BukkitRunnable(){

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PoiManager manager = player.getWorld().getPoiManager();
                    for (RegisteredPoi registeredPoi : manager.getPoisInRange(player.getLocation(), 16)) {
                        player.spawnParticle(Particle.BLOCK_MARKER, registeredPoi.getLocation().toCenterLocation().add(0, 1, 0), 1, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                    }
                }
            }
        }.runTaskTimer(this, 1, 1);
    }

}
