package net.minecraft.server;

import org.bukkit.craftbukkit.TrigMath; // CraftBukkit

public class EntitySquid extends EntityWaterAnimal {

    public float d = 0.0F;
    public float e = 0.0F;
    public float f = 0.0F;
    public float g = 0.0F;
    public float h = 0.0F;
    public float i = 0.0F;
    public float j = 0.0F;
    public float bK = 0.0F;
    private float bL = 0.0F;
    private float bM = 0.0F;
    private float bN = 0.0F;
    private float bO = 0.0F;
    private float bP = 0.0F;
    private float bQ = 0.0F;

    public EntitySquid(World world) {
        super(world);
        this.texture = "/mob/squid.png";
        this.a(0.95F, 0.95F);
        this.bM = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected String aW() {
        return null;
    }

    protected String aX() {
        return null;
    }

    protected String aY() {
        return null;
    }

    protected float aV() {
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
        return this.world.a(this.boundingBox.grow(0.0D, -0.6000000238418579D, 0.0D), Material.WATER, (Entity) this);
    }

    public void c() {
        super.c();
        this.e = this.d;
        this.g = this.f;
        this.i = this.h;
        this.bK = this.j;
        this.h += this.bM;
        if (this.h > 6.2831855F) {
            this.h -= 6.2831855F;
            if (this.random.nextInt(10) == 0) {
                this.bM = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.H()) {
            float f;

            if (this.h < 3.1415927F) {
                f = this.h / 3.1415927F;
                this.j = MathHelper.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double) f > 0.75D) {
                    this.bL = 1.0F;
                    this.bN = 1.0F;
                } else {
                    this.bN *= 0.8F;
                }
            } else {
                this.j = 0.0F;
                this.bL *= 0.9F;
                this.bN *= 0.99F;
            }

            if (!this.world.isStatic) {
                this.motX = (double) (this.bO * this.bL);
                this.motY = (double) (this.bN * this.bL);
                this.motZ = (double) (this.bQ * this.bL);
            }

            f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            // CraftBukkit - Math -> TrigMath
            this.aw += (-((float) TrigMath.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F - this.aw) * 0.1F;
            this.yaw = this.aw;
            this.f += 3.1415927F * this.bN * 1.5F;
            // CraftBukkit - Math -> TrigMath
            this.d += (-((float) TrigMath.atan2((double) f, this.motY)) * 180.0F / 3.1415927F - this.d) * 0.1F;
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

    protected void bk() {
        ++this.bC;
        if (this.bC > 100) {
            this.bO = this.bP = this.bQ = 0.0F;
        } else if (this.random.nextInt(50) == 0 || !this.ad || this.bO == 0.0F && this.bP == 0.0F && this.bQ == 0.0F) {
            float f = this.random.nextFloat() * 3.1415927F * 2.0F;

            this.bO = MathHelper.cos(f) * 0.2F;
            this.bP = -0.1F + this.random.nextFloat() * 0.2F;
            this.bQ = MathHelper.sin(f) * 0.2F;
        }

        this.bh();
    }

    public boolean canSpawn() {
        return this.locY > 45.0D && this.locY < 63.0D && super.canSpawn();
    }
}
