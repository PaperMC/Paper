package net.minecraft.server;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockInteractEvent;

import java.util.Random;

public class BlockBed extends Block {

    public static final int[][] a = new int[][] { { 0, 1}, { -1, 0}, { 0, -1}, { 1, 0}};

    public BlockBed(int i) {
        super(i, 134, Material.CLOTH);
        this.f();
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        int l = world.getData(i, j, k);

        if (!d(l)) {
            int i1 = c(l);

            i += a[i1][0];
            k += a[i1][1];
            if (world.getTypeId(i, j, k) != this.id) {
                return true;
            }

            l = world.getData(i, j, k);
        }

        // CraftBukkit start - Interact Bed
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer server = ((WorldServer) world).getServer();
        Event.Type eventType = Event.Type.BLOCK_INTERACT;
        CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
        LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

        BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }
        // CraftBukkit end

        if (f(l)) {
            entityhuman.a("tile.bed.occupied");
            return true;
        } else if (entityhuman.a(i, j, k)) {
            a(world, i, j, k, true);
            return true;
        } else {
            entityhuman.a("tile.bed.noSleep");
            return true;
        }
    }

    public int a(int i, int j) {
        if (i == 0) {
            return Block.WOOD.textureId;
        } else {
            int k = c(j);
            int l = BedBlockTextures.c[k][i];

            return d(j) ? (l == 2 ? this.textureId + 2 + 16 : (l != 5 && l != 4 ? this.textureId + 1 : this.textureId + 1 + 16)) : (l == 3 ? this.textureId - 1 + 16 : (l != 5 && l != 4 ? this.textureId : this.textureId + 16));
        }
    }

    public boolean a() {
        return false;
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        this.f();
    }

    public void a(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = c(i1);

        if (d(i1)) {
            if (world.getTypeId(i - a[j1][0], j, k - a[j1][1]) != this.id) {
                world.e(i, j, k, 0);
            }
        } else if (world.getTypeId(i + a[j1][0], j, k + a[j1][1]) != this.id) {
            world.e(i, j, k, 0);
            if (!world.isStatic) {
                this.b_(world, i, j, k, i1);
            }
        }
    }

    public int a(int i, Random random) {
        return d(i) ? 0 : Item.BED.id;
    }

    private void f() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    public static int c(int i) {
        return i & 3;
    }

    public static boolean d(int i) {
        return (i & 8) != 0;
    }

    public static boolean f(int i) {
        return (i & 4) != 0;
    }

    public static void a(World world, int i, int j, int k, boolean flag) {
        int l = world.getData(i, j, k);

        if (flag) {
            l |= 4;
        } else {
            l &= -5;
        }

        world.c(i, j, k, l);
    }

    public static ChunkCoordinates g(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = c(i1);

        for (int k1 = 0; k1 <= 1; ++k1) {
            int l1 = i - a[j1][0] * k1 - 1;
            int i2 = k - a[j1][1] * k1 - 1;
            int j2 = l1 + 2;
            int k2 = i2 + 2;

            for (int l2 = l1; l2 <= j2; ++l2) {
                for (int i3 = i2; i3 <= k2; ++i3) {
                    if (world.d(l2, j - 1, i3) && world.isEmpty(l2, j, i3) && world.isEmpty(l2, j + 1, i3)) {
                        if (l <= 0) {
                            return new ChunkCoordinates(l2, j, i3);
                        }

                        --l;
                    }
                }
            }
        }

        return new ChunkCoordinates(i, j + 1, k);
    }
}
