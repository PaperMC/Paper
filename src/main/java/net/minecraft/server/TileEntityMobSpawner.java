package net.minecraft.server;

public class TileEntityMobSpawner extends TileEntity {

    public int spawnDelay = -1;
    public String mobName = "Pig"; // CraftBukkit - private -> public
    public double b;
    public double c = 0.0D;

    public TileEntityMobSpawner() {
        this.spawnDelay = 20;
    }

    public void a(String s) {
        this.mobName = s;
    }

    public boolean a() {
        return this.world.a((double) this.e + 0.5D, (double) this.f + 0.5D, (double) this.g + 0.5D, 16.0D) != null;
    }

    public void g_() {
        this.c = this.b;
        if (this.a()) {
            double d0 = (double) ((float) this.e + this.world.random.nextFloat());
            double d1 = (double) ((float) this.f + this.world.random.nextFloat());
            double d2 = (double) ((float) this.g + this.world.random.nextFloat());

            this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

            for (this.b += (double) (1000.0F / ((float) this.spawnDelay + 200.0F)); this.b > 360.0D; this.c -= 360.0D) {
                this.b -= 360.0D;
            }

            if (this.spawnDelay == -1) {
                this.c();
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                byte b0 = 4;

                for (int i = 0; i < b0; ++i) {
                    EntityLiving entityliving = (EntityLiving) ((EntityLiving) EntityTypes.a(this.mobName, this.world));

                    if (entityliving == null) {
                        return;
                    }

                    // CraftBukkit start - The world we're spawning in accepts this creature
                    boolean isAnimal = entityliving instanceof EntityAnimal || entityliving instanceof EntityWaterAnimal;
                    if ((isAnimal && !this.world.allowAnimals) || (!isAnimal && !this.world.allowMonsters)) {
                        return;
                    }
                    // CraftBukkit end

                    int j = this.world.a(entityliving.getClass(), AxisAlignedBB.b((double) this.e, (double) this.f, (double) this.g, (double) (this.e + 1), (double) (this.f + 1), (double) (this.g + 1)).b(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.c();
                        return;
                    }

                    if (entityliving != null) {
                        double d3 = (double) this.e + (this.world.random.nextDouble() - this.world.random.nextDouble()) * 4.0D;
                        double d4 = (double) (this.f + this.world.random.nextInt(3) - 1);
                        double d5 = (double) this.g + (this.world.random.nextDouble() - this.world.random.nextDouble()) * 4.0D;

                        entityliving.setPositionRotation(d3, d4, d5, this.world.random.nextFloat() * 360.0F, 0.0F);
                        if (entityliving.d()) {
                            this.world.addEntity(entityliving);

                            for (int k = 0; k < 20; ++k) {
                                d0 = (double) this.e + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                d1 = (double) this.f + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                d2 = (double) this.g + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                                this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                            }

                            entityliving.O();
                            this.c();
                        }
                    }
                }

                super.g_();
            }
        }
    }

    private void c() {
        this.spawnDelay = 200 + this.world.random.nextInt(600);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.mobName = nbttagcompound.getString("EntityId");
        this.spawnDelay = nbttagcompound.d("Delay");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("EntityId", this.mobName);
        nbttagcompound.a("Delay", (short) this.spawnDelay);
    }
}
