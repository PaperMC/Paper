package io.papermc.paper.testplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(BlockIgniteEvent e) {
        e.setCancelled(true);
    }
}
