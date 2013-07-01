package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.event.entity.CreatureSpawnEvent; // CraftBukkit

public abstract class MobSpawnerAbstract {

    public int spawnDelay = 20;
    private String mobName = "Pig";
    private List mobs;
    private TileEntityMobSpawnerData spawnData;
    public double c;
    public double d;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private Entity j;
    private int maxNearbyEntities = 6;
    private int requiredPlayerRange = 16;
    private int spawnRange = 4;

    public MobSpawnerAbstract() {}

    public String getMobName() {
        if (this.i() == null) {
            if (this.mobName.equals("Minecart")) {
                this.mobName = "MinecartRideable";
            }

            return this.mobName;
        } else {
            return this.i().c;
        }
    }

    public void a(String s) {
        this.mobName = s;
    }

    public boolean f() {
        return this.a().findNearbyPlayer((double) this.b() + 0.5D, (double) this.c() + 0.5D, (double) this.d() + 0.5D, (double) this.requiredPlayerRange) != null;
    }

    public void g() {
        if (this.f()) {
            double d0;

            if (this.a().isStatic) {
                double d1 = (double) ((float) this.b() + this.a().random.nextFloat());
                double d2 = (double) ((float) this.c() + this.a().random.nextFloat());

                d0 = (double) ((float) this.d() + this.a().random.nextFloat());
                this.a().addParticle("smoke", d1, d2, d0, 0.0D, 0.0D, 0.0D);
                this.a().addParticle("flame", d1, d2, d0, 0.0D, 0.0D, 0.0D);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }

                this.d = this.c;
                this.c = (this.c + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;
            } else {
                if (this.spawnDelay == -1) {
                    this.j();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i) {
                    Entity entity = EntityTypes.createEntityByName(this.getMobName(), this.a());

                    if (entity == null) {
                        return;
                    }

                    int j = this.a().a(entity.getClass(), AxisAlignedBB.a().a((double) this.b(), (double) this.c(), (double) this.d(), (double) (this.b() + 1), (double) (this.c() + 1), (double) (this.d() + 1)).grow((double) (this.spawnRange * 2), 4.0D, (double) (this.spawnRange * 2))).size();

                    if (j >= this.maxNearbyEntities) {
                        this.j();
                        return;
                    }

                    d0 = (double) this.b() + (this.a().random.nextDouble() - this.a().random.nextDouble()) * (double) this.spawnRange;
                    double d3 = (double) (this.c() + this.a().random.nextInt(3) - 1);
                    double d4 = (double) this.d() + (this.a().random.nextDouble() - this.a().random.nextDouble()) * (double) this.spawnRange;
                    EntityInsentient entityinsentient = entity instanceof EntityInsentient ? (EntityInsentient) entity : null;

                    entity.setPositionRotation(d0, d3, d4, this.a().random.nextFloat() * 360.0F, 0.0F);
                    if (entityinsentient == null || entityinsentient.canSpawn()) {
                        this.a(entity);
                        this.a().triggerEffect(2004, this.b(), this.c(), this.d(), 0);
                        if (entityinsentient != null) {
                            entityinsentient.q();
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.j();
                }
            }
        }
    }

    public Entity a(Entity entity) {
        if (this.i() != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entity.d(nbttagcompound);
            Iterator iterator = this.i().b.c().iterator();

            while (iterator.hasNext()) {
                NBTBase nbtbase = (NBTBase) iterator.next();

                nbttagcompound.set(nbtbase.getName(), nbtbase.clone());
            }

            entity.f(nbttagcompound);
            if (entity.world != null) {
                entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.SPAWNER); // CraftBukkit
            }

            NBTTagCompound nbttagcompound1;

            for (Entity entity1 = entity; nbttagcompound.hasKey("Riding"); nbttagcompound = nbttagcompound1) {
                nbttagcompound1 = nbttagcompound.getCompound("Riding");
                Entity entity2 = EntityTypes.createEntityByName(nbttagcompound1.getString("id"), entity.world);

                if (entity2 != null) {
                    NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                    entity2.d(nbttagcompound2);
                    Iterator iterator1 = nbttagcompound1.c().iterator();

                    while (iterator1.hasNext()) {
                        NBTBase nbtbase1 = (NBTBase) iterator1.next();

                        nbttagcompound2.set(nbtbase1.getName(), nbtbase1.clone());
                    }

                    entity2.f(nbttagcompound2);
                    entity2.setPositionRotation(entity1.locX, entity1.locY, entity1.locZ, entity1.yaw, entity1.pitch);
                    if (entity.world != null) {
                        entity.world.addEntity(entity2, CreatureSpawnEvent.SpawnReason.SPAWNER); // CraftBukkit
                    }

                    entity1.mount(entity2);
                }

                entity1 = entity2;
            }
        } else if (entity instanceof EntityLiving && entity.world != null) {
            ((EntityInsentient) entity).a((GroupDataEntity) null);
            this.a().addEntity(entity, CreatureSpawnEvent.SpawnReason.SPAWNER); // CraftBukkit
        }

        return entity;
    }

    private void j() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;

            this.spawnDelay = this.minSpawnDelay + this.a().random.nextInt(i);
        }

        if (this.mobs != null && this.mobs.size() > 0) {
            this.a((TileEntityMobSpawnerData) WeightedRandom.a(this.a().random, (Collection) this.mobs));
        }

        this.a(1);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.mobName = nbttagcompound.getString("EntityId");
        this.spawnDelay = nbttagcompound.getShort("Delay");
        if (nbttagcompound.hasKey("SpawnPotentials")) {
            this.mobs = new ArrayList();
            NBTTagList nbttaglist = nbttagcompound.getList("SpawnPotentials");

            for (int i = 0; i < nbttaglist.size(); ++i) {
                this.mobs.add(new TileEntityMobSpawnerData(this, (NBTTagCompound) nbttaglist.get(i)));
            }
        } else {
            this.mobs = null;
        }

        if (nbttagcompound.hasKey("SpawnData")) {
            this.a(new TileEntityMobSpawnerData(this, nbttagcompound.getCompound("SpawnData"), this.mobName));
        } else {
            this.a((TileEntityMobSpawnerData) null);
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

        if (this.a() != null && this.a().isStatic) {
            this.j = null;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("EntityId", this.getMobName());
        nbttagcompound.setShort("Delay", (short) this.spawnDelay);
        nbttagcompound.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
        nbttagcompound.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
        nbttagcompound.setShort("SpawnCount", (short) this.spawnCount);
        nbttagcompound.setShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
        nbttagcompound.setShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
        nbttagcompound.setShort("SpawnRange", (short) this.spawnRange);
        if (this.i() != null) {
            nbttagcompound.setCompound("SpawnData", (NBTTagCompound) this.i().b.clone());
        }

        if (this.i() != null || this.mobs != null && this.mobs.size() > 0) {
            NBTTagList nbttaglist = new NBTTagList();

            if (this.mobs != null && this.mobs.size() > 0) {
                Iterator iterator = this.mobs.iterator();

                while (iterator.hasNext()) {
                    TileEntityMobSpawnerData tileentitymobspawnerdata = (TileEntityMobSpawnerData) iterator.next();

                    nbttaglist.add(tileentitymobspawnerdata.a());
                }
            } else {
                nbttaglist.add(this.i().a());
            }

            nbttagcompound.set("SpawnPotentials", nbttaglist);
        }
    }

    public boolean b(int i) {
        if (i == 1 && this.a().isStatic) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        } else {
            return false;
        }
    }

    public TileEntityMobSpawnerData i() {
        return this.spawnData;
    }

    public void a(TileEntityMobSpawnerData tileentitymobspawnerdata) {
        this.spawnData = tileentitymobspawnerdata;
    }

    public abstract void a(int i);

    public abstract World a();

    public abstract int b();

    public abstract int c();

    public abstract int d();
}
