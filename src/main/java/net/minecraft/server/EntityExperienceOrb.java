package net.minecraft.server;

public class EntityExperienceOrb extends Entity {

    public int a;
    public int b = 0;
    public int c;
    private int e = 5;
    public int value; // CraftBukkit - priv to pub
    public float d = (float) (Math.random() * 3.141592653589793D * 2.0D);

    public EntityExperienceOrb(World world, double d0, double d1, double d2, int i) {
        super(world);
        this.b(0.5F, 0.5F);
        this.height = this.width / 2.0F;
        this.setPosition(d0, d1, d2);
        this.yaw = (float) (Math.random() * 360.0D);
        this.motX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.motZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.value = i;
    }

    protected boolean e_() {
        return false;
    }

    public EntityExperienceOrb(World world) {
        super(world);
        this.b(0.25F, 0.25F);
        this.height = this.width / 2.0F;
    }

    protected void b() {}

    public void s_() {
        super.s_();
        if (this.c > 0) {
            --this.c;
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.029999999329447746D;
        if (this.world.getMaterial(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) == Material.LAVA) {
            this.motY = 0.20000000298023224D;
            this.motX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.motZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.world.makeSound(this, "random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        this.g(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
        double d0 = 8.0D;
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, d0);

        if (entityhuman != null) {
            double d1 = (entityhuman.locX - this.locX) / d0;
            double d2 = (entityhuman.locY + (double) entityhuman.t() - this.locY) / d0;
            double d3 = (entityhuman.locZ - this.locZ) / d0;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D) {
                d5 *= d5;
                this.motX += d1 / d4 * d5 * 0.1D;
                this.motY += d2 / d4 * d5 * 0.1D;
                this.motZ += d3 / d4 * d5 * 0.1D;
            }
        }

        this.move(this.motX, this.motY, this.motZ);
        float f = 0.98F;

        if (this.onGround) {
            f = 0.58800006F;
            int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

            if (i > 0) {
                f = Block.byId[i].frictionFactor * 0.98F;
            }
        }

        this.motX *= (double) f;
        this.motY *= 0.9800000190734863D;
        this.motZ *= (double) f;
        if (this.onGround) {
            this.motY *= -0.8999999761581421D;
        }

        ++this.a;
        ++this.b;
        if (this.b >= 6000) {
            this.die();
        }
    }

    public boolean f_() {
        return this.world.a(this.boundingBox, Material.WATER, this);
    }

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.aq();
        this.e -= i;
        if (this.e <= 0) {
            this.die();
        }

        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) ((byte) this.e));
        nbttagcompound.a("Age", (short) this.b);
        nbttagcompound.a("Value", (short) this.value);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.d("Health") & 255;
        this.b = nbttagcompound.d("Age");
        this.value = nbttagcompound.d("Value");
    }

    public void a_(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            if (this.c == 0 && entityhuman.w == 0) {
                entityhuman.w = 2;
                this.world.makeSound(this, "random.pop", 0.2F, 0.5F * ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F));
                entityhuman.receive(this, 1);
                entityhuman.d(this.value);
                this.die();
            }
        }
    }

    public int j_() {
        return this.value;
    }

    public static int b(int i) {
        // CraftBukkit start
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        // CraftBukkit end

        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }
}
