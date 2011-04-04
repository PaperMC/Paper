package net.minecraft.server;

import java.util.Random;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockSoil extends Block {

    protected BlockSoil(int i) {
        super(i, Material.EARTH);
        this.textureId = 87;
        this.a(true);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.e(255);
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return AxisAlignedBB.b((double) (i + 0), (double) (j + 0), (double) (k + 0), (double) (i + 1), (double) (j + 1), (double) (k + 1));
    }

    public boolean a() {
        return false;
    }

    public int a(int i, int j) {
        return i == 1 && j > 0 ? this.textureId - 1 : (i == 1 ? this.textureId : 2);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (random.nextInt(5) == 0) {
            if (this.h(world, i, j, k)) {
                world.c(i, j, k, 7);
            } else {
                int l = world.getData(i, j, k);

                if (l > 0) {
                    world.c(i, j, k, l - 1);
                } else if (!this.g(world, i, j, k)) {
                    world.e(i, j, k, Block.DIRT.id);
                }
            }
        }
    }

    public void b(World world, int i, int j, int k, Entity entity) {
        // CraftBukkit start - Interact Soil
        Cancellable cancellable;
        if (entity instanceof EntityHuman) {
            cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, Action.PHYSICAL, i, j, k, -1, null);
        }
        else {
            cancellable = new EntityInteractEvent(entity.getBukkitEntity(), ((WorldServer)world).getWorld().getBlockAt(i, j, k));
        }
        if (!cancellable.isCancelled()) {
            if (world.k.nextInt(4) == 0) {
                world.e(i, j, k, Block.DIRT.id);
            }
        }
        // Craftbukkit end
    }

    private boolean g(World world, int i, int j, int k) {
        byte b0 = 0;

        for (int l = i - b0; l <= i + b0; ++l) {
            for (int i1 = k - b0; i1 <= k + b0; ++i1) {
                if (world.getTypeId(l, j + 1, i1) == Block.CROPS.id) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean h(World world, int i, int j, int k) {
        for (int l = i - 4; l <= i + 4; ++l) {
            for (int i1 = j; i1 <= j + 1; ++i1) {
                for (int j1 = k - 4; j1 <= k + 4; ++j1) {
                    if (world.getMaterial(l, i1, j1) == Material.WATER) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void a(World world, int i, int j, int k, int l) {
        super.a(world, i, j, k, l);
        Material material = world.getMaterial(i, j + 1, k);

        if (material.isBuildable()) {
            world.e(i, j, k, Block.DIRT.id);
        }
    }

    public int a(int i, Random random) {
        return Block.DIRT.a(0, random);
    }
}
