package net.minecraft.server;

public class SecondaryWorldServer extends WorldServer {
    // CraftBukkit start
    public SecondaryWorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, long j, WorldServer worldserver, org.bukkit.World.Environment env) {
        super(minecraftserver, idatamanager, s, i, j, env);
        // CraftBukkit end
        this.z = worldserver.z;
    }
}
