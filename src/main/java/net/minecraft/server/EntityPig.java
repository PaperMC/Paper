package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
// CraftBukkit end

public class EntityPig extends EntityAnimal {

    private final PathfinderGoalPassengerCarrotStick bp;

    public EntityPig(World world) {
        super(world);
        this.a(0.9F, 0.9F);
        this.getNavigation().a(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(2, this.bp = new PathfinderGoalPassengerCarrotStick(this, 0.3F));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT_STICK, false));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
        this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.1D));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    public boolean bk() {
        return true;
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.a).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.25D);
    }

    protected void bn() {
        super.bn();
    }

    public boolean bC() {
        ItemStack itemstack = ((EntityHuman) this.passenger).be();

        return itemstack != null && itemstack.getItem() == Items.CARROT_STICK;
    }

    protected void c() {
        super.c();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.hasSaddle());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSaddle(nbttagcompound.getBoolean("Saddle"));
    }

    protected String t() {
        return "mob.pig.say";
    }

    protected String aT() {
        return "mob.pig.say";
    }

    protected String aU() {
        return "mob.pig.death";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.pig.step", 0.15F, 1.0F);
    }

    public boolean a(EntityHuman entityhuman) {
        if (super.a(entityhuman)) {
            return true;
        } else if (this.hasSaddle() && !this.world.isStatic && (this.passenger == null || this.passenger == entityhuman)) {
            entityhuman.mount(this);
            return true;
        } else {
            return false;
        }
    }

    protected Item getLoot() {
        return this.isBurning() ? Items.GRILLED_PORK : Items.PORK;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);

        if (j > 0) {
            if (this.isBurning()) {
                loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.GRILLED_PORK), j));
            } else {
                loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.PORK), j));
            }
        }

        if (this.hasSaddle()) {
            loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.SADDLE), 1));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddle(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
    }

    public void a(EntityLightning entitylightning) {
        if (!this.world.isStatic) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);

            // CraftBukkit start
            if (CraftEventFactory.callPigZapEvent(this, entitylightning, entitypigzombie).isCancelled()) {
                return;
            }
            // CraftBukkit end

            entitypigzombie.setEquipment(0, new ItemStack(Items.GOLD_SWORD));
            entitypigzombie.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            // CraftBukkit - added a reason for spawning this creature
            this.world.addEntity(entitypigzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.die();
        }
    }

    protected void b(float f) {
        super.b(f);
        if (f > 5.0F && this.passenger instanceof EntityHuman) {
            ((EntityHuman) this.passenger).a((Statistic) AchievementList.u);
        }
    }

    public EntityPig b(EntityAgeable entityageable) {
        return new EntityPig(this.world);
    }

    public boolean c(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == Items.CARROT;
    }

    public PathfinderGoalPassengerCarrotStick bY() {
        return this.bp;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
