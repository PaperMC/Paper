package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityWither extends EntityMonster implements IRangedEntity {

    private float[] bp = new float[2];
    private float[] bq = new float[2];
    private float[] br = new float[2];
    private float[] bs = new float[2];
    private int[] bt = new int[2];
    private int[] bu = new int[2];
    private int bv;
    private static final IEntitySelector bw = new EntitySelectorNotUndead();

    public EntityWither(World world) {
        super(world);
        this.setHealth(this.getMaxHealth());
        this.a(0.9F, 4.0F);
        this.fireProof = true;
        this.getNavigation().e(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, false, bw));
        this.b = 50;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(0));
        this.datawatcher.a(19, new Integer(0));
        this.datawatcher.a(20, new Integer(0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Invul", this.bU());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.p(nbttagcompound.getInt("Invul"));
    }

    protected String r() {
        return "mob.wither.idle";
    }

    protected String aO() {
        return "mob.wither.hurt";
    }

    protected String aP() {
        return "mob.wither.death";
    }

    public void c() {
        this.motY *= 0.6000000238418579D;
        double d0;
        double d1;
        double d2;

        if (!this.world.isStatic && this.q(0) > 0) {
            Entity entity = this.world.getEntity(this.q(0));

            if (entity != null) {
                if (this.locY < entity.locY || !this.bV() && this.locY < entity.locY + 5.0D) {
                    if (this.motY < 0.0D) {
                        this.motY = 0.0D;
                    }

                    this.motY += (0.5D - this.motY) * 0.6000000238418579D;
                }

                double d3 = entity.locX - this.locX;

                d0 = entity.locZ - this.locZ;
                d1 = d3 * d3 + d0 * d0;
                if (d1 > 9.0D) {
                    d2 = (double) MathHelper.sqrt(d1);
                    this.motX += (d3 / d2 * 0.5D - this.motX) * 0.6000000238418579D;
                    this.motZ += (d0 / d2 * 0.5D - this.motZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806D) {
            this.yaw = (float) Math.atan2(this.motZ, this.motX) * 57.295776F - 90.0F;
        }

        super.c();

        int i;

        for (i = 0; i < 2; ++i) {
            this.bs[i] = this.bq[i];
            this.br[i] = this.bp[i];
        }

        int j;

        for (i = 0; i < 2; ++i) {
            j = this.q(i + 1);
            Entity entity1 = null;

            if (j > 0) {
                entity1 = this.world.getEntity(j);
            }

            if (entity1 != null) {
                d0 = this.r(i + 1);
                d1 = this.s(i + 1);
                d2 = this.t(i + 1);
                double d4 = entity1.locX - d0;
                double d5 = entity1.locY + (double) entity1.getHeadHeight() - d1;
                double d6 = entity1.locZ - d2;
                double d7 = (double) MathHelper.sqrt(d4 * d4 + d6 * d6);
                float f = (float) (Math.atan2(d6, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f1 = (float) (-(Math.atan2(d5, d7) * 180.0D / 3.1415927410125732D));

                this.bp[i] = this.b(this.bp[i], f1, 40.0F);
                this.bq[i] = this.b(this.bq[i], f, 10.0F);
            } else {
                this.bq[i] = this.b(this.bq[i], this.aN, 10.0F);
            }
        }

        boolean flag = this.bV();

        for (j = 0; j < 3; ++j) {
            double d8 = this.r(j);
            double d9 = this.s(j);
            double d10 = this.t(j);

            this.world.addParticle("smoke", d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
            if (flag && this.world.random.nextInt(4) == 0) {
                this.world.addParticle("mobSpell", d8 + this.random.nextGaussian() * 0.30000001192092896D, d9 + this.random.nextGaussian() * 0.30000001192092896D, d10 + this.random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (this.bU() > 0) {
            for (j = 0; j < 3; ++j) {
                this.world.addParticle("mobSpell", this.locX + this.random.nextGaussian() * 1.0D, this.locY + (double) (this.random.nextFloat() * 3.3F), this.locZ + this.random.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    protected void bi() {
        int i;

        if (this.bU() > 0) {
            i = this.bU() - 1;
            if (i <= 0) {
                // CraftBukkit start
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.createExplosion(this, this.locX, this.locY + (double) this.getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }
                // CraftBukkit end

                this.world.createExplosion(this, this.locX, this.locY + (double) this.getHeadHeight(), this.locZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.d(1013, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
            }

            this.p(i);
            if (this.ticksLived % 10 == 0) {
                this.heal(10.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER_SPAWN); // CraftBukkit
            }
        } else {
            super.bi();

            int j;

            for (i = 1; i < 3; ++i) {
                if (this.ticksLived >= this.bt[i - 1]) {
                    this.bt[i - 1] = this.ticksLived + 10 + this.random.nextInt(10);
                    if (this.world.difficulty >= 2) {
                        int i1001 = i - 1;
                        int i1003 = this.bu[i - 1];

                        this.bu[i1001] = this.bu[i - 1] + 1;
                        if (i1003 > 15) {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.a(this.random, this.locX - (double) f, this.locX + (double) f);
                            double d1 = MathHelper.a(this.random, this.locY - (double) f1, this.locY + (double) f1);
                            double d2 = MathHelper.a(this.random, this.locZ - (double) f, this.locZ + (double) f);

                            this.a(i + 1, d0, d1, d2, true);
                            this.bu[i - 1] = 0;
                        }
                    }

                    j = this.q(i);
                    if (j > 0) {
                        Entity entity = this.world.getEntity(j);

                        if (entity != null && entity.isAlive() && this.e(entity) <= 900.0D && this.o(entity)) {
                            this.a(i + 1, (EntityLiving) entity);
                            this.bt[i - 1] = this.ticksLived + 40 + this.random.nextInt(20);
                            this.bu[i - 1] = 0;
                        } else {
                            this.c(i, 0);
                        }
                    } else {
                        List list = this.world.a(EntityLiving.class, this.boundingBox.grow(20.0D, 8.0D, 20.0D), bw);

                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1) {
                            EntityLiving entityliving = (EntityLiving) list.get(this.random.nextInt(list.size()));

                            if (entityliving != this && entityliving.isAlive() && this.o(entityliving)) {
                                if (entityliving instanceof EntityHuman) {
                                    if (!((EntityHuman) entityliving).abilities.isInvulnerable) {
                                        this.c(i, entityliving.id);
                                    }
                                } else {
                                    this.c(i, entityliving.id);
                                }
                                break;
                            }

                            list.remove(entityliving);
                        }
                    }
                }
            }

            if (this.getGoalTarget() != null) {
                this.c(0, this.getGoalTarget().id);
            } else {
                this.c(0, 0);
            }

            if (this.bv > 0) {
                --this.bv;
                if (this.bv == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    i = MathHelper.floor(this.locY);
                    j = MathHelper.floor(this.locX);
                    int j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;

                    for (int k1 = -1; k1 <= 1; ++k1) {
                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                int i3 = this.world.getTypeId(j2, k2, l2);

                                if (i3 > 0 && i3 != Block.BEDROCK.id && i3 != Block.ENDER_PORTAL.id && i3 != Block.ENDER_PORTAL_FRAME.id) {
                                    // CraftBukkit start
                                    if (CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, 0, 0).isCancelled()) {
                                        continue;
                                    }
                                    // CraftBukkit end

                                    flag = this.world.setAir(j2, k2, l2, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag) {
                        this.world.a((EntityHuman) null, 1012, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                    }
                }
            }

            if (this.ticksLived % 20 == 0) {
                this.heal(1.0F);
            }
        }
    }

    public void bT() {
        this.p(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    public void am() {}

    public int aQ() {
        return 4;
    }

    private double r(int i) {
        if (i <= 0) {
            return this.locX;
        } else {
            float f = (this.aN + (float) (180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.cos(f);

            return this.locX + (double) f1 * 1.3D;
        }
    }

    private double s(int i) {
        return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
    }

    private double t(int i) {
        if (i <= 0) {
            return this.locZ;
        } else {
            float f = (this.aN + (float) (180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.sin(f);

            return this.locZ + (double) f1 * 1.3D;
        }
    }

    private float b(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    private void a(int i, EntityLiving entityliving) {
        this.a(i, entityliving.locX, entityliving.locY + (double) entityliving.getHeadHeight() * 0.5D, entityliving.locZ, i == 0 && this.random.nextFloat() < 0.001F);
    }

    private void a(int i, double d0, double d1, double d2, boolean flag) {
        this.world.a((EntityHuman) null, 1014, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
        double d3 = this.r(i);
        double d4 = this.s(i);
        double d5 = this.t(i);
        double d6 = d0 - d3;
        double d7 = d1 - d4;
        double d8 = d2 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);

        if (flag) {
            entitywitherskull.a(true);
        }

        entitywitherskull.locY = d4;
        entitywitherskull.locX = d3;
        entitywitherskull.locZ = d5;
        this.world.addEntity(entitywitherskull);
    }

    public void a(EntityLiving entityliving, float f) {
        this.a(0, entityliving);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (damagesource == DamageSource.DROWN) {
            return false;
        } else if (this.bU() > 0) {
            return false;
        } else {
            Entity entity;

            if (this.bV()) {
                entity = damagesource.h();
                if (entity instanceof EntityArrow) {
                    return false;
                }
            }

            entity = damagesource.getEntity();
            if (entity != null && !(entity instanceof EntityHuman) && entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == this.getMonsterType()) {
                return false;
            } else {
                if (this.bv <= 0) {
                    this.bv = 20;
                }

                for (int i = 0; i < this.bu.length; ++i) {
                    this.bu[i] += 3;
                }

                return super.damageEntity(damagesource, f);
            }
        }
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        loot.add(new org.bukkit.inventory.ItemStack(Item.NETHER_STAR.id, 1));
        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected void u() {
        this.aV = 0;
    }

    public boolean L() {
        return !this.dead;
    }

    protected void b(float f) {}

    public void addEffect(MobEffect mobeffect) {}

    protected boolean bf() {
        return true;
    }

    protected void az() {
        super.az();
        this.getAttributeInstance(GenericAttributes.a).setValue(300.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.6000000238418579D);
        this.getAttributeInstance(GenericAttributes.b).setValue(40.0D);
    }

    public int bU() {
        return this.datawatcher.getInt(20);
    }

    public void p(int i) {
        this.datawatcher.watch(20, Integer.valueOf(i));
    }

    public int q(int i) {
        return this.datawatcher.getInt(17 + i);
    }

    public void c(int i, int j) {
        this.datawatcher.watch(17 + i, Integer.valueOf(j));
    }

    public boolean bV() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    public void mount(Entity entity) {
        this.vehicle = null;
    }
}
