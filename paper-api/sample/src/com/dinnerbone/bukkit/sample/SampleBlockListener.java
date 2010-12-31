
package com.dinnerbone.bukkit.sample;

import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * Sample block listener
 * @author Dinnerbone
 */
public class SampleBlockListener extends BlockListener {
    private final SamplePlugin plugin;

    public SampleBlockListener(final SamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        if ((block.getType() == 12) || (block.getType() == 13)) {
            Block above = block.getFace(BlockFace.Up);
            if (above.getType() == 42) {
                event.setCancelled(true);
            }
        }
    }
}
