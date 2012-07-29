package net.minecraft.server;

public class EntitySquid extends EntityWaterAnimal {

    public float d = 0.0F;
    public float e = 0.0F;
    public float f = 0.0F;
    public float g = 0.0F;
    public float h = 0.0F;
    public float i = 0.0F;
    public float j = 0.0F;
    public float by = 0.0F;
    private float bz = 0.0F;
    private float bA = 0.0F;
    private float bB = 0.0F;
    private float bC = 0.0F;
    private float bD = 0.0F;
    private float bE = 0.0F;

    public EntitySquid(World world) {
        super(world);
        this.texture = "/mob/squid.png";
        this.a(0.95F, 0.95F);
        this.bA = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected String aQ() {
        return null;
    }

    protected String aR() {
        return null;
    }

    protected String aS() {
        return null;
    }

    protected float aP() {
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

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean H() {
        return this.world.a(this.boundingBox.grow(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, this);
    }

    public void d() {
        super.d();
        this.e = this.d;
        this.g = this.f;
        this.i = this.h;
        this.by = this.j;
        this.h += this.bA;
        if (this.h > 6.2831855F) {
            this.h -= 6.2831855F;
            if (this.random.nextInt(10) == 0) {
                this.bA = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.H()) {
            float f;

            if (this.h < 3.1415927F) {
                f = this.h / 3.1415927F;
                this.j = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double) f > 0.75D) {
                    this.bz = 1.0F;
                    this.bB = 1.0F;
                } else {
                    this.bB *= 0.8F;
                }
            } else {
                this.j = 0.0F;
                this.bz *= 0.9F;
                this.bB *= 0.99F;
            }

            if (!this.world.isStatic) {
                this.motX = (double) (this.bC * this.bz);
                this.motY = (double) (this.bD * this.bz);
                this.motZ = (double) (this.bE * this.bz);
            }

            f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            this.aq += (-((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F - this.aq) * 0.1F;
            this.yaw = this.aq;
            this.f += 3.1415927F * this.bB * 1.5F;
            this.d += (-((float) Math.atan2((double) f, this.motY)) * 180.0F / 3.1415927F - this.d) * 0.1F;
        } else {
            this.j = MathHelper.abs(MathHelper.sin(this.h)) * 3.1415927F * 0.25F;
            if (!this.world.isStatic) {
                this.motX = 0.0D;
                this.motY -= 0.08D;
                this.motY *= 0.9800000190734863D;
                this.motZ = 0.0D;
            }

            this.d = (float) ((double) this.d + (double) (-90.0F - this.d) * 0.02D);
        }
    }

    public void e(float f, float f1) {
        this.move(this.motX, this.motY, this.motZ);
    }

    protected void be() {
        ++this.bq;
        if (this.bq > 100) {
            this.bC = this.bD = this.bE = 0.0F;
        } else if (this.random.nextInt(50) == 0 || !this.ac || this.bC == 0.0F && this.bD == 0.0F && this.bE == 0.0F) {
            float f = this.random.nextFloat() * 3.1415927F * 2.0F;

            this.bC = MathHelper.cos(f) * 0.2F;
            this.bD = -0.1F + this.random.nextFloat() * 0.2F;
            this.bE = MathHelper.sin(f) * 0.2F;
        }

        this.bb();
    }

    public boolean canSpawn() {
        return this.locY > 45.0D && this.locY < 63.0D && super.canSpawn();
    }
}
