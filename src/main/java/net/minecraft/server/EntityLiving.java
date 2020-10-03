package net.minecraft.server;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.google.common.base.Function;
import org.bukkit.Location;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    private static final UUID b = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final UUID c = UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038");
    private static final AttributeModifier d = new AttributeModifier(EntityLiving.b, "Sprinting speed boost", 0.30000001192092896D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    protected static final DataWatcherObject<Byte> ag = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.a);
    public static final DataWatcherObject<Float> HEALTH = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.c);
    private static final DataWatcherObject<Integer> f = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> g = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.i);
    public static final DataWatcherObject<Integer> ARROWS_IN_BODY = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> bi = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Optional<BlockPosition>> bj = DataWatcher.a(EntityLiving.class, DataWatcherRegistry.m);
    protected static final EntitySize ah = EntitySize.c(0.2F, 0.2F);
    private final AttributeMapBase attributeMap;
    public CombatTracker combatTracker = new CombatTracker(this);
    public final Map<MobEffectList, MobEffect> effects = Maps.newHashMap();
    private final NonNullList<ItemStack> bn;
    private final NonNullList<ItemStack> bo;
    public boolean ai;
    public EnumHand aj;
    public int ak;
    public int arrowCooldown;
    public int am;
    public int hurtTicks;
    public int hurtDuration;
    public float ap;
    public int deathTicks;
    public float ar;
    public float as;
    protected int at;
    public float au;
    public float av;
    public float aw;
    public int maxNoDamageTicks;
    public final float ay;
    public final float az;
    public float aA;
    public float aB;
    public float aC;
    public float aD;
    public float aE;
    @Nullable
    public EntityHuman killer;
    protected int lastDamageByPlayerTime;
    protected boolean killed;
    protected int ticksFarFromPlayer;
    protected float aJ;
    protected float aK;
    protected float aL;
    protected float aM;
    protected float aN;
    protected int aO;
    public float lastDamage;
    protected boolean jumping;
    public float aR;
    public float aS;
    public float aT;
    protected int aU;
    protected double aV;
    protected double aW;
    protected double aX;
    protected double aY;
    protected double aZ;
    protected double ba;
    protected int bb;
    public boolean updateEffects;
    @Nullable
    public EntityLiving lastDamager;
    public int hurtTimestamp;
    private EntityLiving bs;
    private int bt;
    private float bu;
    private int jumpTicks;
    private float bw;
    protected ItemStack activeItem;
    protected int bd;
    protected int be;
    private BlockPosition bx;
    private Optional<BlockPosition> by;
    private DamageSource bz;
    private long bA;
    protected int bf;
    private float bB;
    private float bC;
    protected BehaviorController<?> bg;
    // CraftBukkit start
    public int expToDrop;
    public int maxAirTicks = 300;
    boolean forceDrops;
    ArrayList<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
    public final org.bukkit.craftbukkit.attribute.CraftAttributeMap craftAttributes;
    public boolean collides = true;
    public Set<UUID> collidableExemptions = new HashSet<>();
    public boolean canPickUpLoot;

    @Override
    public float getBukkitYaw() {
        return getHeadRotation();
    }
    // CraftBukkit end

    protected EntityLiving(EntityTypes<? extends EntityLiving> entitytypes, World world) {
        super(entitytypes, world);
        this.bn = NonNullList.a(2, ItemStack.b);
        this.bo = NonNullList.a(4, ItemStack.b);
        this.maxNoDamageTicks = 20;
        this.aE = 0.02F;
        this.updateEffects = true;
        this.activeItem = ItemStack.b;
        this.by = Optional.empty();
        this.attributeMap = new AttributeMapBase(AttributeDefaults.a(entitytypes));
        this.craftAttributes = new CraftAttributeMap(attributeMap); // CraftBukkit
        // CraftBukkit - setHealth(getMaxHealth()) inlined and simplified to skip the instanceof check for EntityPlayer, as getBukkitEntity() is not initialized in constructor
        this.datawatcher.set(EntityLiving.HEALTH, (float) this.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue());
        this.i = true;
        this.az = (float) ((Math.random() + 1.0D) * 0.009999999776482582D);
        this.ae();
        this.ay = (float) Math.random() * 12398.0F;
        this.yaw = (float) (Math.random() * 6.2831854820251465D);
        this.aC = this.yaw;
        this.G = 0.6F;
        DynamicOpsNBT dynamicopsnbt = DynamicOpsNBT.a;

        this.bg = this.a(new Dynamic(dynamicopsnbt, dynamicopsnbt.createMap((Map) ImmutableMap.of(dynamicopsnbt.createString("memories"), dynamicopsnbt.emptyMap()))));
    }

    public BehaviorController<?> getBehaviorController() {
        return this.bg;
    }

    protected BehaviorController.b<?> cJ() {
        return BehaviorController.a((Collection) ImmutableList.of(), (Collection) ImmutableList.of());
    }

    protected BehaviorController<?> a(Dynamic<?> dynamic) {
        return this.cJ().a(dynamic);
    }

    @Override
    public void killEntity() {
        this.damageEntity(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    public boolean a(EntityTypes<?> entitytypes) {
        return true;
    }

    @Override
    protected void initDatawatcher() {
        this.datawatcher.register(EntityLiving.ag, (byte) 0);
        this.datawatcher.register(EntityLiving.f, 0);
        this.datawatcher.register(EntityLiving.g, false);
        this.datawatcher.register(EntityLiving.ARROWS_IN_BODY, 0);
        this.datawatcher.register(EntityLiving.bi, 0);
        this.datawatcher.register(EntityLiving.HEALTH, 1.0F);
        this.datawatcher.register(EntityLiving.bj, Optional.empty());
    }

    public static AttributeProvider.Builder cK() {
        return AttributeProvider.a().a(GenericAttributes.MAX_HEALTH).a(GenericAttributes.KNOCKBACK_RESISTANCE).a(GenericAttributes.MOVEMENT_SPEED).a(GenericAttributes.ARMOR).a(GenericAttributes.ARMOR_TOUGHNESS);
    }

    @Override
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {
        if (!this.isInWater()) {
            this.aK();
        }

        if (!this.world.isClientSide && flag && this.fallDistance > 0.0F) {
            this.cP();
            this.cQ();
        }

        if (!this.world.isClientSide && this.fallDistance > 3.0F && flag) {
            float f = (float) MathHelper.f(this.fallDistance - 3.0F);

            if (!iblockdata.isAir()) {
                double d1 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
                int i = (int) (150.0D * d1);

                // CraftBukkit start - visiblity api
                if (this instanceof EntityPlayer) {
                    ((WorldServer) this.world).sendParticles((EntityPlayer) this, new ParticleParamBlock(Particles.BLOCK, iblockdata), this.locX(), this.locY(), this.locZ(), i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, false);
                } else {
                    ((WorldServer) this.world).a(new ParticleParamBlock(Particles.BLOCK, iblockdata), this.locX(), this.locY(), this.locZ(), i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
                }
                // CraftBukkit end
            }
        }

        super.a(d0, flag, iblockdata, blockposition);
    }

    public boolean cL() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    @Override
    public void entityBaseTick() {
        this.ar = this.as;
        if (this.justCreated) {
            this.getBedPosition().ifPresent(this::a);
        }

        if (this.cM()) {
            this.cN();
        }

        super.entityBaseTick();
        this.world.getMethodProfiler().enter("livingEntityBaseTick");
        boolean flag = this instanceof EntityHuman;

        if (this.isAlive()) {
            if (this.inBlock()) {
                this.damageEntity(DamageSource.STUCK, 1.0F);
            } else if (flag && !this.world.getWorldBorder().a(this.getBoundingBox())) {
                double d0 = this.world.getWorldBorder().a((Entity) this) + this.world.getWorldBorder().getDamageBuffer();

                if (d0 < 0.0D) {
                    double d1 = this.world.getWorldBorder().getDamageAmount();

                    if (d1 > 0.0D) {
                        this.damageEntity(DamageSource.STUCK, (float) Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.isFireProof() || this.world.isClientSide) {
            this.extinguish();
        }

        boolean flag1 = flag && ((EntityHuman) this).abilities.isInvulnerable;

        if (this.isAlive()) {
            if (this.a((Tag) TagsFluid.WATER) && !this.world.getType(new BlockPosition(this.locX(), this.getHeadY(), this.locZ())).a(Blocks.BUBBLE_COLUMN)) {
                if (!this.cL() && !MobEffectUtil.c(this) && !flag1) {
                    this.setAirTicks(this.l(this.getAirTicks()));
                    if (this.getAirTicks() == -20) {
                        this.setAirTicks(0);
                        Vec3D vec3d = this.getMot();

                        for (int i = 0; i < 8; ++i) {
                            double d2 = this.random.nextDouble() - this.random.nextDouble();
                            double d3 = this.random.nextDouble() - this.random.nextDouble();
                            double d4 = this.random.nextDouble() - this.random.nextDouble();

                            this.world.addParticle(Particles.BUBBLE, this.locX() + d2, this.locY() + d3, this.locZ() + d4, vec3d.x, vec3d.y, vec3d.z);
                        }

                        this.damageEntity(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.world.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().bs()) {
                    this.stopRiding();
                }
            } else if (this.getAirTicks() < this.bG()) {
                this.setAirTicks(this.m(this.getAirTicks()));
            }

            if (!this.world.isClientSide) {
                BlockPosition blockposition = this.getChunkCoordinates();

                if (!Objects.equal(this.bx, blockposition)) {
                    this.bx = blockposition;
                    this.c(blockposition);
                }
            }
        }

        if (this.isAlive() && this.aF()) {
            this.extinguish();
        }

        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }

        if (this.noDamageTicks > 0 && !(this instanceof EntityPlayer)) {
            --this.noDamageTicks;
        }

        if (this.dk()) {
            this.cT();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.killer = null;
        }

        if (this.bs != null && !this.bs.isAlive()) {
            this.bs = null;
        }

        if (this.lastDamager != null) {
            if (!this.lastDamager.isAlive()) {
                this.setLastDamager((EntityLiving) null);
            } else if (this.ticksLived - this.hurtTimestamp > 100) {
                this.setLastDamager((EntityLiving) null);
            }
        }

        this.tickPotionEffects();
        this.aM = this.aL;
        this.aB = this.aA;
        this.aD = this.aC;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.world.getMethodProfiler().exit();
    }

    public boolean cM() {
        return this.ticksLived % 5 == 0 && this.getMot().x != 0.0D && this.getMot().z != 0.0D && !this.isSpectator() && EnchantmentManager.j(this) && this.cO();
    }

    protected void cN() {
        Vec3D vec3d = this.getMot();

        this.world.addParticle(Particles.SOUL, this.locX() + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), this.locY() + 0.1D, this.locZ() + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), vec3d.x * -0.2D, 0.1D, vec3d.z * -0.2D);
        float f = this.random.nextFloat() * 0.4F + this.random.nextFloat() > 0.9F ? 0.6F : 0.0F;

        this.playSound(SoundEffects.PARTICLE_SOUL_ESCAPE, f, 0.6F + this.random.nextFloat() * 0.4F);
    }

    protected boolean cO() {
        return this.world.getType(this.ar()).a((Tag) TagsBlock.SOUL_SPEED_BLOCKS);
    }

    @Override
    protected float getBlockSpeedFactor() {
        return this.cO() && EnchantmentManager.a(Enchantments.SOUL_SPEED, this) > 0 ? 1.0F : super.getBlockSpeedFactor();
    }

    protected boolean b(IBlockData iblockdata) {
        return !iblockdata.isAir() || this.isGliding();
    }

    protected void cP() {
        AttributeModifiable attributemodifiable = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

        if (attributemodifiable != null) {
            if (attributemodifiable.a(EntityLiving.c) != null) {
                attributemodifiable.b(EntityLiving.c);
            }

        }
    }

    protected void cQ() {
        if (!this.aM().isAir()) {
            int i = EnchantmentManager.a(Enchantments.SOUL_SPEED, this);

            if (i > 0 && this.cO()) {
                AttributeModifiable attributemodifiable = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

                if (attributemodifiable == null) {
                    return;
                }

                attributemodifiable.b(new AttributeModifier(EntityLiving.c, "Soul speed boost", (double) (0.03F * (1.0F + (float) i * 0.35F)), AttributeModifier.Operation.ADDITION));
                if (this.getRandom().nextFloat() < 0.04F) {
                    ItemStack itemstack = this.getEquipment(EnumItemSlot.FEET);

                    itemstack.damage(1, this, (entityliving) -> {
                        entityliving.broadcastItemBreak(EnumItemSlot.FEET);
                    });
                }
            }
        }

    }

    protected void c(BlockPosition blockposition) {
        int i = EnchantmentManager.a(Enchantments.FROST_WALKER, this);

        if (i > 0) {
            EnchantmentFrostWalker.a(this, this.world, blockposition, i);
        }

        if (this.b(this.aM())) {
            this.cP();
        }

        this.cQ();
    }

    public boolean isBaby() {
        return false;
    }

    public float cR() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    protected boolean cS() {
        return true;
    }

    @Override
    public boolean bs() {
        return false;
    }

    protected void cT() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.dead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            this.die();

            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.addParticle(Particles.POOF, this.d(1.0D), this.cE(), this.g(1.0D), d0, d1, d2);
            }
        }

    }

    protected boolean isDropExperience() {
        return !this.isBaby();
    }

    protected boolean cV() {
        return !this.isBaby();
    }

    protected int l(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int m(int i) {
        return Math.min(i + 4, this.bG());
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return 0;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public Random getRandom() {
        return this.random;
    }

    @Nullable
    public EntityLiving getLastDamager() {
        return this.lastDamager;
    }

    public int cZ() {
        return this.hurtTimestamp;
    }

    public void e(@Nullable EntityHuman entityhuman) {
        this.killer = entityhuman;
        this.lastDamageByPlayerTime = this.ticksLived;
    }

    public void setLastDamager(@Nullable EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.hurtTimestamp = this.ticksLived;
    }

    @Nullable
    public EntityLiving da() {
        return this.bs;
    }

    public int db() {
        return this.bt;
    }

    public void z(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.bs = (EntityLiving) entity;
        } else {
            this.bs = null;
        }

        this.bt = this.ticksLived;
    }

    public int dc() {
        return this.ticksFarFromPlayer;
    }

    public void n(int i) {
        this.ticksFarFromPlayer = i;
    }

    protected void b(ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            SoundEffect soundeffect = SoundEffects.ITEM_ARMOR_EQUIP_GENERIC;
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor) {
                soundeffect = ((ItemArmor) item).ab_().b();
            } else if (item == Items.ELYTRA) {
                soundeffect = SoundEffects.ITEM_ARMOR_EQUIP_ELYTRA;
            }

            this.playSound(soundeffect, 1.0F, 1.0F);
        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setFloat("Health", this.getHealth());
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setInt("HurtByTimestamp", this.hurtTimestamp);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setFloat("AbsorptionAmount", this.getAbsorptionHearts());
        nbttagcompound.set("Attributes", this.getAttributeMap().c());
        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                nbttaglist.add(mobeffect.a(new NBTTagCompound()));
            }

            nbttagcompound.set("ActiveEffects", nbttaglist);
        }

        nbttagcompound.setBoolean("FallFlying", this.isGliding());
        this.getBedPosition().ifPresent((blockposition) -> {
            nbttagcompound.setInt("SleepingX", blockposition.getX());
            nbttagcompound.setInt("SleepingY", blockposition.getY());
            nbttagcompound.setInt("SleepingZ", blockposition.getZ());
        });
        DataResult<NBTBase> dataresult = this.bg.a((DynamicOps) DynamicOpsNBT.a);
        Logger logger = EntityLiving.LOGGER;

        logger.getClass();
        dataresult.resultOrPartial(logger::error).ifPresent((nbtbase) -> {
            nbttagcompound.set("Brain", nbtbase);
        });
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.setAbsorptionHearts(nbttagcompound.getFloat("AbsorptionAmount"));
        if (nbttagcompound.hasKeyOfType("Attributes", 9) && this.world != null && !this.world.isClientSide) {
            this.getAttributeMap().a(nbttagcompound.getList("Attributes", 10));
        }

        if (nbttagcompound.hasKeyOfType("ActiveEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);
                MobEffect mobeffect = MobEffect.b(nbttagcompound1);

                if (mobeffect != null) {
                    this.effects.put(mobeffect.getMobEffect(), mobeffect);
                }
            }
        }

        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxHealth")) {
            NBTBase nbtbase = nbttagcompound.get("Bukkit.MaxHealth");
            if (nbtbase.getTypeId() == 5) {
                this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(((NBTTagFloat) nbtbase).asDouble());
            } else if (nbtbase.getTypeId() == 3) {
                this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(((NBTTagInt) nbtbase).asDouble());
            }
        }
        // CraftBukkit end

        if (nbttagcompound.hasKeyOfType("Health", 99)) {
            this.setHealth(nbttagcompound.getFloat("Health"));
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.hurtTimestamp = nbttagcompound.getInt("HurtByTimestamp");
        if (nbttagcompound.hasKeyOfType("Team", 8)) {
            String s = nbttagcompound.getString("Team");
            ScoreboardTeam scoreboardteam = this.world.getScoreboard().getTeam(s);
            boolean flag = scoreboardteam != null && this.world.getScoreboard().addPlayerToTeam(this.getUniqueIDString(), scoreboardteam);

            if (!flag) {
                EntityLiving.LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", s);
            }
        }

        if (nbttagcompound.getBoolean("FallFlying")) {
            this.setFlag(7, true);
        }

        if (nbttagcompound.hasKeyOfType("SleepingX", 99) && nbttagcompound.hasKeyOfType("SleepingY", 99) && nbttagcompound.hasKeyOfType("SleepingZ", 99)) {
            BlockPosition blockposition = new BlockPosition(nbttagcompound.getInt("SleepingX"), nbttagcompound.getInt("SleepingY"), nbttagcompound.getInt("SleepingZ"));

            this.e(blockposition);
            this.datawatcher.set(EntityLiving.POSE, EntityPose.SLEEPING);
            if (!this.justCreated) {
                this.a(blockposition);
            }
        }

        if (nbttagcompound.hasKeyOfType("Brain", 10)) {
            this.bg = this.a(new Dynamic(DynamicOpsNBT.a, nbttagcompound.get("Brain")));
        }

    }

    // CraftBukkit start
    private boolean isTickingEffects = false;
    private List<ProcessableEffect> effectsToProcess = Lists.newArrayList();

    private static class ProcessableEffect {

        private MobEffectList type;
        private MobEffect effect;
        private final EntityPotionEffectEvent.Cause cause;

        private ProcessableEffect(MobEffect effect, EntityPotionEffectEvent.Cause cause) {
            this.effect = effect;
            this.cause = cause;
        }

        private ProcessableEffect(MobEffectList type, EntityPotionEffectEvent.Cause cause) {
            this.type = type;
            this.cause = cause;
        }
    }
    // CraftBukkit end

    protected void tickPotionEffects() {
        Iterator iterator = this.effects.keySet().iterator();

        isTickingEffects = true; // CraftBukkit
        try {
            while (iterator.hasNext()) {
                MobEffectList mobeffectlist = (MobEffectList) iterator.next();
                MobEffect mobeffect = (MobEffect) this.effects.get(mobeffectlist);

                if (!mobeffect.tick(this, () -> {
                    this.a(mobeffect, true);
                })) {
                    if (!this.world.isClientSide) {
                        // CraftBukkit start
                        EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, mobeffect, null, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.EXPIRATION);
                        if (event.isCancelled()) {
                            continue;
                        }
                        // CraftBukkit end
                        iterator.remove();
                        this.b(mobeffect);
                    }
                } else if (mobeffect.getDuration() % 600 == 0) {
                    this.a(mobeffect, false);
                }
            }
        } catch (ConcurrentModificationException concurrentmodificationexception) {
            ;
        }
        // CraftBukkit start
        isTickingEffects = false;
        for (ProcessableEffect e : effectsToProcess) {
            if (e.effect != null) {
                addEffect(e.effect, e.cause);
            } else {
                removeEffect(e.type, e.cause);
            }
        }
        effectsToProcess.clear();
        // CraftBukkit end

        if (this.updateEffects) {
            if (!this.world.isClientSide) {
                this.C();
            }

            this.updateEffects = false;
        }

        int i = (Integer) this.datawatcher.get(EntityLiving.f);
        boolean flag = (Boolean) this.datawatcher.get(EntityLiving.g);

        if (i > 0) {
            boolean flag1;

            if (this.isInvisible()) {
                flag1 = this.random.nextInt(15) == 0;
            } else {
                flag1 = this.random.nextBoolean();
            }

            if (flag) {
                flag1 &= this.random.nextInt(5) == 0;
            }

            if (flag1 && i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.world.addParticle(flag ? Particles.AMBIENT_ENTITY_EFFECT : Particles.ENTITY_EFFECT, this.d(0.5D), this.cE(), this.g(0.5D), d0, d1, d2);
            }
        }

    }

    protected void C() {
        if (this.effects.isEmpty()) {
            this.de();
            this.setInvisible(false);
        } else {
            Collection<MobEffect> collection = this.effects.values();

            this.datawatcher.set(EntityLiving.g, c(collection));
            this.datawatcher.set(EntityLiving.f, PotionUtil.a(collection));
            this.setInvisible(this.hasEffect(MobEffects.INVISIBILITY));
        }

    }

    public double A(@Nullable Entity entity) {
        double d0 = 1.0D;

        if (this.bw()) {
            d0 *= 0.8D;
        }

        if (this.isInvisible()) {
            float f = this.dE();

            if (f < 0.1F) {
                f = 0.1F;
            }

            d0 *= 0.7D * (double) f;
        }

        if (entity != null) {
            ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);
            Item item = itemstack.getItem();
            EntityTypes<?> entitytypes = entity.getEntityType();

            if (entitytypes == EntityTypes.SKELETON && item == Items.SKELETON_SKULL || entitytypes == EntityTypes.ZOMBIE && item == Items.ZOMBIE_HEAD || entitytypes == EntityTypes.CREEPER && item == Items.CREEPER_HEAD) {
                d0 *= 0.5D;
            }
        }

        return d0;
    }

    public boolean c(EntityLiving entityliving) {
        return true;
    }

    public boolean a(EntityLiving entityliving, PathfinderTargetCondition pathfindertargetcondition) {
        return pathfindertargetcondition.a(this, entityliving);
    }

    public static boolean c(Collection<MobEffect> collection) {
        Iterator iterator = collection.iterator();

        MobEffect mobeffect;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            mobeffect = (MobEffect) iterator.next();
        } while (mobeffect.isAmbient());

        return false;
    }

    protected void de() {
        this.datawatcher.set(EntityLiving.g, false);
        this.datawatcher.set(EntityLiving.f, 0);
    }

    // CraftBukkit start
    public boolean removeAllEffects() {
        return removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause.UNKNOWN);
    }

    public boolean removeAllEffects(EntityPotionEffectEvent.Cause cause) {
        // CraftBukkit end
        if (this.world.isClientSide) {
            return false;
        } else {
            Iterator<MobEffect> iterator = this.effects.values().iterator();

            boolean flag;

            for (flag = false; iterator.hasNext(); flag = true) {
                // CraftBukkit start
                MobEffect effect = (MobEffect) iterator.next();
                EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, effect, null, cause, EntityPotionEffectEvent.Action.CLEARED);
                if (event.isCancelled()) {
                    continue;
                }
                this.b(effect);
                // CraftBukkit end
                iterator.remove();
            }

            return flag;
        }
    }

    public Collection<MobEffect> getEffects() {
        return this.effects.values();
    }

    public Map<MobEffectList, MobEffect> dh() {
        return this.effects;
    }

    public boolean hasEffect(MobEffectList mobeffectlist) {
        return this.effects.containsKey(mobeffectlist);
    }

    @Nullable
    public MobEffect getEffect(MobEffectList mobeffectlist) {
        return (MobEffect) this.effects.get(mobeffectlist);
    }

    // CraftBukkit start
    public boolean addEffect(MobEffect mobeffect) {
        return addEffect(mobeffect, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.UNKNOWN);
    }

    public boolean addEffect(MobEffect mobeffect, EntityPotionEffectEvent.Cause cause) {
        if (isTickingEffects) {
            effectsToProcess.add(new ProcessableEffect(mobeffect, cause));
            return true;
        }
        // CraftBukkit end

        if (!this.d(mobeffect)) {
            return false;
        } else {
            MobEffect mobeffect1 = (MobEffect) this.effects.get(mobeffect.getMobEffect());

            // CraftBukkit start
            boolean override = false;
            if (mobeffect1 != null) {
                override = new MobEffect(mobeffect1).b(mobeffect);
            }

            EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, mobeffect1, mobeffect, cause, override);
            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            if (mobeffect1 == null) {
                this.effects.put(mobeffect.getMobEffect(), mobeffect);
                this.a(mobeffect);
                return true;
                // CraftBukkit start
            } else if (event.isOverride()) {
                mobeffect1.b(mobeffect);
                this.a(mobeffect1, true);
                // CraftBukkit end
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean d(MobEffect mobeffect) {
        if (this.getMonsterType() == EnumMonsterType.UNDEAD) {
            MobEffectList mobeffectlist = mobeffect.getMobEffect();

            if (mobeffectlist == MobEffects.REGENERATION || mobeffectlist == MobEffects.POISON) {
                return false;
            }
        }

        return true;
    }

    public boolean di() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    // CraftBukkit start
    @Nullable
    public MobEffect c(@Nullable MobEffectList mobeffectlist) {
        return c(mobeffectlist, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.UNKNOWN);
    }

    @Nullable
    public MobEffect c(@Nullable MobEffectList mobeffectlist, EntityPotionEffectEvent.Cause cause) {
        if (isTickingEffects) {
            effectsToProcess.add(new ProcessableEffect(mobeffectlist, cause));
            return null;
        }

        MobEffect effect = this.effects.get(mobeffectlist);
        if (effect == null) {
            return null;
        }

        EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, effect, null, cause);
        if (event.isCancelled()) {
            return null;
        }

        return (MobEffect) this.effects.remove(mobeffectlist);
    }

    public boolean removeEffect(MobEffectList mobeffectlist) {
        return removeEffect(mobeffectlist, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.UNKNOWN);
    }

    public boolean removeEffect(MobEffectList mobeffectlist, EntityPotionEffectEvent.Cause cause) {
        MobEffect mobeffect = this.c(mobeffectlist, cause);
        // CraftBukkit end

        if (mobeffect != null) {
            this.b(mobeffect);
            return true;
        } else {
            return false;
        }
    }

    protected void a(MobEffect mobeffect) {
        this.updateEffects = true;
        if (!this.world.isClientSide) {
            mobeffect.getMobEffect().b(this, this.getAttributeMap(), mobeffect.getAmplifier());
        }

    }

    protected void a(MobEffect mobeffect, boolean flag) {
        this.updateEffects = true;
        if (flag && !this.world.isClientSide) {
            MobEffectList mobeffectlist = mobeffect.getMobEffect();

            mobeffectlist.a(this, this.getAttributeMap(), mobeffect.getAmplifier());
            mobeffectlist.b(this, this.getAttributeMap(), mobeffect.getAmplifier());
        }

    }

    protected void b(MobEffect mobeffect) {
        this.updateEffects = true;
        if (!this.world.isClientSide) {
            mobeffect.getMobEffect().a(this, this.getAttributeMap(), mobeffect.getAmplifier());
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
            // Suppress during worldgen
            if (this.valid) {
                this.world.getServer().getPluginManager().callEvent(event);
            }

            if (!event.isCancelled()) {
                this.setHealth((float) (this.getHealth() + event.getAmount()));
            }
            // CraftBukkit end
        }

    }

    public float getHealth() {
        // CraftBukkit start - Use unscaled health
        if (this instanceof EntityPlayer) {
            return (float) ((EntityPlayer) this).getBukkitEntity().getHealth();
        }
        // CraftBukkit end
        return (Float) this.datawatcher.get(EntityLiving.HEALTH);
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

            player.updateScaledHealth(false);
            return;
        }
        // CraftBukkit end
        this.datawatcher.set(EntityLiving.HEALTH, MathHelper.a(f, 0.0F, this.getMaxHealth()));
    }

    public boolean dk() {
        return this.getHealth() <= 0.0F;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (this.world.isClientSide) {
            return false;
        } else if (this.dead || this.killed || this.getHealth() <= 0.0F) { // CraftBukkit - Don't allow entities that got set to dead/killed elsewhere to get damaged and die
            return false;
        } else if (damagesource.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            if (this.isSleeping() && !this.world.isClientSide) {
                this.entityWakeup();
            }

            this.ticksFarFromPlayer = 0;
            float f1 = f;

            // CraftBukkit - Moved into damageEntity0(DamageSource, float)
            if (false && (damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && !this.getEquipment(EnumItemSlot.HEAD).isEmpty()) {
                this.getEquipment(EnumItemSlot.HEAD).damage((int) (f * 4.0F + this.random.nextFloat() * f * 2.0F), this, (entityliving) -> {
                    entityliving.broadcastItemBreak(EnumItemSlot.HEAD);
                });
                f *= 0.75F;
            }

            boolean flag = f > 0.0F && this.applyBlockingModifier(damagesource); // Copied from below
            float f2 = 0.0F;

            // CraftBukkit - Moved into damageEntity0(DamageSource, float)
            if (false && f > 0.0F && this.applyBlockingModifier(damagesource)) {
                this.damageShield(f);
                f2 = f;
                f = 0.0F;
                if (!damagesource.b()) {
                    Entity entity = damagesource.j();

                    if (entity instanceof EntityLiving) {
                        this.shieldBlock((EntityLiving) entity);
                    }
                }

                flag = true;
            }

            this.av = 1.5F;
            boolean flag1 = true;

            if ((float) this.noDamageTicks > 10.0F) {
                if (f <= this.lastDamage) {
                    this.forceExplosionKnockback = true; // CraftBukkit - SPIGOT-949 - for vanilla consistency, cooldown does not prevent explosion knockback
                    return false;
                }

                // CraftBukkit start
                if (!this.damageEntity0(damagesource, f - this.lastDamage)) {
                    return false;
                }
                // CraftBukkit end
                this.lastDamage = f;
                flag1 = false;
            } else {
                // CraftBukkit start
                if (!this.damageEntity0(damagesource, f)) {
                    return false;
                }
                this.lastDamage = f;
                this.noDamageTicks = 20;
                // this.damageEntity0(damagesource, f);
                // CraftBukkit end
                this.hurtDuration = 10;
                this.hurtTicks = this.hurtDuration;
            }

            // CraftBukkit start
            if (this instanceof EntityAnimal) {
                ((EntityAnimal) this).resetLove();
                if (this instanceof EntityTameableAnimal) {
                    ((EntityTameableAnimal) this).setWillSit(false);
                }
            }
            // CraftBukkit end

            this.ap = 0.0F;
            Entity entity1 = damagesource.getEntity();

            if (entity1 != null) {
                if (entity1 instanceof EntityLiving) {
                    this.setLastDamager((EntityLiving) entity1);
                }

                if (entity1 instanceof EntityHuman) {
                    this.lastDamageByPlayerTime = 100;
                    this.killer = (EntityHuman) entity1;
                } else if (entity1 instanceof EntityWolf) {
                    EntityWolf entitywolf = (EntityWolf) entity1;

                    if (entitywolf.isTamed()) {
                        this.lastDamageByPlayerTime = 100;
                        EntityLiving entityliving = entitywolf.getOwner();

                        if (entityliving != null && entityliving.getEntityType() == EntityTypes.PLAYER) {
                            this.killer = (EntityHuman) entityliving;
                        } else {
                            this.killer = null;
                        }
                    }
                }
            }

            if (flag1) {
                if (flag) {
                    this.world.broadcastEntityEffect(this, (byte) 29);
                } else if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).y()) {
                    this.world.broadcastEntityEffect(this, (byte) 33);
                } else {
                    byte b0;

                    if (damagesource == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (damagesource.isFire()) {
                        b0 = 37;
                    } else if (damagesource == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else {
                        b0 = 2;
                    }

                    this.world.broadcastEntityEffect(this, b0);
                }

                if (damagesource != DamageSource.DROWN && (!flag || f > 0.0F)) {
                    this.velocityChanged();
                }

                if (entity1 != null) {
                    double d0 = entity1.locX() - this.locX();

                    double d1;

                    for (d1 = entity1.locZ() - this.locZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                        d0 = (Math.random() - Math.random()) * 0.01D;
                    }

                    this.ap = (float) (MathHelper.d(d1, d0) * 57.2957763671875D - (double) this.yaw);
                    this.a(0.4F, d0, d1);
                } else {
                    this.ap = (float) ((int) (Math.random() * 2.0D) * 180);
                }
            }

            if (this.dk()) {
                if (!this.f(damagesource)) {
                    SoundEffect soundeffect = this.getSoundDeath();

                    if (flag1 && soundeffect != null) {
                        this.playSound(soundeffect, this.getSoundVolume(), this.dG());
                    }

                    this.die(damagesource);
                }
            } else if (flag1) {
                this.c(damagesource);
            }

            boolean flag2 = !flag || f > 0.0F;

            if (flag2) {
                this.bz = damagesource;
                this.bA = this.world.getTime();
            }

            if (this instanceof EntityPlayer) {
                CriterionTriggers.h.a((EntityPlayer) this, damagesource, f1, f, flag);
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    ((EntityPlayer) this).a(StatisticList.DAMAGE_BLOCKED_BY_SHIELD, Math.round(f2 * 10.0F));
                }
            }

            if (entity1 instanceof EntityPlayer) {
                CriterionTriggers.g.a((EntityPlayer) entity1, this, damagesource, f1, f, flag);
            }

            return flag2;
        }
    }

    protected void shieldBlock(EntityLiving entityliving) {
        entityliving.e(this);
    }

    protected void e(EntityLiving entityliving) {
        entityliving.a(0.5F, entityliving.locX() - this.locX(), entityliving.locZ() - this.locZ());
    }

    private boolean f(DamageSource damagesource) {
        if (damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            ItemStack itemstack = null;
            EnumHand[] aenumhand = EnumHand.values();
            int i = aenumhand.length;

            // CraftBukkit start
            ItemStack itemstack1 = ItemStack.b;
            for (int j = 0; j < i; ++j) {
                EnumHand enumhand = aenumhand[j];
                itemstack1 = this.b(enumhand);

                if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING) {
                    itemstack = itemstack1.cloneItemStack();
                    // itemstack1.subtract(1); // CraftBukkit
                    break;
                }
            }

            EntityResurrectEvent event = new EntityResurrectEvent((LivingEntity) this.getBukkitEntity());
            event.setCancelled(itemstack == null);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (!itemstack1.isEmpty()) {
                    itemstack1.subtract(1);
                }
                if (itemstack != null && this instanceof EntityPlayer) {
                    // CraftBukkit end
                    EntityPlayer entityplayer = (EntityPlayer) this;

                    entityplayer.b(StatisticList.ITEM_USED.b(Items.TOTEM_OF_UNDYING));
                    CriterionTriggers.B.a(entityplayer, itemstack);
                }

                this.setHealth(1.0F);
                // CraftBukkit start
                this.removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause.TOTEM);
                this.addEffect(new MobEffect(MobEffects.REGENERATION, 900, 1), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.TOTEM);
                this.addEffect(new MobEffect(MobEffects.ABSORBTION, 100, 1), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.TOTEM);
                this.addEffect(new MobEffect(MobEffects.FIRE_RESISTANCE, 800, 0), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.TOTEM);
                // CraftBukkit end
                this.world.broadcastEntityEffect(this, (byte) 35);
            }

            return !event.isCancelled();
        }
    }

    @Nullable
    public DamageSource dl() {
        if (this.world.getTime() - this.bA > 40L) {
            this.bz = null;
        }

        return this.bz;
    }

    protected void c(DamageSource damagesource) {
        SoundEffect soundeffect = this.getSoundHurt(damagesource);

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.dG());
        }

    }

    private boolean applyBlockingModifier(DamageSource damagesource) {
        Entity entity = damagesource.j();
        boolean flag = false;

        if (entity instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.getPierceLevel() > 0) {
                flag = true;
            }
        }

        if (!damagesource.ignoresArmor() && this.isBlocking() && !flag) {
            Vec3D vec3d = damagesource.w();

            if (vec3d != null) {
                Vec3D vec3d1 = this.f(1.0F);
                Vec3D vec3d2 = vec3d.a(this.getPositionVector()).d();

                vec3d2 = new Vec3D(vec3d2.x, 0.0D, vec3d2.z);
                if (vec3d2.b(vec3d1) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    public void die(DamageSource damagesource) {
        if (!this.dead && !this.killed) {
            Entity entity = damagesource.getEntity();
            EntityLiving entityliving = this.getKillingEntity();

            if (this.aO >= 0 && entityliving != null) {
                entityliving.a(this, this.aO, damagesource);
            }

            if (this.isSleeping()) {
                this.entityWakeup();
            }

            this.killed = true;
            this.getCombatTracker().g();
            if (this.world instanceof WorldServer) {
                if (entity != null) {
                    entity.a((WorldServer) this.world, this);
                }

                this.d(damagesource);
                this.f(entityliving);
            }

            this.world.broadcastEntityEffect(this, (byte) 3);
            this.setPose(EntityPose.DYING);
        }
    }

    protected void f(@Nullable EntityLiving entityliving) {
        if (!this.world.isClientSide) {
            boolean flag = false;

            if (entityliving instanceof EntityWither) {
                if (this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                    BlockPosition blockposition = this.getChunkCoordinates();
                    IBlockData iblockdata = Blocks.WITHER_ROSE.getBlockData();

                    if (this.world.getType(blockposition).isAir() && iblockdata.canPlace(this.world, blockposition)) {
                        this.world.setTypeAndData(blockposition, iblockdata, 3);
                        flag = true;
                    }
                }

                if (!flag) {
                    EntityItem entityitem = new EntityItem(this.world, this.locX(), this.locY(), this.locZ(), new ItemStack(Items.bt));

                    this.world.addEntity(entityitem);
                }
            }

        }
    }

    protected void d(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();
        int i;

        if (entity instanceof EntityHuman) {
            i = EnchantmentManager.g((EntityLiving) entity);
        } else {
            i = 0;
        }

        boolean flag = this.lastDamageByPlayerTime > 0;

        this.dropInventory(); // CraftBukkit - from below
        if (this.cV() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            this.a(damagesource, flag);
            this.dropDeathLoot(damagesource, i, flag);
        }
        // CraftBukkit start - Call death event
        CraftEventFactory.callEntityDeathEvent(this, this.drops);
        this.drops = new ArrayList<>();
        // CraftBukkit end

        // this.dropInventory();// CraftBukkit - moved up
        this.dropExperience();
    }

    protected void dropInventory() {}

    // CraftBukkit start
    public int getExpReward() {
        if (!this.world.isClientSide && (this.alwaysGivesExp() || this.lastDamageByPlayerTime > 0 && this.isDropExperience() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
            int i = this.getExpValue(this.killer);
            return i;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    protected void dropExperience() {
        // CraftBukkit start - Update getExpReward() above if the removed if() changes!
        if (true) {
            int i = this.expToDrop;
            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX(), this.locY(), this.locZ(), j));
            }
            this.expToDrop = 0;
        }
        // CraftBukkit end

    }

    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {}

    public MinecraftKey do_() {
        return this.getEntityType().i();
    }

    protected void a(DamageSource damagesource, boolean flag) {
        MinecraftKey minecraftkey = this.do_();
        LootTable loottable = this.world.getMinecraftServer().getLootTableRegistry().getLootTable(minecraftkey);
        LootTableInfo.Builder loottableinfo_builder = this.a(flag, damagesource);

        loottable.populateLoot(loottableinfo_builder.build(LootContextParameterSets.ENTITY), this::a);
    }

    protected LootTableInfo.Builder a(boolean flag, DamageSource damagesource) {
        LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).a(this.random).set(LootContextParameters.THIS_ENTITY, this).set(LootContextParameters.ORIGIN, this.getPositionVector()).set(LootContextParameters.DAMAGE_SOURCE, damagesource).setOptional(LootContextParameters.KILLER_ENTITY, damagesource.getEntity()).setOptional(LootContextParameters.DIRECT_KILLER_ENTITY, damagesource.j());

        if (flag && this.killer != null) {
            loottableinfo_builder = loottableinfo_builder.set(LootContextParameters.LAST_DAMAGE_PLAYER, this.killer).a(this.killer.eT());
        }

        return loottableinfo_builder;
    }

    public void a(float f, double d0, double d1) {
        f = (float) ((double) f * (1.0D - this.b(GenericAttributes.KNOCKBACK_RESISTANCE)));
        if (f > 0.0F) {
            this.impulse = true;
            Vec3D vec3d = this.getMot();
            Vec3D vec3d1 = (new Vec3D(d0, 0.0D, d1)).d().a((double) f);

            this.setMot(vec3d.x / 2.0D - vec3d1.x, this.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double) f) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
        }
    }

    @Nullable
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_GENERIC_DEATH;
    }

    protected SoundEffect getSoundFall(int i) {
        return i > 4 ? SoundEffects.ENTITY_GENERIC_BIG_FALL : SoundEffects.ENTITY_GENERIC_SMALL_FALL;
    }

    protected SoundEffect c(ItemStack itemstack) {
        return itemstack.G();
    }

    public SoundEffect d(ItemStack itemstack) {
        return itemstack.H();
    }

    @Override
    public void setOnGround(boolean flag) {
        super.setOnGround(flag);
        if (flag) {
            this.by = Optional.empty();
        }

    }

    public Optional<BlockPosition> dq() {
        return this.by;
    }

    public boolean isClimbing() {
        if (this.isSpectator()) {
            return false;
        } else {
            BlockPosition blockposition = this.getChunkCoordinates();
            IBlockData iblockdata = this.dr();
            Block block = iblockdata.getBlock();

            if (block.a((Tag) TagsBlock.CLIMBABLE)) {
                this.by = Optional.of(blockposition);
                return true;
            } else if (block instanceof BlockTrapdoor && this.c(blockposition, iblockdata)) {
                this.by = Optional.of(blockposition);
                return true;
            } else {
                return false;
            }
        }
    }

    public IBlockData dr() {
        return this.world.getType(this.getChunkCoordinates());
    }

    private boolean c(BlockPosition blockposition, IBlockData iblockdata) {
        if ((Boolean) iblockdata.get(BlockTrapdoor.OPEN)) {
            IBlockData iblockdata1 = this.world.getType(blockposition.down());

            if (iblockdata1.a(Blocks.LADDER) && iblockdata1.get(BlockLadder.FACING) == iblockdata.get(BlockTrapdoor.FACING)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAlive() {
        return !this.dead && this.getHealth() > 0.0F;
    }

    @Override
    public boolean b(float f, float f1) {
        boolean flag = super.b(f, f1);
        int i = this.e(f, f1);

        if (i > 0) {
            // CraftBukkit start
            if (!this.damageEntity(DamageSource.FALL, (float) i)) {
                return true;
            }
            // CraftBukkit end
            this.playSound(this.getSoundFall(i), 1.0F, 1.0F);
            this.playBlockStepSound();
            // this.damageEntity(DamageSource.FALL, (float) i); // CraftBukkit - moved up
            return true;
        } else {
            return flag;
        }
    }

    protected int e(float f, float f1) {
        MobEffect mobeffect = this.getEffect(MobEffects.JUMP);
        float f2 = mobeffect == null ? 0.0F : (float) (mobeffect.getAmplifier() + 1);

        return MathHelper.f((f - 3.0F - f2) * f1);
    }

    protected void playBlockStepSound() {
        if (!this.isSilent()) {
            int i = MathHelper.floor(this.locX());
            int j = MathHelper.floor(this.locY() - 0.20000000298023224D);
            int k = MathHelper.floor(this.locZ());
            IBlockData iblockdata = this.world.getType(new BlockPosition(i, j, k));

            if (!iblockdata.isAir()) {
                SoundEffectType soundeffecttype = iblockdata.getStepSound();

                this.playSound(soundeffecttype.g(), soundeffecttype.a() * 0.5F, soundeffecttype.b() * 0.75F);
            }

        }
    }

    public int getArmorStrength() {
        return MathHelper.floor(this.b(GenericAttributes.ARMOR));
    }

    protected void damageArmor(DamageSource damagesource, float f) {}

    protected void damageShield(float f) {}

    protected float applyArmorModifier(DamageSource damagesource, float f) {
        if (!damagesource.ignoresArmor()) {
            // this.damageArmor(damagesource, f); // CraftBukkit - Moved into damageEntity0(DamageSource, float)
            f = CombatMath.a(f, (float) this.getArmorStrength(), (float) this.b(GenericAttributes.ARMOR_TOUGHNESS));
        }

        return f;
    }

    protected float applyMagicModifier(DamageSource damagesource, float f) {
        if (damagesource.isStarvation()) {
            return f;
        } else {
            int i;

            // CraftBukkit - Moved to damageEntity0(DamageSource, float)
            if (false && this.hasEffect(MobEffects.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                i = (this.getEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f1 = f * (float) j;
                float f2 = f;

                f = Math.max(f1 / 25.0F, 0.0F);
                float f3 = f2 - f;

                if (f3 > 0.0F && f3 < 3.4028235E37F) {
                    if (this instanceof EntityPlayer) {
                        ((EntityPlayer) this).a(StatisticList.DAMAGE_RESISTED, Math.round(f3 * 10.0F));
                    } else if (damagesource.getEntity() instanceof EntityPlayer) {
                        ((EntityPlayer) damagesource.getEntity()).a(StatisticList.DAMAGE_DEALT_RESISTED, Math.round(f3 * 10.0F));
                    }
                }
            }

            if (f <= 0.0F) {
                return 0.0F;
            } else {
                i = EnchantmentManager.a(this.getArmorItems(), damagesource);
                if (i > 0) {
                    f = CombatMath.a(f, (float) i);
                }

                return f;
            }
        }
    }

    // CraftBukkit start
    protected boolean damageEntity0(final DamageSource damagesource, float f) { // void -> boolean, add final
       if (!this.isInvulnerable(damagesource)) {
            final boolean human = this instanceof EntityHuman;
            float originalDamage = f;
            Function<Double, Double> hardHat = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && !EntityLiving.this.getEquipment(EnumItemSlot.HEAD).isEmpty()) {
                        return -(f - (f * 0.75F));

                    }
                    return -0.0;
                }
            };
            float hardHatModifier = hardHat.apply((double) f).floatValue();
            f += hardHatModifier;

            Function<Double, Double> blocking = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -((EntityLiving.this.applyBlockingModifier(damagesource)) ? f : 0.0);
                }
            };
            float blockingModifier = blocking.apply((double) f).floatValue();
            f += blockingModifier;

            Function<Double, Double> armor = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLiving.this.applyArmorModifier(damagesource, f.floatValue()));
                }
            };
            float armorModifier = armor.apply((double) f).floatValue();
            f += armorModifier;

            Function<Double, Double> resistance = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if (!damagesource.isStarvation() && EntityLiving.this.hasEffect(MobEffects.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                        int i = (EntityLiving.this.getEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                        int j = 25 - i;
                        float f1 = f.floatValue() * (float) j;
                        return -(f - (f1 / 25.0F));
                    }
                    return -0.0;
                }
            };
            float resistanceModifier = resistance.apply((double) f).floatValue();
            f += resistanceModifier;

            Function<Double, Double> magic = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLiving.this.applyMagicModifier(damagesource, f.floatValue()));
                }
            };
            float magicModifier = magic.apply((double) f).floatValue();
            f += magicModifier;

            Function<Double, Double> absorption = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(Math.max(f - Math.max(f - EntityLiving.this.getAbsorptionHearts(), 0.0F), 0.0F));
                }
            };
            float absorptionModifier = absorption.apply((double) f).floatValue();

            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, originalDamage, hardHatModifier, blockingModifier, armorModifier, resistanceModifier, magicModifier, absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
            if (damagesource.getEntity() instanceof EntityHuman) {
                ((EntityHuman) damagesource.getEntity()).resetAttackCooldown(); // Moved from EntityHuman in order to make the cooldown reset get called after the damage event is fired
            }
            if (event.isCancelled()) {
                return false;
            }

            f = (float) event.getFinalDamage();

            // Resistance
            if (event.getDamage(DamageModifier.RESISTANCE) < 0) {
                float f3 = (float) -event.getDamage(DamageModifier.RESISTANCE);
                if (f3 > 0.0F && f3 < 3.4028235E37F) {
                    if (this instanceof EntityPlayer) {
                        ((EntityPlayer) this).a(StatisticList.DAMAGE_RESISTED, Math.round(f3 * 10.0F));
                    } else if (damagesource.getEntity() instanceof EntityPlayer) {
                        ((EntityPlayer) damagesource.getEntity()).a(StatisticList.DAMAGE_DEALT_RESISTED, Math.round(f3 * 10.0F));
                    }
                }
            }

            // Apply damage to helmet
            if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(EnumItemSlot.HEAD) != null) {
                this.getEquipment(EnumItemSlot.HEAD).damage((int) (event.getDamage() * 4.0F + this.random.nextFloat() * event.getDamage() * 2.0F), this, (entityliving) -> {
                    entityliving.broadcastItemBreak(EnumItemSlot.HEAD);
                });
            }

            // Apply damage to armor
            if (!damagesource.ignoresArmor()) {
                float armorDamage = (float) (event.getDamage() + event.getDamage(DamageModifier.BLOCKING) + event.getDamage(DamageModifier.HARD_HAT));
                this.damageArmor(damagesource, armorDamage);
            }

            // Apply blocking code // PAIL: steal from above
            if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                this.world.broadcastEntityEffect(this, (byte) 29); // SPIGOT-4635 - shield damage sound
                this.damageShield((float) -event.getDamage(DamageModifier.BLOCKING));
                Entity entity = damagesource.j();

                if (entity instanceof EntityLiving) {
                    this.shieldBlock((EntityLiving) entity);
                }
            }

            absorptionModifier = (float) -event.getDamage(DamageModifier.ABSORPTION);
            this.setAbsorptionHearts(Math.max(this.getAbsorptionHearts() - absorptionModifier, 0.0F));
            float f2 = absorptionModifier;

            if (f2 > 0.0F && f2 < 3.4028235E37F && this instanceof EntityHuman) {
                ((EntityHuman) this).a(StatisticList.DAMAGE_ABSORBED, Math.round(f2 * 10.0F));
            }
            if (f2 > 0.0F && f2 < 3.4028235E37F && damagesource.getEntity() instanceof EntityPlayer) {
                ((EntityPlayer) damagesource.getEntity()).a(StatisticList.DAMAGE_DEALT_ABSORBED, Math.round(f2 * 10.0F));
            }

            if (f > 0 || !human) {
                if (human) {
                    // PAIL: Be sure to drag all this code from the EntityHuman subclass each update.
                    ((EntityHuman) this).applyExhaustion(damagesource.getExhaustionCost());
                    if (f < 3.4028235E37F) {
                        ((EntityHuman) this).a(StatisticList.DAMAGE_TAKEN, Math.round(f * 10.0F));
                    }
                }
                // CraftBukkit end
                float f3 = this.getHealth();

                this.setHealth(f3 - f);
                this.getCombatTracker().trackDamage(damagesource, f3, f);
                // CraftBukkit start
                if (!human) {
                    this.setAbsorptionHearts(this.getAbsorptionHearts() - f);
                }

                return true;
            } else {
                // Duplicate triggers if blocking
                if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                    if (this instanceof EntityPlayer) {
                        CriterionTriggers.h.a((EntityPlayer) this, damagesource, f, originalDamage, true);
                        f2 = (float) -event.getDamage(DamageModifier.BLOCKING);
                        if (f2 > 0.0F && f2 < 3.4028235E37F) {
                            ((EntityPlayer) this).a(StatisticList.DAMAGE_BLOCKED_BY_SHIELD, Math.round(originalDamage * 10.0F));
                        }
                    }

                    if (damagesource.getEntity() instanceof EntityPlayer) {
                        CriterionTriggers.g.a((EntityPlayer) damagesource.getEntity(), this, damagesource, f, originalDamage, true);
                    }

                    return false;
                } else {
                    return originalDamage > 0;
                }
                // CraftBukkit end
            }
        }
        return false; // CraftBukkit
    }

    public CombatTracker getCombatTracker() {
        return this.combatTracker;
    }

    @Nullable
    public EntityLiving getKillingEntity() {
        return (EntityLiving) (this.combatTracker.c() != null ? this.combatTracker.c() : (this.killer != null ? this.killer : (this.lastDamager != null ? this.lastDamager : null)));
    }

    public final float getMaxHealth() {
        return (float) this.b(GenericAttributes.MAX_HEALTH);
    }

    public final int getArrowCount() {
        return (Integer) this.datawatcher.get(EntityLiving.ARROWS_IN_BODY);
    }

    public final void setArrowCount(int i) {
        // CraftBukkit start
        setArrowCount(i, false);
    }

    public final void setArrowCount(int i, boolean flag) {
        ArrowBodyCountChangeEvent event = CraftEventFactory.callArrowBodyCountChangeEvent(this, getArrowCount(), i, flag);
        if (event.isCancelled()) {
            return;
        }
        this.datawatcher.set(EntityLiving.ARROWS_IN_BODY, event.getNewAmount());
    }
    // CraftBukkit end

    public final int dy() {
        return (Integer) this.datawatcher.get(EntityLiving.bi);
    }

    public final void q(int i) {
        this.datawatcher.set(EntityLiving.bi, i);
    }

    private int m() {
        return MobEffectUtil.a(this) ? 6 - (1 + MobEffectUtil.b(this)) : (this.hasEffect(MobEffects.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffects.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    public void swingHand(EnumHand enumhand) {
        this.swingHand(enumhand, false);
    }

    public void swingHand(EnumHand enumhand, boolean flag) {
        if (!this.ai || this.ak >= this.m() / 2 || this.ak < 0) {
            this.ak = -1;
            this.ai = true;
            this.aj = enumhand;
            if (this.world instanceof WorldServer) {
                PacketPlayOutAnimation packetplayoutanimation = new PacketPlayOutAnimation(this, enumhand == EnumHand.MAIN_HAND ? 0 : 3);
                ChunkProviderServer chunkproviderserver = ((WorldServer) this.world).getChunkProvider();

                if (flag) {
                    chunkproviderserver.broadcastIncludingSelf(this, packetplayoutanimation);
                } else {
                    chunkproviderserver.broadcast(this, packetplayoutanimation);
                }
            }
        }

    }

    @Override
    protected void am() {
        this.damageEntity(DamageSource.OUT_OF_WORLD, 4.0F);
    }

    protected void dz() {
        int i = this.m();

        if (this.ai) {
            ++this.ak;
            if (this.ak >= i) {
                this.ak = 0;
                this.ai = false;
            }
        } else {
            this.ak = 0;
        }

        this.as = (float) this.ak / (float) i;
    }

    @Nullable
    public AttributeModifiable getAttributeInstance(AttributeBase attributebase) {
        return this.getAttributeMap().a(attributebase);
    }

    public double b(AttributeBase attributebase) {
        return this.getAttributeMap().c(attributebase);
    }

    public double c(AttributeBase attributebase) {
        return this.getAttributeMap().d(attributebase);
    }

    public AttributeMapBase getAttributeMap() {
        return this.attributeMap;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEFINED;
    }

    public ItemStack getItemInMainHand() {
        return this.getEquipment(EnumItemSlot.MAINHAND);
    }

    public ItemStack getItemInOffHand() {
        return this.getEquipment(EnumItemSlot.OFFHAND);
    }

    public boolean a(Item item) {
        return this.a((item1) -> {
            return item1 == item;
        });
    }

    public boolean a(Predicate<Item> predicate) {
        return predicate.test(this.getItemInMainHand().getItem()) || predicate.test(this.getItemInOffHand().getItem());
    }

    public ItemStack b(EnumHand enumhand) {
        if (enumhand == EnumHand.MAIN_HAND) {
            return this.getEquipment(EnumItemSlot.MAINHAND);
        } else if (enumhand == EnumHand.OFF_HAND) {
            return this.getEquipment(EnumItemSlot.OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + enumhand);
        }
    }

    public void a(EnumHand enumhand, ItemStack itemstack) {
        if (enumhand == EnumHand.MAIN_HAND) {
            this.setSlot(EnumItemSlot.MAINHAND, itemstack);
        } else {
            if (enumhand != EnumHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + enumhand);
            }

            this.setSlot(EnumItemSlot.OFFHAND, itemstack);
        }

    }

    public boolean a(EnumItemSlot enumitemslot) {
        return !this.getEquipment(enumitemslot).isEmpty();
    }

    @Override
    public abstract Iterable<ItemStack> getArmorItems();

    public abstract ItemStack getEquipment(EnumItemSlot enumitemslot);

    @Override
    public abstract void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack);

    public float dE() {
        Iterable<ItemStack> iterable = this.getArmorItems();
        int i = 0;
        int j = 0;

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); ++i) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (!itemstack.isEmpty()) {
                ++j;
            }
        }

        return i > 0 ? (float) j / (float) i : 0.0F;
    }

    @Override
    public void setSprinting(boolean flag) {
        super.setSprinting(flag);
        AttributeModifiable attributemodifiable = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

        if (attributemodifiable.a(EntityLiving.b) != null) {
            attributemodifiable.removeModifier(EntityLiving.d);
        }

        if (flag) {
            attributemodifiable.b(EntityLiving.d);
        }

    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    protected float dG() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean isFrozen() {
        return this.dk();
    }

    @Override
    public void collide(Entity entity) {
        if (!this.isSleeping()) {
            super.collide(entity);
        }

    }

    private void a(Entity entity) {
        Vec3D vec3d;

        if (!entity.dead && !this.world.getType(entity.getChunkCoordinates()).getBlock().a((Tag) TagsBlock.PORTALS)) {
            vec3d = entity.b(this);
        } else {
            vec3d = new Vec3D(entity.locX(), entity.locY() + (double) entity.getHeight(), entity.locZ());
        }

        this.enderTeleportTo(vec3d.x, vec3d.y, vec3d.z);
    }

    protected float dI() {
        return 0.42F * this.getBlockJumpFactor();
    }

    protected void jump() {
        float f = this.dI();

        if (this.hasEffect(MobEffects.JUMP)) {
            f += 0.1F * (float) (this.getEffect(MobEffects.JUMP).getAmplifier() + 1);
        }

        Vec3D vec3d = this.getMot();

        this.setMot(vec3d.x, (double) f, vec3d.z);
        if (this.isSprinting()) {
            float f1 = this.yaw * 0.017453292F;

            this.setMot(this.getMot().add((double) (-MathHelper.sin(f1) * 0.2F), 0.0D, (double) (MathHelper.cos(f1) * 0.2F)));
        }

        this.impulse = true;
    }

    protected void c(Tag<FluidType> tag) {
        this.setMot(this.getMot().add(0.0D, 0.03999999910593033D, 0.0D));
    }

    protected float dL() {
        return 0.8F;
    }

    public boolean a(FluidType fluidtype) {
        return false;
    }

    public void g(Vec3D vec3d) {
        if (this.doAITick() || this.cr()) {
            double d0 = 0.08D;
            boolean flag = this.getMot().y <= 0.0D;

            if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
                d0 = 0.01D;
                this.fallDistance = 0.0F;
            }

            Fluid fluid = this.world.getFluid(this.getChunkCoordinates());
            double d1;
            float f;

            if (this.isInWater() && this.cS() && !this.a(fluid.getType())) {
                d1 = this.locY();
                f = this.isSprinting() ? 0.9F : this.dL();
                float f1 = 0.02F;
                float f2 = (float) EnchantmentManager.e(this);

                if (f2 > 3.0F) {
                    f2 = 3.0F;
                }

                if (!this.onGround) {
                    f2 *= 0.5F;
                }

                if (f2 > 0.0F) {
                    f += (0.54600006F - f) * f2 / 3.0F;
                    f1 += (this.dM() - f1) * f2 / 3.0F;
                }

                if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                    f = 0.96F;
                }

                this.a(f1, vec3d);
                this.move(EnumMoveType.SELF, this.getMot());
                Vec3D vec3d1 = this.getMot();

                if (this.positionChanged && this.isClimbing()) {
                    vec3d1 = new Vec3D(vec3d1.x, 0.2D, vec3d1.z);
                }

                this.setMot(vec3d1.d((double) f, 0.800000011920929D, (double) f));
                Vec3D vec3d2 = this.a(d0, flag, this.getMot());

                this.setMot(vec3d2);
                if (this.positionChanged && this.e(vec3d2.x, vec3d2.y + 0.6000000238418579D - this.locY() + d1, vec3d2.z)) {
                    this.setMot(vec3d2.x, 0.30000001192092896D, vec3d2.z);
                }
            } else if (this.aP() && this.cS() && !this.a(fluid.getType())) {
                d1 = this.locY();
                this.a(0.02F, vec3d);
                this.move(EnumMoveType.SELF, this.getMot());
                Vec3D vec3d3;

                if (this.b((Tag) TagsFluid.LAVA) <= this.cw()) {
                    this.setMot(this.getMot().d(0.5D, 0.800000011920929D, 0.5D));
                    vec3d3 = this.a(d0, flag, this.getMot());
                    this.setMot(vec3d3);
                } else {
                    this.setMot(this.getMot().a(0.5D));
                }

                if (!this.isNoGravity()) {
                    this.setMot(this.getMot().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                vec3d3 = this.getMot();
                if (this.positionChanged && this.e(vec3d3.x, vec3d3.y + 0.6000000238418579D - this.locY() + d1, vec3d3.z)) {
                    this.setMot(vec3d3.x, 0.30000001192092896D, vec3d3.z);
                }
            } else if (this.isGliding()) {
                Vec3D vec3d4 = this.getMot();

                if (vec3d4.y > -0.5D) {
                    this.fallDistance = 1.0F;
                }

                Vec3D vec3d5 = this.getLookDirection();

                f = this.pitch * 0.017453292F;
                double d2 = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                double d3 = Math.sqrt(c(vec3d4));
                double d4 = vec3d5.f();
                float f3 = MathHelper.cos(f);

                f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d4 / 0.4D));
                vec3d4 = this.getMot().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
                double d5;

                if (vec3d4.y < 0.0D && d2 > 0.0D) {
                    d5 = vec3d4.y * -0.1D * (double) f3;
                    vec3d4 = vec3d4.add(vec3d5.x * d5 / d2, d5, vec3d5.z * d5 / d2);
                }

                if (f < 0.0F && d2 > 0.0D) {
                    d5 = d3 * (double) (-MathHelper.sin(f)) * 0.04D;
                    vec3d4 = vec3d4.add(-vec3d5.x * d5 / d2, d5 * 3.2D, -vec3d5.z * d5 / d2);
                }

                if (d2 > 0.0D) {
                    vec3d4 = vec3d4.add((vec3d5.x / d2 * d3 - vec3d4.x) * 0.1D, 0.0D, (vec3d5.z / d2 * d3 - vec3d4.z) * 0.1D);
                }

                this.setMot(vec3d4.d(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));
                this.move(EnumMoveType.SELF, this.getMot());
                if (this.positionChanged && !this.world.isClientSide) {
                    d5 = Math.sqrt(c(this.getMot()));
                    double d6 = d3 - d5;
                    float f4 = (float) (d6 * 10.0D - 3.0D);

                    if (f4 > 0.0F) {
                        this.playSound(this.getSoundFall((int) f4), 1.0F, 1.0F);
                        this.damageEntity(DamageSource.FLY_INTO_WALL, f4);
                    }
                }

                if (this.onGround && !this.world.isClientSide) {
                    if (getFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) // CraftBukkit
                    this.setFlag(7, false);
                }
            } else {
                BlockPosition blockposition = this.ar();
                float f5 = this.world.getType(blockposition).getBlock().getFrictionFactor();

                f = this.onGround ? f5 * 0.91F : 0.91F;
                Vec3D vec3d6 = this.a(vec3d, f5);
                double d7 = vec3d6.y;

                if (this.hasEffect(MobEffects.LEVITATION)) {
                    d7 += (0.05D * (double) (this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2D;
                    this.fallDistance = 0.0F;
                } else if (this.world.isClientSide && !this.world.isLoaded(blockposition)) {
                    if (this.locY() > 0.0D) {
                        d7 = -0.1D;
                    } else {
                        d7 = 0.0D;
                    }
                } else if (!this.isNoGravity()) {
                    d7 -= d0;
                }

                this.setMot(vec3d6.x * (double) f, d7 * 0.9800000190734863D, vec3d6.z * (double) f);
            }
        }

        this.a(this, this instanceof EntityBird);
    }

    public void a(EntityLiving entityliving, boolean flag) {
        entityliving.au = entityliving.av;
        double d0 = entityliving.locX() - entityliving.lastX;
        double d1 = flag ? entityliving.locY() - entityliving.lastY : 0.0D;
        double d2 = entityliving.locZ() - entityliving.lastZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 4.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        entityliving.av += (f - entityliving.av) * 0.4F;
        entityliving.aw += entityliving.av;
    }

    public Vec3D a(Vec3D vec3d, float f) {
        this.a(this.t(f), vec3d);
        this.setMot(this.i(this.getMot()));
        this.move(EnumMoveType.SELF, this.getMot());
        Vec3D vec3d1 = this.getMot();

        if ((this.positionChanged || this.jumping) && this.isClimbing()) {
            vec3d1 = new Vec3D(vec3d1.x, 0.2D, vec3d1.z);
        }

        return vec3d1;
    }

    public Vec3D a(double d0, boolean flag, Vec3D vec3d) {
        if (!this.isNoGravity() && !this.isSprinting()) {
            double d1;

            if (flag && Math.abs(vec3d.y - 0.005D) >= 0.003D && Math.abs(vec3d.y - d0 / 16.0D) < 0.003D) {
                d1 = -0.003D;
            } else {
                d1 = vec3d.y - d0 / 16.0D;
            }

            return new Vec3D(vec3d.x, d1, vec3d.z);
        } else {
            return vec3d;
        }
    }

    private Vec3D i(Vec3D vec3d) {
        if (this.isClimbing()) {
            this.fallDistance = 0.0F;
            float f = 0.15F;
            double d0 = MathHelper.a(vec3d.x, -0.15000000596046448D, 0.15000000596046448D);
            double d1 = MathHelper.a(vec3d.z, -0.15000000596046448D, 0.15000000596046448D);
            double d2 = Math.max(vec3d.y, -0.15000000596046448D);

            if (d2 < 0.0D && !this.dr().a(Blocks.SCAFFOLDING) && this.ed() && this instanceof EntityHuman) {
                d2 = 0.0D;
            }

            vec3d = new Vec3D(d0, d2, d1);
        }

        return vec3d;
    }

    private float t(float f) {
        return this.onGround ? this.dM() * (0.21600002F / (f * f * f)) : this.aE;
    }

    public float dM() {
        return this.bu;
    }

    public void q(float f) {
        this.bu = f;
    }

    public boolean attackEntity(Entity entity) {
        this.z(entity);
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.t();
        this.v();
        if (!this.world.isClientSide) {
            int i = this.getArrowCount();

            if (i > 0) {
                if (this.arrowCooldown <= 0) {
                    this.arrowCooldown = 20 * (30 - i);
                }

                --this.arrowCooldown;
                if (this.arrowCooldown <= 0) {
                    this.setArrowCount(i - 1);
                }
            }

            int j = this.dy();

            if (j > 0) {
                if (this.am <= 0) {
                    this.am = 20 * (30 - j);
                }

                --this.am;
                if (this.am <= 0) {
                    this.q(j - 1);
                }
            }

            this.p();
            if (this.ticksLived % 20 == 0) {
                this.getCombatTracker().g();
            }

            if (!this.glowing) {
                boolean flag = this.hasEffect(MobEffects.GLOWING);

                if (this.getFlag(6) != flag) {
                    this.setFlag(6, flag);
                }
            }

            if (this.isSleeping() && !this.x()) {
                this.entityWakeup();
            }
        }

        this.movementTick();
        double d0 = this.locX() - this.lastX;
        double d1 = this.locZ() - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.aA;
        float f2 = 0.0F;

        this.aJ = this.aK;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            float f4 = (float) MathHelper.d(d1, d0) * 57.295776F - 90.0F;
            float f5 = MathHelper.e(MathHelper.g(this.yaw) - f4);

            if (95.0F < f5 && f5 < 265.0F) {
                f1 = f4 - 180.0F;
            } else {
                f1 = f4;
            }
        }

        if (this.as > 0.0F) {
            f1 = this.yaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.aK += (f3 - this.aK) * 0.3F;
        this.world.getMethodProfiler().enter("headTurn");
        f2 = this.f(f1, f2);
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("rangeChecks");

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.aA - this.aB < -180.0F) {
            this.aB -= 360.0F;
        }

        while (this.aA - this.aB >= 180.0F) {
            this.aB += 360.0F;
        }

        while (this.pitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.aC - this.aD < -180.0F) {
            this.aD -= 360.0F;
        }

        while (this.aC - this.aD >= 180.0F) {
            this.aD += 360.0F;
        }

        this.world.getMethodProfiler().exit();
        this.aL += f2;
        if (this.isGliding()) {
            ++this.be;
        } else {
            this.be = 0;
        }

        if (this.isSleeping()) {
            this.pitch = 0.0F;
        }

    }

    private void p() {
        Map<EnumItemSlot, ItemStack> map = this.q();

        if (map != null) {
            this.a(map);
            if (!map.isEmpty()) {
                this.b(map);
            }
        }

    }

    @Nullable
    private Map<EnumItemSlot, ItemStack> q() {
        Map<EnumItemSlot, ItemStack> map = null;
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack;

            switch (enumitemslot.a()) {
                case HAND:
                    itemstack = this.e(enumitemslot);
                    break;
                case ARMOR:
                    itemstack = this.d(enumitemslot);
                    break;
                default:
                    continue;
            }

            ItemStack itemstack1 = this.getEquipment(enumitemslot);

            if (!ItemStack.matches(itemstack1, itemstack)) {
                if (map == null) {
                    map = Maps.newEnumMap(EnumItemSlot.class);
                }

                map.put(enumitemslot, itemstack1);
                if (!itemstack.isEmpty()) {
                    this.getAttributeMap().a(itemstack.a(enumitemslot));
                }

                if (!itemstack1.isEmpty()) {
                    this.getAttributeMap().b(itemstack1.a(enumitemslot));
                }
            }
        }

        return map;
    }

    private void a(Map<EnumItemSlot, ItemStack> map) {
        ItemStack itemstack = (ItemStack) map.get(EnumItemSlot.MAINHAND);
        ItemStack itemstack1 = (ItemStack) map.get(EnumItemSlot.OFFHAND);

        if (itemstack != null && itemstack1 != null && ItemStack.matches(itemstack, this.e(EnumItemSlot.OFFHAND)) && ItemStack.matches(itemstack1, this.e(EnumItemSlot.MAINHAND))) {
            ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutEntityStatus(this, (byte) 55));
            map.remove(EnumItemSlot.MAINHAND);
            map.remove(EnumItemSlot.OFFHAND);
            this.c(EnumItemSlot.MAINHAND, itemstack.cloneItemStack());
            this.c(EnumItemSlot.OFFHAND, itemstack1.cloneItemStack());
        }

    }

    private void b(Map<EnumItemSlot, ItemStack> map) {
        List<Pair<EnumItemSlot, ItemStack>> list = Lists.newArrayListWithCapacity(map.size());

        map.forEach((enumitemslot, itemstack) -> {
            ItemStack itemstack1 = itemstack.cloneItemStack();

            list.add(Pair.of(enumitemslot, itemstack1));
            switch (enumitemslot.a()) {
                case HAND:
                    this.c(enumitemslot, itemstack1);
                    break;
                case ARMOR:
                    this.b(enumitemslot, itemstack1);
            }

        });
        ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutEntityEquipment(this.getId(), list));
    }

    private ItemStack d(EnumItemSlot enumitemslot) {
        return (ItemStack) this.bo.get(enumitemslot.b());
    }

    private void b(EnumItemSlot enumitemslot, ItemStack itemstack) {
        this.bo.set(enumitemslot.b(), itemstack);
    }

    private ItemStack e(EnumItemSlot enumitemslot) {
        return (ItemStack) this.bn.get(enumitemslot.b());
    }

    private void c(EnumItemSlot enumitemslot, ItemStack itemstack) {
        this.bn.set(enumitemslot.b(), itemstack);
    }

    protected float f(float f, float f1) {
        float f2 = MathHelper.g(f - this.aA);

        this.aA += f2 * 0.3F;
        float f3 = MathHelper.g(this.yaw - this.aA);
        boolean flag = f3 < -90.0F || f3 >= 90.0F;

        if (f3 < -75.0F) {
            f3 = -75.0F;
        }

        if (f3 >= 75.0F) {
            f3 = 75.0F;
        }

        this.aA = this.yaw - f3;
        if (f3 * f3 > 2500.0F) {
            this.aA += f3 * 0.2F;
        }

        if (flag) {
            f1 *= -1.0F;
        }

        return f1;
    }

    public void movementTick() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }

        if (this.cr()) {
            this.aU = 0;
            this.c(this.locX(), this.locY(), this.locZ());
        }

        if (this.aU > 0) {
            double d0 = this.locX() + (this.aV - this.locX()) / (double) this.aU;
            double d1 = this.locY() + (this.aW - this.locY()) / (double) this.aU;
            double d2 = this.locZ() + (this.aX - this.locZ()) / (double) this.aU;
            double d3 = MathHelper.g(this.aY - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aU);
            this.pitch = (float) ((double) this.pitch + (this.aZ - (double) this.pitch) / (double) this.aU);
            --this.aU;
            this.setPosition(d0, d1, d2);
            this.setYawPitch(this.yaw, this.pitch);
        } else if (!this.doAITick()) {
            this.setMot(this.getMot().a(0.98D));
        }

        if (this.bb > 0) {
            this.aC = (float) ((double) this.aC + MathHelper.g(this.ba - (double) this.aC) / (double) this.bb);
            --this.bb;
        }

        Vec3D vec3d = this.getMot();
        double d4 = vec3d.x;
        double d5 = vec3d.y;
        double d6 = vec3d.z;

        if (Math.abs(vec3d.x) < 0.003D) {
            d4 = 0.0D;
        }

        if (Math.abs(vec3d.y) < 0.003D) {
            d5 = 0.0D;
        }

        if (Math.abs(vec3d.z) < 0.003D) {
            d6 = 0.0D;
        }

        this.setMot(d4, d5, d6);
        this.world.getMethodProfiler().enter("ai");
        if (this.isFrozen()) {
            this.jumping = false;
            this.aR = 0.0F;
            this.aT = 0.0F;
        } else if (this.doAITick()) {
            this.world.getMethodProfiler().enter("newAi");
            this.doTick();
            this.world.getMethodProfiler().exit();
        }

        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("jump");
        if (this.jumping && this.cS()) {
            double d7;

            if (this.aP()) {
                d7 = this.b((Tag) TagsFluid.LAVA);
            } else {
                d7 = this.b((Tag) TagsFluid.WATER);
            }

            boolean flag = this.isInWater() && d7 > 0.0D;
            double d8 = this.cw();

            if (flag && (!this.onGround || d7 > d8)) {
                this.c((Tag) TagsFluid.WATER);
            } else if (this.aP() && (!this.onGround || d7 > d8)) {
                this.c((Tag) TagsFluid.LAVA);
            } else if ((this.onGround || flag && d7 <= d8) && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        } else {
            this.jumpTicks = 0;
        }

        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("travel");
        this.aR *= 0.98F;
        this.aT *= 0.98F;
        this.r();
        AxisAlignedBB axisalignedbb = this.getBoundingBox();

        this.g(new Vec3D((double) this.aR, (double) this.aS, (double) this.aT));
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("push");
        if (this.bf > 0) {
            --this.bf;
            this.a(axisalignedbb, this.getBoundingBox());
        }

        this.collideNearby();
        this.world.getMethodProfiler().exit();
        if (!this.world.isClientSide && this.dN() && this.aF()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
        }

    }

    public boolean dN() {
        return false;
    }

    private void r() {
        boolean flag = this.getFlag(7);

        if (flag && !this.onGround && !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.getEquipment(EnumItemSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.d(itemstack)) {
                flag = true;
                if (!this.world.isClientSide && (this.be + 1) % 20 == 0) {
                    itemstack.damage(1, this, (entityliving) -> {
                        entityliving.broadcastItemBreak(EnumItemSlot.CHEST);
                    });
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        if (!this.world.isClientSide) {
            if (flag != this.getFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, flag).isCancelled()) // CraftBukkit
            this.setFlag(7, flag);
        }

    }

    protected void doTick() {}

    protected void collideNearby() {
        List<Entity> list = this.world.getEntities(this, this.getBoundingBox(), IEntitySelector.a(this));

        if (!list.isEmpty()) {
            int i = this.world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
            int j;

            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                j = 0;

                for (int k = 0; k < list.size(); ++k) {
                    if (!((Entity) list.get(k)).isPassenger()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.damageEntity(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (j = 0; j < list.size(); ++j) {
                Entity entity = (Entity) list.get(j);

                this.C(entity);
            }
        }

    }

    protected void a(AxisAlignedBB axisalignedbb, AxisAlignedBB axisalignedbb1) {
        AxisAlignedBB axisalignedbb2 = axisalignedbb.b(axisalignedbb1);
        List<Entity> list = this.world.getEntities(this, axisalignedbb2);

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity instanceof EntityLiving) {
                    this.g((EntityLiving) entity);
                    this.bf = 0;
                    this.setMot(this.getMot().a(-0.2D));
                    break;
                }
            }
        } else if (this.positionChanged) {
            this.bf = 0;
        }

        if (!this.world.isClientSide && this.bf <= 0) {
            this.c(4, false);
        }

    }

    protected void C(Entity entity) {
        entity.collide(this);
    }

    protected void g(EntityLiving entityliving) {}

    public void r(int i) {
        this.bf = i;
        if (!this.world.isClientSide) {
            this.c(4, true);
        }

    }

    public boolean isRiptiding() {
        return ((Byte) this.datawatcher.get(EntityLiving.ag) & 4) != 0;
    }

    @Override
    public void stopRiding() {
        Entity entity = this.getVehicle();

        super.stopRiding();
        if (entity != null && entity != this.getVehicle() && !this.world.isClientSide) {
            this.a(entity);
        }

    }

    @Override
    public void passengerTick() {
        super.passengerTick();
        this.aJ = this.aK;
        this.aK = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void setJumping(boolean flag) {
        this.jumping = flag;
    }

    public void a(EntityItem entityitem) {
        EntityHuman entityhuman = entityitem.getThrower() != null ? this.world.b(entityitem.getThrower()) : null;

        if (entityhuman instanceof EntityPlayer) {
            CriterionTriggers.O.a((EntityPlayer) entityhuman, entityitem.getItemStack(), this);
        }

    }

    public void receive(Entity entity, int i) {
        if (!entity.dead && !this.world.isClientSide && (entity instanceof EntityItem || entity instanceof EntityArrow || entity instanceof EntityExperienceOrb)) {
            ((WorldServer) this.world).getChunkProvider().broadcast(entity, new PacketPlayOutCollect(entity.getId(), this.getId(), i));
        }

    }

    public boolean hasLineOfSight(Entity entity) {
        if (this.world != entity.world) return false; // CraftBukkit - SPIGOT-5675, SPIGOT-5798, MC-149563
        Vec3D vec3d = new Vec3D(this.locX(), this.getHeadY(), this.locZ());
        Vec3D vec3d1 = new Vec3D(entity.locX(), entity.getHeadY(), entity.locZ());

        return this.world.rayTrace(new RayTrace(vec3d, vec3d1, RayTrace.BlockCollisionOption.COLLIDER, RayTrace.FluidCollisionOption.NONE, this)).getType() == MovingObjectPosition.EnumMovingObjectType.MISS;
    }

    @Override
    public float h(float f) {
        return f == 1.0F ? this.aC : MathHelper.g(f, this.aD, this.aC);
    }

    public boolean doAITick() {
        return !this.world.isClientSide;
    }

    @Override
    public boolean isInteractable() {
        return !this.dead && this.collides; // CraftBukkit
    }

    @Override
    public boolean isCollidable() {
        return this.isAlive() && !this.isSpectator() && !this.isClimbing() && this.collides; // CraftBukkit
    }

    // CraftBukkit start - collidable API
    @Override
    public boolean canCollideWith(Entity entity) {
        return isCollidable() && this.collides != this.collidableExemptions.contains(entity.getUniqueID());
    }
    // CraftBukkit end

    @Override
    protected void velocityChanged() {
        this.velocityChanged = this.random.nextDouble() >= this.b(GenericAttributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    public float getHeadRotation() {
        return this.aC;
    }

    @Override
    public void setHeadRotation(float f) {
        this.aC = f;
    }

    @Override
    public void n(float f) {
        this.aA = f;
    }

    @Override
    protected Vec3D a(EnumDirection.EnumAxis enumdirection_enumaxis, BlockUtil.Rectangle blockutil_rectangle) {
        return h(super.a(enumdirection_enumaxis, blockutil_rectangle));
    }

    public static Vec3D h(Vec3D vec3d) {
        return new Vec3D(vec3d.x, vec3d.y, 0.0D);
    }

    public float getAbsorptionHearts() {
        return this.bw;
    }

    public void setAbsorptionHearts(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.bw = f;
    }

    public void enterCombat() {}

    public void exitCombat() {}

    protected void dT() {
        this.updateEffects = true;
    }

    public abstract EnumMainHand getMainHand();

    public boolean isHandRaised() {
        return ((Byte) this.datawatcher.get(EntityLiving.ag) & 1) > 0;
    }

    public EnumHand getRaisedHand() {
        return ((Byte) this.datawatcher.get(EntityLiving.ag) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    private void t() {
        if (this.isHandRaised()) {
            if (ItemStack.d(this.b(this.getRaisedHand()), this.activeItem)) {
                this.activeItem = this.b(this.getRaisedHand());
                this.activeItem.b(this.world, this, this.dY());
                if (this.u()) {
                    this.b(this.activeItem, 5);
                }

                if (--this.bd == 0 && !this.world.isClientSide && !this.activeItem.m()) {
                    this.s();
                }
            } else {
                this.clearActiveItem();
            }
        }

    }

    private boolean u() {
        int i = this.dY();
        FoodInfo foodinfo = this.activeItem.getItem().getFoodInfo();
        boolean flag = foodinfo != null && foodinfo.e();

        flag |= i <= this.activeItem.k() - 7;
        return flag && i % 4 == 0;
    }

    private void v() {
        this.bC = this.bB;
        if (this.bB()) {
            this.bB = Math.min(1.0F, this.bB + 0.09F);
        } else {
            this.bB = Math.max(0.0F, this.bB - 0.09F);
        }

    }

    protected void c(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityLiving.ag);
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.datawatcher.set(EntityLiving.ag, (byte) j);
    }

    public void c(EnumHand enumhand) {
        ItemStack itemstack = this.b(enumhand);

        if (!itemstack.isEmpty() && !this.isHandRaised()) {
            this.activeItem = itemstack;
            this.bd = itemstack.k();
            if (!this.world.isClientSide) {
                this.c(1, true);
                this.c(2, enumhand == EnumHand.OFF_HAND);
            }

        }
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
        if (EntityLiving.bj.equals(datawatcherobject)) {
            if (this.world.isClientSide) {
                this.getBedPosition().ifPresent(this::a);
            }
        } else if (EntityLiving.ag.equals(datawatcherobject) && this.world.isClientSide) {
            if (this.isHandRaised() && this.activeItem.isEmpty()) {
                this.activeItem = this.b(this.getRaisedHand());
                if (!this.activeItem.isEmpty()) {
                    this.bd = this.activeItem.k();
                }
            } else if (!this.isHandRaised() && !this.activeItem.isEmpty()) {
                this.activeItem = ItemStack.b;
                this.bd = 0;
            }
        }

    }

    @Override
    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Vec3D vec3d) {
        super.a(argumentanchor_anchor, vec3d);
        this.aD = this.aC;
        this.aA = this.aC;
        this.aB = this.aA;
    }

    protected void b(ItemStack itemstack, int i) {
        if (!itemstack.isEmpty() && this.isHandRaised()) {
            if (itemstack.l() == EnumAnimation.DRINK) {
                this.playSound(this.c(itemstack), 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (itemstack.l() == EnumAnimation.EAT) {
                this.a(itemstack, i);
                this.playSound(this.d(itemstack), 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

        }
    }

    private void a(ItemStack itemstack, int i) {
        for (int j = 0; j < i; ++j) {
            Vec3D vec3d = new Vec3D(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d = vec3d.a(-this.pitch * 0.017453292F);
            vec3d = vec3d.b(-this.yaw * 0.017453292F);
            double d0 = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
            Vec3D vec3d1 = new Vec3D(((double) this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

            vec3d1 = vec3d1.a(-this.pitch * 0.017453292F);
            vec3d1 = vec3d1.b(-this.yaw * 0.017453292F);
            vec3d1 = vec3d1.add(this.locX(), this.getHeadY(), this.locZ());
            this.world.addParticle(new ParticleParamItem(Particles.ITEM, itemstack), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
        }

    }

    protected void s() {
        EnumHand enumhand = this.getRaisedHand();

        if (!this.activeItem.equals(this.b(enumhand))) {
            this.releaseActiveItem();
        } else {
            if (!this.activeItem.isEmpty() && this.isHandRaised()) {
                this.b(this.activeItem, 16);
                // CraftBukkit start - fire PlayerItemConsumeEvent
                ItemStack itemstack;
                if (this instanceof EntityPlayer) {
                    org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.activeItem);
                    PlayerItemConsumeEvent event = new PlayerItemConsumeEvent((Player) this.getBukkitEntity(), craftItem);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        // Update client
                        ((EntityPlayer) this).getBukkitEntity().updateInventory();
                        ((EntityPlayer) this).getBukkitEntity().updateScaledHealth();
                        return;
                    }

                    itemstack = (craftItem.equals(event.getItem())) ? this.activeItem.a(this.world, this) : CraftItemStack.asNMSCopy(event.getItem()).a(world, this);
                } else {
                    itemstack = this.activeItem.a(this.world, this);
                }
                // CraftBukkit end

                if (itemstack != this.activeItem) {
                    this.a(enumhand, itemstack);
                }

                this.clearActiveItem();
            }

        }
    }

    public ItemStack getActiveItem() {
        return this.activeItem;
    }

    public int dY() {
        return this.bd;
    }

    public int dZ() {
        return this.isHandRaised() ? this.activeItem.k() - this.dY() : 0;
    }

    public void releaseActiveItem() {
        if (!this.activeItem.isEmpty()) {
            this.activeItem.a(this.world, this, this.dY());
            if (this.activeItem.m()) {
                this.t();
            }
        }

        this.clearActiveItem();
    }

    public void clearActiveItem() {
        if (!this.world.isClientSide) {
            this.c(1, false);
        }

        this.activeItem = ItemStack.b;
        this.bd = 0;
    }

    public boolean isBlocking() {
        if (this.isHandRaised() && !this.activeItem.isEmpty()) {
            Item item = this.activeItem.getItem();

            return item.d_(this.activeItem) != EnumAnimation.BLOCK ? false : item.e_(this.activeItem) - this.bd >= 5;
        } else {
            return false;
        }
    }

    public boolean ed() {
        return this.isSneaking();
    }

    public boolean isGliding() {
        return this.getFlag(7);
    }

    @Override
    public boolean bB() {
        return super.bB() || !this.isGliding() && this.getPose() == EntityPose.FALL_FLYING;
    }

    public boolean a(double d0, double d1, double d2, boolean flag) {
        double d3 = this.locX();
        double d4 = this.locY();
        double d5 = this.locZ();
        double d6 = d1;
        boolean flag1 = false;
        BlockPosition blockposition = new BlockPosition(d0, d1, d2);
        World world = this.world;

        if (world.isLoaded(blockposition)) {
            boolean flag2 = false;

            while (!flag2 && blockposition.getY() > 0) {
                BlockPosition blockposition1 = blockposition.down();
                IBlockData iblockdata = world.getType(blockposition1);

                if (iblockdata.getMaterial().isSolid()) {
                    flag2 = true;
                } else {
                    --d6;
                    blockposition = blockposition1;
                }
            }

            if (flag2) {
                // CraftBukkit start - Teleport event
                // this.enderTeleportTo(d0, d6, d2);
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.world.getWorld(), d3, d4, d5), new Location(this.world.getWorld(), d0, d6, d2));
                this.world.getServer().getPluginManager().callEvent(teleport);
                if (!teleport.isCancelled()) {
                    Location to = teleport.getTo();
                    this.enderTeleportTo(to.getX(), to.getY(), to.getZ());
                    if (world.getCubes(this) && !world.containsLiquid(this.getBoundingBox())) {
                        flag1 = true;
                    }
                }
                // CraftBukkit end
            }
        }

        if (!flag1) {
            this.enderTeleportTo(d3, d4, d5);
            return false;
        } else {
            if (flag) {
                world.broadcastEntityEffect(this, (byte) 46);
            }

            if (this instanceof EntityCreature) {
                ((EntityCreature) this).getNavigation().o();
            }

            return true;
        }
    }

    public boolean eg() {
        return true;
    }

    public boolean eh() {
        return true;
    }

    public boolean e(ItemStack itemstack) {
        return false;
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntityLiving(this);
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        return entitypose == EntityPose.SLEEPING ? EntityLiving.ah : super.a(entitypose).a(this.cR());
    }

    public ImmutableList<EntityPose> ei() {
        return ImmutableList.of(EntityPose.STANDING);
    }

    public AxisAlignedBB f(EntityPose entitypose) {
        EntitySize entitysize = this.a(entitypose);

        return new AxisAlignedBB((double) (-entitysize.width / 2.0F), 0.0D, (double) (-entitysize.width / 2.0F), (double) (entitysize.width / 2.0F), (double) entitysize.height, (double) (entitysize.width / 2.0F));
    }

    public Optional<BlockPosition> getBedPosition() {
        return (Optional) this.datawatcher.get(EntityLiving.bj);
    }

    public void e(BlockPosition blockposition) {
        this.datawatcher.set(EntityLiving.bj, Optional.of(blockposition));
    }

    public void ek() {
        this.datawatcher.set(EntityLiving.bj, Optional.empty());
    }

    public boolean isSleeping() {
        return this.getBedPosition().isPresent();
    }

    public void entitySleep(BlockPosition blockposition) {
        if (this.isPassenger()) {
            this.stopRiding();
        }

        IBlockData iblockdata = this.world.getType(blockposition);

        if (iblockdata.getBlock() instanceof BlockBed) {
            this.world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockBed.OCCUPIED, true), 3);
        }

        this.setPose(EntityPose.SLEEPING);
        this.a(blockposition);
        this.e(blockposition);
        this.setMot(Vec3D.ORIGIN);
        this.impulse = true;
    }

    private void a(BlockPosition blockposition) {
        this.setPosition((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.6875D, (double) blockposition.getZ() + 0.5D);
    }

    private boolean x() {
        return (Boolean) this.getBedPosition().map((blockposition) -> {
            return this.world.getType(blockposition).getBlock() instanceof BlockBed;
        }).orElse(false);
    }

    public void entityWakeup() {
        Optional<BlockPosition> optional = this.getBedPosition(); // CraftBukkit - decompile error
        World world = this.world;

        this.world.getClass();
        optional.filter(world::isLoaded).ifPresent((blockposition) -> {
            IBlockData iblockdata = this.world.getType(blockposition);

            if (iblockdata.getBlock() instanceof BlockBed) {
                this.world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockBed.OCCUPIED, false), 3);
                Vec3D vec3d = (Vec3D) BlockBed.a(this.getEntityType(), this.world, blockposition, this.yaw).orElseGet(() -> {
                    BlockPosition blockposition1 = blockposition.up();

                    return new Vec3D((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.1D, (double) blockposition1.getZ() + 0.5D);
                });
                Vec3D vec3d1 = Vec3D.c((BaseBlockPosition) blockposition).d(vec3d).d();
                float f = (float) MathHelper.g(MathHelper.d(vec3d1.z, vec3d1.x) * 57.2957763671875D - 90.0D);

                this.setPosition(vec3d.x, vec3d.y, vec3d.z);
                this.yaw = f;
                this.pitch = 0.0F;
            }

        });
        Vec3D vec3d = this.getPositionVector();

        this.setPose(EntityPose.STANDING);
        this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        this.ek();
    }

    @Override
    public boolean inBlock() {
        return !this.isSleeping() && super.inBlock();
    }

    @Override
    protected final float getHeadHeight(EntityPose entitypose, EntitySize entitysize) {
        return entitypose == EntityPose.SLEEPING ? 0.2F : this.b(entitypose, entitysize);
    }

    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return super.getHeadHeight(entitypose, entitysize);
    }

    public ItemStack f(ItemStack itemstack) {
        return ItemStack.b;
    }

    public ItemStack a(World world, ItemStack itemstack) {
        if (itemstack.F()) {
            world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), this.d(itemstack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            this.a(itemstack, world, this);
            if (!(this instanceof EntityHuman) || !((EntityHuman) this).abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
        }

        return itemstack;
    }

    private void a(ItemStack itemstack, World world, EntityLiving entityliving) {
        Item item = itemstack.getItem();

        if (item.isFood()) {
            List<Pair<MobEffect, Float>> list = item.getFoodInfo().f();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Pair<MobEffect, Float> pair = (Pair) iterator.next();

                if (!world.isClientSide && pair.getFirst() != null && world.random.nextFloat() < (Float) pair.getSecond()) {
                    entityliving.addEffect(new MobEffect((MobEffect) pair.getFirst()), EntityPotionEffectEvent.Cause.FOOD); // CraftBukkit
                }
            }
        }

    }

    private static byte f(EnumItemSlot enumitemslot) {
        switch (enumitemslot) {
            case MAINHAND:
                return 47;
            case OFFHAND:
                return 48;
            case HEAD:
                return 49;
            case CHEST:
                return 50;
            case FEET:
                return 52;
            case LEGS:
                return 51;
            default:
                return 47;
        }
    }

    public void broadcastItemBreak(EnumItemSlot enumitemslot) {
        this.world.broadcastEntityEffect(this, f(enumitemslot));
    }

    public void broadcastItemBreak(EnumHand enumhand) {
        this.broadcastItemBreak(enumhand == EnumHand.MAIN_HAND ? EnumItemSlot.MAINHAND : EnumItemSlot.OFFHAND);
    }
}
