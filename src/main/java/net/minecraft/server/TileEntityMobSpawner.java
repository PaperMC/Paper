package net.minecraft.server;

import java.util.Iterator;

public class TileEntityMobSpawner extends TileEntity {

    public int spawnDelay = -1;
    public String mobName = "Pig"; // CraftBukkit - private -> public
    private NBTTagCompound spawnData = null;
    public double b;
    public double c = 0.0D;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private int maxNearbyEntities = 6;
    private int requiredPlayerRange = 16;
    private int spawnRange = 4;

    public TileEntityMobSpawner() {
        this.spawnDelay = 20;
    }

    public void a(String s) {
        this.mobName = s;
    }

    public boolean b() {
        return this.world.findNearbyPlayer((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D, (double) this.requiredPlayerRange) != null;
    }

    public void g() {
        if (this.b()) {
            if (this.world.isStatic) {
                double d0 = (double) ((float) this.x + this.world.random.nextFloat());
                double d1 = (double) ((float) this.y + this.world.random.nextFloat());
                double d2 = (double) ((float) this.z + this.world.random.nextFloat());

                this.world.addParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                this.world.addParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }

                this.c = this.b;
                this.b = (this.b + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;
            } else {
                if (this.spawnDelay == -1) {
                    this.e();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                for (int i = 0; i < this.spawnCount; ++i) {
                    Entity entity = EntityTypes.createEntityByName(this.mobName, this.world);

                    if (entity == null) {
                        return;
                    }

                    int j = this.world.a(entity.getClass(), AxisAlignedBB.a().a((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).grow((double) (this.spawnRange * 2), 4.0D, (double) (this.spawnRange * 2))).size();

                    if (j >= this.maxNearbyEntities) {
                        this.e();
                        return;
                    }

                    if (entity != null) {
                        double d3 = (double) this.x + (this.world.random.nextDouble() - this.world.random.nextDouble()) * (double) this.spawnRange;
                        double d4 = (double) (this.y + this.world.random.nextInt(3) - 1);
                        double d5 = (double) this.z + (this.world.random.nextDouble() - this.world.random.nextDouble()) * (double) this.spawnRange;
                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;

                        entity.setPositionRotation(d3, d4, d5, this.world.random.nextFloat() * 360.0F, 0.0F);
                        if (entityliving == null || entityliving.canSpawn()) {
                            this.a(entity);
                            this.world.addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER); // CraftBukkit
                            this.world.triggerEffect(2004, this.x, this.y, this.z, 0);
                            if (entityliving != null) {
                                entityliving.aQ();
                            }

                            this.e();
                        }
                    }
                }
            }

            super.g();
        }
    }

    public void a(Entity entity) {
        if (this.spawnData != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entity.c(nbttagcompound);
            Iterator iterator = this.spawnData.c().iterator();

            while (iterator.hasNext()) {
                NBTBase nbtbase = (NBTBase) iterator.next();

                nbttagcompound.set(nbtbase.getName(), nbtbase.clone());
            }

            entity.e(nbttagcompound);
        } else if (entity instanceof EntityLiving && entity.world != null) {
            ((EntityLiving) entity).bD();
        }
    }

    private void e() {
        this.spawnDelay = this.minSpawnDelay + this.world.random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        this.world.playNote(this.x, this.y, this.z, this.q().id, 1, 0);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.mobName = nbttagcompound.getString("EntityId");
        this.spawnDelay = nbttagcompound.getShort("Delay");
        if (nbttagcompound.hasKey("SpawnData")) {
            this.spawnData = nbttagcompound.getCompound("SpawnData");
        } else {
            this.spawnData = null;
        }

        if (nbttagcompound.hasKey("MinSpawnDelay")) {
            this.minSpawnDelay = nbttagcompound.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbttagcompound.getShort("MaxSpawnDelay");
            this.spawnCount = nbttagcompound.getShort("SpawnCount");
        }

        if (nbttagcompound.hasKey("MaxNearbyEntities")) {
            this.maxNearbyEntities = nbttagcompound.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = nbttagcompound.getShort("RequiredPlayerRange");
        }

        if (nbttagcompound.hasKey("SpawnRange")) {
            this.spawnRange = nbttagcompound.getShort("SpawnRange");
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("EntityId", this.mobName);
        nbttagcompound.setShort("Delay", (short) this.spawnDelay);
        nbttagcompound.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
        nbttagcompound.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
        nbttagcompound.setShort("SpawnCount", (short) this.spawnCount);
        nbttagcompound.setShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
        nbttagcompound.setShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
        nbttagcompound.setShort("SpawnRange", (short) this.spawnRange);
        if (this.spawnData != null) {
            nbttagcompound.setCompound("SpawnData", this.spawnData);
        }
    }

    public Packet l() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 1, nbttagcompound);
    }

    public void b(int i, int j) {
        if (i == 1 && this.world.isStatic) {
            this.spawnDelay = this.minSpawnDelay;
        }
    }
}
