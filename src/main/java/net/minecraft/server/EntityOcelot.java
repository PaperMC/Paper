package net.minecraft.server;

public class EntityOcelot extends EntityTameableAnimal {

    private PathfinderGoalTempt e;

    public EntityOcelot(World world) {
        super(world);
        this.texture = "/mob/ozelot.png";
        this.a(0.6F, 0.8F);
        this.getNavigation().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.d);
        this.goalSelector.a(3, this.e = new PathfinderGoalTempt(this, 0.18F, Item.RAW_FISH.id, true));
        this.goalSelector.a(4, new PathfinderGoalAvoidPlayer(this, EntityHuman.class, 16.0F, 0.23F, 0.4F));
        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 0.3F, 10.0F, 5.0F));
        this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.4F));
        this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
        this.goalSelector.a(8, new PathfinderGoalOcelotAttack(this));
        this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.23F));
        this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.23F));
        this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
        this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, 14.0F, 750, false));
    }

    protected void a() {
        super.a();
        this.datawatcher.a(18, Byte.valueOf((byte) 0));
    }

    public void bj() {
        if (this.getControllerMove().a()) {
            float f = this.getControllerMove().b();

            if (f == 0.18F) {
                this.setSneaking(true);
                this.setSprinting(false);
            } else if (f == 0.4F) {
                this.setSneaking(false);
                this.setSprinting(true);
            } else {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        } else {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }

    protected boolean bg() {
        return !this.isTamed();
    }

    public boolean bb() {
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

    protected String aW() {
        return this.isTamed() ? (this.r() ? "mob.cat.purr" : (this.random.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }

    protected String aX() {
        return "mob.cat.hitt";
    }

    protected String aY() {
        return "mob.cat.hitt";
    }

    protected float aV() {
        return 0.4F;
    }

    protected int getLootId() {
        return Item.LEATHER.id;
    }

    public boolean l(Entity entity) {
        return entity.damageEntity(DamageSource.mobAttack(this), 3);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.d.a(false);
        return super.damageEntity(damagesource, i);
    }

    protected void dropDeathLoot(boolean flag, int i) {
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this); // CraftBukkit - Call EntityDeathEvent
    }

    public boolean c(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.c(itemstack)) {
                this.d.a(!this.isSitting());
            }
        } else if (this.e.f() && itemstack != null && itemstack.id == Item.RAW_FISH.id && entityhuman.e(this) < 9.0D) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                // CraftBukkit - added event call and isCancelled check
                if (this.random.nextInt(3) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.setTamed(true);
                    this.setCatType(1 + this.world.random.nextInt(3));
                    this.setOwnerName(entityhuman.name);
                    this.f(true);
                    this.d.a(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.f(false);
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        return super.c(entityhuman);
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

    public boolean c(ItemStack itemstack) {
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

            return !entityocelot.isTamed() ? false : this.r() && entityocelot.r();
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
            if (this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox)) {
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

    public void bD() {
        if (this.world.random.nextInt(7) == 0) {
            for (int i = 0; i < 2; ++i) {
                EntityOcelot entityocelot = new EntityOcelot(this.world);

                entityocelot.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
                entityocelot.setAge(-24000);
                this.world.addEntity(entityocelot);
            }
        }
    }
}
