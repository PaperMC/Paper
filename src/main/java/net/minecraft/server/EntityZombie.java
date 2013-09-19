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
    private int bs;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit

    public EntityZombie(World world) {
        super(world);
        this.getNavigation().b(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
        this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 0, false));
    }

    protected void az() {
        super.az();
        this.getAttributeInstance(GenericAttributes.b).setValue(40.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.23000000417232513D);
        this.getAttributeInstance(GenericAttributes.e).setValue(3.0D);
        this.aX().b(bp).setValue(this.random.nextDouble() * 0.10000000149011612D);
    }

    protected void a() {
        super.a();
        this.getDataWatcher().a(12, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(13, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(14, Byte.valueOf((byte) 0));
    }

    public int aQ() {
        int i = super.aQ() + 2;

        if (i > 20) {
            i = 20;
        }

        return i;
    }

    protected boolean bf() {
        return true;
    }

    public boolean isBaby() {
        return this.getDataWatcher().getByte(12) == 1;
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
    }

    public boolean isVillager() {
        return this.getDataWatcher().getByte(13) == 1;
    }

    public void setVillager(boolean flag) {
        this.getDataWatcher().watch(13, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public void c() {
        if (this.world.v() && !this.world.isStatic && !this.isBaby()) {
            float f = this.d(1.0F);

            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.l(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ))) {
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

        super.c();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!super.damageEntity(damagesource, f)) {
            return false;
        } else {
            EntityLiving entityliving = this.getGoalTarget();

            if (entityliving == null && this.bN() instanceof EntityLiving) {
                entityliving = (EntityLiving) this.bN();
            }

            if (entityliving == null && damagesource.getEntity() instanceof EntityLiving) {
                entityliving = (EntityLiving) damagesource.getEntity();
            }

            if (entityliving != null && this.world.difficulty >= 3 && (double) this.random.nextFloat() < this.getAttributeInstance(bp).getValue()) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);
                EntityZombie entityzombie = new EntityZombie(this.world);

                for (int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
                    int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);

                    if (this.world.w(i1, j1 - 1, k1) && this.world.getLightLevel(i1, j1, k1) < 10) {
                        entityzombie.setPosition((double) i1, (double) j1, (double) k1);
                        if (this.world.b(entityzombie.boundingBox) && this.world.getCubes(entityzombie, entityzombie.boundingBox).isEmpty() && !this.world.containsLiquid(entityzombie.boundingBox)) {
                            this.world.addEntity(entityzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
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

    public void l_() {
        if (!this.world.isStatic && this.bV()) {
            int i = this.bX();

            // CraftBukkit start - Use wall time instead of ticks for villager conversion
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.lastTick = MinecraftServer.currentTick;
            i *= elapsedTicks;
            // CraftBukkit end

            this.bs -= i;
            if (this.bs <= 0) {
                this.bW();
            }
        }

        super.l_();
    }

    public boolean m(Entity entity) {
        boolean flag = super.m(entity);

        if (flag && this.aZ() == null && this.isBurning() && this.random.nextFloat() < (float) this.world.difficulty * 0.3F) {
            // CraftBukkit start
            EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 2 * this.world.difficulty);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                entity.setOnFire(event.getDuration());
            }
            // CraftBukkit end
        }

        return flag;
    }

    protected String r() {
        return "mob.zombie.say";
    }

    protected String aO() {
        return "mob.zombie.hurt";
    }

    protected String aP() {
        return "mob.zombie.death";
    }

    protected void a(int i, int j, int k, int l) {
        this.makeSound("mob.zombie.step", 0.15F, 1.0F);
    }

    protected int getLootId() {
        return Item.ROTTEN_FLESH.id;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    // CraftBukkit start - Return rare dropped item instead of dropping it
    protected ItemStack l(int i) {
        switch (this.random.nextInt(3)) {
        case 0:
            return new ItemStack(Item.IRON_INGOT.id, 1, 0);
        case 1:
            return new ItemStack(Item.CARROT.id, 1, 0);
        case 2:
            return new ItemStack(Item.POTATO.id, 1, 0);
        default:
            return null;
        }
    }
    // CraftBukkit end

    protected void bw() {
        super.bw();
        if (this.random.nextFloat() < (this.world.difficulty == 3 ? 0.05F : 0.01F)) {
            int i = this.random.nextInt(3);

            if (i == 0) {
                this.setEquipment(0, new ItemStack(Item.IRON_SWORD));
            } else {
                this.setEquipment(0, new ItemStack(Item.IRON_SPADE));
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

        nbttagcompound.setInt("ConversionTime", this.bV() ? this.bs : -1);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.getBoolean("IsBaby")) {
            this.setBaby(true);
        }

        if (nbttagcompound.getBoolean("IsVillager")) {
            this.setVillager(true);
        }

        if (nbttagcompound.hasKey("ConversionTime") && nbttagcompound.getInt("ConversionTime") > -1) {
            this.a(nbttagcompound.getInt("ConversionTime"));
        }
    }

    public void a(EntityLiving entityliving) {
        super.a(entityliving);
        if (this.world.difficulty >= 2 && entityliving instanceof EntityVillager) {
            if (this.world.difficulty == 2 && this.random.nextBoolean()) {
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
            object = new GroupDataZombie(this, this.world.random.nextFloat() < 0.05F, this.world.random.nextFloat() < 0.05F, (EmptyClass4) null);
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

        this.bw();
        this.bx();
        if (this.getEquipment(4) == null) {
            Calendar calendar = this.world.W();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25F) {
                this.setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Block.JACK_O_LANTERN : Block.PUMPKIN));
                this.dropChances[4] = 0.0F;
            }
        }

        this.getAttributeInstance(GenericAttributes.c).a(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806D, 0));
        this.getAttributeInstance(GenericAttributes.b).a(new AttributeModifier("Random zombie-spawn bonus", this.random.nextDouble() * 1.5D, 2));
        if (this.random.nextFloat() < f * 0.05F) {
            this.getAttributeInstance(bp).a(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, 0));
            this.getAttributeInstance(GenericAttributes.a).a(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, 2));
        }

        return (GroupDataEntity) object;
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.by();

        if (itemstack != null && itemstack.getItem() == Item.GOLDEN_APPLE && itemstack.getData() == 0 && this.isVillager() && this.hasEffect(MobEffectList.WEAKNESS)) {
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
        this.bs = i;
        this.getDataWatcher().watch(14, Byte.valueOf((byte) 1));
        this.k(MobEffectList.WEAKNESS.id);
        this.addEffect(new MobEffect(MobEffectList.INCREASE_DAMAGE.id, i, Math.min(this.world.difficulty - 1, 0)));
        this.world.broadcastEntityEffect(this, (byte) 16);
    }

    protected boolean isTypeNotPersistent() {
        return !this.bV();
    }

    public boolean bV() {
        return this.getDataWatcher().getByte(14) == 1;
    }

    protected void bW() {
        EntityVillager entityvillager = new EntityVillager(this.world);

        entityvillager.j(this);
        entityvillager.a((GroupDataEntity) null);
        entityvillager.bX();
        if (this.isBaby()) {
            entityvillager.setAge(-24000);
        }

        this.world.kill(this);
        this.world.addEntity(entityvillager);
        entityvillager.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 200, 0));
        this.world.a((EntityHuman) null, 1017, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
    }

    protected int bX() {
        int i = 1;

        if (this.random.nextFloat() < 0.01F) {
            int j = 0;

            for (int k = (int) this.locX - 4; k < (int) this.locX + 4 && j < 14; ++k) {
                for (int l = (int) this.locY - 4; l < (int) this.locY + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.locZ - 4; i1 < (int) this.locZ + 4 && j < 14; ++i1) {
                        int j1 = this.world.getTypeId(k, l, i1);

                        if (j1 == Block.IRON_FENCE.id || j1 == Block.BED.id) {
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
}
