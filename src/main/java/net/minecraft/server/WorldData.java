package net.minecraft.server;

import java.util.List;

public class WorldData {

    private long seed;
    private WorldType type;
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
    private boolean hardcore;

    public WorldData(NBTTagCompound nbttagcompound) {
        this.type = WorldType.NORMAL;
        this.hardcore = false;
        this.seed = nbttagcompound.getLong("RandomSeed");
        if (nbttagcompound.hasKey("generatorName")) {
            String s = nbttagcompound.getString("generatorName");

            this.type = WorldType.a(s);
            if (this.type == null) {
                this.type = WorldType.NORMAL;
            }
        }

        this.gameType = nbttagcompound.getInt("GameType");
        if (nbttagcompound.hasKey("MapFeatures")) {
            this.useMapFeatures = nbttagcompound.getBoolean("MapFeatures");
        } else {
            this.useMapFeatures = true;
        }

        this.spawnX = nbttagcompound.getInt("SpawnX");
        this.spawnY = nbttagcompound.getInt("SpawnY");
        this.spawnZ = nbttagcompound.getInt("SpawnZ");
        this.time = nbttagcompound.getLong("Time");
        this.lastPlayed = nbttagcompound.getLong("LastPlayed");
        this.sizeOnDisk = nbttagcompound.getLong("SizeOnDisk");
        this.name = nbttagcompound.getString("LevelName");
        this.version = nbttagcompound.getInt("version");
        this.rainTicks = nbttagcompound.getInt("rainTime");
        this.isRaining = nbttagcompound.getBoolean("raining");
        this.thunderTicks = nbttagcompound.getInt("thunderTime");
        this.isThundering = nbttagcompound.getBoolean("thundering");
        this.hardcore = nbttagcompound.getBoolean("hardcore");
        if (nbttagcompound.hasKey("Player")) {
            this.playerData = nbttagcompound.getCompound("Player");
            this.dimension = this.playerData.getInt("Dimension");
        }
    }

    public WorldData(WorldSettings worldsettings, String s) {
        this.type = WorldType.NORMAL;
        this.hardcore = false;
        this.seed = worldsettings.a();
        this.gameType = worldsettings.b();
        this.useMapFeatures = worldsettings.d();
        this.name = s;
        this.hardcore = worldsettings.c();
        this.type = worldsettings.e();
    }

    public WorldData(WorldData worlddata) {
        this.type = WorldType.NORMAL;
        this.hardcore = false;
        this.seed = worlddata.seed;
        this.type = worlddata.type;
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
        this.hardcore = worlddata.hardcore;
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
        nbttagcompound.setString("generatorName", this.type.name());
        nbttagcompound.setInt("GameType", this.gameType);
        nbttagcompound.setBoolean("MapFeatures", this.useMapFeatures);
        nbttagcompound.setInt("SpawnX", this.spawnX);
        nbttagcompound.setInt("SpawnY", this.spawnY);
        nbttagcompound.setInt("SpawnZ", this.spawnZ);
        nbttagcompound.setLong("Time", this.time);
        nbttagcompound.setLong("SizeOnDisk", this.sizeOnDisk);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", this.name);
        nbttagcompound.setInt("version", this.version);
        nbttagcompound.setInt("rainTime", this.rainTicks);
        nbttagcompound.setBoolean("raining", this.isRaining);
        nbttagcompound.setInt("thunderTime", this.thunderTicks);
        nbttagcompound.setBoolean("thundering", this.isThundering);
        nbttagcompound.setBoolean("hardcore", this.hardcore);
        if (nbttagcompound1 != null) {
            nbttagcompound.setCompound("Player", nbttagcompound1);
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

    public long getTime() {
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

    public boolean isHardcore() {
        return this.hardcore;
    }

    public WorldType getType() {
        return this.type;
    }
}
