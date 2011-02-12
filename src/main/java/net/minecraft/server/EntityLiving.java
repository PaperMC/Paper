package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float aG;
    public float aH;
    public float aI = 0.0F;
    public float aJ = 0.0F;
    protected float aK;
    protected float aL;
    protected float aM;
    protected float aN;
    protected boolean aO = true;
    protected String texture = "/mob/char.png";
    protected boolean aQ = true;
    protected float aR = 0.0F;
    protected String aS = null;
    protected float aT = 1.0F;
    protected int aU = 0;
    protected float aV = 0.0F;
    public boolean aW = false;
    public float aX;
    public float aY;
    public int health = 10;
    public int ba;
    private int a;
    public int hurtTicks;
    public int bc;
    public float bd = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float bg;
    public float bh;
    protected boolean bi = false;
    public int bj = -1;
    public float bk = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float bl;
    public float bm;
    public float bn;
    protected int bo;
    protected double bp;
    protected double bq;
    protected double br;
    protected double bs;
    protected double bt;
    float bu = 0.0F;
    protected int lastDamage = 0;
    protected int bw = 0;
    protected float bx;
    protected float by;
    protected float bz;
    protected boolean bA = false;
    protected float bB = 0.0F;
    protected float bC = 0.7F;
    private Entity b;
    private int c = 0;

    public EntityLiving(World world) {
        super(world);
        this.i = true;
        this.aH = (float) (Math.random() + 1.0D) * 0.01F;
        this.a(this.locX, this.locY, this.locZ);
        this.aG = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.S = 0.5F;
    }

    protected void a() {}

    public boolean i(Entity entity) {
        return this.world.a(Vec3D.b(this.locX, this.locY + (double) this.w(), this.locZ), Vec3D.b(entity.locX, entity.locY + (double) entity.w(), entity.locZ)) == null;
    }

    public boolean c_() {
        return !this.dead;
    }

    public boolean z() {
        return !this.dead;
    }

    public float w() {
        return this.width * 0.85F;
    }

    public int c() {
        return 80;
    }

    public void r() {
        this.aX = this.aY;
        super.r();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.c();
            String s = this.e();

            if (s != null) {
                this.world.a(this, s, this.i(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
        }

        if (this.B() && this.C()) {
            // CraftBukkit start
            CraftServer server = ((WorldServer) this.world).getServer();
            org.bukkit.entity.Entity victim = this.getBukkitEntity();
            DamageCause damageType = EntityDamageEvent.DamageCause.SUFFOCATION;
            EntityDamageEvent event = new EntityDamageEvent(victim, damageType, 1);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()){
                this.a((Entity) null, 1);
            }
            // CraftBukkit end
        }

        if (this.ae || this.world.isStatic) {
            this.fireTicks = 0;
        }

        int i;

        if (this.B() && this.a(Material.WATER) && !this.d_()) {
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

                if (!event.isCancelled()){
                    this.a((Entity) null, event.getDamage());
                }
                // CraftBukkit end
            }

            this.fireTicks = 0;
        } else {
            this.airTicks = this.maxAirTicks;
        }

        this.bg = this.bh;
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
                this.q();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
                }
            }
        }

        this.aN = this.aM;
        this.aJ = this.aI;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void R() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.width) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length - d2 * d3, d0, d1, d2);
        }
    }

    public void D() {
        super.D();
        this.aK = this.aL;
        this.aL = 0.0F;
    }

    public void b_() {
        super.b_();
        this.o();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.aI;
        float f2 = 0.0F;

        this.aK = this.aL;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            f1 = (float) Math.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aY > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aL += (f3 - this.aL) * 0.3F;

        float f4;

        for (f4 = f1 - this.aI; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.aI += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.aI; f5 < -180.0F; f5 += 360.0F) {
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

        this.aI = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.aI += f5 * 0.2F;
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

        while (this.aI - this.aJ < -180.0F) {
            this.aJ -= 360.0F;
        }

        while (this.aI - this.aJ >= 180.0F) {
            this.aJ += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.aM += f2;
    }

    protected void a(float f, float f1) {
        super.a(f, f1);
    }

    public void d(int i) {
        if (this.health > 0) {
            this.health += i;
            if (this.health > 20) {
                this.health = 20;
            }

            this.noDamageTicks = this.maxNoDamageTicks / 2;
        }
    }

    public boolean a(Entity entity, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.bw = 0;
            if (this.health <= 0) {
                return false;
            } else {
                this.bm = 1.5F;
                boolean flag = true;

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.e(i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.ba = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.e(i);
                    this.hurtTicks = this.bc = 10;
                }

                this.bd = 0.0F;
                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.y();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.bd = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.bd = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.a(this, this.g(), this.i(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    }

                    this.f(entity);
                } else if (flag) {
                    this.world.a(this, this.f(), this.i(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                return true;
            }
        }
    }

    protected void e(int i) {
        this.health -= i;
    }

    protected float i() {
        return 1.0F;
    }

    protected String e() {
        return null;
    }

    protected String f() {
        return "random.hurt";
    }

    protected String g() {
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

    public void f(Entity entity) {
        if (this.aU > 0 && entity != null) {
            entity.b(this, this.aU);
        }

        this.bi = true;
        if (!this.world.isStatic) {
            this.g_();
        }

        this.world.a(this, (byte) 3);
    }

    protected void g_() {
        // Craftbukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new ArrayList<org.bukkit.inventory.ItemStack>();
        int drop = this.h();
        int count = random.nextInt(3);

        if ((drop > 0) && (count > 0)) {
            loot.add(new org.bukkit.inventory.ItemStack(drop, count));
        }

        CraftEntity entity = (CraftEntity)getBukkitEntity();
        EntityDeathEvent event = new EntityDeathEvent(Type.ENTITY_DEATH, entity, loot);
        CraftWorld cworld = ((WorldServer)world).getWorld();
        Server server = ((WorldServer)world).getServer();
        server.getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack : event.getDrops()) {
            cworld.dropItemNaturally(entity.getLocation(), stack);
        }
        // Craftbukkit end
    }

    protected int h() {
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

            if (!event.isCancelled()){
                this.a((Entity) null, event.getDamage());
            }
            // CraftBukkit end

            int j = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.b(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.world.a(this, stepsound.c(), stepsound.a() * 0.5F, stepsound.b() * 0.75F);
            }
        }
    }

    public void c(float f, float f1) {
        double d0;

        if (this.v()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.B && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.x()) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.B && this.b(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (i > 0) {
                    f2 = Block.byId[i].frictionFactor * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);

            this.a(f, f1, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            if (this.m()) {
                this.fallDistance = 0.0F;
                if (this.motY < -0.15D) {
                    this.motY = -0.15D;
                }
            }

            this.c(this.motX, this.motY, this.motZ);
            if (this.B && this.m()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.bl = this.bm;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f4 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.bm += (f4 - this.bm) * 0.4F;
        this.bn += this.bm;
    }

    public boolean m() {
        int i = MathHelper.b(this.locX);
        int j = MathHelper.b(this.boundingBox.b);
        int k = MathHelper.b(this.locZ);

        return this.world.getTypeId(i, j, k) == Block.LADDER.id || this.world.getTypeId(i, j + 1, k) == Block.LADDER.id;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) this.health);
        nbttagcompound.a("HurtTime", (short) this.hurtTicks);
        nbttagcompound.a("DeathTime", (short) this.deathTicks);
        nbttagcompound.a("AttackTime", (short) this.attackTicks);
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.c("Health");
        if (!nbttagcompound.a("Health")) {
            this.health = 10;
        }

        this.hurtTicks = nbttagcompound.c("HurtTime");
        this.deathTicks = nbttagcompound.c("DeathTime");
        this.attackTicks = nbttagcompound.c("AttackTime");
    }

    public boolean B() {
        return !this.dead && this.health > 0;
    }

    public boolean d_() {
        return false;
    }

    public void o() {
        if (this.bo > 0) {
            double d0 = this.locX + (this.bp - this.locX) / (double) this.bo;
            double d1 = this.locY + (this.bq - this.locY) / (double) this.bo;
            double d2 = this.locZ + (this.br - this.locZ) / (double) this.bo;

            double d3;

            for (d3 = this.bs - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.bo);
            this.pitch = (float) ((double) this.pitch + (this.bt - (double) this.pitch) / (double) this.bo);
            --this.bo;
            this.a(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        }

        if (this.health <= 0) {
            this.bA = false;
            this.bx = 0.0F;
            this.by = 0.0F;
            this.bz = 0.0F;
        } else if (!this.aW) {
            this.d();
        }

        boolean flag = this.v();
        boolean flag1 = this.x();

        if (this.bA) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.S();
            }
        }

        this.bx *= 0.98F;
        this.by *= 0.98F;
        this.bz *= 0.9F;
        this.c(this.bx, this.by);
        List list = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.z()) {
                    entity.c((Entity) this);
                }
            }
        }
    }

    protected void S() {
        this.motY = 0.41999998688697815D;
    }

    protected void d() {
        ++this.bw;
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.q();
            }

            if (this.bw > 600 && this.random.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.bw = 0;
                } else {
                    this.q();
                }
            }
        }

        this.bx = 0.0F;
        this.by = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.a(this, (double) f);
            if (entityhuman != null) {
                this.b = entityhuman;
                this.c = 10 + this.random.nextInt(20);
            } else {
                this.bz = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.b != null) {
            this.b(this.b, 10.0F);
            if (this.c-- <= 0 || this.b.dead || this.b.b((Entity) this) > (double) (f * f)) {
                this.b = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.bz = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.bz;
            this.pitch = this.bB;
        }

        boolean flag = this.v();
        boolean flag1 = this.x();

        if (flag || flag1) {
            this.bA = this.random.nextFloat() < 0.8F;
        }
    }

    public void b(Entity entity, float f) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = entityliving.locY + (double) entityliving.w() - (this.locY + (double) this.w());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.w());
        }

        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f2 = (float) (Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D);

        this.pitch = -this.b(this.pitch, f2, f);
        this.yaw = this.b(this.yaw, f1, f);
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

    public boolean b() {
        return this.world.a(this.boundingBox) && this.world.a((Entity) this, this.boundingBox).size() == 0 && !this.world.b(this.boundingBox);
    }

    protected void t() {
        this.a((Entity) null, 4);
    }

    public Vec3D G() {
        return this.c(1.0F);
    }

    public Vec3D c(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.b(-this.yaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.a(-this.yaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.b(-this.pitch * 0.017453292F);
            f4 = MathHelper.a(-this.pitch * 0.017453292F);
            return Vec3D.b((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.b(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.a(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.b(-f1 * 0.017453292F);
            float f6 = MathHelper.a(-f1 * 0.017453292F);

            return Vec3D.b((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int j() {
        return 4;
    }
}
