package io.papermc.testplugin;

import io.papermc.paper.inventory.item.properties.ItemProperties;
import io.papermc.paper.property.StoredPropertyHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void swapHands(PlayerSwapHandItemsEvent event) {
        ItemStack itemStack = Bukkit.getUnsafe().newItem(Material.DIAMOND_SWORD, 1);
        //StoredPropertyHolder holder = itemStack.getPropertyHolder().create(ItemProperties.DISPLAY); TODO:  properly apply adapters
        itemStack.getPropertyHolder().set(ItemProperties.DISPLAY_NAME, Component.text("hi"));

        event.getPlayer().getInventory().addItem(itemStack);
    }
}
