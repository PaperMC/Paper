package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
// CraftBukkit end

import java.util.Random;

public class BlockCactus extends Block {

    protected BlockCactus(int i, int j) {
        super(i, j, Material.CACTUS);
        this.a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.isEmpty(i, j + 1, k)) {
            int l;

            for (l = 1; world.getTypeId(i, j - l, k) == this.id; ++l) {
                ;
            }

            if (l < 3) {
                int i1 = world.getData(i, j, k);

                if (i1 == 15) {
                    world.e(i, j + 1, k, this.id);
                    world.c(i, j, k, 0);
                } else {
                    world.c(i, j, k, i1 + 1);
                }
            }
        }
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        float f = 0.0625F;

        return AxisAlignedBB.b((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) (j + 1) - f), (double) ((float) (k + 1) - f));
    }

    public int a(int i) {
        return i == 1 ? this.textureId - 1 : (i == 0 ? this.textureId + 1 : this.textureId);
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        return !super.a(world, i, j, k) ? false : this.f(world, i, j, k);
    }

    public void a(World world, int i, int j, int k, int l) {
        if (!this.f(world, i, j, k)) {
            this.b_(world, i, j, k, world.getData(i, j, k));
            world.e(i, j, k, 0);
        }
    }

    public boolean f(World world, int i, int j, int k) {
        if (world.getMaterial(i - 1, j, k).isBuildable()) {
            return false;
        } else if (world.getMaterial(i + 1, j, k).isBuildable()) {
            return false;
        } else if (world.getMaterial(i, j, k - 1).isBuildable()) {
            return false;
        } else if (world.getMaterial(i, j, k + 1).isBuildable()) {
            return false;
        } else {
            int l = world.getTypeId(i, j - 1, k);

            return l == Block.CACTUS.id || l == Block.SAND.id;
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        // CraftBukkit start - ENTITY_DAMAGEBY_BLOCK event
        if(entity instanceof EntityLiving) {
            CraftServer server = ((WorldServer) world).getServer();
            org.bukkit.block.Block damager = ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Entity damagee = (entity == null)?null:entity.getBukkitEntity();
            DamageCause damageType = EntityDamageEvent.DamageCause.CONTACT;
            int damageDone = 1;

            EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, damageType, damageDone);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()){
                entity.a((Entity) null, event.getDamage());
            }
            return;
        }
        // CraftBukkit end
        entity.a((Entity) null, 1);
    }
}
