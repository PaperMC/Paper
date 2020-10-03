package net.minecraft.server;

import java.util.Iterator;
import javax.annotation.Nullable;

public class EntityLlama extends EntityHorseChestedAbstract implements IRangedEntity {

    private static final RecipeItemStack bw = RecipeItemStack.a(Items.WHEAT, Blocks.HAY_BLOCK.getItem());
    private static final DataWatcherObject<Integer> bx = DataWatcher.a(EntityLlama.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> by = DataWatcher.a(EntityLlama.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> bz = DataWatcher.a(EntityLlama.class, DataWatcherRegistry.b);
    private boolean bA;
    @Nullable
    private EntityLlama bB;
    @Nullable
    private EntityLlama bC;

    public EntityLlama(EntityTypes<? extends EntityLlama> entitytypes, World world) {
        super(entitytypes, world);
    }

    public void setStrength(int i) {
        this.datawatcher.set(EntityLlama.bx, Math.max(1, Math.min(5, i)));
    }

    private void fE() {
        int i = this.random.nextFloat() < 0.04F ? 5 : 3;

        this.setStrength(1 + this.random.nextInt(i));
    }

    public int getStrength() {
        return (Integer) this.datawatcher.get(EntityLlama.bx);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Variant", this.getVariant());
        nbttagcompound.setInt("Strength", this.getStrength());
        if (!this.inventoryChest.getItem(1).isEmpty()) {
            nbttagcompound.set("DecorItem", this.inventoryChest.getItem(1).save(new NBTTagCompound()));
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.setStrength(nbttagcompound.getInt("Strength"));
        super.loadData(nbttagcompound);
        this.setVariant(nbttagcompound.getInt("Variant"));
        if (nbttagcompound.hasKeyOfType("DecorItem", 10)) {
            this.inventoryChest.setItem(1, ItemStack.a(nbttagcompound.getCompound("DecorItem")));
        }

        this.fe();
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
        this.goalSelector.a(2, new PathfinderGoalLlamaFollow(this, 2.0999999046325684D));
        this.goalSelector.a(3, new PathfinderGoalArrowAttack(this, 1.25D, 40, 20.0F));
        this.goalSelector.a(3, new PathfinderGoalPanic(this, 1.2D));
        this.goalSelector.a(4, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.7D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new EntityLlama.c(this));
        this.targetSelector.a(2, new EntityLlama.a(this));
    }

    public static AttributeProvider.Builder fw() {
        return eL().a(GenericAttributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityLlama.bx, 0);
        this.datawatcher.register(EntityLlama.by, -1);
        this.datawatcher.register(EntityLlama.bz, 0);
    }

    public int getVariant() {
        return MathHelper.clamp((Integer) this.datawatcher.get(EntityLlama.bz), 0, 3);
    }

    public void setVariant(int i) {
        this.datawatcher.set(EntityLlama.bz, i);
    }

    @Override
    protected int getChestSlots() {
        return this.isCarryingChest() ? 2 + 3 * this.eU() : super.getChestSlots();
    }

    @Override
    public void k(Entity entity) {
        if (this.w(entity)) {
            float f = MathHelper.cos(this.aA * 0.017453292F);
            float f1 = MathHelper.sin(this.aA * 0.017453292F);
            float f2 = 0.3F;

            entity.setPosition(this.locX() + (double) (0.3F * f1), this.locY() + this.bb() + entity.ba(), this.locZ() - (double) (0.3F * f));
        }
    }

    @Override
    public double bb() {
        return (double) this.getHeight() * 0.67D;
    }

    @Override
    public boolean er() {
        return false;
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityLlama.bw.test(itemstack);
    }

    @Override
    protected boolean c(EntityHuman entityhuman, ItemStack itemstack) {
        byte b0 = 0;
        byte b1 = 0;
        float f = 0.0F;
        boolean flag = false;
        Item item = itemstack.getItem();

        if (item == Items.WHEAT) {
            b0 = 10;
            b1 = 3;
            f = 2.0F;
        } else if (item == Blocks.HAY_BLOCK.getItem()) {
            b0 = 90;
            b1 = 6;
            f = 10.0F;
            if (this.isTamed() && this.getAge() == 0 && this.eP()) {
                flag = true;
                this.g(entityhuman);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && b0 > 0) {
            this.world.addParticle(Particles.HAPPY_VILLAGER, this.d(1.0D), this.cE() + 0.5D, this.g(1.0D), 0.0D, 0.0D, 0.0D);
            if (!this.world.isClientSide) {
                this.setAge(b0);
            }

            flag = true;
        }

        if (b1 > 0 && (flag || !this.isTamed()) && this.getTemper() < this.getMaxDomestication()) {
            flag = true;
            if (!this.world.isClientSide) {
                this.v(b1);
            }
        }

        if (flag && !this.isSilent()) {
            SoundEffect soundeffect = this.fg();

            if (soundeffect != null) {
                this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), this.fg(), this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
        }

        return flag;
    }

    @Override
    protected boolean isFrozen() {
        return this.dk() || this.eZ();
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.fE();
        int i;

        if (groupdataentity instanceof EntityLlama.b) {
            i = ((EntityLlama.b) groupdataentity).a;
        } else {
            i = this.random.nextInt(4);
            groupdataentity = new EntityLlama.b(i);
        }

        this.setVariant(i);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    @Override
    protected SoundEffect getSoundAngry() {
        return SoundEffects.ENTITY_LLAMA_ANGRY;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_LLAMA_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_LLAMA_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_LLAMA_DEATH;
    }

    @Nullable
    @Override
    protected SoundEffect fg() {
        return SoundEffects.ENTITY_LLAMA_EAT;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_LLAMA_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void eO() {
        this.playSound(SoundEffects.ENTITY_LLAMA_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public void fm() {
        SoundEffect soundeffect = this.getSoundAngry();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.dG());
        }

    }

    @Override
    public int eU() {
        return this.getStrength();
    }

    @Override
    public boolean fs() {
        return true;
    }

    @Override
    public boolean ft() {
        return !this.inventoryChest.getItem(1).isEmpty();
    }

    @Override
    public boolean l(ItemStack itemstack) {
        Item item = itemstack.getItem();

        return TagsItem.CARPETS.isTagged(item);
    }

    @Override
    public boolean canSaddle() {
        return false;
    }

    @Override
    public void a(IInventory iinventory) {
        EnumColor enumcolor = this.fy();

        super.a(iinventory);
        EnumColor enumcolor1 = this.fy();

        if (this.ticksLived > 20 && enumcolor1 != null && enumcolor1 != enumcolor) {
            this.playSound(SoundEffects.ENTITY_LLAMA_SWAG, 0.5F, 1.0F);
        }

    }

    @Override
    protected void fe() {
        if (!this.world.isClientSide) {
            super.fe();
            this.a(m(this.inventoryChest.getItem(1)));
        }
    }

    private void a(@Nullable EnumColor enumcolor) {
        this.datawatcher.set(EntityLlama.by, enumcolor == null ? -1 : enumcolor.getColorIndex());
    }

    @Nullable
    private static EnumColor m(ItemStack itemstack) {
        Block block = Block.asBlock(itemstack.getItem());

        return block instanceof BlockCarpet ? ((BlockCarpet) block).c() : null;
    }

    @Nullable
    public EnumColor fy() {
        int i = (Integer) this.datawatcher.get(EntityLlama.by);

        return i == -1 ? null : EnumColor.fromColorIndex(i);
    }

    @Override
    public int getMaxDomestication() {
        return 30;
    }

    @Override
    public boolean mate(EntityAnimal entityanimal) {
        return entityanimal != this && entityanimal instanceof EntityLlama && this.fo() && ((EntityLlama) entityanimal).fo();
    }

    @Override
    public EntityLlama createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntityLlama entityllama = this.fz();

        this.a(entityageable, (EntityHorseAbstract) entityllama);
        EntityLlama entityllama1 = (EntityLlama) entityageable;
        int i = this.random.nextInt(Math.max(this.getStrength(), entityllama1.getStrength())) + 1;

        if (this.random.nextFloat() < 0.03F) {
            ++i;
        }

        entityllama.setStrength(i);
        entityllama.setVariant(this.random.nextBoolean() ? this.getVariant() : entityllama1.getVariant());
        return entityllama;
    }

    protected EntityLlama fz() {
        return (EntityLlama) EntityTypes.LLAMA.a(this.world);
    }

    private void i(EntityLiving entityliving) {
        EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
        double d0 = entityliving.locX() - this.locX();
        double d1 = entityliving.e(0.3333333333333333D) - entityllamaspit.locY();
        double d2 = entityliving.locZ() - this.locZ();
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

        entityllamaspit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        if (!this.isSilent()) {
            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        this.world.addEntity(entityllamaspit);
        this.bA = true;
    }

    private void A(boolean flag) {
        this.bA = flag;
    }

    @Override
    public boolean b(float f, float f1) {
        int i = this.e(f, f1);

        if (i <= 0) {
            return false;
        } else {
            if (f >= 6.0F) {
                this.damageEntity(DamageSource.FALL, (float) i);
                if (this.isVehicle()) {
                    Iterator iterator = this.getAllPassengers().iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        entity.damageEntity(DamageSource.FALL, (float) i);
                    }
                }
            }

            this.playBlockStepSound();
            return true;
        }
    }

    public void fA() {
        if (this.bB != null) {
            this.bB.bC = null;
        }

        this.bB = null;
    }

    public void a(EntityLlama entityllama) {
        this.bB = entityllama;
        this.bB.bC = this;
    }

    public boolean fB() {
        return this.bC != null;
    }

    public boolean fC() {
        return this.bB != null;
    }

    @Nullable
    public EntityLlama fD() {
        return this.bB;
    }

    @Override
    protected double eJ() {
        return 2.0D;
    }

    @Override
    protected void fk() {
        if (!this.fC() && this.isBaby()) {
            super.fk();
        }

    }

    @Override
    public boolean fl() {
        return false;
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        this.i(entityliving);
    }

    static class a extends PathfinderGoalNearestAttackableTarget<EntityWolf> {

        public a(EntityLlama entityllama) {
            super(entityllama, EntityWolf.class, 16, false, true, (entityliving) -> {
                return !((EntityWolf) entityliving).isTamed();
            });
        }

        @Override
        protected double k() {
            return super.k() * 0.25D;
        }
    }

    static class c extends PathfinderGoalHurtByTarget {

        public c(EntityLlama entityllama) {
            super(entityllama);
        }

        @Override
        public boolean b() {
            if (this.e instanceof EntityLlama) {
                EntityLlama entityllama = (EntityLlama) this.e;

                if (entityllama.bA) {
                    entityllama.A(false);
                    return false;
                }
            }

            return super.b();
        }
    }

    static class b extends EntityAgeable.a {

        public final int a;

        private b(int i) {
            super(true);
            this.a = i;
        }
    }
}
