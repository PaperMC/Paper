package net.minecraft.server;

public class EntityOcelot extends EntityTameableAnimal {

    private PathfinderGoalTempt b;

    public EntityOcelot(World world) {
        super(world);
        this.texture = "/mob/ozelot.png";
        this.b(0.6F, 0.8F);
        this.al().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.a);
        this.goalSelector.a(3, this.b = new PathfinderGoalTempt(this, 0.18F, Item.RAW_FISH.id, true));
        this.goalSelector.a(4, new PathfinderGoalAvoidPlayer(this, EntityHuman.class, 16.0F, 0.23F, 0.4F));
        this.goalSelector.a(5, new PathfinderGoalJumpOnBlock(this, 0.4F));
        this.goalSelector.a(6, new PathfinderGoalFollowOwner(this, 0.3F, 10.0F, 5.0F));
        this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
        this.goalSelector.a(8, new PathfinderGoalOzelotAttack(this));
        this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.23F));
        this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.23F));
        this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
        this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, 14.0F, 750, false));
    }

    protected void b() {
        super.b();
        this.datawatcher.a(18, Byte.valueOf((byte) 0));
    }

    public void g() {
        if (!this.getControllerMove().a()) {
            this.setSneak(false);
            this.setSprinting(false);
        } else {
            float f = this.getControllerMove().b();

            if (f == 0.18F) {
                this.setSneak(true);
                this.setSprinting(false);
            } else if (f == 0.4F) {
                this.setSneak(false);
                this.setSprinting(true);
            } else {
                this.setSneak(false);
                this.setSprinting(false);
            }
        }
    }

    protected boolean n() {
        return !this.isTamed();
    }

    public boolean c_() {
        return true;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected void a(float f) {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("CatType", this.getCatType());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCatType(nbttagcompound.getInt("CatType"));
    }

    protected String i() {
        return this.isTamed() ? (this.r_() ? "mob.cat.purr" : (this.random.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }

    protected String j() {
        return "mob.cat.hitt";
    }

    protected String k() {
        return "mob.cat.hitt";
    }

    protected float p() {
        return 0.4F;
    }

    protected int getLootId() {
        return Item.LEATHER.id;
    }

    public boolean a(Entity entity) {
        return entity.damageEntity(DamageSource.mobAttack(this), 3);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.a.a(false);
        return super.damageEntity(damagesource, i);
    }

    protected void dropDeathLoot(boolean flag, int i) {
        super.dropDeathLoot(flag, i); // CraftBukkit - Calls EntityDeathEvent
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (!this.isTamed()) {
            if (this.b.f() && itemstack != null && itemstack.id == Item.RAW_FISH.id && entityhuman.j(this) < 9.0D) {
                --itemstack.count;
                if (itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                if (!this.world.isStatic) {
                    // CraftBukkit - added event call and isCancelled check.
                    if (this.random.nextInt(3) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                        this.setTamed(true);
                        this.setCatType(1 + this.world.random.nextInt(3));
                        this.setOwnerName(entityhuman.name);
                        this.a(true);
                        this.a.a(true);
                        this.world.broadcastEntityEffect(this, (byte) 7);
                    } else {
                        this.a(false);
                        this.world.broadcastEntityEffect(this, (byte) 6);
                    }
                }
            }

            return true;
        } else {
            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.a(itemstack)) {
                this.a.a(!this.isSitting());
            }

            return super.b(entityhuman);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        EntityOcelot entityocelot = new EntityOcelot(this.world);

        if (this.isTamed()) {
            entityocelot.setOwnerName(this.getOwnerName());
            entityocelot.setTamed(true);
            entityocelot.setCatType(this.getCatType());
        }

        return entityocelot;
    }

    public boolean a(ItemStack itemstack) {
        return itemstack != null && itemstack.id == Item.RAW_FISH.id;
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityOcelot)) {
            return false;
        } else {
            EntityOcelot entityocelot = (EntityOcelot) entityanimal;

            return !entityocelot.isTamed() ? false : this.r_() && entityocelot.r_();
        }
    }

    public int getCatType() {
        return this.datawatcher.getByte(18);
    }

    public void setCatType(int i) {
        this.datawatcher.watch(18, Byte.valueOf((byte) i));
    }

    public boolean canSpawn() {
        if (this.world.random.nextInt(3) == 0) {
            return false;
        } else {
            if (this.world.containsEntity(this.boundingBox) && this.world.getCubes(this, this.boundingBox).size() == 0 && !this.world.containsLiquid(this.boundingBox)) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.boundingBox.b);
                int k = MathHelper.floor(this.locZ);

                if (j < 63) {
                    return false;
                }

                int l = this.world.getTypeId(i, j - 1, k);

                if (l == Block.GRASS.id || l == Block.LEAVES.id) {
                    return true;
                }
            }

            return false;
        }
    }

    public String getLocalizedName() {
        return this.isTamed() ? "entity.Cat.name" : super.getLocalizedName();
    }
}
