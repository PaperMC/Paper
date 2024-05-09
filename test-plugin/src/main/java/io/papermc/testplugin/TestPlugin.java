package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void a(ChatEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(Bukkit.createProfileExact(UUID.randomUUID(), player.getName() + "aaaaaaaaaaaaaaaaaaa"));
        item.setItemMeta(meta);

        player.getInventory().setItemInMainHand(item);
    }
}
