package net.minecraft.server;

import org.bukkit.generator.ChunkGenerator; // CraftBukkit

public class SecondaryWorldServer extends WorldServer {
    // CraftBukkit start
    public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, long j, WorldServer worldserver, org.bukkit.World.Environment env, ChunkGenerator gen) {
        super(minecraftserver, idatamanager, s, i, j, env, gen);
        // CraftBukkit end
        this.z = worldserver.z;
    }
}
