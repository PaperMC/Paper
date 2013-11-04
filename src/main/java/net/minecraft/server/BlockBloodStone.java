package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockBloodStone extends Block {

    public BlockBloodStone() {
        super(Material.STONE);
        this.a(CreativeModeTab.b);
    }

    public MaterialMapColor f(int i) {
        return MaterialMapColor.K;
    }

    // CraftBukkit start
    public void doPhysics(World world, int i, int j, int k, int l) {
        if (net.minecraft.server.Block.e(l) != null && net.minecraft.server.Block.e(l).isPowerSource()) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int power = block.getBlockPower();

            BlockRedstoneEvent event = new BlockRedstoneEvent(block, power, power);
            world.getServer().getPluginManager().callEvent(event);
        }
    }
    // CraftBukkit end
}
