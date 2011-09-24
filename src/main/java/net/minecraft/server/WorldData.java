package net.minecraft.server;

import java.util.List;

public class WorldData {

    private long seed;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long time;
    private long lastPlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerData;
    private int dimension;
    public String name; // CraftBukkit - private -> public
    private int version;
    private boolean isRaining;
    private int rainTicks;
    private boolean isThundering;
    private int thunderTicks;
    private int gameType;
    private boolean useMapFeatures;

    public WorldData(NBTTagCompound nbttagcompound) {
        this.seed = nbttagcompound.getLong("RandomSeed");
        this.gameType = nbttagcompound.e("GameType");
        if (nbttagcompound.hasKey("MapFeatures")) {
            this.useMapFeatures = nbttagcompound.m("MapFeatures");
        } else {
            this.useMapFeatures = true;
        }

        this.spawnX = nbttagcompound.e("SpawnX");
        this.spawnY = nbttagcompound.e("SpawnY");
        this.spawnZ = nbttagcompound.e("SpawnZ");
        this.time = nbttagcompound.getLong("Time");
        this.lastPlayed = nbttagcompound.getLong("LastPlayed");
        this.sizeOnDisk = nbttagcompound.getLong("SizeOnDisk");
        this.name = nbttagcompound.getString("LevelName");
        this.version = nbttagcompound.e("version");
        this.rainTicks = nbttagcompound.e("rainTime");
        this.isRaining = nbttagcompound.m("raining");
        this.thunderTicks = nbttagcompound.e("thunderTime");
        this.isThundering = nbttagcompound.m("thundering");
        if (nbttagcompound.hasKey("Player")) {
            this.playerData = nbttagcompound.k("Player");
            this.dimension = this.playerData.e("Dimension");
        }
    }

    public WorldData(WorldSettings worldsettings, String s) {
        this.seed = worldsettings.a();
        this.gameType = worldsettings.b();
        this.useMapFeatures = worldsettings.c();
        this.name = s;
    }

    public WorldData(WorldData worlddata) {
        this.seed = worlddata.seed;
        this.gameType = worlddata.gameType;
        this.useMapFeatures = worlddata.useMapFeatures;
        this.spawnX = worlddata.spawnX;
        this.spawnY = worlddata.spawnY;
        this.spawnZ = worlddata.spawnZ;
        this.time = worlddata.time;
        this.lastPlayed = worlddata.lastPlayed;
        this.sizeOnDisk = worlddata.sizeOnDisk;
        this.playerData = worlddata.playerData;
        this.dimension = worlddata.dimension;
        this.name = worlddata.name;
        this.version = worlddata.version;
        this.rainTicks = worlddata.rainTicks;
        this.isRaining = worlddata.isRaining;
        this.thunderTicks = worlddata.thunderTicks;
        this.isThundering = worlddata.isThundering;
    }

    public NBTTagCompound a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.a(nbttagcompound, this.playerData);
        return nbttagcompound;
    }

    public NBTTagCompound a(List list) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        EntityHuman entityhuman = null;
        NBTTagCompound nbttagcompound1 = null;

        if (list.size() > 0) {
            entityhuman = (EntityHuman) list.get(0);
        }

        if (entityhuman != null) {
            nbttagcompound1 = new NBTTagCompound();
            entityhuman.d(nbttagcompound1);
        }

        this.a(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        nbttagcompound.setLong("RandomSeed", this.seed);
        nbttagcompound.a("GameType", this.gameType);
        nbttagcompound.a("MapFeatures", this.useMapFeatures);
        nbttagcompound.a("SpawnX", this.spawnX);
        nbttagcompound.a("SpawnY", this.spawnY);
        nbttagcompound.a("SpawnZ", this.spawnZ);
        nbttagcompound.setLong("Time", this.time);
        nbttagcompound.setLong("SizeOnDisk", this.sizeOnDisk);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", this.name);
        nbttagcompound.a("version", this.version);
        nbttagcompound.a("rainTime", this.rainTicks);
        nbttagcompound.a("raining", this.isRaining);
        nbttagcompound.a("thunderTime", this.thunderTicks);
        nbttagcompound.a("thundering", this.isThundering);
        if (nbttagcompound1 != null) {
            nbttagcompound.a("Player", nbttagcompound1);
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public int c() {
        return this.spawnX;
    }

    public int d() {
        return this.spawnY;
    }

    public int e() {
        return this.spawnZ;
    }

    public long f() {
        return this.time;
    }

    public long g() {
        return this.sizeOnDisk;
    }

    public int h() {
        return this.dimension;
    }

    public void a(long i) {
        this.time = i;
    }

    public void b(long i) {
        this.sizeOnDisk = i;
    }

    public void setSpawn(int i, int j, int k) {
        this.spawnX = i;
        this.spawnY = j;
        this.spawnZ = k;
    }

    public void a(String s) {
        this.name = s;
    }

    public int i() {
        return this.version;
    }

    public void a(int i) {
        this.version = i;
    }

    public boolean isThundering() {
        return this.isThundering;
    }

    public void setThundering(boolean flag) {
        this.isThundering = flag;
    }

    public int getThunderDuration() {
        return this.thunderTicks;
    }

    public void setThunderDuration(int i) {
        this.thunderTicks = i;
    }

    public boolean hasStorm() {
        return this.isRaining;
    }

    public void setStorm(boolean flag) {
        this.isRaining = flag;
    }

    public int getWeatherDuration() {
        return this.rainTicks;
    }

    public void setWeatherDuration(int i) {
        this.rainTicks = i;
    }

    public int getGameType() {
        return this.gameType;
    }

    public boolean o() {
        return this.useMapFeatures;
    }

    public void setGameType(int i) {
        this.gameType = i;
    }
}
