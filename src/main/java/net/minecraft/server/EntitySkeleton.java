package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public class EntitySkeleton extends EntityMonster {

    private static final ItemStack a = new ItemStack(Item.BOW, 1);

    public EntitySkeleton(World world) {
        super(world);
        this.texture = "/mob/skeleton.png";
    }

    public int getMaxHealth() {
        return 20;
    }

    protected String c_() {
        return "mob.skeleton";
    }

    protected String m() {
        return "mob.skeletonhurt";
    }

    protected String n() {
        return "mob.skeletonhurt";
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return super.damageEntity(damagesource, i);
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (damagesource.g() instanceof EntityArrow && damagesource.getEntity() instanceof EntityHuman) {
            EntityHuman entityhuman = (EntityHuman) damagesource.getEntity();
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locZ - this.locZ;

            if (d0 * d0 + d1 * d1 >= 2500.0D) {
                entityhuman.a((Statistic) AchievementList.v);
            }
        }
    }

    public void d() {
        if (this.world.e() && !this.world.isStatic) {
            float f = this.a(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.setOnFire(event.getDuration());
                }
                // CraftBukkit end
            }
        }

        super.d();
    }

    protected void a(Entity entity, float f) {
        if (f < 10.0F) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;

            if (this.attackTicks == 0) {
                EntityArrow entityarrow = new EntityArrow(this.world, this, 1.0F);
                double d2 = entity.locY + (double) entity.x() - 0.699999988079071D - entityarrow.locY;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1) * 0.2F;

                this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
                this.world.addEntity(entityarrow);
                entityarrow.a(d0, d2 + (double) f1, d1, 1.6F, 12.0F);
                this.attackTicks = 60;
            }

            this.yaw = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            this.e = true;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int e() {
        return Item.ARROW.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        int count = this.random.nextInt(3 + i);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.ARROW, count));
        }

        count = this.random.nextInt(3 + i);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.BONE, count));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public MonsterType getMonsterType() {
        return MonsterType.UNDEAD;
    }
}
