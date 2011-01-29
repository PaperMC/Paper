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

public class BlockFurnace extends BlockContainer {

    private final boolean a;

    protected BlockFurnace(int i, boolean flag) {
        super(i, Material.STONE);
        this.a = flag;
        this.textureId = 45;
    }

    public int a(int i, Random random) {
        return Block.FURNACE.id;
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        this.g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);
        byte b0 = 3;

        if (Block.o[l] && !Block.o[i1]) {
            b0 = 3;
        }

        if (Block.o[i1] && !Block.o[l]) {
            b0 = 2;
        }

        if (Block.o[j1] && !Block.o[k1]) {
            b0 = 5;
        }

        if (Block.o[k1] && !Block.o[j1]) {
            b0 = 4;
        }

        world.c(i, j, k, b0);
    }

    public int a(int i) {
        return i == 1 ? this.textureId + 17 : (i == 0 ? this.textureId + 17 : (i == 3 ? this.textureId - 1 : this.textureId));
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            // CraftBukkit start - Interact Furnace
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

            BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            TileEntityFurnace tileentityfurnace = (TileEntityFurnace) world.getTileEntity(i, j, k);

            entityhuman.a(tileentityfurnace);
            return true;
        }
    }

    public static void a(boolean flag, World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        TileEntity tileentity = world.getTileEntity(i, j, k);

        if (flag) {
            world.e(i, j, k, Block.BURNING_FURNACE.id);
        } else {
            world.e(i, j, k, Block.FURNACE.id);
        }

        world.c(i, j, k, l);
        world.setTileEntity(i, j, k, tileentity);
    }

    protected TileEntity a_() {
        return new TileEntityFurnace();
    }

    public void a(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.b((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.c(i, j, k, 2);
        }

        if (l == 1) {
            world.c(i, j, k, 5);
        }

        if (l == 2) {
            world.c(i, j, k, 3);
        }

        if (l == 3) {
            world.c(i, j, k, 4);
        }
    }
}
