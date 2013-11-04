package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    private static final UUID b = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier c = (new AttributeModifier(b, "Sprinting speed boost", 0.30000001192092896D, 2)).a(false);
    private AttributeMapBase d;
    public CombatTracker combatTracker = new CombatTracker(this); // CraftBukkit - private -> public, remove final
    public final HashMap effects = new HashMap(); // CraftBukkit - protected -> public
    private final ItemStack[] g = new ItemStack[5];
    public boolean au;
    public int av;
    public int aw;
    public float ax;
    public int hurtTicks;
    public int az;
    public float aA;
    public int deathTicks;
    public int attackTicks;
    public float aD;
    public float aE;
    public float aF;
    public float aG;
    public float aH;
    public int maxNoDamageTicks = 20;
    public float aJ;
    public float aK;
    public float aL;
    public float aM;
    public float aN;
    public float aO;
    public float aP;
    public float aQ;
    public float aR = 0.02F;
    public EntityHuman killer; // CraftBukkit - protected -> public
    protected int lastDamageByPlayerTime;
    protected boolean aU;
    protected int aV;
    protected float aW;
    protected float aX;
    protected float aY;
    protected float aZ;
    protected float ba;
    protected int bb;
    public float lastDamage; // CraftBukkit - protected -> public
    protected boolean bd;
    public float be;
    public float bf;
    protected float bg;
    protected int bh;
    protected double bi;
    protected double bj;
    protected double bk;
    protected double bl;
    protected double bm;
    public boolean updateEffects = true; // CraftBukkit - private -> public
    public EntityLiving lastDamager; // CraftBukkit - private -> public
    private int j;
    private EntityLiving bn;
    private int bo;
    private float bp;
    private int bq;
    private float br;
    // CraftBukkit start
    public int expToDrop;
    public int maxAirTicks = 300;
    // CraftBukkit end

    public EntityLiving(World world) {
        super(world);
        this.aD();
        // CraftBukkit - setHealth(getMaxHealth()) inlined and simplified to skip the instanceof check for EntityPlayer, as getBukkitEntity() is not initialized in constructor
        this.datawatcher.watch(6, (float) this.getAttributeInstance(GenericAttributes.a).getValue());
        this.l = true;
        this.aM = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.aL = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.aP = this.yaw;
        this.X = 0.5F;
    }

    protected void c() {
        this.datawatcher.a(7, Integer.valueOf(0));
        this.datawatcher.a(8, Byte.valueOf((byte) 0));
        this.datawatcher.a(9, Byte.valueOf((byte) 0));
        this.datawatcher.a(6, Float.valueOf(1.0F));
    }

    protected void aD() {
        this.bc().b(GenericAttributes.a);
        this.bc().b(GenericAttributes.c);
        this.bc().b(GenericAttributes.d);
        if (!this.bk()) {
            this.getAttributeInstance(GenericAttributes.d).setValue(0.10000000149011612D);
        }
    }

    protected void a(double d0, boolean flag) {
        if (!this.M()) {
            this.N();
        }

        if (flag && this.fallDistance > 0.0F) {
            int i = MathHelper.floor(this.locX);
            int j = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
            int k = MathHelper.floor(this.locZ);
            Block block = this.world.getType(i, j, k);

            if (block.getMaterial() == Material.AIR) {
                int l = this.world.getType(i, j - 1, k).b();

                if (l == 11 || l == 32 || l == 21) {
                    block = this.world.getType(i, j - 1, k);
                }
            } else if (!this.world.isStatic && this.fallDistance > 3.0F) {
                this.world.triggerEffect(2006, i, j, k, MathHelper.f(this.fallDistance - 3.0F));
            }

            block.a(this.world, i, j, k, this, this.fallDistance);
        }

        super.a(d0, flag);
    }

    public boolean aE() {
        return false;
    }

    public void C() {
        this.aD = this.aE;
        super.C();
        this.world.methodProfiler.a("livingEntityBaseTick");
        if (this.isAlive() && this.inBlock()) {
            this.damageEntity(DamageSource.STUCK, 1.0F);
        }

        if (this.isFireproof() || this.world.isStatic) {
            this.extinguish();
        }

        boolean flag = this instanceof EntityHuman && ((EntityHuman) this).abilities.isInvulnerable;

        if (this.isAlive() && this.a(Material.WATER)) {
            if (!this.aE() && !this.hasEffect(MobEffectList.WATER_BREATHING.id) && !flag) {
                this.setAirTicks(this.j(this.getAirTicks()));
                if (this.getAirTicks() == -20) {
                    this.setAirTicks(0);

                    for (int i = 0; i < 8; ++i) {
                        float f = this.random.nextFloat() - this.random.nextFloat();
                        float f1 = this.random.nextFloat() - this.random.nextFloat();
                        float f2 = this.random.nextFloat() - this.random.nextFloat();

                        this.world.addParticle("bubble", this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                    }

                    this.damageEntity(DamageSource.DROWN, 2.0F);
                }
            }

            if (!this.world.isStatic && this.am() && this.vehicle instanceof EntityLiving) {
                this.mount((Entity) null);
            }
        } else {
            // CraftBukkit start - Only set if needed to work around a DataWatcher inefficiency
            if (this.getAirTicks() != 300) {
                this.setAirTicks(maxAirTicks);
            }
            // CraftBukkit end
        }

        if (this.isAlive() && this.L()) {
            this.extinguish();
        }

        this.aJ = this.aK;
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }

        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }

        if (this.noDamageTicks > 0 && !(this instanceof EntityPlayer)) {
            --this.noDamageTicks;
        }

        if (this.getHealth() <= 0.0F) {
            this.aF();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.killer = null;
        }

        if (this.bn != null && !this.bn.isAlive()) {
            this.bn = null;
        }

        if (this.lastDamager != null) {
            if (!this.lastDamager.isAlive()) {
                this.b((EntityLiving) null);
            } else if (this.ticksLived - this.j > 100) {
                this.b((EntityLiving) null);
            }
        }

        this.removeAllEffects();
        this.aZ = this.aY;
        this.aO = this.aN;
        this.aQ = this.aP;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.world.methodProfiler.b();
    }

    // CraftBukkit start
    public int getExpReward() {
        int exp = this.getExpValue(this.killer);

        if (!this.world.isStatic && (this.lastDamageByPlayerTime > 0 || this.alwaysGivesExp()) && this.aG()) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    public boolean isBaby() {
        return false;
    }

    protected void aF() {
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

    protected boolean aG() {
        return !this.isBaby();
    }

    protected int j(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return 0;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public Random aI() {
        return this.random;
    }

    public EntityLiving getLastDamager() {
        return this.lastDamager;
    }

    public int aK() {
        return this.j;
    }

    public void b(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.j = this.ticksLived;
    }

    public EntityLiving aL() {
        return this.bn;
    }

    public int aM() {
        return this.bo;
    }

    public void k(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.bn = (EntityLiving) entity;
        } else {
            this.bn = null;
        }

        this.bo = this.ticksLived;
    }

    public int aN() {
        return this.aV;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setFloat("HealF", this.getHealth());
        nbttagcompound.setShort("Health", (short) ((int) Math.ceil((double) this.getHealth())));
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setShort("AttackTime", (short) this.attackTicks);
        nbttagcompound.setFloat("AbsorptionAmount", this.bs());
        ItemStack[] aitemstack = this.getEquipment();
        int i = aitemstack.length;

        int j;
        ItemStack itemstack;

        for (j = 0; j < i; ++j) {
            itemstack = aitemstack[j];
            if (itemstack != null) {
                this.d.a(itemstack.D());
            }
        }

        nbttagcompound.set("Attributes", GenericAttributes.a(this.bc()));
        aitemstack = this.getEquipment();
        i = aitemstack.length;

        for (j = 0; j < i; ++j) {
            itemstack = aitemstack[j];
            if (itemstack != null) {
                this.d.b(itemstack.D());
            }
        }

        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                nbttaglist.add(mobeffect.a(new NBTTagCompound()));
            }

            nbttagcompound.set("ActiveEffects", nbttaglist);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.m(nbttagcompound.getFloat("AbsorptionAmount"));
        if (nbttagcompound.hasKeyOfType("Attributes", 9) && this.world != null && !this.world.isStatic) {
            GenericAttributes.a(this.bc(), nbttagcompound.getList("Attributes", 10));
        }

        if (nbttagcompound.hasKeyOfType("ActiveEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
                MobEffect mobeffect = MobEffect.b(nbttagcompound1);

                if (mobeffect != null) {
                    this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                }
            }
        }

        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxHealth")) {
            NBTBase nbtbase = nbttagcompound.get("Bukkit.MaxHealth");
            if (nbtbase.getTypeId() == 5) {
                this.getAttributeInstance(GenericAttributes.a).setValue((double) ((NBTTagFloat) nbtbase).c());
            } else if (nbtbase.getTypeId() == 3) {
                this.getAttributeInstance(GenericAttributes.a).setValue((double) ((NBTTagInt) nbtbase).d());
            }
        }
        // CraftBukkit end

        if (nbttagcompound.hasKeyOfType("HealF", 99)) {
            this.setHealth(nbttagcompound.getFloat("HealF"));
        } else {
            NBTBase nbtbase = nbttagcompound.get("Health");

            if (nbtbase == null) {
                this.setHealth(this.getMaxHealth());
            } else if (nbtbase.getTypeId() == 5) {
                this.setHealth(((NBTTagFloat) nbtbase).h());
            } else if (nbtbase.getTypeId() == 2) {
                this.setHealth((float) ((NBTTagShort) nbtbase).e());
            }
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");
    }

    protected void removeAllEffects() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!mobeffect.tick(this)) {
                if (!this.world.isStatic) {
                    iterator.remove();
                    this.b(mobeffect);
                }
            } else if (mobeffect.getDuration() % 600 == 0) {
                this.a(mobeffect, false);
            }
        }

        int i;

        if (this.updateEffects) {
            if (!this.world.isStatic) {
                if (this.effects.isEmpty()) {
                    this.datawatcher.watch(8, Byte.valueOf((byte) 0));
                    this.datawatcher.watch(7, Integer.valueOf(0));
                    this.setInvisible(false);
                } else {
                    i = PotionBrewer.a(this.effects.values());
                    this.datawatcher.watch(8, Byte.valueOf((byte) (PotionBrewer.b(this.effects.values()) ? 1 : 0)));
                    this.datawatcher.watch(7, Integer.valueOf(i));
                    this.setInvisible(this.hasEffect(MobEffectList.INVISIBILITY.id));
                }
            }

            this.updateEffects = false;
        }

        i = this.datawatcher.getInt(7);
        boolean flag = this.datawatcher.getByte(8) > 0;

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

    public void aP() {
        Iterator iterator = this.effects.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.effects.get(integer);

            if (!this.world.isStatic) {
                iterator.remove();
                this.b(mobeffect);
            }
        }
    }

    public Collection getEffects() {
        return this.effects.values();
    }

    public boolean hasEffect(int i) {
        // CraftBukkit - Add size check for efficiency
        return this.effects.size() != 0 && this.effects.containsKey(Integer.valueOf(i));
    }

    public boolean hasEffect(MobEffectList mobeffectlist) {
        // CraftBukkit - Add size check for efficiency
        return this.effects.size() != 0 && this.effects.containsKey(Integer.valueOf(mobeffectlist.id));
    }

    public MobEffect getEffect(MobEffectList mobeffectlist) {
        return (MobEffect) this.effects.get(Integer.valueOf(mobeffectlist.id));
    }

    public void addEffect(MobEffect mobeffect) {
        if (this.d(mobeffect)) {
            if (this.effects.containsKey(Integer.valueOf(mobeffect.getEffectId()))) {
                ((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
                this.a((MobEffect) this.effects.get(Integer.valueOf(mobeffect.getEffectId())), true);
            } else {
                this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                this.a(mobeffect);
            }
        }
    }

    public boolean d(MobEffect mobeffect) {
        if (this.getMonsterType() == EnumMonsterType.UNDEAD) {
            int i = mobeffect.getEffectId();

            if (i == MobEffectList.REGENERATION.id || i == MobEffectList.POISON.id) {
                return false;
            }
        }

        return true;
    }

    public boolean aR() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    public void m(int i) {
        MobEffect mobeffect = (MobEffect) this.effects.remove(Integer.valueOf(i));

        if (mobeffect != null) {
            this.b(mobeffect);
        }
    }

    protected void a(MobEffect mobeffect) {
        this.updateEffects = true;
        if (!this.world.isStatic) {
            MobEffectList.byId[mobeffect.getEffectId()].b(this, this.bc(), mobeffect.getAmplifier());
        }
    }

    protected void a(MobEffect mobeffect, boolean flag) {
        this.updateEffects = true;
        if (flag && !this.world.isStatic) {
            MobEffectList.byId[mobeffect.getEffectId()].a(this, this.bc(), mobeffect.getAmplifier());
            MobEffectList.byId[mobeffect.getEffectId()].b(this, this.bc(), mobeffect.getAmplifier());
        }
    }

    protected void b(MobEffect mobeffect) {
        this.updateEffects = true;
        if (!this.world.isStatic) {
            MobEffectList.byId[mobeffect.getEffectId()].a(this, this.bc(), mobeffect.getAmplifier());
        }
    }

    // CraftBukkit start - Delegate so we can handle providing a reason for health being regained
    public void heal(float f) {
        heal(f, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason) {
        float f1 = this.getHealth();

        if (f1 > 0.0F) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), f, regainReason);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.setHealth((float) (this.getHealth() + event.getAmount()));
            }
        }
    }

    public final float getHealth() {
        // CraftBukkit start - Use unscaled health
        if (this instanceof EntityPlayer) {
            return (float) ((EntityPlayer) this).getBukkitEntity().getHealth();
        }
        // CraftBukkit end
        return this.datawatcher.getFloat(6);
    }

    public void setHealth(float f) {
        // CraftBukkit start - Handle scaled health
        if (this instanceof EntityPlayer) {
            org.bukkit.craftbukkit.entity.CraftPlayer player = ((EntityPlayer) this).getBukkitEntity();
            // Squeeze
            if (f < 0.0F) {
                player.setRealHealth(0.0D);
            } else if (f > player.getMaxHealth()) {
                player.setRealHealth(player.getMaxHealth());
            } else {
                player.setRealHealth(f);
            }

            this.datawatcher.watch(6, Float.valueOf(player.getScaledHealth()));
            return;
        }
        // CraftBukkit end
        this.datawatcher.watch(6, Float.valueOf(MathHelper.a(f, 0.0F, this.getMaxHealth())));
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (this.world.isStatic) {
            return false;
        } else {
            this.aV = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else if (damagesource.o() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
                    this.getEquipment(4).damage((int) (f * 4.0F + this.random.nextFloat() * f * 2.0F), this);
                    f *= 0.75F;
                }

                this.aG = 1.5F;
                boolean flag = true;

                // CraftBukkit start
                EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(this, damagesource, f);
                if (event != null) {
                    if (event.isCancelled()) {
                        return false;
                    }
                    f = (float) event.getDamage();
                }
                // CraftBukkit end

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (f <= this.lastDamage) {
                        return false;
                    }

                    this.d(damagesource, f - this.lastDamage);
                    this.lastDamage = f;
                    flag = false;
                } else {
                    this.lastDamage = f;
                    this.ax = this.getHealth();
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.d(damagesource, f);
                    this.hurtTicks = this.az = 10;
                }

                this.aA = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.b((EntityLiving) entity);
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
                        this.Q();
                    }

                    if (entity != null) {
                        double d0 = entity.locX - this.locX;

                        double d1;

                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.aA = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
                        this.a(entity, f, d0, d1);
                    } else {
                        this.aA = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                String s;

                if (this.getHealth() <= 0.0F) {
                    s = this.aU();
                    if (flag && s != null) {
                        this.makeSound(s, this.bf(), this.bg());
                    }

                    this.die(damagesource);
                } else {
                    s = this.aT();
                    if (flag && s != null) {
                        this.makeSound(s, this.bf(), this.bg());
                    }
                }

                return true;
            }
        }
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
            this.world.addParticle("iconcrack_" + Item.b(itemstack.getItem()), vec3d1.c, vec3d1.d, vec3d1.e, vec3d.c, vec3d.d + 0.05D, vec3d.e);
        }
    }

    public void die(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();
        EntityLiving entityliving = this.aX();

        if (this.bb >= 0 && entityliving != null) {
            entityliving.b(this, this.bb);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.aU = true;
        if (!this.world.isStatic) {
            int i = 0;

            if (entity instanceof EntityHuman) {
                i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
            }

            if (this.aG() && this.world.getGameRules().getBoolean("doMobLoot")) {
                this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
                this.dropEquipment(this.lastDamageByPlayerTime > 0, i);
                if (false && this.lastDamageByPlayerTime > 0) { // CraftBukkit - move rare item drop call to dropDeathLoot
                    int j = this.random.nextInt(200) - i;

                    if (j < 5) {
                        this.getRareDrop(j <= 0 ? 1 : 0);
                    }
                }
            } else { // CraftBukkit
                CraftEventFactory.callEntityDeathEvent(this); // CraftBukkit
            }
        }

        this.world.broadcastEntityEffect(this, (byte) 3);
    }

    protected void dropEquipment(boolean flag, int i) {}

    public void a(Entity entity, float f, double d0, double d1) {
        if (this.random.nextDouble() >= this.getAttributeInstance(GenericAttributes.c).getValue()) {
            this.am = true;
            float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1);
            float f2 = 0.4F;

            this.motX /= 2.0D;
            this.motY /= 2.0D;
            this.motZ /= 2.0D;
            this.motX -= d0 / (double) f1 * (double) f2;
            this.motY += (double) f2;
            this.motZ -= d1 / (double) f1 * (double) f2;
            if (this.motY > 0.4000000059604645D) {
                this.motY = 0.4000000059604645D;
            }
        }
    }

    protected String aT() {
        return "game.neutral.hurt";
    }

    protected String aU() {
        return "game.neutral.die";
    }

    // CraftBukkit start - Change return type to ItemStack
    protected ItemStack getRareDrop(int i) {
        return null;
    }
    // CraftBukkit end

    protected void dropDeathLoot(boolean flag, int i) {}

    public boolean h_() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);
        Block block = this.world.getType(i, j, k);

        return block == Blocks.LADDER || block == Blocks.VINE;
    }

    public boolean isAlive() {
        return !this.dead && this.getHealth() > 0.0F;
    }

    protected void b(float f) {
        super.b(f);
        MobEffect mobeffect = this.getEffect(MobEffectList.JUMP);
        float f1 = mobeffect != null ? (float) (mobeffect.getAmplifier() + 1) : 0.0F;
        // CraftBukkit start
        float i = MathHelper.f(f - 3.0F - f1);

        if (i > 0) {
            EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(null, this, EntityDamageEvent.DamageCause.FALL, i);
            if (event.isCancelled()) {
                return;
            }

            i = (float) event.getDamage();
            if (i > 0) {
                this.getBukkitEntity().setLastDamageCause(event);
            }
        }
        // CraftBukkit end

        if (i > 0) {
            this.makeSound(this.o(org.bukkit.util.NumberConversions.ceil(i)), 1.0F, 1.0F); // CraftBukkit - ceil
            this.damageEntity(DamageSource.FALL, (float) i);
            int j = MathHelper.floor(this.locX);
            int k = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
            int l = MathHelper.floor(this.locZ);
            Block block = this.world.getType(j, k, l);

            if (block.getMaterial() != Material.AIR) {
                StepSound stepsound = block.stepSound;

                this.makeSound(stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    protected String o(int i) {
        return i > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }

    public int aV() {
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

    protected void h(float f) {}

    protected float b(DamageSource damagesource, float f) {
        if (!damagesource.ignoresArmor()) {
            int i = 25 - this.aV();
            float f1 = f * (float) i;

            this.h(f);
            f = f1 / 25.0F;
        }

        return f;
    }

    protected float c(DamageSource damagesource, float f) {
        if (damagesource.h()) {
            return f;
        } else {
            if (this instanceof EntityZombie) {
                f = f;
            }

            int i;
            int j;
            float f1;

            if (this.hasEffect(MobEffectList.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                i = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
                j = 25 - i;
                f1 = f * (float) j;
                f = f1 / 25.0F;
            }

            if (f <= 0.0F) {
                return 0.0F;
            } else {
                i = EnchantmentManager.a(this.getEquipment(), damagesource);
                if (i > 20) {
                    i = 20;
                }

                if (i > 0 && i <= 20) {
                    j = 25 - i;
                    f1 = f * (float) j;
                    f = f1 / 25.0F;
                }

                return f;
            }
        }
    }

    protected void d(DamageSource damagesource, float f) {
        if (!this.isInvulnerable()) {
            f = this.b(damagesource, f);
            f = this.c(damagesource, f);
            float f1 = f;

            f = Math.max(f - this.bs(), 0.0F);
            this.m(this.bs() - (f1 - f));
            if (f != 0.0F) {
                float f2 = this.getHealth();

                this.setHealth(f2 - f);
                this.aW().a(damagesource, f2, f);
                this.m(this.bs() - f);
            }
        }
    }

    public CombatTracker aW() {
        return this.combatTracker;
    }

    public EntityLiving aX() {
        return (EntityLiving) (this.combatTracker.c() != null ? this.combatTracker.c() : (this.killer != null ? this.killer : (this.lastDamager != null ? this.lastDamager : null)));
    }

    public final float getMaxHealth() {
        return (float) this.getAttributeInstance(GenericAttributes.a).getValue();
    }

    public final int aZ() {
        return this.datawatcher.getByte(9);
    }

    public final void p(int i) {
        this.datawatcher.watch(9, Byte.valueOf((byte) i));
    }

    private int j() {
        return this.hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    public void ba() {
        if (!this.au || this.av >= this.j() / 2 || this.av < 0) {
            this.av = -1;
            this.au = true;
            if (this.world instanceof WorldServer) {
                ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new PacketPlayOutAnimation(this, 0)));
            }
        }
    }

    protected void G() {
        this.damageEntity(DamageSource.OUT_OF_WORLD, 4.0F);
    }

    protected void bb() {
        int i = this.j();

        if (this.au) {
            ++this.av;
            if (this.av >= i) {
                this.av = 0;
                this.au = false;
            }
        } else {
            this.av = 0;
        }

        this.aE = (float) this.av / (float) i;
    }

    public AttributeInstance getAttributeInstance(IAttribute iattribute) {
        return this.bc().a(iattribute);
    }

    public AttributeMapBase bc() {
        if (this.d == null) {
            this.d = new AttributeMapServer();
        }

        return this.d;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEFINED;
    }

    public abstract ItemStack be();

    public abstract ItemStack getEquipment(int i);

    public abstract void setEquipment(int i, ItemStack itemstack);

    public void setSprinting(boolean flag) {
        super.setSprinting(flag);
        AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

        if (attributeinstance.a(b) != null) {
            attributeinstance.b(c);
        }

        if (flag) {
            attributeinstance.a(c);
        }
    }

    public abstract ItemStack[] getEquipment();

    protected float bf() {
        return 1.0F;
    }

    protected float bg() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean bh() {
        return this.getHealth() <= 0.0F;
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
    }

    public void l(Entity entity) {
        double d0 = entity.locX;
        double d1 = entity.boundingBox.b + (double) entity.length;
        double d2 = entity.locZ;
        byte b0 = 3;

        for (int i = -b0; i <= b0; ++i) {
            for (int j = -b0; j < b0; ++j) {
                if (i != 0 || j != 0) {
                    int k = (int) (this.locX + (double) i);
                    int l = (int) (this.locZ + (double) j);
                    AxisAlignedBB axisalignedbb = this.boundingBox.c((double) i, 1.0D, (double) j);

                    if (this.world.a(axisalignedbb).isEmpty()) {
                        if (World.a((IBlockAccess) this.world, k, (int) this.locY, l)) {
                            this.enderTeleportTo(this.locX + (double) i, this.locY + 1.0D, this.locZ + (double) j);
                            return;
                        }

                        if (World.a((IBlockAccess) this.world, k, (int) this.locY - 1, l) || this.world.getType(k, (int) this.locY - 1, l).getMaterial() == Material.WATER) {
                            d0 = this.locX + (double) i;
                            d1 = this.locY + 1.0D;
                            d2 = this.locZ + (double) j;
                        }
                    }
                }
            }
        }

        this.enderTeleportTo(d0, d1, d2);
    }

    protected void bj() {
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

    public void e(float f, float f1) {
        double d0;

        if (this.M() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
            d0 = this.locY;
            this.a(f, f1, this.bk() ? 0.04F : 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
            this.motY -= 0.02D;
            if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896D;
            }
        } else if (this.P() && (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.isFlying)) {
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
                f2 = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ)).frictionFactor * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            float f4;

            if (this.onGround) {
                f4 = this.bl() * f3;
            } else {
                f4 = this.aR;
            }

            this.a(f, f1, f4);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ)).frictionFactor * 0.91F;
            }

            if (this.h_()) {
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
            if (this.positionChanged && this.h_()) {
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

        this.aF = this.aG;
        d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aG += (f6 - this.aG) * 0.4F;
        this.aH += this.aG;
    }

    protected boolean bk() {
        return false;
    }

    public float bl() {
        return this.bk() ? this.bp : 0.1F;
    }

    public void i(float f) {
        this.bp = f;
    }

    public boolean m(Entity entity) {
        this.k(entity);
        return false;
    }

    public boolean isSleeping() {
        return false;
    }

    public void h() {
        super.h();
        if (!this.world.isStatic) {
            int i = this.aZ();

            if (i > 0) {
                if (this.aw <= 0) {
                    this.aw = 20 * (30 - i);
                }

                --this.aw;
                if (this.aw <= 0) {
                    this.p(i - 1);
                }
            }

            for (int j = 0; j < 5; ++j) {
                ItemStack itemstack = this.g[j];
                ItemStack itemstack1 = this.getEquipment(j);

                if (!ItemStack.matches(itemstack1, itemstack)) {
                    ((WorldServer) this.world).getTracker().a((Entity) this, (Packet) (new PacketPlayOutEntityEquipment(this.getId(), j, itemstack1)));
                    if (itemstack != null) {
                        this.d.a(itemstack.D());
                    }

                    if (itemstack1 != null) {
                        this.d.b(itemstack1.D());
                    }

                    this.g[j] = itemstack1 == null ? null : itemstack1.cloneItemStack();
                }
            }
        }

        this.e();
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.aN;
        float f2 = 0.0F;

        this.aW = this.aX;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aE > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aX += (f3 - this.aX) * 0.3F;
        this.world.methodProfiler.a("headTurn");
        f2 = this.f(f1, f2);
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("rangeChecks");

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.aN - this.aO < -180.0F) {
            this.aO -= 360.0F;
        }

        while (this.aN - this.aO >= 180.0F) {
            this.aO += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.aP - this.aQ < -180.0F) {
            this.aQ -= 360.0F;
        }

        while (this.aP - this.aQ >= 180.0F) {
            this.aQ += 360.0F;
        }

        this.world.methodProfiler.b();
        this.aY += f2;
    }

    protected float f(float f, float f1) {
        float f2 = MathHelper.g(f - this.aN);

        this.aN += f2 * 0.3F;
        float f3 = MathHelper.g(this.yaw - this.aN);
        boolean flag = f3 < -90.0F || f3 >= 90.0F;

        if (f3 < -75.0F) {
            f3 = -75.0F;
        }

        if (f3 >= 75.0F) {
            f3 = 75.0F;
        }

        this.aN = this.yaw - f3;
        if (f3 * f3 > 2500.0F) {
            this.aN += f3 * 0.2F;
        }

        if (flag) {
            f1 *= -1.0F;
        }

        return f1;
    }

    public void e() {
        if (this.bq > 0) {
            --this.bq;
        }

        if (this.bh > 0) {
            double d0 = this.locX + (this.bi - this.locX) / (double) this.bh;
            double d1 = this.locY + (this.bj - this.locY) / (double) this.bh;
            double d2 = this.locZ + (this.bk - this.locZ) / (double) this.bh;
            double d3 = MathHelper.g(this.bl - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.bh);
            this.pitch = (float) ((double) this.pitch + (this.bm - (double) this.pitch) / (double) this.bh);
            --this.bh;
            this.setPosition(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        } else if (!this.br()) {
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
        if (this.bh()) {
            this.bd = false;
            this.be = 0.0F;
            this.bf = 0.0F;
            this.bg = 0.0F;
        } else if (this.br()) {
            if (this.bk()) {
                this.world.methodProfiler.a("newAi");
                this.bn();
                this.world.methodProfiler.b();
            } else {
                this.world.methodProfiler.a("oldAi");
                this.bq();
                this.world.methodProfiler.b();
                this.aP = this.yaw;
            }
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("jump");
        if (this.bd) {
            if (!this.M() && !this.P()) {
                if (this.onGround && this.bq == 0) {
                    this.bj();
                    this.bq = 10;
                }
            } else {
                this.motY += 0.03999999910593033D;
            }
        } else {
            this.bq = 0;
        }

        this.world.methodProfiler.b();
        this.world.methodProfiler.a("travel");
        this.be *= 0.98F;
        this.bf *= 0.98F;
        this.bg *= 0.9F;
        this.e(this.be, this.bf);
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("push");
        if (!this.world.isStatic) {
            this.bo();
        }

        this.world.methodProfiler.b();
    }

    protected void bn() {}

    protected void bo() {
        List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                // TODO better check now?
                // CraftBukkit start - Only handle mob (non-player) collisions every other tick
                if (entity instanceof EntityLiving && !(this instanceof EntityPlayer) && this.ticksLived % 2 == 0) {
                    continue;
                }
                // CraftBukkit end

                if (entity.S()) {
                    this.n(entity);
                }
            }
        }
    }

    protected void n(Entity entity) {
        entity.collide(this);
    }

    public void ab() {
        super.ab();
        this.aW = this.aX;
        this.aX = 0.0F;
        this.fallDistance = 0.0F;
    }

    protected void bp() {}

    protected void bq() {
        ++this.aV;
    }

    public void f(boolean flag) {
        this.bd = flag;
    }

    public void receive(Entity entity, int i) {
        if (!entity.dead && !this.world.isStatic) {
            EntityTracker entitytracker = ((WorldServer) this.world).getTracker();

            if (entity instanceof EntityItem) {
                entitytracker.a(entity, (Packet) (new PacketPlayOutCollect(entity.getId(), this.getId())));
            }

            if (entity instanceof EntityArrow) {
                entitytracker.a(entity, (Packet) (new PacketPlayOutCollect(entity.getId(), this.getId())));
            }

            if (entity instanceof EntityExperienceOrb) {
                entitytracker.a(entity, (Packet) (new PacketPlayOutCollect(entity.getId(), this.getId())));
            }
        }
    }

    public boolean o(Entity entity) {
        return this.world.a(this.world.getVec3DPool().create(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ), this.world.getVec3DPool().create(entity.locX, entity.locY + (double) entity.getHeadHeight(), entity.locZ)) == null;
    }

    public Vec3D ag() {
        return this.j(1.0F);
    }

    public Vec3D j(float f) {
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

    public boolean br() {
        return !this.world.isStatic;
    }

    public boolean R() {
        return !this.dead;
    }

    public boolean S() {
        return !this.dead;
    }

    public float getHeadHeight() {
        return this.length * 0.85F;
    }

    protected void Q() {
        this.velocityChanged = this.random.nextDouble() >= this.getAttributeInstance(GenericAttributes.c).getValue();
    }

    public float getHeadRotation() {
        return this.aP;
    }

    public float bs() {
        return this.br;
    }

    public void m(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.br = f;
    }

    public ScoreboardTeamBase getScoreboardTeam() {
        return null;
    }

    public boolean c(EntityLiving entityliving) {
        return this.a(entityliving.getScoreboardTeam());
    }

    public boolean a(ScoreboardTeamBase scoreboardteambase) {
        return this.getScoreboardTeam() != null ? this.getScoreboardTeam().isAlly(scoreboardteambase) : false;
    }
}
