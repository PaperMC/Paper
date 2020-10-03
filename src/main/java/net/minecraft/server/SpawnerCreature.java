package net.minecraft.server;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public final class SpawnerCreature {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int b = (int) Math.pow(17.0D, 2.0D);
    private static final EnumCreatureType[] c = (EnumCreatureType[]) Stream.of(EnumCreatureType.values()).filter((enumcreaturetype) -> {
        return enumcreaturetype != EnumCreatureType.MISC;
    }).toArray((i) -> {
        return new EnumCreatureType[i];
    });

    public static SpawnerCreature.d a(int i, Iterable<Entity> iterable, SpawnerCreature.b spawnercreature_b) {
        SpawnerCreatureProbabilities spawnercreatureprobabilities = new SpawnerCreatureProbabilities();
        Object2IntOpenHashMap<EnumCreatureType> object2intopenhashmap = new Object2IntOpenHashMap();
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityInsentient) {
                EntityInsentient entityinsentient = (EntityInsentient) entity;

                // CraftBukkit - Split out persistent check, don't apply it to special persistent mobs
                if (entityinsentient.isTypeNotPersistent(0) && entityinsentient.isPersistent()) {
                    continue;
                }
            }

            EnumCreatureType enumcreaturetype = entity.getEntityType().e();

            if (enumcreaturetype != EnumCreatureType.MISC) {
                BlockPosition blockposition = entity.getChunkCoordinates();
                long j = ChunkCoordIntPair.pair(blockposition.getX() >> 4, blockposition.getZ() >> 4);

                spawnercreature_b.query(j, (chunk) -> {
                    BiomeSettingsMobs.b biomesettingsmobs_b = b(blockposition, chunk).b().a(entity.getEntityType());

                    if (biomesettingsmobs_b != null) {
                        spawnercreatureprobabilities.a(entity.getChunkCoordinates(), biomesettingsmobs_b.b());
                    }

                    object2intopenhashmap.addTo(enumcreaturetype, 1);
                });
            }
        }

        return new SpawnerCreature.d(i, object2intopenhashmap, spawnercreatureprobabilities);
    }

    private static BiomeBase b(BlockPosition blockposition, IChunkAccess ichunkaccess) {
        return GenLayerZoomerBiome.INSTANCE.a(0L, blockposition.getX(), blockposition.getY(), blockposition.getZ(), ichunkaccess.getBiomeIndex());
    }

    public static void a(WorldServer worldserver, Chunk chunk, SpawnerCreature.d spawnercreature_d, boolean flag, boolean flag1, boolean flag2) {
        worldserver.getMethodProfiler().enter("spawner");
        EnumCreatureType[] aenumcreaturetype = SpawnerCreature.c;
        int i = aenumcreaturetype.length;

        // CraftBukkit start - Other mob type spawn tick rate
        WorldData worlddata = worldserver.getWorldData();
        boolean spawnAnimalThisTick = worldserver.ticksPerAnimalSpawns != 0L && worlddata.getTime() % worldserver.ticksPerAnimalSpawns == 0L;
        boolean spawnMonsterThisTick = worldserver.ticksPerMonsterSpawns != 0L && worlddata.getTime() % worldserver.ticksPerMonsterSpawns == 0L;
        boolean spawnWaterThisTick = worldserver.ticksPerWaterSpawns != 0L && worlddata.getTime() % worldserver.ticksPerWaterSpawns == 0L;
        boolean spawnAmbientThisTick = worldserver.ticksPerAmbientSpawns != 0L && worlddata.getTime() % worldserver.ticksPerAmbientSpawns == 0L;
        boolean spawnWaterAmbientThisTick = worldserver.ticksPerWaterAmbientSpawns != 0L && worlddata.getTime() % worldserver.ticksPerWaterAmbientSpawns == 0L;
        // CraftBukkit end

        for (int j = 0; j < i; ++j) {
            EnumCreatureType enumcreaturetype = aenumcreaturetype[j];
            // CraftBukkit start - Use per-world spawn limits
            boolean spawnThisTick = true;
            int limit = enumcreaturetype.c();
            switch (enumcreaturetype) {
                case MONSTER:
                    spawnThisTick = spawnMonsterThisTick;
                    limit = worldserver.getWorld().getMonsterSpawnLimit();
                    break;
                case CREATURE:
                    spawnThisTick = spawnAnimalThisTick;
                    limit = worldserver.getWorld().getAnimalSpawnLimit();
                    break;
                case WATER_CREATURE:
                    spawnThisTick = spawnWaterThisTick;
                    limit = worldserver.getWorld().getWaterAnimalSpawnLimit();
                    break;
                case AMBIENT:
                    spawnThisTick = spawnAmbientThisTick;
                    limit = worldserver.getWorld().getAmbientSpawnLimit();
                    break;
                case WATER_AMBIENT:
                    spawnThisTick = spawnWaterAmbientThisTick;
                    limit = worldserver.getWorld().getWaterAmbientSpawnLimit();
                    break;
            }

            if (!spawnThisTick || limit == 0) {
                continue;
            }

            if ((flag || !enumcreaturetype.d()) && (flag1 || enumcreaturetype.d()) && (flag2 || !enumcreaturetype.e()) && spawnercreature_d.a(enumcreaturetype, limit)) {
                // CraftBukkit end
                a(enumcreaturetype, worldserver, chunk, (entitytypes, blockposition, ichunkaccess) -> {
                    return spawnercreature_d.a(entitytypes, blockposition, ichunkaccess);
                }, (entityinsentient, ichunkaccess) -> {
                    spawnercreature_d.a(entityinsentient, ichunkaccess);
                });
            }
        }

        worldserver.getMethodProfiler().exit();
    }

    public static void a(EnumCreatureType enumcreaturetype, WorldServer worldserver, Chunk chunk, SpawnerCreature.c spawnercreature_c, SpawnerCreature.a spawnercreature_a) {
        BlockPosition blockposition = getRandomPosition(worldserver, chunk);

        if (blockposition.getY() >= 1) {
            a(enumcreaturetype, worldserver, (IChunkAccess) chunk, blockposition, spawnercreature_c, spawnercreature_a);
        }
    }

    public static void a(EnumCreatureType enumcreaturetype, WorldServer worldserver, IChunkAccess ichunkaccess, BlockPosition blockposition, SpawnerCreature.c spawnercreature_c, SpawnerCreature.a spawnercreature_a) {
        StructureManager structuremanager = worldserver.getStructureManager();
        ChunkGenerator chunkgenerator = worldserver.getChunkProvider().getChunkGenerator();
        int i = blockposition.getY();
        IBlockData iblockdata = ichunkaccess.getType(blockposition);

        if (!iblockdata.isOccluding(ichunkaccess, blockposition)) {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            int j = 0;
            int k = 0;

            while (k < 3) {
                int l = blockposition.getX();
                int i1 = blockposition.getZ();
                boolean flag = true;
                BiomeSettingsMobs.c biomesettingsmobs_c = null;
                GroupDataEntity groupdataentity = null;
                int j1 = MathHelper.f(worldserver.random.nextFloat() * 4.0F);
                int k1 = 0;
                int l1 = 0;

                while (true) {
                    if (l1 < j1) {
                        label53:
                        {
                            l += worldserver.random.nextInt(6) - worldserver.random.nextInt(6);
                            i1 += worldserver.random.nextInt(6) - worldserver.random.nextInt(6);
                            blockposition_mutableblockposition.d(l, i, i1);
                            double d0 = (double) l + 0.5D;
                            double d1 = (double) i1 + 0.5D;
                            EntityHuman entityhuman = worldserver.a(d0, (double) i, d1, -1.0D, false);

                            if (entityhuman != null) {
                                double d2 = entityhuman.h(d0, (double) i, d1);

                                if (a(worldserver, ichunkaccess, blockposition_mutableblockposition, d2)) {
                                    if (biomesettingsmobs_c == null) {
                                        biomesettingsmobs_c = a(worldserver, structuremanager, chunkgenerator, enumcreaturetype, worldserver.random, (BlockPosition) blockposition_mutableblockposition);
                                        if (biomesettingsmobs_c == null) {
                                            break label53;
                                        }

                                        j1 = biomesettingsmobs_c.d + worldserver.random.nextInt(1 + biomesettingsmobs_c.e - biomesettingsmobs_c.d);
                                    }

                                    if (a(worldserver, enumcreaturetype, structuremanager, chunkgenerator, biomesettingsmobs_c, blockposition_mutableblockposition, d2) && spawnercreature_c.test(biomesettingsmobs_c.c, blockposition_mutableblockposition, ichunkaccess)) {
                                        EntityInsentient entityinsentient = a(worldserver, biomesettingsmobs_c.c);

                                        if (entityinsentient == null) {
                                            return;
                                        }

                                        entityinsentient.setPositionRotation(d0, (double) i, d1, worldserver.random.nextFloat() * 360.0F, 0.0F);
                                        if (a(worldserver, entityinsentient, d2)) {
                                            groupdataentity = entityinsentient.prepare(worldserver, worldserver.getDamageScaler(entityinsentient.getChunkCoordinates()), EnumMobSpawn.NATURAL, groupdataentity, (NBTTagCompound) null);
                                            // CraftBukkit start
                                            if (worldserver.addAllEntities(entityinsentient, SpawnReason.NATURAL)) {
                                                ++j;
                                                ++k1;
                                                spawnercreature_a.run(entityinsentient, ichunkaccess);
                                            }
                                            // CraftBukkit end
                                            if (j >= entityinsentient.getMaxSpawnGroup()) {
                                                return;
                                            }

                                            if (entityinsentient.c(k1)) {
                                                break label53;
                                            }
                                        }
                                    }
                                }
                            }

                            ++l1;
                            continue;
                        }
                    }

                    ++k;
                    break;
                }
            }

        }
    }

    private static boolean a(WorldServer worldserver, IChunkAccess ichunkaccess, BlockPosition.MutableBlockPosition blockposition_mutableblockposition, double d0) {
        if (d0 <= 576.0D) {
            return false;
        } else if (worldserver.getSpawn().a((IPosition) (new Vec3D((double) blockposition_mutableblockposition.getX() + 0.5D, (double) blockposition_mutableblockposition.getY(), (double) blockposition_mutableblockposition.getZ() + 0.5D)), 24.0D)) {
            return false;
        } else {
            ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(blockposition_mutableblockposition);

            return Objects.equals(chunkcoordintpair, ichunkaccess.getPos()) || worldserver.getChunkProvider().a(chunkcoordintpair);
        }
    }

    private static boolean a(WorldServer worldserver, EnumCreatureType enumcreaturetype, StructureManager structuremanager, ChunkGenerator chunkgenerator, BiomeSettingsMobs.c biomesettingsmobs_c, BlockPosition.MutableBlockPosition blockposition_mutableblockposition, double d0) {
        EntityTypes<?> entitytypes = biomesettingsmobs_c.c;

        if (entitytypes.e() == EnumCreatureType.MISC) {
            return false;
        } else if (!entitytypes.d() && d0 > (double) (entitytypes.e().f() * entitytypes.e().f())) {
            return false;
        } else if (entitytypes.b() && a(worldserver, structuremanager, chunkgenerator, enumcreaturetype, biomesettingsmobs_c, (BlockPosition) blockposition_mutableblockposition)) {
            EntityPositionTypes.Surface entitypositiontypes_surface = EntityPositionTypes.a(entitytypes);

            return !a(entitypositiontypes_surface, (IWorldReader) worldserver, blockposition_mutableblockposition, entitytypes) ? false : (!EntityPositionTypes.a(entitytypes, worldserver, EnumMobSpawn.NATURAL, blockposition_mutableblockposition, worldserver.random) ? false : worldserver.b(entitytypes.a((double) blockposition_mutableblockposition.getX() + 0.5D, (double) blockposition_mutableblockposition.getY(), (double) blockposition_mutableblockposition.getZ() + 0.5D)));
        } else {
            return false;
        }
    }

    @Nullable
    private static EntityInsentient a(WorldServer worldserver, EntityTypes<?> entitytypes) {
        try {
            Entity entity = entitytypes.a((World) worldserver);

            if (!(entity instanceof EntityInsentient)) {
                throw new IllegalStateException("Trying to spawn a non-mob: " + IRegistry.ENTITY_TYPE.getKey(entitytypes));
            } else {
                EntityInsentient entityinsentient = (EntityInsentient) entity;

                return entityinsentient;
            }
        } catch (Exception exception) {
            SpawnerCreature.LOGGER.warn("Failed to create mob", exception);
            return null;
        }
    }

    private static boolean a(WorldServer worldserver, EntityInsentient entityinsentient, double d0) {
        return d0 > (double) (entityinsentient.getEntityType().e().f() * entityinsentient.getEntityType().e().f()) && entityinsentient.isTypeNotPersistent(d0) ? false : entityinsentient.a((GeneratorAccess) worldserver, EnumMobSpawn.NATURAL) && entityinsentient.a((IWorldReader) worldserver);
    }

    @Nullable
    private static BiomeSettingsMobs.c a(WorldServer worldserver, StructureManager structuremanager, ChunkGenerator chunkgenerator, EnumCreatureType enumcreaturetype, Random random, BlockPosition blockposition) {
        BiomeBase biomebase = worldserver.getBiome(blockposition);

        if (enumcreaturetype == EnumCreatureType.WATER_AMBIENT && biomebase.t() == BiomeBase.Geography.RIVER && random.nextFloat() < 0.98F) {
            return null;
        } else {
            List<BiomeSettingsMobs.c> list = a(worldserver, structuremanager, chunkgenerator, enumcreaturetype, blockposition, biomebase);

            return list.isEmpty() ? null : (BiomeSettingsMobs.c) WeightedRandom.a(random, list);
        }
    }

    private static boolean a(WorldServer worldserver, StructureManager structuremanager, ChunkGenerator chunkgenerator, EnumCreatureType enumcreaturetype, BiomeSettingsMobs.c biomesettingsmobs_c, BlockPosition blockposition) {
        return a(worldserver, structuremanager, chunkgenerator, enumcreaturetype, blockposition, (BiomeBase) null).contains(biomesettingsmobs_c);
    }

    private static List<BiomeSettingsMobs.c> a(WorldServer worldserver, StructureManager structuremanager, ChunkGenerator chunkgenerator, EnumCreatureType enumcreaturetype, BlockPosition blockposition, @Nullable BiomeBase biomebase) {
        return enumcreaturetype == EnumCreatureType.MONSTER && worldserver.getType(blockposition.down()).getBlock() == Blocks.NETHER_BRICKS && structuremanager.a(blockposition, false, StructureGenerator.FORTRESS).e() ? StructureGenerator.FORTRESS.c() : chunkgenerator.getMobsFor(biomebase != null ? biomebase : worldserver.getBiome(blockposition), structuremanager, enumcreaturetype, blockposition);
    }

    private static BlockPosition getRandomPosition(World world, Chunk chunk) {
        ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
        int i = chunkcoordintpair.d() + world.random.nextInt(16);
        int j = chunkcoordintpair.e() + world.random.nextInt(16);
        int k = chunk.getHighestBlock(HeightMap.Type.WORLD_SURFACE, i, j) + 1;
        int l = world.random.nextInt(k + 1);

        return new BlockPosition(i, l, j);
    }

    public static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid, EntityTypes<?> entitytypes) {
        return iblockdata.r(iblockaccess, blockposition) ? false : (iblockdata.isPowerSource() ? false : (!fluid.isEmpty() ? false : (iblockdata.a((Tag) TagsBlock.PREVENT_MOB_SPAWNING_INSIDE) ? false : !entitytypes.a(iblockdata))));
    }

    public static boolean a(EntityPositionTypes.Surface entitypositiontypes_surface, IWorldReader iworldreader, BlockPosition blockposition, @Nullable EntityTypes<?> entitytypes) {
        if (entitypositiontypes_surface == EntityPositionTypes.Surface.NO_RESTRICTIONS) {
            return true;
        } else if (entitytypes != null && iworldreader.getWorldBorder().a(blockposition)) {
            IBlockData iblockdata = iworldreader.getType(blockposition);
            Fluid fluid = iworldreader.getFluid(blockposition);
            BlockPosition blockposition1 = blockposition.up();
            BlockPosition blockposition2 = blockposition.down();

            switch (entitypositiontypes_surface) {
                case IN_WATER:
                    return fluid.a((Tag) TagsFluid.WATER) && iworldreader.getFluid(blockposition2).a((Tag) TagsFluid.WATER) && !iworldreader.getType(blockposition1).isOccluding(iworldreader, blockposition1);
                case IN_LAVA:
                    return fluid.a((Tag) TagsFluid.LAVA);
                case ON_GROUND:
                default:
                    IBlockData iblockdata1 = iworldreader.getType(blockposition2);

                    return !iblockdata1.a((IBlockAccess) iworldreader, blockposition2, entitytypes) ? false : a((IBlockAccess) iworldreader, blockposition, iblockdata, fluid, entitytypes) && a((IBlockAccess) iworldreader, blockposition1, iworldreader.getType(blockposition1), iworldreader.getFluid(blockposition1), entitytypes);
            }
        } else {
            return false;
        }
    }

    public static void a(WorldAccess worldaccess, BiomeBase biomebase, int i, int j, Random random) {
        BiomeSettingsMobs biomesettingsmobs = biomebase.b();
        List<BiomeSettingsMobs.c> list = biomesettingsmobs.a(EnumCreatureType.CREATURE);

        if (!list.isEmpty()) {
            int k = i << 4;
            int l = j << 4;

            while (random.nextFloat() < biomesettingsmobs.a()) {
                BiomeSettingsMobs.c biomesettingsmobs_c = (BiomeSettingsMobs.c) WeightedRandom.a(random, list);
                int i1 = biomesettingsmobs_c.d + random.nextInt(1 + biomesettingsmobs_c.e - biomesettingsmobs_c.d);
                GroupDataEntity groupdataentity = null;
                int j1 = k + random.nextInt(16);
                int k1 = l + random.nextInt(16);
                int l1 = j1;
                int i2 = k1;

                for (int j2 = 0; j2 < i1; ++j2) {
                    boolean flag = false;

                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        BlockPosition blockposition = a(worldaccess, biomesettingsmobs_c.c, j1, k1);

                        if (biomesettingsmobs_c.c.b() && a(EntityPositionTypes.a(biomesettingsmobs_c.c), (IWorldReader) worldaccess, blockposition, biomesettingsmobs_c.c)) {
                            float f = biomesettingsmobs_c.c.j();
                            double d0 = MathHelper.a((double) j1, (double) k + (double) f, (double) k + 16.0D - (double) f);
                            double d1 = MathHelper.a((double) k1, (double) l + (double) f, (double) l + 16.0D - (double) f);

                            if (!worldaccess.b(biomesettingsmobs_c.c.a(d0, (double) blockposition.getY(), d1)) || !EntityPositionTypes.a(biomesettingsmobs_c.c, worldaccess, EnumMobSpawn.CHUNK_GENERATION, new BlockPosition(d0, (double) blockposition.getY(), d1), worldaccess.getRandom())) {
                                continue;
                            }

                            Entity entity;

                            try {
                                entity = biomesettingsmobs_c.c.a((World) worldaccess.getMinecraftWorld());
                            } catch (Exception exception) {
                                SpawnerCreature.LOGGER.warn("Failed to create mob", exception);
                                continue;
                            }

                            entity.setPositionRotation(d0, (double) blockposition.getY(), d1, random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof EntityInsentient) {
                                EntityInsentient entityinsentient = (EntityInsentient) entity;

                                if (entityinsentient.a((GeneratorAccess) worldaccess, EnumMobSpawn.CHUNK_GENERATION) && entityinsentient.a((IWorldReader) worldaccess)) {
                                    groupdataentity = entityinsentient.prepare(worldaccess, worldaccess.getDamageScaler(entityinsentient.getChunkCoordinates()), EnumMobSpawn.CHUNK_GENERATION, groupdataentity, (NBTTagCompound) null);
                                    worldaccess.addAllEntities(entityinsentient, SpawnReason.CHUNK_GEN); // CraftBukkit
                                    flag = true;
                                }
                            }
                        }

                        j1 += random.nextInt(5) - random.nextInt(5);

                        for (k1 += random.nextInt(5) - random.nextInt(5); j1 < k || j1 >= k + 16 || k1 < l || k1 >= l + 16; k1 = i2 + random.nextInt(5) - random.nextInt(5)) {
                            j1 = l1 + random.nextInt(5) - random.nextInt(5);
                        }
                    }
                }
            }

        }
    }

    private static BlockPosition a(IWorldReader iworldreader, EntityTypes<?> entitytypes, int i, int j) {
        int k = iworldreader.a(EntityPositionTypes.b(entitytypes), i, j);
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition(i, k, j);

        if (iworldreader.getDimensionManager().hasCeiling()) {
            do {
                blockposition_mutableblockposition.c(EnumDirection.DOWN);
            } while (!iworldreader.getType(blockposition_mutableblockposition).isAir());

            do {
                blockposition_mutableblockposition.c(EnumDirection.DOWN);
            } while (iworldreader.getType(blockposition_mutableblockposition).isAir() && blockposition_mutableblockposition.getY() > 0);
        }

        if (EntityPositionTypes.a(entitytypes) == EntityPositionTypes.Surface.ON_GROUND) {
            BlockPosition blockposition = blockposition_mutableblockposition.down();

            if (iworldreader.getType(blockposition).a((IBlockAccess) iworldreader, blockposition, PathMode.LAND)) {
                return blockposition;
            }
        }

        return blockposition_mutableblockposition.immutableCopy();
    }

    @FunctionalInterface
    public interface b {

        void query(long i, Consumer<Chunk> consumer);
    }

    @FunctionalInterface
    public interface a {

        void run(EntityInsentient entityinsentient, IChunkAccess ichunkaccess);
    }

    @FunctionalInterface
    public interface c {

        boolean test(EntityTypes<?> entitytypes, BlockPosition blockposition, IChunkAccess ichunkaccess);
    }

    public static class d {

        private final int a;
        private final Object2IntOpenHashMap<EnumCreatureType> b;
        private final SpawnerCreatureProbabilities c;
        private final Object2IntMap<EnumCreatureType> d;
        @Nullable
        private BlockPosition e;
        @Nullable
        private EntityTypes<?> f;
        private double g;

        private d(int i, Object2IntOpenHashMap<EnumCreatureType> object2intopenhashmap, SpawnerCreatureProbabilities spawnercreatureprobabilities) {
            this.a = i;
            this.b = object2intopenhashmap;
            this.c = spawnercreatureprobabilities;
            this.d = Object2IntMaps.unmodifiable(object2intopenhashmap);
        }

        private boolean a(EntityTypes<?> entitytypes, BlockPosition blockposition, IChunkAccess ichunkaccess) {
            this.e = blockposition;
            this.f = entitytypes;
            BiomeSettingsMobs.b biomesettingsmobs_b = SpawnerCreature.b(blockposition, ichunkaccess).b().a(entitytypes);

            if (biomesettingsmobs_b == null) {
                this.g = 0.0D;
                return true;
            } else {
                double d0 = biomesettingsmobs_b.b();

                this.g = d0;
                double d1 = this.c.b(blockposition, d0);

                return d1 <= biomesettingsmobs_b.a();
            }
        }

        private void a(EntityInsentient entityinsentient, IChunkAccess ichunkaccess) {
            EntityTypes<?> entitytypes = entityinsentient.getEntityType();
            BlockPosition blockposition = entityinsentient.getChunkCoordinates();
            double d0;

            if (blockposition.equals(this.e) && entitytypes == this.f) {
                d0 = this.g;
            } else {
                BiomeSettingsMobs.b biomesettingsmobs_b = SpawnerCreature.b(blockposition, ichunkaccess).b().a(entitytypes);

                if (biomesettingsmobs_b != null) {
                    d0 = biomesettingsmobs_b.b();
                } else {
                    d0 = 0.0D;
                }
            }

            this.c.a(blockposition, d0);
            this.b.addTo(entitytypes.e(), 1);
        }

        public Object2IntMap<EnumCreatureType> b() {
            return this.d;
        }

        // CraftBukkit start
        private boolean a(EnumCreatureType enumcreaturetype, int limit) {
            int i = limit * this.a / SpawnerCreature.b;
            // CraftBukkit end

            return this.b.getInt(enumcreaturetype) < i;
        }
    }
}
