package net.minecraft.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.BlockChangeDelegate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

public class WorldServer extends World implements BlockChangeDelegate {
// CraftBukkit end

    public ChunkProviderServer A;
    public boolean B = false;
    public boolean C;
    public final MinecraftServer D; // Craftbukkit
    private EntityList E = new EntityList();
    public PlayerManager manager; // Craftbukkit

    public WorldServer(MinecraftServer minecraftserver, File file1, String s, int i) {
        super(file1, s, (new Random()).nextLong(), WorldProvider.a(i));
        this.D = minecraftserver;

        // CraftBukkit start
        this.server = minecraftserver.server;
        this.world = new CraftWorld(this);
        this.manager = new PlayerManager(minecraftserver, this);
    }

    private final CraftWorld world;
    private final CraftServer server;

    public CraftWorld getWorld() {
        return world;
    }

    public CraftServer getServer() {
        return server;
    }
    // CraftBukkit end

    public void f() {
        super.f();
    }

    public void a(Entity entity, boolean flag) {
        // CraftBukkit start -- We prevent spawning in general, so this butching is not needed
        //if (!this.D.m && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
        //    entity.q();
        //}
        // CraftBukkit end

        if (entity.passenger == null || !(entity.passenger instanceof EntityHuman)) {
            super.a(entity, flag);
        }
    }

    public void b(Entity entity, boolean flag) {
        super.a(entity, flag);
    }

    protected IChunkProvider a(File file1) {
        this.A = new ChunkProviderServer(this, this.q.a(file1), this.q.c());
        return this.A;
    }

    public List d(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = new ArrayList();

        for (int k1 = 0; k1 < this.c.size(); ++k1) {
            TileEntity tileentity = (TileEntity) this.c.get(k1);

            if (tileentity.b >= i && tileentity.c >= j && tileentity.d >= k && tileentity.b < l && tileentity.c < i1 && tileentity.d < j1) {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        int l = (int) MathHelper.e((float) (i - this.spawnX));
        int i1 = (int) MathHelper.e((float) (k - this.spawnZ));

        if (l > i1) {
            i1 = l;
        }

        return i1 > this.D.spawnProtection || this.D.f.g(entityhuman.name); // CraftBukkit Configurable spawn protection start
    }

    protected void b(Entity entity) {
        super.b(entity);
        this.E.a(entity.id, entity);
    }

    protected void c(Entity entity) {
        super.c(entity);
        this.E.d(entity.id);
    }

    public Entity a(int i) {
        return (Entity) this.E.a(i);
    }

    public void a(Entity entity, byte b0) {
        Packet38EntityStatus packet38entitystatus = new Packet38EntityStatus(entity.id, b0);

        this.D.k.b(entity, packet38entitystatus);
    }

    public Explosion a(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        Explosion explosion = super.a(entity, d0, d1, d2, f, flag);

        // Craftbukkit start
        if(explosion.wasCanceled) {
            return explosion;
        }
        // Craftbukkit end

        this.D.f.a(d0, d1, d2, 64.0D, new Packet60Explosion(d0, d1, d2, f, explosion.g));
        return explosion;
    }

    public void c(int i, int j, int k, int l, int i1) {
        super.c(i, j, k, l, i1);
        this.D.f.a((double) i, (double) j, (double) k, 64.0D, new Packet54PlayNoteBlock(i, j, k, l, i1));
    }
}
