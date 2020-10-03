package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
// CraftBukkit end

public class EntityArmorStand extends EntityLiving {

    private static final Vector3f bj = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f bk = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f bl = new Vector3f(-10.0F, 0.0F, -10.0F);
    private static final Vector3f bm = new Vector3f(-15.0F, 0.0F, 10.0F);
    private static final Vector3f bn = new Vector3f(-1.0F, 0.0F, -1.0F);
    private static final Vector3f bo = new Vector3f(1.0F, 0.0F, 1.0F);
    private static final EntitySize bp = new EntitySize(0.0F, 0.0F, true);
    private static final EntitySize bq = EntityTypes.ARMOR_STAND.l().a(0.5F);
    public static final DataWatcherObject<Byte> b = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.a);
    public static final DataWatcherObject<Vector3f> c = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    public static final DataWatcherObject<Vector3f> d = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    public static final DataWatcherObject<Vector3f> e = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    public static final DataWatcherObject<Vector3f> f = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    public static final DataWatcherObject<Vector3f> g = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    public static final DataWatcherObject<Vector3f> bh = DataWatcher.a(EntityArmorStand.class, DataWatcherRegistry.k);
    private static final Predicate<Entity> br = (entity) -> {
        return entity instanceof EntityMinecartAbstract && ((EntityMinecartAbstract) entity).getMinecartType() == EntityMinecartAbstract.EnumMinecartType.RIDEABLE;
    };
    private final NonNullList<ItemStack> handItems;
    private final NonNullList<ItemStack> armorItems;
    private boolean armorStandInvisible;
    public long bi;
    public int disabledSlots;
    public Vector3f headPose;
    public Vector3f bodyPose;
    public Vector3f leftArmPose;
    public Vector3f rightArmPose;
    public Vector3f leftLegPose;
    public Vector3f rightLegPose;

    public EntityArmorStand(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
        this.handItems = NonNullList.a(2, ItemStack.b);
        this.armorItems = NonNullList.a(4, ItemStack.b);
        this.headPose = EntityArmorStand.bj;
        this.bodyPose = EntityArmorStand.bk;
        this.leftArmPose = EntityArmorStand.bl;
        this.rightArmPose = EntityArmorStand.bm;
        this.leftLegPose = EntityArmorStand.bn;
        this.rightLegPose = EntityArmorStand.bo;
        this.G = 0.0F;
    }

    public EntityArmorStand(World world, double d0, double d1, double d2) {
        this(EntityTypes.ARMOR_STAND, world);
        this.setPosition(d0, d1, d2);
    }

    // CraftBukkit start - SPIGOT-3607, SPIGOT-3637
    @Override
    public float getBukkitYaw() {
        return this.yaw;
    }
    // CraftBukkit end

    @Override
    public void updateSize() {
        double d0 = this.locX();
        double d1 = this.locY();
        double d2 = this.locZ();

        super.updateSize();
        this.setPosition(d0, d1, d2);
    }

    private boolean A() {
        return !this.isMarker() && !this.isNoGravity();
    }

    @Override
    public boolean doAITick() {
        return super.doAITick() && this.A();
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityArmorStand.b, (byte) 0);
        this.datawatcher.register(EntityArmorStand.c, EntityArmorStand.bj);
        this.datawatcher.register(EntityArmorStand.d, EntityArmorStand.bk);
        this.datawatcher.register(EntityArmorStand.e, EntityArmorStand.bl);
        this.datawatcher.register(EntityArmorStand.f, EntityArmorStand.bm);
        this.datawatcher.register(EntityArmorStand.g, EntityArmorStand.bn);
        this.datawatcher.register(EntityArmorStand.bh, EntityArmorStand.bo);
    }

    @Override
    public Iterable<ItemStack> bm() {
        return this.handItems;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.armorItems;
    }

    @Override
    public ItemStack getEquipment(EnumItemSlot enumitemslot) {
        switch (enumitemslot.a()) {
            case HAND:
                return (ItemStack) this.handItems.get(enumitemslot.b());
            case ARMOR:
                return (ItemStack) this.armorItems.get(enumitemslot.b());
            default:
                return ItemStack.b;
        }
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.a()) {
            case HAND:
                this.b(itemstack);
                this.handItems.set(enumitemslot.b(), itemstack);
                break;
            case ARMOR:
                this.b(itemstack);
                this.armorItems.set(enumitemslot.b(), itemstack);
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

        if (!itemstack.isEmpty() && !EntityInsentient.c(enumitemslot, itemstack) && enumitemslot != EnumItemSlot.HEAD) {
            return false;
        } else {
            this.setSlot(enumitemslot, itemstack);
            return true;
        }
    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);

        return this.getEquipment(enumitemslot).isEmpty() && !this.d(enumitemslot);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.armorItems.iterator(); iterator.hasNext(); nbttaglist.add(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.save(nbttagcompound1);
            }
        }

        nbttagcompound.set("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.handItems.iterator(); iterator1.hasNext(); nbttaglist1.add(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.save(nbttagcompound2);
            }
        }

        nbttagcompound.set("HandItems", nbttaglist1);
        nbttagcompound.setBoolean("Invisible", this.isInvisible());
        nbttagcompound.setBoolean("Small", this.isSmall());
        nbttagcompound.setBoolean("ShowArms", this.hasArms());
        nbttagcompound.setInt("DisabledSlots", this.disabledSlots);
        nbttagcompound.setBoolean("NoBasePlate", this.hasBasePlate());
        if (this.isMarker()) {
            nbttagcompound.setBoolean("Marker", this.isMarker());
        }

        nbttagcompound.set("Pose", this.B());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKeyOfType("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.getList("ArmorItems", 10);

            for (i = 0; i < this.armorItems.size(); ++i) {
                this.armorItems.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("HandItems", 9)) {
            nbttaglist = nbttagcompound.getList("HandItems", 10);

            for (i = 0; i < this.handItems.size(); ++i) {
                this.handItems.set(i, ItemStack.a(nbttaglist.getCompound(i)));
            }
        }

        this.setInvisible(nbttagcompound.getBoolean("Invisible"));
        this.setSmall(nbttagcompound.getBoolean("Small"));
        this.setArms(nbttagcompound.getBoolean("ShowArms"));
        this.disabledSlots = nbttagcompound.getInt("DisabledSlots");
        this.setBasePlate(nbttagcompound.getBoolean("NoBasePlate"));
        this.setMarker(nbttagcompound.getBoolean("Marker"));
        this.noclip = !this.A();
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Pose");

        this.g(nbttagcompound1);
    }

    private void g(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getList("Head", 5);

        this.setHeadPose(nbttaglist.isEmpty() ? EntityArmorStand.bj : new Vector3f(nbttaglist));
        NBTTagList nbttaglist1 = nbttagcompound.getList("Body", 5);

        this.setBodyPose(nbttaglist1.isEmpty() ? EntityArmorStand.bk : new Vector3f(nbttaglist1));
        NBTTagList nbttaglist2 = nbttagcompound.getList("LeftArm", 5);

        this.setLeftArmPose(nbttaglist2.isEmpty() ? EntityArmorStand.bl : new Vector3f(nbttaglist2));
        NBTTagList nbttaglist3 = nbttagcompound.getList("RightArm", 5);

        this.setRightArmPose(nbttaglist3.isEmpty() ? EntityArmorStand.bm : new Vector3f(nbttaglist3));
        NBTTagList nbttaglist4 = nbttagcompound.getList("LeftLeg", 5);

        this.setLeftLegPose(nbttaglist4.isEmpty() ? EntityArmorStand.bn : new Vector3f(nbttaglist4));
        NBTTagList nbttaglist5 = nbttagcompound.getList("RightLeg", 5);

        this.setRightLegPose(nbttaglist5.isEmpty() ? EntityArmorStand.bo : new Vector3f(nbttaglist5));
    }

    private NBTTagCompound B() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!EntityArmorStand.bj.equals(this.headPose)) {
            nbttagcompound.set("Head", this.headPose.a());
        }

        if (!EntityArmorStand.bk.equals(this.bodyPose)) {
            nbttagcompound.set("Body", this.bodyPose.a());
        }

        if (!EntityArmorStand.bl.equals(this.leftArmPose)) {
            nbttagcompound.set("LeftArm", this.leftArmPose.a());
        }

        if (!EntityArmorStand.bm.equals(this.rightArmPose)) {
            nbttagcompound.set("RightArm", this.rightArmPose.a());
        }

        if (!EntityArmorStand.bn.equals(this.leftLegPose)) {
            nbttagcompound.set("LeftLeg", this.leftLegPose.a());
        }

        if (!EntityArmorStand.bo.equals(this.rightLegPose)) {
            nbttagcompound.set("RightLeg", this.rightLegPose.a());
        }

        return nbttagcompound;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    protected void C(Entity entity) {}

    @Override
    protected void collideNearby() {
        List<Entity> list = this.world.getEntities(this, this.getBoundingBox(), EntityArmorStand.br);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (this.h(entity) <= 0.2D) {
                entity.collide(this);
            }
        }

    }

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!this.isMarker() && itemstack.getItem() != Items.NAME_TAG) {
            if (entityhuman.isSpectator()) {
                return EnumInteractionResult.SUCCESS;
            } else if (entityhuman.world.isClientSide) {
                return EnumInteractionResult.CONSUME;
            } else {
                EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);

                if (itemstack.isEmpty()) {
                    EnumItemSlot enumitemslot1 = this.i(vec3d);
                    EnumItemSlot enumitemslot2 = this.d(enumitemslot1) ? enumitemslot : enumitemslot1;

                    if (this.a(enumitemslot2) && this.a(entityhuman, enumitemslot2, itemstack, enumhand)) {
                        return EnumInteractionResult.SUCCESS;
                    }
                } else {
                    if (this.d(enumitemslot)) {
                        return EnumInteractionResult.FAIL;
                    }

                    if (enumitemslot.a() == EnumItemSlot.Function.HAND && !this.hasArms()) {
                        return EnumInteractionResult.FAIL;
                    }

                    if (this.a(entityhuman, enumitemslot, itemstack, enumhand)) {
                        return EnumInteractionResult.SUCCESS;
                    }
                }

                return EnumInteractionResult.PASS;
            }
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    private EnumItemSlot i(Vec3D vec3d) {
        EnumItemSlot enumitemslot = EnumItemSlot.MAINHAND;
        boolean flag = this.isSmall();
        double d0 = flag ? vec3d.y * 2.0D : vec3d.y;
        EnumItemSlot enumitemslot1 = EnumItemSlot.FEET;

        if (d0 >= 0.1D && d0 < 0.1D + (flag ? 0.8D : 0.45D) && this.a(enumitemslot1)) {
            enumitemslot = EnumItemSlot.FEET;
        } else if (d0 >= 0.9D + (flag ? 0.3D : 0.0D) && d0 < 0.9D + (flag ? 1.0D : 0.7D) && this.a(EnumItemSlot.CHEST)) {
            enumitemslot = EnumItemSlot.CHEST;
        } else if (d0 >= 0.4D && d0 < 0.4D + (flag ? 1.0D : 0.8D) && this.a(EnumItemSlot.LEGS)) {
            enumitemslot = EnumItemSlot.LEGS;
        } else if (d0 >= 1.6D && this.a(EnumItemSlot.HEAD)) {
            enumitemslot = EnumItemSlot.HEAD;
        } else if (!this.a(EnumItemSlot.MAINHAND) && this.a(EnumItemSlot.OFFHAND)) {
            enumitemslot = EnumItemSlot.OFFHAND;
        }

        return enumitemslot;
    }

    private boolean d(EnumItemSlot enumitemslot) {
        return (this.disabledSlots & 1 << enumitemslot.getSlotFlag()) != 0 || enumitemslot.a() == EnumItemSlot.Function.HAND && !this.hasArms();
    }

    private boolean a(EntityHuman entityhuman, EnumItemSlot enumitemslot, ItemStack itemstack, EnumHand enumhand) {
        ItemStack itemstack1 = this.getEquipment(enumitemslot);

        if (!itemstack1.isEmpty() && (this.disabledSlots & 1 << enumitemslot.getSlotFlag() + 8) != 0) {
            return false;
        } else if (itemstack1.isEmpty() && (this.disabledSlots & 1 << enumitemslot.getSlotFlag() + 16) != 0) {
            return false;
        } else {
            ItemStack itemstack2;
            // CraftBukkit start
            org.bukkit.inventory.ItemStack armorStandItem = CraftItemStack.asCraftMirror(itemstack1);
            org.bukkit.inventory.ItemStack playerHeldItem = CraftItemStack.asCraftMirror(itemstack);

            Player player = (Player) entityhuman.getBukkitEntity();
            ArmorStand self = (ArmorStand) this.getBukkitEntity();

            EquipmentSlot slot = CraftEquipmentSlot.getSlot(enumitemslot);
            PlayerArmorStandManipulateEvent armorStandManipulateEvent = new PlayerArmorStandManipulateEvent(player,self,playerHeldItem,armorStandItem,slot);
            this.world.getServer().getPluginManager().callEvent(armorStandManipulateEvent);

            if (armorStandManipulateEvent.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            if (entityhuman.abilities.canInstantlyBuild && itemstack1.isEmpty() && !itemstack.isEmpty()) {
                itemstack2 = itemstack.cloneItemStack();
                itemstack2.setCount(1);
                this.setSlot(enumitemslot, itemstack2);
                return true;
            } else if (!itemstack.isEmpty() && itemstack.getCount() > 1) {
                if (!itemstack1.isEmpty()) {
                    return false;
                } else {
                    itemstack2 = itemstack.cloneItemStack();
                    itemstack2.setCount(1);
                    this.setSlot(enumitemslot, itemstack2);
                    itemstack.subtract(1);
                    return true;
                }
            } else {
                this.setSlot(enumitemslot, itemstack);
                entityhuman.a(enumhand, itemstack1);
                return true;
            }
        }
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.world.isClientSide && !this.dead) {
            if (DamageSource.OUT_OF_WORLD.equals(damagesource)) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                this.killEntity(); // CraftBukkit - this.die() -> this.killEntity()
                return false;
            } else if (!this.isInvulnerable(damagesource) && (true || !this.armorStandInvisible) && !this.isMarker()) { // CraftBukkit
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f, true, this.armorStandInvisible)) {
                    return false;
                }
                // CraftBukkit end
                if (damagesource.isExplosion()) {
                    this.g(damagesource);
                    this.killEntity(); // CraftBukkit - this.die() -> this.killEntity()
                    return false;
                } else if (DamageSource.FIRE.equals(damagesource)) {
                    if (this.isBurning()) {
                        this.f(damagesource, 0.15F);
                    } else {
                        this.setOnFire(5);
                    }

                    return false;
                } else if (DamageSource.BURN.equals(damagesource) && this.getHealth() > 0.5F) {
                    this.f(damagesource, 4.0F);
                    return false;
                } else {
                    boolean flag = damagesource.j() instanceof EntityArrow;
                    boolean flag1 = flag && ((EntityArrow) damagesource.j()).getPierceLevel() > 0;
                    boolean flag2 = "player".equals(damagesource.q());

                    if (!flag2 && !flag) {
                        return false;
                    } else if (damagesource.getEntity() instanceof EntityHuman && !((EntityHuman) damagesource.getEntity()).abilities.mayBuild) {
                        return false;
                    } else if (damagesource.v()) {
                        this.F();
                        this.D();
                        this.killEntity(); // CraftBukkit - this.die() -> this.killEntity()
                        return flag1;
                    } else {
                        long i = this.world.getTime();

                        if (i - this.bi > 5L && !flag) {
                            this.world.broadcastEntityEffect(this, (byte) 32);
                            this.bi = i;
                        } else {
                            this.f(damagesource);
                            this.D();
                            this.die(); // CraftBukkit - SPIGOT-4890: remain as this.die() since above damagesource method will call death event
                        }

                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void D() {
        if (this.world instanceof WorldServer) {
            ((WorldServer) this.world).a(new ParticleParamBlock(Particles.BLOCK, Blocks.OAK_PLANKS.getBlockData()), this.locX(), this.e(0.6666666666666666D), this.locZ(), 10, (double) (this.getWidth() / 4.0F), (double) (this.getHeight() / 4.0F), (double) (this.getWidth() / 4.0F), 0.05D);
        }

    }

    private void f(DamageSource damagesource, float f) {
        float f1 = this.getHealth();

        f1 -= f;
        if (f1 <= 0.5F) {
            this.g(damagesource);
            this.killEntity(); // CraftBukkit - this.die() -> this.killEntity()
        } else {
            this.setHealth(f1);
        }

    }

    private void f(DamageSource damagesource) {
        drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(new ItemStack(Items.ARMOR_STAND))); // CraftBukkit - add to drops
        this.g(damagesource);
    }

    private void g(DamageSource damagesource) {
        this.F();
        // this.d(damagesource); // CraftBukkit - moved down

        ItemStack itemstack;
        int i;

        for (i = 0; i < this.handItems.size(); ++i) {
            itemstack = (ItemStack) this.handItems.get(i);
            if (!itemstack.isEmpty()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.handItems.set(i, ItemStack.b);
            }
        }

        for (i = 0; i < this.armorItems.size(); ++i) {
            itemstack = (ItemStack) this.armorItems.get(i);
            if (!itemstack.isEmpty()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.armorItems.set(i, ItemStack.b);
            }
        }
        this.d(damagesource); // CraftBukkit - moved from above

    }

    private void F() {
        this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_ARMOR_STAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    protected float f(float f, float f1) {
        this.aB = this.lastYaw;
        this.aA = this.yaw;
        return 0.0F;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * (this.isBaby() ? 0.5F : 0.9F);
    }

    @Override
    public double ba() {
        return this.isMarker() ? 0.0D : 0.10000000149011612D;
    }

    @Override
    public void g(Vec3D vec3d) {
        if (this.A()) {
            super.g(vec3d);
        }
    }

    @Override
    public void n(float f) {
        this.aB = this.lastYaw = f;
        this.aD = this.aC = f;
    }

    @Override
    public void setHeadRotation(float f) {
        this.aB = this.lastYaw = f;
        this.aD = this.aC = f;
    }

    @Override
    public void tick() {
        super.tick();
        Vector3f vector3f = (Vector3f) this.datawatcher.get(EntityArmorStand.c);

        if (!this.headPose.equals(vector3f)) {
            this.setHeadPose(vector3f);
        }

        Vector3f vector3f1 = (Vector3f) this.datawatcher.get(EntityArmorStand.d);

        if (!this.bodyPose.equals(vector3f1)) {
            this.setBodyPose(vector3f1);
        }

        Vector3f vector3f2 = (Vector3f) this.datawatcher.get(EntityArmorStand.e);

        if (!this.leftArmPose.equals(vector3f2)) {
            this.setLeftArmPose(vector3f2);
        }

        Vector3f vector3f3 = (Vector3f) this.datawatcher.get(EntityArmorStand.f);

        if (!this.rightArmPose.equals(vector3f3)) {
            this.setRightArmPose(vector3f3);
        }

        Vector3f vector3f4 = (Vector3f) this.datawatcher.get(EntityArmorStand.g);

        if (!this.leftLegPose.equals(vector3f4)) {
            this.setLeftLegPose(vector3f4);
        }

        Vector3f vector3f5 = (Vector3f) this.datawatcher.get(EntityArmorStand.bh);

        if (!this.rightLegPose.equals(vector3f5)) {
            this.setRightLegPose(vector3f5);
        }

    }

    @Override
    protected void C() {
        this.setInvisible(this.armorStandInvisible);
    }

    @Override
    public void setInvisible(boolean flag) {
        this.armorStandInvisible = flag;
        super.setInvisible(flag);
    }

    @Override
    public boolean isBaby() {
        return this.isSmall();
    }

    // CraftBukkit start
    @Override
    protected boolean isDropExperience() {
        return true; // MC-157395, SPIGOT-5193 even baby (small) armor stands should drop
    }
    // CraftBukkit end

    @Override
    public void killEntity() {
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, drops); // CraftBukkit - call event
        this.die();
    }

    @Override
    public boolean ch() {
        return this.isInvisible();
    }

    @Override
    public EnumPistonReaction getPushReaction() {
        return this.isMarker() ? EnumPistonReaction.IGNORE : super.getPushReaction();
    }

    public void setSmall(boolean flag) {
        this.datawatcher.set(EntityArmorStand.b, this.a((Byte) this.datawatcher.get(EntityArmorStand.b), 1, flag));
    }

    public boolean isSmall() {
        return ((Byte) this.datawatcher.get(EntityArmorStand.b) & 1) != 0;
    }

    public void setArms(boolean flag) {
        this.datawatcher.set(EntityArmorStand.b, this.a((Byte) this.datawatcher.get(EntityArmorStand.b), 4, flag));
    }

    public boolean hasArms() {
        return ((Byte) this.datawatcher.get(EntityArmorStand.b) & 4) != 0;
    }

    public void setBasePlate(boolean flag) {
        this.datawatcher.set(EntityArmorStand.b, this.a((Byte) this.datawatcher.get(EntityArmorStand.b), 8, flag));
    }

    public boolean hasBasePlate() {
        return ((Byte) this.datawatcher.get(EntityArmorStand.b) & 8) != 0;
    }

    public void setMarker(boolean flag) {
        this.datawatcher.set(EntityArmorStand.b, this.a((Byte) this.datawatcher.get(EntityArmorStand.b), 16, flag));
    }

    public boolean isMarker() {
        return ((Byte) this.datawatcher.get(EntityArmorStand.b) & 16) != 0;
    }

    private byte a(byte b0, int i, boolean flag) {
        if (flag) {
            b0 = (byte) (b0 | i);
        } else {
            b0 = (byte) (b0 & ~i);
        }

        return b0;
    }

    public void setHeadPose(Vector3f vector3f) {
        this.headPose = vector3f;
        this.datawatcher.set(EntityArmorStand.c, vector3f);
    }

    public void setBodyPose(Vector3f vector3f) {
        this.bodyPose = vector3f;
        this.datawatcher.set(EntityArmorStand.d, vector3f);
    }

    public void setLeftArmPose(Vector3f vector3f) {
        this.leftArmPose = vector3f;
        this.datawatcher.set(EntityArmorStand.e, vector3f);
    }

    public void setRightArmPose(Vector3f vector3f) {
        this.rightArmPose = vector3f;
        this.datawatcher.set(EntityArmorStand.f, vector3f);
    }

    public void setLeftLegPose(Vector3f vector3f) {
        this.leftLegPose = vector3f;
        this.datawatcher.set(EntityArmorStand.g, vector3f);
    }

    public void setRightLegPose(Vector3f vector3f) {
        this.rightLegPose = vector3f;
        this.datawatcher.set(EntityArmorStand.bh, vector3f);
    }

    public Vector3f r() {
        return this.headPose;
    }

    public Vector3f t() {
        return this.bodyPose;
    }

    @Override
    public boolean isInteractable() {
        return super.isInteractable() && !this.isMarker();
    }

    @Override
    public boolean t(Entity entity) {
        return entity instanceof EntityHuman && !this.world.a((EntityHuman) entity, this.getChunkCoordinates());
    }

    @Override
    public EnumMainHand getMainHand() {
        return EnumMainHand.RIGHT;
    }

    @Override
    protected SoundEffect getSoundFall(int i) {
        return SoundEffects.ENTITY_ARMOR_STAND_FALL;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_ARMOR_STAND_HIT;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_ARMOR_STAND_BREAK;
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {}

    @Override
    public boolean eg() {
        return false;
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityArmorStand.b.equals(datawatcherobject)) {
            this.updateSize();
            this.i = !this.isMarker();
        }

        super.a(datawatcherobject);
    }

    @Override
    public boolean eh() {
        return false;
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        return this.s(this.isMarker());
    }

    private EntitySize s(boolean flag) {
        return flag ? EntityArmorStand.bp : (this.isBaby() ? EntityArmorStand.bq : this.getEntityType().l());
    }
}
