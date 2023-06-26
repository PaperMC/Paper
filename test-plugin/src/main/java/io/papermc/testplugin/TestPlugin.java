package io.papermc.testplugin;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    long start = Long.MIN_VALUE;
    boolean cancel = false;
    long count = 0;

    @EventHandler
    public void onEvent(ChatEvent event) {
        if (!this.cancel) {
            for (final Entity entity : event.getPlayer().getWorld().getEntities()) {
                if (!(entity instanceof Player)) {
                    entity.remove();
                }
            }
            event.getPlayer().sendPlainMessage("starting track");
            this.count = 0;
            this.cancel = true;
            this.start = System.currentTimeMillis();
        } else {
            this.cancel = false;
            event.getPlayer().sendPlainMessage("Count: " + this.count + ". Time: " + ((System.currentTimeMillis() - this.start) / 1000) + " secs.");
        }
    }

    @EventHandler
    public void onEvent(PreCreatureSpawnEvent event) {
        if (this.cancel) {
            this.count++;
            event.setCancelled(true);
            event.setShouldAbortSpawn(true);
        }
    }
}
