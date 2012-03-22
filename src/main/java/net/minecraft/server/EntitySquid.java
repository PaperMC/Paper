package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntitySquid extends EntityWaterAnimal {

    public float a = 0.0F;
    public float b = 0.0F;
    public float c = 0.0F;
    public float g = 0.0F;
    public float h = 0.0F;
    public float i = 0.0F;
    public float j = 0.0F;
    public float k = 0.0F;
    private float l = 0.0F;
    private float m = 0.0F;
    private float n = 0.0F;
    private float o = 0.0F;
    private float p = 0.0F;
    private float q = 0.0F;

    public EntitySquid(World world) {
        super(world);
        this.texture = "/mob/squid.png";
        this.b(0.95F, 0.95F);
        this.m = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    public int getMaxHealth() {
        return 10;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected String i() {
        return null;
    }

    protected String j() {
        return null;
    }

    protected String k() {
        return null;
    }

    protected float p() {
        return 0.4F;
    }

    protected int getLootId() {
        return 0;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        int count = this.random.nextInt(3 + i) + 1;
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.INK_SACK, count));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean b(EntityHuman entityhuman) {
        return super.b(entityhuman);
    }

    public boolean aU() {
        return this.world.a(this.boundingBox.grow(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, this);
    }

    public void e() {
        super.e();
        this.b = this.a;
        this.g = this.c;
        this.i = this.h;
        this.k = this.j;
        this.h += this.m;
        if (this.h > 6.2831855F) {
            this.h -= 6.2831855F;
            if (this.random.nextInt(10) == 0) {
                this.m = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.aU()) {
            float f;

            if (this.h < 3.1415927F) {
                f = this.h / 3.1415927F;
                this.j = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double) f > 0.75D) {
                    this.l = 1.0F;
                    this.n = 1.0F;
                } else {
                    this.n *= 0.8F;
                }
            } else {
                this.j = 0.0F;
                this.l *= 0.9F;
                this.n *= 0.99F;
            }

            if (!this.world.isStatic) {
                this.motX = (double) (this.o * this.l);
                this.motY = (double) (this.p * this.l);
                this.motZ = (double) (this.q * this.l);
            }

            f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            this.V += (-((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F - this.V) * 0.1F;
            this.yaw = this.V;
            this.c += 3.1415927F * this.n * 1.5F;
            this.a += (-((float) Math.atan2((double) f, this.motY)) * 180.0F / 3.1415927F - this.a) * 0.1F;
        } else {
            this.j = MathHelper.abs(MathHelper.sin(this.h)) * 3.1415927F * 0.25F;
            if (!this.world.isStatic) {
                this.motX = 0.0D;
                this.motY -= 0.08D;
                this.motY *= 0.9800000190734863D;
                this.motZ = 0.0D;
            }

            this.a = (float) ((double) this.a + (double) (-90.0F - this.a) * 0.02D);
        }
    }

    public void a(float f, float f1) {
        this.move(this.motX, this.motY, this.motZ);
    }

    protected void d_() {
        ++this.aV;
        if (this.aV > 100) {
            this.o = this.p = this.q = 0.0F;
        } else if (this.random.nextInt(50) == 0 || !this.bV || this.o == 0.0F && this.p == 0.0F && this.q == 0.0F) {
            float f = this.random.nextFloat() * 3.1415927F * 2.0F;

            this.o = MathHelper.cos(f) * 0.2F;
            this.p = -0.1F + this.random.nextFloat() * 0.2F;
            this.q = MathHelper.sin(f) * 0.2F;
        }

        this.aG();
    }

    public boolean canSpawn() {
        return this.locY > 45.0D && this.locY < 63.0D && super.canSpawn();
    }
}
