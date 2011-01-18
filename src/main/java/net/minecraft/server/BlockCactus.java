package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.LivingEntity;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
// CraftBukkit end

import java.util.Random;

public class BlockCactus extends Block {

    protected BlockCactus(int i, int j) {
        super(i, j, Material.u);
        a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.e(i, j + 1, k)) {
            int l;

            for (l = 1; world.a(i, j - l, k) == bi; l++) {
                ;
            }
            if (l < 3) {
                int i1 = world.b(i, j, k);

                if (i1 == 15) {
                    world.e(i, j + 1, k, bi);
                    world.c(i, j, k, 0);
                } else {
                    world.c(i, j, k, i1 + 1);
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
            return bh - 1;
        }
        if (i == 0) {
            return bh + 1;
        } else {
            return bh;
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
            world.e(i, j, k, 0);
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

            return l == Block.aV.bi || l == Block.E.bi;
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        // CraftBukkit start - ENTITY_DAMAGEBY_BLOCK event

        if(entity instanceof EntityLiving) {
            CraftServer server = ((WorldServer) world).getServer();
            org.bukkit.block.Block damager = ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Entity damagee = entity.getBukkitEntity();
            DamageCause damageType = EntityDamageEvent.DamageCause.CONTACT;
            int damageDone = 1;

            EntityDamageByBlockEvent edbbe = new EntityDamageByBlockEvent(damager, damagee, damageType, damageDone);
            server.getPluginManager().callEvent(edbbe);

            if (!edbbe.isCancelled()){
                entity.a(((Entity) (null)), edbbe.getDamage());
            }
            return;
        } else {
            entity.a(((Entity) (null)), 1);
        }
     // CraftBukkit end
    }
}
