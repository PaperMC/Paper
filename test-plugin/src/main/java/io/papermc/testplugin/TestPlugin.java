package io.papermc.testplugin;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

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

//        Bukkit.getPlayer("Owen1212055").sendBlockChanges(event.getBlocks());
//
//        new BukkitRunnable(){
//
//            @Override
//            public void run() {
//                event.getBlocks().forEach((state) -> Bukkit.getPlayer("Owen1212055").sendBlockChange(state.getLocation(), state.getBlock().getType().createBlockData()));
//            }
//        }.runTaskLater(this, 20 * 2);

    }


    @EventHandler
    public void blockPlace(StructureGrowEvent event) {
//        event.setCancelled(true);
//
//        event.getPlayer().sendBlockChanges(event.getBlocks());
//
//        new BukkitRunnable(){
//
//            @Override
//            public void run() {
//                event.getBlocks().forEach((state) -> event.getPlayer().sendBlockChange(state.getLocation(), state.getBlock().getType().createBlockData()));
//            }
//        }.runTaskLater(this, 20 * 2);
    }


    @EventHandler
    public void blockPlace(PlayerSwapHandItemsEvent event) {
        event.getPlayer().getTargetBlockExact(5).applyBoneMeal(BlockFace.UP);

        event.getPlayer().getWorld().generateTree(event.getPlayer().getLocation(), TreeType.values()[(int) (TreeType.values().length * Math.random())]);
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        event.getPlayer().sendActionBar("Replaced: " + event.getBlockReplacedState().getType());
        if (event.getPlayer().getInventory().contains(Material.DIAMOND)) {
            event.getBlock().setType(Material.CHEST);

            Chest state = (Chest) event.getBlock().getState(false);
            state.getBlockInventory().setItem(0, new ItemStack(Material.STONE));
            System.out.println(state);
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
