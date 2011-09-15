package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float S;
    public float T;
    public float U = 0.0F;
    public float V = 0.0F;
    protected float W;
    protected float X;
    protected float Y;
    protected float Z;
    protected boolean aa = true;
    protected String texture = "/mob/char.png";
    protected boolean ac = true;
    protected float ad = 0.0F;
    protected String ae = null;
    protected float af = 1.0F;
    protected int ag = 0;
    protected float ah = 0.0F;
    public boolean ai = false;
    public float aj = 0.1F;
    public float ak = 0.02F;
    public float al;
    public float am;
    public int health = 10;
    public int ao;
    private int a;
    public int hurtTicks;
    public int aq;
    public float ar = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float au;
    public float av;
    protected boolean aw = false;
    protected int ax;
    public int ay = -1;
    public float az = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aA;
    public float aB;
    public float aC;
    private EntityHuman b = null;
    private int c = 0;
    public int aD = 0;
    public int aE = 0;
    protected HashMap aF = new HashMap();
    protected int aG;
    protected double aH;
    protected double aI;
    protected double aJ;
    protected double aK;
    protected double aL;
    float aM = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int aO = 0;
    protected float aP;
    protected float aQ;
    protected float aR;
    protected boolean aS = false;
    protected float aT = 0.0F;
    protected float aU = 0.7F;
    private Entity d;
    protected int aV = 0;

    public EntityLiving(World world) {
        super(world);
        this.aY = true;
        this.T = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.S = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.bI = 0.5F;
    }

    protected void b() {}

    public boolean f(Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + (double) this.t(), this.locZ), Vec3D.create(entity.locX, entity.locY + (double) entity.t(), entity.locZ)) == null;
    }

    public boolean r_() {
        return !this.dead;
    }

    public boolean g() {
        return !this.dead;
    }

    public float t() {
        return this.width * 0.85F;
    }

    public int e() {
        return 80;
    }

    public void Z() {
        String s = this.h();

        if (s != null) {
            this.world.makeSound(this, s, this.l(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public void aa() {
        this.al = this.am;
        super.aa();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.e();
            this.Z();
        }

        if (this.ac() && this.O()) {
            // CraftBukkit start
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.SUFFOCATION, 1);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.damageEntity(DamageSource.STUCK, event.getDamage());
            }
            // CraftBukkit end
        }

        if (this.fireProof || this.world.isStatic) {
            this.fireTicks = 0;
        }

        int i;

        if (this.ac() && this.a(Material.WATER) && !this.b_() && !this.aF.containsKey(Integer.valueOf(MobEffectList.o.H))) {
            --this.airTicks;
            if (this.airTicks == -20) {
                this.airTicks = 0;

                for (i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.world.a("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }

                // CraftBukkit start
                EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.DROWNING, 2);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled() && event.getDamage() != 0) {
                    this.damageEntity(DamageSource.DROWN, event.getDamage());
                }
                // CraftBukkit end
            }

            this.fireTicks = 0;
        } else {
            this.airTicks = this.maxAirTicks;
        }

        this.au = this.av;
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }

        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }

        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        if (this.health <= 0) {
            ++this.deathTicks;
            if (this.deathTicks > 20) {
                if (this.c > 0 || this.X()) {
                    i = this.a(this.b);

                    while (i > 0) {
                        int j = EntityExperienceOrb.b(i);

                        i -= j;
                        this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                    }
                }

                this.ag();
                this.die();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
                }
            }
        }

        if (this.c > 0) {
            --this.c;
        } else {
            this.b = null;
        }

        this.aj();
        this.Z = this.Y;
        this.V = this.U;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    protected int a(EntityHuman entityhuman) {
        return this.ax;
    }

    protected boolean X() {
        return false;
    }

    public void ab() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.width) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d2 * d3, d0, d1, d2);
        }
    }

    public void I() {
        super.I();
        this.W = this.X;
        this.X = 0.0F;
    }

    public void s_() {
        super.s_();
        if (this.aD > 0) {
            if (this.aE <= 0) {
                this.aE = 60;
            }

            --this.aE;
            if (this.aE <= 0) {
                --this.aD;
            }
        }

        this.s();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.U;
        float f2 = 0.0F;

        this.W = this.X;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.am > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.X += (f3 - this.X) * 0.3F;

        float f4;

        for (f4 = f1 - this.U; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.U += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.U; f5 < -180.0F; f5 += 360.0F) {
            ;
        }

        while (f5 >= 180.0F) {
            f5 -= 360.0F;
        }

        boolean flag = f5 < -90.0F || f5 >= 90.0F;

        if (f5 < -75.0F) {
            f5 = -75.0F;
        }

        if (f5 >= 75.0F) {
            f5 = 75.0F;
        }

        this.U = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.U += f5 * 0.2F;
        }

        if (flag) {
            f2 *= -1.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.U - this.V < -180.0F) {
            this.V -= 360.0F;
        }

        while (this.U - this.V >= 180.0F) {
            this.V += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.Y += f2;
    }

    protected void b(float f, float f1) {
        super.b(f, f1);
    }

    // CraftBukkit start - delegate so we can handle providing a reason for health being regained
    public void c(int i) {
        c(i, RegainReason.CUSTOM);
    }

    public void c(int i, RegainReason regainReason) {
        if (this.health > 0) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i, regainReason);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.health += event.getAmount();
            }
            // CraftBukkit end
            if (this.health > 20) {
                this.health = 20;
            }

            this.noDamageTicks = this.maxNoDamageTicks / 2;
        }
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.aO = 0;
            if (this.health <= 0) {
                return false;
            } else {
                this.aB = 1.5F;
                boolean flag = true;

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.b(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.ao = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.b(damagesource, i);
                    this.hurtTicks = this.aq = 10;
                }

                this.ar = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityHuman) {
                        this.c = 60;
                        this.b = (EntityHuman) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.c = 60;
                            this.b = null;
                        }
                    }
                }

                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.aq();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.ar = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.ar = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.j(), this.l(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.world.makeSound(this, this.i(), this.l(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                return true;
            }
        }
    }

    protected void b(DamageSource damagesource, int i) {
        this.health -= i;
    }

    protected float l() {
        return 1.0F;
    }

    protected String h() {
        return null;
    }

    protected String i() {
        return "random.hurt";
    }

    protected String j() {
        return "random.hurt";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.ca = true;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = 0.4F;

        this.motX /= 2.0D;
        this.motY /= 2.0D;
        this.motZ /= 2.0D;
        this.motX -= d0 / (double) f * (double) f1;
        this.motY += 0.4000000059604645D;
        this.motZ -= d1 / (double) f * (double) f1;
        if (this.motY > 0.4000000059604645D) {
            this.motY = 0.4000000059604645D;
        }
    }

    public void die(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();

        if (this.ag >= 0 && entity != null) {
            entity.b(this, this.ag);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.aw = true;
        if (!this.world.isStatic) {
            this.a(this.c > 0);
        }

        this.world.a(this, (byte) 3);
    }

    protected void a(boolean flag) {
        int i = this.k();

        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int count = this.random.nextInt(3);

        if ((i > 0) && (count > 0)) {
            loot.add(new org.bukkit.inventory.ItemStack(i, count));
        }

        CraftEntity entity = (CraftEntity) this.getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, loot);
        org.bukkit.World bworld = this.world.getWorld();
        this.world.getServer().getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            bworld.dropItemNaturally(entity.getLocation(), stack);
        }
        // CraftBukkit end
    }

    protected int k() {
        return 0;
    }

    protected void a(float f) {
        super.a(f);
        int i = (int) Math.ceil((double) (f - 3.0F));

        if (i > 0) {
            // CraftBukkit start
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FALL, i);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getDamage() != 0) {
                this.damageEntity(DamageSource.FALL, event.getDamage());
            }
            // CraftBukkit end

            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    public void a(float f, float f1) {
        double d0;

        if (this.ao()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.ap()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (i > 0) {
                    f2 = Block.byId[i].frictionFactor * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            float f4 = this.onGround ? this.aj * f3 : this.ak;

            this.a(f, f1, f4);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            if (this.p()) {
                float f5 = 0.15F;

                if (this.motX < (double) (-f5)) {
                    this.motX = (double) (-f5);
                }

                if (this.motX > (double) f5) {
                    this.motX = (double) f5;
                }

                if (this.motZ < (double) (-f5)) {
                    this.motZ = (double) (-f5);
                }

                if (this.motZ > (double) f5) {
                    this.motZ = (double) f5;
                }

                this.fallDistance = 0.0F;
                if (this.motY < -0.15D) {
                    this.motY = -0.15D;
                }

                if (this.isSneaking() && this.motY < 0.0D) {
                    this.motY = 0.0D;
                }
            }

            this.move(this.motX, this.motY, this.motZ);
            if (this.positionChanged && this.p()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.aA = this.aB;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aB += (f6 - this.aB) * 0.4F;
        this.aC += this.aB;
    }

    public boolean p() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        return this.world.getTypeId(i, j, k) == Block.LADDER.id;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) this.health);
        nbttagcompound.a("HurtTime", (short) this.hurtTicks);
        nbttagcompound.a("DeathTime", (short) this.deathTicks);
        nbttagcompound.a("AttackTime", (short) this.attackTicks);
        if (!this.aF.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.aF.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Id", (byte) mobeffect.a());
                nbttagcompound1.a("Amplifier", (byte) mobeffect.c());
                nbttagcompound1.a("Duration", mobeffect.b());
                nbttaglist.a((NBTBase) nbttagcompound1);
            }

            nbttagcompound.a("ActiveEffects", (NBTBase) nbttaglist);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.d("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = 10;
        }

        this.hurtTicks = nbttagcompound.d("HurtTime");
        this.deathTicks = nbttagcompound.d("DeathTime");
        this.attackTicks = nbttagcompound.d("AttackTime");
        if (nbttagcompound.hasKey("ActiveEffects")) {
            NBTTagList nbttaglist = nbttagcompound.l("ActiveEffects");

            for (int i = 0; i < nbttaglist.c(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
                byte b0 = nbttagcompound1.c("Id");
                byte b1 = nbttagcompound1.c("Amplifier");
                int j = nbttagcompound1.e("Duration");

                this.aF.put(Integer.valueOf(b0), new MobEffect(b0, j, b1));
            }
        }
    }

    public boolean ac() {
        return !this.dead && this.health > 0;
    }

    public boolean b_() {
        return false;
    }

    public void s() {
        if (this.aG > 0) {
            double d0 = this.locX + (this.aH - this.locX) / (double) this.aG;
            double d1 = this.locY + (this.aI - this.locY) / (double) this.aG;
            double d2 = this.locZ + (this.aJ - this.locZ) / (double) this.aG;

            double d3;

            for (d3 = this.aK - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aG);
            this.pitch = (float) ((double) this.pitch + (this.aL - (double) this.pitch) / (double) this.aG);
            --this.aG;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
            List list = this.world.getEntities(this, this.boundingBox.shrink(0.03125D, 0.0D, 0.03125D));

            if (list.size() > 0) {
                double d4 = 0.0D;

                for (int i = 0; i < list.size(); ++i) {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB) list.get(i);

                    if (axisalignedbb.e > d4) {
                        d4 = axisalignedbb.e;
                    }
                }

                d1 += d4 - this.boundingBox.b;
                this.setPosition(d0, d1, d2);
            }
        }

        if (this.H()) {
            this.aS = false;
            this.aP = 0.0F;
            this.aQ = 0.0F;
            this.aR = 0.0F;
        } else if (!this.ai) {
            this.c_();
        }

        boolean flag = this.ao();
        boolean flag1 = this.ap();

        if (this.aS) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.S();
            }
        }

        this.aP *= 0.98F;
        this.aQ *= 0.98F;
        this.aR *= 0.9F;
        float f = this.aj;

        this.aj *= this.D();
        this.a(this.aP, this.aQ);
        this.aj = f;
        List list1 = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list1 != null && list1.size() > 0) {
            for (int j = 0; j < list1.size(); ++j) {
                Entity entity = (Entity) list1.get(j);

                if (entity.g()) {
                    entity.collide(this);
                }
            }
        }
    }

    protected boolean H() {
        return this.health <= 0;
    }

    public boolean G() {
        return false;
    }

    protected void S() {
        this.motY = 0.41999998688697815D;
        if (this.at()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.ca = true;
    }

    protected boolean d_() {
        return true;
    }

    protected void ad() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        if (this.d_() && entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.die();
            }

            if (this.aO > 600 && this.random.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.aO = 0;
                } else {
                    this.die();
                }
            }
        }
    }

    protected void c_() {
        ++this.aO;
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        this.ad();
        this.aP = 0.0F;
        this.aQ = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.findNearbyPlayer(this, (double) f);
            if (entityhuman != null) {
                this.d = entityhuman;
                this.aV = 10 + this.random.nextInt(20);
            } else {
                this.aR = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.d != null) {
            this.a(this.d, 10.0F, (float) this.u());
            if (this.aV-- <= 0 || this.d.dead || this.d.h(this) > (double) (f * f)) {
                this.d = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.aR = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.aR;
            this.pitch = this.aT;
        }

        boolean flag = this.ao();
        boolean flag1 = this.ap();

        if (flag || flag1) {
            this.aS = this.random.nextFloat() < 0.8F;
        }
    }

    protected int u() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = this.locY + (double) this.t() - (entityliving.locY + (double) entityliving.t());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.t());
        }

        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.pitch = -this.b(this.pitch, f3, f1);
        this.yaw = this.b(this.yaw, f2, f);
    }

    public boolean ae() {
        return this.d != null;
    }

    public Entity af() {
        return this.d;
    }

    private float b(float f, float f1, float f2) {
        float f3;

        for (f3 = f1 - f; f3 < -180.0F; f3 += 360.0F) {
            ;
        }

        while (f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public void ag() {}

    public boolean d() {
        return this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected void ah() {
        // CraftBukkit start
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, this.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, 4);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        this.damageEntity(DamageSource.OUT_OF_WORLD, event.getDamage());
        // CraftBukkit end
    }

    public Vec3D ai() {
        return this.c(1.0F);
    }

    public Vec3D c(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.cos(-this.yaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.sin(-this.yaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.cos(-this.pitch * 0.017453292F);
            f4 = MathHelper.sin(-this.pitch * 0.017453292F);
            return Vec3D.create((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);

            return Vec3D.create((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int m() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void aj() {
        Iterator iterator = this.aF.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.aF.get(integer);

            if (!mobeffect.a(this) && !this.world.isStatic) {
                iterator.remove();
                this.c(mobeffect);
            }
        }
    }

    public Collection ak() {
        return this.aF.values();
    }

    public boolean a(MobEffectList mobeffectlist) {
        return this.aF.containsKey(Integer.valueOf(mobeffectlist.H));
    }

    public MobEffect b(MobEffectList mobeffectlist) {
        return (MobEffect) this.aF.get(Integer.valueOf(mobeffectlist.H));
    }

    public void d(MobEffect mobeffect) {
        if (this.aF.containsKey(Integer.valueOf(mobeffect.a()))) {
            ((MobEffect) this.aF.get(Integer.valueOf(mobeffect.a()))).a(mobeffect);
            this.b((MobEffect) this.aF.get(Integer.valueOf(mobeffect.a())));
        } else {
            this.aF.put(Integer.valueOf(mobeffect.a()), mobeffect);
            this.a(mobeffect);
        }
    }

    protected void a(MobEffect mobeffect) {}

    protected void b(MobEffect mobeffect) {}

    protected void c(MobEffect mobeffect) {}

    protected float D() {
        float f = 1.0F;

        if (this.a(MobEffectList.c)) {
            f *= 1.0F + 0.2F * (float) (this.b(MobEffectList.c).c() + 1);
        }

        if (this.a(MobEffectList.d)) {
            f *= 1.0F - 0.15F * (float) (this.b(MobEffectList.d).c() + 1);
        }

        return f;
    }
}
