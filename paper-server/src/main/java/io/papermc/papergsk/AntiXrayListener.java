package io.papermc.papergsk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;

/**
 * Listens for block break events and flags suspicious xray activity
 */
public class AntiXrayListener implements Listener {
    
    private final AntiXraySystem antiXraySystem;
    
    public AntiXrayListener(AntiXraySystem antiXraySystem) {
        this.antiXraySystem = antiXraySystem;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        antiXraySystem.recordOreBreak(player, event.getBlock());
    }
}
