package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MobSpawnerAbstract {

    private static final Logger LOGGER = LogManager.getLogger();
    public int spawnDelay = 20;
    public final List<MobSpawnerData> mobs = Lists.newArrayList();
    public MobSpawnerData spawnData = new MobSpawnerData();
    private double e;
    private double f;
    public int minSpawnDelay = 200;
    public int maxSpawnDelay = 800;
    public int spawnCount = 4;
    @Nullable
    private Entity j;
    public int maxNearbyEntities = 6;
    public int requiredPlayerRange = 16;
    public int spawnRange = 4;

    public MobSpawnerAbstract() {}

    @Nullable
    public MinecraftKey getMobName() {
        String s = this.spawnData.getEntity().getString("id");

        try {
            return UtilColor.b(s) ? null : new MinecraftKey(s);
        } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
            BlockPosition blockposition = this.b();

            MobSpawnerAbstract.LOGGER.warn("Invalid entity id '{}' at spawner {}:[{},{},{}]", s, this.a().getDimensionKey().a(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
            return null;
        }
    }

    public void setMobName(EntityTypes<?> entitytypes) {
        this.spawnData.getEntity().setString("id", IRegistry.ENTITY_TYPE.getKey(entitytypes).toString());
    }

    private boolean h() {
        BlockPosition blockposition = this.b();

        return this.a().isPlayerNearby((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, (double) this.requiredPlayerRange);
    }

    public void c() {
        if (!this.h()) {
            this.f = this.e;
        } else {
            World world = this.a();
            BlockPosition blockposition = this.b();

            if (!(world instanceof WorldServer)) {
                double d0 = (double) blockposition.getX() + world.random.nextDouble();
                double d1 = (double) blockposition.getY() + world.random.nextDouble();
                double d2 = (double) blockposition.getZ() + world.random.nextDouble();

                world.addParticle(Particles.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                world.addParticle(Particles.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }

                this.f = this.e;
                this.e = (this.e + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;
            } else {
                if (this.spawnDelay == -1) {
                    this.i();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i) {
                    NBTTagCompound nbttagcompound = this.spawnData.getEntity();
                    Optional<EntityTypes<?>> optional = EntityTypes.a(nbttagcompound);

                    if (!optional.isPresent()) {
                        this.i();
                        return;
                    }

                    NBTTagList nbttaglist = nbttagcompound.getList("Pos", 6);
                    int j = nbttaglist.size();
                    double d3 = j >= 1 ? nbttaglist.h(0) : (double) blockposition.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;
                    double d4 = j >= 2 ? nbttaglist.h(1) : (double) (blockposition.getY() + world.random.nextInt(3) - 1);
                    double d5 = j >= 3 ? nbttaglist.h(2) : (double) blockposition.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double) this.spawnRange + 0.5D;

                    if (world.b(((EntityTypes) optional.get()).a(d3, d4, d5))) {
                        WorldServer worldserver = (WorldServer) world;

                        if (EntityPositionTypes.a((EntityTypes) optional.get(), worldserver, EnumMobSpawn.SPAWNER, new BlockPosition(d3, d4, d5), world.getRandom())) {
                            Entity entity = EntityTypes.a(nbttagcompound, world, (entity1) -> {
                                entity1.setPositionRotation(d3, d4, d5, entity1.yaw, entity1.pitch);
                                return entity1;
                            });

                            if (entity == null) {
                                this.i();
                                return;
                            }

                            int k = world.a(entity.getClass(), (new AxisAlignedBB((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1))).g((double) this.spawnRange)).size();

                            if (k >= this.maxNearbyEntities) {
                                this.i();
                                return;
                            }

                            entity.setPositionRotation(entity.locX(), entity.locY(), entity.locZ(), world.random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof EntityInsentient) {
                                EntityInsentient entityinsentient = (EntityInsentient) entity;

                                if (!entityinsentient.a((GeneratorAccess) world, EnumMobSpawn.SPAWNER) || !entityinsentient.a((IWorldReader) world)) {
                                    continue;
                                }

                                if (this.spawnData.getEntity().e() == 1 && this.spawnData.getEntity().hasKeyOfType("id", 8)) {
                                    ((EntityInsentient) entity).prepare(worldserver, world.getDamageScaler(entity.getChunkCoordinates()), EnumMobSpawn.SPAWNER, (GroupDataEntity) null, (NBTTagCompound) null);
                                }
                            }

                            if (!worldserver.addAllEntitiesSafely(entity)) {
                                this.i();
                                return;
                            }

                            world.triggerEffect(2004, blockposition, 0);
                            if (entity instanceof EntityInsentient) {
                                ((EntityInsentient) entity).doSpawnEffect();
                            }

                            flag = true;
                        }
                    }
                }

                if (flag) {
                    this.i();
                }
            }

        }
    }

    private void i() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;

            this.spawnDelay = this.minSpawnDelay + this.a().random.nextInt(i);
        }

        if (!this.mobs.isEmpty()) {
            this.setSpawnData((MobSpawnerData) WeightedRandom.a(this.a().random, this.mobs));
        }

        this.a(1);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.spawnDelay = nbttagcompound.getShort("Delay");
        this.mobs.clear();
        if (nbttagcompound.hasKeyOfType("SpawnPotentials", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                this.mobs.add(new MobSpawnerData(nbttaglist.getCompound(i)));
            }
        }

        if (nbttagcompound.hasKeyOfType("SpawnData", 10)) {
            this.setSpawnData(new MobSpawnerData(1, nbttagcompound.getCompound("SpawnData")));
        } else if (!this.mobs.isEmpty()) {
            this.setSpawnData((MobSpawnerData) WeightedRandom.a(this.a().random, this.mobs));
        }

        if (nbttagcompound.hasKeyOfType("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nbttagcompound.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbttagcompound.getShort("MaxSpawnDelay");
            this.spawnCount = nbttagcompound.getShort("SpawnCount");
        }

        if (nbttagcompound.hasKeyOfType("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nbttagcompound.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = nbttagcompound.getShort("RequiredPlayerRange");
        }

        if (nbttagcompound.hasKeyOfType("SpawnRange", 99)) {
            this.spawnRange = nbttagcompound.getShort("SpawnRange");
        }

        if (this.a() != null) {
            this.j = null;
        }

    }

    public NBTTagCompound b(NBTTagCompound nbttagcompound) {
        MinecraftKey minecraftkey = this.getMobName();

        if (minecraftkey == null) {
            return nbttagcompound;
        } else {
            nbttagcompound.setShort("Delay", (short) this.spawnDelay);
            nbttagcompound.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
            nbttagcompound.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
            nbttagcompound.setShort("SpawnCount", (short) this.spawnCount);
            nbttagcompound.setShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
            nbttagcompound.setShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
            nbttagcompound.setShort("SpawnRange", (short) this.spawnRange);
            nbttagcompound.set("SpawnData", this.spawnData.getEntity().clone());
            NBTTagList nbttaglist = new NBTTagList();

            if (this.mobs.isEmpty()) {
                nbttaglist.add(this.spawnData.a());
            } else {
                Iterator iterator = this.mobs.iterator();

                while (iterator.hasNext()) {
                    MobSpawnerData mobspawnerdata = (MobSpawnerData) iterator.next();

                    nbttaglist.add(mobspawnerdata.a());
                }
            }

            nbttagcompound.set("SpawnPotentials", nbttaglist);
            return nbttagcompound;
        }
    }

    public boolean b(int i) {
        if (i == 1 && this.a().isClientSide) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        } else {
            return false;
        }
    }

    public void setSpawnData(MobSpawnerData mobspawnerdata) {
        this.spawnData = mobspawnerdata;
    }

    public abstract void a(int i);

    public abstract World a();

    public abstract BlockPosition b();
}
