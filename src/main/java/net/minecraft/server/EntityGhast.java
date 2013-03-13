package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityGhast extends EntityFlying implements IMonster {

    public int b = 0;
    public double c;
    public double d;
    public double e;
    private Entity target = null;
    private int i = 0;
    public int f = 0;
    public int g = 0;
    private int explosionPower = 1;

    public EntityGhast(World world) {
        super(world);
        this.texture = "/mob/ghast.png";
        this.a(4.0F, 4.0F);
        this.fireProof = true;
        this.be = 5;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.isInvulnerable()) {
            return false;
        } else if ("fireball".equals(damagesource.n()) && damagesource.getEntity() instanceof EntityHuman) {
            super.damageEntity(damagesource, 1000);
            ((EntityHuman) damagesource.getEntity()).a((Statistic) AchievementList.y);
            return true;
        } else {
            return super.damageEntity(damagesource, i);
        }
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public int getMaxHealth() {
        return 10;
    }

    public void l_() {
        super.l_();
        byte b0 = this.datawatcher.getByte(16);

        this.texture = b0 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
    }

    protected void bq() {
        if (!this.world.isStatic && this.world.difficulty == 0) {
            this.die();
        }

        this.bn();
        this.f = this.g;
        double d0 = this.c - this.locX;
        double d1 = this.d - this.locY;
        double d2 = this.e - this.locZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0D || d3 > 3600.0D) {
            this.c = this.locX + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.d = this.locY + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.e = this.locZ + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.b-- <= 0) {
            this.b += this.random.nextInt(5) + 2;
            d3 = (double) MathHelper.sqrt(d3);
            if (this.a(this.c, this.d, this.e, d3)) {
                this.motX += d0 / d3 * 0.1D;
                this.motY += d1 / d3 * 0.1D;
                this.motZ += d2 / d3 * 0.1D;
            } else {
                this.c = this.locX;
                this.d = this.locY;
                this.e = this.locZ;
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

        if (this.target == null || this.i-- <= 0) {
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
                this.i = 20;
            }
        }

        double d4 = 64.0D;

        if (this.target != null && this.target.e((Entity) this) < d4 * d4) {
            double d5 = this.target.locX - this.locX;
            double d6 = this.target.boundingBox.b + (double) (this.target.length / 2.0F) - (this.locY + (double) (this.length / 2.0F));
            double d7 = this.target.locZ - this.locZ;

            this.ay = this.yaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.n(this.target)) {
                if (this.g == 10) {
                    this.world.a((EntityHuman) null, 1007, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                }

                ++this.g;
                if (this.g == 20) {
                    this.world.a((EntityHuman) null, 1008, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.world, this, d5, d6, d7);

                    // CraftBukkit - set yield when setting explosionpower
                    entitylargefireball.yield = entitylargefireball.e = this.explosionPower;
                    double d8 = 4.0D;
                    Vec3D vec3d = this.i(1.0F);

                    entitylargefireball.locX = this.locX + vec3d.c * d8;
                    entitylargefireball.locY = this.locY + (double) (this.length / 2.0F) + 0.5D;
                    entitylargefireball.locZ = this.locZ + vec3d.e * d8;
                    this.world.addEntity(entitylargefireball);
                    this.g = -40;
                }
            } else if (this.g > 0) {
                --this.g;
            }
        } else {
            this.ay = this.yaw = -((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F;
            if (this.g > 0) {
                --this.g;
            }
        }

        if (!this.world.isStatic) {
            byte b0 = this.datawatcher.getByte(16);
            byte b1 = (byte) (this.g > 10 ? 1 : 0);

            if (b0 != b1) {
                this.datawatcher.watch(16, Byte.valueOf(b1));
            }
        }
    }

    private boolean a(double d0, double d1, double d2, double d3) {
        double d4 = (this.c - this.locX) / d3;
        double d5 = (this.d - this.locY) / d3;
        double d6 = (this.e - this.locZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.clone();

        for (int i = 1; (double) i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (!this.world.getCubes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected String bb() {
        return "mob.ghast.moan";
    }

    protected String bc() {
        return "mob.ghast.scream";
    }

    protected String bd() {
        return "mob.ghast.death";
    }

    protected int getLootId() {
        return Item.SULPHUR.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(2) + this.random.nextInt(1 + i);

        int k;

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Item.GHAST_TEAR, j));
        }

        j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Item.SULPHUR, j));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected float ba() {
        return 10.0F;
    }

    public boolean canSpawn() {
        return this.random.nextInt(20) == 0 && super.canSpawn() && this.world.difficulty > 0;
    }

    public int by() {
        return 1;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("ExplosionPower", this.explosionPower);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("ExplosionPower")) {
            this.explosionPower = nbttagcompound.getInt("ExplosionPower");
        }
    }
}
