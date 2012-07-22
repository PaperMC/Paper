package net.minecraft.server;

public class SecondaryWorldServer extends WorldServer {
    // CraftBukkit start
    public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, WorldSettings worldsettings, WorldServer worldserver, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, s, i, worldsettings, env, gen);
        // CraftBukkit end
        this.worldMaps = worldserver.worldMaps;
    }
}
