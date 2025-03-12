package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    private Inventory inventory;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
        this.inventory = Bukkit.createInventory(null, 27);
    }

    @EventHandler
    public void onBreak(BlockDropItemEvent event) {
        inventory.addItem(event.getItems().stream().map(Item::getItemStack).toArray(ItemStack[]::new));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getPlayer().openInventory(MenuType.GENERIC_9X3.builder()
                .title(Component.text("OBSERVE ALL DROPS EVER"))
                .inventory(this.inventory)
                .build(event.getPlayer()));
    }
}
