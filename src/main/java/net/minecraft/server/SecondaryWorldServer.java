package net.minecraft.server;

public class SecondaryWorldServer extends WorldServer {
    // CraftBukkit start - Changed signature
    public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, WorldSettings worldsettings, WorldServer worldserver, MethodProfiler methodprofiler, IConsoleLogManager iconsolelogmanager, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, s, i, worldsettings, methodprofiler, iconsolelogmanager, env, gen);
        // CraftBukkit end
        this.worldMaps = worldserver.worldMaps;
        this.scoreboard = worldserver.getScoreboard();
        // this.worldData = new SecondaryWorldData(worldserver.getWorldData()); // CraftBukkit - use unique worlddata
    }

    // protected void a() {} // CraftBukkit - save world data!
}
