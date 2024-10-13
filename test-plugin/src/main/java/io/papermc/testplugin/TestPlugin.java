package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LoomInventory;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        final StonecutterInventory inv = (StonecutterInventory) this.getServer().createInventory(null, InventoryType.STONECUTTER);
        // inv.setItem(0, new ItemStack(Material.STONE));
        this.getServer().getScheduler().runTaskLater(this, () -> {
            inv.setItem(0, new ItemStack(Material.STONE));
        }, 40L);
        event.getPlayer().openInventory(inv);
    }

}
