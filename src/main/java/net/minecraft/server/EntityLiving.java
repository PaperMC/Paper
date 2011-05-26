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
    public float H;
    public float I;
    public float J = 0.0F;
    public float K = 0.0F;
    protected float L;
    protected float M;
    protected float N;
    protected float O;
    protected boolean P = true;
    protected String texture = "/mob/char.png";
    protected boolean R = true;
    protected float S = 0.0F;
    protected String T = null;
    protected float U = 1.0F;
    protected int V = 0;
    protected float W = 0.0F;
    public boolean X = false;
    public float Y;
    public float Z;
    public int health = 10;
    public int ab;
    private int a;
    public int hurtTicks;
    public int ad;
    public float ae = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float ah;
    public float ai;
    protected boolean aj = false;
    public int ak = -1;
    public float al = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float am;
    public float an;
    public float ao;
    protected int ap;
    protected double aq;
    protected double ar;
    protected double as;
    protected double at;
    protected double au;
    float av = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int ax = 0;
    protected float ay;
    protected float az;
    protected float aA;
    protected boolean aB = false;
    protected float aC = 0.0F;
    protected float aD = 0.7F;
    private Entity b;
    protected int aE = 0;

    public EntityLiving(World world) {
        super(world);
        this.aH = true;
        this.I = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.H = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.br = 0.5F;
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

    public void N() {
        String s = this.g();

        if (s != null) {
            this.world.makeSound(this, s, this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public void O() {
        this.Y = this.Z;
        super.O();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.e();
            this.N();
        }

        if (this.Q() && this.H()) {
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

        if (this.bC || this.world.isStatic) {
            this.fireTicks = 0;
        }

        int i;

        if (this.Q() && this.a(Material.WATER) && !this.b_()) {
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

        this.ah = this.ai;
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
                this.U();
                this.die();

                for (i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;

                    this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
                }
            }
        }

        this.O = this.N;
        this.K = this.J;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void P() {
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
        this.L = this.M;
        this.M = 0.0F;
    }

    public void p_() {
        super.p_();
        this.u();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.a(d0 * d0 + d1 * d1);
        float f1 = this.J;
        float f2 = 0.0F;

        this.L = this.M;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.Z > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.M += (f3 - this.M) * 0.3F;

        float f4;

        for (f4 = f1 - this.J; f4 < -180.0F; f4 += 360.0F) {
            ;
        }

        while (f4 >= 180.0F) {
            f4 -= 360.0F;
        }

        this.J += f4 * 0.3F;

        float f5;

        for (f5 = this.yaw - this.J; f5 < -180.0F; f5 += 360.0F) {
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

        this.J = this.yaw - f5;
        if (f5 * f5 > 2500.0F) {
            this.J += f5 * 0.2F;
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

        while (this.J - this.K < -180.0F) {
            this.K -= 360.0F;
        }

        while (this.J - this.K >= 180.0F) {
            this.K += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        this.N += f2;
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
            this.ax = 0;
            if (this.health <= 0) {
                return false;
            } else {
                this.an = 1.5F;
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
                    this.ab = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.c(i);
                    this.hurtTicks = this.ad = 10;
                }

                this.ae = 0.0F;
                if (flag) {
                    this.world.a(this, (byte) 2);
                    this.ac();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.ae = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.ae = (float) ((int) (Math.random() * 2.0D) * 180);
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
        if (this.V >= 0 && entity != null) {
            entity.c(this, this.V);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.aj = true;
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
        super.a(f);
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

        if (this.aa()) {
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
        } else if (this.ab()) {
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

        this.am = this.an;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f5 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.an += (f5 - this.an) * 0.4F;
        this.ao += this.an;
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

    public boolean Q() {
        return !this.dead && this.health > 0;
    }

    public boolean b_() {
        return false;
    }

    public void u() {
        if (this.ap > 0) {
            double d0 = this.locX + (this.aq - this.locX) / (double) this.ap;
            double d1 = this.locY + (this.ar - this.locY) / (double) this.ap;
            double d2 = this.locZ + (this.as - this.locZ) / (double) this.ap;

            double d3;

            for (d3 = this.at - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.ap);
            this.pitch = (float) ((double) this.pitch + (this.au - (double) this.pitch) / (double) this.ap);
            --this.ap;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
        }

        if (this.A()) {
            this.aB = false;
            this.ay = 0.0F;
            this.az = 0.0F;
            this.aA = 0.0F;
        } else if (!this.X) {
            this.c_();
        }

        boolean flag = this.aa();
        boolean flag1 = this.ab();

        if (this.aB) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround) {
                this.L();
            }
        }

        this.ay *= 0.98F;
        this.az *= 0.98F;
        this.aA *= 0.9F;
        this.a(this.ay, this.az);
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

    protected void R() {
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        if (this.l_() && entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 16384.0D) {
                this.die();
            }

            if (this.ax > 600 && this.random.nextInt(800) == 0) {
                if (d3 < 1024.0D) {
                    this.ax = 0;
                } else {
                    this.die();
                }
            }
        }
    }

    protected void c_() {
        ++this.ax;
        EntityHuman entityhuman = this.world.a(this, -1.0D);

        this.R();
        this.ay = 0.0F;
        this.az = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            entityhuman = this.world.a(this, (double) f);
            if (entityhuman != null) {
                this.b = entityhuman;
                this.aE = 10 + this.random.nextInt(20);
            } else {
                this.aA = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.b != null) {
            this.a(this.b, 10.0F, (float) this.v());
            if (this.aE-- <= 0 || this.b.dead || this.b.g(this) > (double) (f * f)) {
                this.b = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.aA = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.aA;
            this.pitch = this.aC;
        }

        boolean flag = this.aa();
        boolean flag1 = this.ab();

        if (flag || flag1) {
            this.aB = this.random.nextFloat() < 0.8F;
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

    public boolean S() {
        return this.b != null;
    }

    public Entity T() {
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

    public void U() {}

    public boolean d() {
        return this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected void V() {
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

    public Vec3D W() {
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
