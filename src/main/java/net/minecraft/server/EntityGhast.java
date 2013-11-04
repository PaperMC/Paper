package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityGhast extends EntityFlying implements IMonster {

    public int h;
    public double i;
    public double j;
    public double bn;
    private Entity target;
    private int br;
    public int bo;
    public int bp;
    private int explosionPower = 1;

    public EntityGhast(World world) {
        super(world);
        this.a(4.0F, 4.0F);
        this.fireProof = true;
        this.b = 5;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if ("fireball".equals(damagesource.p()) && damagesource.getEntity() instanceof EntityHuman) {
            super.damageEntity(damagesource, 1000.0F);
            ((EntityHuman) damagesource.getEntity()).a((Statistic) AchievementList.z);
            return true;
        } else {
            return super.damageEntity(damagesource, f);
        }
    }

    protected void c() {
        super.c();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.a).setValue(10.0D);
    }

    protected void bq() {
        if (!this.world.isStatic && this.world.difficulty == EnumDifficulty.PEACEFUL) {
            this.die();
        }

        this.w();
        this.bo = this.bp;
        double d0 = this.i - this.locX;
        double d1 = this.j - this.locY;
        double d2 = this.bn - this.locZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0D || d3 > 3600.0D) {
            this.i = this.locX + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.j = this.locY + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.bn = this.locZ + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.h-- <= 0) {
            this.h += this.random.nextInt(5) + 2;
            d3 = (double) MathHelper.sqrt(d3);
            if (this.a(this.i, this.j, this.bn, d3)) {
                this.motX += d0 / d3 * 0.1D;
                this.motY += d1 / d3 * 0.1D;
                this.motZ += d2 / d3 * 0.1D;
            } else {
                this.i = this.locX;
                this.j = this.locY;
                this.bn = this.locZ;
            }
        }

        if (this.target != null && this.target.dead) {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.target = null;
                } else {
                    this.target = ((CraftEntity) event.getTarget()).getHandle();
                }
            }
            // CraftBukkit end
        }

        if (this.target == null || this.br-- <= 0) {
            // CraftBukkit start
            Entity target = this.world.findNearbyVulnerablePlayer(this, 100.0D);
            if (target != null) {
                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.target = null;
                    } else {
                        this.target = ((CraftEntity) event.getTarget()).getHandle();
                    }
                }
            }
            // CraftBukkit end

            if (this.target != null) {
                this.br = 20;
            }
        }

        double d4 = 64.0D;

        if (this.target != null && this.target.e((Entity) this) < d4 * d4) {
            double d5 = this.target.locX - this.locX;
            double d6 = this.target.boundingBox.b + (double) (this.target.length / 2.0F) - (this.locY + (double) (this.length / 2.0F));
            double d7 = this.target.locZ - this.locZ;

            this.aN = this.yaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.o(this.target)) {
                if (this.bp == 10) {
                    this.world.a((EntityHuman) null, 1007, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                }

                ++this.bp;
                if (this.bp == 20) {
                    this.world.a((EntityHuman) null, 1008, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.world, this, d5, d6, d7);

                    // CraftBukkit - set bukkitYield when setting explosionpower
                    entitylargefireball.bukkitYield = entitylargefireball.yield  = this.explosionPower;
                    double d8 = 4.0D;
                    Vec3D vec3d = this.j(1.0F);

                    entitylargefireball.locX = this.locX + vec3d.c * d8;
                    entitylargefireball.locY = this.locY + (double) (this.length / 2.0F) + 0.5D;
                    entitylargefireball.locZ = this.locZ + vec3d.e * d8;
                    this.world.addEntity(entitylargefireball);
                    this.bp = -40;
                }
            } else if (this.bp > 0) {
                --this.bp;
            }
        } else {
            this.aN = this.yaw = -((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F;
            if (this.bp > 0) {
                --this.bp;
            }
        }

        if (!this.world.isStatic) {
            byte b0 = this.datawatcher.getByte(16);
            byte b1 = (byte) (this.bp > 10 ? 1 : 0);

            if (b0 != b1) {
                this.datawatcher.watch(16, Byte.valueOf(b1));
            }
        }
    }

    private boolean a(double d0, double d1, double d2, double d3) {
        double d4 = (this.i - this.locX) / d3;
        double d5 = (this.j - this.locY) / d3;
        double d6 = (this.bn - this.locZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.clone();

        for (int i = 1; (double) i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (!this.world.getCubes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected String t() {
        return "mob.ghast.moan";
    }

    protected String aT() {
        return "mob.ghast.scream";
    }

    protected String aU() {
        return "mob.ghast.death";
    }

    protected Item getLoot() {
        return Items.SULPHUR;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(2) + this.random.nextInt(1 + i);

        int k;

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Items.GHAST_TEAR, j));
        }

        j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Items.SULPHUR, j));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected float bf() {
        return 10.0F;
    }

    public boolean canSpawn() {
        return this.random.nextInt(20) == 0 && super.canSpawn() && this.world.difficulty != EnumDifficulty.PEACEFUL;
    }

    public int bz() {
        return 1;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("ExplosionPower", this.explosionPower);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
            this.explosionPower = nbttagcompound.getInt("ExplosionPower");
        }
    }
}
