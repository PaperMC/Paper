package net.minecraft.server;

public abstract class EntityAgeable extends EntityCreature {

    private float bp = -1.0F;
    private float bq;
    public boolean ageLocked = false; // CraftBukkit

    public EntityAgeable(World world) {
        super(world);
    }

    public abstract EntityAgeable createChild(EntityAgeable entityageable);

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.getItem() == Items.MONSTER_EGG) {
            if (!this.world.isStatic) {
                Class oclass = EntityTypes.a(itemstack.getData());

                if (oclass != null && oclass.isAssignableFrom(this.getClass())) {
                    EntityAgeable entityageable = this.createChild(this);

                    if (entityageable != null) {
                        entityageable.setAge(-24000);
                        entityageable.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
                        this.world.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                        if (itemstack.hasName()) {
                            entityageable.setCustomName(itemstack.getName());
                        }

                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                            if (itemstack.count == 0) {  // CraftBukkit - allow less than 0 stacks as "infinite"
                                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected void c() {
        super.c();
        this.datawatcher.a(12, new Integer(0));
    }

    public int getAge() {
        return this.datawatcher.getInt(12);
    }

    public void a(int i) {
        int j = this.getAge();

        j += i * 20;
        if (j > 0) {
            j = 0;
        }

        this.setAge(j);
    }

    public void setAge(int i) {
        this.datawatcher.watch(12, Integer.valueOf(i));
        this.a(this.isBaby());
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Age", this.getAge());
        nbttagcompound.setBoolean("AgeLocked", this.ageLocked); // CraftBukkit
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAge(nbttagcompound.getInt("Age"));
        this.ageLocked = nbttagcompound.getBoolean("AgeLocked"); // CraftBukkit
    }

    public void e() {
        super.e();
        if (this.world.isStatic || this.ageLocked) { // CraftBukkit
            this.a(this.isBaby());
        } else {
            int i = this.getAge();

            if (i < 0) {
                ++i;
                this.setAge(i);
            } else if (i > 0) {
                --i;
                this.setAge(i);
            }
        }
    }

    public boolean isBaby() {
        return this.getAge() < 0;
    }

    public void a(boolean flag) {
        this.a(flag ? 0.5F : 1.0F);
    }

    protected final void a(float f, float f1) {
        boolean flag = this.bp > 0.0F;

        this.bp = f;
        this.bq = f1;
        if (!flag) {
            this.a(1.0F);
        }
    }

    protected final void a(float f) {
        super.a(this.bp * f, this.bq * f);
    }
}
