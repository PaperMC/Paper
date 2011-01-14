package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockChest extends BlockContainer {

    private Random a;

    protected BlockChest(int i) {
        super(i, Material.c);
        a = new Random();
        bh = 26;
    }

    public int a(int i) {
        if (i == 1) {
            return bh - 1;
        }
        if (i == 0) {
            return bh - 1;
        }
        if (i == 3) {
            return bh + 1;
        } else {
            return bh;
        }
    }

    public boolean a(World world, int i, int j, int k) {
        int l = 0;

        if (world.a(i - 1, j, k) == bi) {
            l++;
        }
        if (world.a(i + 1, j, k) == bi) {
            l++;
        }
        if (world.a(i, j, k - 1) == bi) {
            l++;
        }
        if (world.a(i, j, k + 1) == bi) {
            l++;
        }
        if (l > 1) {
            return false;
        }
        if (g(world, i - 1, j, k)) {
            return false;
        }
        if (g(world, i + 1, j, k)) {
            return false;
        }
        if (g(world, i, j, k - 1)) {
            return false;
        }
        return !g(world, i, j, k + 1);
    }

    private boolean g(World world, int i, int j, int k) {
        if (world.a(i, j, k) != bi) {
            return false;
        }
        if (world.a(i - 1, j, k) == bi) {
            return true;
        }
        if (world.a(i + 1, j, k) == bi) {
            return true;
        }
        if (world.a(i, j, k - 1) == bi) {
            return true;
        }
        return world.a(i, j, k + 1) == bi;
    }

    public void b(World world, int i, int j, int k) {
        TileEntityChest tileentitychest = (TileEntityChest) world.m(i, j, k);

        label0:
        for (int l = 0; l < ((IInventory) (tileentitychest)).h_(); l++) {
            ItemStack itemstack = ((IInventory) (tileentitychest)).a(l);

            if (itemstack == null) {
                continue;
            }
            float f = a.nextFloat() * 0.8F + 0.1F;
            float f1 = a.nextFloat() * 0.8F + 0.1F;
            float f2 = a.nextFloat() * 0.8F + 0.1F;

            do {
                if (itemstack.a <= 0) {
                    continue label0;
                }
                int i1 = a.nextInt(21) + 10;

                if (i1 > itemstack.a) {
                    i1 = itemstack.a;
                }
                itemstack.a -= i1;
                EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.c, i1, itemstack.h()));
                float f3 = 0.05F;

                entityitem.s = (float) a.nextGaussian() * f3;
                entityitem.t = (float) a.nextGaussian() * f3 + 0.2F;
                entityitem.u = (float) a.nextGaussian() * f3;
                world.a(((Entity) (entityitem)));
            } while (true);
        }

        super.b(world, i, j, k);
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        Object obj = (((TileEntityChest) world.m(i, j, k)));

        if (world.d(i, j + 1, k)) {
            return true;
        }
        if (world.a(i - 1, j, k) == bi && world.d(i - 1, j + 1, k)) {
            return true;
        }
        if (world.a(i + 1, j, k) == bi && world.d(i + 1, j + 1, k)) {
            return true;
        }
        if (world.a(i, j, k - 1) == bi && world.d(i, j + 1, k - 1)) {
            return true;
        }
        if (world.a(i, j, k + 1) == bi && world.d(i, j + 1, k + 1)) {
            return true;
        }
        if (world.a(i - 1, j, k) == bi) {
            obj = ((new InventoryLargeChest("Large chest", ((IInventory) ((TileEntityChest) world.m(i - 1, j, k))), ((IInventory) (obj)))));
        }
        if (world.a(i + 1, j, k) == bi) {
            obj = ((new InventoryLargeChest("Large chest", ((IInventory) (obj)), ((IInventory) ((TileEntityChest) world.m(i + 1, j, k))))));
        }
        if (world.a(i, j, k - 1) == bi) {
            obj = ((new InventoryLargeChest("Large chest", ((IInventory) ((TileEntityChest) world.m(i, j, k - 1))), ((IInventory) (obj)))));
        }
        if (world.a(i, j, k + 1) == bi) {
            obj = ((new InventoryLargeChest("Large chest", ((IInventory) (obj)), ((IInventory) ((TileEntityChest) world.m(i, j, k + 1))))));
        }
        if (world.z) {
            return true;
        } else {
            // CraftBukkit start - Interact Chest
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);

            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

            if (bie.isCancelled()) return true;
            // CraftBukkit end

            entityplayer.a(((IInventory) (obj)));
            return true;
        }
    }

    protected TileEntity a_() {
        return ((TileEntity) (new TileEntityChest()));
    }
}
