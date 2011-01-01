
package com.dinnerbone.bukkit.sample;

import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
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

        if ((block.getType() == Material.Sand) || (block.getType() == Material.Gravel)) {
            Block above = block.getFace(BlockFace.Up);
            if (above.getType() == Material.IronBlock) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        Material mat = event.getMaterial();

        if (mat.equals(Material.Cactus)) {
            event.setBuildable(true);
        }
    }
}
