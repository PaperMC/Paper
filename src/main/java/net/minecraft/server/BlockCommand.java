package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockCommand extends BlockContainer {

    public BlockCommand(int i) {
        super(i, Material.ORE);
    }

    public TileEntity b(World world) {
        return new TileEntityCommand();
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            boolean flag = world.isBlockIndirectlyPowered(i, j, k);
            int i1 = world.getData(i, j, k);
            boolean flag1 = (i1 & 1) != 0;

            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = flag1 ? 15 : 0;
            int current = flag ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);
            // CraftBukkit end

            if (eventRedstone.getNewCurrent() > 0 && !(eventRedstone.getOldCurrent() > 0)) { // CraftBukkit
                world.setData(i, j, k, i1 | 1, 4);
                world.a(i, j, k, this.id, this.a(world));
            } else if (!(eventRedstone.getNewCurrent() > 0) && eventRedstone.getOldCurrent() > 0) { // CraftBukkit
                world.setData(i, j, k, i1 & -2, 4);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        TileEntity tileentity = world.getTileEntity(i, j, k);

        if (tileentity != null && tileentity instanceof TileEntityCommand) {
            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;

            tileentitycommand.a(tileentitycommand.a(world));
            world.m(i, j, k, this.id);
        }
    }

    public int a(World world) {
        return 1;
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        TileEntityCommand tileentitycommand = (TileEntityCommand) world.getTileEntity(i, j, k);

        if (tileentitycommand != null) {
            entityhuman.a((TileEntity) tileentitycommand);
        }

        return true;
    }

    public boolean q_() {
        return true;
    }

    public int b_(World world, int i, int j, int k, int l) {
        TileEntity tileentity = world.getTileEntity(i, j, k);

        return tileentity != null && tileentity instanceof TileEntityCommand ? ((TileEntityCommand) tileentity).f() : 0;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        TileEntityCommand tileentitycommand = (TileEntityCommand) world.getTileEntity(i, j, k);

        if (itemstack.hasName()) {
            tileentitycommand.b(itemstack.getName());
        }
    }

    public int a(Random random) {
        return 0;
    }
}
