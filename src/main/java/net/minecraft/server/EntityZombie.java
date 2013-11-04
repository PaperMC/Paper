package net.minecraft.server;

import java.util.Calendar;
import java.util.UUID;

//CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
//CraftBukkit end

public class EntityZombie extends EntityMonster {

    protected static final IAttribute bp = (new AttributeRanged("zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).a("Spawn Reinforcements Chance");
    private static final UUID bq = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier br = new AttributeModifier(bq, "Baby speed boost", 0.5D, 1);
    private final PathfinderGoalBreakDoor bs = new PathfinderGoalBreakDoor(this);
    private int bt;
    private boolean bu = false;
    private float bv = -1.0F;
    private float bw;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit

    public EntityZombie(World world) {
        super(world);
        this.getNavigation().b(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 0, false));
        this.a(0.6F, 1.8F);
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.b).setValue(40.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.23000000417232513D);
        this.getAttributeInstance(GenericAttributes.e).setValue(3.0D);
        this.bc().b(bp).setValue(this.random.nextDouble() * 0.10000000149011612D);
    }

    protected void c() {
        super.c();
        this.getDataWatcher().a(12, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(13, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(14, Byte.valueOf((byte) 0));
    }

    public int aV() {
        int i = super.aV() + 2;

        if (i > 20) {
            i = 20;
        }

        return i;
    }

    protected boolean bk() {
        return true;
    }

    public boolean bX() {
        return this.bu;
    }

    public void a(boolean flag) {
        if (this.bu != flag) {
            this.bu = flag;
            if (flag) {
                this.goalSelector.a(1, this.bs);
            } else {
                this.goalSelector.a((PathfinderGoal) this.bs);
            }
        }
    }

    public boolean isBaby() {
        return this.getDataWatcher().getByte(12) == 1;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        if (this.isBaby()) {
            this.b = (int) ((float) this.b * 2.5F);
        }

        return super.getExpValue(entityhuman);
    }

    public void setBaby(boolean flag) {
        this.getDataWatcher().watch(12, Byte.valueOf((byte) (flag ? 1 : 0)));
        if (this.world != null && !this.world.isStatic) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

            attributeinstance.b(br);
            if (flag) {
                attributeinstance.a(br);
            }
        }

        this.k(flag);
    }

    public boolean isVillager() {
        return this.getDataWatcher().getByte(13) == 1;
    }

    public void setVillager(boolean flag) {
        this.getDataWatcher().watch(13, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public void e() {
        if (this.world.v() && !this.world.isStatic && !this.isBaby()) {
            float f = this.d(1.0F);

            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.i(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ))) {
                boolean flag = true;
                ItemStack itemstack = this.getEquipment(4);

                if (itemstack != null) {
                    if (itemstack.g()) {
                        itemstack.setData(itemstack.j() + this.random.nextInt(2));
                        if (itemstack.j() >= itemstack.l()) {
                            this.a(itemstack);
                            this.setEquipment(4, (ItemStack) null);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    // CraftBukkit start
                    EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.setOnFire(event.getDuration());
                    }
                    // CraftBukkit end
                }
            }
        }

        super.e();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!super.damageEntity(damagesource, f)) {
            return false;
        } else {
            EntityLiving entityliving = this.getGoalTarget();

            if (entityliving == null && this.bR() instanceof EntityLiving) {
                entityliving = (EntityLiving) this.bR();
            }

            if (entityliving == null && damagesource.getEntity() instanceof EntityLiving) {
                entityliving = (EntityLiving) damagesource.getEntity();
            }

            if (entityliving != null && this.world.difficulty == EnumDifficulty.HARD && (double) this.random.nextFloat() < this.getAttributeInstance(bp).getValue()) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);
                EntityZombie entityzombie = new EntityZombie(this.world);

                for (int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);

                    if (World.a((IBlockAccess) this.world, i1, j1 - 1, k1) && this.world.getLightLevel(i1, j1, k1) < 10) {
                        entityzombie.setPosition((double) i1, (double) j1, (double) k1);
                        if (this.world.b(entityzombie.boundingBox) && this.world.getCubes(entityzombie, entityzombie.boundingBox).isEmpty() && !this.world.containsLiquid(entityzombie.boundingBox)) {
                            this.world.addEntity(entityzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.REINFORCEMENTS); // CraftBukkit
                            entityzombie.setGoalTarget(entityliving);
                            entityzombie.a((GroupDataEntity) null);
                            this.getAttributeInstance(bp).a(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.getAttributeInstance(bp).a(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        }
    }

    public void h() {
        if (!this.world.isStatic && this.ca()) {
            int i = this.cc();

            // CraftBukkit start - Use wall time instead of ticks for villager conversion
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.lastTick = MinecraftServer.currentTick;
            i *= elapsedTicks;
            // CraftBukkit end

            this.bt -= i;
            if (this.bt <= 0) {
                this.cb();
            }
        }

        super.h();
    }

    public boolean m(Entity entity) {
        boolean flag = super.m(entity);

        if (flag) {
            int i = this.world.difficulty.a();

            if (this.be() == null && this.isBurning() && this.random.nextFloat() < (float) i * 0.3F) {
                // CraftBukkit start
                EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 2 * i);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    entity.setOnFire(event.getDuration());
                }
                // CraftBukkit end
            }
        }

        return flag;
    }

    protected String t() {
        return "mob.zombie.say";
    }

    protected String aT() {
        return "mob.zombie.hurt";
    }

    protected String aU() {
        return "mob.zombie.death";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.zombie.step", 0.15F, 1.0F);
    }

    protected Item getLoot() {
        return Items.ROTTEN_FLESH;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    // CraftBukkit start - Return rare dropped item instead of dropping it
    protected ItemStack getRareDrop(int i) {
        switch (this.random.nextInt(3)) {
        case 0:
            return new ItemStack(Items.IRON_INGOT, 1, 0);
        case 1:
            return new ItemStack(Items.CARROT, 1, 0);
        case 2:
            return new ItemStack(Items.POTATO, 1, 0);
        default:
            return null;
        }
    }
    // CraftBukkit end

    protected void bA() {
        super.bA();
        if (this.random.nextFloat() < (this.world.difficulty == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
            int i = this.random.nextInt(3);

            if (i == 0) {
                this.setEquipment(0, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setEquipment(0, new ItemStack(Items.IRON_SPADE));
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.isBaby()) {
            nbttagcompound.setBoolean("IsBaby", true);
        }

        if (this.isVillager()) {
            nbttagcompound.setBoolean("IsVillager", true);
        }

        nbttagcompound.setInt("ConversionTime", this.ca() ? this.bt : -1);
        nbttagcompound.setBoolean("CanBreakDoors", this.bX());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.getBoolean("IsBaby")) {
            this.setBaby(true);
        }

        if (nbttagcompound.getBoolean("IsVillager")) {
            this.setVillager(true);
        }

        if (nbttagcompound.hasKeyOfType("ConversionTime", 99) && nbttagcompound.getInt("ConversionTime") > -1) {
            this.a(nbttagcompound.getInt("ConversionTime"));
        }

        this.a(nbttagcompound.getBoolean("CanBreakDoors"));
    }

    public void a(EntityLiving entityliving) {
        super.a(entityliving);
        if ((this.world.difficulty == EnumDifficulty.NORMAL || this.world.difficulty == EnumDifficulty.HARD) && entityliving instanceof EntityVillager) {
            if (this.random.nextBoolean()) {
                return;
            }

            EntityZombie entityzombie = new EntityZombie(this.world);

            entityzombie.j(entityliving);
            this.world.kill(entityliving);
            entityzombie.a((GroupDataEntity) null);
            entityzombie.setVillager(true);
            if (entityliving.isBaby()) {
                entityzombie.setBaby(true);
            }

            this.world.addEntity(entityzombie);
            this.world.a((EntityHuman) null, 1016, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
        }
    }

    public GroupDataEntity a(GroupDataEntity groupdataentity) {
        Object object = super.a(groupdataentity);
        float f = this.world.b(this.locX, this.locY, this.locZ);

        this.h(this.random.nextFloat() < 0.55F * f);
        if (object == null) {
            object = new GroupDataZombie(this, this.world.random.nextFloat() < 0.05F, this.world.random.nextFloat() < 0.05F, (EmptyClassZombie) null);
        }

        if (object instanceof GroupDataZombie) {
            GroupDataZombie groupdatazombie = (GroupDataZombie) object;

            if (groupdatazombie.b) {
                this.setVillager(true);
            }

            if (groupdatazombie.a) {
                this.setBaby(true);
            }
        }

        this.a(this.random.nextFloat() < f * 0.1F);
        this.bA();
        this.bB();
        if (this.getEquipment(4) == null) {
            Calendar calendar = this.world.V();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25F) {
                this.setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.PUMPKIN));
                this.dropChances[4] = 0.0F;
            }
        }

        this.getAttributeInstance(GenericAttributes.c).a(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.random.nextDouble() * 1.5D * (double) this.world.b(this.locX, this.locY, this.locZ);

        if (d0 > 1.0D) {
            this.getAttributeInstance(GenericAttributes.b).a(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.random.nextFloat() < f * 0.05F) {
            this.getAttributeInstance(bp).a(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, 0));
            this.getAttributeInstance(GenericAttributes.a).a(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, 2));
            this.a(true);
        }

        return (GroupDataEntity) object;
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.bD();

        if (itemstack != null && itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getData() == 0 && this.isVillager() && this.hasEffect(MobEffectList.WEAKNESS)) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                this.a(this.random.nextInt(2401) + 3600);
            }

            return true;
        } else {
            return false;
        }
    }

    protected void a(int i) {
        this.bt = i;
        this.getDataWatcher().watch(14, Byte.valueOf((byte) 1));
        this.m(MobEffectList.WEAKNESS.id);
        this.addEffect(new MobEffect(MobEffectList.INCREASE_DAMAGE.id, i, Math.min(this.world.difficulty.a() - 1, 0)));
        this.world.broadcastEntityEffect(this, (byte) 16);
    }

    protected boolean isTypeNotPersistent() {
        return !this.ca();
    }

    public boolean ca() {
        return this.getDataWatcher().getByte(14) == 1;
    }

    protected void cb() {
        EntityVillager entityvillager = new EntityVillager(this.world);

        entityvillager.j(this);
        entityvillager.a((GroupDataEntity) null);
        entityvillager.cb();
        if (this.isBaby()) {
            entityvillager.setAge(-24000);
        }

        this.world.kill(this);
        this.world.addEntity(entityvillager);
        entityvillager.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 200, 0));
        this.world.a((EntityHuman) null, 1017, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
    }

    protected int cc() {
        int i = 1;

        if (this.random.nextFloat() < 0.01F) {
            int j = 0;

            for (int k = (int) this.locX - 4; k < (int) this.locX + 4 && j < 14; ++k) {
                for (int l = (int) this.locY - 4; l < (int) this.locY + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.locZ - 4; i1 < (int) this.locZ + 4 && j < 14; ++i1) {
                        Block block = this.world.getType(k, l, i1);

                        if (block == Blocks.IRON_FENCE || block == Blocks.BED) {
                            if (this.random.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    public void k(boolean flag) {
        this.a(flag ? 0.5F : 1.0F);
    }

    protected final void a(float f, float f1) {
        boolean flag = this.bv > 0.0F && this.bw > 0.0F;

        this.bv = f;
        this.bw = f1;
        if (!flag) {
            this.a(1.0F);
        }
    }

    protected final void a(float f) {
        super.a(this.bv * f, this.bw * f);
    }
}
