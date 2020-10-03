package net.minecraft.server;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason; // CraftBukkit

public abstract class EntityHorseAbstract extends EntityAnimal implements IInventoryListener, IJumpable, ISaddleable {

    private static final Predicate<EntityLiving> bw = (entityliving) -> {
        return entityliving instanceof EntityHorseAbstract && ((EntityHorseAbstract) entityliving).hasReproduced();
    };
    private static final PathfinderTargetCondition bx = (new PathfinderTargetCondition()).a(16.0D).a().b().c().a(EntityHorseAbstract.bw);
    private static final RecipeItemStack by = RecipeItemStack.a(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.getItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
    private static final DataWatcherObject<Byte> bz = DataWatcher.a(EntityHorseAbstract.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Optional<UUID>> bA = DataWatcher.a(EntityHorseAbstract.class, DataWatcherRegistry.o);
    private int bB;
    private int bC;
    private int bD;
    public int bo;
    public int bp;
    protected boolean bq;
    public InventorySubcontainer inventoryChest;
    protected int bs;
    protected float jumpPower;
    private boolean canSlide;
    private float bF;
    private float bG;
    private float bH;
    private float bI;
    private float bJ;
    private float bK;
    protected boolean bu = true;
    protected int bv;
    public int maxDomestication = 100; // CraftBukkit - store max domestication value

    protected EntityHorseAbstract(EntityTypes<? extends EntityHorseAbstract> entitytypes, World world) {
        super(entitytypes, world);
        this.G = 1.0F;
        this.loadChest();
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D));
        this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D, EntityHorseAbstract.class));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.7D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.eV();
    }

    protected void eV() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityHorseAbstract.bz, (byte) 0);
        this.datawatcher.register(EntityHorseAbstract.bA, Optional.empty());
    }

    protected boolean t(int i) {
        return ((Byte) this.datawatcher.get(EntityHorseAbstract.bz) & i) != 0;
    }

    protected void d(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityHorseAbstract.bz);

        if (flag) {
            this.datawatcher.set(EntityHorseAbstract.bz, (byte) (b0 | i));
        } else {
            this.datawatcher.set(EntityHorseAbstract.bz, (byte) (b0 & ~i));
        }

    }

    public boolean isTamed() {
        return this.t(2);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return (UUID) ((Optional) this.datawatcher.get(EntityHorseAbstract.bA)).orElse((Object) null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.datawatcher.set(EntityHorseAbstract.bA, Optional.ofNullable(uuid));
    }

    public boolean eY() {
        return this.bq;
    }

    public void setTamed(boolean flag) {
        this.d(2, flag);
    }

    public void v(boolean flag) {
        this.bq = flag;
    }

    @Override
    protected void x(float f) {
        if (f > 6.0F && this.eZ()) {
            this.x(false);
        }

    }

    public boolean eZ() {
        return this.t(16);
    }

    public boolean fa() {
        return this.t(32);
    }

    public boolean hasReproduced() {
        return this.t(8);
    }

    public void w(boolean flag) {
        this.d(8, flag);
    }

    @Override
    public boolean canSaddle() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }

    @Override
    public void saddle(@Nullable SoundCategory soundcategory) {
        this.inventoryChest.setItem(0, new ItemStack(Items.SADDLE));
        if (soundcategory != null) {
            this.world.playSound((EntityHuman) null, (Entity) this, SoundEffects.ENTITY_HORSE_SADDLE, soundcategory, 0.5F, 1.0F);
        }

    }

    @Override
    public boolean hasSaddle() {
        return this.t(4);
    }

    public int getTemper() {
        return this.bs;
    }

    public void setTemper(int i) {
        this.bs = i;
    }

    public int v(int i) {
        int j = MathHelper.clamp(this.getTemper() + i, 0, this.getMaxDomestication());

        this.setTemper(j);
        return j;
    }

    @Override
    public boolean isCollidable() {
        return !this.isVehicle();
    }

    private void eL() {
        this.eO();
        if (!this.isSilent()) {
            SoundEffect soundeffect = this.fg();

            if (soundeffect != null) {
                this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), soundeffect, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
        }

    }

    @Override
    public boolean b(float f, float f1) {
        if (f > 1.0F) {
            this.playSound(SoundEffects.ENTITY_HORSE_LAND, 0.4F, 1.0F);
        }

        int i = this.e(f, f1);

        if (i <= 0) {
            return false;
        } else {
            this.damageEntity(DamageSource.FALL, (float) i);
            if (this.isVehicle()) {
                Iterator iterator = this.getAllPassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.damageEntity(DamageSource.FALL, (float) i);
                }
            }

            this.playBlockStepSound();
            return true;
        }
    }

    @Override
    protected int e(float f, float f1) {
        return MathHelper.f((f * 0.5F - 3.0F) * f1);
    }

    protected int getChestSlots() {
        return 2;
    }

    public void loadChest() {
        InventorySubcontainer inventorysubcontainer = this.inventoryChest;

        this.inventoryChest = new InventorySubcontainer(this.getChestSlots(), (org.bukkit.entity.AbstractHorse) this.getBukkitEntity()); // CraftBukkit
        if (inventorysubcontainer != null) {
            inventorysubcontainer.b((IInventoryListener) this);
            int i = Math.min(inventorysubcontainer.getSize(), this.inventoryChest.getSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventorysubcontainer.getItem(j);

                if (!itemstack.isEmpty()) {
                    this.inventoryChest.setItem(j, itemstack.cloneItemStack());
                }
            }
        }

        this.inventoryChest.a((IInventoryListener) this);
        this.fe();
    }

    protected void fe() {
        if (!this.world.isClientSide) {
            this.d(4, !this.inventoryChest.getItem(0).isEmpty());
        }
    }

    @Override
    public void a(IInventory iinventory) {
        boolean flag = this.hasSaddle();

        this.fe();
        if (this.ticksLived > 20 && !flag && this.hasSaddle()) {
            this.playSound(SoundEffects.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
        }

    }

    public double getJumpStrength() {
        return this.b(GenericAttributes.JUMP_STRENGTH);
    }

    @Nullable
    protected SoundEffect fg() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        if (this.random.nextInt(3) == 0) {
            this.eU();
        }

        return null;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        if (this.random.nextInt(10) == 0 && !this.isFrozen()) {
            this.eU();
        }

        return null;
    }

    @Nullable
    protected SoundEffect getSoundAngry() {
        this.eU();
        return null;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        if (!iblockdata.getMaterial().isLiquid()) {
            IBlockData iblockdata1 = this.world.getType(blockposition.up());
            SoundEffectType soundeffecttype = iblockdata.getStepSound();

            if (iblockdata1.a(Blocks.SNOW)) {
                soundeffecttype = iblockdata1.getStepSound();
            }

            if (this.isVehicle() && this.bu) {
                ++this.bv;
                if (this.bv > 5 && this.bv % 3 == 0) {
                    this.a(soundeffecttype);
                } else if (this.bv <= 5) {
                    this.playSound(SoundEffects.ENTITY_HORSE_STEP_WOOD, soundeffecttype.a() * 0.15F, soundeffecttype.b());
                }
            } else if (soundeffecttype == SoundEffectType.a) {
                this.playSound(SoundEffects.ENTITY_HORSE_STEP_WOOD, soundeffecttype.a() * 0.15F, soundeffecttype.b());
            } else {
                this.playSound(SoundEffects.ENTITY_HORSE_STEP, soundeffecttype.a() * 0.15F, soundeffecttype.b());
            }

        }
    }

    protected void a(SoundEffectType soundeffecttype) {
        this.playSound(SoundEffects.ENTITY_HORSE_GALLOP, soundeffecttype.a() * 0.15F, soundeffecttype.b());
    }

    public static AttributeProvider.Builder fi() {
        return EntityInsentient.p().a(GenericAttributes.JUMP_STRENGTH).a(GenericAttributes.MAX_HEALTH, 53.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.22499999403953552D);
    }

    @Override
    public int getMaxSpawnGroup() {
        return 6;
    }

    public int getMaxDomestication() {
        return this.maxDomestication; // CraftBukkit - return stored max domestication instead of 100
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public int D() {
        return 400;
    }

    public void f(EntityHuman entityhuman) {
        if (!this.world.isClientSide && (!this.isVehicle() || this.w(entityhuman)) && this.isTamed()) {
            entityhuman.openHorseInventory(this, this.inventoryChest);
        }

    }

    public EnumInteractionResult b(EntityHuman entityhuman, ItemStack itemstack) {
        boolean flag = this.c(entityhuman, itemstack);

        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        return this.world.isClientSide ? EnumInteractionResult.CONSUME : (flag ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS);
    }

    protected boolean c(EntityHuman entityhuman, ItemStack itemstack) {
        boolean flag = false;
        float f = 0.0F;
        short short0 = 0;
        byte b0 = 0;
        Item item = itemstack.getItem();

        if (item == Items.WHEAT) {
            f = 2.0F;
            short0 = 20;
            b0 = 3;
        } else if (item == Items.SUGAR) {
            f = 1.0F;
            short0 = 30;
            b0 = 3;
        } else if (item == Blocks.HAY_BLOCK.getItem()) {
            f = 20.0F;
            short0 = 180;
        } else if (item == Items.APPLE) {
            f = 3.0F;
            short0 = 60;
            b0 = 3;
        } else if (item == Items.GOLDEN_CARROT) {
            f = 4.0F;
            short0 = 60;
            b0 = 5;
            if (!this.world.isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.g(entityhuman);
            }
        } else if (item == Items.GOLDEN_APPLE || item == Items.ENCHANTED_GOLDEN_APPLE) {
            f = 10.0F;
            short0 = 240;
            b0 = 10;
            if (!this.world.isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.g(entityhuman);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f, RegainReason.EATING); // CraftBukkit
            flag = true;
        }

        if (this.isBaby() && short0 > 0) {
            this.world.addParticle(Particles.HAPPY_VILLAGER, this.d(1.0D), this.cE() + 0.5D, this.g(1.0D), 0.0D, 0.0D, 0.0D);
            if (!this.world.isClientSide) {
                this.setAge(short0);
            }

            flag = true;
        }

        if (b0 > 0 && (flag || !this.isTamed()) && this.getTemper() < this.getMaxDomestication()) {
            flag = true;
            if (!this.world.isClientSide) {
                this.v(b0);
            }
        }

        if (flag) {
            this.eL();
        }

        return flag;
    }

    protected void h(EntityHuman entityhuman) {
        this.x(false);
        this.setStanding(false);
        if (!this.world.isClientSide) {
            entityhuman.yaw = this.yaw;
            entityhuman.pitch = this.pitch;
            entityhuman.startRiding(this);
        }

    }

    @Override
    protected boolean isFrozen() {
        return super.isFrozen() && this.isVehicle() && this.hasSaddle() || this.eZ() || this.fa();
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityHorseAbstract.by.test(itemstack);
    }

    private void eM() {
        this.bo = 1;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.inventoryChest != null) {
            for (int i = 0; i < this.inventoryChest.getSize(); ++i) {
                ItemStack itemstack = this.inventoryChest.getItem(i);

                if (!itemstack.isEmpty() && !EnchantmentManager.shouldNotDrop(itemstack)) {
                    this.a(itemstack);
                }
            }

        }
    }

    @Override
    public void movementTick() {
        if (this.random.nextInt(200) == 0) {
            this.eM();
        }

        super.movementTick();
        if (!this.world.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTicks == 0) {
                this.heal(1.0F, RegainReason.REGEN); // CraftBukkit
            }

            if (this.fl()) {
                if (!this.eZ() && !this.isVehicle() && this.random.nextInt(300) == 0 && this.world.getType(this.getChunkCoordinates().down()).a(Blocks.GRASS_BLOCK)) {
                    this.x(true);
                }

                if (this.eZ() && ++this.bB > 50) {
                    this.bB = 0;
                    this.x(false);
                }
            }

            this.fk();
        }
    }

    protected void fk() {
        if (this.hasReproduced() && this.isBaby() && !this.eZ()) {
            EntityLiving entityliving = this.world.a(EntityHorseAbstract.class, EntityHorseAbstract.bx, this, this.locX(), this.locY(), this.locZ(), this.getBoundingBox().g(16.0D));

            if (entityliving != null && this.h((Entity) entityliving) > 4.0D) {
                this.navigation.a((Entity) entityliving, 0);
            }
        }

    }

    public boolean fl() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.bC > 0 && ++this.bC > 30) {
            this.bC = 0;
            this.d(64, false);
        }

        if ((this.cr() || this.doAITick()) && this.bD > 0 && ++this.bD > 20) {
            this.bD = 0;
            this.setStanding(false);
        }

        if (this.bo > 0 && ++this.bo > 8) {
            this.bo = 0;
        }

        if (this.bp > 0) {
            ++this.bp;
            if (this.bp > 300) {
                this.bp = 0;
            }
        }

        this.bG = this.bF;
        if (this.eZ()) {
            this.bF += (1.0F - this.bF) * 0.4F + 0.05F;
            if (this.bF > 1.0F) {
                this.bF = 1.0F;
            }
        } else {
            this.bF += (0.0F - this.bF) * 0.4F - 0.05F;
            if (this.bF < 0.0F) {
                this.bF = 0.0F;
            }
        }

        this.bI = this.bH;
        if (this.fa()) {
            this.bF = 0.0F;
            this.bG = this.bF;
            this.bH += (1.0F - this.bH) * 0.4F + 0.05F;
            if (this.bH > 1.0F) {
                this.bH = 1.0F;
            }
        } else {
            this.canSlide = false;
            this.bH += (0.8F * this.bH * this.bH * this.bH - this.bH) * 0.6F - 0.05F;
            if (this.bH < 0.0F) {
                this.bH = 0.0F;
            }
        }

        this.bK = this.bJ;
        if (this.t(64)) {
            this.bJ += (1.0F - this.bJ) * 0.7F + 0.05F;
            if (this.bJ > 1.0F) {
                this.bJ = 1.0F;
            }
        } else {
            this.bJ += (0.0F - this.bJ) * 0.7F - 0.05F;
            if (this.bJ < 0.0F) {
                this.bJ = 0.0F;
            }
        }

    }

    private void eO() {
        if (!this.world.isClientSide) {
            this.bC = 1;
            this.d(64, true);
        }

    }

    public void x(boolean flag) {
        this.d(16, flag);
    }

    public void setStanding(boolean flag) {
        if (flag) {
            this.x(false);
        }

        this.d(32, flag);
    }

    private void eU() {
        if (this.cr() || this.doAITick()) {
            this.bD = 1;
            this.setStanding(true);
        }

    }

    public void fm() {
        if (!this.fa()) {
            this.eU();
            SoundEffect soundeffect = this.getSoundAngry();

            if (soundeffect != null) {
                this.playSound(soundeffect, this.getSoundVolume(), this.dG());
            }
        }

    }

    public boolean i(EntityHuman entityhuman) {
        this.setOwnerUUID(entityhuman.getUniqueID());
        this.setTamed(true);
        if (entityhuman instanceof EntityPlayer) {
            CriterionTriggers.x.a((EntityPlayer) entityhuman, (EntityAnimal) this);
        }

        this.world.broadcastEntityEffect(this, (byte) 7);
        return true;
    }

    @Override
    public void g(Vec3D vec3d) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.er() && this.hasSaddle()) {
                EntityLiving entityliving = (EntityLiving) this.getRidingPassenger();

                this.yaw = entityliving.yaw;
                this.lastYaw = this.yaw;
                this.pitch = entityliving.pitch * 0.5F;
                this.setYawPitch(this.yaw, this.pitch);
                this.aA = this.yaw;
                this.aC = this.aA;
                float f = entityliving.aR * 0.5F;
                float f1 = entityliving.aT;

                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                    this.bv = 0;
                }

                if (this.onGround && this.jumpPower == 0.0F && this.fa() && !this.canSlide) {
                    f = 0.0F;
                    f1 = 0.0F;
                }

                if (this.jumpPower > 0.0F && !this.eY() && this.onGround) {
                    double d0 = this.getJumpStrength() * (double) this.jumpPower * (double) this.getBlockJumpFactor();
                    double d1;

                    if (this.hasEffect(MobEffects.JUMP)) {
                        d1 = d0 + (double) ((float) (this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vec3D vec3d1 = this.getMot();

                    this.setMot(vec3d1.x, d1, vec3d1.z);
                    this.v(true);
                    this.impulse = true;
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yaw * 0.017453292F);
                        float f3 = MathHelper.cos(this.yaw * 0.017453292F);

                        this.setMot(this.getMot().add((double) (-0.4F * f2 * this.jumpPower), 0.0D, (double) (0.4F * f3 * this.jumpPower)));
                    }

                    this.jumpPower = 0.0F;
                }

                this.aE = this.dM() * 0.1F;
                if (this.cr()) {
                    this.q((float) this.b(GenericAttributes.MOVEMENT_SPEED));
                    super.g(new Vec3D((double) f, vec3d.y, (double) f1));
                } else if (entityliving instanceof EntityHuman) {
                    this.setMot(Vec3D.ORIGIN);
                }

                if (this.onGround) {
                    this.jumpPower = 0.0F;
                    this.v(false);
                }

                this.a((EntityLiving) this, false);
            } else {
                this.aE = 0.02F;
                super.g(vec3d);
            }
        }
    }

    protected void fn() {
        this.playSound(SoundEffects.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("EatingHaystack", this.eZ());
        nbttagcompound.setBoolean("Bred", this.hasReproduced());
        nbttagcompound.setInt("Temper", this.getTemper());
        nbttagcompound.setBoolean("Tame", this.isTamed());
        if (this.getOwnerUUID() != null) {
            nbttagcompound.a("Owner", this.getOwnerUUID());
        }
        nbttagcompound.setInt("Bukkit.MaxDomestication", this.maxDomestication); // CraftBukkit

        if (!this.inventoryChest.getItem(0).isEmpty()) {
            nbttagcompound.set("SaddleItem", this.inventoryChest.getItem(0).save(new NBTTagCompound()));
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.x(nbttagcompound.getBoolean("EatingHaystack"));
        this.w(nbttagcompound.getBoolean("Bred"));
        this.setTemper(nbttagcompound.getInt("Temper"));
        this.setTamed(nbttagcompound.getBoolean("Tame"));
        UUID uuid;

        if (nbttagcompound.b("Owner")) {
            uuid = nbttagcompound.a("Owner");
        } else {
            String s = nbttagcompound.getString("Owner");

            uuid = NameReferencingFileConverter.a(this.getMinecraftServer(), s);
        }

        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxDomestication")) {
            this.maxDomestication = nbttagcompound.getInt("Bukkit.MaxDomestication");
        }
        // CraftBukkit end

        if (nbttagcompound.hasKeyOfType("SaddleItem", 10)) {
            ItemStack itemstack = ItemStack.a(nbttagcompound.getCompound("SaddleItem"));

            if (itemstack.getItem() == Items.SADDLE) {
                this.inventoryChest.setItem(0, itemstack);
            }
        }

        this.fe();
    }

    @Override
    public boolean mate(EntityAnimal entityanimal) {
        return false;
    }

    protected boolean fo() {
        return !this.isVehicle() && !this.isPassenger() && this.isTamed() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    @Nullable
    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return null;
    }

    protected void a(EntityAgeable entityageable, EntityHorseAbstract entityhorseabstract) {
        double d0 = this.c(GenericAttributes.MAX_HEALTH) + entityageable.c(GenericAttributes.MAX_HEALTH) + (double) this.fp();

        entityhorseabstract.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(d0 / 3.0D);
        double d1 = this.c(GenericAttributes.JUMP_STRENGTH) + entityageable.c(GenericAttributes.JUMP_STRENGTH) + this.fq();

        entityhorseabstract.getAttributeInstance(GenericAttributes.JUMP_STRENGTH).setValue(d1 / 3.0D);
        double d2 = this.c(GenericAttributes.MOVEMENT_SPEED) + entityageable.c(GenericAttributes.MOVEMENT_SPEED) + this.fr();

        entityhorseabstract.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(d2 / 3.0D);
    }

    @Override
    public boolean er() {
        return this.getRidingPassenger() instanceof EntityLiving;
    }

    @Override
    public boolean P_() {
        return this.hasSaddle();
    }

    @Override
    public void b(int i) {
        // CraftBukkit start
        float power;
        if (i >= 90) {
            power = 1.0F;
        } else {
            power = 0.4F + 0.4F * (float) i / 90.0F;
        }
        org.bukkit.event.entity.HorseJumpEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callHorseJumpEvent(this, power);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end
        this.canSlide = true;
        this.eU();
        this.fn();
    }

    @Override
    public void c() {}

    @Override
    public void k(Entity entity) {
        super.k(entity);
        if (entity instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) entity;

            this.aA = entityinsentient.aA;
        }

        if (this.bI > 0.0F) {
            float f = MathHelper.sin(this.aA * 0.017453292F);
            float f1 = MathHelper.cos(this.aA * 0.017453292F);
            float f2 = 0.7F * this.bI;
            float f3 = 0.15F * this.bI;

            entity.setPosition(this.locX() + (double) (f2 * f), this.locY() + this.bb() + entity.ba() + (double) f3, this.locZ() - (double) (f2 * f1));
            if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).aA = this.aA;
            }
        }

    }

    protected float fp() {
        return 15.0F + (float) this.random.nextInt(8) + (float) this.random.nextInt(9);
    }

    protected double fq() {
        return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
    }

    protected double fr() {
        return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.95F;
    }

    public boolean fs() {
        return false;
    }

    public boolean ft() {
        return !this.getEquipment(EnumItemSlot.CHEST).isEmpty();
    }

    public boolean l(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        int j = i - 400;

        if (j >= 0 && j < 2 && j < this.inventoryChest.getSize()) {
            if (j == 0 && itemstack.getItem() != Items.SADDLE) {
                return false;
            } else if (j == 1 && (!this.fs() || !this.l(itemstack))) {
                return false;
            } else {
                this.inventoryChest.setItem(j, itemstack);
                this.fe();
                return true;
            }
        } else {
            int k = i - 500 + 2;

            if (k >= 2 && k < this.inventoryChest.getSize()) {
                this.inventoryChest.setItem(k, itemstack);
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    @Override
    public Entity getRidingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    @Nullable
    private Vec3D a(Vec3D vec3d, EntityLiving entityliving) {
        double d0 = this.locX() + vec3d.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.locZ() + vec3d.z;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        UnmodifiableIterator unmodifiableiterator = entityliving.ei().iterator();

        while (unmodifiableiterator.hasNext()) {
            EntityPose entitypose = (EntityPose) unmodifiableiterator.next();

            blockposition_mutableblockposition.c(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while (true) {
                double d4 = this.world.h(blockposition_mutableblockposition);

                if ((double) blockposition_mutableblockposition.getY() + d4 > d3) {
                    break;
                }

                if (DismountUtil.a(d4)) {
                    AxisAlignedBB axisalignedbb = entityliving.f(entitypose);
                    Vec3D vec3d1 = new Vec3D(d0, (double) blockposition_mutableblockposition.getY() + d4, d2);

                    if (DismountUtil.a(this.world, entityliving, axisalignedbb.c(vec3d1))) {
                        entityliving.setPose(entitypose);
                        return vec3d1;
                    }
                }

                blockposition_mutableblockposition.c(EnumDirection.UP);
                if ((double) blockposition_mutableblockposition.getY() >= d3) {
                    break;
                }
            }
        }

        return null;
    }

    @Override
    public Vec3D b(EntityLiving entityliving) {
        Vec3D vec3d = a((double) this.getWidth(), (double) entityliving.getWidth(), this.yaw + (entityliving.getMainHand() == EnumMainHand.RIGHT ? 90.0F : -90.0F));
        Vec3D vec3d1 = this.a(vec3d, entityliving);

        if (vec3d1 != null) {
            return vec3d1;
        } else {
            Vec3D vec3d2 = a((double) this.getWidth(), (double) entityliving.getWidth(), this.yaw + (entityliving.getMainHand() == EnumMainHand.LEFT ? 90.0F : -90.0F));
            Vec3D vec3d3 = this.a(vec3d2, entityliving);

            return vec3d3 != null ? vec3d3 : this.getPositionVector();
        }
    }

    protected void eK() {}

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(0.2F);
        }

        this.eK();
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }
}
