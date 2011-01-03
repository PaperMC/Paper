package net.minecraft.server;

import org.bukkit.LivingEntity;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftEntity;
import org.bukkit.craftbukkit.CraftLivingEntity;
import org.bukkit.event.entity.EntityDamagedByBlockEvent;

import java.util.Random;

public class BlockCactus extends Block {

    protected BlockCactus(int i, int j) {
        super(i, j, Material.u);
        a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.e(i, j + 1, k)) {
            int l;

            for (l = 1; world.a(i, j - l, k) == bh; l++) {
                ;
            }
            if (l < 3) {
                int i1 = world.b(i, j, k);

                if (i1 == 15) {
                    world.d(i, j + 1, k, bh);
                    world.b(i, j, k, 0);
                } else {
                    world.b(i, j, k, i1 + 1);
                }
            }
        }
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        float f1 = 0.0625F;

        return AxisAlignedBB.b((float) i + f1, j, (float) k + f1, (float) (i + 1) - f1, (float) (j + 1) - f1, (float) (k + 1) - f1);
    }

    public int a(int i) {
        if (i == 1) {
            return bg - 1;
        }
        if (i == 0) {
            return bg + 1;
        } else {
            return bg;
        }
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        if (!super.a(world, i, j, k)) {
            return false;
        } else {
            return f(world, i, j, k);
        }
    }

    public void b(World world, int i, int j, int k, int l) {
        if (!f(world, i, j, k)) {
            a_(world, i, j, k, world.b(i, j, k));
            world.d(i, j, k, 0);
        }
    }

    public boolean f(World world, int i, int j, int k) {
        if (world.c(i - 1, j, k).a()) {
            return false;
        }
        if (world.c(i + 1, j, k).a()) {
            return false;
        }
        if (world.c(i, j, k - 1).a()) {
            return false;
        }
        if (world.c(i, j, k + 1).a()) {
            return false;
        } else {
            int l = world.a(i, j - 1, k);

            return l == Block.aV.bh || l == Block.E.bh;
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        // Craftbukkit: ENTITY_DAMAGEBY_BLOCK event
        CraftEntity toPassIn = null;
        if(entity instanceof EntityLiving)
        {
            toPassIn = new CraftLivingEntity(((WorldServer)world).getServer(), (EntityLiving)entity);
        }
        if(toPassIn != null)
        {
            EntityDamagedByBlockEvent edbbe = new EntityDamagedByBlockEvent(((WorldServer)world).getWorld().getBlockAt(i, j, k), toPassIn, 1);
            ((WorldServer)world).getServer().getPluginManager().callEvent(edbbe);
            if(edbbe.isCancelled()) return;
        }
        // Craftbukkit TODO: Other entities (when their respective classes are added) hitting a Cactus
        entity.a(null, 1);
    }
}

