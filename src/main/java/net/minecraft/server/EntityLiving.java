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
    public float ao;
    public float ap;
    public float aq = 0.0F;
    public float ar = 0.0F;
    public float as = 0.0F;
    public float at = 0.0F;
    protected float au;
    protected float av;
    protected float aw;
    protected float ax;
    protected boolean ay = true;
    protected String texture = "/mob/char.png";
    protected boolean aA = true;
    protected float aB = 0.0F;
    protected String aC = null;
    protected float aD = 1.0F;
    protected int aE = 0;
    protected float aF = 0.0F;
    public float aG = 0.1F;
    public float aH = 0.02F;
    public float aI;
    public float aJ;
    protected int health = this.getMaxHealth();
    public int aL;
    protected int aM;
    private int a;
    public int hurtTicks;
    public int aO;
    public float aP = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float aS;
    public float aT;
    protected boolean aU = false;
    protected int aV;
    public int aW = -1;
    public float aX = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aY;
    public float aZ;
    public float ba;
    public EntityHuman killer = null; // CraftBukkit - protected -> public
    protected int lastDamageByPlayerTime = 0;
    public EntityLiving lastDamager = null; // CraftBukkit - private -> public
    private int c = 0;
    private EntityLiving d = null;
    public int bd = 0;
    public int be = 0;
    public HashMap effects = new HashMap(); // CraftBukkit - protected -> public
    public boolean updateEffects = true; // CraftBukkit - private -> public
    private int f;
    private ControllerLook lookController;
    private ControllerMove moveController;
    private ControllerJump jumpController;
    private EntityAIBodyControl senses;
    private Navigation navigation;
    protected final PathfinderGoalSelector goalSelector;
    protected final PathfinderGoalSelector targetSelector;
    private EntityLiving bz;
    private EntitySenses bA;
    private float bB;
    private ChunkCoordinates bC = new ChunkCoordinates(0, 0, 0);
    private float bD = -1.0F;
    protected int bi;
    protected double bj;
    protected double bk;
    protected double bl;
    protected double bm;
    protected double bn;
    float bo = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int bq = 0;
    protected float br;
    protected float bs;
    protected float bt;
    protected boolean bu = false;
    protected float bv = 0.0F;
    protected float bw = 0.7F;
    private int bE = 0;
    private Entity bF;
    protected int bx = 0;
    public int expToDrop = 0; // CraftBukkit
    public int maxAirTicks = 300; // CraftBukkit

    public EntityLiving(World world) {
        super(world);
        this.m = true;
        this.goalSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.targetSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.lookController = new ControllerLook(this);
        this.moveController = new ControllerMove(this);
        this.jumpController = new ControllerJump(this);
        this.senses = new EntityAIBodyControl(this);
        this.navigation = new Navigation(this, world, 16.0F);
        this.bA = new EntitySenses(this);
        this.ap = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.ao = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.as = this.yaw;
        this.W = 0.5F;
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

    public Navigation getNavigation() {
        return this.navigation;
    }

    public EntitySenses at() {
        return this.bA;
    }

    public Random au() {
        return this.random;
    }

    public EntityLiving av() {
        return this.lastDamager;
    }

    public EntityLiving aw() {
        return this.d;
    }

    public void j(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.d = (EntityLiving) entity;
        }
    }

    public int ax() {
        return this.bq;
    }

    public float am() {
        return this.as;
    }

    public float ay() {
        return this.bB;
    }

    public void e(float f) {
        this.bB = f;
        this.f(f);
    }

    public boolean k(Entity entity) {
        this.j(entity);
        return false;
    }

    public EntityLiving az() {
        return this.bz;
    }

    public void b(EntityLiving entityliving) {
        this.bz = entityliving;
    }

    public boolean a(Class oclass) {
        return EntityCreeper.class != oclass && EntityGhast.class != oclass;
    }

    public void aA() {}

    public boolean aB() {
        return this.d(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean d(int i, int j, int k) {
        return this.bD == -1.0F ? true : this.bC.e(i, j, k) < this.bD * this.bD;
    }

    public void b(int i, int j, int k, int l) {
        this.bC.b(i, j, k);
        this.bD = (float) l;
    }

    public ChunkCoordinates aC() {
        return this.bC;
    }

    public float aD() {
        return this.bD;
    }

    public void aE() {
        this.bD = -1.0F;
    }

    public boolean aF() {
        return this.bD != -1.0F;
    }

    public void c(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.c = this.lastDamager != null ? 60 : 0;
    }

    protected void a() {
        this.datawatcher.a(8, Integer.valueOf(this.f));
    }

    public boolean l(Entity entity) {
        return this.world.a(Vec3D.a().create(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ), Vec3D.a().create(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ)) == null;
    }

    public boolean L() {
        return !this.dead;
    }

    public boolean M() {
        return !this.dead;
    }

    public float getHeadHeight() {
        return this.length * 0.85F;
    }

    public int aG() {
        return 80;
    }

    public void aH() {
        String s = this.aQ();

        if (s != null) {
            this.world.makeSound(this, s, this.aP(), this.i());
        }
    }

    public void z() {
        this.aI = this.aJ;
        super.z();
        // this.world.methodProfiler.a("mobBaseTick"); // CraftBukkit - not in production code
        if (this.isAlive() && this.random.nextInt(1000) < this.a++) {
            this.a = -this.aG();
            this.aH();
        }

        // CraftBukkit start
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

        if (this.isAlive() && this.a(Material.WATER) && !this.aU() && !this.effects.containsKey(Integer.valueOf(MobEffectList.WATER_BREATHING.id))) {
            this.setAirTicks(this.h(this.getAirTicks()));
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

        this.aS = this.aT;
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
            this.aI();
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
                this.c((EntityLiving) null);
            } else if (this.c > 0) {
                --this.c;
            } else {
                this.c((EntityLiving) null);
            }
        }

        this.bo();
        this.ax = this.aw;
        this.ar = this.aq;
        this.at = this.as;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
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

    protected void aI() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.dead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - update getExpReward() above if the removed if() changes!
            i = expToDrop;
            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }
            // CraftBukkit end

            this.die();

            for (i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    protected int h(int i) {
        return i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return this.aV;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void aK() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.a("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void U() {
        super.U();
        this.au = this.av;
        this.av = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void h_() {
        super.h_();
        if (this.bd > 0) {
            if (this.be <= 0) {
                this.be = 60;
            }

            --this.be;
            if (this.be <= 0) {
                --this.bd;
            }
        }

        this.d();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.aq;
        float f2 = 0.0F;

        this.au = this.av;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aJ > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.av += (f3 - this.av) * 0.3F;
        // this.world.methodProfiler.a("headTurn"); // CraftBukkit - not in production code
        if (this.aV()) {
            this.senses.a();
        } else {
            float f4 = MathHelper.g(f1 - this.aq);

            this.aq += f4 * 0.3F;
            float f5 = MathHelper.g(this.yaw - this.aq);
            boolean flag = f5 < -90.0F || f5 >= 90.0F;

            if (f5 < -75.0F) {
                f5 = -75.0F;
            }

            if (f5 >= 75.0F) {
                f5 = 75.0F;
            }

            this.aq = this.yaw - f5;
            if (f5 * f5 > 2500.0F) {
                this.aq += f5 * 0.2F;
            }

            if (flag) {
                f2 *= -1.0F;
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("rangeChecks"); // CraftBukkit - not in production code

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.aq - this.ar < -180.0F) {
            this.ar -= 360.0F;
        }

        while (this.aq - this.ar >= 180.0F) {
            this.ar += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.as - this.at < -180.0F) {
            this.at -= 360.0F;
        }

        while (this.as - this.at >= 180.0F) {
            this.at += 360.0F;
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        this.aw += f2;
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
            this.bq = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aZ = 1.5F;
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

                    this.d(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.aL = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.d(damagesource, i);
                    this.hurtTicks = this.aO = 10;
                }

                this.aP = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.c((EntityLiving) entity);
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
                    if (damagesource != DamageSource.DROWN && damagesource != DamageSource.EXPLOSION2) {
                        this.K();
                    }

                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.aP = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.aP = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound(this, this.aS(), this.aP(), this.i());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.world.makeSound(this, this.aR(), this.aP(), this.i());
                }

                return true;
            }
        }
    }

    private float i() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int aO() {
        return 0;
    }

    protected void k(int i) {}

    protected int b(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.aO();
            int k = i * j + this.aM;

            this.k(i);
            i = k / 25;
            this.aM = k % 25;
        }

        return i;
    }

    protected int c(DamageSource damagesource, int i) {
        if (this.hasEffect(MobEffectList.RESISTANCE)) {
            int j = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + this.aM;

            i = l / 25;
            this.aM = l % 25;
        }

        return i;
    }

    protected void d(DamageSource damagesource, int i) {
        i = this.b(damagesource, i);
        i = this.c(damagesource, i);
        this.health -= i;
    }

    protected float aP() {
        return 1.0F;
    }

    protected String aQ() {
        return null;
    }

    protected String aR() {
        return "damage.hurtflesh";
    }

    protected String aS() {
        return "damage.hurtflesh";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.al = true;
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

        if (this.aE >= 0 && entity != null) {
            entity.c(this, this.aE);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.aU = true;
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
                        this.l(j <= 0 ? 1 : 0);
                    }
                }
            } else { // CraftBukkit
                CraftEventFactory.callEntityDeathEvent(this); // CraftBukkit
            }
        }

        this.world.broadcastEntityEffect(this, (byte) 3);
    }

    // CraftBukkit start - change return type to ItemStack
    protected ItemStack l(int i) {
        return null;
    }
    // CraftBukkit end

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.getLootId();

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
                ItemStack itemstack = this.l(k <= 0 ? 1 : 0);
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
        int i = MathHelper.f(f - 3.0F);

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

    public void e(float f, float f1) {
        double d0;

        if (this.H() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
            d0 = this.locY;
            this.a(f, f1, this.aV() ? 0.04F : 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.J() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
            d0 = this.locY;
            this.a(f, f1, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
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
                if (this.aV()) {
                    f4 = this.ay();
                } else {
                    f4 = this.aG;
                }

                f4 *= f3;
            } else {
                f4 = this.aH;
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

            if (this.f_()) {
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
            if (this.positionChanged && this.f_()) {
                this.motY = 0.2D;
            }

            this.motY -= 0.08D;
            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.aY = this.aZ;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aZ += (f6 - this.aZ) * 0.4F;
        this.ba += this.aZ;
    }

    public boolean f_() {
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

    public boolean aU() {
        return false;
    }

    public void f(float f) {
        this.bs = f;
    }

    public void d(boolean flag) {
        this.bu = flag;
    }

    public void d() {
        if (this.bE > 0) {
            --this.bE;
        }

        if (this.bi > 0) {
            double d0 = this.locX + (this.bj - this.locX) / (double) this.bi;
            double d1 = this.locY + (this.bk - this.locY) / (double) this.bi;
            double d2 = this.locZ + (this.bl - this.locZ) / (double) this.bi;
            double d3 = MathHelper.g(this.bm - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.bi);
            this.pitch = (float) ((double) this.pitch + (this.bn - (double) this.pitch) / (double) this.bi);
            --this.bi;
            this.setPosition(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        }

        if (Math.abs(this.motX) < 0.005D) {
            this.motX = 0.0D;
        }

        if (Math.abs(this.motY) < 0.005D) {
            this.motY = 0.0D;
        }

        if (Math.abs(this.motZ) < 0.005D) {
            this.motZ = 0.0D;
        }

        // this.world.methodProfiler.a("ai"); // CraftBukkit - not in production code
        if (this.aX()) {
            this.bu = false;
            this.br = 0.0F;
            this.bs = 0.0F;
            this.bt = 0.0F;
        } else if (this.aW()) {
            if (this.aV()) {
                // this.world.methodProfiler.a("newAi"); // CraftBukkit - not in production code
                this.bc();
                // this.world.methodProfiler.b(); // CraftBukkit - not in production code
            } else {
                // this.world.methodProfiler.a("oldAi"); // CraftBukkit - not in production code
                this.be();
                // this.world.methodProfiler.b(); // CraftBukkit - not in production code
                this.as = this.yaw;
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("jump"); // CraftBukkit - not in production code
        if (this.bu) {
            if (!this.H() && !this.J()) {
                if (this.onGround && this.bE == 0) {
                    this.aZ();
                    this.bE = 10;
                }
            } else {
                this.motY += 0.03999999910593033D;
            }
        } else {
            this.bE = 0;
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("travel"); // CraftBukkit - not in production code
        this.br *= 0.98F;
        this.bs *= 0.98F;
        this.bt *= 0.9F;
        float f = this.aG;

        this.aG *= this.bs();
        this.e(this.br, this.bs);
        this.aG = f;
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("push"); // CraftBukkit - not in production code
        if (!this.world.isStatic) {
            List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && !list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (entity.M()) {
                        entity.collide(this);
                    }
                }
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected boolean aV() {
        return false;
    }

    protected boolean aW() {
        return !this.world.isStatic;
    }

    protected boolean aX() {
        return this.health <= 0;
    }

    public boolean aY() {
        return false;
    }

    protected void aZ() {
        this.motY = 0.41999998688697815D;
        if (this.hasEffect(MobEffectList.JUMP)) {
            this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.al = true;
    }

    protected boolean ba() {
        return true;
    }

    protected void bb() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.locX - this.locX;
            double d1 = entityhuman.locY - this.locY;
            double d2 = entityhuman.locZ - this.locZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.ba() && d3 > 16384.0D) {
                this.die();
            }

            if (this.bq > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D && this.ba()) {
                this.die();
            } else if (d3 < 1024.0D) {
                this.bq = 0;
            }
        }
    }

    protected void bc() {
        ++this.bq;
        // this.world.methodProfiler.a("checkDespawn"); // CraftBukkit - not in production code
        this.bb();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("sensing"); // CraftBukkit - not in production code
        this.bA.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("targetSelector"); // CraftBukkit - not in production code
        this.targetSelector.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("goalSelector"); // CraftBukkit - not in production code
        this.goalSelector.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("navigation"); // CraftBukkit - not in production code
        this.navigation.e();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("mob tick"); // CraftBukkit - not in production code
        this.bd();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("controls"); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("move"); // CraftBukkit - not in production code
        this.moveController.c();
        // this.world.methodProfiler.c("look"); // CraftBukkit - not in production code
        this.lookController.a();
        // this.world.methodProfiler.c("jump"); // CraftBukkit - not in production code
        this.jumpController.b();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected void bd() {}

    protected void be() {
        ++this.bq;
        this.bb();
        this.br = 0.0F;
        this.bs = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, (double) f);

            if (entityhuman != null) {
                this.bF = entityhuman;
                this.bx = 10 + this.random.nextInt(20);
            } else {
                this.bt = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.bF != null) {
            this.a(this.bF, 10.0F, (float) this.bf());
            if (this.bx-- <= 0 || this.bF.dead || this.bF.e((Entity) this) > (double) (f * f)) {
                this.bF = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.bt = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.bt;
            this.pitch = this.bv;
        }

        boolean flag = this.H();
        boolean flag1 = this.J();

        if (flag || flag1) {
            this.bu = this.random.nextFloat() < 0.8F;
        }
    }

    public int bf() {
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
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean canSpawn() {
        return this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox);
    }

    protected void C() {
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

    public Vec3D Z() {
        return this.i(1.0F);
    }

    public Vec3D i(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.cos(-this.yaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.sin(-this.yaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.cos(-this.pitch * 0.017453292F);
            f4 = MathHelper.sin(-this.pitch * 0.017453292F);
            return Vec3D.a().create((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);

            return Vec3D.a().create((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int bl() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void bo() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!mobeffect.tick(this) && !this.world.isStatic) {
                iterator.remove();
                this.c(mobeffect);
            }
        }

        int i;

        if (this.updateEffects) {
            if (!this.world.isStatic) {
                if (this.effects.isEmpty()) {
                    this.datawatcher.watch(8, Integer.valueOf(0));
                } else {
                    i = PotionBrewer.a(this.effects.values());
                    this.datawatcher.watch(8, Integer.valueOf(i));
                }
            }

            this.updateEffects = false;
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

    public void bp() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!this.world.isStatic) {
                iterator.remove();
                this.c(mobeffect);
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
        if (this.e(mobeffect)) {
            if (this.effects.containsKey(Integer.valueOf(mobeffect.getEffectId()))) {
                ((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
                this.b((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId())));
            } else {
                this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                this.a(mobeffect);
            }
        }
    }

    public boolean e(MobEffect mobeffect) {
        if (this.getMonsterType() == EnumMonsterType.UNDEAD) {
            int i = mobeffect.getEffectId();

            if (i == MobEffectList.REGENERATION.id || i == MobEffectList.POISON.id) {
                return false;
            }
        }

        return true;
    }

    public boolean br() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    protected void a(MobEffect mobeffect) {
        this.updateEffects = true;
    }

    protected void b(MobEffect mobeffect) {
        this.updateEffects = true;
    }

    protected void c(MobEffect mobeffect) {
        this.updateEffects = true;
    }

    protected float bs() {
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

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEFINED;
    }

    public void a(ItemStack itemstack) {
        this.world.makeSound(this, "random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3D vec3d = Vec3D.a().create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d.b(-this.yaw * 3.1415927F / 180.0F);
            Vec3D vec3d1 = Vec3D.a().create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

            vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
            vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ);
            this.world.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
        }
    }
}
