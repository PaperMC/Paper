package io.papermc.testplugin;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == org.bukkit.Material.DIAMOND_SWORD) {
            e.getPlayer().getLastTwoTargetBlocks(null, 5).forEach(block -> {
                e.getPlayer().sendMessage(block.getLocation().toString());
            });
        }
    }


    @EventHandler
    public void pistonPushEvent(BlockPistonExtendEvent e) {
        e.getBlocks().forEach(block -> {
            block.getRelative(e.getDirection()).getRelative(BlockFace.UP).setType(Material.BLUE_STAINED_GLASS);
            System.out.println(block);
        });
    }
}
