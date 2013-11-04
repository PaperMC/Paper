package net.minecraft.server;

import org.bukkit.craftbukkit.TrigMath; // CraftBukkit

public class EntitySquid extends EntityWaterAnimal {

    public float bp;
    public float bq;
    public float br;
    public float bs;
    public float bt;
    public float bu;
    public float bv;
    public float bw;
    private float bx;
    private float by;
    private float bz;
    private float bA;
    private float bB;
    private float bC;

    public EntitySquid(World world) {
        super(world);
        this.a(0.95F, 0.95F);
        this.by = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.a).setValue(10.0D);
    }

    protected String t() {
        return null;
    }

    protected String aT() {
        return null;
    }

    protected String aU() {
        return null;
    }

    protected float bf() {
        return 0.4F;
    }

    protected Item getLoot() {
        return Item.d(0);
    }

    protected boolean g_() {
        return false;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - Whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        int count = this.random.nextInt(3 + i) + 1;
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.INK_SACK, count));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    /* CraftBukkit start - Delegate to Entity to use existing inWater value
    public boolean M() {
        return this.world.a(this.boundingBox.grow(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, (Entity) this);
    }
    // CraftBukkit end */

    public void e() {
        super.e();
        this.bq = this.bp;
        this.bs = this.br;
        this.bu = this.bt;
        this.bw = this.bv;
        this.bt += this.by;
        if (this.bt > 6.2831855F) {
            this.bt -= 6.2831855F;
            if (this.random.nextInt(10) == 0) {
                this.by = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.M()) {
            float f;

            if (this.bt < 3.1415927F) {
                f = this.bt / 3.1415927F;
                this.bv = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double) f > 0.75D) {
                    this.bx = 1.0F;
                    this.bz = 1.0F;
                } else {
                    this.bz *= 0.8F;
                }
            } else {
                this.bv = 0.0F;
                this.bx *= 0.9F;
                this.bz *= 0.99F;
            }

            if (!this.world.isStatic) {
                this.motX = (double) (this.bA * this.bx);
                this.motY = (double) (this.bB * this.bx);
                this.motZ = (double) (this.bC * this.bx);
            }

            f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            // CraftBukkit - Math -> TrigMath
            this.aN += (-((float) TrigMath.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F - this.aN) * 0.1F;
            this.yaw = this.aN;
            this.br += 3.1415927F * this.bz * 1.5F;
            // CraftBukkit - Math -> TrigMath
            this.bp += (-((float) TrigMath.atan2((double) f, this.motY)) * 180.0F / 3.1415927F - this.bp) * 0.1F;
        } else {
            this.bv = MathHelper.abs(MathHelper.sin(this.bt)) * 3.1415927F * 0.25F;
            if (!this.world.isStatic) {
                this.motX = 0.0D;
                this.motY -= 0.08D;
                this.motY *= 0.9800000190734863D;
                this.motZ = 0.0D;
            }

            this.bp = (float) ((double) this.bp + (double) (-90.0F - this.bp) * 0.02D);
        }
    }

    public void e(float f, float f1) {
        this.move(this.motX, this.motY, this.motZ);
    }

    protected void bq() {
        ++this.aV;
        if (this.aV > 100) {
            this.bA = this.bB = this.bC = 0.0F;
        } else if (this.random.nextInt(50) == 0 || !this.inWater || this.bA == 0.0F && this.bB == 0.0F && this.bC == 0.0F) {
            float f = this.random.nextFloat() * 3.1415927F * 2.0F;

            this.bA = MathHelper.cos(f) * 0.2F;
            this.bB = -0.1F + this.random.nextFloat() * 0.2F;
            this.bC = MathHelper.sin(f) * 0.2F;
        }

        this.w();
    }

    public boolean canSpawn() {
        return this.locY > 45.0D && this.locY < 63.0D && super.canSpawn();
    }
}
