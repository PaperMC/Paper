package net.minecraft.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class StructureGenerator<C extends WorldGenFeatureConfiguration> {

    public static final BiMap<String, StructureGenerator<?>> a = HashBiMap.create();
    private static final Map<StructureGenerator<?>, WorldGenStage.Decoration> u = Maps.newHashMap();
    private static final Logger LOGGER = LogManager.getLogger();
    public static final StructureGenerator<WorldGenFeatureVillageConfiguration> PILLAGER_OUTPOST = a("Pillager_Outpost", new WorldGenFeaturePillagerOutpost(WorldGenFeatureVillageConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenMineshaftConfiguration> MINESHAFT = a("Mineshaft", new WorldGenMineshaft(WorldGenMineshaftConfiguration.a), WorldGenStage.Decoration.UNDERGROUND_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> MANSION = a("Mansion", new WorldGenWoodlandMansion(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> JUNGLE_PYRAMID = a("Jungle_Pyramid", new WorldGenFeatureJunglePyramid(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> DESERT_PYRAMID = a("Desert_Pyramid", new WorldGenFeatureDesertPyramid(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> IGLOO = a("Igloo", new WorldGenFeatureIgloo(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureRuinedPortalConfiguration> RUINED_PORTAL = a("Ruined_Portal", new WorldGenFeatureRuinedPortal(WorldGenFeatureRuinedPortalConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureShipwreckConfiguration> SHIPWRECK = a("Shipwreck", new WorldGenFeatureShipwreck(WorldGenFeatureShipwreckConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final WorldGenFeatureSwampHut SWAMP_HUT = (WorldGenFeatureSwampHut) a("Swamp_Hut", new WorldGenFeatureSwampHut(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> STRONGHOLD = a("Stronghold", new WorldGenStronghold(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.STRONGHOLDS);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> MONUMENT = a("Monument", new WorldGenMonument(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureOceanRuinConfiguration> OCEAN_RUIN = a("Ocean_Ruin", new WorldGenFeatureOceanRuin(WorldGenFeatureOceanRuinConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> FORTRESS = a("Fortress", new WorldGenNether(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.UNDERGROUND_DECORATION);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> ENDCITY = a("EndCity", new WorldGenEndCity(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureConfigurationChance> BURIED_TREASURE = a("Buried_Treasure", new WorldGenBuriedTreasure(WorldGenFeatureConfigurationChance.b), WorldGenStage.Decoration.UNDERGROUND_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureVillageConfiguration> VILLAGE = a("Village", new WorldGenVillage(WorldGenFeatureVillageConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final StructureGenerator<WorldGenFeatureEmptyConfiguration> NETHER_FOSSIL = a("Nether_Fossil", new WorldGenFeatureNetherFossil(WorldGenFeatureEmptyConfiguration.a), WorldGenStage.Decoration.UNDERGROUND_DECORATION);
    public static final StructureGenerator<WorldGenFeatureVillageConfiguration> BASTION_REMNANT = a("Bastion_Remnant", new WorldGenFeatureBastionRemnant(WorldGenFeatureVillageConfiguration.a), WorldGenStage.Decoration.SURFACE_STRUCTURES);
    public static final List<StructureGenerator<?>> t = ImmutableList.of(StructureGenerator.PILLAGER_OUTPOST, StructureGenerator.VILLAGE, StructureGenerator.NETHER_FOSSIL);
    private static final MinecraftKey w = new MinecraftKey("jigsaw");
    private static final Map<MinecraftKey, MinecraftKey> x = ImmutableMap.builder().put(new MinecraftKey("nvi"), StructureGenerator.w).put(new MinecraftKey("pcp"), StructureGenerator.w).put(new MinecraftKey("bastionremnant"), StructureGenerator.w).put(new MinecraftKey("runtime"), StructureGenerator.w).build();
    private final Codec<StructureFeature<C, StructureGenerator<C>>> y;

    private static <F extends StructureGenerator<?>> F a(String s, F f0, WorldGenStage.Decoration worldgenstage_decoration) {
        StructureGenerator.a.put(s.toLowerCase(Locale.ROOT), f0);
        StructureGenerator.u.put(f0, worldgenstage_decoration);
        return (StructureGenerator) IRegistry.a(IRegistry.STRUCTURE_FEATURE, s.toLowerCase(Locale.ROOT), (Object) f0);
    }

    public StructureGenerator(Codec<C> codec) {
        this.y = codec.fieldOf("config").xmap((worldgenfeatureconfiguration) -> {
            return new StructureFeature<>(this, worldgenfeatureconfiguration);
        }, (structurefeature) -> {
            return structurefeature.e;
        }).codec();
    }

    public WorldGenStage.Decoration f() {
        return (WorldGenStage.Decoration) StructureGenerator.u.get(this);
    }

    public static void g() {}

    @Nullable
    public static StructureStart<?> a(DefinedStructureManager definedstructuremanager, NBTTagCompound nbttagcompound, long i) {
        String s = nbttagcompound.getString("id");

        if ("INVALID".equals(s)) {
            return StructureStart.a;
        } else {
            StructureGenerator<?> structuregenerator = (StructureGenerator) IRegistry.STRUCTURE_FEATURE.get(new MinecraftKey(s.toLowerCase(Locale.ROOT)));

            if (structuregenerator == null) {
                StructureGenerator.LOGGER.error("Unknown feature id: {}", s);
                return null;
            } else {
                int j = nbttagcompound.getInt("ChunkX");
                int k = nbttagcompound.getInt("ChunkZ");
                int l = nbttagcompound.getInt("references");
                StructureBoundingBox structureboundingbox = nbttagcompound.hasKey("BB") ? new StructureBoundingBox(nbttagcompound.getIntArray("BB")) : StructureBoundingBox.a();
                NBTTagList nbttaglist = nbttagcompound.getList("Children", 10);

                try {
                    StructureStart<?> structurestart = structuregenerator.a(j, k, structureboundingbox, l, i);

                    for (int i1 = 0; i1 < nbttaglist.size(); ++i1) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i1);
                        String s1 = nbttagcompound1.getString("id").toLowerCase(Locale.ROOT);
                        MinecraftKey minecraftkey = new MinecraftKey(s1);
                        MinecraftKey minecraftkey1 = (MinecraftKey) StructureGenerator.x.getOrDefault(minecraftkey, minecraftkey);
                        WorldGenFeatureStructurePieceType worldgenfeaturestructurepiecetype = (WorldGenFeatureStructurePieceType) IRegistry.STRUCTURE_PIECE.get(minecraftkey1);

                        if (worldgenfeaturestructurepiecetype == null) {
                            StructureGenerator.LOGGER.error("Unknown structure piece id: {}", minecraftkey1);
                        } else {
                            try {
                                StructurePiece structurepiece = worldgenfeaturestructurepiecetype.load(definedstructuremanager, nbttagcompound1);

                                structurestart.d().add(structurepiece);
                            } catch (Exception exception) {
                                StructureGenerator.LOGGER.error("Exception loading structure piece with id {}", minecraftkey1, exception);
                            }
                        }
                    }

                    return structurestart;
                } catch (Exception exception1) {
                    StructureGenerator.LOGGER.error("Failed Start with id {}", s, exception1);
                    return null;
                }
            }
        }
    }

    public Codec<StructureFeature<C, StructureGenerator<C>>> h() {
        return this.y;
    }

    public StructureFeature<C, ? extends StructureGenerator<C>> a(C c0) {
        return new StructureFeature<>(this, c0);
    }

    @Nullable
    public BlockPosition getNearestGeneratedFeature(IWorldReader iworldreader, StructureManager structuremanager, BlockPosition blockposition, int i, boolean flag, long j, StructureSettingsFeature structuresettingsfeature) {
        int k = structuresettingsfeature.a();
        int l = blockposition.getX() >> 4;
        int i1 = blockposition.getZ() >> 4;
        int j1 = 0;
        SeededRandom seededrandom = new SeededRandom();

        while (j1 <= i) {
            int k1 = -j1;

            while (true) {
                if (k1 <= j1) {
                    boolean flag1 = k1 == -j1 || k1 == j1;

                    for (int l1 = -j1; l1 <= j1; ++l1) {
                        boolean flag2 = l1 == -j1 || l1 == j1;

                        if (flag1 || flag2) {
                            int i2 = l + k * k1;
                            int j2 = i1 + k * l1;
                            ChunkCoordIntPair chunkcoordintpair = this.a(structuresettingsfeature, j, seededrandom, i2, j2);
                            IChunkAccess ichunkaccess = iworldreader.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z, ChunkStatus.STRUCTURE_STARTS);
                            StructureStart<?> structurestart = structuremanager.a(SectionPosition.a(ichunkaccess.getPos(), 0), this, ichunkaccess);

                            if (structurestart != null && structurestart.e()) {
                                if (flag && structurestart.h()) {
                                    structurestart.i();
                                    return structurestart.a();
                                }

                                if (!flag) {
                                    return structurestart.a();
                                }
                            }

                            if (j1 == 0) {
                                break;
                            }
                        }
                    }

                    if (j1 != 0) {
                        ++k1;
                        continue;
                    }
                }

                ++j1;
                break;
            }
        }

        return null;
    }

    protected boolean b() {
        return true;
    }

    public final ChunkCoordIntPair a(StructureSettingsFeature structuresettingsfeature, long i, SeededRandom seededrandom, int j, int k) {
        int l = structuresettingsfeature.a();
        int i1 = structuresettingsfeature.b();
        int j1 = Math.floorDiv(j, l);
        int k1 = Math.floorDiv(k, l);

        seededrandom.a(i, j1, k1, structuresettingsfeature.c());
        int l1;
        int i2;

        if (this.b()) {
            l1 = seededrandom.nextInt(l - i1);
            i2 = seededrandom.nextInt(l - i1);
        } else {
            l1 = (seededrandom.nextInt(l - i1) + seededrandom.nextInt(l - i1)) / 2;
            i2 = (seededrandom.nextInt(l - i1) + seededrandom.nextInt(l - i1)) / 2;
        }

        return new ChunkCoordIntPair(j1 * l + l1, k1 * l + i2);
    }

    protected boolean a(ChunkGenerator chunkgenerator, WorldChunkManager worldchunkmanager, long i, SeededRandom seededrandom, int j, int k, BiomeBase biomebase, ChunkCoordIntPair chunkcoordintpair, C c0) {
        return true;
    }

    private StructureStart<C> a(int i, int j, StructureBoundingBox structureboundingbox, int k, long l) {
        return this.a().create(this, i, j, structureboundingbox, k, l);
    }

    public StructureStart<?> a(IRegistryCustom iregistrycustom, ChunkGenerator chunkgenerator, WorldChunkManager worldchunkmanager, DefinedStructureManager definedstructuremanager, long i, ChunkCoordIntPair chunkcoordintpair, BiomeBase biomebase, int j, SeededRandom seededrandom, StructureSettingsFeature structuresettingsfeature, C c0) {
        ChunkCoordIntPair chunkcoordintpair1 = this.a(structuresettingsfeature, i, seededrandom, chunkcoordintpair.x, chunkcoordintpair.z);

        if (chunkcoordintpair.x == chunkcoordintpair1.x && chunkcoordintpair.z == chunkcoordintpair1.z && this.a(chunkgenerator, worldchunkmanager, i, seededrandom, chunkcoordintpair.x, chunkcoordintpair.z, biomebase, chunkcoordintpair1, c0)) {
            StructureStart<C> structurestart = this.a(chunkcoordintpair.x, chunkcoordintpair.z, StructureBoundingBox.a(), j, i);

            structurestart.a(iregistrycustom, chunkgenerator, definedstructuremanager, chunkcoordintpair.x, chunkcoordintpair.z, biomebase, c0);
            if (structurestart.e()) {
                return structurestart;
            }
        }

        return StructureStart.a;
    }

    public abstract StructureGenerator.a<C> a();

    public String i() {
        return (String) StructureGenerator.a.inverse().get(this);
    }

    public List<BiomeSettingsMobs.c> c() {
        return ImmutableList.of();
    }

    public List<BiomeSettingsMobs.c> j() {
        return ImmutableList.of();
    }

    public interface a<C extends WorldGenFeatureConfiguration> {

        StructureStart<C> create(StructureGenerator<C> structuregenerator, int i, int j, StructureBoundingBox structureboundingbox, int k, long l);
    }
}
