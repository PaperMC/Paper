package net.minecraft.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.BlockChangeDelegate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class WorldServer extends World implements BlockChangeDelegate {
// CraftBukkit end

    public ChunkProviderServer A;
    public boolean B = false;
    public boolean C;
    private MinecraftServer D;
    private EntityList E = new EntityList();
    public PlayerManager manager; // Craftbukkit

    public WorldServer(MinecraftServer minecraftserver, File file1, String s, int i) {
        super(file1, s, (new Random()).nextLong(), WorldProvider.a(i));
        this.D = minecraftserver;

        // CraftBukkit start
        world = new CraftWorld(this);
        server = minecraftserver.server;
        manager = new PlayerManager(minecraftserver, this);
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
        if (!this.D.m && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
            entity.q();
        }

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

        return i1 > 16 || this.D.f.g(entityhuman.name);
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

        this.D.f.a(d0, d1, d2, 64.0D, new Packet60Explosion(d0, d1, d2, f, explosion.g));
        return explosion;
    }

    public void c(int i, int j, int k, int l, int i1) {
        super.c(i, j, k, l, i1);
        this.D.f.a((double) i, (double) j, (double) k, 64.0D, new Packet54PlayNoteBlock(i, j, k, l, i1));
    }

    // XXX: the following method is straight from the World.java with tweaks as noted. KEEP THEM UPDATED!
    // XXX: done because it calls private k()
    @Override
    public void h(int i, int j, int k, int l) {
        this.l(i - 1, j, k, l);
        this.l(i + 1, j, k, l);
        this.l(i, j - 1, k, l);
        this.l(i, j + 1, k, l);
        this.l(i, j, k - 1, l);
        this.l(i, j, k + 1, l);
    }

    // XXX: the following method is straight from the World.java with tweaks as noted. KEEP THEM UPDATED!
    private void l(int i, int j, int k, int l) {
        if (!this.i && !this.isStatic) {
            Block block = Block.byId[this.getTypeId(i, j, k)];

            if (block != null) {
                // CraftBukkit start
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(Event.Type.BLOCK_PHYSICS, world.getBlockAt(i, j, k), l);
                    server.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit stop

                block.b(this, i, j, k, l);
            }
        }
    }

    // XXX: the following method is straight from the World.java with tweaks as noted. KEEP THEM UPDATED!
    @Override
    public boolean a(int i, int j, int k, int l, boolean flag) {
        int i1 = this.getTypeId(j, k, l);
        Block block = Block.byId[i1];
        Block block1 = Block.byId[i];
        AxisAlignedBB axisalignedbb = block1.d(this, j, k, l);

        if (flag) {
            axisalignedbb = null;
        }

        // Craftbukkit start - We dont want to allow the user to override the bounding box check
        boolean defaultReturn = axisalignedbb != null && !this.a(axisalignedbb) ? false : (block != Block.WATER && block != Block.STATIONARY_WATER && block != Block.LAVA && block != Block.STATIONARY_LAVA && block != Block.FIRE && block != Block.SNOW ? i > 0 && block == null && block1.a(this, j, k, l) : true);

        if (!defaultReturn) {
            return false;
        }

        BlockCanBuildEvent event = new BlockCanBuildEvent(Type.BLOCK_CANBUILD, getWorld().getBlockAt(j, k, l), i1, defaultReturn);
        server.getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }
}
