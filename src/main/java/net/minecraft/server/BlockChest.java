package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockChest extends BlockContainer {

    private Random a = new Random();

    protected BlockChest(int i) {
        super(i, Material.WOOD);
        this.textureId = 26;
    }

    public int a(int i) {
        return i == 1 ? this.textureId - 1 : (i == 0 ? this.textureId - 1 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean a(World world, int i, int j, int k) {
        int l = 0;

        if (world.getTypeId(i - 1, j, k) == this.id) {
            ++l;
        }

        if (world.getTypeId(i + 1, j, k) == this.id) {
            ++l;
        }

        if (world.getTypeId(i, j, k - 1) == this.id) {
            ++l;
        }

        if (world.getTypeId(i, j, k + 1) == this.id) {
            ++l;
        }

        return l > 1 ? false : (this.g(world, i - 1, j, k) ? false : (this.g(world, i + 1, j, k) ? false : (this.g(world, i, j, k - 1) ? false : !this.g(world, i, j, k + 1))));
    }

    private boolean g(World world, int i, int j, int k) {
        return world.getTypeId(i, j, k) != this.id ? false : (world.getTypeId(i - 1, j, k) == this.id ? true : (world.getTypeId(i + 1, j, k) == this.id ? true : (world.getTypeId(i, j, k - 1) == this.id ? true : world.getTypeId(i, j, k + 1) == this.id)));
    }

    public void b(World world, int i, int j, int k) {
        TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(i, j, k);

        for (int l = 0; l < tileentitychest.h_(); ++l) {
            ItemStack itemstack = tileentitychest.a(l);

            if (itemstack != null) {
                float f = this.a.nextFloat() * 0.8F + 0.1F;
                float f1 = this.a.nextFloat() * 0.8F + 0.1F;
                float f2 = this.a.nextFloat() * 0.8F + 0.1F;

                while (itemstack.count > 0) {
                    int i1 = this.a.nextInt(21) + 10;

                    if (i1 > itemstack.count) {
                        i1 = itemstack.count;
                    }

                    itemstack.count -= i1;
                    EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, i1, itemstack.h()));
                    float f3 = 0.05F;

                    entityitem.motX = (double) ((float) this.a.nextGaussian() * f3);
                    entityitem.motY = (double) ((float) this.a.nextGaussian() * f3 + 0.2F);
                    entityitem.motZ = (double) ((float) this.a.nextGaussian() * f3);
                    world.a((Entity) entityitem);
                }
            }
        }

        super.b(world, i, j, k);
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        Object object = (TileEntityChest) world.getTileEntity(i, j, k);

        if (world.d(i, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i - 1, j, k) == this.id && world.d(i - 1, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i + 1, j, k) == this.id && world.d(i + 1, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i, j, k - 1) == this.id && world.d(i, j + 1, k - 1)) {
            return true;
        } else if (world.getTypeId(i, j, k + 1) == this.id && world.d(i, j + 1, k + 1)) {
            return true;
        } else {
            if (world.getTypeId(i - 1, j, k) == this.id) {
                object = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(i - 1, j, k), (IInventory) object);
            }

            if (world.getTypeId(i + 1, j, k) == this.id) {
                object = new InventoryLargeChest("Large chest", (IInventory) object, (TileEntityChest) world.getTileEntity(i + 1, j, k));
            }

            if (world.getTypeId(i, j, k - 1) == this.id) {
                object = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(i, j, k - 1), (IInventory) object);
            }

            if (world.getTypeId(i, j, k + 1) == this.id) {
                object = new InventoryLargeChest("Large chest", (IInventory) object, (TileEntityChest) world.getTileEntity(i, j, k + 1));
            }

            if (world.isStatic) {
                return true;
            } else {
                // CraftBukkit start - Interact Chest
                CraftWorld craftWorld = ((WorldServer) world).getWorld();
                CraftServer server = ((WorldServer) world).getServer();
                Type eventType = Type.BLOCK_INTERACT;
                CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
                LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

                BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) return true;
                // CraftBukkit end

                entityhuman.a((IInventory) object);
                return true;
            }
        }
    }

    protected TileEntity a_() {
        return new TileEntityChest();
    }
}
