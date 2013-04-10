package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    private static final float[] b = new float[] { 0.0F, 0.0F, 0.1F, 0.2F};
    private static final float[] c = new float[] { 0.0F, 0.0F, 0.25F, 0.5F};
    private static final float[] d = new float[] { 0.0F, 0.0F, 0.05F, 0.02F};
    public static final float[] au = new float[] { 0.0F, 0.1F, 0.15F, 0.45F};
    public int maxNoDamageTicks = 20;
    public float aw;
    public float ax;
    public float ay = 0.0F;
    public float az = 0.0F;
    public float aA = 0.0F;
    public float aB = 0.0F;
    protected float aC;
    protected float aD;
    protected float aE;
    protected float aF;
    protected boolean aG = true;
    protected String texture = "/mob/char.png";
    protected boolean aI = true;
    protected float aJ = 0.0F;
    protected String aK = null;
    protected float aL = 1.0F;
    protected int aM = 0;
    protected float aN = 0.0F;
    public float aO = 0.1F;
    public float aP = 0.02F;
    public float aQ;
    public float aR;
    protected int health = this.getMaxHealth();
    public int aT;
    protected int aU;
    public int aV;
    public int hurtTicks;
    public int aX;
    public float aY = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float bb;
    public float bc;
    protected boolean bd = false;
    protected int be;
    public int bf = -1;
    public float bg = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float bh;
    public float bi;
    public float bj;
    public EntityHuman killer = null; // CraftBukkit - protected -> public
    protected int lastDamageByPlayerTime = 0;
    public EntityLiving lastDamager = null; // CraftBukkit - private -> public
    private int f = 0;
    private EntityLiving g = null;
    public int bm = 0;
    public HashMap effects = new HashMap(); // CraftBukkit - protected -> public
    public boolean updateEffects = true; // CraftBukkit - private -> public
    private int i;
    private ControllerLook lookController;
    private ControllerMove moveController;
    private ControllerJump jumpController;
    private EntityAIBodyControl senses;
    private Navigation navigation;
    protected final PathfinderGoalSelector goalSelector;
    protected final PathfinderGoalSelector targetSelector;
    private EntityLiving goalTarget;
    private EntitySenses bP;
    private float bQ;
    private ChunkCoordinates bR = new ChunkCoordinates(0, 0, 0);
    private float bS = -1.0F;
    private ItemStack[] equipment = new ItemStack[5];
    public float[] dropChances = new float[5]; // CraftBukkit - protected -> public
    private ItemStack[] bU = new ItemStack[5];
    public boolean br = false;
    public int bs = 0;
    public boolean canPickUpLoot = false; // CraftBukkit - protected -> public
    public boolean persistent = !this.isTypeNotPersistent(); // CraftBukkit - private -> public, change value
    protected final CombatTracker bt = new CombatTracker(this);
    protected int bu;
    protected double bv;
    protected double bw;
    protected double bx;
    protected double by;
    protected double bz;
    float bA = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int bC = 0;
    protected float bD;
    protected float bE;
    protected float bF;
    protected boolean bG = false;
    protected float bH = 0.0F;
    protected float bI = 0.7F;
    private int bX = 0;
    private Entity bY;
    protected int bJ = 0;
    // CraftBukkit start
    public int expToDrop = 0;
    public int maxAirTicks = 300;
    public int maxHealth = this.getMaxHealth();
    // CraftBukkit end

    public EntityLiving(World world) {
        super(world);
        this.m = true;
        this.goalSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.targetSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.lookController = new ControllerLook(this);
        this.moveController = new ControllerMove(this);
        this.jumpController = new ControllerJump(this);
        this.senses = new EntityAIBodyControl(this);
        this.navigation = new Navigation(this, world, (float) this.ay());
        this.bP = new EntitySenses(this);
        this.ax = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.aw = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.aA = this.yaw;

        for (int i = 0; i < this.dropChances.length; ++i) {
            this.dropChances[i] = 0.085F;
        }

        this.Y = 0.5F;
    }

    protected int ay() {
        return 16;
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

    public EntitySenses aD() {
        return this.bP;
    }

    public Random aE() {
        return this.random;
    }

    public EntityLiving aF() {
        return this.lastDamager;
    }

    public EntityLiving aG() {
        return this.g;
    }

    public void l(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.g = (EntityLiving) entity;
        }
    }

    public int aH() {
        return this.bC;
    }

    public float ao() {
        return this.aA;
    }

    public float aI() {
        return this.bQ;
    }

    public void e(float f) {
        this.bQ = f;
        this.f(f);
    }

    public boolean m(Entity entity) {
        this.l(entity);
        return false;
    }

    public EntityLiving getGoalTarget() {
        return this.goalTarget;
    }

    public void setGoalTarget(EntityLiving entityliving) {
        this.goalTarget = entityliving;
    }

    public boolean a(Class oclass) {
        return EntityCreeper.class != oclass && EntityGhast.class != oclass;
    }

    public void aK() {}

    protected void a(double d0, boolean flag) {
        if (!this.G()) {
            this.H();
        }

        if (flag && this.fallDistance > 0.0F) {
            int i = MathHelper.floor(this.locX);
            int j = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
            int k = MathHelper.floor(this.locZ);
            int l = this.world.getTypeId(i, j, k);

            if (l == 0) {
                int i1 = this.world.e(i, j - 1, k);

                if (i1 == 11 || i1 == 32 || i1 == 21) {
                    l = this.world.getTypeId(i, j - 1, k);
                }
            }

            if (l > 0) {
                Block.byId[l].a(this.world, i, j, k, this, this.fallDistance);
            }
        }

        super.a(d0, flag);
    }

    public boolean aL() {
        return this.d(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean d(int i, int j, int k) {
        return this.bS == -1.0F ? true : this.bR.e(i, j, k) < this.bS * this.bS;
    }

    public void b(int i, int j, int k, int l) {
        this.bR.b(i, j, k);
        this.bS = (float) l;
    }

    public ChunkCoordinates aM() {
        return this.bR;
    }

    public float aN() {
        return this.bS;
    }

    public void aO() {
        this.bS = -1.0F;
    }

    public boolean aP() {
        return this.bS != -1.0F;
    }

    public void c(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.f = this.lastDamager != null ? 100 : 0;
    }

    protected void a() {
        this.datawatcher.a(8, Integer.valueOf(this.i));
        this.datawatcher.a(9, Byte.valueOf((byte) 0));
        this.datawatcher.a(10, Byte.valueOf((byte) 0));
        this.datawatcher.a(6, Byte.valueOf((byte) 0));
        this.datawatcher.a(5, "");
    }

    public boolean n(Entity entity) {
        return this.world.a(this.world.getVec3DPool().create(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ), this.world.getVec3DPool().create(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ)) == null;
    }

    public boolean K() {
        return !this.dead;
    }

    public boolean L() {
        return !this.dead;
    }

    public float getHeadHeight() {
        return this.length * 0.85F;
    }

    public int aQ() {
        return 80;
    }

    public void aR() {
        String s = this.bb();

        if (s != null) {
            this.makeSound(s, this.ba(), this.aY());
        }
    }

    public void x() {
        this.aQ = this.aR;
        super.x();
        this.world.methodProfiler.a("mobBaseTick");
        if (this.isAlive() && this.random.nextInt(1000) < this.aV++) {
            this.aV = -this.aQ();
            this.aR();
        }

        if (this.isAlive() && this.inBlock()) {
            this.damageEntity(DamageSource.STUCK, 1);
        }

        if (this.isFireproof() || this.world.isStatic) {
            this.extinguish();
        }

        boolean flag = this instanceof EntityHuman && ((EntityHuman) this).abilities.isInvulnerable;

        if (this.isAlive() && this.a(Material.WATER) && !this.bf() && !this.effects.containsKey(Integer.valueOf(MobEffectList.WATER_BREATHING.id)) && !flag) {
            this.setAirTicks(this.h(this.getAirTicks()));
            if (this.getAirTicks() == -20) {
                this.setAirTicks(0);

                for (int i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.world.addParticle("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }

                this.damageEntity(DamageSource.DROWN, 2);
            }

            this.extinguish();
        } else {
            // CraftBukkit start - Only set if needed to work around a DataWatcher inefficiency
            if (this.getAirTicks() != 300) {
                this.setAirTicks(maxAirTicks);
            }
            // CraftBukkit end
        }

        this.bb = this.bc;
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
            this.aS();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.killer = null;
        }

        if (this.g != null && !this.g.isAlive()) {
            this.g = null;
        }

        if (this.lastDamager != null) {
            if (!this.lastDamager.isAlive()) {
                this.c((EntityLiving) null);
            } else if (this.f > 0) {
                --this.f;
            } else {
                this.c((EntityLiving) null);
            }
        }

        this.bA();
        this.aF = this.aE;
        this.az = this.ay;
        this.aB = this.aA;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.world.methodProfiler.b();
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

    public int getScaledHealth() {
        if (this.maxHealth != this.getMaxHealth() && this.getHealth() > 0) {
            return this.getHealth() * this.getMaxHealth() / this.maxHealth + 1;
        } else {
            return this.getHealth();
        }
    }
    // CraftBukkit end

    protected void aS() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.dead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - Update getExpReward() above if the removed if() changes!
            i = this.expToDrop;
            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }
            this.expToDrop = 0;
            // CraftBukkit end

            this.die();

            for (i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.addParticle("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    protected int h(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        if (this.be > 0) {
            int i = this.be;
            ItemStack[] aitemstack = this.getEquipment();

            for (int j = 0; j < aitemstack.length; ++j) {
                if (aitemstack[j] != null && this.dropChances[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            return i;
        } else {
            return this.be;
        }
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void aU() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.addParticle("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void T() {
        super.T();
        this.aC = this.aD;
        this.aD = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void l_() {
        super.l_();
        if (!this.world.isStatic) {
            int i;

            for (i = 0; i < 5; ++i) {
                ItemStack itemstack = this.getEquipment(i);

                if (!ItemStack.matches(itemstack, this.bU[i])) {
                    ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new Packet5EntityEquipment(this.id, i, itemstack)));
                    this.bU[i] = itemstack == null ? null : itemstack.cloneItemStack();
                }
            }

            i = this.bM();
            if (i > 0) {
                if (this.bm <= 0) {
                    this.bm = 20 * (30 - i);
                }

                --this.bm;
                if (this.bm <= 0) {
                    this.r(i - 1);
                }
            }
        }

        this.c();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.ay;
        float f2 = 0.0F;

        this.aC = this.aD;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aR > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aD += (f3 - this.aD) * 0.3F;
        this.world.methodProfiler.a("headTurn");
        if (this.bh()) {
            this.senses.a();
        } else {
            float f4 = MathHelper.g(f1 - this.ay);

            this.ay += f4 * 0.3F;
            float f5 = MathHelper.g(this.yaw - this.ay);
            boolean flag = f5 < -90.0F || f5 >= 90.0F;

            if (f5 < -75.0F) {
                f5 = -75.0F;
            }

            if (f5 >= 75.0F) {
                f5 = 75.0F;
            }

            this.ay = this.yaw - f5;
            if (f5 * f5 > 2500.0F) {
                this.ay += f5 * 0.2F;
            }

            if (flag) {
                f2 *= -1.0F;
            }
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("rangeChecks");

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.ay - this.az < -180.0F) {
            this.az -= 360.0F;
        }

        while (this.ay - this.az >= 180.0F) {
            this.az += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.aA - this.aB < -180.0F) {
            this.aB -= 360.0F;
        }

        while (this.aA - this.aB >= 180.0F) {
            this.aB += 360.0F;
        }

        this.world.methodProfiler.b();
        this.aE += f2;
    }

    // CraftBukkit start - Delegate so we can handle providing a reason for health being regained
    public void heal(int i) {
        heal(i, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(int i, EntityRegainHealthEvent.RegainReason regainReason) {
        if (this.health > 0) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i, regainReason);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.setHealth(this.getHealth() + event.getAmount());
            }

            // this.getMaxHealth() -> this.maxHealth
            if (this.health > this.maxHealth) {
                this.setHealth(this.maxHealth);
                // CraftBukkit end
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
        if (this.isInvulnerable()) {
            return false;
        } else if (this.world.isStatic) {
            return false;
        } else {
            this.bC = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.m() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
                    this.getEquipment(4).damage(i * 4 + this.random.nextInt(i * 2), this);
                    i = (int) ((float) i * 0.75F);
                }

                this.bi = 1.5F;
                boolean flag = true;

                // CraftBukkit start
                EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(this, damagesource, i);
                if (event != null) {
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
                    this.aT = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.d(damagesource, i);
                    this.hurtTicks = this.aX = 10;
                }

                this.aY = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.c((EntityLiving) entity);
                    }

                    if (entity instanceof EntityHuman) {
                        this.lastDamageByPlayerTime = 100;
                        this.killer = (EntityHuman) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 100;
                            this.killer = null;
                        }
                    }
                }

                if (flag) {
                    this.world.broadcastEntityEffect(this, (byte) 2);
                    if (damagesource != DamageSource.DROWN) {
                        this.J();
                    }

                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.aY = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.aY = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.makeSound(this.bd(), this.ba(), this.aY());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.makeSound(this.bc(), this.ba(), this.aY());
                }

                return true;
            }
        }
    }

    protected float aY() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int aZ() {
        int i = 0;
        ItemStack[] aitemstack = this.getEquipment();
        int j = aitemstack.length;

        for (int k = 0; k < j; ++k) {
            ItemStack itemstack = aitemstack[k];

            if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                int l = ((ItemArmor) itemstack.getItem()).c;

                i += l;
            }
        }

        return i;
    }

    protected void k(int i) {}

    protected int b(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.aZ();
            int k = i * j + this.aU;

            this.k(i);
            i = k / 25;
            this.aU = k % 25;
        }

        return i;
    }

    protected int c(DamageSource damagesource, int i) {
        int j;
        int k;
        int l;

        if (this.hasEffect(MobEffectList.RESISTANCE)) {
            j = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            k = 25 - j;
            l = i * k + this.aU;
            i = l / 25;
            this.aU = l % 25;
        }

        if (i <= 0) {
            return 0;
        } else {
            j = EnchantmentManager.a(this.getEquipment(), damagesource);
            if (j > 20) {
                j = 20;
            }

            if (j > 0 && j <= 20) {
                k = 25 - j;
                l = i * k + this.aU;
                i = l / 25;
                this.aU = l % 25;
            }

            return i;
        }
    }

    protected void d(DamageSource damagesource, int i) {
        if (!this.isInvulnerable()) {
            i = this.b(damagesource, i);
            i = this.c(damagesource, i);
            int j = this.getHealth();

            this.health -= i;
            this.bt.a(damagesource, j, i);
        }
    }

    protected float ba() {
        return 1.0F;
    }

    protected String bb() {
        return null;
    }

    protected String bc() {
        return "damage.hit";
    }

    protected String bd() {
        return "damage.hit";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.an = true;
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
        EntityLiving entityliving = this.bN();

        if (this.aM >= 0 && entityliving != null) {
            entityliving.c(this, this.aM);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.bd = true;
        if (!this.world.isStatic) {
            int i = 0;

            if (entity instanceof EntityHuman) {
                i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
            }

            if (!this.isBaby() && this.world.getGameRules().getBoolean("doMobLoot")) {
                this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
                this.dropEquipment(this.lastDamageByPlayerTime > 0, i);
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

    // CraftBukkit start - Change return type to ItemStack
    protected ItemStack l(int i) {
        return null;
    }
    // CraftBukkit end

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - Whole method
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
                    loot.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack));
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

        // CraftBukkit start
        if (i > 0) {
            EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(null, this, EntityDamageEvent.DamageCause.FALL, i);
            if (!event.isCancelled()) {
                i = event.getDamage();
                if (i > 0) {
                    this.getBukkitEntity().setLastDamageCause(event);
                }
            }
        }
        // CraftBukkit end

        if (i > 0) {
            if (i > 4) {
                this.makeSound("damage.fallbig", 1.0F, 1.0F);
            } else {
                this.makeSound("damage.fallsmall", 1.0F, 1.0F);
            }

            this.damageEntity(DamageSource.FALL, i);
            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.makeSound(stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    public void e(float f, float f1) {
        double d0;

        if (this.G() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
            d0 = this.locY;
            this.a(f, f1, this.bh() ? 0.04F : 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.I() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
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
                if (this.bh()) {
                    f4 = this.aI();
                } else {
                    f4 = this.aO;
                }

                f4 *= f3;
            } else {
                f4 = this.aP;
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

            if (this.g_()) {
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
            if (this.positionChanged && this.g_()) {
                this.motY = 0.2D;
            }

            if (this.world.isStatic && (!this.world.isLoaded((int) this.locX, 0, (int) this.locZ) || !this.world.getChunkAtWorldCoords((int) this.locX, (int) this.locZ).d)) {
                if (this.locY > 0.0D) {
                    this.motY = -0.1D;
                } else {
                    this.motY = 0.0D;
                }
            } else {
                this.motY -= 0.08D;
            }

            this.motY *= 0.9800000190734863D;
            this.motX *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.bh = this.bi;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.bi += (f6 - this.bi) * 0.4F;
        this.bj += this.bi;
    }

    public boolean g_() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);
        int l = this.world.getTypeId(i, j, k);

        return l == Block.LADDER.id || l == Block.VINE.id;
    }

    public void b(NBTTagCompound nbttagcompound) {
        if (this.health < -32768) {
            this.health = -32768;
        }

        nbttagcompound.setShort("Health", (short) this.health);
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setShort("AttackTime", (short) this.attackTicks);
        nbttagcompound.setBoolean("CanPickUpLoot", this.bS());
        nbttagcompound.setBoolean("PersistenceRequired", this.persistent);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.equipment.length; ++i) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (this.equipment[i] != null) {
                this.equipment[i].save(nbttagcompound1);
            }

            nbttaglist.add(nbttagcompound1);
        }

        nbttagcompound.set("Equipment", nbttaglist);
        NBTTagList nbttaglist1;

        if (!this.effects.isEmpty()) {
            nbttaglist1 = new NBTTagList();
            Iterator iterator = this.effects.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                nbttaglist1.add(mobeffect.a(new NBTTagCompound()));
            }

            nbttagcompound.set("ActiveEffects", nbttaglist1);
        }

        nbttaglist1 = new NBTTagList();

        for (int j = 0; j < this.dropChances.length; ++j) {
            nbttaglist1.add(new NBTTagFloat(j + "", this.dropChances[j]));
        }

        nbttagcompound.set("DropChances", nbttaglist1);
        nbttagcompound.setString("CustomName", this.getCustomName());
        nbttagcompound.setBoolean("CustomNameVisible", this.getCustomNameVisible());
        nbttagcompound.setInt("Bukkit.MaxHealth", this.maxHealth); // CraftBukkit
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.getShort("Health");
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxHealth")) {
            this.maxHealth = nbttagcompound.getInt("Bukkit.MaxHealth");
        }
        // CraftBukkit end

        if (!nbttagcompound.hasKey("Health")) {
            this.health = this.maxHealth; // CraftBukkit - this.getMaxHealth() -> this.maxHealth
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");

        // CraftBukkit start - If looting or persistence is false only use it if it was set after we started using it
        boolean data = nbttagcompound.getBoolean("CanPickUpLoot");
        if (isLevelAtLeast(nbttagcompound, 1) || data) {
            this.canPickUpLoot = data;
        }

        data = nbttagcompound.getBoolean("PersistenceRequired");
        if (isLevelAtLeast(nbttagcompound, 1) || data) {
            this.persistent = data;
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("CustomName") && nbttagcompound.getString("CustomName").length() > 0) {
            this.setCustomName(nbttagcompound.getString("CustomName"));
        }

        this.setCustomNameVisible(nbttagcompound.getBoolean("CustomNameVisible"));
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKey("Equipment")) {
            nbttaglist = nbttagcompound.getList("Equipment");

            for (i = 0; i < this.equipment.length; ++i) {
                this.equipment[i] = ItemStack.createStack((NBTTagCompound) nbttaglist.get(i));
            }
        }

        if (nbttagcompound.hasKey("ActiveEffects")) {
            nbttaglist = nbttagcompound.getList("ActiveEffects");

            for (i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                MobEffect mobeffect = MobEffect.b(nbttagcompound1);

                this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
            }
        }

        if (nbttagcompound.hasKey("DropChances")) {
            nbttaglist = nbttagcompound.getList("DropChances");

            for (i = 0; i < nbttaglist.size(); ++i) {
                this.dropChances[i] = ((NBTTagFloat) nbttaglist.get(i)).data;
            }
        }
    }

    public boolean isAlive() {
        return !this.dead && this.health > 0;
    }

    public boolean bf() {
        return false;
    }

    public void f(float f) {
        this.bE = f;
    }

    public void f(boolean flag) {
        this.bG = flag;
    }

    public void c() {
        if (this.bX > 0) {
            --this.bX;
        }

        if (this.bu > 0) {
            double d0 = this.locX + (this.bv - this.locX) / (double) this.bu;
            double d1 = this.locY + (this.bw - this.locY) / (double) this.bu;
            double d2 = this.locZ + (this.bx - this.locZ) / (double) this.bu;
            double d3 = MathHelper.g(this.by - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.bu);
            this.pitch = (float) ((double) this.pitch + (this.bz - (double) this.pitch) / (double) this.bu);
            --this.bu;
            this.setPosition(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        } else if (!this.bi()) {
            this.motX *= 0.98D;
            this.motY *= 0.98D;
            this.motZ *= 0.98D;
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

        this.world.methodProfiler.a("ai");
        if (this.bj()) {
            this.bG = false;
            this.bD = 0.0F;
            this.bE = 0.0F;
            this.bF = 0.0F;
        } else if (this.bi()) {
            if (this.bh()) {
                this.world.methodProfiler.a("newAi");
                this.bo();
                this.world.methodProfiler.b();
            } else {
                this.world.methodProfiler.a("oldAi");
                this.bq();
                this.world.methodProfiler.b();
                this.aA = this.yaw;
            }
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("jump");
        if (this.bG) {
            if (!this.G() && !this.I()) {
                if (this.onGround && this.bX == 0) {
                    this.bl();
                    this.bX = 10;
                }
            } else {
                this.motY += 0.03999999910593033D;
            }
        } else {
            this.bX = 0;
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("travel");
        this.bD *= 0.98F;
        this.bE *= 0.98F;
        this.bF *= 0.9F;
        float f = this.aO;

        this.aO *= this.bE();
        this.e(this.bD, this.bE);
        this.aO = f;
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("push");
        if (!this.world.isStatic) {
            this.bg();
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("looting");
        // CraftBukkit - Don't run mob pickup code on players
        if (!this.world.isStatic && !(this instanceof EntityPlayer) && this.bS() && !this.bd && this.world.getGameRules().getBoolean("mobGriefing")) {
            List list = this.world.a(EntityItem.class, this.boundingBox.grow(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (!entityitem.dead && entityitem.getItemStack() != null) {
                    ItemStack itemstack = entityitem.getItemStack();
                    int i = b(itemstack);

                    if (i > -1) {
                        boolean flag = true;
                        ItemStack itemstack1 = this.getEquipment(i);

                        if (itemstack1 != null) {
                            if (i == 0) {
                                if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
                                    flag = true;
                                } else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
                                    ItemSword itemsword = (ItemSword) itemstack.getItem();
                                    ItemSword itemsword1 = (ItemSword) itemstack1.getItem();

                                    if (itemsword.g() == itemsword1.g()) {
                                        flag = itemstack.getData() > itemstack1.getData() || itemstack.hasTag() && !itemstack1.hasTag();
                                    } else {
                                        flag = itemsword.g() > itemsword1.g();
                                    }
                                } else {
                                    flag = false;
                                }
                            } else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
                                flag = true;
                            } else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor) {
                                ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
                                ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();

                                if (itemarmor.c == itemarmor1.c) {
                                    flag = itemstack.getData() > itemstack1.getData() || itemstack.hasTag() && !itemstack1.hasTag();
                                } else {
                                    flag = itemarmor.c > itemarmor1.c;
                                }
                            } else {
                                flag = false;
                            }
                        }

                        if (flag) {
                            if (itemstack1 != null && this.random.nextFloat() - 0.1F < this.dropChances[i]) {
                                this.a(itemstack1, 0.0F);
                            }

                            this.setEquipment(i, itemstack);
                            this.dropChances[i] = 2.0F;
                            this.persistent = true;
                            this.receive(entityitem, 1);
                            entityitem.die();
                        }
                    }
                }
            }
        }

        this.world.methodProfiler.b();
    }

    protected void bg() {
        List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.L()) {
                    this.o(entity);
                }
            }
        }
    }

    protected void o(Entity entity) {
        entity.collide(this);
    }

    protected boolean bh() {
        return false;
    }

    protected boolean bi() {
        return !this.world.isStatic;
    }

    protected boolean bj() {
        return this.health <= 0;
    }

    public boolean bk() {
        return false;
    }

    protected void bl() {
        this.motY = 0.41999998688697815D;
        if (this.hasEffect(MobEffectList.JUMP)) {
            this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.an = true;
    }

    protected boolean isTypeNotPersistent() {
        return true;
    }

    protected void bn() {
        if (!this.persistent) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

            if (entityhuman != null) {
                double d0 = entityhuman.locX - this.locX;
                double d1 = entityhuman.locY - this.locY;
                double d2 = entityhuman.locZ - this.locZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 16384.0D) { // CraftBukkit - remove isTypeNotPersistent() check
                    this.die();
                }

                if (this.bC > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D) { // CraftBukkit - remove isTypeNotPersistent() check
                    this.die();
                } else if (d3 < 1024.0D) {
                    this.bC = 0;
                }
            }
        // CraftBukkit start
        } else {
            this.bC = 0;
        }
        // CraftBukkit end
    }

    protected void bo() {
        ++this.bC;
        this.world.methodProfiler.a("checkDespawn");
        this.bn();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("sensing");
        this.bP.a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("targetSelector");
        this.targetSelector.a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("goalSelector");
        this.goalSelector.a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("navigation");
        this.navigation.e();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("mob tick");
        this.bp();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("controls");
        this.world.methodProfiler.a("move");
        this.moveController.c();
        this.world.methodProfiler.c("look");
        this.lookController.a();
        this.world.methodProfiler.c("jump");
        this.jumpController.b();
        this.world.methodProfiler.b();
        this.world.methodProfiler.b();
    }

    protected void bp() {}

    protected void bq() {
        ++this.bC;
        this.bn();
        this.bD = 0.0F;
        this.bE = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, (double) f);

            if (entityhuman != null) {
                this.bY = entityhuman;
                this.bJ = 10 + this.random.nextInt(20);
            } else {
                this.bF = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.bY != null) {
            this.a(this.bY, 10.0F, (float) this.bs());
            if (this.bJ-- <= 0 || this.bY.dead || this.bY.e((Entity) this) > (double) (f * f)) {
                this.bY = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.bF = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.bF;
            this.pitch = this.bH;
        }

        boolean flag = this.G();
        boolean flag1 = this.I();

        if (flag || flag1) {
            this.bG = this.random.nextFloat() < 0.8F;
        }
    }

    protected void br() {
        int i = this.h();

        if (this.br) {
            ++this.bs;
            if (this.bs >= i) {
                this.bs = 0;
                this.br = false;
            }
        } else {
            this.bs = 0;
        }

        this.aR = (float) this.bs / (float) i;
    }

    public int bs() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX - this.locX;
        double d1 = entity.locZ - this.locZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = entityliving.locY + (double) entityliving.getHeadHeight() - (this.locY + (double) this.getHeadHeight());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.locY + (double) this.getHeadHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.pitch = this.b(this.pitch, f3, f1);
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

    protected void B() {
        this.damageEntity(DamageSource.OUT_OF_WORLD, 4);
    }

    public Vec3D Y() {
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
            return this.world.getVec3DPool().create((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.yaw - this.lastYaw) * f;
            f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);

            return this.world.getVec3DPool().create((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int by() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void bA() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            try {
                if (!mobeffect.tick(this)) {
                    if (!this.world.isStatic) {
                        iterator.remove();
                        this.c(mobeffect);
                    }
                } else if (mobeffect.getDuration() % 600 == 0) {
                    this.b(mobeffect);
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Ticking mob effect instance");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Mob effect being ticked");

                crashreportsystemdetails.a("Effect Name", (Callable) (new CrashReportEffectName(this, mobeffect)));
                crashreportsystemdetails.a("Effect ID", (Callable) (new CrashReportEffectID(this, mobeffect)));
                crashreportsystemdetails.a("Effect Duration", (Callable) (new CrashReportEffectDuration(this, mobeffect)));
                crashreportsystemdetails.a("Effect Amplifier", (Callable) (new CrashReportEffectAmplifier(this, mobeffect)));
                crashreportsystemdetails.a("Effect is Splash", (Callable) (new CrashReportEffectSplash(this, mobeffect)));
                crashreportsystemdetails.a("Effect is Ambient", (Callable) (new CrashReportEffectAmbient(this, mobeffect)));
                throw new ReportedException(crashreport);
            }
        }

        int i;

        if (this.updateEffects) {
            if (!this.world.isStatic) {
                if (this.effects.isEmpty()) {
                    this.datawatcher.watch(9, Byte.valueOf((byte) 0));
                    this.datawatcher.watch(8, Integer.valueOf(0));
                    this.setInvisible(false);
                } else {
                    i = PotionBrewer.a(this.effects.values());
                    this.datawatcher.watch(9, Byte.valueOf((byte) (PotionBrewer.b(this.effects.values()) ? 1 : 0)));
                    this.datawatcher.watch(8, Integer.valueOf(i));
                    this.setInvisible(this.hasEffect(MobEffectList.INVISIBILITY.id));
                }
            }

            this.updateEffects = false;
        }

        i = this.datawatcher.getInt(8);
        boolean flag = this.datawatcher.getByte(9) > 0;

        if (i > 0) {
            boolean flag1 = false;

            if (!this.isInvisible()) {
                flag1 = this.random.nextBoolean();
            } else {
                flag1 = this.random.nextInt(15) == 0;
            }

            if (flag) {
                flag1 &= this.random.nextInt(5) == 0;
            }

            if (flag1 && i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.world.addParticle(flag ? "mobSpellAmbient" : "mobSpell", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.locY + this.random.nextDouble() * (double) this.length - (double) this.height, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2);
            }
        }
    }

    public void bB() {
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

    public boolean hasEffect(int i) {
        return this.effects.containsKey(Integer.valueOf(i));
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

    public boolean bD() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    public void o(int i) {
        MobEffect mobeffect = (MobEffect) this.effects.remove(Integer.valueOf(i));

        if (mobeffect != null) {
            this.c(mobeffect);
        }
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

    public float bE() {
        float f = 1.0F;

        if (this.hasEffect(MobEffectList.FASTER_MOVEMENT)) {
            f *= 1.0F + 0.2F * (float) (this.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1);
        }

        if (this.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
            f *= 1.0F - 0.15F * (float) (this.getEffect(MobEffectList.SLOWER_MOVEMENT).getAmplifier() + 1);
        }

        if (f < 0.0F) {
            f = 0.0F;
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
        this.makeSound("random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3D vec3d = this.world.getVec3DPool().create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d.b(-this.yaw * 3.1415927F / 180.0F);
            Vec3D vec3d1 = this.world.getVec3DPool().create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

            vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
            vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
            vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ);
            this.world.addParticle("iconcrack_" + itemstack.getItem().id, vec3d1.c, vec3d1.d, vec3d1.e, vec3d.c, vec3d.d + 0.05D, vec3d.e);
        }
    }

    public int ar() {
        if (this.getGoalTarget() == null) {
            return 3;
        } else {
            int i = (int) ((float) this.health - (float) this.maxHealth * 0.33F); // CraftBukkit - this.getMaxHealth() -> this.maxHealth

            i -= (3 - this.world.difficulty) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    public ItemStack bG() {
        return this.equipment[0];
    }

    public ItemStack getEquipment(int i) {
        return this.equipment[i];
    }

    public ItemStack q(int i) {
        return this.equipment[i + 1];
    }

    public void setEquipment(int i, ItemStack itemstack) {
        this.equipment[i] = itemstack;
    }

    public ItemStack[] getEquipment() {
        return this.equipment;
    }

    protected void dropEquipment(boolean flag, int i) {
        for (int j = 0; j < this.getEquipment().length; ++j) {
            ItemStack itemstack = this.getEquipment(j);
            boolean flag1 = this.dropChances[j] > 1.0F;

            if (itemstack != null && (flag || flag1) && this.random.nextFloat() - (float) i * 0.01F < this.dropChances[j]) {
                if (!flag1 && itemstack.g()) {
                    int k = Math.max(itemstack.l() - 25, 1);
                    int l = itemstack.l() - this.random.nextInt(this.random.nextInt(k) + 1);

                    if (l > k) {
                        l = k;
                    }

                    if (l < 1) {
                        l = 1;
                    }

                    itemstack.setData(l);
                }

                this.a(itemstack, 0.0F);
            }
        }
    }

    protected void bH() {
        if (this.random.nextFloat() < d[this.world.difficulty]) {
            int i = this.random.nextInt(2);
            float f = this.world.difficulty == 3 ? 0.1F : 0.25F;

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            for (int j = 3; j >= 0; --j) {
                ItemStack itemstack = this.q(j);

                if (j < 3 && this.random.nextFloat() < f) {
                    break;
                }

                if (itemstack == null) {
                    Item item = a(j + 1, i);

                    if (item != null) {
                        this.setEquipment(j + 1, new ItemStack(item));
                    }
                }
            }
        }
    }

    public void receive(Entity entity, int i) {
        if (!entity.dead && !this.world.isStatic) {
            EntityTracker entitytracker = ((WorldServer) this.world).getTracker();

            if (entity instanceof EntityItem) {
                entitytracker.a(entity, (Packet) (new Packet22Collect(entity.id, this.id)));
            }

            if (entity instanceof EntityArrow) {
                entitytracker.a(entity, (Packet) (new Packet22Collect(entity.id, this.id)));
            }

            if (entity instanceof EntityExperienceOrb) {
                entitytracker.a(entity, (Packet) (new Packet22Collect(entity.id, this.id)));
            }
        }
    }

    public static int b(ItemStack itemstack) {
        if (itemstack.id != Block.PUMPKIN.id && itemstack.id != Item.SKULL.id) {
            if (itemstack.getItem() instanceof ItemArmor) {
                switch (((ItemArmor) itemstack.getItem()).b) {
                case 0:
                    return 4;

                case 1:
                    return 3;

                case 2:
                    return 2;

                case 3:
                    return 1;
                }
            }

            return 0;
        } else {
            return 4;
        }
    }

    public static Item a(int i, int j) {
        switch (i) {
        case 4:
            if (j == 0) {
                return Item.LEATHER_HELMET;
            } else if (j == 1) {
                return Item.GOLD_HELMET;
            } else if (j == 2) {
                return Item.CHAINMAIL_HELMET;
            } else if (j == 3) {
                return Item.IRON_HELMET;
            } else if (j == 4) {
                return Item.DIAMOND_HELMET;
            }

        case 3:
            if (j == 0) {
                return Item.LEATHER_CHESTPLATE;
            } else if (j == 1) {
                return Item.GOLD_CHESTPLATE;
            } else if (j == 2) {
                return Item.CHAINMAIL_CHESTPLATE;
            } else if (j == 3) {
                return Item.IRON_CHESTPLATE;
            } else if (j == 4) {
                return Item.DIAMOND_CHESTPLATE;
            }

        case 2:
            if (j == 0) {
                return Item.LEATHER_LEGGINGS;
            } else if (j == 1) {
                return Item.GOLD_LEGGINGS;
            } else if (j == 2) {
                return Item.CHAINMAIL_LEGGINGS;
            } else if (j == 3) {
                return Item.IRON_LEGGINGS;
            } else if (j == 4) {
                return Item.DIAMOND_LEGGINGS;
            }

        case 1:
            if (j == 0) {
                return Item.LEATHER_BOOTS;
            } else if (j == 1) {
                return Item.GOLD_BOOTS;
            } else if (j == 2) {
                return Item.CHAINMAIL_BOOTS;
            } else if (j == 3) {
                return Item.IRON_BOOTS;
            } else if (j == 4) {
                return Item.DIAMOND_BOOTS;
            }

        default:
            return null;
        }
    }

    protected void bI() {
        if (this.bG() != null && this.random.nextFloat() < b[this.world.difficulty]) {
            EnchantmentManager.a(this.random, this.bG(), 5 + this.world.difficulty * this.random.nextInt(6));
        }

        for (int i = 0; i < 4; ++i) {
            ItemStack itemstack = this.q(i);

            if (itemstack != null && this.random.nextFloat() < c[this.world.difficulty]) {
                EnchantmentManager.a(this.random, itemstack, 5 + this.world.difficulty * this.random.nextInt(6));
            }
        }
    }

    public void bJ() {}

    private int h() {
        return this.hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    public void bK() {
        if (!this.br || this.bs >= this.h() / 2 || this.bs < 0) {
            this.bs = -1;
            this.br = true;
            if (this.world instanceof WorldServer) {
                ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new Packet18ArmAnimation(this, 1)));
            }
        }
    }

    public boolean bL() {
        return false;
    }

    public final int bM() {
        return this.datawatcher.getByte(10);
    }

    public final void r(int i) {
        this.datawatcher.watch(10, Byte.valueOf((byte) i));
    }

    public EntityLiving bN() {
        return (EntityLiving) (this.bt.c() != null ? this.bt.c() : (this.killer != null ? this.killer : (this.lastDamager != null ? this.lastDamager : null)));
    }

    public String getLocalizedName() {
        return this.hasCustomName() ? this.getCustomName() : super.getLocalizedName();
    }

    public void setCustomName(String s) {
        this.datawatcher.watch(5, s);
    }

    public String getCustomName() {
        return this.datawatcher.getString(5);
    }

    public boolean hasCustomName() {
        return this.datawatcher.getString(5).length() > 0;
    }

    public void setCustomNameVisible(boolean flag) {
        this.datawatcher.watch(6, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public boolean getCustomNameVisible() {
        return this.datawatcher.getByte(6) == 1;
    }

    public void a(int i, float f) {
        this.dropChances[i] = f;
    }

    public boolean bS() {
        return this.canPickUpLoot;
    }

    public void h(boolean flag) {
        this.canPickUpLoot = flag;
    }
}
