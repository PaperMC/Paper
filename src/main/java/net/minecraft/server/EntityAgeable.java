package net.minecraft.server;

public abstract class EntityAgeable extends EntityCreature {
    public boolean ageLocked = false; // CraftBukkit

    public EntityAgeable(World world) {
        super(world);
    }

    public abstract EntityAgeable createChild(EntityAgeable entityageable);

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.MONSTER_EGG.id && !this.world.isStatic) {
            Class oclass = EntityTypes.a(itemstack.getData());

            if (oclass != null && oclass.isAssignableFrom(this.getClass())) {
                EntityAgeable entityageable = this.createChild(this);

                if (entityageable != null) {
                    entityageable.setAge(-24000);
                    entityageable.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
                    this.world.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit

                    // CraftBukkit start
                    if (!entityhuman.abilities.canInstantlyBuild) {
                        itemstack.count--;
                    }
                    // CraftBukkit end
                }
            }
        }

        return super.a(entityhuman);
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
        int i = this.getAge();

        if (ageLocked) return; // CraftBukkit
        if (i < 0) {
            ++i;
            this.setAge(i);
        } else if (i > 0) {
            --i;
            this.setAge(i);
        }
    }

    public boolean isBaby() {
        return this.getAge() < 0;
    }
}
