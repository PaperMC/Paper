package io.papermc.testplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.stream.Collectors;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        getLogger().info(event.getItemDrop().getItemStack().getType().getKey().asMinimalString() + ": " + event.getItemDrop().getItemStack().getCreativeCategories().stream().map(CreativeCategory::name).collect(Collectors.joining(", ")));
    }
}
