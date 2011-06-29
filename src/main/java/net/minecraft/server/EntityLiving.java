package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float I;
    public float J;
    public float K = 0.0F;
    public float L = 0.0F;
    protected float M;
    protected float N;
    protected float O;
    protected float P;
    protected boolean Q = true;
    protected String texture = "/mob/char.png";
    protected boolean S = true;
    protected float T = 0.0F;
    protected String U = null;
    protected float V = 1.0F;
    protected int W = 0;
    protected float X = 0.0F;
    public boolean Y = false;
    public float Z;
    public float aa;
    public int health = 10;
    public int ac;
    private int a;
    public int hurtTicks;
    public int ae;
    public float af = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float ai;
    public float aj;
    protected boolean ak = false;
    public int al = -1;
    public float am = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float an;
    public float ao;
    public float ap;
    protected int aq;
    protected double ar;
    protected double as;
    protected double at;
    protected double au;
    protected double av;
    float aw = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int ay = 0;
    protected float az;
    protected float aA;
    protected float aB;
    protected boolean aC = false;
    protected float aD = 0.0F;
    protected float aE = 0.7F;
    private Entity b;
    protected int aF = 0;

    public EntityLiving(World world) {
        super(world);
        this.aI = true;
        this.J = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.I = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.bs = 0.5F;
    }

    protected void b() {}

    public boolean e(Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + (double) this.t(), this.locZ), Vec3D.create(entity.locX, entity.locY + (double) entity.t(), entity.locZ)) == null;
    }

    public boolean l_() {
        return !this.dead;
    }

    public boolean d_() {
        return !this.dead;
    }

    public float t() {
        return this.width * 0.85F;
    }

    public int e() {
        return 80;
    }

    public void Q() {
        String s = this.g();

        if (s != null) {
            this.world.makeSound(this, s, this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public void R() {
        this.Z = this.aa;
        super.R();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.e();
            this.Q();
        }

        if (this.T() && this.K()) {
            // CraftBukkit start
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.SUFFOCATION, 1);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.damageEntity((Entity) null, event.getDamage());
            }
            // CraftBukkit end
        }

        if (this.fireProof || this.world.isStatic) {
            this.fireTicks = 0;
        }

        int i;

        if (this.T() && this.a(Material.WATER) && !this.b_()) {
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
                    this.damageEntity((Entity) null, event.getDamage());
                }
                // CraftBukkit end
            }

            this.fireTicks = 0;
        } else {
            this.airTicks = this.maxAirTicks;
        }

        this.ai = this.aj;
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
                this.X();
                this.die();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
                }
            }
        }

        this.P = this.O;
        this.L = this.K;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void S() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.width) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d2 * d3, d0, d1, d2);
        }
    }

    public void E() {
        super.E();
        this.M = this.N;
        this.N = 0.0F;
    }

    public void m_() {
        super.m_();
        this.v();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.K;
        float f2 = 0.0F;

        this.M = this.N;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aa > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.N += (f3 - this.N) * 0.3F;

        float f4;

        for (f4 = f1 - this.K; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.K += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.K; f5 < -180.0F; f5 += 360.0F) {
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

        this.K = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.K += f5 * 0.2F;
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

        while (this.K - this.L < -180.0F) {
            this.L -= 360.0F;
        }

        while (this.K - this.L >= 180.0F) {
            this.L += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.O += f2;
    }

    protected void b(float f, float f1) {
        super.b(f, f1);
    }

    public void b(int i) {
        // CraftBukkit start - Added event
        if (this.health > 0) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i);
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

    public boolean damageEntity(Entity entity, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.ay = 0;
            if (this.health <= 0) {
                return false;
            } else {
                this.ao = 1.5F;
                boolean flag = true;

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.c(i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.ac = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.c(i);
                    this.hurtTicks = this.ae = 10;
                }

                this.af = 0.0F;
                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.af();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.af = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.af = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.i(), this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    this.die(entity);
                } else if (flag) {
                    this.world.makeSound(this, this.h(), this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                return true;
            }
        }
    }

    protected void c(int i) {
        this.health -= i;
    }

    protected float k() {
        return 1.0F;
    }

    protected String g() {
        return null;
    }

    protected String h() {
        return "random.hurt";
    }

    protected String i() {
        return "random.hurt";
    }

    public void a(Entity entity, int i, double d0, double d1) {
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

    public void die(Entity entity) {
        if (this.W >= 0 && entity != null) {
            entity.c(this, this.W);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.ak = true;
        if (!this.world.isStatic) {
            this.q();
        }

        this.world.a(this, (byte) 3);
    }

    protected void q() {
        int i = this.j();

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

    protected int j() {
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
                this.damageEntity((Entity) null, event.getDamage());
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

        if (this.ad()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.ae()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
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

            this.a(f, f1, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            if (this.p()) {
                float f4 = 0.15F;

                if (this.motX < (double) (-f4)) {
                    this.motX = (double) (-f4);
                }

                if (this.motX > (double) f4) {
                    this.motX = (double) f4;
                }

                if (this.motZ < (double) (-f4)) {
                    this.motZ = (double) (-f4);
                }

                if (this.motZ > (double) f4) {
                    this.motZ = (double) f4;
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

        this.an = this.ao;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f5 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.ao += (f5 - this.ao) * 0.4F;
        this.ap += this.ao;
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
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.d("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = 10;
        }

        this.hurtTicks = nbttagcompound.d("HurtTime");
        this.deathTicks = nbttagcompound.d("DeathTime");
        this.attackTicks = nbttagcompound.d("AttackTime");
    }

    public boolean T() {
        return !this.dead && this.health > 0;
    }

    public boolean b_() {
        return false;
    }

    public void v() {
        if (this.aq > 0) {
            double d0 = this.locX + (this.ar - this.locX) / (double) this.aq;
            double d1 = this.locY + (this.as - this.locY) / (double) this.aq;
            double d2 = this.locZ + (this.at - this.locZ) / (double) this.aq;

            double d3;

            for (d3 = this.au - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aq);
            this.pitch = (float) ((double) this.pitch + (this.av - (double) this.pitch) / (double) this.aq);
            --this.aq;
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

        if (this.D()) {
            this.aC = false;
            this.az = 0.0F;
            this.aA = 0.0F;
            this.aB = 0.0F;
        } else if (!this.Y) {
            this.c_();
        }

        boolean flag = this.ad();
        boolean flag1 = this.ae();

        if (this.aC) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.O();
            }
        }

        this.az *= 0.98F;
        this.aA *= 0.98F;
        this.aB *= 0.9F;
        this.a(this.az, this.aA);
        List list1 = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list1 != null && list1.size() > 0) {
            for (int j = 0; j < list1.size(); ++j) {
                Entity entity = (Entity) list1.get(j);

                if (entity.d_()) {
                    entity.collide(this);
                }
            }
        }
    }

    protected boolean D() {
        return this.health <= 0;
    }

    protected void O() {
        this.motY = 0.41999998688697815D;
    }

    protected boolean h_() {
        return true;
    }

    protected void U() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        if (this.h_() && entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.die();
            }

            if (this.ay > 600 && this.random.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.ay = 0;
                } else {
                    this.die();
                }
            }
        }
    }

    protected void c_() {
        ++this.ay;
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        this.U();
        this.az = 0.0F;
        this.aA = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.findNearbyPlayer(this, (double) f);
            if (entityhuman != null) {
                this.b = entityhuman;
                this.aF = 10 + this.random.nextInt(20);
            } else {
                this.aB = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.b != null) {
            this.a(this.b, 10.0F, (float) this.u());
            if (this.aF-- <= 0 || this.b.dead || this.b.g(this) > (double) (f * f)) {
                this.b = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.aB = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.aB;
            this.pitch = this.aD;
        }

        boolean flag = this.ad();
        boolean flag1 = this.ae();

        if (flag || flag1) {
            this.aC = this.random.nextFloat() < 0.8F;
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

    public boolean V() {
        return this.b != null;
    }

    public Entity W() {
        return this.b;
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

    public void X() {}

    public boolean d() {
        return this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected void Y() {
        // CraftBukkit start
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, this.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, 4);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        this.damageEntity((Entity) null, event.getDamage());
        // CraftBukkit end
    }

    public Vec3D Z() {
        return this.b(1.0F);
    }

    public Vec3D b(float f) {
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

    public int l() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }
}
