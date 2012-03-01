package net.minecraft.server;

public abstract class EntityAgeable extends EntityCreature {
    public boolean ageLocked = false; // CraftBukkit

    public EntityAgeable(World world) {
        super(world);
    }

    protected void b() {
        super.b();
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

    public void e() {
        super.e();
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
