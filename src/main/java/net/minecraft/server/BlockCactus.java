package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.entity.EntityDamageByBlockEvent; // CraftBukkit

public class BlockCactus extends Block {

    protected BlockCactus(int i, int j) {
        super(i, j, Material.CACTUS);
        this.b(true);
        this.a(CreativeModeTab.c);
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (world.isEmpty(i, j + 1, k)) {
            int l;

            for (l = 1; world.getTypeId(i, j - l, k) == this.id; ++l) {
                ;
            }

            if (l < 3) {
                int i1 = world.getData(i, j, k);

                if (i1 == 15) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j + 1, k, this.id, 0); // CraftBukkit
                    world.setData(i, j, k, 0);
                } else {
                    world.setData(i, j, k, i1 + 1);
                }
            }
        }
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        float f = 0.0625F;

        return AxisAlignedBB.a().a((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) (j + 1) - f), (double) ((float) (k + 1) - f));
    }

    public int a(int i) {
        return i == 1 ? this.textureId - 1 : (i == 0 ? this.textureId + 1 : this.textureId);
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int d() {
        return 13;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !super.canPlace(world, i, j, k) ? false : this.d(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.d(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
        }
    }

    public boolean d(World world, int i, int j, int k) {
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
        // CraftBukkit start - EntityDamageByBlock event
        if (entity instanceof EntityLiving) {
            org.bukkit.block.Block damager = world.getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();

            EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, org.bukkit.event.entity.EntityDamageEvent.DamageCause.CONTACT, 1);
            world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                damagee.setLastDamageCause(event);
                entity.damageEntity(DamageSource.CACTUS, event.getDamage());
            }
            return;
        }
        // CraftBukkit end

        entity.damageEntity(DamageSource.CACTUS, 1);
    }
}
