package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float E;
    public float F;
    public float G = 0.0F;
    public float H = 0.0F;
    protected float I;
    protected float J;
    protected float K;
    protected float L;
    protected boolean M = true;
    protected String texture = "/mob/char.png";
    protected boolean O = true;
    protected float P = 0.0F;
    protected String Q = null;
    protected float R = 1.0F;
    protected int S = 0;
    protected float T = 0.0F;
    public boolean U = false;
    public float V;
    public float W;
    public int health = 10;
    public int Y;
    private int a;
    public int hurtTicks;
    public int aa;
    public float ab = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float ae;
    public float af;
    protected boolean ag = false;
    public int ah = -1;
    public float ai = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aj;
    public float ak;
    public float al;
    protected int am;
    protected double an;
    protected double ao;
    protected double ap;
    protected double aq;
    protected double ar;
    float as = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int au = 0;
    protected float av;
    protected float aw;
    protected float ax;
    protected boolean ay = false;
    protected float az = 0.0F;
    protected float aA = 0.7F;
    private Entity b;
    protected int aB = 0;

    public EntityLiving(World world) {
        super(world);
        this.aE = true;
        this.F = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.E = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.bo = 0.5F;
    }

    protected void b() {}

    public boolean e(Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + (double) this.s(), this.locZ), Vec3D.create(entity.locX, entity.locY + (double) entity.s(), entity.locZ)) == null;
    }

    public boolean o_() {
        return !this.dead;
    }

    public boolean d_() {
        return !this.dead;
    }

    public float s() {
        return this.width * 0.85F;
    }

    public int e() {
        return 80;
    }

    public void M() {
        String s = this.g();

        if (s != null) {
            this.world.makeSound(this, s, this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public void N() {
        this.V = this.W;
        super.N();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.e();
            this.M();
        }

        if (this.P() && this.H()) {
            // CraftBukkit start
            CraftServer server = ((WorldServer) this.world).getServer();
            org.bukkit.entity.Entity victim = this.getBukkitEntity();
            DamageCause damageType = EntityDamageEvent.DamageCause.SUFFOCATION;
            int damage = 1;

            EntityDamageEvent event = new EntityDamageEvent(victim, damageType, damage);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.damageEntity((Entity) null, event.getDamage());
            }
            // CraftBukkit end
        }

        if (this.bz || this.world.isStatic) {
            this.fireTicks = 0;
        }

        int i;

        if (this.P() && this.a(Material.WATER) && !this.b_()) {
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
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.DROWNING;
                int damageDone = 2;

                EntityDamageEvent event = new EntityDamageEvent(damagee, damageType, damageDone);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.damageEntity((Entity) null, event.getDamage());
                }
                // CraftBukkit end
            }

            this.fireTicks = 0;
        } else {
            this.airTicks = this.maxAirTicks;
        }

        this.ae = this.af;
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
                this.T();
                this.die();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
                }
            }
        }

        this.L = this.K;
        this.H = this.G;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void O() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.width) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d2 * d3, d0, d1, d2);
        }
    }

    public void B() {
        super.B();
        this.I = this.J;
        this.J = 0.0F;
    }

    public void p_() {
        super.p_();
        this.u();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.G;
        float f2 = 0.0F;

        this.I = this.J;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.W > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.J += (f3 - this.J) * 0.3F;

        float f4;

        for (f4 = f1 - this.G; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.G += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.G; f5 < -180.0F; f5 += 360.0F) {
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

        this.G = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.G += f5 * 0.2F;
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

        while (this.G - this.H < -180.0F) {
            this.H -= 360.0F;
        }

        while (this.G - this.H >= 180.0F) {
            this.H += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.K += f2;
    }

    protected void b(float f, float f1) {
        super.b(f, f1);
    }

    public void b(int i) {
        if (this.health > 0) {
            this.health += i;
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
            this.au = 0;
            if (this.health <= 0) {
                return false;
            } else {
                this.ak = 1.5F;
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
                    this.Y = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.c(i);
                    this.hurtTicks = this.aa = 10;
                }

                this.ab = 0.0F;
                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.ab();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.ab = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.ab = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.i(), this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    this.a(entity);
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

    public void a(Entity entity) {
        if (this.S >= 0 && entity != null) {
            entity.c(this, this.S);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.ag = true;
        if (!this.world.isStatic) {
            this.r();
        }

        this.world.a(this, (byte) 3);
    }

    protected void r() {
        int i = this.j();

        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new ArrayList<org.bukkit.inventory.ItemStack>();
        int count = random.nextInt(3);

        if ((i > 0) && (count > 0)) {
            loot.add(new org.bukkit.inventory.ItemStack(i, count));
        }

        CraftEntity entity = (CraftEntity) getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(entity, loot);
        CraftWorld cworld = ((WorldServer) world).getWorld();
        Server server = ((WorldServer) world).getServer();
        server.getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            cworld.dropItemNaturally(entity.getLocation(), stack);
        }
        // CraftBukkit end
    }

    protected int j() {
        return 0;
    }

    protected void a(float f) {
        int i = (int) Math.ceil((double) (f - 3.0F));

        if (i > 0) {
            // CraftBukkit start
            CraftServer server = ((WorldServer) this.world).getServer();
            org.bukkit.entity.Entity victim = this.getBukkitEntity();
            DamageCause damageType = EntityDamageEvent.DamageCause.FALL;

            EntityDamageEvent event = new EntityDamageEvent(victim, damageType, i);
            server.getPluginManager().callEvent(event);

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

        if (this.Z()) {
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
        } else if (this.aa()) {
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

        this.aj = this.ak;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f4 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.ak += (f4 - this.ak) * 0.4F;
        this.al += this.ak;
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

    public boolean P() {
        return !this.dead && this.health > 0;
    }

    public boolean b_() {
        return false;
    }

    public void u() {
        if (this.am > 0) {
            double d0 = this.locX + (this.an - this.locX) / (double) this.am;
            double d1 = this.locY + (this.ao - this.locY) / (double) this.am;
            double d2 = this.locZ + (this.ap - this.locZ) / (double) this.am;

            double d3;

            for (d3 = this.aq - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.am);
            this.pitch = (float) ((double) this.pitch + (this.ar - (double) this.pitch) / (double) this.am);
            --this.am;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
        }

        if (this.A()) {
            this.ay = false;
            this.av = 0.0F;
            this.aw = 0.0F;
            this.ax = 0.0F;
        } else if (!this.U) {
            this.c_();
        }

        boolean flag = this.Z();
        boolean flag1 = this.aa();

        if (this.ay) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.L();
            }
        }

        this.av *= 0.98F;
        this.aw *= 0.98F;
        this.ax *= 0.9F;
        this.a(this.av, this.aw);
        List list = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.d_()) {
                    entity.collide(this);
                }
            }
        }
    }

    protected boolean A() {
        return this.health <= 0;
    }

    protected void L() {
        this.motY = 0.41999998688697815D;
    }

    protected boolean l_() {
        return true;
    }

    protected void Q() {
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        if (this.l_() && entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.die();
            }

            if (this.au > 600 && this.random.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.au = 0;
                } else {
                    this.die();
                }
            }
        }
    }

    protected void c_() {
        ++this.au;
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        this.Q();
        this.av = 0.0F;
        this.aw = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.a(this, (double) f);
            if (entityhuman != null) {
                this.b = entityhuman;
                this.aB = 10 + this.random.nextInt(20);
            } else {
                this.ax = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.b != null) {
            this.a(this.b, 10.0F, (float) this.v());
            if (this.aB-- <= 0 || this.b.dead || this.b.g(this) > (double) (f * f)) {
                this.b = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.ax = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.ax;
            this.pitch = this.az;
        }

        boolean flag = this.Z();
        boolean flag1 = this.aa();

        if (flag || flag1) {
            this.ay = this.random.nextFloat() < 0.8F;
        }
    }

    protected int v() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = this.locY + (double) this.s() - (entityliving.locY + (double) entityliving.s());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.s());
        }

        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.pitch = -this.b(this.pitch, f3, f1);
        this.yaw = this.b(this.yaw, f2, f);
    }

    public boolean R() {
        return this.b != null;
    }

    public Entity S() {
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

    public void T() {}

    public boolean d() {
        return this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected void U() {
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        DamageCause damageType = EntityDamageEvent.DamageCause.VOID;
        org.bukkit.block.Block damager = null;
        org.bukkit.entity.Entity damagee = this.getBukkitEntity();
        int damageDone = 4;
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, damageType, damageDone);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        damageDone = event.getDamage();
        this.damageEntity((Entity) null, damageDone);
        // CraftBukkit end
    }

    public Vec3D V() {
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
