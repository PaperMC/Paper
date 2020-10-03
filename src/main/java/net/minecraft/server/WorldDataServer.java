package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldDataServer implements IWorldDataServer, SaveData {

    private static final Logger LOGGER = LogManager.getLogger();
    private WorldSettings b;
    private final GeneratorSettings c;
    private final Lifecycle d;
    private int e;
    private int f;
    private int g;
    private float h;
    private long i;
    private long j;
    @Nullable
    private final DataFixer k;
    private final int l;
    private boolean m;
    @Nullable
    private NBTTagCompound n;
    private final int o;
    private int clearWeatherTime;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
    private boolean u;
    private boolean v;
    private WorldBorder.c w;
    private NBTTagCompound x;
    @Nullable
    private NBTTagCompound customBossEvents;
    private int z;
    private int A;
    @Nullable
    private UUID B;
    private final Set<String> C;
    private boolean D;
    private final CustomFunctionCallbackTimerQueue<MinecraftServer> E;

    private WorldDataServer(@Nullable DataFixer datafixer, int i, @Nullable NBTTagCompound nbttagcompound, boolean flag, int j, int k, int l, float f, long i1, long j1, int k1, int l1, int i2, boolean flag1, int j2, boolean flag2, boolean flag3, boolean flag4, WorldBorder.c worldborder_c, int k2, int l2, @Nullable UUID uuid, LinkedHashSet<String> linkedhashset, CustomFunctionCallbackTimerQueue<MinecraftServer> customfunctioncallbacktimerqueue, @Nullable NBTTagCompound nbttagcompound1, NBTTagCompound nbttagcompound2, WorldSettings worldsettings, GeneratorSettings generatorsettings, Lifecycle lifecycle) {
        this.k = datafixer;
        this.D = flag;
        this.e = j;
        this.f = k;
        this.g = l;
        this.h = f;
        this.i = i1;
        this.j = j1;
        this.o = k1;
        this.clearWeatherTime = l1;
        this.rainTime = i2;
        this.raining = flag1;
        this.thunderTime = j2;
        this.thundering = flag2;
        this.u = flag3;
        this.v = flag4;
        this.w = worldborder_c;
        this.z = k2;
        this.A = l2;
        this.B = uuid;
        this.C = linkedhashset;
        this.n = nbttagcompound;
        this.l = i;
        this.E = customfunctioncallbacktimerqueue;
        this.customBossEvents = nbttagcompound1;
        this.x = nbttagcompound2;
        this.b = worldsettings;
        this.c = generatorsettings;
        this.d = lifecycle;
    }

    public WorldDataServer(WorldSettings worldsettings, GeneratorSettings generatorsettings, Lifecycle lifecycle) {
        this((DataFixer) null, SharedConstants.getGameVersion().getWorldVersion(), (NBTTagCompound) null, false, 0, 0, 0, 0.0F, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, WorldBorder.c, 0, 0, (UUID) null, Sets.newLinkedHashSet(), new CustomFunctionCallbackTimerQueue<>(CustomFunctionCallbackTimers.a), (NBTTagCompound) null, new NBTTagCompound(), worldsettings.h(), generatorsettings, lifecycle);
    }

    public static WorldDataServer a(Dynamic<NBTBase> dynamic, DataFixer datafixer, int i, @Nullable NBTTagCompound nbttagcompound, WorldSettings worldsettings, LevelVersion levelversion, GeneratorSettings generatorsettings, Lifecycle lifecycle) {
        long j = dynamic.get("Time").asLong(0L);
        NBTTagCompound nbttagcompound1 = (NBTTagCompound) dynamic.get("DragonFight").result().map(Dynamic::getValue).orElseGet(() -> {
            return (NBTBase) dynamic.get("DimensionData").get("1").get("DragonFight").orElseEmptyMap().getValue();
        });

        return new WorldDataServer(datafixer, i, nbttagcompound, dynamic.get("WasModded").asBoolean(false), dynamic.get("SpawnX").asInt(0), dynamic.get("SpawnY").asInt(0), dynamic.get("SpawnZ").asInt(0), dynamic.get("SpawnAngle").asFloat(0.0F), j, dynamic.get("DayTime").asLong(j), levelversion.a(), dynamic.get("clearWeatherTime").asInt(0), dynamic.get("rainTime").asInt(0), dynamic.get("raining").asBoolean(false), dynamic.get("thunderTime").asInt(0), dynamic.get("thundering").asBoolean(false), dynamic.get("initialized").asBoolean(true), dynamic.get("DifficultyLocked").asBoolean(false), WorldBorder.c.a(dynamic, WorldBorder.c), dynamic.get("WanderingTraderSpawnDelay").asInt(0), dynamic.get("WanderingTraderSpawnChance").asInt(0), (UUID) dynamic.get("WanderingTraderId").read(MinecraftSerializableUUID.a).result().orElse((Object) null), (LinkedHashSet) dynamic.get("ServerBrands").asStream().flatMap((dynamic1) -> {
            return SystemUtils.a(dynamic1.asString().result());
        }).collect(Collectors.toCollection(Sets::newLinkedHashSet)), new CustomFunctionCallbackTimerQueue<>(CustomFunctionCallbackTimers.a, dynamic.get("ScheduledEvents").asStream()), (NBTTagCompound) dynamic.get("CustomBossEvents").orElseEmptyMap().getValue(), nbttagcompound1, worldsettings, generatorsettings, lifecycle);
    }

    @Override
    public NBTTagCompound a(IRegistryCustom iregistrycustom, @Nullable NBTTagCompound nbttagcompound) {
        this.J();
        if (nbttagcompound == null) {
            nbttagcompound = this.n;
        }

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        this.a(iregistrycustom, nbttagcompound1, nbttagcompound);
        return nbttagcompound1;
    }

    private void a(IRegistryCustom iregistrycustom, NBTTagCompound nbttagcompound, @Nullable NBTTagCompound nbttagcompound1) {
        NBTTagList nbttaglist = new NBTTagList();

        this.C.stream().map(NBTTagString::a).forEach(nbttaglist::add);
        nbttagcompound.set("ServerBrands", nbttaglist);
        nbttagcompound.setBoolean("WasModded", this.D);
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.setString("Name", SharedConstants.getGameVersion().getName());
        nbttagcompound2.setInt("Id", SharedConstants.getGameVersion().getWorldVersion());
        nbttagcompound2.setBoolean("Snapshot", !SharedConstants.getGameVersion().isStable());
        nbttagcompound.set("Version", nbttagcompound2);
        nbttagcompound.setInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());
        RegistryWriteOps<NBTBase> registrywriteops = RegistryWriteOps.a(DynamicOpsNBT.a, iregistrycustom);
        DataResult dataresult = GeneratorSettings.a.encodeStart(registrywriteops, this.c);
        Logger logger = WorldDataServer.LOGGER;

        logger.getClass();
        dataresult.resultOrPartial(SystemUtils.a("WorldGenSettings: ", logger::error)).ifPresent((nbtbase) -> {
            nbttagcompound.set("WorldGenSettings", nbtbase);
        });
        nbttagcompound.setInt("GameType", this.b.getGameType().getId());
        nbttagcompound.setInt("SpawnX", this.e);
        nbttagcompound.setInt("SpawnY", this.f);
        nbttagcompound.setInt("SpawnZ", this.g);
        nbttagcompound.setFloat("SpawnAngle", this.h);
        nbttagcompound.setLong("Time", this.i);
        nbttagcompound.setLong("DayTime", this.j);
        nbttagcompound.setLong("LastPlayed", SystemUtils.getTimeMillis());
        nbttagcompound.setString("LevelName", this.b.getLevelName());
        nbttagcompound.setInt("version", 19133);
        nbttagcompound.setInt("clearWeatherTime", this.clearWeatherTime);
        nbttagcompound.setInt("rainTime", this.rainTime);
        nbttagcompound.setBoolean("raining", this.raining);
        nbttagcompound.setInt("thunderTime", this.thunderTime);
        nbttagcompound.setBoolean("thundering", this.thundering);
        nbttagcompound.setBoolean("hardcore", this.b.isHardcore());
        nbttagcompound.setBoolean("allowCommands", this.b.e());
        nbttagcompound.setBoolean("initialized", this.u);
        this.w.a(nbttagcompound);
        nbttagcompound.setByte("Difficulty", (byte) this.b.getDifficulty().a());
        nbttagcompound.setBoolean("DifficultyLocked", this.v);
        nbttagcompound.set("GameRules", this.b.getGameRules().a());
        nbttagcompound.set("DragonFight", this.x);
        if (nbttagcompound1 != null) {
            nbttagcompound.set("Player", nbttagcompound1);
        }

        DataPackConfiguration.b.encodeStart(DynamicOpsNBT.a, this.b.g()).result().ifPresent((nbtbase) -> {
            nbttagcompound.set("DataPacks", nbtbase);
        });
        if (this.customBossEvents != null) {
            nbttagcompound.set("CustomBossEvents", this.customBossEvents);
        }

        nbttagcompound.set("ScheduledEvents", this.E.b());
        nbttagcompound.setInt("WanderingTraderSpawnDelay", this.z);
        nbttagcompound.setInt("WanderingTraderSpawnChance", this.A);
        if (this.B != null) {
            nbttagcompound.a("WanderingTraderId", this.B);
        }

    }

    @Override
    public int a() {
        return this.e;
    }

    @Override
    public int b() {
        return this.f;
    }

    @Override
    public int c() {
        return this.g;
    }

    @Override
    public float d() {
        return this.h;
    }

    @Override
    public long getTime() {
        return this.i;
    }

    @Override
    public long getDayTime() {
        return this.j;
    }

    private void J() {
        if (!this.m && this.n != null) {
            if (this.l < SharedConstants.getGameVersion().getWorldVersion()) {
                if (this.k == null) {
                    throw (NullPointerException) SystemUtils.c((Throwable) (new NullPointerException("Fixer Upper not set inside LevelData, and the player tag is not upgraded.")));
                }

                this.n = GameProfileSerializer.a(this.k, DataFixTypes.PLAYER, this.n, this.l);
            }

            this.m = true;
        }
    }

    @Override
    public NBTTagCompound y() {
        this.J();
        return this.n;
    }

    @Override
    public void b(int i) {
        this.e = i;
    }

    @Override
    public void c(int i) {
        this.f = i;
    }

    @Override
    public void d(int i) {
        this.g = i;
    }

    @Override
    public void a(float f) {
        this.h = f;
    }

    @Override
    public void setTime(long i) {
        this.i = i;
    }

    @Override
    public void setDayTime(long i) {
        this.j = i;
    }

    @Override
    public void setSpawn(BlockPosition blockposition, float f) {
        this.e = blockposition.getX();
        this.f = blockposition.getY();
        this.g = blockposition.getZ();
        this.h = f;
    }

    @Override
    public String getName() {
        return this.b.getLevelName();
    }

    @Override
    public int z() {
        return this.o;
    }

    @Override
    public int h() {
        return this.clearWeatherTime;
    }

    @Override
    public void a(int i) {
        this.clearWeatherTime = i;
    }

    @Override
    public boolean isThundering() {
        return this.thundering;
    }

    @Override
    public void setThundering(boolean flag) {
        this.thundering = flag;
    }

    @Override
    public int getThunderDuration() {
        return this.thunderTime;
    }

    @Override
    public void setThunderDuration(int i) {
        this.thunderTime = i;
    }

    @Override
    public boolean hasStorm() {
        return this.raining;
    }

    @Override
    public void setStorm(boolean flag) {
        this.raining = flag;
    }

    @Override
    public int getWeatherDuration() {
        return this.rainTime;
    }

    @Override
    public void setWeatherDuration(int i) {
        this.rainTime = i;
    }

    @Override
    public EnumGamemode getGameType() {
        return this.b.getGameType();
    }

    @Override
    public void setGameType(EnumGamemode enumgamemode) {
        this.b = this.b.a(enumgamemode);
    }

    @Override
    public boolean isHardcore() {
        return this.b.isHardcore();
    }

    @Override
    public boolean o() {
        return this.b.e();
    }

    @Override
    public boolean p() {
        return this.u;
    }

    @Override
    public void c(boolean flag) {
        this.u = flag;
    }

    @Override
    public GameRules q() {
        return this.b.getGameRules();
    }

    @Override
    public WorldBorder.c r() {
        return this.w;
    }

    @Override
    public void a(WorldBorder.c worldborder_c) {
        this.w = worldborder_c;
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return this.b.getDifficulty();
    }

    @Override
    public void setDifficulty(EnumDifficulty enumdifficulty) {
        this.b = this.b.a(enumdifficulty);
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.v;
    }

    @Override
    public void d(boolean flag) {
        this.v = flag;
    }

    @Override
    public CustomFunctionCallbackTimerQueue<MinecraftServer> u() {
        return this.E;
    }

    @Override
    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        IWorldDataServer.super.a(crashreportsystemdetails);
        SaveData.super.a(crashreportsystemdetails);
    }

    @Override
    public GeneratorSettings getGeneratorSettings() {
        return this.c;
    }

    @Override
    public NBTTagCompound C() {
        return this.x;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        this.x = nbttagcompound;
    }

    @Override
    public DataPackConfiguration D() {
        return this.b.g();
    }

    @Override
    public void a(DataPackConfiguration datapackconfiguration) {
        this.b = this.b.a(datapackconfiguration);
    }

    @Nullable
    @Override
    public NBTTagCompound getCustomBossEvents() {
        return this.customBossEvents;
    }

    @Override
    public void setCustomBossEvents(@Nullable NBTTagCompound nbttagcompound) {
        this.customBossEvents = nbttagcompound;
    }

    @Override
    public int v() {
        return this.z;
    }

    @Override
    public void g(int i) {
        this.z = i;
    }

    @Override
    public int w() {
        return this.A;
    }

    @Override
    public void h(int i) {
        this.A = i;
    }

    @Override
    public void a(UUID uuid) {
        this.B = uuid;
    }

    @Override
    public void a(String s, boolean flag) {
        this.C.add(s);
        this.D |= flag;
    }

    @Override
    public boolean F() {
        return this.D;
    }

    @Override
    public Set<String> G() {
        return ImmutableSet.copyOf(this.C);
    }

    @Override
    public IWorldDataServer H() {
        return this;
    }
}
