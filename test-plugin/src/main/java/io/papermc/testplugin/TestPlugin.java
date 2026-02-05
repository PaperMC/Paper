package io.papermc.testplugin;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void blockPlace(BlockFertilizeEvent event) {
       //event.setCancelled(true);

        event.getPlayer().sendBlockChanges(event.getBlocks());

        new BukkitRunnable(){

            @Override
            public void run() {
                event.getBlocks().forEach((state) -> event.getPlayer().sendBlockChange(state.getLocation(), state.getBlock().getType().createBlockData()));
            }
        }.runTaskLater(this, 20 * 2);

    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        event.getPlayer().sendActionBar("Replaced: " + event.getBlockReplacedState().getType());
        if (event.getPlayer().getInventory().contains(Material.DIAMOND)) {
            event.getBlock().setType(Material.CHEST);

            Chest state = (Chest) event.getBlock().getState(false);
            state.getBlockInventory().setItem(0, new ItemStack(Material.STONE));
        } else if (event.getPlayer().getInventory().contains(Material.GOLD_INGOT)) {
            event.setCancelled(true);
        }
        event.getPlayer().sendMessage(event.getBlock().getType().toString());
    }

    @EventHandler
    public void blockPlace(BlockMultiPlaceEvent event) {
        event.getPlayer().sendActionBar("Replaced: " + String.join(",", event.getReplacedBlockStates().stream().map(BlockState::getType).map(Material::toString).toList()));

        event.getPlayer().sendMessage(event.getBlock().getType().toString());
        if (event.getPlayer().getInventory().contains(Material.DIAMOND)) {
            event.setCancelled(true);
        }
    }



}
