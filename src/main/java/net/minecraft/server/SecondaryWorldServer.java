package net.minecraft.server;

public class SecondaryWorldServer extends WorldServer {
    // CraftBukkit start - Changed signature
    public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, WorldSettings worldsettings, WorldServer worldserver, MethodProfiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, s, i, worldsettings, methodprofiler, env, gen);
        // CraftBukkit end
        this.worldMaps = worldserver.worldMaps;
        // this.worldData = new SecondaryWorldData(worldserver.getWorldData()); // CraftBukkit - use unique worlddata
    }

    protected void a() {}
}
