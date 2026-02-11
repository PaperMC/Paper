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

import static net.kyori.adventure.text.Component.text;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void on(BlockFertilizeEvent event) {
        //event.setCancelled(true);

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
    public void on(StructureGrowEvent event) {
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
    public void on(PlayerSwapHandItemsEvent event) {
        event.getPlayer().getTargetBlockExact(5).applyBoneMeal(BlockFace.UP);

        event.getPlayer().getWorld().generateTree(event.getPlayer().getLocation(), TreeType.values()[(int) (TreeType.values().length * Math.random())]);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        event.getPlayer().sendActionBar(text("Replaced: " + event.getBlockReplacedState().getType()));
        if (event.getPlayer().getInventory().contains(Material.DIAMOND)) {
            event.getBlock().setType(Material.AIR);
        } else if (event.getPlayer().getInventory().contains(Material.GOLD_INGOT)) {
            event.setCancelled(true);
        } else if (event.getPlayer().getInventory().contains(Material.EMERALD)) {
            event.getBlock().setType(Material.STONE);
        } else if (event.getPlayer().getInventory().contains(Material.GOLD_NUGGET)) {
            event.getBlock().getRelative(BlockFace.SOUTH).setType(Material.STONE);
        }
        event.getPlayer().sendMessage(event.getBlock().getType().toString());

        event.getBlockAgainst().setType(Material.DIAMOND_BLOCK);
    }

    @EventHandler
    public void blockPlace(BlockMultiPlaceEvent event) {
        event.getPlayer().sendActionBar(text("Replaced: " + event.getReplacedBlockStates().stream().map(BlockState::getBlockData).toList()));

        event.getPlayer().sendMessage(event.getBlock().getType().toString());
        if (event.getPlayer().getInventory().contains(Material.DIAMOND)) {
            event.setCancelled(true);
        }
    }
}
