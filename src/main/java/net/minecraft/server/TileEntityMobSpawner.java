package net.minecraft.server;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason; // CraftBukkit

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
        return this.world.a((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D, 16.0D) != null;
    }

    // CraftBukkit start
    public int getId() {
        return EntityTypes.getIdFromClass(EntityTypes.getClassFromName(mobName));
    }

    public void setId(int id) {
        mobName = EntityTypes.getNameFromClass(EntityTypes.getClassFromId(id));
        if (mobName == null || mobName.length() == 0) {
            mobName = "Pig";
        }
        if  (EntityTypes.a(mobName,  world) == null) {
            mobName = "Pig";
        }
    }
    // CraftBukkit end

    public void h_() {
        this.c = this.b;
        if (this.a()) {
            double d0 = (double) ((float) this.x + this.world.random.nextFloat());
            double d1 = (double) ((float) this.y + this.world.random.nextFloat());
            double d2 = (double) ((float) this.z + this.world.random.nextFloat());

            this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

            for (this.b += (double) (1000.0F / ((float) this.spawnDelay + 200.0F)); this.b > 360.0D; this.c -= 360.0D) {
                this.b -= 360.0D;
            }

            if (!this.world.isStatic) {
                if (this.spawnDelay == -1) {
                    this.c();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                byte b0 = 4;

                for (int i = 0; i < b0; ++i) {
                    // CraftBukkit start
                    Entity entityliving = EntityTypes.a(this.mobName, this.world);

                    if (entityliving == null) {
                        return;
                    }

                    int j = this.world.a(entityliving.getClass(), AxisAlignedBB.b((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).b(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.c();
                        return;
                    }

                    if (entityliving != null) {
                        double d3 = (double) this.x + (this.world.random.nextDouble() - this.world.random.nextDouble()) * 4.0D;
                        double d4 = (double) (this.y + this.world.random.nextInt(3) - 1);
                        double d5 = (double) this.z + (this.world.random.nextDouble() - this.world.random.nextDouble()) * 4.0D;

                        entityliving.setPositionRotation(d3, d4, d5, this.world.random.nextFloat() * 360.0F, 0.0F);
                        if ((entityliving instanceof EntityLiving && ((EntityLiving)entityliving).d()) ||
                            (!(entityliving instanceof EntityLiving) && entityliving.world.containsEntity(entityliving.boundingBox) && entityliving.world.getEntities(entityliving, entityliving.boundingBox).size() == 0 && !entityliving.world.c(entityliving.boundingBox))) {
                            // CraftBukkit end
                            // CraftBukkit - added a reason for spawning this creature
                            this.world.addEntity(entityliving, SpawnReason.SPAWNER);

                            for (int k = 0; k < 20; ++k) {
                                d0 = (double) this.x + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                d1 = (double) this.y + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                d2 = (double) this.z + 0.5D + ((double) this.world.random.nextFloat() - 0.5D) * 2.0D;
                                this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                                this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                            }

                            // entity.ab(); // CraftBukkit - client side code, and not available in entity
                            this.c();
                        }
                    }
                }
            }

            super.h_();
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
