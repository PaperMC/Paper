package net.minecraft.server;

public class WorldManager implements IWorldAccess {

    private MinecraftServer server;
    public WorldServer world; // CraftBukkit

    // CraftBukkit - changed signature
    public WorldManager(MinecraftServer minecraftserver, WorldServer world) {
        this.server = minecraftserver;
        this.world = world; // CraftBukkit
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public void a(Entity entity) {
        this.server.tracker.a(entity);
    }

    public void b(Entity entity) {
        this.server.tracker.untrackEntity(entity);
    }

    public void a(String s, double d0, double d1, double d2, float f, float f1) {}

    public void a(int i, int j, int k, int l, int i1, int j1) {}

    public void a() {}

    public void a(int i, int j, int k) {
        // CraftBukkit - add world argument
        this.server.serverConfigurationManager.flagDirty(i, j, k, world);
    }

    public void a(String s, int i, int j, int k) {}

    public void a(int i, int j, int k, TileEntity tileentity) {
        this.server.serverConfigurationManager.a(i, j, k, tileentity);
    }
}
