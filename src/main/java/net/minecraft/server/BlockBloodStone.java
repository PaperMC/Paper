package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
// CraftBukkit end

public class BlockBloodStone extends Block {

    public BlockBloodStone(int i, int j) {
        super(i, j, Material.STONE);
    }

    // CraftBukkit start
    public void doPhysics(World world, int i, int j, int k, int l) {
        if (net.minecraft.server.Block.byId[l] != null && net.minecraft.server.Block.byId[l].isPowerSource()) {
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            org.bukkit.block.Block block = craftWorld.getBlockAt(i, j, k);
            int power = block.getBlockPower();

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, power, power);
            server.getPluginManager().callEvent(eventRedstone);
        }
    }
    // CraftBukkit end
}
