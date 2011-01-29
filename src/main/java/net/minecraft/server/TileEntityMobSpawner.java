package net.minecraft.server;

public class TileEntityMobSpawner extends TileEntity {

    public int e = -1;
    public String h = "Pig"; // CraftBukkit private -> public
    public double f;
    public double g = 0.0D;

    public TileEntityMobSpawner() {
        this.e = 20;
    }

    public void a(String s) {
        this.h = s;
    }

    public boolean a() {
        return this.a.a((double) this.b + 0.5D, (double) this.c + 0.5D, (double) this.d + 0.5D, 16.0D) != null;
    }

    public void f() {
        this.g = this.f;
        if (this.a()) {
            double d0 = (double) ((float) this.b + this.a.l.nextFloat());
            double d1 = (double) ((float) this.c + this.a.l.nextFloat());
            double d2 = (double) ((float) this.d + this.a.l.nextFloat());

            this.a.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            this.a.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

            for (this.f += (double) (1000.0F / ((float) this.e + 200.0F)); this.f > 360.0D; this.g -= 360.0D) {
                this.f -= 360.0D;
            }

            if (this.e == -1) {
                this.b();
            }

            if (this.e > 0) {
                --this.e;
            } else {
                byte b0 = 4;

                for (int i = 0; i < b0; ++i) {
                    EntityLiving entityliving = (EntityLiving) ((EntityLiving) EntityTypes.a(this.h, this.a));

                    if (entityliving == null) {
                        return;
                    }

                    int j = this.a.a(entityliving.getClass(), AxisAlignedBB.b((double) this.b, (double) this.c, (double) this.d, (double) (this.b + 1), (double) (this.c + 1), (double) (this.d + 1)).b(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.b();
                        return;
                    }

                    if (entityliving != null) {
                        double d3 = (double) this.b + (this.a.l.nextDouble() - this.a.l.nextDouble()) * 4.0D;
                        double d4 = (double) (this.c + this.a.l.nextInt(3) - 1);
                        double d5 = (double) this.d + (this.a.l.nextDouble() - this.a.l.nextDouble()) * 4.0D;

                        entityliving.c(d3, d4, d5, this.a.l.nextFloat() * 360.0F, 0.0F);
                        if (entityliving.b()) {
                            this.a.a((Entity) entityliving);

                            for (int k = 0; k < 20; ++k) {
                                d0 = (double) this.b + 0.5D + ((double) this.a.l.nextFloat() - 0.5D) * 2.0D;
                                d1 = (double) this.c + 0.5D + ((double) this.a.l.nextFloat() - 0.5D) * 2.0D;
                                d2 = (double) this.d + 0.5D + ((double) this.a.l.nextFloat() - 0.5D) * 2.0D;
                                this.a.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                                this.a.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                            }

                            entityliving.R();
                            this.b();
                        }
                    }
                }

                super.f();
            }
        }
    }

    private void b() {
        this.e = 200 + this.a.l.nextInt(600);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.h = nbttagcompound.h("EntityId");
        this.e = nbttagcompound.c("Delay");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("EntityId", this.h);
        nbttagcompound.a("Delay", (short) this.e);
    }
}
