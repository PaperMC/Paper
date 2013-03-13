package net.minecraft.server;

public abstract class EntityAgeable extends EntityCreature {

    private float d = -1.0F;
    private float e;
    public boolean ageLocked = false; // CraftBukkit

    public EntityAgeable(World world) {
        super(world);
    }

    public abstract EntityAgeable createChild(EntityAgeable entityageable);

    public boolean a_(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.MONSTER_EGG.id && !this.world.isStatic) {
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
                        if (itemstack.count == 0) { // CraftBukkit - allow less than 0 stacks as "infinite"
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }
                    }
                }
            }
        }

        return super.a_(entityhuman);
    }

    protected void a() {
        super.a();
        this.datawatcher.a(12, new Integer(0));
    }

    public int getAge() {
        return this.datawatcher.getInt(12);
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

    public void c() {
        super.c();
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
        this.j(flag ? 0.5F : 1.0F);
    }

    protected final void a(float f, float f1) {
        boolean flag = this.d > 0.0F;

        this.d = f;
        this.e = f1;
        if (!flag) {
            this.j(1.0F);
        }
    }

    private void j(float f) {
        super.a(this.d * f, this.e * f);
    }
}
