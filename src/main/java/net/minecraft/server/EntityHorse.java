package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class EntityHorse extends EntityAnimal implements IInventoryListener {

    private static final IEntitySelector bu = new EntitySelectorHorse();
    private static final IAttribute bv = (new AttributeRanged("horse.jumpStrength", 0.7D, 0.0D, 2.0D)).a("Jump Strength").a(true);
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
    private InventoryHorseChest bG;
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

    public EntityHorse(World world) {
        super(world);
        this.a(1.4F, 1.6F);
        this.fireProof = false;
        this.m(false);
        this.getNavigation().a(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D));
        this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.7D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.cD();
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Integer.valueOf(0));
        this.datawatcher.a(19, Byte.valueOf((byte) 0));
        this.datawatcher.a(20, Integer.valueOf(0));
        this.datawatcher.a(21, String.valueOf(""));
        this.datawatcher.a(22, Integer.valueOf(0));
    }

    public void p(int i) {
        this.datawatcher.watch(19, Byte.valueOf((byte) i));
        this.cF();
    }

    public int bP() {
        return this.datawatcher.getByte(19);
    }

    public void q(int i) {
        this.datawatcher.watch(20, Integer.valueOf(i));
        this.cF();
    }

    public int bQ() {
        return this.datawatcher.getInt(20);
    }

    public String getLocalizedName() {
        if (this.hasCustomName()) {
            return this.getCustomName();
        } else {
            int i = this.bP();

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

    public boolean bR() {
        return !this.isBaby();
    }

    public boolean bS() {
        return this.w(2);
    }

    public boolean bW() {
        return this.bR();
    }

    public void b(String s) {
        this.datawatcher.watch(21, s);
    }

    public float bY() {
        int i = this.getAge();

        return i >= 0 ? 1.0F : 0.5F + (float) (-24000 - i) / -24000.0F * 0.5F;
    }

    public void a(boolean flag) {
        if (flag) {
            this.a(this.bY());
        } else {
            this.a(1.0F);
        }
    }

    public boolean bZ() {
        return this.br;
    }

    public void j(boolean flag) {
        this.b(2, flag);
    }

    public void k(boolean flag) {
        this.br = flag;
    }

    public boolean bC() {
        return !this.cu() && super.bC();
    }

    public boolean ca() {
        return this.w(8);
    }

    public int cb() {
        return this.datawatcher.getInt(22);
    }

    public int d(ItemStack itemstack) {
        return itemstack == null ? 0 : (itemstack.id == Item.HORSE_ARMOR_IRON.id ? 1 : (itemstack.id == Item.HORSE_ARMOR_GOLD.id ? 2 : (itemstack.id == Item.HORSE_ARMOR_DIAMOND.id ? 3 : 0)));
    }

    public boolean cc() {
        return this.w(32);
    }

    public boolean cd() {
        return this.w(64);
    }

    public boolean ce() {
        return this.w(16);
    }

    public boolean cf() {
        return this.bH;
    }

    public void r(int i) {
        this.datawatcher.watch(22, Integer.valueOf(i));
        this.cF();
    }

    public void l(boolean flag) {
        this.b(16, flag);
    }

    public void m(boolean flag) {
        this.b(8, flag);
    }

    public void n(boolean flag) {
        this.bH = flag;
    }

    public void o(boolean flag) {
        this.b(4, flag);
    }

    public int cg() {
        return this.bs;
    }

    public void s(int i) {
        this.bs = i;
    }

    public int t(int i) {
        int j = MathHelper.a(this.cg() + i, 0, this.cm());

        this.s(j);
        return j;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        Entity entity = damagesource.getEntity();

        if (this.passenger != null && this.passenger.equals(entity)) {
            return false;
        } else if (entity instanceof EntityWolf) {
            ((EntityWolf) entity).setTarget((Entity) null);
            return false;
        } else {
            return super.damageEntity(damagesource, f);
        }
    }

    public int aM() {
        return by[this.cb()];
    }

    public boolean L() {
        return this.passenger == null;
    }

    public boolean ch() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locZ);

        this.world.getBiome(i, j);
        return true;
    }

    public void ci() {
        if (!this.world.isStatic && this.ca()) {
            this.b(Block.CHEST.id, 1);
            this.m(false);
        }
    }

    private void cB() {
        this.cI();
        this.world.makeSound(this, "eating", 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }

    protected void b(float f) {
        if (f > 1.0F) {
            this.makeSound("mob.horse.land", 0.4F, 1.0F);
        }

        int i = MathHelper.f(f * 0.5F - 3.0F);

        if (i > 0) {
            this.damageEntity(DamageSource.FALL, (float) i);
            if (this.passenger != null) {
                this.passenger.damageEntity(DamageSource.FALL, (float) i);
            }

            int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.2D - (double) this.lastYaw), MathHelper.floor(this.locZ));

            if (j > 0) {
                StepSound stepsound = Block.byId[j].stepSound;

                this.world.makeSound(this, stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    private int cC() {
        int i = this.bP();

        return this.ca() && (i == 1 || i == 2) ? 17 : 2;
    }

    private void cD() {
        InventoryHorseChest inventoryhorsechest = this.bG;

        this.bG = new InventoryHorseChest("HorseChest", this.cC());
        this.bG.a(this.getLocalizedName());
        if (inventoryhorsechest != null) {
            inventoryhorsechest.b(this);
            int i = Math.min(inventoryhorsechest.getSize(), this.bG.getSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryhorsechest.getItem(j);

                if (itemstack != null) {
                    this.bG.setItem(j, itemstack.cloneItemStack());
                }
            }

            inventoryhorsechest = null;
        }

        this.bG.a(this);
        this.cE();
    }

    private void cE() {
        if (!this.world.isStatic) {
            this.o(this.bG.getItem(0) != null);
            if (this.cr()) {
                this.r(this.d(this.bG.getItem(1)));
            }
        }
    }

    public void a(InventorySubcontainer inventorysubcontainer) {
        int i = this.cb();
        boolean flag = this.ck();

        this.cE();
        if (this.ticksLived > 20) {
            if (i == 0 && i != this.cb()) {
                this.makeSound("mob.horse.armor", 0.5F, 1.0F);
            }

            if (!flag && this.ck()) {
                this.makeSound("mob.horse.leather", 0.5F, 1.0F);
            }
        }
    }

    public boolean canSpawn() {
        this.ch();
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

    public double cj() {
        return this.a(bv).e();
    }

    protected String aL() {
        this.cI();
        int i = this.bP();

        return i == 3 ? "mob.horse.zombie.death" : (i == 4 ? "mob.horse.skeleton.death" : (i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    protected int getLootId() {
        boolean flag = this.random.nextInt(4) == 0;
        int i = this.bP();

        return i == 4 ? Item.BONE.id : (i == 3 ? (flag ? 0 : Item.ROTTEN_FLESH.id) : Item.LEATHER.id);
    }

    protected String aK() {
        this.cI();
        if (this.random.nextInt(3) == 0) {
            this.cK();
        }

        int i = this.bP();

        return i == 3 ? "mob.horse.zombie.hit" : (i == 4 ? "mob.horse.skeleton.hit" : (i != 1 && i != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean ck() {
        return this.w(4);
    }

    protected String r() {
        this.cI();
        if (this.random.nextInt(10) == 0 && !this.aY()) {
            this.cK();
        }

        int i = this.bP();

        return i == 3 ? "mob.horse.zombie.idle" : (i == 4 ? "mob.horse.skeleton.idle" : (i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String cl() {
        this.cI();
        this.cK();
        int i = this.bP();

        return i != 3 && i != 4 ? (i != 1 && i != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    protected void a(int i, int j, int k, int l) {
        StepSound stepsound = Block.byId[l].stepSound;

        if (this.world.getTypeId(i, j + 1, k) == Block.SNOW.id) {
            stepsound = Block.SNOW.stepSound;
        }

        if (!Block.byId[l].material.isLiquid()) {
            int i1 = this.bP();

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

    protected void ax() {
        super.ax();
        this.aT().b(bv);
        this.a(GenericAttributes.a).a(53.0D);
        this.a(GenericAttributes.d).a(0.22499999403953552D);
    }

    public int br() {
        return 6;
    }

    public int cm() {
        return 100;
    }

    protected float aW() {
        return 0.8F;
    }

    public int o() {
        return 400;
    }

    private void cF() {
        this.bQ = null;
    }

    public void f(EntityHuman entityhuman) {
        if (!this.world.isStatic && (this.passenger == null || this.passenger == entityhuman) && this.bS()) {
            this.bG.a(this.getLocalizedName());
            entityhuman.openHorseInventory(this, this.bG);
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.MONSTER_EGG.id) {
            return super.a(entityhuman);
        } else if (!this.bS() && this.cu()) {
            return false;
        } else if (this.bS() && this.bR() && entityhuman.isSneaking()) {
            this.f(entityhuman);
            return true;
        } else if (this.bW() && this.passenger != null) {
            return super.a(entityhuman);
        } else {
            if (itemstack != null) {
                boolean flag = false;

                if (this.cr()) {
                    byte b0 = -1;

                    if (itemstack.id == Item.HORSE_ARMOR_IRON.id) {
                        b0 = 1;
                    } else if (itemstack.id == Item.HORSE_ARMOR_GOLD.id) {
                        b0 = 2;
                    } else if (itemstack.id == Item.HORSE_ARMOR_DIAMOND.id) {
                        b0 = 3;
                    }

                    if (b0 >= 0) {
                        if (!this.bS()) {
                            this.cz();
                            return true;
                        }

                        this.f(entityhuman);
                        return true;
                    }
                }

                if (!flag && !this.cu()) {
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
                        if (this.bS() && this.getAge() == 0) {
                            flag = true;
                            this.bT();
                        }
                    } else if (itemstack.id == Item.GOLDEN_APPLE.id) {
                        f = 10.0F;
                        short1 = 240;
                        b1 = 10;
                        if (this.bS() && this.getAge() == 0) {
                            flag = true;
                            this.bT();
                        }
                    }

                    if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
                        this.heal(f);
                        flag = true;
                    }

                    if (!this.bR() && short1 > 0) {
                        this.a(short1);
                        flag = true;
                    }

                    if (b1 > 0 && (flag || !this.bS()) && b1 < this.cm()) {
                        flag = true;
                        this.t(b1);
                    }

                    if (flag) {
                        this.cB();
                    }
                }

                if (!this.bS() && !flag) {
                    if (itemstack != null && itemstack.a(entityhuman, (EntityLiving) this)) {
                        return true;
                    }

                    this.cz();
                    return true;
                }

                if (!flag && this.cs() && !this.ca() && itemstack.id == Block.CHEST.id) {
                    this.m(true);
                    this.makeSound("mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    flag = true;
                    this.cD();
                }

                if (!flag && this.bW() && !this.ck() && itemstack.id == Item.SADDLE.id) {
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

            if (this.bW() && this.passenger == null) {
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
        this.p(false);
        this.q(false);
        if (!this.world.isStatic) {
            entityhuman.mount(this);
        }
    }

    public boolean cr() {
        return this.bP() == 0;
    }

    public boolean cs() {
        int i = this.bP();

        return i == 2 || i == 1;
    }

    protected boolean aY() {
        return this.passenger != null && this.ck() ? true : this.cc() || this.cd();
    }

    public boolean cu() {
        int i = this.bP();

        return i == 3 || i == 4;
    }

    public boolean cv() {
        return this.cu() || this.bP() == 2;
    }

    public boolean c(ItemStack itemstack) {
        return false;
    }

    private void cH() {
        this.bp = 1;
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (!this.world.isStatic) {
            this.cA();
        }
    }

    public void c() {
        if (this.random.nextInt(200) == 0) {
            this.cH();
        }

        super.c();
        if (!this.world.isStatic) {
            if (this.random.nextInt(900) == 0 && this.deathTicks == 0) {
                this.heal(1.0F);
            }

            if (!this.cc() && this.passenger == null && this.random.nextInt(300) == 0 && this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY) - 1, MathHelper.floor(this.locZ)) == Block.GRASS.id) {
                this.p(true);
            }

            if (this.cc() && ++this.bD > 50) {
                this.bD = 0;
                this.p(false);
            }

            if (this.ce() && !this.bR() && !this.cc()) {
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
            this.cF();
        }

        if (this.bE > 0 && ++this.bE > 30) {
            this.bE = 0;
            this.b(128, false);
        }

        if (!this.world.isStatic && this.bF > 0 && ++this.bF > 20) {
            this.bF = 0;
            this.q(false);
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
        if (this.cc()) {
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
        if (this.cd()) {
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

    private void cI() {
        if (!this.world.isStatic) {
            this.bE = 1;
            this.b(128, true);
        }
    }

    private boolean cJ() {
        return this.passenger == null && this.vehicle == null && this.bS() && this.bR() && !this.cv() && this.getHealth() >= this.getMaxHealth();
    }

    public void die() {
        if (this.world.isStatic || !this.bS() && !this.ce() || this.getHealth() <= 0.0F) {
            super.die();
        }
    }

    public void e(boolean flag) {
        this.b(32, flag);
    }

    public void p(boolean flag) {
        this.e(flag);
    }

    public void q(boolean flag) {
        if (flag) {
            this.p(false);
        }

        this.b(64, flag);
    }

    private void cK() {
        if (!this.world.isStatic) {
            this.bF = 1;
            this.q(true);
        }
    }

    public void cz() {
        this.cK();
        String s = this.cl();

        if (s != null) {
            this.makeSound(s, this.aW(), this.aX());
        }
    }

    public void cA() {
        this.a(this, this.bG);
        this.ci();
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
        this.b(entityhuman.getName());
        this.j(true);
        return true;
    }

    public void e(float f, float f1) {
        if (this.passenger != null && this.ck()) {
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

            if (this.onGround && this.bt == 0.0F && this.cd() && !this.bI) {
                f = 0.0F;
                f1 = 0.0F;
            }

            if (this.bt > 0.0F && !this.bZ() && this.onGround) {
                this.motY = this.cj() * (double) this.bt;
                if (this.hasEffect(MobEffectList.JUMP)) {
                    this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
                }

                this.k(true);
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
            this.aR = this.bc() * 0.1F;
            if (!this.world.isStatic) {
                this.i((float) this.a(GenericAttributes.d).e());
                super.e(f, f1);
            }

            if (this.onGround) {
                this.bt = 0.0F;
                this.k(false);
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
        nbttagcompound.setBoolean("EatingHaystack", this.cc());
        nbttagcompound.setBoolean("ChestedHorse", this.ca());
        nbttagcompound.setBoolean("HasReproduced", this.cf());
        nbttagcompound.setBoolean("Bred", this.ce());
        nbttagcompound.setInt("Type", this.bP());
        nbttagcompound.setInt("Variant", this.bQ());
        nbttagcompound.setInt("Temper", this.cg());
        nbttagcompound.setBoolean("Tame", this.bS());
        if (this.ca()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < this.bG.getSize(); ++i) {
                ItemStack itemstack = this.bG.getItem(i);

                if (itemstack != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.setByte("Slot", (byte) i);
                    itemstack.save(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }

            nbttagcompound.set("Items", nbttaglist);
        }

        if (this.bG.getItem(1) != null) {
            nbttagcompound.set("ArmorItem", this.bG.getItem(1).save(new NBTTagCompound("ArmorItem")));
        }

        if (this.bG.getItem(0) != null) {
            nbttagcompound.set("SaddleItem", this.bG.getItem(0).save(new NBTTagCompound("SaddleItem")));
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.p(nbttagcompound.getBoolean("EatingHaystack"));
        this.l(nbttagcompound.getBoolean("Bred"));
        this.m(nbttagcompound.getBoolean("ChestedHorse"));
        this.n(nbttagcompound.getBoolean("HasReproduced"));
        this.p(nbttagcompound.getInt("Type"));
        this.q(nbttagcompound.getInt("Variant"));
        this.s(nbttagcompound.getInt("Temper"));
        this.j(nbttagcompound.getBoolean("Tame"));
        AttributeInstance attributeinstance = this.aT().a("Speed");

        if (attributeinstance != null) {
            this.a(GenericAttributes.d).a(attributeinstance.b() * 0.25D);
        }

        if (this.ca()) {
            NBTTagList nbttaglist = nbttagcompound.getList("Items");

            this.cD();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 2 && j < this.bG.getSize()) {
                    this.bG.setItem(j, ItemStack.createStack(nbttagcompound1));
                }
            }
        }

        ItemStack itemstack;

        if (nbttagcompound.hasKey("ArmorItem")) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("ArmorItem"));
            if (itemstack != null && v(itemstack.id)) {
                this.bG.setItem(1, itemstack);
            }
        }

        if (nbttagcompound.hasKey("SaddleItem")) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("SaddleItem"));
            if (itemstack != null && itemstack.id == Item.SADDLE.id) {
                this.bG.setItem(0, itemstack);
            }
        } else if (nbttagcompound.getBoolean("Saddle")) {
            this.bG.setItem(0, new ItemStack(Item.SADDLE));
        }

        this.cE();
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (entityanimal.getClass() != this.getClass()) {
            return false;
        } else {
            EntityHorse entityhorse = (EntityHorse) entityanimal;

            if (this.cJ() && entityhorse.cJ()) {
                int i = this.bP();
                int j = entityhorse.bP();

                return i == j || i == 0 && j == 1 || i == 1 && j == 0;
            } else {
                return false;
            }
        }
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        EntityHorse entityhorse = (EntityHorse) entityageable;
        EntityHorse entityhorse1 = new EntityHorse(this.world);
        int i = this.bP();
        int j = entityhorse.bP();
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
                i1 = this.bQ() & 255;
            } else if (l < 8) {
                i1 = entityhorse.bQ() & 255;
            } else {
                i1 = this.random.nextInt(7);
            }

            int j1 = this.random.nextInt(5);

            if (j1 < 4) {
                i1 |= this.bQ() & '\uff00';
            } else if (j1 < 8) {
                i1 |= entityhorse.bQ() & '\uff00';
            } else {
                i1 |= this.random.nextInt(5) << 8 & '\uff00';
            }

            entityhorse1.q(i1);
        }

        entityhorse1.p(k);
        double d0 = this.a(GenericAttributes.a).b() + entityageable.a(GenericAttributes.a).b() + (double) this.cL();

        entityhorse1.a(GenericAttributes.a).a(d0 / 3.0D);
        double d1 = this.a(bv).b() + entityageable.a(bv).b() + this.cM();

        entityhorse1.a(bv).a(d1 / 3.0D);
        double d2 = this.a(GenericAttributes.d).b() + entityageable.a(GenericAttributes.d).b() + this.cN();

        entityhorse1.a(GenericAttributes.d).a(d2 / 3.0D);
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

        this.p(j);
        this.q(i);
        if (this.random.nextInt(5) == 0) {
            this.setAge(-24000);
        }

        if (j != 4 && j != 3) {
            this.a(GenericAttributes.a).a((double) this.cL());
            if (j == 0) {
                this.a(GenericAttributes.d).a(this.cN());
            } else {
                this.a(GenericAttributes.d).a(0.17499999701976776D);
            }
        } else {
            this.a(GenericAttributes.a).a(15.0D);
            this.a(GenericAttributes.d).a(0.20000000298023224D);
        }

        if (j != 2 && j != 1) {
            this.a(bv).a(this.cM());
        } else {
            this.a(bv).a(0.5D);
        }

        this.setHealth(this.getMaxHealth());
        return (GroupDataEntity) object;
    }

    protected boolean bb() {
        return true;
    }

    public void u(int i) {
        if (i < 0) {
            i = 0;
        } else {
            this.bI = true;
            this.cK();
        }

        if (i >= 90) {
            this.bt = 1.0F;
        } else {
            this.bt = 0.4F + 0.4F * (float) i / 90.0F;
        }
    }

    public void U() {
        super.U();
        if (this.bM > 0.0F) {
            float f = MathHelper.sin(this.aN * 3.1415927F / 180.0F);
            float f1 = MathHelper.cos(this.aN * 3.1415927F / 180.0F);
            float f2 = 0.7F * this.bM;
            float f3 = 0.15F * this.bM;

            this.passenger.setPosition(this.locX + (double) (f2 * f), this.locY + this.W() + this.passenger.V() + (double) f3, this.locZ - (double) (f2 * f1));
            if (this.passenger instanceof EntityLiving) {
                ((EntityLiving) this.passenger).aN = this.aN;
            }
        }
    }

    private float cL() {
        return 15.0F + (float) this.random.nextInt(8) + (float) this.random.nextInt(9);
    }

    private double cM() {
        return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
    }

    private double cN() {
        return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
    }

    public static boolean v(int i) {
        return i == Item.HORSE_ARMOR_IRON.id || i == Item.HORSE_ARMOR_GOLD.id || i == Item.HORSE_ARMOR_DIAMOND.id;
    }

    public boolean e() {
        return false;
    }
}
