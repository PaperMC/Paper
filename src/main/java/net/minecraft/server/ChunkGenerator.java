package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public abstract class ChunkGenerator {

    public static final Codec<ChunkGenerator> a;
    protected final WorldChunkManager b;
    protected final WorldChunkManager c;
    private final StructureSettings structureSettings;
    private final long e;
    private final List<ChunkCoordIntPair> f;

    public ChunkGenerator(WorldChunkManager worldchunkmanager, StructureSettings structuresettings) {
        this(worldchunkmanager, worldchunkmanager, structuresettings, 0L);
    }

    public ChunkGenerator(WorldChunkManager worldchunkmanager, WorldChunkManager worldchunkmanager1, StructureSettings structuresettings, long i) {
        this.f = Lists.newArrayList();
        this.b = worldchunkmanager;
        this.c = worldchunkmanager1;
        this.structureSettings = structuresettings;
        this.e = i;
    }

    private void g() {
        if (this.f.isEmpty()) {
            StructureSettingsStronghold structuresettingsstronghold = this.structureSettings.b();

            if (structuresettingsstronghold != null && structuresettingsstronghold.c() != 0) {
                List<BiomeBase> list = Lists.newArrayList();
                Iterator iterator = this.b.b().iterator();

                while (iterator.hasNext()) {
                    BiomeBase biomebase = (BiomeBase) iterator.next();

                    if (biomebase.e().a(StructureGenerator.STRONGHOLD)) {
                        list.add(biomebase);
                    }
                }

                int i = structuresettingsstronghold.a();
                int j = structuresettingsstronghold.c();
                int k = structuresettingsstronghold.b();
                Random random = new Random();

                random.setSeed(this.e);
                double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
                int l = 0;
                int i1 = 0;

                for (int j1 = 0; j1 < j; ++j1) {
                    double d1 = (double) (4 * i + i * i1 * 6) + (random.nextDouble() - 0.5D) * (double) i * 2.5D;
                    int k1 = (int) Math.round(Math.cos(d0) * d1);
                    int l1 = (int) Math.round(Math.sin(d0) * d1);
                    BlockPosition blockposition = this.b.a((k1 << 4) + 8, 0, (l1 << 4) + 8, 112, list::contains, random);

                    if (blockposition != null) {
                        k1 = blockposition.getX() >> 4;
                        l1 = blockposition.getZ() >> 4;
                    }

                    this.f.add(new ChunkCoordIntPair(k1, l1));
                    d0 += 6.283185307179586D / (double) k;
                    ++l;
                    if (l == k) {
                        ++i1;
                        l = 0;
                        k += 2 * k / (i1 + 1);
                        k = Math.min(k, j - j1);
                        d0 += random.nextDouble() * 3.141592653589793D * 2.0D;
                    }
                }

            }
        }
    }

    protected abstract Codec<? extends ChunkGenerator> a();

    public void createBiomes(IRegistry<BiomeBase> iregistry, IChunkAccess ichunkaccess) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();

        ((ProtoChunk) ichunkaccess).a(new BiomeStorage(iregistry, chunkcoordintpair, this.c));
    }

    public void doCarving(long i, BiomeManager biomemanager, IChunkAccess ichunkaccess, WorldGenStage.Features worldgenstage_features) {
        BiomeManager biomemanager1 = biomemanager.a(this.b);
        SeededRandom seededrandom = new SeededRandom();
        boolean flag = true;
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();
        int j = chunkcoordintpair.x;
        int k = chunkcoordintpair.z;
        BiomeSettingsGeneration biomesettingsgeneration = this.b.getBiome(chunkcoordintpair.x << 2, 0, chunkcoordintpair.z << 2).e();
        BitSet bitset = ((ProtoChunk) ichunkaccess).b(worldgenstage_features);

        for (int l = j - 8; l <= j + 8; ++l) {
            for (int i1 = k - 8; i1 <= k + 8; ++i1) {
                List<Supplier<WorldGenCarverWrapper<?>>> list = biomesettingsgeneration.a(worldgenstage_features);
                ListIterator listiterator = list.listIterator();

                while (listiterator.hasNext()) {
                    int j1 = listiterator.nextIndex();
                    WorldGenCarverWrapper<?> worldgencarverwrapper = (WorldGenCarverWrapper) ((Supplier) listiterator.next()).get();

                    seededrandom.c(i + (long) j1, l, i1);
                    if (worldgencarverwrapper.a(seededrandom, l, i1)) {
                        worldgencarverwrapper.a(ichunkaccess, biomemanager1::a, seededrandom, this.getSeaLevel(), l, i1, j, k, bitset);
                    }
                }
            }
        }

    }

    @Nullable
    public BlockPosition findNearestMapFeature(WorldServer worldserver, StructureGenerator<?> structuregenerator, BlockPosition blockposition, int i, boolean flag) {
        if (!this.b.a(structuregenerator)) {
            return null;
        } else if (structuregenerator == StructureGenerator.STRONGHOLD) {
            this.g();
            BlockPosition blockposition1 = null;
            double d0 = Double.MAX_VALUE;
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext()) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();

                blockposition_mutableblockposition.d((chunkcoordintpair.x << 4) + 8, 32, (chunkcoordintpair.z << 4) + 8);
                double d1 = blockposition_mutableblockposition.j(blockposition);

                if (blockposition1 == null) {
                    blockposition1 = new BlockPosition(blockposition_mutableblockposition);
                    d0 = d1;
                } else if (d1 < d0) {
                    blockposition1 = new BlockPosition(blockposition_mutableblockposition);
                    d0 = d1;
                }
            }

            return blockposition1;
        } else {
            StructureSettingsFeature structuresettingsfeature = this.structureSettings.a(structuregenerator);

            return structuresettingsfeature == null ? null : structuregenerator.getNearestGeneratedFeature(worldserver, worldserver.getStructureManager(), blockposition, i, flag, worldserver.getSeed(), structuresettingsfeature);
        }
    }

    public void addDecorations(RegionLimitedWorldAccess regionlimitedworldaccess, StructureManager structuremanager) {
        int i = regionlimitedworldaccess.a();
        int j = regionlimitedworldaccess.b();
        int k = i * 16;
        int l = j * 16;
        BlockPosition blockposition = new BlockPosition(k, 0, l);
        BiomeBase biomebase = this.b.getBiome((i << 2) + 2, 2, (j << 2) + 2);
        SeededRandom seededrandom = new SeededRandom();
        long i1 = seededrandom.a(regionlimitedworldaccess.getSeed(), k, l);

        try {
            biomebase.a(structuremanager, this, regionlimitedworldaccess, i1, seededrandom, blockposition);
        } catch (Exception exception) {
            CrashReport crashreport = CrashReport.a(exception, "Biome decoration");

            crashreport.a("Generation").a("CenterX", (Object) i).a("CenterZ", (Object) j).a("Seed", (Object) i1).a("Biome", (Object) biomebase);
            throw new ReportedException(crashreport);
        }
    }

    public abstract void buildBase(RegionLimitedWorldAccess regionlimitedworldaccess, IChunkAccess ichunkaccess);

    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {}

    public StructureSettings getSettings() {
        return this.structureSettings;
    }

    public int getSpawnHeight() {
        return 64;
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.c;
    }

    public int getGenerationDepth() {
        return 256;
    }

    public List<BiomeSettingsMobs.c> getMobsFor(BiomeBase biomebase, StructureManager structuremanager, EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        return biomebase.b().a(enumcreaturetype);
    }

    public void createStructures(IRegistryCustom iregistrycustom, StructureManager structuremanager, IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();
        BiomeBase biomebase = this.b.getBiome((chunkcoordintpair.x << 2) + 2, 0, (chunkcoordintpair.z << 2) + 2);

        this.a(StructureFeatures.k, iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i, chunkcoordintpair, biomebase);
        Iterator iterator = biomebase.e().a().iterator();

        while (iterator.hasNext()) {
            Supplier<StructureFeature<?, ?>> supplier = (Supplier) iterator.next();

            // CraftBukkit start
            StructureFeature<?, ?> structurefeature = (StructureFeature) supplier.get();
            if (structurefeature.c == StructureGenerator.STRONGHOLD) {
                synchronized (structurefeature) {
                    this.a(structurefeature, iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i, chunkcoordintpair, biomebase);
                }
            } else {
                this.a(structurefeature, iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i, chunkcoordintpair, biomebase);
            }
            // CraftBukkit end
        }

    }

    private void a(StructureFeature<?, ?> structurefeature, IRegistryCustom iregistrycustom, StructureManager structuremanager, IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i, ChunkCoordIntPair chunkcoordintpair, BiomeBase biomebase) {
        StructureStart<?> structurestart = structuremanager.a(SectionPosition.a(ichunkaccess.getPos(), 0), structurefeature.d, ichunkaccess);
        int j = structurestart != null ? structurestart.j() : 0;
        StructureSettingsFeature structuresettingsfeature = this.structureSettings.a(structurefeature.d);

        if (structuresettingsfeature != null) {
            StructureStart<?> structurestart1 = structurefeature.a(iregistrycustom, this, this.b, definedstructuremanager, i, chunkcoordintpair, biomebase, j, structuresettingsfeature);

            structuremanager.a(SectionPosition.a(ichunkaccess.getPos(), 0), structurefeature.d, structurestart1, ichunkaccess);
        }

    }

    public void storeStructures(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, IChunkAccess ichunkaccess) {
        boolean flag = true;
        int i = ichunkaccess.getPos().x;
        int j = ichunkaccess.getPos().z;
        int k = i << 4;
        int l = j << 4;
        SectionPosition sectionposition = SectionPosition.a(ichunkaccess.getPos(), 0);

        for (int i1 = i - 8; i1 <= i + 8; ++i1) {
            for (int j1 = j - 8; j1 <= j + 8; ++j1) {
                long k1 = ChunkCoordIntPair.pair(i1, j1);
                Iterator iterator = generatoraccessseed.getChunkAt(i1, j1).h().values().iterator();

                while (iterator.hasNext()) {
                    StructureStart structurestart = (StructureStart) iterator.next();

                    try {
                        if (structurestart != StructureStart.a && structurestart.c().a(k, l, k + 15, l + 15)) {
                            structuremanager.a(sectionposition, structurestart.l(), k1, ichunkaccess);
                            PacketDebug.a(generatoraccessseed, structurestart);
                        }
                    } catch (Exception exception) {
                        CrashReport crashreport = CrashReport.a(exception, "Generating structure reference");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Structure");

                        crashreportsystemdetails.a("Id", () -> {
                            return IRegistry.STRUCTURE_FEATURE.getKey(structurestart.l()).toString();
                        });
                        crashreportsystemdetails.a("Name", () -> {
                            return structurestart.l().i();
                        });
                        crashreportsystemdetails.a("Class", () -> {
                            return structurestart.l().getClass().getCanonicalName();
                        });
                        throw new ReportedException(crashreport);
                    }
                }
            }
        }

    }

    public abstract void buildNoise(GeneratorAccess generatoraccess, StructureManager structuremanager, IChunkAccess ichunkaccess);

    public int getSeaLevel() {
        return 63;
    }

    public abstract int getBaseHeight(int i, int j, HeightMap.Type heightmap_type);

    public abstract IBlockAccess a(int i, int j);

    public int b(int i, int j, HeightMap.Type heightmap_type) {
        return this.getBaseHeight(i, j, heightmap_type);
    }

    public int c(int i, int j, HeightMap.Type heightmap_type) {
        return this.getBaseHeight(i, j, heightmap_type) - 1;
    }

    public boolean a(ChunkCoordIntPair chunkcoordintpair) {
        this.g();
        return this.f.contains(chunkcoordintpair);
    }

    static {
        // CraftBukkit start - decompile errors
        IRegistry.a(IRegistry.CHUNK_GENERATOR, "noise", ChunkGeneratorAbstract.d);
        IRegistry.a(IRegistry.CHUNK_GENERATOR, "flat", ChunkProviderFlat.d);
        IRegistry.a(IRegistry.CHUNK_GENERATOR, "debug", ChunkProviderDebug.d);
        // CraftBukkit end
        a = IRegistry.CHUNK_GENERATOR.dispatchStable(ChunkGenerator::a, Function.identity());
    }
}
