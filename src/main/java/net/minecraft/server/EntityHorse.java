package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
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

    protected void c() {
        super.c();
        this.datawatcher.a(16, Integer.valueOf(0));
        this.datawatcher.a(19, Byte.valueOf((byte) 0));
        this.datawatcher.a(20, Integer.valueOf(0));
        this.datawatcher.a(21, String.valueOf(""));
        this.datawatcher.a(22, Integer.valueOf(0));
    }

    public void setType(int i) {
        this.datawatcher.watch(19, Byte.valueOf((byte) i));
        this.cP();
    }

    public int getType() {
        return this.datawatcher.getByte(19);
    }

    public void setVariant(int i) {
        this.datawatcher.watch(20, Integer.valueOf(i));
        this.cP();
    }

    public int getVariant() {
        return this.datawatcher.getInt(20);
    }

    public String getName() {
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

    private boolean x(int i) {
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

    public boolean cb() {
        return !this.isBaby();
    }

    public boolean isTame() {
        return this.x(2);
    }

    public boolean cg() {
        return this.cb();
    }

    public String getOwnerUUID() {
        return this.datawatcher.getString(21);
    }

    public void setOwnerUUID(String s) {
        this.datawatcher.watch(21, s);
    }

    public float ci() {
        int i = this.getAge();

        return i >= 0 ? 1.0F : 0.5F + (float) (-24000 - i) / -24000.0F * 0.5F;
    }

    public void a(boolean flag) {
        if (flag) {
            this.a(this.ci());
        } else {
            this.a(1.0F);
        }
    }

    public boolean cj() {
        return this.br;
    }

    public void setTame(boolean flag) {
        this.b(2, flag);
    }

    public void j(boolean flag) {
        this.br = flag;
    }

    public boolean bM() {
        return !this.cE() && super.bM();
    }

    protected void o(float f) {
        if (f > 6.0F && this.cm()) {
            this.o(false);
        }
    }

    public boolean hasChest() {
        return this.x(8);
    }

    public int cl() {
        return this.datawatcher.getInt(22);
    }

    private int e(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            Item item = itemstack.getItem();

            return item == Items.HORSE_ARMOR_IRON ? 1 : (item == Items.HORSE_ARMOR_GOLD ? 2 : (item == Items.HORSE_ARMOR_DIAMOND ? 3 : 0));
        }
    }

    public boolean cm() {
        return this.x(32);
    }

    public boolean cn() {
        return this.x(64);
    }

    public boolean co() {
        return this.x(16);
    }

    public boolean cp() {
        return this.bH;
    }

    public void d(ItemStack itemstack) {
        this.datawatcher.watch(22, Integer.valueOf(this.e(itemstack)));
        this.cP();
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

    public int v(int i) {
        int j = MathHelper.a(this.getTemper() + i, 0, this.getMaxDomestication());

        this.setTemper(j);
        return j;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        Entity entity = damagesource.getEntity();

        return this.passenger != null && this.passenger.equals(entity) ? false : super.damageEntity(damagesource, f);
    }

    public int aU() {
        return by[this.cl()];
    }

    public boolean R() {
        return this.passenger == null;
    }

    public boolean cr() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locZ);

        this.world.getBiome(i, j);
        return true;
    }

    public void cs() {
        if (!this.world.isStatic && this.hasChest()) {
            this.a(Item.getItemOf(Blocks.CHEST), 1);
            this.setHasChest(false);
        }
    }

    private void cL() {
        this.cS();
        this.world.makeSound(this, "eating", 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }

    protected void b(float f) {
        if (f > 1.0F) {
            this.makeSound("mob.horse.land", 0.4F, 1.0F);
        }

        int i = MathHelper.f(f * 0.5F - 3.0F);

        if (i > 0) {
            // CraftBukkit start - fire EntityDamageEvent
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

            Block block = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.2D - (double) this.lastYaw), MathHelper.floor(this.locZ));

            if (block.getMaterial() != Material.AIR) {
                StepSound stepsound = block.stepSound;

                this.world.makeSound(this, stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    private int cM() {
        int i = this.getType();

        return this.hasChest() /* && (i == 1 || i == 2) */ ? 17 : 2; // CraftBukkit - Remove type check
    }

    public void loadChest() { // CraftBukkit - private -> public
        InventoryHorseChest inventoryhorsechest = this.inventoryChest;

        this.inventoryChest = new InventoryHorseChest("HorseChest", this.cM(), this); // CraftBukkit - add this horse
        this.inventoryChest.a(this.getName());
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
        this.cO();
    }

    private void cO() {
        if (!this.world.isStatic) {
            this.n(this.inventoryChest.getItem(0) != null);
            if (this.cB()) {
                this.d(this.inventoryChest.getItem(1));
            }
        }
    }

    public void a(InventorySubcontainer inventorysubcontainer) {
        int i = this.cl();
        boolean flag = this.cu();

        this.cO();
        if (this.ticksLived > 20) {
            if (i == 0 && i != this.cl()) {
                this.makeSound("mob.horse.armor", 0.5F, 1.0F);
            } else if (i != this.cl()) {
                this.makeSound("mob.horse.armor", 0.5F, 1.0F);
            }

            if (!flag && this.cu()) {
                this.makeSound("mob.horse.leather", 0.5F, 1.0F);
            }
        }
    }

    public boolean canSpawn() {
        this.cr();
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

    protected String aT() {
        this.cS();
        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.death" : (i == 4 ? "mob.horse.skeleton.death" : (i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    protected Item getLoot() {
        boolean flag = this.random.nextInt(4) == 0;
        int i = this.getType();

        return i == 4 ? Items.BONE : (i == 3 ? (flag ? Item.d(0) : Items.ROTTEN_FLESH) : Items.LEATHER);
    }

    protected String aS() {
        this.cS();
        if (this.random.nextInt(3) == 0) {
            this.cU();
        }

        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.hit" : (i == 4 ? "mob.horse.skeleton.hit" : (i != 1 && i != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean cu() {
        return this.x(4);
    }

    protected String t() {
        this.cS();
        if (this.random.nextInt(10) == 0 && !this.bg()) {
            this.cU();
        }

        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.idle" : (i == 4 ? "mob.horse.skeleton.idle" : (i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String cv() {
        this.cS();
        this.cU();
        int i = this.getType();

        return i != 3 && i != 4 ? (i != 1 && i != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    protected void a(int i, int j, int k, Block block) {
        StepSound stepsound = block.stepSound;

        if (this.world.getType(i, j + 1, k) == Blocks.SNOW) {
            stepsound = Blocks.SNOW.stepSound;
        }

        if (!block.getMaterial().isLiquid()) {
            int l = this.getType();

            if (this.passenger != null && l != 1 && l != 2) {
                ++this.bP;
                if (this.bP > 5 && this.bP % 3 == 0) {
                    this.makeSound("mob.horse.gallop", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    if (l == 0 && this.random.nextInt(10) == 0) {
                        this.makeSound("mob.horse.breathe", stepsound.getVolume1() * 0.6F, stepsound.getVolume2());
                    }
                } else if (this.bP <= 5) {
                    this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                }
            } else if (stepsound == Block.f) {
                this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            } else {
                this.makeSound("mob.horse.soft", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            }
        }
    }

    protected void aC() {
        super.aC();
        this.bb().b(attributeJumpStrength);
        this.getAttributeInstance(GenericAttributes.a).setValue(53.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.22499999403953552D);
    }

    public int bB() {
        return 6;
    }

    public int getMaxDomestication() {
        return this.maxDomestication; // CraftBukkit - return stored max domestication instead of 100
    }

    protected float be() {
        return 0.8F;
    }

    public int q() {
        return 400;
    }

    private void cP() {
        this.bQ = null;
    }

    public void g(EntityHuman entityhuman) {
        if (!this.world.isStatic && (this.passenger == null || this.passenger == entityhuman) && this.isTame()) {
            this.inventoryChest.a(this.getName());
            entityhuman.openHorseInventory(this, this.inventoryChest);
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.getItem() == Items.MONSTER_EGG) {
            return super.a(entityhuman);
        } else if (!this.isTame() && this.cE()) {
            return false;
        } else if (this.isTame() && this.cb() && entityhuman.isSneaking()) {
            this.g(entityhuman);
            return true;
        } else if (this.cg() && this.passenger != null) {
            return super.a(entityhuman);
        } else {
            if (itemstack != null) {
                boolean flag = false;

                if (this.cB()) {
                    byte b0 = -1;

                    if (itemstack.getItem() == Items.HORSE_ARMOR_IRON) {
                        b0 = 1;
                    } else if (itemstack.getItem() == Items.HORSE_ARMOR_GOLD) {
                        b0 = 2;
                    } else if (itemstack.getItem() == Items.HORSE_ARMOR_DIAMOND) {
                        b0 = 3;
                    }

                    if (b0 >= 0) {
                        if (!this.isTame()) {
                            this.cJ();
                            return true;
                        }

                        this.g(entityhuman);
                        return true;
                    }
                }

                if (!flag && !this.cE()) {
                    float f = 0.0F;
                    short short1 = 0;
                    byte b1 = 0;

                    if (itemstack.getItem() == Items.WHEAT) {
                        f = 2.0F;
                        short1 = 60;
                        b1 = 3;
                    } else if (itemstack.getItem() == Items.SUGAR) {
                        f = 1.0F;
                        short1 = 30;
                        b1 = 3;
                    } else if (itemstack.getItem() == Items.BREAD) {
                        f = 7.0F;
                        short1 = 180;
                        b1 = 3;
                    } else if (Block.a(itemstack.getItem()) == Blocks.HAY_BLOCK) {
                        f = 20.0F;
                        short1 = 180;
                    } else if (itemstack.getItem() == Items.APPLE) {
                        f = 3.0F;
                        short1 = 60;
                        b1 = 3;
                    } else if (itemstack.getItem() == Items.CARROT_GOLDEN) {
                        f = 4.0F;
                        short1 = 60;
                        b1 = 5;
                        if (this.isTame() && this.getAge() == 0) {
                            flag = true;
                            this.f(entityhuman);
                        }
                    } else if (itemstack.getItem() == Items.GOLDEN_APPLE) {
                        f = 10.0F;
                        short1 = 240;
                        b1 = 10;
                        if (this.isTame() && this.getAge() == 0) {
                            flag = true;
                            this.f(entityhuman);
                        }
                    }

                    if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
                        this.heal(f, RegainReason.EATING); // CraftBukkit
                        flag = true;
                    }

                    if (!this.cb() && short1 > 0) {
                        this.a(short1);
                        flag = true;
                    }

                    if (b1 > 0 && (flag || !this.isTame()) && b1 < this.getMaxDomestication()) {
                        flag = true;
                        this.v(b1);
                    }

                    if (flag) {
                        this.cL();
                    }
                }

                if (!this.isTame() && !flag) {
                    if (itemstack != null && itemstack.a(entityhuman, (EntityLiving) this)) {
                        return true;
                    }

                    this.cJ();
                    return true;
                }

                if (!flag && this.cC() && !this.hasChest() && itemstack.getItem() == Item.getItemOf(Blocks.CHEST)) {
                    this.setHasChest(true);
                    this.makeSound("mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    flag = true;
                    this.loadChest();
                }

                if (!flag && this.cg() && !this.cu() && itemstack.getItem() == Items.SADDLE) {
                    this.g(entityhuman);
                    return true;
                }

                if (flag) {
                    if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count == 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                    }

                    return true;
                }
            }

            if (this.cg() && this.passenger == null) {
                if (itemstack != null && itemstack.a(entityhuman, (EntityLiving) this)) {
                    return true;
                } else {
                    this.i(entityhuman);
                    return true;
                }
            } else {
                return super.a(entityhuman);
            }
        }
    }

    private void i(EntityHuman entityhuman) {
        entityhuman.yaw = this.yaw;
        entityhuman.pitch = this.pitch;
        this.o(false);
        this.p(false);
        if (!this.world.isStatic) {
            entityhuman.mount(this);
        }
    }

    public boolean cB() {
        return this.getType() == 0;
    }

    public boolean cC() {
        int i = this.getType();

        return i == 2 || i == 1;
    }

    protected boolean bg() {
        return this.passenger != null && this.cu() ? true : this.cm() || this.cn();
    }

    public boolean cE() {
        int i = this.getType();

        return i == 3 || i == 4;
    }

    public boolean cF() {
        return this.cE() || this.getType() == 2;
    }

    public boolean c(ItemStack itemstack) {
        return false;
    }

    private void cR() {
        this.bp = 1;
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (!this.world.isStatic) {
            this.cK();
        }
    }

    public void e() {
        if (this.random.nextInt(200) == 0) {
            this.cR();
        }

        super.e();
        if (!this.world.isStatic) {
            if (this.random.nextInt(900) == 0 && this.deathTicks == 0) {
                this.heal(1.0F, RegainReason.REGEN); // CraftBukkit
            }

            if (!this.cm() && this.passenger == null && this.random.nextInt(300) == 0 && this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY) - 1, MathHelper.floor(this.locZ)) == Blocks.GRASS) {
                this.o(true);
            }

            if (this.cm() && ++this.bD > 50) {
                this.bD = 0;
                this.o(false);
            }

            if (this.co() && !this.cb() && !this.cm()) {
                EntityHorse entityhorse = this.a(this, 16.0D);

                if (entityhorse != null && this.f(entityhorse) > 4.0D) {
                    PathEntity pathentity = this.world.findPath(this, entityhorse, 16.0F, true, false, false, true);

                    this.setPathEntity(pathentity);
                }
            }
        }
    }

    public void h() {
        super.h();
        if (this.world.isStatic && this.datawatcher.a()) {
            this.datawatcher.e();
            this.cP();
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
        if (this.cm()) {
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
        if (this.cn()) {
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
        if (this.x(128)) {
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

    private void cS() {
        if (!this.world.isStatic) {
            this.bE = 1;
            this.b(128, true);
        }
    }

    private boolean cT() {
        return this.passenger == null && this.vehicle == null && this.isTame() && this.cb() && !this.cF() && this.getHealth() >= this.getMaxHealth();
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

    private void cU() {
        if (!this.world.isStatic) {
            this.bF = 1;
            this.p(true);
        }
    }

    public void cJ() {
        this.cU();
        String s = this.cv();

        if (s != null) {
            this.makeSound(s, this.be(), this.bf());
        }
    }

    public void cK() {
        this.a(this, this.inventoryChest);
        this.cs();
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

    public boolean h(EntityHuman entityhuman) {
        this.setOwnerUUID(entityhuman.getUniqueID().toString());
        this.setTame(true);
        return true;
    }

    public void e(float f, float f1) {
        if (this.passenger != null && this.passenger instanceof EntityLiving && this.cu()) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.b(this.yaw, this.pitch);
            this.aO = this.aM = this.yaw;
            f = ((EntityLiving) this.passenger).bd * 0.5F;
            f1 = ((EntityLiving) this.passenger).be;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
                this.bP = 0;
            }

            if (this.onGround && this.bt == 0.0F && this.cn() && !this.bI) {
                f = 0.0F;
                f1 = 0.0F;
            }

            if (this.bt > 0.0F && !this.cj() && this.onGround) {
                this.motY = this.getJumpStrength() * (double) this.bt;
                if (this.hasEffect(MobEffectList.JUMP)) {
                    this.motY += (double) ((float) (this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
                }

                this.j(true);
                this.al = true;
                if (f1 > 0.0F) {
                    float f2 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
                    float f3 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);

                    this.motX += (double) (-0.4F * f2 * this.bt);
                    this.motZ += (double) (0.4F * f3 * this.bt);
                    this.makeSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.bt = 0.0F;
            }

            this.W = 1.0F;
            this.aQ = this.bk() * 0.1F;
            if (!this.world.isStatic) {
                this.i((float) this.getAttributeInstance(GenericAttributes.d).getValue());
                super.e(f, f1);
            }

            if (this.onGround) {
                this.bt = 0.0F;
                this.j(false);
            }

            this.aE = this.aF;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.aF += (f4 - this.aF) * 0.4F;
            this.aG += this.aF;
        } else {
            this.W = 0.5F;
            this.aQ = 0.02F;
            super.e(f, f1);
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("EatingHaystack", this.cm());
        nbttagcompound.setBoolean("ChestedHorse", this.hasChest());
        nbttagcompound.setBoolean("HasReproduced", this.cp());
        nbttagcompound.setBoolean("Bred", this.co());
        nbttagcompound.setInt("Type", this.getType());
        nbttagcompound.setInt("Variant", this.getVariant());
        nbttagcompound.setInt("Temper", this.getTemper());
        nbttagcompound.setBoolean("Tame", this.isTame());
        nbttagcompound.setString("OwnerUUID", this.getOwnerUUID());
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
            nbttagcompound.set("ArmorItem", this.inventoryChest.getItem(1).save(new NBTTagCompound()));
        }

        if (this.inventoryChest.getItem(0) != null) {
            nbttagcompound.set("SaddleItem", this.inventoryChest.getItem(0).save(new NBTTagCompound()));
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
        if (nbttagcompound.hasKeyOfType("OwnerUUID", 8)) {
            this.setOwnerUUID(nbttagcompound.getString("OwnerUUID"));
        }
        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxDomestication")) {
            this.maxDomestication = nbttagcompound.getInt("Bukkit.MaxDomestication");
        }
        // CraftBukkit end
        AttributeInstance attributeinstance = this.bb().a("Speed");

        if (attributeinstance != null) {
            this.getAttributeInstance(GenericAttributes.d).setValue(attributeinstance.b() * 0.25D);
        }

        if (this.hasChest()) {
            NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);

            this.loadChest();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 2 && j < this.inventoryChest.getSize()) {
                    this.inventoryChest.setItem(j, ItemStack.createStack(nbttagcompound1));
                }
            }
        }

        ItemStack itemstack;

        if (nbttagcompound.hasKeyOfType("ArmorItem", 10)) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("ArmorItem"));
            if (itemstack != null && a(itemstack.getItem())) {
                this.inventoryChest.setItem(1, itemstack);
            }
        }

        if (nbttagcompound.hasKeyOfType("SaddleItem", 10)) {
            itemstack = ItemStack.createStack(nbttagcompound.getCompound("SaddleItem"));
            if (itemstack != null && itemstack.getItem() == Items.SADDLE) {
                this.inventoryChest.setItem(0, itemstack);
            }
        } else if (nbttagcompound.getBoolean("Saddle")) {
            this.inventoryChest.setItem(0, new ItemStack(Items.SADDLE));
        }

        this.cO();
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (entityanimal.getClass() != this.getClass()) {
            return false;
        } else {
            EntityHorse entityhorse = (EntityHorse) entityanimal;

            if (this.cT() && entityhorse.cT()) {
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

            if (j1 < 2) {
                i1 |= this.getVariant() & '\uff00';
            } else if (j1 < 4) {
                i1 |= entityhorse.getVariant() & '\uff00';
            } else {
                i1 |= this.random.nextInt(5) << 8 & '\uff00';
            }

            entityhorse1.setVariant(i1);
        }

        entityhorse1.setType(k);
        double d0 = this.getAttributeInstance(GenericAttributes.a).b() + entityageable.getAttributeInstance(GenericAttributes.a).b() + (double) this.cV();

        entityhorse1.getAttributeInstance(GenericAttributes.a).setValue(d0 / 3.0D);
        double d1 = this.getAttributeInstance(attributeJumpStrength).b() + entityageable.getAttributeInstance(attributeJumpStrength).b() + this.cW();

        entityhorse1.getAttributeInstance(attributeJumpStrength).setValue(d1 / 3.0D);
        double d2 = this.getAttributeInstance(GenericAttributes.d).b() + entityageable.getAttributeInstance(GenericAttributes.d).b() + this.cX();

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
            this.getAttributeInstance(GenericAttributes.a).setValue((double) this.cV());
            if (j == 0) {
                this.getAttributeInstance(GenericAttributes.d).setValue(this.cX());
            } else {
                this.getAttributeInstance(GenericAttributes.d).setValue(0.17499999701976776D);
            }
        } else {
            this.getAttributeInstance(GenericAttributes.a).setValue(15.0D);
            this.getAttributeInstance(GenericAttributes.d).setValue(0.20000000298023224D);
        }

        if (j != 2 && j != 1) {
            this.getAttributeInstance(attributeJumpStrength).setValue(this.cW());
        } else {
            this.getAttributeInstance(attributeJumpStrength).setValue(0.5D);
        }

        this.setHealth(this.getMaxHealth());
        return (GroupDataEntity) object;
    }

    protected boolean bj() {
        return true;
    }

    public void w(int i) {
        if (this.cu()) {
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
                this.cU();
                this.bt = event.getPower();
            }
            // CraftBukkit end
        }
    }

    public void ab() {
        super.ab();
        if (this.bM > 0.0F) {
            float f = MathHelper.sin(this.aM * 3.1415927F / 180.0F);
            float f1 = MathHelper.cos(this.aM * 3.1415927F / 180.0F);
            float f2 = 0.7F * this.bM;
            float f3 = 0.15F * this.bM;

            this.passenger.setPosition(this.locX + (double) (f2 * f), this.locY + this.ad() + this.passenger.ac() + (double) f3, this.locZ - (double) (f2 * f1));
            if (this.passenger instanceof EntityLiving) {
                ((EntityLiving) this.passenger).aM = this.aM;
            }
        }
    }

    private float cV() {
        return 15.0F + (float) this.random.nextInt(8) + (float) this.random.nextInt(9);
    }

    private double cW() {
        return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
    }

    private double cX() {
        return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
    }

    public static boolean a(Item item) {
        return item == Items.HORSE_ARMOR_IRON || item == Items.HORSE_ARMOR_GOLD || item == Items.HORSE_ARMOR_DIAMOND;
    }

    public boolean h_() {
        return false;
    }
}
