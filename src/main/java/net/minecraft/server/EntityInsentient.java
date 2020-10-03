package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

public abstract class EntityInsentient extends EntityLiving {

    private static final DataWatcherObject<Byte> b = DataWatcher.a(EntityInsentient.class, DataWatcherRegistry.a);
    public int e;
    protected int f;
    protected ControllerLook lookController;
    protected ControllerMove moveController;
    protected ControllerJump bi;
    private final EntityAIBodyControl c;
    protected NavigationAbstract navigation;
    public PathfinderGoalSelector goalSelector;
    public PathfinderGoalSelector targetSelector;
    private EntityLiving goalTarget;
    private final EntitySenses bo;
    private final NonNullList<ItemStack> bp;
    public final float[] dropChanceHand;
    private final NonNullList<ItemStack> bq;
    public final float[] dropChanceArmor;
    private boolean canPickUpLoot;
    public boolean persistent;
    private final Map<PathType, Float> bt;
    public MinecraftKey lootTableKey;
    public long lootTableSeed;
    @Nullable
    private Entity leashHolder;
    private int bx;
    @Nullable
    private NBTTagCompound by;
    private BlockPosition bz;
    private float bA;

    protected EntityInsentient(EntityTypes<? extends EntityInsentient> entitytypes, World world) {
        super(entitytypes, world);
        this.bp = NonNullList.a(2, ItemStack.b);
        this.dropChanceHand = new float[2];
        this.bq = NonNullList.a(4, ItemStack.b);
        this.dropChanceArmor = new float[4];
        this.bt = Maps.newEnumMap(PathType.class);
        this.bz = BlockPosition.ZERO;
        this.bA = -1.0F;
        this.goalSelector = new PathfinderGoalSelector(world.getMethodProfilerSupplier());
        this.targetSelector = new PathfinderGoalSelector(world.getMethodProfilerSupplier());
        this.lookController = new ControllerLook(this);
        this.moveController = new ControllerMove(this);
        this.bi = new ControllerJump(this);
        this.c = this.r();
        this.navigation = this.b(world);
        this.bo = new EntitySenses(this);
        Arrays.fill(this.dropChanceArmor, 0.085F);
        Arrays.fill(this.dropChanceHand, 0.085F);
        if (world != null && !world.isClientSide) {
            this.initPathfinder();
        }

    }

    protected void initPathfinder() {}

    public static AttributeProvider.Builder p() {
        return EntityLiving.cK().a(GenericAttributes.FOLLOW_RANGE, 16.0D).a(GenericAttributes.ATTACK_KNOCKBACK);
    }

    protected NavigationAbstract b(World world) {
        return new Navigation(this, world);
    }

    protected boolean q() {
        return false;
    }

    public float a(PathType pathtype) {
        EntityInsentient entityinsentient;

        if (this.getVehicle() instanceof EntityInsentient && ((EntityInsentient) this.getVehicle()).q()) {
            entityinsentient = (EntityInsentient) this.getVehicle();
        } else {
            entityinsentient = this;
        }

        Float ofloat = (Float) entityinsentient.bt.get(pathtype);

        return ofloat == null ? pathtype.a() : ofloat;
    }

    public void a(PathType pathtype, float f) {
        this.bt.put(pathtype, f);
    }

    public boolean b(PathType pathtype) {
        return pathtype != PathType.DANGER_FIRE && pathtype != PathType.DANGER_CACTUS && pathtype != PathType.DANGER_OTHER && pathtype != PathType.WALKABLE_DOOR;
    }

    protected EntityAIBodyControl r() {
        return new EntityAIBodyControl(this);
    }

    public ControllerLook getControllerLook() {
        return this.lookController;
    }

    public ControllerMove getControllerMove() {
        if (this.isPassenger() && this.getVehicle() instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) this.getVehicle();

            return entityinsentient.getControllerMove();
        } else {
            return this.moveController;
        }
    }

    public ControllerJump getControllerJump() {
        return this.bi;
    }

    public NavigationAbstract getNavigation() {
        if (this.isPassenger() && this.getVehicle() instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) this.getVehicle();

            return entityinsentient.getNavigation();
        } else {
            return this.navigation;
        }
    }

    public EntitySenses getEntitySenses() {
        return this.bo;
    }

    @Nullable
    public EntityLiving getGoalTarget() {
        return this.goalTarget;
    }

    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        this.goalTarget = entityliving;
    }

    @Override
    public boolean a(EntityTypes<?> entitytypes) {
        return entitytypes != EntityTypes.GHAST;
    }

    public boolean a(ItemProjectileWeapon itemprojectileweapon) {
        return false;
    }

    public void blockEaten() {}

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityInsentient.b, (byte) 0);
    }

    public int D() {
        return 80;
    }

    public void F() {
        SoundEffect soundeffect = this.getSoundAmbient();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.dG());
        }

    }

    @Override
    public void entityBaseTick() {
        super.entityBaseTick();
        this.world.getMethodProfiler().enter("mobBaseTick");
        if (this.isAlive() && this.random.nextInt(1000) < this.e++) {
            this.m();
            this.F();
        }

        this.world.getMethodProfiler().exit();
    }

    @Override
    protected void c(DamageSource damagesource) {
        this.m();
        super.c(damagesource);
    }

    private void m() {
        this.e = -this.D();
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        if (this.f > 0) {
            int i = this.f;

            int j;

            for (j = 0; j < this.bq.size(); ++j) {
                if (!((ItemStack) this.bq.get(j)).isEmpty() && this.dropChanceArmor[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            for (j = 0; j < this.bp.size(); ++j) {
                if (!((ItemStack) this.bp.get(j)).isEmpty() && this.dropChanceHand[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            return i;
        } else {
            return this.f;
        }
    }

    public void doSpawnEffect() {
        if (this.world.isClientSide) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                double d3 = 10.0D;

                this.world.addParticle(Particles.POOF, this.c(1.0D) - d0 * 10.0D, this.cE() - d1 * 10.0D, this.g(1.0D) - d2 * 10.0D, d0, d1, d2);
            }
        } else {
            this.world.broadcastEntityEffect(this, (byte) 20);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClientSide) {
            this.eA();
            if (this.ticksLived % 5 == 0) {
                this.H();
            }
        }

    }

    protected void H() {
        boolean flag = !(this.getRidingPassenger() instanceof EntityInsentient);
        boolean flag1 = !(this.getVehicle() instanceof EntityBoat);

        this.goalSelector.a(PathfinderGoal.Type.MOVE, flag);
        this.goalSelector.a(PathfinderGoal.Type.JUMP, flag && flag1);
        this.goalSelector.a(PathfinderGoal.Type.LOOK, flag);
    }

    @Override
    protected float f(float f, float f1) {
        this.c.a();
        return f1;
    }

    @Nullable
    protected SoundEffect getSoundAmbient() {
        return null;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("CanPickUpLoot", this.canPickupLoot());
        nbttagcompound.setBoolean("PersistenceRequired", this.persistent);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.bq.iterator(); iterator.hasNext(); nbttaglist.add(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.save(nbttagcompound1);
            }
        }

        nbttagcompound.set("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.bp.iterator(); iterator1.hasNext(); nbttaglist1.add(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.save(nbttagcompound2);
            }
        }

        nbttagcompound.set("HandItems", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        float[] afloat = this.dropChanceArmor;
        int i = afloat.length;

        int j;

        for (j = 0; j < i; ++j) {
            float f = afloat[j];

            nbttaglist2.add(NBTTagFloat.a(f));
        }

        nbttagcompound.set("ArmorDropChances", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        float[] afloat1 = this.dropChanceHand;

        j = afloat1.length;

        for (int k = 0; k < j; ++k) {
            float f1 = afloat1[k];

            nbttaglist3.add(NBTTagFloat.a(f1));
        }

        nbttagcompound.set("HandDropChances", nbttaglist3);
        if (this.leashHolder != null) {
            nbttagcompound2 = new NBTTagCompound();
            if (this.leashHolder instanceof EntityLiving) {
                UUID uuid = this.leashHolder.getUniqueID();

                nbttagcompound2.a("UUID", uuid);
            } else if (this.leashHolder instanceof EntityHanging) {
                BlockPosition blockposition = ((EntityHanging) this.leashHolder).getBlockPosition();

                nbttagcompound2.setInt("X", blockposition.getX());
                nbttagcompound2.setInt("Y", blockposition.getY());
                nbttagcompound2.setInt("Z", blockposition.getZ());
            }

            nbttagcompound.set("Leash", nbttagcompound2);
        } else if (this.by != null) {
            nbttagcompound.set("Leash", this.by.clone());
        }

        nbttagcompound.setBoolean("LeftHanded", this.isLeftHanded());
        if (this.lootTableKey != null) {
            nbttagcompound.setString("DeathLootTable", this.lootTableKey.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("DeathLootTableSeed", this.lootTableSeed);
            }
        }

        if (this.isNoAI()) {
            nbttagcompound.setBoolean("NoAI", this.isNoAI());
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("CanPickUpLoot", 1)) {
            this.setCanPickupLoot(nbttagcompound.getBoolean("CanPickUpLoot"));
        }

        this.persistent = nbttagcompound.getBoolean("PersistenceRequired");
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKeyOfType("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.getList("ArmorItems", 10);

            for (i = 0; i < this.bq.size(); ++i) {
                this.bq.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("HandItems", 9)) {
            nbttaglist = nbttagcompound.getList("HandItems", 10);

            for (i = 0; i < this.bp.size(); ++i) {
                this.bp.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.getList("ArmorDropChances", 5);

            for (i = 0; i < nbttaglist.size(); ++i) {
                this.dropChanceArmor[i] = nbttaglist.i(i);
            }
        }

        if (nbttagcompound.hasKeyOfType("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.getList("HandDropChances", 5);

            for (i = 0; i < nbttaglist.size(); ++i) {
                this.dropChanceHand[i] = nbttaglist.i(i);
            }
        }

        if (nbttagcompound.hasKeyOfType("Leash", 10)) {
            this.by = nbttagcompound.getCompound("Leash");
        }

        this.setLeftHanded(nbttagcompound.getBoolean("LeftHanded"));
        if (nbttagcompound.hasKeyOfType("DeathLootTable", 8)) {
            this.lootTableKey = new MinecraftKey(nbttagcompound.getString("DeathLootTable"));
            this.lootTableSeed = nbttagcompound.getLong("DeathLootTableSeed");
        }

        this.setNoAI(nbttagcompound.getBoolean("NoAI"));
    }

    @Override
    protected void a(DamageSource damagesource, boolean flag) {
        super.a(damagesource, flag);
        this.lootTableKey = null;
    }

    @Override
    protected LootTableInfo.Builder a(boolean flag, DamageSource damagesource) {
        return super.a(flag, damagesource).a(this.lootTableSeed, this.random);
    }

    @Override
    public final MinecraftKey do_() {
        return this.lootTableKey == null ? this.getDefaultLootTable() : this.lootTableKey;
    }

    protected MinecraftKey getDefaultLootTable() {
        return super.do_();
    }

    public void t(float f) {
        this.aT = f;
    }

    public void u(float f) {
        this.aS = f;
    }

    public void v(float f) {
        this.aR = f;
    }

    @Override
    public void q(float f) {
        super.q(f);
        this.t(f);
    }

    @Override
    public void movementTick() {
        super.movementTick();
        this.world.getMethodProfiler().enter("looting");
        if (!this.world.isClientSide && this.canPickupLoot() && this.isAlive() && !this.killed && this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            List<EntityItem> list = this.world.a(EntityItem.class, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                if (!entityitem.dead && !entityitem.getItemStack().isEmpty() && !entityitem.p() && this.i(entityitem.getItemStack())) {
                    this.b(entityitem);
                }
            }
        }

        this.world.getMethodProfiler().exit();
    }

    protected void b(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();

        if (this.g(itemstack)) {
            this.a(entityitem);
            this.receive(entityitem, itemstack.getCount());
            entityitem.die();
        }

    }

    public boolean g(ItemStack itemstack) {
        EnumItemSlot enumitemslot = j(itemstack);
        ItemStack itemstack1 = this.getEquipment(enumitemslot);
        boolean flag = this.a(itemstack, itemstack1);

        if (flag && this.canPickup(itemstack)) {
            double d0 = (double) this.e(enumitemslot);

            if (!itemstack1.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
                this.a(itemstack1);
            }

            this.b(enumitemslot, itemstack);
            this.b(itemstack);
            return true;
        } else {
            return false;
        }
    }

    protected void b(EnumItemSlot enumitemslot, ItemStack itemstack) {
        this.setSlot(enumitemslot, itemstack);
        this.d(enumitemslot);
        this.persistent = true;
    }

    public void d(EnumItemSlot enumitemslot) {
        switch (enumitemslot.a()) {
            case HAND:
                this.dropChanceHand[enumitemslot.b()] = 2.0F;
                break;
            case ARMOR:
                this.dropChanceArmor[enumitemslot.b()] = 2.0F;
        }

    }

    protected boolean a(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack1.isEmpty()) {
            return true;
        } else if (itemstack.getItem() instanceof ItemSword) {
            if (!(itemstack1.getItem() instanceof ItemSword)) {
                return true;
            } else {
                ItemSword itemsword = (ItemSword) itemstack.getItem();
                ItemSword itemsword1 = (ItemSword) itemstack1.getItem();

                return itemsword.f() != itemsword1.f() ? itemsword.f() > itemsword1.f() : this.b(itemstack, itemstack1);
            }
        } else if (itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow) {
            return this.b(itemstack, itemstack1);
        } else if (itemstack.getItem() instanceof ItemCrossbow && itemstack1.getItem() instanceof ItemCrossbow) {
            return this.b(itemstack, itemstack1);
        } else if (itemstack.getItem() instanceof ItemArmor) {
            if (EnchantmentManager.d(itemstack1)) {
                return false;
            } else if (!(itemstack1.getItem() instanceof ItemArmor)) {
                return true;
            } else {
                ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
                ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();

                return itemarmor.e() != itemarmor1.e() ? itemarmor.e() > itemarmor1.e() : (itemarmor.f() != itemarmor1.f() ? itemarmor.f() > itemarmor1.f() : this.b(itemstack, itemstack1));
            }
        } else {
            if (itemstack.getItem() instanceof ItemTool) {
                if (itemstack1.getItem() instanceof ItemBlock) {
                    return true;
                }

                if (itemstack1.getItem() instanceof ItemTool) {
                    ItemTool itemtool = (ItemTool) itemstack.getItem();
                    ItemTool itemtool1 = (ItemTool) itemstack1.getItem();

                    if (itemtool.d() != itemtool1.d()) {
                        return itemtool.d() > itemtool1.d();
                    }

                    return this.b(itemstack, itemstack1);
                }
            }

            return false;
        }
    }

    public boolean b(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getDamage() >= itemstack1.getDamage() && (!itemstack.hasTag() || itemstack1.hasTag()) ? (itemstack.hasTag() && itemstack1.hasTag() ? itemstack.getTag().getKeys().stream().anyMatch((s) -> {
            return !s.equals("Damage");
        }) && !itemstack1.getTag().getKeys().stream().anyMatch((s) -> {
            return !s.equals("Damage");
        }) : false) : true;
    }

    public boolean canPickup(ItemStack itemstack) {
        return true;
    }

    public boolean i(ItemStack itemstack) {
        return this.canPickup(itemstack);
    }

    public boolean isTypeNotPersistent(double d0) {
        return true;
    }

    public boolean isSpecialPersistence() {
        return this.isPassenger();
    }

    protected boolean L() {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.L()) {
            this.die();
        } else if (!this.isPersistent() && !this.isSpecialPersistence()) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

            if (entityhuman != null) {
                double d0 = entityhuman.h(this);
                int i = this.getEntityType().e().f();
                int j = i * i;

                if (d0 > (double) j && this.isTypeNotPersistent(d0)) {
                    this.die();
                }

                int k = this.getEntityType().e().g();
                int l = k * k;

                if (this.ticksFarFromPlayer > 600 && this.random.nextInt(800) == 0 && d0 > (double) l && this.isTypeNotPersistent(d0)) {
                    this.die();
                } else if (d0 < (double) l) {
                    this.ticksFarFromPlayer = 0;
                }
            }

        } else {
            this.ticksFarFromPlayer = 0;
        }
    }

    @Override
    protected final void doTick() {
        ++this.ticksFarFromPlayer;
        this.world.getMethodProfiler().enter("sensing");
        this.bo.a();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("targetSelector");
        this.targetSelector.doTick();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("goalSelector");
        this.goalSelector.doTick();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("navigation");
        this.navigation.c();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("mob tick");
        this.mobTick();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("controls");
        this.world.getMethodProfiler().enter("move");
        this.moveController.a();
        this.world.getMethodProfiler().exitEnter("look");
        this.lookController.a();
        this.world.getMethodProfiler().exitEnter("jump");
        this.bi.b();
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().exit();
        this.M();
    }

    protected void M() {
        PacketDebug.a(this.world, this, this.goalSelector);
    }

    protected void mobTick() {}

    public int O() {
        return 40;
    }

    public int eo() {
        return 75;
    }

    public int ep() {
        return 10;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.locX() - this.locX();
        double d1 = entity.locZ() - this.locZ();
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = entityliving.getHeadY() - this.getHeadY();
        } else {
            d2 = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D - this.getHeadY();
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (MathHelper.d(d1, d0) * 57.2957763671875D) - 90.0F;
        float f3 = (float) (-(MathHelper.d(d2, d3) * 57.2957763671875D));

        this.pitch = this.a(this.pitch, f3, f1);
        this.yaw = this.a(this.yaw, f2, f);
    }

    private float a(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public static boolean a(EntityTypes<? extends EntityInsentient> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        BlockPosition blockposition1 = blockposition.down();

        return enummobspawn == EnumMobSpawn.SPAWNER || generatoraccess.getType(blockposition1).a((IBlockAccess) generatoraccess, blockposition1, entitytypes);
    }

    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        return true;
    }

    public boolean a(IWorldReader iworldreader) {
        return !iworldreader.containsLiquid(this.getBoundingBox()) && iworldreader.j((Entity) this);
    }

    public int getMaxSpawnGroup() {
        return 4;
    }

    public boolean c(int i) {
        return false;
    }

    @Override
    public int bO() {
        if (this.getGoalTarget() == null) {
            return 3;
        } else {
            int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);

            i -= (3 - this.world.getDifficulty().a()) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    @Override
    public Iterable<ItemStack> bm() {
        return this.bp;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.bq;
    }

    @Override
    public ItemStack getEquipment(EnumItemSlot enumitemslot) {
        switch (enumitemslot.a()) {
            case HAND:
                return (ItemStack) this.bp.get(enumitemslot.b());
            case ARMOR:
                return (ItemStack) this.bq.get(enumitemslot.b());
            default:
                return ItemStack.b;
        }
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.a()) {
            case HAND:
                this.bp.set(enumitemslot.b(), itemstack);
                break;
            case ARMOR:
                this.bq.set(enumitemslot.b(), itemstack);
        }

    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropDeathLoot(damagesource, i, flag);
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int j = aenumitemslot.length;

        for (int k = 0; k < j; ++k) {
            EnumItemSlot enumitemslot = aenumitemslot[k];
            ItemStack itemstack = this.getEquipment(enumitemslot);
            float f = this.e(enumitemslot);
            boolean flag1 = f > 1.0F;

            if (!itemstack.isEmpty() && !EnchantmentManager.shouldNotDrop(itemstack) && (flag || flag1) && Math.max(this.random.nextFloat() - (float) i * 0.01F, 0.0F) < f) {
                if (!flag1 && itemstack.e()) {
                    itemstack.setDamage(itemstack.h() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.h() - 3, 1))));
                }

                this.a(itemstack);
                this.setSlot(enumitemslot, ItemStack.b);
            }
        }

    }

    protected float e(EnumItemSlot enumitemslot) {
        float f;

        switch (enumitemslot.a()) {
            case HAND:
                f = this.dropChanceHand[enumitemslot.b()];
                break;
            case ARMOR:
                f = this.dropChanceArmor[enumitemslot.b()];
                break;
            default:
                f = 0.0F;
        }

        return f;
    }

    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        if (this.random.nextFloat() < 0.15F * difficultydamagescaler.d()) {
            int i = this.random.nextInt(2);
            float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) {
                ++i;
            }

            boolean flag = true;
            EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EnumItemSlot enumitemslot = aenumitemslot[k];

                if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
                    ItemStack itemstack = this.getEquipment(enumitemslot);

                    if (!flag && this.random.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.isEmpty()) {
                        Item item = a(enumitemslot, i);

                        if (item != null) {
                            this.setSlot(enumitemslot, new ItemStack(item));
                        }
                    }
                }
            }
        }

    }

    public static EnumItemSlot j(ItemStack itemstack) {
        Item item = itemstack.getItem();

        return item != Blocks.CARVED_PUMPKIN.getItem() && (!(item instanceof ItemBlock) || !(((ItemBlock) item).getBlock() instanceof BlockSkullAbstract)) ? (item instanceof ItemArmor ? ((ItemArmor) item).b() : (item == Items.ELYTRA ? EnumItemSlot.CHEST : (item == Items.SHIELD ? EnumItemSlot.OFFHAND : EnumItemSlot.MAINHAND))) : EnumItemSlot.HEAD;
    }

    @Nullable
    public static Item a(EnumItemSlot enumitemslot, int i) {
        switch (enumitemslot) {
            case HEAD:
                if (i == 0) {
                    return Items.LEATHER_HELMET;
                } else if (i == 1) {
                    return Items.GOLDEN_HELMET;
                } else if (i == 2) {
                    return Items.CHAINMAIL_HELMET;
                } else if (i == 3) {
                    return Items.IRON_HELMET;
                } else if (i == 4) {
                    return Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (i == 0) {
                    return Items.LEATHER_CHESTPLATE;
                } else if (i == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                } else if (i == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (i == 3) {
                    return Items.IRON_CHESTPLATE;
                } else if (i == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (i == 0) {
                    return Items.LEATHER_LEGGINGS;
                } else if (i == 1) {
                    return Items.GOLDEN_LEGGINGS;
                } else if (i == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (i == 3) {
                    return Items.IRON_LEGGINGS;
                } else if (i == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (i == 0) {
                    return Items.LEATHER_BOOTS;
                } else if (i == 1) {
                    return Items.GOLDEN_BOOTS;
                } else if (i == 2) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (i == 3) {
                    return Items.IRON_BOOTS;
                } else if (i == 4) {
                    return Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }

    protected void b(DifficultyDamageScaler difficultydamagescaler) {
        float f = difficultydamagescaler.d();

        this.w(f);
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
                this.a(f, enumitemslot);
            }
        }

    }

    protected void w(float f) {
        if (!this.getItemInMainHand().isEmpty() && this.random.nextFloat() < 0.25F * f) {
            this.setSlot(EnumItemSlot.MAINHAND, EnchantmentManager.a(this.random, this.getItemInMainHand(), (int) (5.0F + f * (float) this.random.nextInt(18)), false));
        }

    }

    protected void a(float f, EnumItemSlot enumitemslot) {
        ItemStack itemstack = this.getEquipment(enumitemslot);

        if (!itemstack.isEmpty() && this.random.nextFloat() < 0.5F * f) {
            this.setSlot(enumitemslot, EnchantmentManager.a(this.random, itemstack, (int) (5.0F + f * (float) this.random.nextInt(18)), false));
        }

    }

    @Nullable
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).addModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        if (this.random.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }

        return groupdataentity;
    }

    public boolean er() {
        return false;
    }

    public void setPersistent() {
        this.persistent = true;
    }

    public void a(EnumItemSlot enumitemslot, float f) {
        switch (enumitemslot.a()) {
            case HAND:
                this.dropChanceHand[enumitemslot.b()] = f;
                break;
            case ARMOR:
                this.dropChanceArmor[enumitemslot.b()] = f;
        }

    }

    public boolean canPickupLoot() {
        return this.canPickUpLoot;
    }

    public void setCanPickupLoot(boolean flag) {
        this.canPickUpLoot = flag;
    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = j(itemstack);

        return this.getEquipment(enumitemslot).isEmpty() && this.canPickupLoot();
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    @Override
    public final EnumInteractionResult a(EntityHuman entityhuman, EnumHand enumhand) {
        if (!this.isAlive()) {
            return EnumInteractionResult.PASS;
        } else if (this.getLeashHolder() == entityhuman) {
            this.unleash(true, !entityhuman.abilities.canInstantlyBuild);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            EnumInteractionResult enuminteractionresult = this.c(entityhuman, enumhand);

            if (enuminteractionresult.a()) {
                return enuminteractionresult;
            } else {
                enuminteractionresult = this.b(entityhuman, enumhand);
                return enuminteractionresult.a() ? enuminteractionresult : super.a(entityhuman, enumhand);
            }
        }
    }

    private EnumInteractionResult c(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.LEAD && this.a(entityhuman)) {
            this.setLeashHolder(entityhuman, true);
            itemstack.subtract(1);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            if (itemstack.getItem() == Items.NAME_TAG) {
                EnumInteractionResult enuminteractionresult = itemstack.a(entityhuman, (EntityLiving) this, enumhand);

                if (enuminteractionresult.a()) {
                    return enuminteractionresult;
                }
            }

            if (itemstack.getItem() instanceof ItemMonsterEgg) {
                if (this.world instanceof WorldServer) {
                    ItemMonsterEgg itemmonsteregg = (ItemMonsterEgg) itemstack.getItem();
                    Optional<EntityInsentient> optional = itemmonsteregg.a(entityhuman, this, this.getEntityType(), (WorldServer) this.world, this.getPositionVector(), itemstack);

                    optional.ifPresent((entityinsentient) -> {
                        this.a(entityhuman, entityinsentient);
                    });
                    return optional.isPresent() ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS;
                } else {
                    return EnumInteractionResult.CONSUME;
                }
            } else {
                return EnumInteractionResult.PASS;
            }
        }
    }

    protected void a(EntityHuman entityhuman, EntityInsentient entityinsentient) {}

    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        return EnumInteractionResult.PASS;
    }

    public boolean ev() {
        return this.a(this.getChunkCoordinates());
    }

    public boolean a(BlockPosition blockposition) {
        return this.bA == -1.0F ? true : this.bz.j(blockposition) < (double) (this.bA * this.bA);
    }

    public void a(BlockPosition blockposition, int i) {
        this.bz = blockposition;
        this.bA = (float) i;
    }

    public BlockPosition ew() {
        return this.bz;
    }

    public float ex() {
        return this.bA;
    }

    public boolean ez() {
        return this.bA != -1.0F;
    }

    @Nullable
    public <T extends EntityInsentient> T a(EntityTypes<T> entitytypes, boolean flag) {
        if (this.dead) {
            return null;
        } else {
            T t0 = (EntityInsentient) entitytypes.a(this.world);

            t0.u(this);
            t0.setBaby(this.isBaby());
            t0.setNoAI(this.isNoAI());
            if (this.hasCustomName()) {
                t0.setCustomName(this.getCustomName());
                t0.setCustomNameVisible(this.getCustomNameVisible());
            }

            if (this.isPersistent()) {
                t0.setPersistent();
            }

            t0.setInvulnerable(this.isInvulnerable());
            if (flag) {
                t0.setCanPickupLoot(this.canPickupLoot());
                EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
                int i = aenumitemslot.length;

                for (int j = 0; j < i; ++j) {
                    EnumItemSlot enumitemslot = aenumitemslot[j];
                    ItemStack itemstack = this.getEquipment(enumitemslot);

                    if (!itemstack.isEmpty()) {
                        t0.setSlot(enumitemslot, itemstack.cloneItemStack());
                        t0.a(enumitemslot, this.e(enumitemslot));
                        itemstack.setCount(0);
                    }
                }
            }

            this.world.addEntity(t0);
            if (this.isPassenger()) {
                Entity entity = this.getVehicle();

                this.stopRiding();
                t0.a(entity, true);
            }

            this.die();
            return t0;
        }
    }

    protected void eA() {
        if (this.by != null) {
            this.eI();
        }

        if (this.leashHolder != null) {
            if (!this.isAlive() || !this.leashHolder.isAlive()) {
                this.unleash(true, true);
            }

        }
    }

    public void unleash(boolean flag, boolean flag1) {
        if (this.leashHolder != null) {
            this.attachedToPlayer = false;
            if (!(this.leashHolder instanceof EntityHuman)) {
                this.leashHolder.attachedToPlayer = false;
            }

            this.leashHolder = null;
            this.by = null;
            if (!this.world.isClientSide && flag1) {
                this.a((IMaterial) Items.LEAD);
            }

            if (!this.world.isClientSide && flag && this.world instanceof WorldServer) {
                ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutAttachEntity(this, (Entity) null));
            }
        }

    }

    public boolean a(EntityHuman entityhuman) {
        return !this.isLeashed() && !(this instanceof IMonster);
    }

    public boolean isLeashed() {
        return this.leashHolder != null;
    }

    @Nullable
    public Entity getLeashHolder() {
        if (this.leashHolder == null && this.bx != 0 && this.world.isClientSide) {
            this.leashHolder = this.world.getEntity(this.bx);
        }

        return this.leashHolder;
    }

    public void setLeashHolder(Entity entity, boolean flag) {
        this.leashHolder = entity;
        this.by = null;
        this.attachedToPlayer = true;
        if (!(this.leashHolder instanceof EntityHuman)) {
            this.leashHolder.attachedToPlayer = true;
        }

        if (!this.world.isClientSide && flag && this.world instanceof WorldServer) {
            ((WorldServer) this.world).getChunkProvider().broadcast(this, new PacketPlayOutAttachEntity(this, this.leashHolder));
        }

        if (this.isPassenger()) {
            this.stopRiding();
        }

    }

    @Override
    public boolean a(Entity entity, boolean flag) {
        boolean flag1 = super.a(entity, flag);

        if (flag1 && this.isLeashed()) {
            this.unleash(true, true);
        }

        return flag1;
    }

    private void eI() {
        if (this.by != null && this.world instanceof WorldServer) {
            if (this.by.b("UUID")) {
                UUID uuid = this.by.a("UUID");
                Entity entity = ((WorldServer) this.world).getEntity(uuid);

                if (entity != null) {
                    this.setLeashHolder(entity, true);
                    return;
                }
            } else if (this.by.hasKeyOfType("X", 99) && this.by.hasKeyOfType("Y", 99) && this.by.hasKeyOfType("Z", 99)) {
                BlockPosition blockposition = new BlockPosition(this.by.getInt("X"), this.by.getInt("Y"), this.by.getInt("Z"));

                this.setLeashHolder(EntityLeash.a(this.world, blockposition), true);
                return;
            }

            if (this.ticksLived > 100) {
                this.a((IMaterial) Items.LEAD);
                this.by = null;
            }
        }

    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        EnumItemSlot enumitemslot;

        if (i == 98) {
            enumitemslot = EnumItemSlot.MAINHAND;
        } else if (i == 99) {
            enumitemslot = EnumItemSlot.OFFHAND;
        } else if (i == 100 + EnumItemSlot.HEAD.b()) {
            enumitemslot = EnumItemSlot.HEAD;
        } else if (i == 100 + EnumItemSlot.CHEST.b()) {
            enumitemslot = EnumItemSlot.CHEST;
        } else if (i == 100 + EnumItemSlot.LEGS.b()) {
            enumitemslot = EnumItemSlot.LEGS;
        } else {
            if (i != 100 + EnumItemSlot.FEET.b()) {
                return false;
            }

            enumitemslot = EnumItemSlot.FEET;
        }

        if (!itemstack.isEmpty() && !c(enumitemslot, itemstack) && enumitemslot != EnumItemSlot.HEAD) {
            return false;
        } else {
            this.setSlot(enumitemslot, itemstack);
            return true;
        }
    }

    @Override
    public boolean cr() {
        return this.er() && super.cr();
    }

    public static boolean c(EnumItemSlot enumitemslot, ItemStack itemstack) {
        EnumItemSlot enumitemslot1 = j(itemstack);

        return enumitemslot1 == enumitemslot || enumitemslot1 == EnumItemSlot.MAINHAND && enumitemslot == EnumItemSlot.OFFHAND || enumitemslot1 == EnumItemSlot.OFFHAND && enumitemslot == EnumItemSlot.MAINHAND;
    }

    @Override
    public boolean doAITick() {
        return super.doAITick() && !this.isNoAI();
    }

    public void setNoAI(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 1) : (byte) (b0 & -2));
    }

    public void setLeftHanded(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 2) : (byte) (b0 & -3));
    }

    public void setAggressive(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityInsentient.b);

        this.datawatcher.set(EntityInsentient.b, flag ? (byte) (b0 | 4) : (byte) (b0 & -5));
    }

    public boolean isNoAI() {
        return ((Byte) this.datawatcher.get(EntityInsentient.b) & 1) != 0;
    }

    public boolean isLeftHanded() {
        return ((Byte) this.datawatcher.get(EntityInsentient.b) & 2) != 0;
    }

    public boolean isAggressive() {
        return ((Byte) this.datawatcher.get(EntityInsentient.b) & 4) != 0;
    }

    public void setBaby(boolean flag) {}

    @Override
    public EnumMainHand getMainHand() {
        return this.isLeftHanded() ? EnumMainHand.LEFT : EnumMainHand.RIGHT;
    }

    @Override
    public boolean c(EntityLiving entityliving) {
        return entityliving.getEntityType() == EntityTypes.PLAYER && ((EntityHuman) entityliving).abilities.isInvulnerable ? false : super.c(entityliving);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        float f = (float) this.b(GenericAttributes.ATTACK_DAMAGE);
        float f1 = (float) this.b(GenericAttributes.ATTACK_KNOCKBACK);

        if (entity instanceof EntityLiving) {
            f += EnchantmentManager.a(this.getItemInMainHand(), ((EntityLiving) entity).getMonsterType());
            f1 += (float) EnchantmentManager.b((EntityLiving) this);
        }

        int i = EnchantmentManager.getFireAspectEnchantmentLevel(this);

        if (i > 0) {
            entity.setOnFire(i * 4);
        }

        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);

        if (flag) {
            if (f1 > 0.0F && entity instanceof EntityLiving) {
                ((EntityLiving) entity).a(f1 * 0.5F, (double) MathHelper.sin(this.yaw * 0.017453292F), (double) (-MathHelper.cos(this.yaw * 0.017453292F)));
                this.setMot(this.getMot().d(0.6D, 1.0D, 0.6D));
            }

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                this.a(entityhuman, this.getItemInMainHand(), entityhuman.isHandRaised() ? entityhuman.getActiveItem() : ItemStack.b);
            }

            this.a((EntityLiving) this, entity);
            this.z(entity);
        }

        return flag;
    }

    private void a(EntityHuman entityhuman, ItemStack itemstack, ItemStack itemstack1) {
        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentManager.getDigSpeedEnchantmentLevel(this) * 0.05F;

            if (this.random.nextFloat() < f) {
                entityhuman.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                this.world.broadcastEntityEffect(entityhuman, (byte) 30);
            }
        }

    }

    protected boolean eG() {
        if (this.world.isDay() && !this.world.isClientSide) {
            float f = this.aQ();
            BlockPosition blockposition = this.getVehicle() instanceof EntityBoat ? (new BlockPosition(this.locX(), (double) Math.round(this.locY()), this.locZ())).up() : new BlockPosition(this.locX(), (double) Math.round(this.locY()), this.locZ());

            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.e(blockposition)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void c(Tag<FluidType> tag) {
        if (this.getNavigation().r()) {
            super.c(tag);
        } else {
            this.setMot(this.getMot().add(0.0D, 0.3D, 0.0D));
        }

    }

    @Override
    protected void bM() {
        super.bM();
        this.unleash(true, false);
    }
}
