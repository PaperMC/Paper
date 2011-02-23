package net.minecraft.server;

public class TileEntityMobSpawner extends TileEntity {

    public int a = -1;
    public String h = "Pig"; // CraftBukkit private -> public
    public double b;
    public double c = 0.0D;

    public TileEntityMobSpawner() {
        this.a = 20;
    }

    public void a(String s) {
        this.h = s;
    }

    public boolean a() {
        return this.d.a((double) this.e + 0.5D, (double) this.f + 0.5D, (double) this.g + 0.5D, 16.0D) != null;
    }

    public void i_() {
        this.c = this.b;
        if (this.a()) {
            double d0 = (double) ((float) this.e + this.d.k.nextFloat());
            double d1 = (double) ((float) this.f + this.d.k.nextFloat());
            double d2 = (double) ((float) this.g + this.d.k.nextFloat());

            this.d.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            this.d.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

            for (this.b += (double) (1000.0F / ((float) this.a + 200.0F)); this.b > 360.0D; this.c -= 360.0D) {
                this.b -= 360.0D;
            }

            if (this.a == -1) {
                this.c();
            }

            if (this.a > 0) {
                --this.a;
            } else {
                byte b0 = 4;

                for (int i = 0; i < b0; ++i) {
                    EntityLiving entityliving = (EntityLiving) ((EntityLiving) EntityTypes.a(this.h, this.d));

                    if (entityliving == null) {
                        return;
                    }

                    // CraftBukkit start - The world we're spawning in accepts this creature
                    boolean isAnimal = entityliving instanceof EntityAnimal || entityliving instanceof EntityWaterAnimal;
                    if ((isAnimal && !this.d.E) || (!isAnimal && !this.d.D)) {
                        return;
                    }
                    // CraftBukkit end

                    int j = this.d.a(entityliving.getClass(), AxisAlignedBB.b((double) this.e, (double) this.f, (double) this.g, (double) (this.e + 1), (double) (this.f + 1), (double) (this.g + 1)).b(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.c();
                        return;
                    }

                    if (entityliving != null) {
                        double d3 = (double) this.e + (this.d.k.nextDouble() - this.d.k.nextDouble()) * 4.0D;
                        double d4 = (double) (this.f + this.d.k.nextInt(3) - 1);
                        double d5 = (double) this.g + (this.d.k.nextDouble() - this.d.k.nextDouble()) * 4.0D;

                        entityliving.c(d3, d4, d5, this.d.k.nextFloat() * 360.0F, 0.0F);
                        if (entityliving.b()) {
                            this.d.a((Entity) entityliving);

                            for (int k = 0; k < 20; ++k) {
                                d0 = (double) this.e + 0.5D + ((double) this.d.k.nextFloat() - 0.5D) * 2.0D;
                                d1 = (double) this.f + 0.5D + ((double) this.d.k.nextFloat() - 0.5D) * 2.0D;
                                d2 = (double) this.g + 0.5D + ((double) this.d.k.nextFloat() - 0.5D) * 2.0D;
                                this.d.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                                this.d.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                            }

                            entityliving.I();
                            this.c();
                        }
                    }
                }

                super.i_();
            }
        }
    }

    private void c() {
        this.a = 200 + this.d.k.nextInt(600);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.h = nbttagcompound.i("EntityId");
        this.a = nbttagcompound.d("Delay");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("EntityId", this.h);
        nbttagcompound.a("Delay", (short) this.a);
    }
}
