package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public abstract class EntityHuman extends EntityLiving {

    public static final EntitySize bh = EntitySize.b(0.6F, 1.8F);
    private static final Map<EntityPose, EntitySize> b = ImmutableMap.builder().put(EntityPose.STANDING, EntityHuman.bh).put(EntityPose.SLEEPING, EntityHuman.ah).put(EntityPose.FALL_FLYING, EntitySize.b(0.6F, 0.6F)).put(EntityPose.SWIMMING, EntitySize.b(0.6F, 0.6F)).put(EntityPose.SPIN_ATTACK, EntitySize.b(0.6F, 0.6F)).put(EntityPose.CROUCHING, EntitySize.b(0.6F, 1.5F)).put(EntityPose.DYING, EntitySize.c(0.2F, 0.2F)).build();
    private static final DataWatcherObject<Float> c = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.c);
    private static final DataWatcherObject<Integer> d = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.b);
    protected static final DataWatcherObject<Byte> bi = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.a);
    protected static final DataWatcherObject<Byte> bj = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.a);
    protected static final DataWatcherObject<NBTTagCompound> bk = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.p);
    protected static final DataWatcherObject<NBTTagCompound> bl = DataWatcher.a(EntityHuman.class, DataWatcherRegistry.p);
    private long e;
    public final PlayerInventory inventory = new PlayerInventory(this);
    protected InventoryEnderChest enderChest = new InventoryEnderChest();
    public final ContainerPlayer defaultContainer;
    public Container activeContainer;
    protected FoodMetaData foodData = new FoodMetaData();
    protected int br;
    public float bs;
    public float bt;
    public int bu;
    public double bv;
    public double bw;
    public double bx;
    public double by;
    public double bz;
    public double bA;
    public int sleepTicks;
    protected boolean bB;
    public final PlayerAbilities abilities = new PlayerAbilities();
    public int expLevel;
    public int expTotal;
    public float exp;
    protected int bG;
    protected final float bH = 0.02F;
    private int g;
    private final GameProfile bJ;
    private ItemStack bL;
    private final ItemCooldown bM;
    @Nullable
    public EntityFishingHook hookedFish;

    public EntityHuman(World world, BlockPosition blockposition, float f, GameProfile gameprofile) {
        super(EntityTypes.PLAYER, world);
        this.bL = ItemStack.b;
        this.bM = this.i();
        this.a_(a(gameprofile));
        this.bJ = gameprofile;
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isClientSide, this);
        this.activeContainer = this.defaultContainer;
        this.setPositionRotation((double) blockposition.getX() + 0.5D, (double) (blockposition.getY() + 1), (double) blockposition.getZ() + 0.5D, f, 0.0F);
        this.aN = 180.0F;
    }

    public boolean a(World world, BlockPosition blockposition, EnumGamemode enumgamemode) {
        if (!enumgamemode.d()) {
            return false;
        } else if (enumgamemode == EnumGamemode.SPECTATOR) {
            return true;
        } else if (this.eJ()) {
            return false;
        } else {
            ItemStack itemstack = this.getItemInMainHand();

            return itemstack.isEmpty() || !itemstack.a(world.p(), new ShapeDetectorBlock(world, blockposition, false));
        }
    }

    public static AttributeProvider.Builder eo() {
        return EntityLiving.cK().a(GenericAttributes.ATTACK_DAMAGE, 1.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.10000000149011612D).a(GenericAttributes.ATTACK_SPEED).a(GenericAttributes.LUCK);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityHuman.c, 0.0F);
        this.datawatcher.register(EntityHuman.d, 0);
        this.datawatcher.register(EntityHuman.bi, (byte) 0);
        this.datawatcher.register(EntityHuman.bj, (byte) 1);
        this.datawatcher.register(EntityHuman.bk, new NBTTagCompound());
        this.datawatcher.register(EntityHuman.bl, new NBTTagCompound());
    }

    @Override
    public void tick() {
        this.noclip = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        if (this.bu > 0) {
            --this.bu;
        }

        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.world.isClientSide && this.world.isDay()) {
                this.wakeup(false, true);
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        this.es();
        super.tick();
        if (!this.world.isClientSide && this.activeContainer != null && !this.activeContainer.canUse(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        this.p();
        if (!this.world.isClientSide) {
            this.foodData.a(this);
            this.a(StatisticList.PLAY_ONE_MINUTE);
            if (this.isAlive()) {
                this.a(StatisticList.TIME_SINCE_DEATH);
            }

            if (this.bw()) {
                this.a(StatisticList.SNEAK_TIME);
            }

            if (!this.isSleeping()) {
                this.a(StatisticList.TIME_SINCE_REST);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.a(this.locX(), -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.a(this.locZ(), -2.9999999E7D, 2.9999999E7D);

        if (d0 != this.locX() || d1 != this.locZ()) {
            this.setPosition(d0, this.locY(), d1);
        }

        ++this.at;
        ItemStack itemstack = this.getItemInMainHand();

        if (!ItemStack.matches(this.bL, itemstack)) {
            if (!ItemStack.d(this.bL, itemstack)) {
                this.resetAttackCooldown();
            }

            this.bL = itemstack.cloneItemStack();
        }

        this.o();
        this.bM.a();
        this.et();
    }

    public boolean ep() {
        return this.isSneaking();
    }

    protected boolean eq() {
        return this.isSneaking();
    }

    protected boolean er() {
        return this.isSneaking();
    }

    protected boolean es() {
        this.bB = this.a((Tag) TagsFluid.WATER);
        return this.bB;
    }

    private void o() {
        ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);

        if (itemstack.getItem() == Items.TURTLE_HELMET && !this.a((Tag) TagsFluid.WATER)) {
            this.addEffect(new MobEffect(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
        }

    }

    protected ItemCooldown i() {
        return new ItemCooldown();
    }

    private void p() {
        this.bv = this.by;
        this.bw = this.bz;
        this.bx = this.bA;
        double d0 = this.locX() - this.by;
        double d1 = this.locY() - this.bz;
        double d2 = this.locZ() - this.bA;
        double d3 = 10.0D;

        if (d0 > 10.0D) {
            this.by = this.locX();
            this.bv = this.by;
        }

        if (d2 > 10.0D) {
            this.bA = this.locZ();
            this.bx = this.bA;
        }

        if (d1 > 10.0D) {
            this.bz = this.locY();
            this.bw = this.bz;
        }

        if (d0 < -10.0D) {
            this.by = this.locX();
            this.bv = this.by;
        }

        if (d2 < -10.0D) {
            this.bA = this.locZ();
            this.bx = this.bA;
        }

        if (d1 < -10.0D) {
            this.bz = this.locY();
            this.bw = this.bz;
        }

        this.by += d0 * 0.25D;
        this.bA += d2 * 0.25D;
        this.bz += d1 * 0.25D;
    }

    protected void et() {
        if (this.c(EntityPose.SWIMMING)) {
            EntityPose entitypose;

            if (this.isGliding()) {
                entitypose = EntityPose.FALL_FLYING;
            } else if (this.isSleeping()) {
                entitypose = EntityPose.SLEEPING;
            } else if (this.isSwimming()) {
                entitypose = EntityPose.SWIMMING;
            } else if (this.isRiptiding()) {
                entitypose = EntityPose.SPIN_ATTACK;
            } else if (this.isSneaking() && !this.abilities.isFlying) {
                entitypose = EntityPose.CROUCHING;
            } else {
                entitypose = EntityPose.STANDING;
            }

            EntityPose entitypose1;

            if (!this.isSpectator() && !this.isPassenger() && !this.c(entitypose)) {
                if (this.c(EntityPose.CROUCHING)) {
                    entitypose1 = EntityPose.CROUCHING;
                } else {
                    entitypose1 = EntityPose.SWIMMING;
                }
            } else {
                entitypose1 = entitypose;
            }

            this.setPose(entitypose1);
        }
    }

    @Override
    public int ai() {
        return this.abilities.isInvulnerable ? 1 : 80;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_PLAYER_SWIM;
    }

    @Override
    protected SoundEffect getSoundSplash() {
        return SoundEffects.ENTITY_PLAYER_SPLASH;
    }

    @Override
    protected SoundEffect getSoundSplashHighSpeed() {
        return SoundEffects.ENTITY_PLAYER_SPLASH_HIGH_SPEED;
    }

    @Override
    public int getDefaultPortalCooldown() {
        return 10;
    }

    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) {
        this.world.playSound(this, this.locX(), this.locY(), this.locZ(), soundeffect, this.getSoundCategory(), f, f1);
    }

    public void a(SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {}

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    public int getMaxFireTicks() {
        return 20;
    }

    public void closeInventory() {
        this.activeContainer = this.defaultContainer;
    }

    @Override
    public void passengerTick() {
        if (this.eq() && this.isPassenger()) {
            this.stopRiding();
            this.setSneaking(false);
        } else {
            double d0 = this.locX();
            double d1 = this.locY();
            double d2 = this.locZ();

            super.passengerTick();
            this.bs = this.bt;
            this.bt = 0.0F;
            this.q(this.locX() - d0, this.locY() - d1, this.locZ() - d2);
        }
    }

    @Override
    protected void doTick() {
        super.doTick();
        this.dz();
        this.aC = this.yaw;
    }

    @Override
    public void movementTick() {
        if (this.br > 0) {
            --this.br;
        }

        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksLived % 20 == 0) {
                this.heal(1.0F);
            }

            if (this.foodData.c() && this.ticksLived % 10 == 0) {
                this.foodData.a(this.foodData.getFoodLevel() + 1);
            }
        }

        this.inventory.j();
        this.bs = this.bt;
        super.movementTick();
        this.aE = 0.02F;
        if (this.isSprinting()) {
            this.aE = (float) ((double) this.aE + 0.005999999865889549D);
        }

        this.q((float) this.b(GenericAttributes.MOVEMENT_SPEED));
        float f;

        if (this.onGround && !this.dk() && !this.isSwimming()) {
            f = Math.min(0.1F, MathHelper.sqrt(c(this.getMot())));
        } else {
            f = 0.0F;
        }

        this.bt += (f - this.bt) * 0.4F;
        if (this.getHealth() > 0.0F && !this.isSpectator()) {
            AxisAlignedBB axisalignedbb;

            if (this.isPassenger() && !this.getVehicle().dead) {
                axisalignedbb = this.getBoundingBox().b(this.getVehicle().getBoundingBox()).grow(1.0D, 0.0D, 1.0D);
            } else {
                axisalignedbb = this.getBoundingBox().grow(1.0D, 0.5D, 1.0D);
            }

            List<Entity> list = this.world.getEntities(this, axisalignedbb);

            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (!entity.dead) {
                    this.c(entity);
                }
            }
        }

        this.j(this.getShoulderEntityLeft());
        this.j(this.getShoulderEntityRight());
        if (!this.world.isClientSide && (this.fallDistance > 0.5F || this.isInWater()) || this.abilities.isFlying || this.isSleeping()) {
            this.releaseShoulderEntities();
        }

    }

    private void j(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound != null && (!nbttagcompound.hasKey("Silent") || !nbttagcompound.getBoolean("Silent")) && this.world.random.nextInt(200) == 0) {
            String s = nbttagcompound.getString("id");

            EntityTypes.a(s).filter((entitytypes) -> {
                return entitytypes == EntityTypes.PARROT;
            }).ifPresent((entitytypes) -> {
                if (!EntityParrot.a(this.world, (Entity) this)) {
                    this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), EntityParrot.a(this.world, this.world.random), this.getSoundCategory(), 1.0F, EntityParrot.a(this.world.random));
                }

            });
        }

    }

    private void c(Entity entity) {
        entity.pickup(this);
    }

    public int getScore() {
        return (Integer) this.datawatcher.get(EntityHuman.d);
    }

    public void setScore(int i) {
        this.datawatcher.set(EntityHuman.d, i);
    }

    public void addScore(int i) {
        int j = this.getScore();

        this.datawatcher.set(EntityHuman.d, j + i);
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.ae();
        if (!this.isSpectator()) {
            this.d(damagesource);
        }

        if (damagesource != null) {
            this.setMot((double) (-MathHelper.cos((this.ap + this.yaw) * 0.017453292F) * 0.1F), 0.10000000149011612D, (double) (-MathHelper.sin((this.ap + this.yaw) * 0.017453292F) * 0.1F));
        } else {
            this.setMot(0.0D, 0.1D, 0.0D);
        }

        this.a(StatisticList.DEATHS);
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_DEATH));
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_REST));
        this.extinguish();
        this.setFlag(0, false);
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            this.removeCursedItems();
            this.inventory.dropContents();
        }

    }

    protected void removeCursedItems() {
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);

            if (!itemstack.isEmpty() && EnchantmentManager.shouldNotDrop(itemstack)) {
                this.inventory.splitWithoutUpdate(i);
            }
        }

    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return damagesource == DamageSource.BURN ? SoundEffects.ENTITY_PLAYER_HURT_ON_FIRE : (damagesource == DamageSource.DROWN ? SoundEffects.ENTITY_PLAYER_HURT_DROWN : (damagesource == DamageSource.SWEET_BERRY_BUSH ? SoundEffects.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH : SoundEffects.ENTITY_PLAYER_HURT));
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PLAYER_DEATH;
    }

    public boolean dropItem(boolean flag) {
        return this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, flag && !this.inventory.getItemInHand().isEmpty() ? this.inventory.getItemInHand().getCount() : 1), false, true) != null;
    }

    @Nullable
    public EntityItem drop(ItemStack itemstack, boolean flag) {
        return this.a(itemstack, false, flag);
    }

    @Nullable
    public EntityItem a(ItemStack itemstack, boolean flag, boolean flag1) {
        if (itemstack.isEmpty()) {
            return null;
        } else {
            if (this.world.isClientSide) {
                this.swingHand(EnumHand.MAIN_HAND);
            }

            double d0 = this.getHeadY() - 0.30000001192092896D;
            EntityItem entityitem = new EntityItem(this.world, this.locX(), d0, this.locZ(), itemstack);

            entityitem.setPickupDelay(40);
            if (flag1) {
                entityitem.setThrower(this.getUniqueID());
            }

            float f;
            float f1;

            if (flag) {
                f = this.random.nextFloat() * 0.5F;
                f1 = this.random.nextFloat() * 6.2831855F;
                entityitem.setMot((double) (-MathHelper.sin(f1) * f), 0.20000000298023224D, (double) (MathHelper.cos(f1) * f));
            } else {
                f = 0.3F;
                f1 = MathHelper.sin(this.pitch * 0.017453292F);
                float f2 = MathHelper.cos(this.pitch * 0.017453292F);
                float f3 = MathHelper.sin(this.yaw * 0.017453292F);
                float f4 = MathHelper.cos(this.yaw * 0.017453292F);
                float f5 = this.random.nextFloat() * 6.2831855F;
                float f6 = 0.02F * this.random.nextFloat();

                entityitem.setMot((double) (-f3 * f2 * 0.3F) + Math.cos((double) f5) * (double) f6, (double) (-f1 * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double) (f4 * f2 * 0.3F) + Math.sin((double) f5) * (double) f6);
            }

            return entityitem;
        }
    }

    public float c(IBlockData iblockdata) {
        float f = this.inventory.a(iblockdata);

        if (f > 1.0F) {
            int i = EnchantmentManager.getDigSpeedEnchantmentLevel(this);
            ItemStack itemstack = this.getItemInMainHand();

            if (i > 0 && !itemstack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }

        if (MobEffectUtil.a(this)) {
            f *= 1.0F + (float) (MobEffectUtil.b(this) + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffects.SLOWER_DIG)) {
            float f1;

            switch (this.getEffect(MobEffects.SLOWER_DIG).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (this.a((Tag) TagsFluid.WATER) && !EnchantmentManager.h((EntityLiving) this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean hasBlock(IBlockData iblockdata) {
        return !iblockdata.isRequiresSpecialTool() || this.inventory.getItemInHand().canDestroySpecialBlock(iblockdata);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.a_(a(this.bJ));
        NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);

        this.inventory.b(nbttaglist);
        this.inventory.itemInHandIndex = nbttagcompound.getInt("SelectedItemSlot");
        this.sleepTicks = nbttagcompound.getShort("SleepTimer");
        this.exp = nbttagcompound.getFloat("XpP");
        this.expLevel = nbttagcompound.getInt("XpLevel");
        this.expTotal = nbttagcompound.getInt("XpTotal");
        this.bG = nbttagcompound.getInt("XpSeed");
        if (this.bG == 0) {
            this.bG = this.random.nextInt();
        }

        this.setScore(nbttagcompound.getInt("Score"));
        this.foodData.a(nbttagcompound);
        this.abilities.b(nbttagcompound);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue((double) this.abilities.b());
        if (nbttagcompound.hasKeyOfType("EnderItems", 9)) {
            this.enderChest.a(nbttagcompound.getList("EnderItems", 10));
        }

        if (nbttagcompound.hasKeyOfType("ShoulderEntityLeft", 10)) {
            this.setShoulderEntityLeft(nbttagcompound.getCompound("ShoulderEntityLeft"));
        }

        if (nbttagcompound.hasKeyOfType("ShoulderEntityRight", 10)) {
            this.setShoulderEntityRight(nbttagcompound.getCompound("ShoulderEntityRight"));
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());
        nbttagcompound.set("Inventory", this.inventory.a(new NBTTagList()));
        nbttagcompound.setInt("SelectedItemSlot", this.inventory.itemInHandIndex);
        nbttagcompound.setShort("SleepTimer", (short) this.sleepTicks);
        nbttagcompound.setFloat("XpP", this.exp);
        nbttagcompound.setInt("XpLevel", this.expLevel);
        nbttagcompound.setInt("XpTotal", this.expTotal);
        nbttagcompound.setInt("XpSeed", this.bG);
        nbttagcompound.setInt("Score", this.getScore());
        this.foodData.b(nbttagcompound);
        this.abilities.a(nbttagcompound);
        nbttagcompound.set("EnderItems", this.enderChest.g());
        if (!this.getShoulderEntityLeft().isEmpty()) {
            nbttagcompound.set("ShoulderEntityLeft", this.getShoulderEntityLeft());
        }

        if (!this.getShoulderEntityRight().isEmpty()) {
            nbttagcompound.set("ShoulderEntityRight", this.getShoulderEntityRight());
        }

    }

    @Override
    public boolean isInvulnerable(DamageSource damagesource) {
        return super.isInvulnerable(damagesource) ? true : (damagesource == DamageSource.DROWN ? !this.world.getGameRules().getBoolean(GameRules.DROWNING_DAMAGE) : (damagesource == DamageSource.FALL ? !this.world.getGameRules().getBoolean(GameRules.FALL_DAMAGE) : (damagesource.isFire() ? !this.world.getGameRules().getBoolean(GameRules.FIRE_DAMAGE) : false)));
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (this.abilities.isInvulnerable && !damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            this.ticksFarFromPlayer = 0;
            if (this.dk()) {
                return false;
            } else {
                this.releaseShoulderEntities();
                if (damagesource.s()) {
                    if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                        f = 0.0F;
                    }

                    if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                        f = Math.min(f / 2.0F + 1.0F, f);
                    }

                    if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        f = f * 3.0F / 2.0F;
                    }
                }

                return f == 0.0F ? false : super.damageEntity(damagesource, f);
            }
        }
    }

    @Override
    protected void shieldBlock(EntityLiving entityliving) {
        super.shieldBlock(entityliving);
        if (entityliving.getItemInMainHand().getItem() instanceof ItemAxe) {
            this.p(true);
        }

    }

    public boolean a(EntityHuman entityhuman) {
        ScoreboardTeamBase scoreboardteambase = this.getScoreboardTeam();
        ScoreboardTeamBase scoreboardteambase1 = entityhuman.getScoreboardTeam();

        return scoreboardteambase == null ? true : (!scoreboardteambase.isAlly(scoreboardteambase1) ? true : scoreboardteambase.allowFriendlyFire());
    }

    @Override
    protected void damageArmor(DamageSource damagesource, float f) {
        this.inventory.a(damagesource, f);
    }

    @Override
    protected void damageShield(float f) {
        if (this.activeItem.getItem() == Items.SHIELD) {
            if (!this.world.isClientSide) {
                this.b(StatisticList.ITEM_USED.b(this.activeItem.getItem()));
            }

            if (f >= 3.0F) {
                int i = 1 + MathHelper.d(f);
                EnumHand enumhand = this.getRaisedHand();

                this.activeItem.damage(i, this, (entityhuman) -> {
                    entityhuman.broadcastItemBreak(enumhand);
                });
                if (this.activeItem.isEmpty()) {
                    if (enumhand == EnumHand.MAIN_HAND) {
                        this.setSlot(EnumItemSlot.MAINHAND, ItemStack.b);
                    } else {
                        this.setSlot(EnumItemSlot.OFFHAND, ItemStack.b);
                    }

                    this.activeItem = ItemStack.b;
                    this.playSound(SoundEffects.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);
                }
            }

        }
    }

    @Override
    protected void damageEntity0(DamageSource damagesource, float f) {
        if (!this.isInvulnerable(damagesource)) {
            f = this.applyArmorModifier(damagesource, f);
            f = this.applyMagicModifier(damagesource, f);
            float f1 = f;

            f = Math.max(f - this.getAbsorptionHearts(), 0.0F);
            this.setAbsorptionHearts(this.getAbsorptionHearts() - (f1 - f));
            float f2 = f1 - f;

            if (f2 > 0.0F && f2 < 3.4028235E37F) {
                this.a(StatisticList.DAMAGE_ABSORBED, Math.round(f2 * 10.0F));
            }

            if (f != 0.0F) {
                this.applyExhaustion(damagesource.getExhaustionCost());
                float f3 = this.getHealth();

                this.setHealth(this.getHealth() - f);
                this.getCombatTracker().trackDamage(damagesource, f3, f);
                if (f < 3.4028235E37F) {
                    this.a(StatisticList.DAMAGE_TAKEN, Math.round(f * 10.0F));
                }

            }
        }
    }

    @Override
    protected boolean cO() {
        return !this.abilities.isFlying && super.cO();
    }

    public void openSign(TileEntitySign tileentitysign) {}

    public void a(CommandBlockListenerAbstract commandblocklistenerabstract) {}

    public void a(TileEntityCommand tileentitycommand) {}

    public void a(TileEntityStructure tileentitystructure) {}

    public void a(TileEntityJigsaw tileentityjigsaw) {}

    public void openHorseInventory(EntityHorseAbstract entityhorseabstract, IInventory iinventory) {}

    public OptionalInt openContainer(@Nullable ITileInventory itileinventory) {
        return OptionalInt.empty();
    }

    public void openTrade(int i, MerchantRecipeList merchantrecipelist, int j, int k, boolean flag, boolean flag1) {}

    public void openBook(ItemStack itemstack, EnumHand enumhand) {}

    public EnumInteractionResult a(Entity entity, EnumHand enumhand) {
        if (this.isSpectator()) {
            if (entity instanceof ITileInventory) {
                this.openContainer((ITileInventory) entity);
            }

            return EnumInteractionResult.PASS;
        } else {
            ItemStack itemstack = this.b(enumhand);
            ItemStack itemstack1 = itemstack.cloneItemStack();
            EnumInteractionResult enuminteractionresult = entity.a(this, enumhand);

            if (enuminteractionresult.a()) {
                if (this.abilities.canInstantlyBuild && itemstack == this.b(enumhand) && itemstack.getCount() < itemstack1.getCount()) {
                    itemstack.setCount(itemstack1.getCount());
                }

                return enuminteractionresult;
            } else {
                if (!itemstack.isEmpty() && entity instanceof EntityLiving) {
                    if (this.abilities.canInstantlyBuild) {
                        itemstack = itemstack1;
                    }

                    EnumInteractionResult enuminteractionresult1 = itemstack.a(this, (EntityLiving) entity, enumhand);

                    if (enuminteractionresult1.a()) {
                        if (itemstack.isEmpty() && !this.abilities.canInstantlyBuild) {
                            this.a(enumhand, ItemStack.b);
                        }

                        return enuminteractionresult1;
                    }
                }

                return EnumInteractionResult.PASS;
            }
        }
    }

    @Override
    public double ba() {
        return -0.35D;
    }

    @Override
    public void be() {
        super.be();
        this.j = 0;
    }

    @Override
    protected boolean isFrozen() {
        return super.isFrozen() || this.isSleeping();
    }

    @Override
    public boolean cS() {
        return !this.abilities.isFlying;
    }

    @Override
    protected Vec3D a(Vec3D vec3d, EnumMoveType enummovetype) {
        if (!this.abilities.isFlying && (enummovetype == EnumMoveType.SELF || enummovetype == EnumMoveType.PLAYER) && this.er() && this.q()) {
            double d0 = vec3d.x;
            double d1 = vec3d.z;
            double d2 = 0.05D;

            while (d0 != 0.0D && this.world.getCubes(this, this.getBoundingBox().d(d0, (double) (-this.G), 0.0D))) {
                if (d0 < 0.05D && d0 >= -0.05D) {
                    d0 = 0.0D;
                } else if (d0 > 0.0D) {
                    d0 -= 0.05D;
                } else {
                    d0 += 0.05D;
                }
            }

            while (d1 != 0.0D && this.world.getCubes(this, this.getBoundingBox().d(0.0D, (double) (-this.G), d1))) {
                if (d1 < 0.05D && d1 >= -0.05D) {
                    d1 = 0.0D;
                } else if (d1 > 0.0D) {
                    d1 -= 0.05D;
                } else {
                    d1 += 0.05D;
                }
            }

            while (d0 != 0.0D && d1 != 0.0D && this.world.getCubes(this, this.getBoundingBox().d(d0, (double) (-this.G), d1))) {
                if (d0 < 0.05D && d0 >= -0.05D) {
                    d0 = 0.0D;
                } else if (d0 > 0.0D) {
                    d0 -= 0.05D;
                } else {
                    d0 += 0.05D;
                }

                if (d1 < 0.05D && d1 >= -0.05D) {
                    d1 = 0.0D;
                } else if (d1 > 0.0D) {
                    d1 -= 0.05D;
                } else {
                    d1 += 0.05D;
                }
            }

            vec3d = new Vec3D(d0, vec3d.y, d1);
        }

        return vec3d;
    }

    private boolean q() {
        return this.onGround || this.fallDistance < this.G && !this.world.getCubes(this, this.getBoundingBox().d(0.0D, (double) (this.fallDistance - this.G), 0.0D));
    }

    public void attack(Entity entity) {
        if (entity.bK()) {
            if (!entity.t(this)) {
                float f = (float) this.b(GenericAttributes.ATTACK_DAMAGE);
                float f1;

                if (entity instanceof EntityLiving) {
                    f1 = EnchantmentManager.a(this.getItemInMainHand(), ((EntityLiving) entity).getMonsterType());
                } else {
                    f1 = EnchantmentManager.a(this.getItemInMainHand(), EnumMonsterType.UNDEFINED);
                }

                float f2 = this.getAttackCooldown(0.5F);

                f *= 0.2F + f2 * f2 * 0.8F;
                f1 *= f2;
                this.resetAttackCooldown();
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    byte b0 = 0;
                    int i = b0 + EnchantmentManager.b((EntityLiving) this);

                    if (this.isSprinting() && flag) {
                        this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.isClimbing() && !this.isInWater() && !this.hasEffect(MobEffects.BLINDNESS) && !this.isPassenger() && entity instanceof EntityLiving;

                    flag2 = flag2 && !this.isSprinting();
                    if (flag2) {
                        f *= 1.5F;
                    }

                    f += f1;
                    boolean flag3 = false;
                    double d0 = (double) (this.A - this.z);

                    if (flag && !flag2 && !flag1 && this.onGround && d0 < (double) this.dM()) {
                        ItemStack itemstack = this.b(EnumHand.MAIN_HAND);

                        if (itemstack.getItem() instanceof ItemSword) {
                            flag3 = true;
                        }
                    }

                    float f3 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);

                    if (entity instanceof EntityLiving) {
                        f3 = ((EntityLiving) entity).getHealth();
                        if (j > 0 && !entity.isBurning()) {
                            flag4 = true;
                            entity.setOnFire(1);
                        }
                    }

                    Vec3D vec3d = entity.getMot();
                    boolean flag5 = entity.damageEntity(DamageSource.playerAttack(this), f);

                    if (flag5) {
                        if (i > 0) {
                            if (entity instanceof EntityLiving) {
                                ((EntityLiving) entity).a((float) i * 0.5F, (double) MathHelper.sin(this.yaw * 0.017453292F), (double) (-MathHelper.cos(this.yaw * 0.017453292F)));
                            } else {
                                entity.i((double) (-MathHelper.sin(this.yaw * 0.017453292F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 0.017453292F) * (float) i * 0.5F));
                            }

                            this.setMot(this.getMot().d(0.6D, 1.0D, 0.6D));
                            this.setSprinting(false);
                        }

                        if (flag3) {
                            float f4 = 1.0F + EnchantmentManager.a((EntityLiving) this) * f;
                            List<EntityLiving> list = this.world.a(EntityLiving.class, entity.getBoundingBox().grow(1.0D, 0.25D, 1.0D));
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                EntityLiving entityliving = (EntityLiving) iterator.next();

                                if (entityliving != this && entityliving != entity && !this.r(entityliving) && (!(entityliving instanceof EntityArmorStand) || !((EntityArmorStand) entityliving).isMarker()) && this.h((Entity) entityliving) < 9.0D) {
                                    entityliving.a(0.4F, (double) MathHelper.sin(this.yaw * 0.017453292F), (double) (-MathHelper.cos(this.yaw * 0.017453292F)));
                                    entityliving.damageEntity(DamageSource.playerAttack(this), f4);
                                }
                            }

                            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            this.ew();
                        }

                        if (entity instanceof EntityPlayer && entity.velocityChanged) {
                            ((EntityPlayer) entity).playerConnection.sendPacket(new PacketPlayOutEntityVelocity(entity));
                            entity.velocityChanged = false;
                            entity.setMot(vec3d);
                        }

                        if (flag2) {
                            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                            this.a(entity);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                            } else {
                                this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F) {
                            this.b(entity);
                        }

                        this.z(entity);
                        if (entity instanceof EntityLiving) {
                            EnchantmentManager.a((EntityLiving) entity, (Entity) this);
                        }

                        EnchantmentManager.b((EntityLiving) this, entity);
                        ItemStack itemstack1 = this.getItemInMainHand();
                        Object object = entity;

                        if (entity instanceof EntityComplexPart) {
                            object = ((EntityComplexPart) entity).owner;
                        }

                        if (!this.world.isClientSide && !itemstack1.isEmpty() && object instanceof EntityLiving) {
                            itemstack1.a((EntityLiving) object, this);
                            if (itemstack1.isEmpty()) {
                                this.a(EnumHand.MAIN_HAND, ItemStack.b);
                            }
                        }

                        if (entity instanceof EntityLiving) {
                            float f5 = f3 - ((EntityLiving) entity).getHealth();

                            this.a(StatisticList.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                entity.setOnFire(j * 4);
                            }

                            if (this.world instanceof WorldServer && f5 > 2.0F) {
                                int k = (int) ((double) f5 * 0.5D);

                                ((WorldServer) this.world).a(Particles.DAMAGE_INDICATOR, entity.locX(), entity.e(0.5D), entity.locZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        this.applyExhaustion(0.1F);
                    } else {
                        this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
                        if (flag4) {
                            entity.extinguish();
                        }
                    }
                }

            }
        }
    }

    @Override
    protected void g(EntityLiving entityliving) {
        this.attack(entityliving);
    }

    public void p(boolean flag) {
        float f = 0.25F + (float) EnchantmentManager.getDigSpeedEnchantmentLevel(this) * 0.05F;

        if (flag) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            this.getCooldownTracker().setCooldown(Items.SHIELD, 100);
            this.clearActiveItem();
            this.world.broadcastEntityEffect(this, (byte) 30);
        }

    }

    public void a(Entity entity) {}

    public void b(Entity entity) {}

    public void ew() {
        double d0 = (double) (-MathHelper.sin(this.yaw * 0.017453292F));
        double d1 = (double) MathHelper.cos(this.yaw * 0.017453292F);

        if (this.world instanceof WorldServer) {
            ((WorldServer) this.world).a(Particles.SWEEP_ATTACK, this.locX() + d0, this.e(0.5D), this.locZ() + d1, 0, d0, 0.0D, d1, 0.0D);
        }

    }

    @Override
    public void die() {
        super.die();
        this.defaultContainer.b(this);
        if (this.activeContainer != null) {
            this.activeContainer.b(this);
        }

    }

    public boolean ey() {
        return false;
    }

    public GameProfile getProfile() {
        return this.bJ;
    }

    public Either<EntityHuman.EnumBedResult, Unit> sleep(BlockPosition blockposition) {
        this.entitySleep(blockposition);
        this.sleepTicks = 0;
        return Either.right(Unit.INSTANCE);
    }

    public void wakeup(boolean flag, boolean flag1) {
        super.entityWakeup();
        if (this.world instanceof WorldServer && flag1) {
            ((WorldServer) this.world).everyoneSleeping();
        }

        this.sleepTicks = flag ? 0 : 100;
    }

    @Override
    public void entityWakeup() {
        this.wakeup(true, true);
    }

    public static Optional<Vec3D> getBed(WorldServer worldserver, BlockPosition blockposition, float f, boolean flag, boolean flag1) {
        IBlockData iblockdata = worldserver.getType(blockposition);
        Block block = iblockdata.getBlock();

        if (block instanceof BlockRespawnAnchor && (Integer) iblockdata.get(BlockRespawnAnchor.a) > 0 && BlockRespawnAnchor.a((World) worldserver)) {
            Optional<Vec3D> optional = BlockRespawnAnchor.a(EntityTypes.PLAYER, (ICollisionAccess) worldserver, blockposition);

            if (!flag1 && optional.isPresent()) {
                worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRespawnAnchor.a, (Integer) iblockdata.get(BlockRespawnAnchor.a) - 1), 3);
            }

            return optional;
        } else if (block instanceof BlockBed && BlockBed.a((World) worldserver)) {
            return BlockBed.a(EntityTypes.PLAYER, worldserver, blockposition, f);
        } else if (!flag) {
            return Optional.empty();
        } else {
            boolean flag2 = block.ai_();
            boolean flag3 = worldserver.getType(blockposition.up()).getBlock().ai_();

            return flag2 && flag3 ? Optional.of(new Vec3D((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.1D, (double) blockposition.getZ() + 0.5D)) : Optional.empty();
        }
    }

    public boolean isDeeplySleeping() {
        return this.isSleeping() && this.sleepTicks >= 100;
    }

    public int eB() {
        return this.sleepTicks;
    }

    public void a(IChatBaseComponent ichatbasecomponent, boolean flag) {}

    public void a(MinecraftKey minecraftkey) {
        this.b(StatisticList.CUSTOM.b(minecraftkey));
    }

    public void a(MinecraftKey minecraftkey, int i) {
        this.a(StatisticList.CUSTOM.b(minecraftkey), i);
    }

    public void b(Statistic<?> statistic) {
        this.a(statistic, 1);
    }

    public void a(Statistic<?> statistic, int i) {}

    public void a(Statistic<?> statistic) {}

    public int discoverRecipes(Collection<IRecipe<?>> collection) {
        return 0;
    }

    public void a(MinecraftKey[] aminecraftkey) {}

    public int undiscoverRecipes(Collection<IRecipe<?>> collection) {
        return 0;
    }

    @Override
    public void jump() {
        super.jump();
        this.a(StatisticList.JUMP);
        if (this.isSprinting()) {
            this.applyExhaustion(0.2F);
        } else {
            this.applyExhaustion(0.05F);
        }

    }

    @Override
    public void g(Vec3D vec3d) {
        double d0 = this.locX();
        double d1 = this.locY();
        double d2 = this.locZ();
        double d3;

        if (this.isSwimming() && !this.isPassenger()) {
            d3 = this.getLookDirection().y;
            double d4 = d3 < -0.2D ? 0.085D : 0.06D;

            if (d3 <= 0.0D || this.jumping || !this.world.getType(new BlockPosition(this.locX(), this.locY() + 1.0D - 0.1D, this.locZ())).getFluid().isEmpty()) {
                Vec3D vec3d1 = this.getMot();

                this.setMot(vec3d1.add(0.0D, (d3 - vec3d1.y) * d4, 0.0D));
            }
        }

        if (this.abilities.isFlying && !this.isPassenger()) {
            d3 = this.getMot().y;
            float f = this.aE;

            this.aE = this.abilities.a() * (float) (this.isSprinting() ? 2 : 1);
            super.g(vec3d);
            Vec3D vec3d2 = this.getMot();

            this.setMot(vec3d2.x, d3 * 0.6D, vec3d2.z);
            this.aE = f;
            this.fallDistance = 0.0F;
            this.setFlag(7, false);
        } else {
            super.g(vec3d);
        }

        this.checkMovement(this.locX() - d0, this.locY() - d1, this.locZ() - d2);
    }

    @Override
    public void aI() {
        if (this.abilities.isFlying) {
            this.setSwimming(false);
        } else {
            super.aI();
        }

    }

    protected boolean f(BlockPosition blockposition) {
        return !this.world.getType(blockposition).o(this.world, blockposition);
    }

    @Override
    public float dM() {
        return (float) this.b(GenericAttributes.MOVEMENT_SPEED);
    }

    public void checkMovement(double d0, double d1, double d2) {
        if (!this.isPassenger()) {
            int i;

            if (this.isSwimming()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.SWIM_ONE_CM, i);
                    this.applyExhaustion(0.01F * (float) i * 0.01F);
                }
            } else if (this.a((Tag) TagsFluid.WATER)) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.WALK_UNDER_WATER_ONE_CM, i);
                    this.applyExhaustion(0.01F * (float) i * 0.01F);
                }
            } else if (this.isInWater()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.WALK_ON_WATER_ONE_CM, i);
                    this.applyExhaustion(0.01F * (float) i * 0.01F);
                }
            } else if (this.isClimbing()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.CLIMB_ONE_CM, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    if (this.isSprinting()) {
                        this.a(StatisticList.SPRINT_ONE_CM, i);
                        this.applyExhaustion(0.1F * (float) i * 0.01F);
                    } else if (this.by()) {
                        this.a(StatisticList.CROUCH_ONE_CM, i);
                        this.applyExhaustion(0.0F * (float) i * 0.01F);
                    } else {
                        this.a(StatisticList.WALK_ONE_CM, i);
                        this.applyExhaustion(0.0F * (float) i * 0.01F);
                    }
                }
            } else if (this.isGliding()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                this.a(StatisticList.AVIATE_ONE_CM, i);
            } else {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.FLY_ONE_CM, i);
                }
            }

        }
    }

    private void q(double d0, double d1, double d2) {
        if (this.isPassenger()) {
            int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                Entity entity = this.getVehicle();

                if (entity instanceof EntityMinecartAbstract) {
                    this.a(StatisticList.MINECART_ONE_CM, i);
                } else if (entity instanceof EntityBoat) {
                    this.a(StatisticList.BOAT_ONE_CM, i);
                } else if (entity instanceof EntityPig) {
                    this.a(StatisticList.PIG_ONE_CM, i);
                } else if (entity instanceof EntityHorseAbstract) {
                    this.a(StatisticList.HORSE_ONE_CM, i);
                } else if (entity instanceof EntityStrider) {
                    this.a(StatisticList.STRIDER_ONE_CM, i);
                }
            }
        }

    }

    @Override
    public boolean b(float f, float f1) {
        if (this.abilities.canFly) {
            return false;
        } else {
            if (f >= 2.0F) {
                this.a(StatisticList.FALL_ONE_CM, (int) Math.round((double) f * 100.0D));
            }

            return super.b(f, f1);
        }
    }

    public boolean eC() {
        if (!this.onGround && !this.isGliding() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.getEquipment(EnumItemSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.d(itemstack)) {
                this.startGliding();
                return true;
            }
        }

        return false;
    }

    public void startGliding() {
        this.setFlag(7, true);
    }

    public void stopGliding() {
        this.setFlag(7, true);
        this.setFlag(7, false);
    }

    @Override
    protected void aL() {
        if (!this.isSpectator()) {
            super.aL();
        }

    }

    @Override
    protected SoundEffect getSoundFall(int i) {
        return i > 4 ? SoundEffects.ENTITY_PLAYER_BIG_FALL : SoundEffects.ENTITY_PLAYER_SMALL_FALL;
    }

    @Override
    public void a(WorldServer worldserver, EntityLiving entityliving) {
        this.b(StatisticList.ENTITY_KILLED.b(entityliving.getEntityType()));
    }

    @Override
    public void a(IBlockData iblockdata, Vec3D vec3d) {
        if (!this.abilities.isFlying) {
            super.a(iblockdata, vec3d);
        }

    }

    public void giveExp(int i) {
        this.addScore(i);
        this.exp += (float) i / (float) this.getExpToLevel();
        this.expTotal = MathHelper.clamp(this.expTotal + i, 0, Integer.MAX_VALUE);

        while (this.exp < 0.0F) {
            float f = this.exp * (float) this.getExpToLevel();

            if (this.expLevel > 0) {
                this.levelDown(-1);
                this.exp = 1.0F + f / (float) this.getExpToLevel();
            } else {
                this.levelDown(-1);
                this.exp = 0.0F;
            }
        }

        while (this.exp >= 1.0F) {
            this.exp = (this.exp - 1.0F) * (float) this.getExpToLevel();
            this.levelDown(1);
            this.exp /= (float) this.getExpToLevel();
        }

    }

    public int eF() {
        return this.bG;
    }

    public void enchantDone(ItemStack itemstack, int i) {
        this.expLevel -= i;
        if (this.expLevel < 0) {
            this.expLevel = 0;
            this.exp = 0.0F;
            this.expTotal = 0;
        }

        this.bG = this.random.nextInt();
    }

    public void levelDown(int i) {
        this.expLevel += i;
        if (this.expLevel < 0) {
            this.expLevel = 0;
            this.exp = 0.0F;
            this.expTotal = 0;
        }

        if (i > 0 && this.expLevel % 5 == 0 && (float) this.g < (float) this.ticksLived - 100.0F) {
            float f = this.expLevel > 30 ? 1.0F : (float) this.expLevel / 30.0F;

            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75F, 1.0F);
            this.g = this.ticksLived;
        }

    }

    public int getExpToLevel() {
        return this.expLevel >= 30 ? 112 + (this.expLevel - 30) * 9 : (this.expLevel >= 15 ? 37 + (this.expLevel - 15) * 5 : 7 + this.expLevel * 2);
    }

    public void applyExhaustion(float f) {
        if (!this.abilities.isInvulnerable) {
            if (!this.world.isClientSide) {
                this.foodData.a(f);
            }

        }
    }

    public FoodMetaData getFoodData() {
        return this.foodData;
    }

    public boolean q(boolean flag) {
        return this.abilities.isInvulnerable || flag || this.foodData.c();
    }

    public boolean eI() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean eJ() {
        return this.abilities.mayBuild;
    }

    public boolean a(BlockPosition blockposition, EnumDirection enumdirection, ItemStack itemstack) {
        if (this.abilities.mayBuild) {
            return true;
        } else {
            BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());
            ShapeDetectorBlock shapedetectorblock = new ShapeDetectorBlock(this.world, blockposition1, false);

            return itemstack.b(this.world.p(), shapedetectorblock);
        }
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) && !this.isSpectator()) {
            int i = this.expLevel * 7;

            return i > 100 ? 100 : i;
        } else {
            return 0;
        }
    }

    @Override
    protected boolean alwaysGivesExp() {
        return true;
    }

    @Override
    protected boolean playStepSound() {
        return !this.abilities.isFlying && (!this.onGround || !this.bw());
    }

    public void updateAbilities() {}

    public void a(EnumGamemode enumgamemode) {}

    @Override
    public IChatBaseComponent getDisplayName() {
        return new ChatComponentText(this.bJ.getName());
    }

    public InventoryEnderChest getEnderChest() {
        return this.enderChest;
    }

    @Override
    public ItemStack getEquipment(EnumItemSlot enumitemslot) {
        return enumitemslot == EnumItemSlot.MAINHAND ? this.inventory.getItemInHand() : (enumitemslot == EnumItemSlot.OFFHAND ? (ItemStack) this.inventory.extraSlots.get(0) : (enumitemslot.a() == EnumItemSlot.Function.ARMOR ? (ItemStack) this.inventory.armor.get(enumitemslot.b()) : ItemStack.b));
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        if (enumitemslot == EnumItemSlot.MAINHAND) {
            this.b(itemstack);
            this.inventory.items.set(this.inventory.itemInHandIndex, itemstack);
        } else if (enumitemslot == EnumItemSlot.OFFHAND) {
            this.b(itemstack);
            this.inventory.extraSlots.set(0, itemstack);
        } else if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
            this.b(itemstack);
            this.inventory.armor.set(enumitemslot.b(), itemstack);
        }

    }

    public boolean g(ItemStack itemstack) {
        this.b(itemstack);
        return this.inventory.pickup(itemstack);
    }

    @Override
    public Iterable<ItemStack> bm() {
        return Lists.newArrayList(new ItemStack[]{this.getItemInMainHand(), this.getItemInOffHand()});
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.inventory.armor;
    }

    public boolean g(NBTTagCompound nbttagcompound) {
        if (!this.isPassenger() && this.onGround && !this.isInWater()) {
            if (this.getShoulderEntityLeft().isEmpty()) {
                this.setShoulderEntityLeft(nbttagcompound);
                this.e = this.world.getTime();
                return true;
            } else if (this.getShoulderEntityRight().isEmpty()) {
                this.setShoulderEntityRight(nbttagcompound);
                this.e = this.world.getTime();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void releaseShoulderEntities() {
        if (this.e + 20L < this.world.getTime()) {
            this.spawnEntityFromShoulder(this.getShoulderEntityLeft());
            this.setShoulderEntityLeft(new NBTTagCompound());
            this.spawnEntityFromShoulder(this.getShoulderEntityRight());
            this.setShoulderEntityRight(new NBTTagCompound());
        }

    }

    private void spawnEntityFromShoulder(NBTTagCompound nbttagcompound) {
        if (!this.world.isClientSide && !nbttagcompound.isEmpty()) {
            EntityTypes.a(nbttagcompound, this.world).ifPresent((entity) -> {
                if (entity instanceof EntityTameableAnimal) {
                    ((EntityTameableAnimal) entity).setOwnerUUID(this.uniqueID);
                }

                entity.setPosition(this.locX(), this.locY() + 0.699999988079071D, this.locZ());
                ((WorldServer) this.world).addEntitySerialized(entity);
            });
        }

    }

    @Override
    public abstract boolean isSpectator();

    @Override
    public boolean isSwimming() {
        return !this.abilities.isFlying && !this.isSpectator() && super.isSwimming();
    }

    public abstract boolean isCreative();

    @Override
    public boolean bU() {
        return !this.abilities.isFlying;
    }

    public Scoreboard getScoreboard() {
        return this.world.getScoreboard();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        IChatMutableComponent ichatmutablecomponent = ScoreboardTeam.a(this.getScoreboardTeam(), this.getDisplayName());

        return this.a(ichatmutablecomponent);
    }

    private IChatMutableComponent a(IChatMutableComponent ichatmutablecomponent) {
        String s = this.getProfile().getName();

        return ichatmutablecomponent.format((chatmodifier) -> {
            return chatmodifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/tell " + s + " ")).setChatHoverable(this.ca()).setInsertion(s);
        });
    }

    @Override
    public String getName() {
        return this.getProfile().getName();
    }

    @Override
    public float b(EntityPose entitypose, EntitySize entitysize) {
        switch (entitypose) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                return 0.4F;
            case CROUCHING:
                return 1.27F;
            default:
                return 1.62F;
        }
    }

    @Override
    public void setAbsorptionHearts(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.getDataWatcher().set(EntityHuman.c, f);
    }

    @Override
    public float getAbsorptionHearts() {
        return (Float) this.getDataWatcher().get(EntityHuman.c);
    }

    public static UUID a(GameProfile gameprofile) {
        UUID uuid = gameprofile.getId();

        if (uuid == null) {
            uuid = getOfflineUUID(gameprofile.getName());
        }

        return uuid;
    }

    public static UUID getOfflineUUID(String s) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.inventory.items.size()) {
            this.inventory.setItem(i, itemstack);
            return true;
        } else {
            EnumItemSlot enumitemslot;

            if (i == 100 + EnumItemSlot.HEAD.b()) {
                enumitemslot = EnumItemSlot.HEAD;
            } else if (i == 100 + EnumItemSlot.CHEST.b()) {
                enumitemslot = EnumItemSlot.CHEST;
            } else if (i == 100 + EnumItemSlot.LEGS.b()) {
                enumitemslot = EnumItemSlot.LEGS;
            } else if (i == 100 + EnumItemSlot.FEET.b()) {
                enumitemslot = EnumItemSlot.FEET;
            } else {
                enumitemslot = null;
            }

            if (i == 98) {
                this.setSlot(EnumItemSlot.MAINHAND, itemstack);
                return true;
            } else if (i == 99) {
                this.setSlot(EnumItemSlot.OFFHAND, itemstack);
                return true;
            } else if (enumitemslot == null) {
                int j = i - 200;

                if (j >= 0 && j < this.enderChest.getSize()) {
                    this.enderChest.setItem(j, itemstack);
                    return true;
                } else {
                    return false;
                }
            } else {
                if (!itemstack.isEmpty()) {
                    if (!(itemstack.getItem() instanceof ItemArmor) && !(itemstack.getItem() instanceof ItemElytra)) {
                        if (enumitemslot != EnumItemSlot.HEAD) {
                            return false;
                        }
                    } else if (EntityInsentient.j(itemstack) != enumitemslot) {
                        return false;
                    }
                }

                this.inventory.setItem(enumitemslot.b() + this.inventory.items.size(), itemstack);
                return true;
            }
        }
    }

    @Override
    public void setFireTicks(int i) {
        super.setFireTicks(this.abilities.isInvulnerable ? Math.min(i, 1) : i);
    }

    @Override
    public EnumMainHand getMainHand() {
        return (Byte) this.datawatcher.get(EntityHuman.bj) == 0 ? EnumMainHand.LEFT : EnumMainHand.RIGHT;
    }

    public void a(EnumMainHand enummainhand) {
        this.datawatcher.set(EntityHuman.bj, (byte) (enummainhand == EnumMainHand.LEFT ? 0 : 1));
    }

    public NBTTagCompound getShoulderEntityLeft() {
        return (NBTTagCompound) this.datawatcher.get(EntityHuman.bk);
    }

    public void setShoulderEntityLeft(NBTTagCompound nbttagcompound) {
        this.datawatcher.set(EntityHuman.bk, nbttagcompound);
    }

    public NBTTagCompound getShoulderEntityRight() {
        return (NBTTagCompound) this.datawatcher.get(EntityHuman.bl);
    }

    public void setShoulderEntityRight(NBTTagCompound nbttagcompound) {
        this.datawatcher.set(EntityHuman.bl, nbttagcompound);
    }

    public float eQ() {
        return (float) (1.0D / this.b(GenericAttributes.ATTACK_SPEED) * 20.0D);
    }

    public float getAttackCooldown(float f) {
        return MathHelper.a(((float) this.at + f) / this.eQ(), 0.0F, 1.0F);
    }

    public void resetAttackCooldown() {
        this.at = 0;
    }

    public ItemCooldown getCooldownTracker() {
        return this.bM;
    }

    @Override
    protected float getBlockSpeedFactor() {
        return !this.abilities.isFlying && !this.isGliding() ? super.getBlockSpeedFactor() : 1.0F;
    }

    public float eT() {
        return (float) this.b(GenericAttributes.LUCK);
    }

    public boolean isCreativeAndOp() {
        return this.abilities.canInstantlyBuild && this.y() >= 2;
    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);

        return this.getEquipment(enumitemslot).isEmpty();
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        return (EntitySize) EntityHuman.b.getOrDefault(entitypose, EntityHuman.bh);
    }

    @Override
    public ImmutableList<EntityPose> ei() {
        return ImmutableList.of(EntityPose.STANDING, EntityPose.CROUCHING, EntityPose.SWIMMING);
    }

    @Override
    public ItemStack f(ItemStack itemstack) {
        if (!(itemstack.getItem() instanceof ItemProjectileWeapon)) {
            return ItemStack.b;
        } else {
            Predicate<ItemStack> predicate = ((ItemProjectileWeapon) itemstack.getItem()).e();
            ItemStack itemstack1 = ItemProjectileWeapon.a((EntityLiving) this, predicate);

            if (!itemstack1.isEmpty()) {
                return itemstack1;
            } else {
                predicate = ((ItemProjectileWeapon) itemstack.getItem()).b();

                for (int i = 0; i < this.inventory.getSize(); ++i) {
                    ItemStack itemstack2 = this.inventory.getItem(i);

                    if (predicate.test(itemstack2)) {
                        return itemstack2;
                    }
                }

                return this.abilities.canInstantlyBuild ? new ItemStack(Items.ARROW) : ItemStack.b;
            }
        }
    }

    @Override
    public ItemStack a(World world, ItemStack itemstack) {
        this.getFoodData().a(itemstack.getItem(), itemstack);
        this.b(StatisticList.ITEM_USED.b(itemstack.getItem()));
        world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        if (this instanceof EntityPlayer) {
            CriterionTriggers.z.a((EntityPlayer) this, itemstack);
        }

        return super.a(world, itemstack);
    }

    @Override
    protected boolean b(IBlockData iblockdata) {
        return this.abilities.isFlying || super.b(iblockdata);
    }

    public static enum EnumBedResult {

        NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW(new ChatMessage("block.minecraft.bed.no_sleep")), TOO_FAR_AWAY(new ChatMessage("block.minecraft.bed.too_far_away")), OBSTRUCTED(new ChatMessage("block.minecraft.bed.obstructed")), OTHER_PROBLEM, NOT_SAFE(new ChatMessage("block.minecraft.bed.not_safe"));

        @Nullable
        private final IChatBaseComponent g;

        private EnumBedResult() {
            this.g = null;
        }

        private EnumBedResult(IChatBaseComponent ichatbasecomponent) {
            this.g = ichatbasecomponent;
        }

        @Nullable
        public IChatBaseComponent a() {
            return this.g;
        }
    }
}
