package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockCommand extends BlockContainer {

    public BlockCommand(int i) {
        super(i, 184, Material.ORE);
    }

    public TileEntity a(World world) {
        return new TileEntityCommand();
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            boolean flag = world.isBlockIndirectlyPowered(i, j, k);
            int i1 = world.getData(i, j, k);
            boolean flag1 = (i1 & 1) != 0;

            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = flag1 ? 1 : 0;
            int current = flag ? 1 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);
            // CraftBukkit end

            if (eventRedstone.getNewCurrent() > 0 && !(eventRedstone.getOldCurrent() > 0)) { // CraftBukkit
                TileEntity tileentity = world.getTileEntity(i, j, k);

                if (tileentity != null && tileentity instanceof TileEntityCommand) {
                    ((TileEntityCommand) tileentity).a(world);
                }

                world.setRawData(i, j, k, i1 | 1);
            } else if (!(eventRedstone.getNewCurrent() > 0) && eventRedstone.getOldCurrent() > 0) { // CraftBukkit
                world.setRawData(i, j, k, i1 & -2);
            }
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        TileEntityCommand tileentitycommand = (TileEntityCommand) world.getTileEntity(i, j, k);

        if (tileentitycommand != null) {
            entityhuman.a((TileEntity) tileentitycommand);
        }

        return true;
    }
}
