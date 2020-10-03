package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.annotation.Nullable;

public class ChunkStatus {

    private static final EnumSet<HeightMap.Type> n = EnumSet.of(HeightMap.Type.OCEAN_FLOOR_WG, HeightMap.Type.WORLD_SURFACE_WG);
    private static final EnumSet<HeightMap.Type> o = EnumSet.of(HeightMap.Type.OCEAN_FLOOR, HeightMap.Type.WORLD_SURFACE, HeightMap.Type.MOTION_BLOCKING, HeightMap.Type.MOTION_BLOCKING_NO_LEAVES);
    private static final ChunkStatus.c p = (chunkstatus, worldserver, definedstructuremanager, lightenginethreaded, function, ichunkaccess) -> {
        if (ichunkaccess instanceof ProtoChunk && !ichunkaccess.getChunkStatus().b(chunkstatus)) {
            ((ProtoChunk) ichunkaccess).a(chunkstatus);
        }

        return CompletableFuture.completedFuture(Either.left(ichunkaccess));
    };
    public static final ChunkStatus EMPTY = a("empty", (ChunkStatus) null, -1, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
    });
    public static final ChunkStatus STRUCTURE_STARTS = a("structure_starts", ChunkStatus.EMPTY, 0, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (chunkstatus, worldserver, chunkgenerator, definedstructuremanager, lightenginethreaded, function, list, ichunkaccess) -> {
        if (!ichunkaccess.getChunkStatus().b(chunkstatus)) {
            if (worldserver.worldDataServer.getGeneratorSettings().shouldGenerateMapFeatures()) { // CraftBukkit
                chunkgenerator.createStructures(worldserver.r(), worldserver.getStructureManager(), ichunkaccess, definedstructuremanager, worldserver.getSeed());
            }

            if (ichunkaccess instanceof ProtoChunk) {
                ((ProtoChunk) ichunkaccess).a(chunkstatus);
            }
        }

        return CompletableFuture.completedFuture(Either.left(ichunkaccess));
    });
    public static final ChunkStatus STRUCTURE_REFERENCES = a("structure_references", ChunkStatus.STRUCTURE_STARTS, 8, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        RegionLimitedWorldAccess regionlimitedworldaccess = new RegionLimitedWorldAccess(worldserver, list);

        chunkgenerator.storeStructures(regionlimitedworldaccess, worldserver.getStructureManager().a(regionlimitedworldaccess), ichunkaccess);
    });
    public static final ChunkStatus BIOMES = a("biomes", ChunkStatus.STRUCTURE_REFERENCES, 0, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        chunkgenerator.createBiomes(worldserver.r().b(IRegistry.ay), ichunkaccess);
    });
    public static final ChunkStatus NOISE = a("noise", ChunkStatus.BIOMES, 8, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        RegionLimitedWorldAccess regionlimitedworldaccess = new RegionLimitedWorldAccess(worldserver, list);

        chunkgenerator.buildNoise(regionlimitedworldaccess, worldserver.getStructureManager().a(regionlimitedworldaccess), ichunkaccess);
    });
    public static final ChunkStatus SURFACE = a("surface", ChunkStatus.NOISE, 0, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        chunkgenerator.buildBase(new RegionLimitedWorldAccess(worldserver, list), ichunkaccess);
    });
    public static final ChunkStatus CARVERS = a("carvers", ChunkStatus.SURFACE, 0, ChunkStatus.n, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        chunkgenerator.doCarving(worldserver.getSeed(), worldserver.d(), ichunkaccess, WorldGenStage.Features.AIR);
    });
    public static final ChunkStatus LIQUID_CARVERS = a("liquid_carvers", ChunkStatus.CARVERS, 0, ChunkStatus.o, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        chunkgenerator.doCarving(worldserver.getSeed(), worldserver.d(), ichunkaccess, WorldGenStage.Features.LIQUID);
    });
    public static final ChunkStatus FEATURES = a("features", ChunkStatus.LIQUID_CARVERS, 8, ChunkStatus.o, ChunkStatus.Type.PROTOCHUNK, (chunkstatus, worldserver, chunkgenerator, definedstructuremanager, lightenginethreaded, function, list, ichunkaccess) -> {
        ProtoChunk protochunk = (ProtoChunk) ichunkaccess;

        protochunk.a((LightEngine) lightenginethreaded);
        if (!ichunkaccess.getChunkStatus().b(chunkstatus)) {
            HeightMap.a(ichunkaccess, EnumSet.of(HeightMap.Type.MOTION_BLOCKING, HeightMap.Type.MOTION_BLOCKING_NO_LEAVES, HeightMap.Type.OCEAN_FLOOR, HeightMap.Type.WORLD_SURFACE));
            RegionLimitedWorldAccess regionlimitedworldaccess = new RegionLimitedWorldAccess(worldserver, list);

            chunkgenerator.addDecorations(regionlimitedworldaccess, worldserver.getStructureManager().a(regionlimitedworldaccess));
            protochunk.a(chunkstatus);
        }

        return CompletableFuture.completedFuture(Either.left(ichunkaccess));
    });
    public static final ChunkStatus LIGHT = a("light", ChunkStatus.FEATURES, 1, ChunkStatus.o, ChunkStatus.Type.PROTOCHUNK, (chunkstatus, worldserver, chunkgenerator, definedstructuremanager, lightenginethreaded, function, list, ichunkaccess) -> {
        return a(chunkstatus, lightenginethreaded, ichunkaccess);
    }, (chunkstatus, worldserver, definedstructuremanager, lightenginethreaded, function, ichunkaccess) -> {
        return a(chunkstatus, lightenginethreaded, ichunkaccess);
    });
    public static final ChunkStatus SPAWN = a("spawn", ChunkStatus.LIGHT, 0, ChunkStatus.o, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
        chunkgenerator.addMobs(new RegionLimitedWorldAccess(worldserver, list));
    });
    public static final ChunkStatus HEIGHTMAPS = a("heightmaps", ChunkStatus.SPAWN, 0, ChunkStatus.o, ChunkStatus.Type.PROTOCHUNK, (worldserver, chunkgenerator, list, ichunkaccess) -> {
    });
    public static final ChunkStatus FULL = a("full", ChunkStatus.HEIGHTMAPS, 0, ChunkStatus.o, ChunkStatus.Type.LEVELCHUNK, (chunkstatus, worldserver, chunkgenerator, definedstructuremanager, lightenginethreaded, function, list, ichunkaccess) -> {
        return (CompletableFuture) function.apply(ichunkaccess);
    }, (chunkstatus, worldserver, definedstructuremanager, lightenginethreaded, function, ichunkaccess) -> {
        return (CompletableFuture) function.apply(ichunkaccess);
    });
    private static final List<ChunkStatus> q = ImmutableList.of(ChunkStatus.FULL, ChunkStatus.FEATURES, ChunkStatus.LIQUID_CARVERS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS, ChunkStatus.STRUCTURE_STARTS);
    private static final IntList r = (IntList) SystemUtils.a((new IntArrayList(a().size())), (java.util.function.Consumer<IntArrayList>) (intarraylist) -> { // CraftBukkit - decompile error
        int i = 0;

        for (int j = a().size() - 1; j >= 0; --j) {
            while (i + 1 < ChunkStatus.q.size() && j <= ((ChunkStatus) ChunkStatus.q.get(i + 1)).c()) {
                ++i;
            }

            intarraylist.add(0, i);
        }

    });
    private final String s;
    private final int t;
    private final ChunkStatus u;
    private final ChunkStatus.b v;
    private final ChunkStatus.c w;
    private final int x;
    private final ChunkStatus.Type y;
    private final EnumSet<HeightMap.Type> z;

    private static CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> a(ChunkStatus chunkstatus, LightEngineThreaded lightenginethreaded, IChunkAccess ichunkaccess) {
        boolean flag = a(chunkstatus, ichunkaccess);

        if (!ichunkaccess.getChunkStatus().b(chunkstatus)) {
            ((ProtoChunk) ichunkaccess).a(chunkstatus);
        }

        return lightenginethreaded.a(ichunkaccess, flag).thenApply(Either::left);
    }

    private static ChunkStatus a(String s, @Nullable ChunkStatus chunkstatus, int i, EnumSet<HeightMap.Type> enumset, ChunkStatus.Type chunkstatus_type, ChunkStatus.d chunkstatus_d) {
        return a(s, chunkstatus, i, enumset, chunkstatus_type, (ChunkStatus.b) chunkstatus_d);
    }

    private static ChunkStatus a(String s, @Nullable ChunkStatus chunkstatus, int i, EnumSet<HeightMap.Type> enumset, ChunkStatus.Type chunkstatus_type, ChunkStatus.b chunkstatus_b) {
        return a(s, chunkstatus, i, enumset, chunkstatus_type, chunkstatus_b, ChunkStatus.p);
    }

    private static ChunkStatus a(String s, @Nullable ChunkStatus chunkstatus, int i, EnumSet<HeightMap.Type> enumset, ChunkStatus.Type chunkstatus_type, ChunkStatus.b chunkstatus_b, ChunkStatus.c chunkstatus_c) {
        return (ChunkStatus) IRegistry.a((IRegistry) IRegistry.CHUNK_STATUS, s, (Object) (new ChunkStatus(s, chunkstatus, i, enumset, chunkstatus_type, chunkstatus_b, chunkstatus_c)));
    }

    public static List<ChunkStatus> a() {
        List<ChunkStatus> list = Lists.newArrayList();

        ChunkStatus chunkstatus;

        for (chunkstatus = ChunkStatus.FULL; chunkstatus.e() != chunkstatus; chunkstatus = chunkstatus.e()) {
            list.add(chunkstatus);
        }

        list.add(chunkstatus);
        Collections.reverse(list);
        return list;
    }

    private static boolean a(ChunkStatus chunkstatus, IChunkAccess ichunkaccess) {
        return ichunkaccess.getChunkStatus().b(chunkstatus) && ichunkaccess.r();
    }

    public static ChunkStatus a(int i) {
        return i >= ChunkStatus.q.size() ? ChunkStatus.EMPTY : (i < 0 ? ChunkStatus.FULL : (ChunkStatus) ChunkStatus.q.get(i));
    }

    public static int b() {
        return ChunkStatus.q.size();
    }

    public static int a(ChunkStatus chunkstatus) {
        return ChunkStatus.r.getInt(chunkstatus.c());
    }

    ChunkStatus(String s, @Nullable ChunkStatus chunkstatus, int i, EnumSet<HeightMap.Type> enumset, ChunkStatus.Type chunkstatus_type, ChunkStatus.b chunkstatus_b, ChunkStatus.c chunkstatus_c) {
        this.s = s;
        this.u = chunkstatus == null ? this : chunkstatus;
        this.v = chunkstatus_b;
        this.w = chunkstatus_c;
        this.x = i;
        this.y = chunkstatus_type;
        this.z = enumset;
        this.t = chunkstatus == null ? 0 : chunkstatus.c() + 1;
    }

    public int c() {
        return this.t;
    }

    public String d() {
        return this.s;
    }

    public ChunkStatus e() {
        return this.u;
    }

    public CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> a(WorldServer worldserver, ChunkGenerator chunkgenerator, DefinedStructureManager definedstructuremanager, LightEngineThreaded lightenginethreaded, Function<IChunkAccess, CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> function, List<IChunkAccess> list) {
        return this.v.doWork(this, worldserver, chunkgenerator, definedstructuremanager, lightenginethreaded, function, list, (IChunkAccess) list.get(list.size() / 2));
    }

    public CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> a(WorldServer worldserver, DefinedStructureManager definedstructuremanager, LightEngineThreaded lightenginethreaded, Function<IChunkAccess, CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> function, IChunkAccess ichunkaccess) {
        return this.w.doWork(this, worldserver, definedstructuremanager, lightenginethreaded, function, ichunkaccess);
    }

    public int f() {
        return this.x;
    }

    public ChunkStatus.Type getType() {
        return this.y;
    }

    public static ChunkStatus a(String s) {
        return (ChunkStatus) IRegistry.CHUNK_STATUS.get(MinecraftKey.a(s));
    }

    public EnumSet<HeightMap.Type> h() {
        return this.z;
    }

    public boolean b(ChunkStatus chunkstatus) {
        return this.c() >= chunkstatus.c();
    }

    public String toString() {
        return IRegistry.CHUNK_STATUS.getKey(this).toString();
    }

    public static enum Type {

        PROTOCHUNK, LEVELCHUNK;

        private Type() {}
    }

    interface d extends ChunkStatus.b {

        @Override
        default CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> doWork(ChunkStatus chunkstatus, WorldServer worldserver, ChunkGenerator chunkgenerator, DefinedStructureManager definedstructuremanager, LightEngineThreaded lightenginethreaded, Function<IChunkAccess, CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> function, List<IChunkAccess> list, IChunkAccess ichunkaccess) {
            if (!ichunkaccess.getChunkStatus().b(chunkstatus)) {
                this.doWork(worldserver, chunkgenerator, list, ichunkaccess);
                if (ichunkaccess instanceof ProtoChunk) {
                    ((ProtoChunk) ichunkaccess).a(chunkstatus);
                }
            }

            return CompletableFuture.completedFuture(Either.left(ichunkaccess));
        }

        void doWork(WorldServer worldserver, ChunkGenerator chunkgenerator, List<IChunkAccess> list, IChunkAccess ichunkaccess);
    }

    interface c {

        CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> doWork(ChunkStatus chunkstatus, WorldServer worldserver, DefinedStructureManager definedstructuremanager, LightEngineThreaded lightenginethreaded, Function<IChunkAccess, CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> function, IChunkAccess ichunkaccess);
    }

    interface b {

        CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> doWork(ChunkStatus chunkstatus, WorldServer worldserver, ChunkGenerator chunkgenerator, DefinedStructureManager definedstructuremanager, LightEngineThreaded lightenginethreaded, Function<IChunkAccess, CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> function, List<IChunkAccess> list, IChunkAccess ichunkaccess);
    }
}
