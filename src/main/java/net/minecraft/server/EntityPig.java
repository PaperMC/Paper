package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityPig extends EntityAnimal {

    private final PathfinderGoalPassengerCarrotStick d;

    public EntityPig(World world) {
        super(world);
        this.texture = "/mob/pig.png";
        this.a(0.9F, 0.9F);
        this.getNavigation().a(true);
        float f = 0.25F;

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
        this.goalSelector.a(2, this.d = new PathfinderGoalPassengerCarrotStick(this, 0.34F));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, f));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 0.3F, Item.CARROT_STICK.id, false));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 0.3F, Item.CARROT.id, false));
        this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 0.28F));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, f));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    public boolean bb() {
        return true;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected void bi() {
        super.bi();
    }

    public boolean bF() {
        ItemStack itemstack = ((EntityHuman) this.passenger).bA();

        return itemstack != null && itemstack.id == Item.CARROT_STICK.id;
    }

    protected void a() {
        super.a();
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

    protected String aW() {
        return "mob.pig.say";
    }

    protected String aX() {
        return "mob.pig.say";
    }

    protected String aY() {
        return "mob.pig.death";
    }

    protected void a(int i, int j, int k, int l) {
        this.world.makeSound(this, "mob.pig.step", 0.15F, 1.0F);
    }

    public boolean c(EntityHuman entityhuman) {
        if (super.c(entityhuman)) {
            return true;
        } else if (this.hasSaddle() && !this.world.isStatic && (this.passenger == null || this.passenger == entityhuman)) {
            entityhuman.mount(this);
            return true;
        } else {
            return false;
        }
    }

    protected int getLootId() {
        return this.isBurning() ? Item.GRILLED_PORK.id : Item.PORK.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);

        if (j > 0) {
            if (this.isBurning()) {
                loot.add(new org.bukkit.inventory.ItemStack(Item.GRILLED_PORK.id, j));
            } else {
                loot.add(new org.bukkit.inventory.ItemStack(Item.PORK.id, j));
            }
        }

        if (this.hasSaddle()) {
            loot.add(new org.bukkit.inventory.ItemStack(Item.SADDLE.id, 1));
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

            entitypigzombie.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            // CraftBukkit - added a reason for spawning this creature
            this.world.addEntity(entitypigzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.die();
        }
    }

    protected void a(float f) {
        super.a(f);
        if (f > 5.0F && this.passenger instanceof EntityHuman) {
            ((EntityHuman) this.passenger).a((Statistic) AchievementList.u);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityPig(this.world);
    }

    public boolean c(ItemStack itemstack) {
        return itemstack != null && itemstack.id == Item.CARROT.id;
    }

    public PathfinderGoalPassengerCarrotStick n() {
        return this.d;
    }
}
