package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxNoDamageTicks = 20;
    public float T;
    public float U;
    public float V = 0.0F;
    public float W = 0.0F;
    public float X = 0.0F;
    public float Y = 0.0F;
    protected float Z;
    protected float aa;
    protected float ab;
    protected float ac;
    protected boolean ad = true;
    protected String texture = "/mob/char.png";
    protected boolean af = true;
    protected float ag = 0.0F;
    protected String ah = null;
    protected float ai = 1.0F;
    protected int aj = 0;
    protected float ak = 0.0F;
    public float al = 0.1F;
    public float am = 0.02F;
    public float an;
    public float ao;
    protected int health = this.getMaxHealth();
    public int aq;
    protected int ar;
    private int a;
    public int hurtTicks;
    public int at;
    public float au = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float ax;
    public float ay;
    protected boolean az = false;
    protected int aA;
    public int aB = -1;
    public float aC = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aD;
    public float aE;
    public float aF;
    public EntityHuman killer = null; // CraftBukkit - prot to pub
    protected int lastDamageByPlayerTime = 0;
    public EntityLiving lastDamager = null; // CraftBukkit - priv to pub
    private int c = 0;
    private EntityLiving d = null;
    public int aI = 0;
    public int aJ = 0;
    public HashMap effects = new HashMap(); // CraftBukkit - protected -> public
    public boolean e = true; // CraftBukkit - private -> public
    private int f;
    private ControllerLook lookController;
    private ControllerMove moveController;
    private ControllerJump jumpController;
    private EntityAIBodyControl senses;
    private Navigation navigation;
    protected PathfinderGoalSelector goalSelector = new PathfinderGoalSelector();
    protected PathfinderGoalSelector targetSelector = new PathfinderGoalSelector();
    private EntityLiving l;
    private EntitySenses m;
    private float n;
    private ChunkCoordinates o = new ChunkCoordinates(0, 0, 0);
    private float p = -1.0F;
    protected int aN;
    protected double aO;
    protected double aP;
    protected double aQ;
    protected double aR;
    protected double aS;
    float aT = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int aV = 0;
    protected float aW;
    protected float aX;
    protected float aY;
    protected boolean aZ = false;
    protected float ba = 0.0F;
    protected float bb = 0.7F;
    private int q = 0;
    private Entity r;
    protected int bc = 0;
    public int expToDrop = 0; // CraftBukkit
    public int maxAirTicks = 300; // CraftBukkit

    public EntityLiving(World world) {
        super(world);
        this.bf = true;
        this.lookController = new ControllerLook(this);
        this.moveController = new ControllerMove(this);
        this.jumpController = new ControllerJump(this);
        this.senses = new EntityAIBodyControl(this);
        this.navigation = new Navigation(this, world, 16.0F);
        this.m = new EntitySenses(this);
        this.U = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.T = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.X = this.yaw;
        this.bP = 0.5F;
    }

    public ControllerLook getControllerLook() {
        return this.lookController;
    }

    public ControllerMove getControllerMove() {
        return this.moveController;
    }

    public ControllerJump getControllerJump() {
        return this.jumpController;
    }

    public Navigation al() {
        return this.navigation;
    }

    public EntitySenses am() {
        return this.m;
    }

    public Random an() {
        return this.random;
    }

    public EntityLiving ao() {
        return this.lastDamager;
    }

    public EntityLiving ap() {
        return this.d;
    }

    public void g(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.d = (EntityLiving) entity;
        }
    }

    public int aq() {
        return this.aV;
    }

    public float ar() {
        return this.X;
    }

    public float as() {
        return this.n;
    }

    public void d(float f) {
        this.n = f;
        this.e(f);
    }

    public boolean a(Entity entity) {
        this.g(entity);
        return false;
    }

    public EntityLiving at() {
        return this.l;
    }

    public void b(EntityLiving entityliving) {
        this.l = entityliving;
    }

    public boolean a(Class oclass) {
        return EntityCreeper.class != oclass && EntityGhast.class != oclass;
    }

    public void z() {}

    public boolean au() {
        return this.e(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean e(int i, int j, int k) {
        return this.p == -1.0F ? true : this.o.c(i, j, k) < this.p * this.p;
    }

    public void b(int i, int j, int k, int l) {
        this.o.a(i, j, k);
        this.p = (float) l;
    }

    public ChunkCoordinates av() {
        return this.o;
    }

    public float aw() {
        return this.p;
    }

    public void ax() {
        this.p = -1.0F;
    }

    public boolean ay() {
        return this.p != -1.0F;
    }

    public void a(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.c = this.lastDamager != null ? 60 : 0;
    }

    protected void b() {
        this.datawatcher.a(8, Integer.valueOf(this.f));
    }

    public boolean h(Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ), Vec3D.create(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ)) == null;
    }

    public boolean o_() {
        return !this.dead;
    }

    public boolean e_() {
        return !this.dead;
    }

    public float getHeadHeight() {
        return this.length * 0.85F;
    }

    public int m() {
        return 80;
    }

    public void az() {
        String s = this.i();

        if (s != null) {
            this.world.makeSound(this, s, this.p(), this.A());
        }
    }

    public void aA() {
        this.an = this.ao;
        super.aA();
        // MethodProfiler.a("mobBaseTick"); // CraftBukkit - not in production code
        if (this.isAlive() && this.random.nextInt(1000) < this.a++) {
            this.a = -this.m();
            this.az();
        }

        // CraftBukkit start - don't inline the damage, perform it with an event
        if (this.isAlive() && this.inBlock() && !(this instanceof EntityEnderDragon)) { // EnderDragon's don't suffocate.
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.SUFFOCATION, 1);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
                this.damageEntity(DamageSource.STUCK, event.getDamage());
            }
            // CraftBukkit end
        }

        if (this.isFireproof() || this.world.isStatic) {
            this.extinguish();
        }

        if (this.isAlive() && this.a(Material.WATER) && !this.f_() && !this.effects.containsKey(Integer.valueOf(MobEffectList.WATER_BREATHING.id))) {
            this.setAirTicks(this.b_(this.getAirTicks()));
            if (this.getAirTicks() == -20) {
                this.setAirTicks(0);

                for (int i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.world.a("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }

                // CraftBukkit start
                EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.DROWNING, 2);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled() && event.getDamage() != 0) {
                    event.getEntity().setLastDamageCause(event);
                    this.damageEntity(DamageSource.DROWN, event.getDamage());
                }
                // CraftBukkit end
            }

            this.extinguish();
        } else {
            // CraftBukkit start - only set if needed to work around a datawatcher inefficiency
            if (this.getAirTicks() != 300) {
                this.setAirTicks(maxAirTicks);
            }
            // CraftBukkit end
        }

        this.ax = this.ay;
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
            this.aB();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.killer = null;
        }

        if (this.d != null && !this.d.isAlive()) {
            this.d = null;
        }

        if (this.lastDamager != null) {
            if (!this.lastDamager.isAlive()) {
                this.a((EntityLiving) null);
            } else if (this.c > 0) {
                --this.c;
            } else {
                this.a((EntityLiving) null);
            }
        }

        this.aK();
        this.ac = this.ab;
        this.W = this.V;
        this.Y = this.X;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        // MethodProfiler.a(); // CraftBukkit - not in production code
    }

    // CraftBukkit start
    public int getExpReward() {
        int exp = this.getExpValue(this.killer);

        if (!this.world.isStatic && (this.lastDamageByPlayerTime > 0 || this.alwaysGivesExp()) && !this.isBaby()) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    protected void aB() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.dead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead).
            int i;

            // CraftBukkit start - update getExpReward() above if the removed if() changes!
            i = expToDrop;
            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }
            // CraftBukkit end

            this.aH();
            this.die();

            for (i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    protected int b_(int i) {
        return i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return this.aA;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void aC() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void R() {
        super.R();
        this.Z = this.aa;
        this.aa = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void F_() {
        super.F_();
        if (this.aI > 0) {
            if (this.aJ <= 0) {
                this.aJ = 60;
            }

            --this.aJ;
            if (this.aJ <= 0) {
                --this.aI;
            }
        }

        this.e();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f1 = this.V;
        float f2 = 0.0F;

        this.Z = this.aa;
        float f3 = 0.0F;

        if (f > 0.05F) {
            f3 = 1.0F;
            f2 = f * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.ao > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aa += (f3 - this.aa) * 0.3F;
        if (this.c_()) {
            this.senses.a();
        } else {
            float f4;

            for (f4 = f1 - this.V; f4 < -180.0F; f4 += 360.0F) {
                ;
            }

            while (f4 >= 180.0F) {
                f4 -= 360.0F;
            }

            this.V += f4 * 0.3F;

            float f5;

            for (f5 = this.yaw - this.V; f5 < -180.0F; f5 += 360.0F) {
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

            this.V = this.yaw - f5;
            if (f5 * f5 > 2500.0F) {
                this.V += f5 * 0.2F;
            }

            if (flag) {
                f2 *= -1.0F;
            }
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.V - this.W < -180.0F) {
            this.W -= 360.0F;
        }

        while (this.V - this.W >= 180.0F) {
            this.W += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.X - this.Y < -180.0F) {
            this.Y -= 360.0F;
        }

        while (this.X - this.Y >= 180.0F) {
            this.Y += 360.0F;
        }

        this.ab += f2;
    }

    protected void b(float f, float f1) {
        super.b(f, f1);
    }

    // CraftBukkit start - delegate so we can handle providing a reason for health being regained
    public void heal(int i) {
        heal(i, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(int i, EntityRegainHealthEvent.RegainReason regainReason) {
        if (this.health > 0) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i, regainReason);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.health += event.getAmount();
            }
            // CraftBukkit end

            if (this.health > this.getMaxHealth()) {
                this.health = this.getMaxHealth();
            }

            this.noDamageTicks = this.maxNoDamageTicks / 2;
        }
    }

    public abstract int getMaxHealth();

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int i) {
        this.health = i;
        if (i > this.getMaxHealth()) {
            i = this.getMaxHealth();
        }
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.aV = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aE = 1.5F;
                boolean flag = true;

                // CraftBukkit start
                if (damagesource instanceof EntityDamageSource) {
                    EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(this, damagesource, i);
                    if (event.isCancelled()) {
                        return false;
                    }
                    i = event.getDamage();
                }
                // CraftBukkit end

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.c(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.aq = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.c(damagesource, i);
                    this.hurtTicks = this.at = 10;
                }

                this.au = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.a((EntityLiving) entity);
                    }

                    if (entity instanceof EntityHuman) {
                        this.lastDamageByPlayerTime = 60;
                        this.killer = (EntityHuman) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 60;
                            this.killer = null;
                        }
                    }
                }

                if (flag) {
                    this.world.broadcastEntityEffect(this, (byte) 2);
                    this.aW();
                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.au = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.au = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.k(), this.p(), this.A());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.world.makeSound(this, this.j(), this.p(), this.A());
                }

                return true;
            }
        }
    }

    private float A() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int T() {
        return 0;
    }

    protected void f(int i) {}

    protected int d(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.T();
            int k = i * j + this.ar;

            this.f(i);
            i = k / 25;
            this.ar = k % 25;
        }

        return i;
    }

    protected int b(DamageSource damagesource, int i) {
        if (this.hasEffect(MobEffectList.RESISTANCE)) {
            int j = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + this.ar;

            i = l / 25;
            this.ar = l % 25;
        }

        return i;
    }

    protected void c(DamageSource damagesource, int i) {
        i = this.d(damagesource, i);
        i = this.b(damagesource, i);
        this.health -= i;
    }

    protected float p() {
        return 1.0F;
    }

    protected String i() {
        return null;
    }

    protected String j() {
        return "damage.hurtflesh";
    }

    protected String k() {
        return "damage.hurtflesh";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.ce = true;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f1 = 0.4F;

        this.motX /= 2.0D;
        this.motY /= 2.0D;
        this.motZ /= 2.0D;
        this.motX -= d0 / (double) f * (double) f1;
        this.motY += (double) f1;
        this.motZ -= d1 / (double) f * (double) f1;
        if (this.motY > 0.4000000059604645D) {
            this.motY = 0.4000000059604645D;
        }
    }

    public void die(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();

        if (this.aj >= 0 && entity != null) {
            entity.b(this, this.aj);
        }

        if (entity != null) {
            entity.c(this);
        }

        this.az = true;
        if (!this.world.isStatic) {
            int i = 0;

            if (entity instanceof EntityHuman) {
                i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel(((EntityHuman) entity).inventory);
            }

            if (!this.isBaby()) {
                this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
                if (false && this.lastDamageByPlayerTime > 0) { // CraftBukkit - move rare item drop call to dropDeathLoot
                    int j = this.random.nextInt(200) - i;

                    if (j < 5) {
                        this.b(j <= 0 ? 1 : 0);
                    }
                }
            } else { // CraftBukkit
                CraftEventFactory.callEntityDeathEvent(this); // CraftBukkit
            }
        }

        this.world.broadcastEntityEffect(this, (byte) 3);
    }

    // CraftBukkit start - change return type to ItemStack
    protected ItemStack b(int i) {
        return null;
    }
    // CraftBukkit end

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.getLootId();

        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        if (j > 0) {
            int k = this.random.nextInt(3);

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            if (k > 0) {
                loot.add(new org.bukkit.inventory.ItemStack(j, k));
            }
        }

        // Determine rare item drops and add them to the loot
        if (this.lastDamageByPlayerTime > 0) {
            int k = this.random.nextInt(200) - i;

            if (k < 5) {
                ItemStack itemstack = this.b(k <= 0 ? 1 : 0);
                if (itemstack != null) {
                    loot.add(new org.bukkit.craftbukkit.inventory.CraftItemStack(itemstack));
                }
            }
        }

        CraftEventFactory.callEntityDeathEvent(this, loot); // raise event even for those times when the entity does not drop loot
        // CraftBukkit end
    }

    protected int getLootId() {
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
                i = event.getDamage();

                if (i > 4) {
                    this.world.makeSound(this, "damage.fallbig", 1.0F, 1.0F);
                } else {
                    this.world.makeSound(this, "damage.fallsmall", 1.0F, 1.0F);
                }
                this.getBukkitEntity().setLastDamageCause(event);
                this.damageEntity(DamageSource.FALL, i);
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

        if (this.aU()) {
            d0 = this.locY;
            this.a(f, f1, this.c_() ? 0.04F : 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.aV()) {
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
            float f4;

            if (this.onGround) {
                if (this.c_()) {
                    f4 = this.as();
                } else {
                    f4 = this.al;
                }

                f4 *= f3;
            } else {
                f4 = this.am;
            }

            this.a(f, f1, f4);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            if (this.t()) {
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

                boolean flag = this.isSneaking() && this instanceof EntityHuman;

                if (flag && this.motY < 0.0D) {
                    this.motY = 0.0D;
                }
            }

            this.move(this.motX, this.motY, this.motZ);
            if (this.positionChanged && this.t()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.aD = this.aE;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aE += (f6 - this.aE) * 0.4F;
        this.aF += this.aE;
    }

    public boolean t() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);
        int l = this.world.getTypeId(i, j, k);

        return l == Block.LADDER.id || l == Block.VINE.id;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.health);
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setShort("AttackTime", (short) this.attackTicks);
        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Id", (byte) mobeffect.getEffectId());
                nbttagcompound1.setByte("Amplifier", (byte) mobeffect.getAmplifier());
                nbttagcompound1.setInt("Duration", mobeffect.getDuration());
                nbttaglist.add(nbttagcompound1);
            }

            nbttagcompound.set("ActiveEffects", nbttaglist);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        if (this.health < -32768) {
            this.health = -32768;
        }

        this.health = nbttagcompound.getShort("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = this.getMaxHealth();
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");
        if (nbttagcompound.hasKey("ActiveEffects")) {
            NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects");

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                byte b0 = nbttagcompound1.getByte("Id");
                byte b1 = nbttagcompound1.getByte("Amplifier");
                int j = nbttagcompound1.getInt("Duration");

                this.effects.put(Integer.valueOf(b0), new MobEffect(b0, j, b1));
            }
        }
    }

    public boolean isAlive() {
        return !this.dead && this.health > 0;
    }

    public boolean f_() {
        return false;
    }

    public void e(float f) {
        this.aX = f;
    }

    public void f(boolean flag) {
        this.aZ = flag;
    }

    public void e() {
        if (this.q > 0) {
            --this.q;
        }

        if (this.aN > 0) {
            double d0 = this.locX + (this.aO - this.locX) / (double) this.aN;
            double d1 = this.locY + (this.aP - this.locY) / (double) this.aN;
            double d2 = this.locZ + (this.aQ - this.locZ) / (double) this.aN;

            double d3;

            for (d3 = this.aR - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aN);
            this.pitch = (float) ((double) this.pitch + (this.aS - (double) this.pitch) / (double) this.aN);
            --this.aN;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
            // CraftBukkit start - getCubes is expensive, use an approximation
            if (this.world.getTypeId(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2)) != 0) {
                d1 += 1.0D;
                this.setPosition(d0, d1, d2);
            }
            /*List list = this.world.getCubes(this, this.boundingBox.shrink(0.03125D, 0.0D, 0.03125D));

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
            }*/
            // CraftBukkit end
        }

        // MethodProfiler.a("ai"); // CraftBukkit - not in production code
        if (this.Q()) {
            this.aZ = false;
            this.aW = 0.0F;
            this.aX = 0.0F;
            this.aY = 0.0F;
        } else if (this.aF()) {
            if (this.c_()) {
                // MethodProfiler.a("newAi"); // CraftBukkit - not in production code
                this.z_();
                // MethodProfiler.a(); // CraftBukkit - not in production code
            } else {
                // MethodProfiler.a("oldAi"); // CraftBukkit - not in production code
                this.d_();
                // MethodProfiler.a(); // CraftBukkit - not in production code
                this.X = this.yaw;
            }
        }

        // MethodProfiler.a(); // CraftBukkit - not in production code
        boolean flag = this.aU();
        boolean flag1 = this.aV();

        if (this.aZ) {
            if (flag) {
                this.motY += 0.03999999910593033D;
            } else if (flag1) {
                this.motY += 0.03999999910593033D;
            } else if (this.onGround && this.q == 0) {
                this.ac();
                this.q = 10;
            }
        } else {
            this.q = 0;
        }

        this.aW *= 0.98F;
        this.aX *= 0.98F;
        this.aY *= 0.9F;
        float f = this.al;

        this.al *= this.J();
        this.a(this.aW, this.aX);
        this.al = f;
        // MethodProfiler.a("push"); // CraftBukkit - not in production code
        List list1 = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list1 != null && list1.size() > 0) {
            for (int j = 0; j < list1.size(); ++j) {
                Entity entity = (Entity) list1.get(j);

                if (entity.e_()) {
                    entity.collide(this);
                }
            }
        }

        // MethodProfiler.a(); // CraftBukkit - not in production code
    }

    protected boolean c_() {
        return false;
    }

    protected boolean aF() {
        return !this.world.isStatic;
    }

    protected boolean Q() {
        return this.health <= 0;
    }

    public boolean P() {
        return false;
    }

    protected void ac() {
        this.motY = 0.41999998688697815D;
        if (this.hasEffect(MobEffectList.JUMP)) {
            this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.ce = true;
    }

    protected boolean n() {
        return true;
    }

    protected void aG() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.n() && d3 > 16384.0D) {
                this.die();
            }

            if (this.aV > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D && this.n()) {
                this.die();
            } else if (d3 < 1024.0D) {
                this.aV = 0;
            }
        }
    }

    protected void z_() {
        ++this.aV;
        //MethodProfiler.a("checkDespawn"); // CraftBukkit - not in production code
        this.aG();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("sensing"); // CraftBukkit - not in production code
        this.m.a();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("targetSelector"); // CraftBukkit - not in production code
        this.targetSelector.a();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("goalSelector"); // CraftBukkit - not in production code
        this.goalSelector.a();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("navigation"); // CraftBukkit - not in production code
        this.navigation.d();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("mob tick"); // CraftBukkit - not in production code
        this.g();
        //MethodProfiler.a(); // CraftBukkit - not in production code
        //MethodProfiler.a("controls"); // CraftBukkit - not in production code
        this.moveController.c();
        this.lookController.a();
        this.jumpController.b();
        //MethodProfiler.a(); // CraftBukkit - not in production code
    }

    protected void g() {}

    protected void d_() {
        ++this.aV;
        this.aG();
        this.aW = 0.0F;
        this.aX = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, (double) f);

            if (entityhuman != null) {
                this.r = entityhuman;
                this.bc = 10 + this.random.nextInt(20);
            } else {
                this.aY = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.r != null) {
            this.a(this.r, 10.0F, (float) this.D());
            if (this.bc-- <= 0 || this.r.dead || this.r.j(this) > (double) (f * f)) {
                this.r = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.aY = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.aY;
            this.pitch = this.ba;
        }

        boolean flag = this.aU();
        boolean flag1 = this.aV();

        if (flag || flag1) {
            this.aZ = this.random.nextFloat() < 0.8F;
        }
    }

    public int D() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = this.locY + (double) this.getHeadHeight() - (entityliving.locY + (double) entityliving.getHeadHeight());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.getHeadHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.pitch = -this.b(this.pitch, f3, f1);
        this.yaw = this.b(this.yaw, f2, f);
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

    public void aH() {}

    public boolean canSpawn() {
        return this.world.containsEntity(this.boundingBox) && this.world.getCubes(this, this.boundingBox).size() == 0 && !this.world.containsLiquid(this.boundingBox);
    }

    protected void aI() {
        // CraftBukkit start
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, this.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, 4);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        event.getEntity().setLastDamageCause(event);
        this.damageEntity(DamageSource.OUT_OF_WORLD, event.getDamage());
        // CraftBukkit end
    }

    public Vec3D aJ() {
        return this.f(1.0F);
    }

    public Vec3D f(float f) {
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

    public int q() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void aK() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!mobeffect.tick(this) && !this.world.isStatic) {
                iterator.remove();
                this.d(mobeffect);
            }
        }

        int i;

        if (this.e) {
            if (!this.world.isStatic) {
                if (!this.effects.isEmpty()) {
                    i = PotionBrewer.a(this.effects.values());
                    this.datawatcher.watch(8, Integer.valueOf(i));
                } else {
                    this.datawatcher.watch(8, Integer.valueOf(0));
                }
            }

            this.e = false;
        }

        if (this.random.nextBoolean()) {
            i = this.datawatcher.getInt(8);
            if (i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.world.a("mobSpell", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.locY + this.random.nextDouble() * (double) this.length - (double) this.height, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2);
            }
        }
    }

    public void aL() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!this.world.isStatic) {
                iterator.remove();
                this.d(mobeffect);
            }
        }
    }

    public Collection getEffects() {
        return this.effects.values();
    }

    public boolean hasEffect(MobEffectList mobeffectlist) {
        return this.effects.containsKey(Integer.valueOf(mobeffectlist.id));
    }

    public MobEffect getEffect(MobEffectList mobeffectlist) {
        return (MobEffect) this.effects.get(Integer.valueOf(mobeffectlist.id));
    }

    public void addEffect(MobEffect mobeffect) {
        if (this.a(mobeffect)) {
            if (this.effects.containsKey(Integer.valueOf(mobeffect.getEffectId()))) {
                ((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
                this.c((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId())));
            } else {
                this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                this.b(mobeffect);
            }
        }
    }

    public boolean a(MobEffect mobeffect) {
        if (this.getMonsterType() == MonsterType.UNDEAD) {
            int i = mobeffect.getEffectId();

            if (i == MobEffectList.REGENERATION.id || i == MobEffectList.POISON.id) {
                return false;
            }
        }

        return true;
    }

    public boolean aN() {
        return this.getMonsterType() == MonsterType.UNDEAD;
    }

    protected void b(MobEffect mobeffect) {
        this.e = true;
    }

    protected void c(MobEffect mobeffect) {
        this.e = true;
    }

    protected void d(MobEffect mobeffect) {
        this.e = true;
    }

    protected float J() {
        float f = 1.0F;

        if (this.hasEffect(MobEffectList.FASTER_MOVEMENT)) {
            f *= 1.0F + 0.2F * (float) (this.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1);
        }

        if (this.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
            f *= 1.0F - 0.15F * (float) (this.getEffect(MobEffectList.SLOWER_MOVEMENT).getAmplifier() + 1);
        }

        return f;
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
    }

    public boolean isBaby() {
        return false;
    }

    public MonsterType getMonsterType() {
        return MonsterType.UNDEFINED;
    }

    public void c(ItemStack itemstack) {
        this.world.makeSound(this, "random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3D vec3d = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d.b(-this.yaw * 3.1415927F / 180.0F);
            Vec3D vec3d1 = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

            vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
            vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ);
            this.world.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
        }
    }
}
