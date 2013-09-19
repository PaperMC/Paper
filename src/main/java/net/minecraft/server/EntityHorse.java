package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
// CraftBukkit end

public class EntityHorse extends EntityAnimal implements IInventoryListener {

    private static final IEntitySelector bu = new EntitySelectorHorse();
    public static final IAttribute attributeJumpStrength = (new AttributeRanged("horse.jumpStrength", 0.7D, 0.0D, 2.0D)).a("Jump Strength").a(true); // CraftBukkit - private -> public
    private static final String[] bw = new String[] { null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png"};
    private static final String[] bx = new String[] { "", "meo", "goo", "dio"};
    private static final int[] by = new int[] { 0, 5, 7, 11};
    private static final String[] bz = new String[] { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] bA = new String[] { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] bB = new String[] { null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] bC = new String[] { "", "wo_", "wmo", "wdo", "bdo"};
    private int bD;
    private int bE;
    private int bF;
    public int bp;
    public int bq;
    protected boolean br;
    public InventoryHorseChest inventoryChest; // CraftBukkit - private -> public
    private boolean bH;
    protected int bs;
    protected float bt;
    private boolean bI;
    private float bJ;
    private float bK;
    private float bL;
    private float bM;
    private float bN;
    private float bO;
    private int bP;
    private String bQ;
    private String[] bR = new String[3];
    public int maxDomestication = 100; // CraftBukkit - store max domestication value

    public EntityHorse(World world) {
        super(world);
        this.a(1.4F, 1.6F);
        this.fireProof = false;
        this.setHasChest(false);
        this.getNavigation().a(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D));
        this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.7D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.loadChest();
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Integer.valueOf(0));
        this.datawatcher.a(19, Byte.valueOf((byte) 0));
        this.datawatcher.a(20, Integer.valueOf(0));
        this.datawatcher.a(21, String.valueOf(""));
        this.datawatcher.a(22, Integer.valueOf(0));
    }

    public void setType(int i) {
        this.datawatcher.watch(19, Byte.valueOf((byte) i));
        this.cJ();
    }

    public int getType() {
        return this.datawatcher.getByte(19);
    }

    public void setVariant(int i) {
        this.datawatcher.watch(20, Integer.valueOf(i));
        this.cJ();
    }

    public int getVariant() {
        return this.datawatcher.getInt(20);
    }

    public String getLocalizedName() {
        if (this.hasCustomName()) {
            return this.getCustomName();
        } else {
            int i = this.getType();

            switch (i) {
            case 0:
            default:
                return LocaleI18n.get("entity.horse.name");

            case 1:
                return LocaleI18n.get("entity.donkey.name");

            case 2:
                return LocaleI18n.get("entity.mule.name");

            case 3:
                return LocaleI18n.get("entity.zombiehorse.name");

            case 4:
                return LocaleI18n.get("entity.skeletonhorse.name");
            }
        }
    }

    private boolean w(int i) {
        return (this.datawatcher.getInt(16) & i) != 0;
    }

    private void b(int i, boolean flag) {
        int j = this.datawatcher.getInt(16);

        if (flag) {
            this.datawatcher.watch(16, Integer.valueOf(j | i));
        } else {
            this.datawatcher.watch(16, Integer.valueOf(j & ~i));
        }
    }

    public boolean bV() {
        return !this.isBaby();
    }

    public boolean isTame() {
        return this.w(2);
    }

    public boolean ca() {
        return this.bV();
    }

    public String getOwnerName() {
        return this.datawatcher.getString(21);
    }

    public void setOwnerName(String s) {
        this.datawatcher.watch(21, s);
    }

    public float cc() {
        int i = this.getAge();

        return i >= 0 ? 1.0F : 0.5F + (float) (-24000 - i) / -24000.0F * 0.5F;
    }

    public void a(boolean flag) {
        if (flag) {
            this.a(this.cc());
        } else {
            this.a(1.0F);
        }
    }

    public boolean cd() {
        return this.br;
    }

    public void setTame(boolean flag) {
        this.b(2, flag);
    }

    public void j(boolean flag) {
        this.br = flag;
    }

    public boolean bG() {
        return !this.cy() && super.bG();
    }

    protected void o(float f) {
        if (f > 6.0F && this.cg()) {
            this.o(false);
        }
    }

    public boolean hasChest() {
        return this.w(8);
    }

    public int cf() {
        return this.datawatcher.getInt(22);
    }

    public int d(ItemStack itemstack) {
        return itemstack == null ? 0 : (itemstack.id == Item.HORSE_ARMOR_IRON.id ? 1 : (itemstack.id == Item.HORSE_ARMOR_GOLD.id ? 2 : (itemstack.id == Item.HORSE_ARMOR_DIAMOND.id ? 3 : 0)));
    }

    public boolean cg() {
        return this.w(32);
    }

    public boolean ch() {
        return this.w(64);
    }

    public boolean ci() {
        return this.w(16);
    }

    public boolean cj() {
        return this.bH;
    }

    public void r(int i) {
        this.datawatcher.watch(22, Integer.valueOf(i));
        this.cJ();
    }

    public void k(boolean flag) {
        this.b(16, flag);
    }

    public void setHasChest(boolean flag) {
        this.b(8, flag);
    }

    public void m(boolean flag) {
        this.bH = flag;
    }

    public void n(boolean flag) {
        this.b(4, flag);
    }

    public int getTemper() {
        return this.bs;
    }

    public void setTemper(int i) {
        this.bs = i;
    }

    public int t(int i) {
        int j = MathHelper.a(this.getTemper() + i, 0, this.getMaxDomestication());

        this.setTemper(j);
        return j;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        Entity entity = damagesource.getEntity();

        return this.passenger != null && this.passenger.equals(entity) ? false : super.damageEntity(damagesource, f);
    }

    public int aQ() {
        return by[this.cf()];
    }

    public boolean M() {
        return this.passenger == null;
    }

    public boolean cl() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locZ);

        this.world.getBiome(i, j);
        return true;
    }

    public void cm() {
        if (!this.world.isStatic && this.hasChest()) {
            this.b(Block.CHEST.id, 1);
            this.setHasChest(false);
        }
    }

    private void cF() {
        this.cM();
        this.world.makeSound(this, "eating", 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }

    protected void b(float f) {
        if (f > 1.0F) {
            this.makeSound("mob.horse.land", 0.4F, 1.0F);
        }

        int i = MathHelper.f(f * 0.5F - 3.0F);

        if (i > 0) {
            // CraftBukkit start
            EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(null, this, EntityDamageEvent.DamageCause.FALL, i);
            if (!event.isCancelled()) {
                float damage = (float) event.getDamage();
                if (damage > 0) {
                    this.getBukkitEntity().setLastDamageCause(event);
                    this.damageEntity(DamageSource.FALL, damage);
                }
            }

            if (this.passenger != null) {
                EntityDamageEvent passengerEvent = CraftEventFactory.callEntityDamageEvent(null, this.passenger, EntityDamageEvent.DamageCause.FALL, i);
                if (!passengerEvent.isCancelled() && this.passenger != null) { // Check again in case of plugin
                    float damage = (float) passengerEvent.getDamage();
                    if (damage > 0) {
                        this.passenger.getBukkitEntity().setLastDamageCause(passengerEvent);
                        this.passenger.damageEntity(DamageSource.FALL, damage);
                    }
                }
                // CraftBukkit end
            }

            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.2D - (double) this.lastYaw), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.world.makeSound(this, stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    private int cG() {
        int i = this.getType();

        return this.hasChest() /* && (i == 1 || i == 2) */ ? 17 : 2; // CraftBukkit - Remove type check
    }

    public void loadChest() { // CraftBukkit - private -> public
        InventoryHorseChest inventoryhorsechest = this.inventoryChest;

        this.inventoryChest = new InventoryHorseChest("HorseChest", this.cG(), this); // CraftBukkit - add this horse
        this.inventoryChest.a(this.getLocalizedName());
        if (inventoryhorsechest != null) {
            inventoryhorsechest.b(this);
            int i = Math.min(inventoryhorsechest.getSize(), this.inventoryChest.getSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryhorsechest.getItem(j);

                if (itemstack != null) {
                    this.inventoryChest.setItem(j, itemstack.cloneItemStack());
                }
            }

            inventoryhorsechest = null;
        }

        this.inventoryChest.a(this);
        this.cI();
    }

    private void cI() {
        if (!this.world.isStatic) {
            this.n(this.inventoryChest.getItem(0) != null);
            if (this.cv()) {
                this.r(this.d(this.inventoryChest.getItem(1)));
            }
        }
    }

    public void a(InventorySubcontainer inventorysubcontainer) {
        int i = this.cf();
        boolean flag = this.co();

        this.cI();
        if (this.ticksLived > 20) {
            if (i == 0 && i != this.cf()) {
                this.makeSound("mob.horse.armor", 0.5F, 1.0F);
            }

            if (!flag && this.co()) {
                this.makeSound("mob.horse.leather", 0.5F, 1.0F);
            }
        }
    }

    public boolean canSpawn() {
        this.cl();
        return super.canSpawn();
    }

    protected EntityHorse a(Entity entity, double d0) {
        double d1 = Double.MAX_VALUE;
        Entity entity1 = null;
        List list = this.world.getEntities(entity, entity.boundingBox.a(d0, d0, d0), bu);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity2 = (Entity) iterator.next();
            double d2 = entity2.e(entity.locX, entity.locY, entity.locZ);

            if (d2 < d1) {
                entity1 = entity2;
                d1 = d2;
            }
        }

        return (EntityHorse) entity1;
    }

    public double getJumpStrength() {
        return this.getAttributeInstance(attributeJumpStrength).getValue();
    }

    protected String aP() {
        this.cM();
        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.death" : (i == 4 ? "mob.horse.skeleton.death" : (i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    protected int getLootId() {
        boolean flag = this.random.nextInt(4) == 0;
        int i = this.getType();

        return i == 4 ? Item.BONE.id : (i == 3 ? (flag ? 0 : Item.ROTTEN_FLESH.id) : Item.LEATHER.id);
    }

    protected String aO() {
        this.cM();
        if (this.random.nextInt(3) == 0) {
            this.cO();
        }

        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.hit" : (i == 4 ? "mob.horse.skeleton.hit" : (i != 1 && i != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean co() {
        return this.w(4);
    }

    protected String r() {
        this.cM();
        if (this.random.nextInt(10) == 0 && !this.bc()) {
            this.cO();
        }

        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.idle" : (i == 4 ? "mob.horse.skeleton.idle" : (i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String cp() {
        this.cM();
        this.cO();
        int i = this.getType();

        return i != 3 && i != 4 ? (i != 1 && i != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    protected void a(int i, int j, int k, int l) {
        StepSound stepsound = Block.byId[l].stepSound;

        if (this.world.getTypeId(i, j + 1, k) == Block.SNOW.id) {
            stepsound = Block.SNOW.stepSound;
        }

        if (!Block.byId[l].material.isLiquid()) {
            int i1 = this.getType();

            if (this.passenger != null && i1 != 1 && i1 != 2) {
                ++this.bP;
                if (this.bP > 5 && this.bP % 3 == 0) {
                    this.makeSound("mob.horse.gallop", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    if (i1 == 0 && this.random.nextInt(10) == 0) {
                        this.makeSound("mob.horse.breathe", stepsound.getVolume1() * 0.6F, stepsound.getVolume2());
                    }
                } else if (this.bP <= 5) {
                    this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                }
            } else if (stepsound == Block.h) {
                this.makeSound("mob.horse.soft", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            } else {
                this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            }
        }
    }

    protected void az() {
        super.az();
        this.aX().b(attributeJumpStrength);
        this.getAttributeInstance(GenericAttributes.a).setValue(53.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.22499999403953552D);
    }

    public int bv() {
        return 6;
    }

    public int getMaxDomestication() {
        return this.maxDomestication; // CraftBukkit - return stored max domestication instead of 100
    }

    protected float ba() {
        return 0.8F;
    }

    public int o() {
        return 400;
    }

    private void cJ() {
        this.bQ = null;
    }

    public void f(EntityHuman entityhuman) {
        if (!this.world.isStatic && (this.passenger == null || this.passenger == entityhuman) && this.isTame()) {
            this.inventoryChest.a(this.getLocalizedName());
            entityhuman.openHorseInventory(this, this.inventoryChest);
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.MONSTER_EGG.id) {
            return super.a(entityhuman);
        } else if (!this.isTame() && this.cy()) {
            return false;
        } else if (this.isTame() && this.bV() && entityhuman.isSneaking()) {
            this.f(entityhuman);
            return true;
        } else if (this.ca() && this.passenger != null) {
            return super.a(entityhuman);
        } else {
            if (itemstack != null) {
                boolean flag = false;

                if (this.cv()) {
                    byte b0 = -1;

                    if (itemstack.id == Item.HORSE_ARMOR_IRON.id) {
                        b0 = 1;
                    } else if (itemstack.id == Item.HORSE_ARMOR_GOLD.id) {
                        b0 = 2;
                    } else if (itemstack.id == Item.HORSE_ARMOR_DIAMOND.id) {
                        b0 = 3;
                    }

                    if (b0 >= 0) {
                        if (!this.isTame()) {
                            this.cD();
                            return true;
                        }

                        this.f(entityhuman);
                        return true;
                    }
                }

                if (!flag && !this.cy()) {
                    float f = 0.0F;
                    short short1 = 0;
                    byte b1 = 0;

                    if (itemstack.id == Item.WHEAT.id) {
                        f = 2.0F;
                        short1 = 60;
                        b1 = 3;
                    } else if (itemstack.id == Item.SUGAR.id) {
                        f = 1.0F;
                        short1 = 30;
                        b1 = 3;
                    } else if (itemstack.id == Item.BREAD.id) {
                        f = 7.0F;
                        short1 = 180;
                        b1 = 3;
                    } else if (itemstack.id == Block.HAY_BLOCK.id) {
                        f = 20.0F;
                        short1 = 180;
                    } else if (itemstack.id == Item.APPLE.id) {
                        f = 3.0F;
                        short1 = 60;
                        b1 = 3;
                    } else if (itemstack.id == Item.CARROT_GOLDEN.id) {
                        f = 4.0F;
                        short1 = 60;
                        b1 = 5;
                        if (this.isTame() && this.getAge() == 0) {
                            flag = true;
                            this.bX();
                        }
                    } else if (itemstack.id == Item.GOLDEN_APPLE.id) {
                        f = 10.0F;
                        short1 = 240;
                        b1 = 10;
                        if (this.isTame() && this.getAge() == 0) {
                            flag = true;
                            this.bX();
                        }
                    }

                    if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
                        this.heal(f);
                        flag = true;
                    }

                    if (!this.bV() && short1 > 0) {
                        this.a(short1);
                        flag = true;
                    }

                    if (b1 > 0 && (flag || !this.isTame()) && b1 < this.getMaxDomestication()) {
                        flag = true;
                        this.t(b1);
                    }

                    if (flag) {
                        this.cF();
                    }
                }

                if (!this.isTame() && !flag) {
                    if (itemstack != null && itemstack.a(entityhuman, (EntityLiving) this)) {
                        return true;
                    }

                    this.cD();
                    return true;
                }

                if (!flag && this.cw() && !this.hasChest() && itemstack.id == Block.CHEST.id) {
                    this.setHasChest(true);
                    this.makeSound("mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    flag = true;
                    this.loadChest();
                }

                if (!flag && this.ca() && !this.co() && itemstack.id == Item.SADDLE.id) {
                    this.f(entityhuman);
                    return true;
                }

                if (flag) {
                    if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count == 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                    }

                    return true;
                }
            }

            if (this.ca() && this.passenger == null) {
                if (itemstack != null && itemstack.a(entityhuman, (EntityLiving) this)) {
                    return true;
                } else {
                    this.h(entityhuman);
                    return true;
                }
            } else {
                return super.a(entityhuman);
            }
        }
    }

    private void h(EntityHuman entityhuman) {
        entityhuman.yaw = this.yaw;
        entityhuman.pitch = this.pitch;
        this.o(false);
        this.p(false);
        if (!this.world.isStatic) {
            entityhuman.mount(this);
        }
    }

    public boolean cv() {
        return this.getType() == 0;
    }

    public boolean cw() {
        int i = this.getType();

        return i == 2 || i == 1;
    }

    protected boolean bc() {
        return this.passenger != null && this.co() ? true : this.cg() || this.ch();
    }

    public boolean cy() {
        int i = this.getType();

        return i == 3 || i == 4;
    }

    public boolean cz() {
        return this.cy() || this.getType() == 2;
    }

    public boolean c(ItemStack itemstack) {
        return false;
    }

    private void cL() {
        this.bp = 1;
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (!this.world.isStatic) {
            this.cE();
        }
    }

    public void c() {
        if (this.random.nextInt(200) == 0) {
            this.cL();
        }

        super.c();
        if (!this.world.isStatic) {
            if (this.random.nextInt(900) == 0 && this.deathTicks == 0) {
                this.heal(1.0F);
            }

            if (!this.cg() && this.passenger == null && this.random.nextInt(300) == 0 && this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY) - 1, MathHelper.floor(this.locZ)) == Block.GRASS.id) {
                this.o(true);
            }

            if (this.cg() && ++this.bD > 50) {
                this.bD = 0;
                this.o(false);
            }

            if (this.ci() && !this.bV() && !this.cg()) {
                EntityHorse entityhorse = this.a(this, 16.0D);

                if (entityhorse != null && this.e(entityhorse) > 4.0D) {
                    PathEntity pathentity = this.world.findPath(this, entityhorse, 16.0F, true, false, false, true);

                    this.setPathEntity(pathentity);
                }
            }
        }
    }

    public void l_() {
        super.l_();
        if (this.world.isStatic && this.datawatcher.a()) {
            this.datawatcher.e();
            this.cJ();
        }

        if (this.bE > 0 && ++this.bE > 30) {
            this.bE = 0;
            this.b(128, false);
        }

        if (!this.world.isStatic && this.bF > 0 && ++this.bF > 20) {
            this.bF = 0;
            this.p(false);
        }

        if (this.bp > 0 && ++this.bp > 8) {
            this.bp = 0;
        }

        if (this.bq > 0) {
            ++this.bq;
            if (this.bq > 300) {
                this.bq = 0;
            }
        }

        this.bK = this.bJ;
        if (this.cg()) {
            this.bJ += (1.0F - this.bJ) * 0.4F + 0.05F;
            if (this.bJ > 1.0F) {
                this.bJ = 1.0F;
            }
        } else {
            this.bJ += (0.0F - this.bJ) * 0.4F - 0.05F;
            if (this.bJ < 0.0F) {
                this.bJ = 0.0F;
            }
        }

        this.bM = this.bL;
        if (this.ch()) {
            this.bK = this.bJ = 0.0F;
            this.bL += (1.0F - this.bL) * 0.4F + 0.05F;
            if (this.bL > 1.0F) {
                this.bL = 1.0F;
            }
        } else {
            this.bI = false;
            this.bL += (0.8F * this.bL * this.bL * this.bL - this.bL) * 0.6F - 0.05F;
            if (this.bL < 0.0F) {
                this.bL = 0.0F;
            }
        }

        this.bO = this.bN;
        if (this.w(128)) {
            this.bN += (1.0F - this.bN) * 0.7F + 0.05F;
            if (this.bN > 1.0F) {
                this.bN = 1.0F;
            }
        } else {
            this.bN += (0.0F - this.bN) * 0.7F - 0.05F;
            if (this.bN < 0.0F) {
                this.bN = 0.0F;
            }
        }
    }

    private void cM() {
        if (!this.world.isStatic) {
            this.bE = 1;
            this.b(128, true);
        }
    }

    private boolean cN() {
        return this.passenger == null && this.vehicle == null && this.isTame() && this.bV() && !this.cz() && this.getHealth() >= this.getMaxHealth();
    }

    public void e(boolean flag) {
        this.b(32, flag);
    }

    public void o(boolean flag) {
        this.e(flag);
    }

    public void p(boolean flag) {
        if (flag) {
            this.o(false);
        }

        this.b(64, flag);
    }

    private void cO() {
        if (!this.world.isStatic) {
            this.bF = 1;
            this.p(true);
        }
    }

    public void cD() {
        this.cO();
        String s = this.cp();

        if (s != null) {
            this.makeSound(s, this.ba(), this.bb());
        }
    }

    public void cE() {
        this.a(this, this.inventoryChest);
        this.cm();
    }

    private void a(Entity entity, InventoryHorseChest inventoryhorsechest) {
        if (inventoryhorsechest != null && !this.world.isStatic) {
            for (int i = 0; i < inventoryhorsechest.getSize(); ++i) {
                ItemStack itemstack = inventoryhorsechest.getItem(i);

                if (itemstack != null) {
                    this.a(itemstack, 0.0F);
                }
            }
        }
    }

    public boolean g(EntityHuman entityhuman) {
        this.setOwnerName(entityhuman.getName());
        this.setTame(true);
        return true;
    }

    public void e(float f, float f1) {
        if (this.passenger != null && this.co()) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.b(this.yaw, this.pitch);
            this.aP = this.aN = this.yaw;
            f = ((EntityLiving) this.passenger).be * 0.5F;
            f1 = ((EntityLiving) this.passenger).bf;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
                this.bP = 0;
            }

            if (this.onGround && this.bt == 0.0F && this.ch() && !this.bI) {
                f = 0.0F;
                f1 = 0.0F;
            }

            if (this.bt > 0.0F && !this.cd() && this.onGround) {
                this.motY = this.getJumpStrength() * (double) this.bt;
                if (this.hasEffect(MobEffectList.JUMP)) {
                    this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
                }

                this.j(true);
                this.an = true;
                if (f1 > 0.0F) {
                    float f2 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
                    float f3 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);

                    this.motX += (double) (-0.4F * f2 * this.bt);
                    this.motZ += (double) (0.4F * f3 * this.bt);
                    this.makeSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.bt = 0.0F;
            }

            this.Y = 1.0F;
            this.aR = this.bg() * 0.1F;
            if (!this.world.isStatic) {
                this.i((float) this.getAttributeInstance(GenericAttributes.d).getValue());
                super.e(f, f1);
            }

            if (this.onGround) {
                this.bt = 0.0F;
                this.j(false);
            }

            this.aF = this.aG;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.aG += (f4 - this.aG) * 0.4F;
            this.aH += this.aG;
        } else {
            this.Y = 0.5F;
            this.aR = 0.02F;
            super.e(f, f1);
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("EatingHaystack", this.cg());
        nbttagcompound.setBoolean("ChestedHorse", this.hasChest());
        nbttagcompound.setBoolean("HasReproduced", this.cj());
        nbttagcompound.setBoolean("Bred", this.ci());
        nbttagcompound.setInt("Type", this.getType());
        nbttagcompound.setInt("Variant", this.getVariant());
        nbttagcompound.setInt("Temper", this.getTemper());
        nbttagcompound.setBoolean("Tame", this.isTame());
        nbttagcompound.setString("OwnerName", this.getOwnerName());
        nbttagcompound.setInt("Bukkit.MaxDomestication", this.maxDomestication); // CraftBukkit
        if (this.hasChest()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < this.inventoryChest.getSize(); ++i) {
                ItemStack itemstack = this.inventoryChest.getItem(i);

                if (itemstack != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.setByte("Slot", (byte) i);
                    itemstack.save(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }

            nbttagcompound.set("Items", nbttaglist);
        }

        if (this.inventoryChest.getItem(1) != null) {
            nbttagcompound.set("ArmorItem", this.inventoryChest.getItem(1).save(new NBTTagCompound("ArmorItem")));
        }

        if (this.inventoryChest.getItem(0) != null) {
            nbttagcompound.set("SaddleItem", this.inventoryChest.getItem(0).save(new NBTTagCompound("SaddleItem")));
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.o(nbttagcompound.getBoolean("EatingHaystack"));
        this.k(nbttagcompound.getBoolean("Bred"));
        this.setHasChest(nbttagcompound.getBoolean("ChestedHorse"));
        this.m(nbttagcompound.getBoolean("HasReproduced"));
        this.setType(nbttagcompound.getInt("Type"));
        this.setVariant(nbttagcompound.getInt("Variant"));
        this.setTemper(nbttagcompound.getInt("Temper"));
        this.setTame(nbttagcompound.getBoolean("Tame"));
        if (nbttagcompound.hasKey("OwnerName")) {
            this.setOwnerName(nbttagcompound.getString("OwnerName"));
        }
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxDomestication")) {
            this.maxDomestication = nbttagcompound.getInt("Bukkit.MaxDomestication");
        }
        // CraftBukkit end
        AttributeInstance attributeinstance = this.aX().a("Speed");

        if (attributeinstance != null) {
            this.getAttributeInstance(GenericAttributes.d).setValue(attributeinstance.b() * 0.25D);
        }

        if (this.hasChest()) {
            NBTTagList nbttaglist = nbttagcompound.getList("Items");

            this.loadChest();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 2 && j < this.inventoryChest.getSize()) {
                    this.inventoryChest.setItem(j, ItemStack.createStack(nbttagcompound1));
                }
            }
        }

        ItemStack itemstack;

        if (nbttagcompound.hasKey("ArmorItem")) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("ArmorItem"));
            if (itemstack != null && v(itemstack.id)) {
                this.inventoryChest.setItem(1, itemstack);
            }
        }

        if (nbttagcompound.hasKey("SaddleItem")) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("SaddleItem"));
            if (itemstack != null && itemstack.id == Item.SADDLE.id) {
                this.inventoryChest.setItem(0, itemstack);
            }
        } else if (nbttagcompound.getBoolean("Saddle")) {
            this.inventoryChest.setItem(0, new ItemStack(Item.SADDLE));
        }

        this.cI();
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (entityanimal.getClass() != this.getClass()) {
            return false;
        } else {
            EntityHorse entityhorse = (EntityHorse) entityanimal;

            if (this.cN() && entityhorse.cN()) {
                int i = this.getType();
                int j = entityhorse.getType();

                return i == j || i == 0 && j == 1 || i == 1 && j == 0;
            } else {
                return false;
            }
        }
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        EntityHorse entityhorse = (EntityHorse) entityageable;
        EntityHorse entityhorse1 = new EntityHorse(this.world);
        int i = this.getType();
        int j = entityhorse.getType();
        int k = 0;

        if (i == j) {
            k = i;
        } else if (i == 0 && j == 1 || i == 1 && j == 0) {
            k = 2;
        }

        if (k == 0) {
            int l = this.random.nextInt(9);
            int i1;

            if (l < 4) {
                i1 = this.getVariant() & 255;
            } else if (l < 8) {
                i1 = entityhorse.getVariant() & 255;
            } else {
                i1 = this.random.nextInt(7);
            }

            int j1 = this.random.nextInt(5);

            if (j1 < 4) {
                i1 |= this.getVariant() & '\uff00';
            } else if (j1 < 8) {
                i1 |= entityhorse.getVariant() & '\uff00';
            } else {
                i1 |= this.random.nextInt(5) << 8 & '\uff00';
            }

            entityhorse1.setVariant(i1);
        }

        entityhorse1.setType(k);
        double d0 = this.getAttributeInstance(GenericAttributes.a).b() + entityageable.getAttributeInstance(GenericAttributes.a).b() + (double) this.cP();

        entityhorse1.getAttributeInstance(GenericAttributes.a).setValue(d0 / 3.0D);
        double d1 = this.getAttributeInstance(attributeJumpStrength).b() + entityageable.getAttributeInstance(attributeJumpStrength).b() + this.cQ();

        entityhorse1.getAttributeInstance(attributeJumpStrength).setValue(d1 / 3.0D);
        double d2 = this.getAttributeInstance(GenericAttributes.d).b() + entityageable.getAttributeInstance(GenericAttributes.d).b() + this.cR();

        entityhorse1.getAttributeInstance(GenericAttributes.d).setValue(d2 / 3.0D);
        return entityhorse1;
    }

    public GroupDataEntity a(GroupDataEntity groupdataentity) {
        Object object = super.a(groupdataentity);
        boolean flag = false;
        int i = 0;
        int j;

        if (object instanceof GroupDataHorse) {
            j = ((GroupDataHorse) object).a;
            i = ((GroupDataHorse) object).b & 255 | this.random.nextInt(5) << 8;
        } else {
            if (this.random.nextInt(10) == 0) {
                j = 1;
            } else {
                int k = this.random.nextInt(7);
                int l = this.random.nextInt(5);

                j = 0;
                i = k | l << 8;
            }

            object = new GroupDataHorse(j, i);
        }

        this.setType(j);
        this.setVariant(i);
        if (this.random.nextInt(5) == 0) {
            this.setAge(-24000);
        }

        if (j != 4 && j != 3) {
            this.getAttributeInstance(GenericAttributes.a).setValue((double) this.cP());
            if (j == 0) {
                this.getAttributeInstance(GenericAttributes.d).setValue(this.cR());
            } else {
                this.getAttributeInstance(GenericAttributes.d).setValue(0.17499999701976776D);
            }
        } else {
            this.getAttributeInstance(GenericAttributes.a).setValue(15.0D);
            this.getAttributeInstance(GenericAttributes.d).setValue(0.20000000298023224D);
        }

        if (j != 2 && j != 1) {
            this.getAttributeInstance(attributeJumpStrength).setValue(this.cQ());
        } else {
            this.getAttributeInstance(attributeJumpStrength).setValue(0.5D);
        }

        this.setHealth(this.getMaxHealth());
        return (GroupDataEntity) object;
    }

    protected boolean bf() {
        return true;
    }

    public void u(int i) {
        if (this.co()) {
            // CraftBukkit start - fire HorseJumpEvent, use event power
            if (i < 0) {
                i = 0;
            }

            float power;
            if (i >= 90) {
                power = 1.0F;
            } else {
                power = 0.4F + 0.4F * (float) i / 90.0F;
            }

            org.bukkit.event.entity.HorseJumpEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callHorseJumpEvent(this, power);
            if (!event.isCancelled()) {
                this.bI = true;
                this.cO();
                this.bt = event.getPower();
            }
            // CraftBukkit end
        }
    }

    public void W() {
        super.W();
        if (this.bM > 0.0F) {
            float f = MathHelper.sin(this.aN * 3.1415927F / 180.0F);
            float f1 = MathHelper.cos(this.aN * 3.1415927F / 180.0F);
            float f2 = 0.7F * this.bM;
            float f3 = 0.15F * this.bM;

            this.passenger.setPosition(this.locX + (double) (f2 * f), this.locY + this.Y() + this.passenger.X() + (double) f3, this.locZ - (double) (f2 * f1));
            if (this.passenger instanceof EntityLiving) {
                ((EntityLiving) this.passenger).aN = this.aN;
            }
        }
    }

    private float cP() {
        return 15.0F + (float) this.random.nextInt(8) + (float) this.random.nextInt(9);
    }

    private double cQ() {
        return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
    }

    private double cR() {
        return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
    }

    public static boolean v(int i) {
        return i == Item.HORSE_ARMOR_IRON.id || i == Item.HORSE_ARMOR_GOLD.id || i == Item.HORSE_ARMOR_DIAMOND.id;
    }

    public boolean e() {
        return false;
    }
}
