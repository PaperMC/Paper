package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockFadeEvent; // CraftBukkit

public class BlockIce extends BlockBreakable {

    public BlockIce(int i, int j) {
        super(i, j, Material.ICE, false);
        this.frictionFactor = 0.98F;
        this.a(true);
    }

    public void remove(World world, int i, int j, int k) {
        Material material = world.getMaterial(i, j - 1, k);

        if (material.isSolid() || material.isLiquid()) {
            world.setTypeId(i, j, k, Block.WATER.id);
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.a(EnumSkyBlock.BLOCK, i, j, k) > 11 - Block.q[this.id]) {
            // CraftBukkit start
            org.bukkit.block.BlockState blockState = world.getWorld().getBlockAt(i, j, k).getState();
            blockState.setTypeId(this.id);

            BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, Block.STATIONARY_WATER.id);
        }
    }
}
