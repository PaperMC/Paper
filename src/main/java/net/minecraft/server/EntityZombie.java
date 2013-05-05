package net.minecraft.server;

import java.util.Calendar;

//CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
//CraftBukkit end

public class EntityZombie extends EntityMonster {

    private int d = 0;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit

    public EntityZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.bI = 0.23F;
        this.getNavigation().b(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bI, false));
        this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, this.bI, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bI));
        this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bI, false));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bI));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
    }

    protected int ay() {
        return 40;
    }

    public float bE() {
        return super.bE() * (this.isBaby() ? 1.5F : 1.0F);
    }

    protected void a() {
        super.a();
        this.getDataWatcher().a(12, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(13, Byte.valueOf((byte) 0));
        this.getDataWatcher().a(14, Byte.valueOf((byte) 0));
    }

    public int getMaxHealth() {
        return 20;
    }

    public int aZ() {
        int i = super.aZ() + 2;

        if (i > 20) {
            i = 20;
        }

        return i;
    }

    protected boolean bh() {
        return true;
    }

    public boolean isBaby() {
        return this.getDataWatcher().getByte(12) == 1;
    }

    public void setBaby(boolean flag) {
        this.getDataWatcher().watch(12, Byte.valueOf((byte) (flag ? 1 : 0))); // CraftBukkit - added flag
    }

    public boolean isVillager() {
        return this.getDataWatcher().getByte(13) == 1;
    }

    public void setVillager(boolean flag) {
        this.getDataWatcher().watch(13, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public void c() {
        if (this.world.v() && !this.world.isStatic && !this.isBaby()) {
            float f = this.c(1.0F);

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

    public void l_() {
        if (!this.world.isStatic && this.o()) {
            int i = this.q();

            // CraftBukkit start - Use wall time instead of ticks for villager conversion
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.lastTick = MinecraftServer.currentTick;
            i *= elapsedTicks;
            // CraftBukkit end

            this.d -= i;
            if (this.d <= 0) {
                this.p();
            }
        }

        super.l_();
    }

    public boolean m(Entity entity) {
        boolean flag = super.m(entity);

        if (flag && this.bG() == null && this.isBurning() && this.random.nextFloat() < (float) this.world.difficulty * 0.3F) {
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

    public int c(Entity entity) {
        ItemStack itemstack = this.bG();
        // CraftBukkit - getMaxHealth() -> ((CraftLivingEntity) this.bukkitEntity).getMaxHealth()
        float f = (float) (((CraftLivingEntity) this.bukkitEntity).getMaxHealth() - this.getHealth()) / (float) ((CraftLivingEntity) this.bukkitEntity).getMaxHealth();
        int i = 3 + MathHelper.d(f * 4.0F);

        if (itemstack != null) {
            i += itemstack.a((Entity) this);
        }

        return i;
    }

    protected String bb() {
        return "mob.zombie.say";
    }

    protected String bc() {
        return "mob.zombie.hurt";
    }

    protected String bd() {
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

    protected void bH() {
        super.bH();
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

        nbttagcompound.setInt("ConversionTime", this.o() ? this.d : -1);
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

            entityzombie.k(entityliving);
            this.world.kill(entityliving);
            entityzombie.bJ();
            entityzombie.setVillager(true);
            if (entityliving.isBaby()) {
                entityzombie.setBaby(true);
            }

            this.world.addEntity(entityzombie);
            this.world.a((EntityHuman) null, 1016, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
        }
    }

    public void bJ() {
        this.h(this.random.nextFloat() < au[this.world.difficulty]);
        if (this.world.random.nextFloat() < 0.05F) {
            this.setVillager(true);
        }

        this.bH();
        this.bI();
        if (this.getEquipment(4) == null) {
            Calendar calendar = this.world.V();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.random.nextFloat() < 0.25F) {
                this.setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Block.JACK_O_LANTERN : Block.PUMPKIN));
                this.dropChances[4] = 0.0F;
            }
        }
    }

    public boolean a_(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.cd();

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
        this.d = i;
        this.getDataWatcher().watch(14, Byte.valueOf((byte) 1));
        this.o(MobEffectList.WEAKNESS.id);
        this.addEffect(new MobEffect(MobEffectList.INCREASE_DAMAGE.id, i, Math.min(this.world.difficulty - 1, 0)));
        this.world.broadcastEntityEffect(this, (byte) 16);
    }

    public boolean o() {
        return this.getDataWatcher().getByte(14) == 1;
    }

    protected void p() {
        EntityVillager entityvillager = new EntityVillager(this.world);

        entityvillager.k(this);
        entityvillager.bJ();
        entityvillager.q();
        if (this.isBaby()) {
            entityvillager.setAge(-24000);
        }

        this.world.kill(this);
        this.world.addEntity(entityvillager);
        entityvillager.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 200, 0));
        this.world.a((EntityHuman) null, 1017, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
    }

    protected int q() {
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
