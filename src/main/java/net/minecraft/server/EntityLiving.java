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

    private static final float[] b = new float[] { 0.0F, 0.0F, 0.005F, 0.01F};
    private static final float[] c = new float[] { 0.0F, 0.0F, 0.05F, 0.1F};
    private static final float[] d = new float[] { 0.0F, 0.0F, 0.005F, 0.02F};
    public static final float[] as = new float[] { 0.0F, 0.01F, 0.07F, 0.2F};
    public int maxNoDamageTicks = 20;
    public float au;
    public float av;
    public float aw = 0.0F;
    public float ax = 0.0F;
    public float ay = 0.0F;
    public float az = 0.0F;
    protected float aA;
    protected float aB;
    protected float aC;
    protected float aD;
    protected boolean aE = true;
    protected String texture = "/mob/char.png";
    protected boolean aG = true;
    protected float aH = 0.0F;
    protected String aI = null;
    protected float aJ = 1.0F;
    protected int aK = 0;
    protected float aL = 0.0F;
    public float aM = 0.1F;
    public float aN = 0.02F;
    public float aO;
    public float aP;
    protected int health = this.getMaxHealth();
    public int aR;
    protected int aS;
    public int aT;
    public int hurtTicks;
    public int aV;
    public float aW = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float aZ;
    public float ba;
    protected boolean bb = false;
    protected int bc;
    public int bd = -1;
    public float be = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float bf;
    public float bg;
    public float bh;
    public EntityHuman killer = null; // CraftBukkit - protected -> public
    protected int lastDamageByPlayerTime = 0;
    public EntityLiving lastDamager = null; // CraftBukkit - private -> public
    private int f = 0;
    private EntityLiving g = null;
    public int bk = 0;
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
    private EntityLiving bM;
    private EntitySenses bN;
    private float bO;
    private ChunkCoordinates bP = new ChunkCoordinates(0, 0, 0);
    private float bQ = -1.0F;
    private ItemStack[] equipment = new ItemStack[5];
    public float[] dropChances = new float[5]; // CraftBukkit - protected -> public
    private ItemStack[] bS = new ItemStack[5];
    public boolean bp = false;
    public int bq = 0;
    public boolean canPickUpLoot = false; // CraftBukkit - protected -> public
    public boolean persistent = !this.bj(); // CraftBukkit - private -> public, change value
    protected int bs;
    protected double bt;
    protected double bu;
    protected double bv;
    protected double bw;
    protected double bx;
    float by = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int bA = 0;
    protected float bB;
    protected float bC;
    protected float bD;
    protected boolean bE = false;
    protected float bF = 0.0F;
    protected float bG = 0.7F;
    private int bU = 0;
    private Entity bV;
    protected int bH = 0;
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
        this.bN = new EntitySenses(this);
        this.av = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.au = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.ay = this.yaw;

        for (int i = 0; i < this.dropChances.length; ++i) {
            this.dropChances[i] = 0.05F;
        }

        this.X = 0.5F;
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

    public EntitySenses aA() {
        return this.bN;
    }

    public Random aB() {
        return this.random;
    }

    public EntityLiving aC() {
        return this.lastDamager;
    }

    public EntityLiving aD() {
        return this.g;
    }

    public void l(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.g = (EntityLiving) entity;
        }
    }

    public int aE() {
        return this.bA;
    }

    public float ap() {
        return this.ay;
    }

    public float aF() {
        return this.bO;
    }

    public void e(float f) {
        this.bO = f;
        this.f(f);
    }

    public boolean m(Entity entity) {
        this.l(entity);
        return false;
    }

    public EntityLiving aG() {
        return this.bM;
    }

    public void b(EntityLiving entityliving) {
        this.bM = entityliving;
    }

    public boolean a(Class oclass) {
        return EntityCreeper.class != oclass && EntityGhast.class != oclass;
    }

    public void aH() {}

    protected void a(double d0, boolean flag) {
        if (!this.H()) {
            this.I();
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

    public boolean aI() {
        return this.e(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean e(int i, int j, int k) {
        return this.bQ == -1.0F ? true : this.bP.e(i, j, k) < this.bQ * this.bQ;
    }

    public void b(int i, int j, int k, int l) {
        this.bP.b(i, j, k);
        this.bQ = (float) l;
    }

    public ChunkCoordinates aJ() {
        return this.bP;
    }

    public float aK() {
        return this.bQ;
    }

    public void aL() {
        this.bQ = -1.0F;
    }

    public boolean aM() {
        return this.bQ != -1.0F;
    }

    public void c(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.f = this.lastDamager != null ? 60 : 0;
    }

    protected void a() {
        this.datawatcher.a(8, Integer.valueOf(this.i));
        this.datawatcher.a(9, Byte.valueOf((byte) 0));
        this.datawatcher.a(10, Byte.valueOf((byte) 0));
    }

    public boolean n(Entity entity) {
        return this.world.a(this.world.getVec3DPool().create(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ), this.world.getVec3DPool().create(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ)) == null;
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

    public int aN() {
        return 80;
    }

    public void aO() {
        String s = this.aY();

        if (s != null) {
            this.makeSound(s, this.aX(), this.aV());
        }
    }

    public void y() {
        this.aO = this.aP;
        super.y();
        this.world.methodProfiler.a("mobBaseTick");
        if (this.isAlive() && this.random.nextInt(1000) < this.aT++) {
            this.aT = -this.aN();
            this.aO();
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

        if (this.isAlive() && this.a(Material.WATER) && !this.bc() && !this.effects.containsKey(Integer.valueOf(MobEffectList.WATER_BREATHING.id))) {
            this.setAirTicks(this.g(this.getAirTicks()));
            if (this.getAirTicks() == -20) {
                this.setAirTicks(0);

                for (int i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.world.addParticle("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
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

        this.aZ = this.ba;
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
            this.aP();
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

        this.bx();
        this.aD = this.aC;
        this.ax = this.aw;
        this.az = this.ay;
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
    // CraftBukkit end

    protected void aP() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.dead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - update getExpReward() above if the removed if() changes!
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

    protected int g(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return this.bc;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void aR() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.world.addParticle("explode", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.locY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void U() {
        super.U();
        this.aA = this.aB;
        this.aB = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void j_() {
        super.j_();
        if (!this.world.isStatic) {
            int i;

            for (i = 0; i < 5; ++i) {
                ItemStack itemstack = this.getEquipment(i);

                if (!ItemStack.matches(itemstack, this.bS[i])) {
                    ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new Packet5EntityEquipment(this.id, i, itemstack)));
                    this.bS[i] = itemstack == null ? null : itemstack.cloneItemStack();
                }
            }

            i = this.bJ();
            if (i > 0) {
                if (this.bk <= 0) {
                    this.bk = 20 * (30 - i);
                }

                --this.bk;
                if (this.bk <= 0) {
                    this.r(i - 1);
                }
            }
        }

        this.c();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.aw;
        float f2 = 0.0F;

        this.aA = this.aB;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aP > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aB += (f3 - this.aB) * 0.3F;
        this.world.methodProfiler.a("headTurn");
        if (this.be()) {
            this.senses.a();
        } else {
            float f4 = MathHelper.g(f1 - this.aw);

            this.aw += f4 * 0.3F;
            float f5 = MathHelper.g(this.yaw - this.aw);
            boolean flag = f5 < -90.0F || f5 >= 90.0F;

            if (f5 < -75.0F) {
                f5 = -75.0F;
            }

            if (f5 >= 75.0F) {
                f5 = 75.0F;
            }

            this.aw = this.yaw - f5;
            if (f5 * f5 > 2500.0F) {
                this.aw += f5 * 0.2F;
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

        while (this.aw - this.ax < -180.0F) {
            this.ax -= 360.0F;
        }

        while (this.aw - this.ax >= 180.0F) {
            this.ax += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.ay - this.az < -180.0F) {
            this.az -= 360.0F;
        }

        while (this.ay - this.az >= 180.0F) {
            this.az += 360.0F;
        }

        this.world.methodProfiler.b();
        this.aC += f2;
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
        if (this.isInvulnerable()) {
            return false;
        } else if (this.world.isStatic) {
            return false;
        } else {
            this.bA = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
                    i = (int) ((float) i * 0.55F);
                }

                this.bg = 1.5F;
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
                    this.aR = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.d(damagesource, i);
                    this.hurtTicks = this.aV = 10;
                }

                this.aW = 0.0F;
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

                        this.aW = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.aW = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.makeSound(this.ba(), this.aX(), this.aV());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.makeSound(this.aZ(), this.aX(), this.aV());
                }

                return true;
            }
        }
    }

    protected float aV() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int aW() {
        int i = 0;
        ItemStack[] aitemstack = this.getEquipment();
        int j = aitemstack.length;

        for (int k = 0; k < j; ++k) {
            ItemStack itemstack = aitemstack[k];

            if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                int l = ((ItemArmor) itemstack.getItem()).b;

                i += l;
            }
        }

        return i;
    }

    protected void k(int i) {}

    protected int b(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.aW();
            int k = i * j + this.aS;

            this.k(i);
            i = k / 25;
            this.aS = k % 25;
        }

        return i;
    }

    protected int c(DamageSource damagesource, int i) {
        if (this.hasEffect(MobEffectList.RESISTANCE)) {
            int j = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + this.aS;

            i = l / 25;
            this.aS = l % 25;
        }

        return i;
    }

    protected void d(DamageSource damagesource, int i) {
        if (!this.isInvulnerable()) {
            i = this.b(damagesource, i);
            i = this.c(damagesource, i);
            this.health -= i;
        }
    }

    protected float aX() {
        return 1.0F;
    }

    protected String aY() {
        return null;
    }

    protected String aZ() {
        return "damage.hit";
    }

    protected String ba() {
        return "damage.hit";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.am = true;
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

        if (this.aK >= 0 && entity != null) {
            entity.c(this, this.aK);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.bb = true;
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
                    this.makeSound("damage.fallbig", 1.0F, 1.0F);
                } else {
                    this.makeSound("damage.fallsmall", 1.0F, 1.0F);
                }

                this.getBukkitEntity().setLastDamageCause(event);
                this.damageEntity(DamageSource.FALL, i);
            }
            // CraftBukkit end

            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.makeSound(stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    public void e(float f, float f1) {
        double d0;

        if (this.H() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
            d0 = this.locY;
            this.a(f, f1, this.be() ? 0.04F : 0.02F);
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
                if (this.be()) {
                    f4 = this.aF();
                } else {
                    f4 = this.aM;
                }

                f4 *= f3;
            } else {
                f4 = this.aN;
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

        this.bf = this.bg;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.bg += (f6 - this.bg) * 0.4F;
        this.bh += this.bg;
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
        nbttagcompound.setBoolean("CanPickUpLoot", this.canPickUpLoot);
        nbttagcompound.setBoolean("PersistenceRequired", this.persistent);
        nbttagcompound.setBoolean("Bukkit.PersistenceUpdated", true); // CraftBukkit
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
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.getShort("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = this.getMaxHealth();
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");
        this.canPickUpLoot = nbttagcompound.getBoolean("CanPickUpLoot");
        // CraftBukkit start - if persistence is false only use it if it was set after we started using it
        boolean data = nbttagcompound.getBoolean("PersistenceRequired");
        if (nbttagcompound.hasKey("Bukkit.PersistenceUpdated") || data) {
            this.persistent = data;
        } else {
            this.persistent = !this.bj();
        }
        // CraftBukkit end

        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKey("Equipment")) {
            nbttaglist = nbttagcompound.getList("Equipment");

            for (i = 0; i < this.equipment.length; ++i) {
                this.equipment[i] = ItemStack.a((NBTTagCompound) nbttaglist.get(i));
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

    public boolean bc() {
        return false;
    }

    public void f(float f) {
        this.bC = f;
    }

    public void e(boolean flag) {
        this.bE = flag;
    }

    public void c() {
        if (this.bU > 0) {
            --this.bU;
        }

        if (this.bs > 0) {
            double d0 = this.locX + (this.bt - this.locX) / (double) this.bs;
            double d1 = this.locY + (this.bu - this.locY) / (double) this.bs;
            double d2 = this.locZ + (this.bv - this.locZ) / (double) this.bs;
            double d3 = MathHelper.g(this.bw - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.bs);
            this.pitch = (float) ((double) this.pitch + (this.bx - (double) this.pitch) / (double) this.bs);
            --this.bs;
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

        this.world.methodProfiler.a("ai");
        if (this.bg()) {
            this.bE = false;
            this.bB = 0.0F;
            this.bC = 0.0F;
            this.bD = 0.0F;
        } else if (this.bf()) {
            if (this.be()) {
                this.world.methodProfiler.a("newAi");
                this.bl();
                this.world.methodProfiler.b();
            } else {
                this.world.methodProfiler.a("oldAi");
                this.bn();
                this.world.methodProfiler.b();
                this.ay = this.yaw;
            }
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("jump");
        if (this.bE) {
            if (!this.H() && !this.J()) {
                if (this.onGround && this.bU == 0) {
                    this.bi();
                    this.bU = 10;
                }
            } else {
                this.motY += 0.03999999910593033D;
            }
        } else {
            this.bU = 0;
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("travel");
        this.bB *= 0.98F;
        this.bC *= 0.98F;
        this.bD *= 0.9F;
        float f = this.aM;

        this.aM *= this.bB();
        this.e(this.bB, this.bC);
        this.aM = f;
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("push");
        if (!this.world.isStatic) {
            this.bd();
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("looting");
        if (!this.world.isStatic && this.canPickUpLoot && !this.bb && this.world.getGameRules().getBoolean("mobGriefing")) {
            List list = this.world.a(EntityItem.class, this.boundingBox.grow(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (!entityitem.dead && entityitem.itemStack != null) {
                    ItemStack itemstack = entityitem.itemStack;
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

                                if (itemarmor.b == itemarmor1.b) {
                                    flag = itemstack.getData() > itemstack1.getData() || itemstack.hasTag() && !itemstack1.hasTag();
                                } else {
                                    flag = itemarmor.b > itemarmor1.b;
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

    protected void bd() {
        List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.M()) {
                    this.o(entity);
                }
            }
        }
    }

    protected void o(Entity entity) {
        entity.collide(this);
    }

    protected boolean be() {
        return false;
    }

    protected boolean bf() {
        return !this.world.isStatic;
    }

    protected boolean bg() {
        return this.health <= 0;
    }

    public boolean bh() {
        return false;
    }

    protected void bi() {
        this.motY = 0.41999998688697815D;
        if (this.hasEffect(MobEffectList.JUMP)) {
            this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.yaw * 0.017453292F;

            this.motX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.am = true;
    }

    protected boolean bj() {
        return true;
    }

    protected void bk() {
        if (!this.persistent) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

            if (entityhuman != null) {
                double d0 = entityhuman.locX - this.locX;
                double d1 = entityhuman.locY - this.locY;
                double d2 = entityhuman.locZ - this.locZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 16384.0D) { // CraftBukkit - remove this.bj() check
                    this.die();
                }

                if (this.bA > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D) { // CraftBukkit - remove this.bj() check
                    this.die();
                } else if (d3 < 1024.0D) {
                    this.bA = 0;
                }
            }
        // CraftBukkit start
        } else {
            this.bA = 0;
        }
        // CraftBukkit end
    }

    protected void bl() {
        ++this.bA;
        this.world.methodProfiler.a("checkDespawn");
        this.bk();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("sensing");
        this.bN.a();
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
        this.bm();
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

    protected void bm() {}

    protected void bn() {
        ++this.bA;
        this.bk();
        this.bB = 0.0F;
        this.bC = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, (double) f);

            if (entityhuman != null) {
                this.bV = entityhuman;
                this.bH = 10 + this.random.nextInt(20);
            } else {
                this.bD = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.bV != null) {
            this.a(this.bV, 10.0F, (float) this.bp());
            if (this.bH-- <= 0 || this.bV.dead || this.bV.e((Entity) this) > (double) (f * f)) {
                this.bV = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.bD = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.yaw += this.bD;
            this.pitch = this.bF;
        }

        boolean flag = this.H();
        boolean flag1 = this.J();

        if (flag || flag1) {
            this.bE = this.random.nextFloat() < 0.8F;
        }
    }

    protected void bo() {
        int i = this.h();

        if (this.bp) {
            ++this.bq;
            if (this.bq >= i) {
                this.bq = 0;
                this.bp = false;
            }
        } else {
            this.bq = 0;
        }

        this.aP = (float) this.bq / (float) i;
    }

    public int bp() {
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

    public int bv() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void bx() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!mobeffect.tick(this)) {
                if (!this.world.isStatic) {
                    iterator.remove();
                    this.c(mobeffect);
                }
            } else if (mobeffect.getDuration() % 600 == 0) {
                this.b(mobeffect);
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

    public void by() {
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

    public boolean bA() {
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

    public float bB() {
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

    public int as() {
        if (this.aG() == null) {
            return 3;
        } else {
            int i = (int) ((float) this.health - (float) this.getMaxHealth() * 0.33F);

            i -= (3 - this.world.difficulty) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    public ItemStack bD() {
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
                if (!flag1 && itemstack.f()) {
                    int k = Math.max(itemstack.k() - 25, 1);
                    int l = itemstack.k() - this.random.nextInt(this.random.nextInt(k) + 1);

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

    protected void bE() {
        if (this.random.nextFloat() < d[this.world.difficulty]) {
            int i = this.random.nextInt(2);
            float f = this.world.difficulty == 3 ? 0.1F : 0.25F;

            if (this.random.nextFloat() < 0.07F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.07F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.07F) {
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
                switch (((ItemArmor) itemstack.getItem()).a) {
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

    protected void bF() {
        if (this.bD() != null && this.random.nextFloat() < b[this.world.difficulty]) {
            EnchantmentManager.a(this.random, this.bD(), 5);
        }

        for (int i = 0; i < 4; ++i) {
            ItemStack itemstack = this.q(i);

            if (itemstack != null && this.random.nextFloat() < c[this.world.difficulty]) {
                EnchantmentManager.a(this.random, itemstack, 5);
            }
        }
    }

    public void bG() {}

    private int h() {
        return this.hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    public void bH() {
        if (!this.bp || this.bq >= this.h() / 2 || this.bq < 0) {
            this.bq = -1;
            this.bp = true;
            if (this.world instanceof WorldServer) {
                ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new Packet18ArmAnimation(this, 1)));
            }
        }
    }

    public boolean bI() {
        return false;
    }

    public final int bJ() {
        return this.datawatcher.getByte(10);
    }

    public final void r(int i) {
        this.datawatcher.watch(10, Byte.valueOf((byte) i));
    }
}
