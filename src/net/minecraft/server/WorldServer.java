package net.minecraft.server;


import java.io.File;
import java.util.*;
import org.bukkit.craftbukkit.CraftWorld;


public class WorldServer extends World {

    public ChunkProviderServer A;
    public boolean B;
    public boolean C;
    private MinecraftServer D;
    private MCHashTable E;
    private final CraftWorld world; // CraftBukkit

    public WorldServer(MinecraftServer minecraftserver, File file, String s, int i) {
        super(file, s, (new Random()).nextLong(), WorldProvider.a(i));
        B = false;
        E = new MCHashTable();
        D = minecraftserver;
        world = new CraftWorld(this); // CraftBukkit
    }

    // CraftBukkit start
    @Override
    public boolean c(int i1, int j1, int k1, int l1) {
        boolean result = super.c(i1, j1, k1, l1);
        world.updateBlock(i1, j1, k1);
        return result;
    }

    @Override
    public boolean d(int i1, int j1, int k1, int l1) {
        boolean result = super.d(i1, j1, k1, l1);
        world.updateBlock(i1, j1, k1);
        return result;
    }

    public CraftWorld getWorld() {
        return world;
    }
    // CraftBukkit stop

    public void f() {
        super.f();
    }

    public void a(Entity entity, boolean flag) {
        if (!D.m && (entity instanceof EntityAnimals)) {
            entity.l();
        }
        if (entity.j == null || !(entity.j instanceof EntityPlayer)) {
            super.a(entity, flag);
        }
    }

    public void b(Entity entity, boolean flag) {
        super.a(entity, flag);
    }

    protected IChunkProvider a(File file) {
        A = new ChunkProviderServer(this, q.a(file), q.c());
        return A;
    }

    public List d(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = new ArrayList();

        for (int k1 = 0; k1 < c.size(); k1++) {
            TileEntity tileentity = (TileEntity) c.get(k1);

            if (tileentity.b >= i && tileentity.c >= j && tileentity.d >= k && tileentity.b < l && tileentity.c < i1 && tileentity.d < j1) {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    public boolean a(EntityPlayer entityplayer, int i, int j, int k) {
        int l = (int) MathHelper.e(i - m);
        int i1 = (int) MathHelper.e(k - o);

        if (l > i1) {
            i1 = l;
        }
        return i1 > 16 || D.f.g(entityplayer.aw);
    }

    protected void b(Entity entity) {
        super.b(entity);
        E.a(entity.g, entity);
    }

    protected void c(Entity entity) {
        super.c(entity);
        E.d(entity.g);
    }

    public Entity a(int i) {
        return (Entity) E.a(i);
    }

    public void a(Entity entity, byte byte0) {
        Packet38 packet38 = new Packet38(entity.g, byte0);

        D.k.b(entity, packet38);
    }

    public Explosion a(Entity entity, double d1, double d2, double d3,
            float f1, boolean flag) {
        Explosion explosion = super.a(entity, d1, d2, d3, f1, flag);

        D.f.a(d1, d2, d3, 64D, new Packet60(d1, d2, d3, f1, explosion.g));
        return explosion;
    }
}

