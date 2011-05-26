package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import org.bukkit.BlockChangeDelegate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.weather.LightningStrikeEvent;

public class WorldServer extends World implements BlockChangeDelegate {
// CraftBukkit end

    public ChunkProviderServer chunkProviderServer;
    public boolean weirdIsOpCache = false;
    public boolean E;
    public final MinecraftServer server; // CraftBukkit - private -> public final
    private EntityList G = new EntityList();

    public WorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, String s, int i, long j) {
        super(idatamanager, s, j, WorldProvider.a(i));
        this.server = minecraftserver;

        // CraftBukkit start
        this.dimension = i;
        this.cserver = minecraftserver.server;
        this.world = new CraftWorld(this);
        this.pvpMode = minecraftserver.pvpMode;
        this.manager = new PlayerManager(minecraftserver, dimension, minecraftserver.propertyManager.getInt("view-distance", 10));
    }

    public final int dimension;
    private final CraftWorld world;
    private final CraftServer cserver;
    public EntityTracker tracker;
    public PlayerManager manager;

    public CraftWorld getWorld() {
        return world;
    }

    public CraftServer getServer() {
        return cserver;
    }
    // CraftBukkit end

    public void entityJoinedWorld(Entity entity, boolean flag) {
        /* CraftBukkit start - We prevent spawning in general, so this butchering is not needed
        if (!this.server.spawnAnimals && (entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal)) {
            entity.die();
        }
        // CraftBukkit end */

        if (entity.passenger == null || !(entity.passenger instanceof EntityHuman)) {
            super.entityJoinedWorld(entity, flag);
        }
    }

    public void vehicleEnteredWorld(Entity entity, boolean flag) {
        super.entityJoinedWorld(entity, flag);
    }

    protected IChunkProvider b() {
        IChunkLoader ichunkloader = this.w.a(this.worldProvider);

        this.chunkProviderServer = new ChunkProviderServer(this, ichunkloader, this.worldProvider.b());
        return this.chunkProviderServer;
    }

    public List getTileEntities(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = new ArrayList();

        for (int k1 = 0; k1 < this.c.size(); ++k1) {
            TileEntity tileentity = (TileEntity) this.c.get(k1);

            if (tileentity.e >= i && tileentity.f >= j && tileentity.g >= k && tileentity.e < l && tileentity.f < i1 && tileentity.g < j1) {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    public boolean a(EntityHuman entityhuman, int i, int j, int k) {
        int l = (int) MathHelper.abs((float) (i - this.worldData.c()));
        int i1 = (int) MathHelper.abs((float) (k - this.worldData.e()));

        if (l > i1) {
            i1 = l;
        }

        // CraftBukkit - Configurable spawn protection
        return i1 > this.server.spawnProtection || this.server.serverConfigurationManager.isOp(entityhuman.name);
    }

    protected void c(Entity entity) {
        super.c(entity);
        this.G.a(entity.id, entity);
    }

    protected void d(Entity entity) {
        super.d(entity);
        this.G.d(entity.id);
    }

    public Entity getEntity(int i) {
        return (Entity) this.G.a(i);
    }

    public boolean a(Entity entity) {
        if (super.a(entity)) {
            // CraftBukkit start
            LightningStrikeEvent lightning = new LightningStrikeEvent((org.bukkit.World) world, (org.bukkit.entity.LightningStrike) entity.getBukkitEntity());
            this.cserver.getPluginManager().callEvent(lightning);

            if (lightning.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            this.server.serverConfigurationManager.a(entity.locX, entity.locY, entity.locZ, 512.0D, this.worldProvider.dimension, new Packet71Weather(entity));
            return true;
        } else {
            return false;
        }
    }

    public void a(Entity entity, byte b0) {
        Packet38EntityStatus packet38entitystatus = new Packet38EntityStatus(entity.id, b0);

        this.server.b(this.worldProvider.dimension).b(entity, packet38entitystatus);
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        // CraftBukkit start
        Explosion explosion = super.createExplosion(entity, d0, d1, d2, f, flag);

        if (explosion.wasCanceled) {
            return explosion;
        }
        // CraftBukkit end

        this.server.serverConfigurationManager.a(d0, d1, d2, 64.0D, this.worldProvider.dimension, new Packet60Explosion(d0, d1, d2, f, explosion.g));
        return explosion;
    }

    public void d(int i, int j, int k, int l, int i1) {
        super.d(i, j, k, l, i1);
        this.server.serverConfigurationManager.a((double) i, (double) j, (double) k, 64.0D, this.worldProvider.dimension, new Packet54PlayNoteBlock(i, j, k, l, i1));
    }

    public void saveLevel() {
        this.w.e();
    }

    protected void i() {
        boolean flag = this.v();

        super.i();
        if (flag != this.v()) {
            if (flag) {
                this.server.serverConfigurationManager.sendAll(new Packet70Bed(2));
            } else {
                this.server.serverConfigurationManager.sendAll(new Packet70Bed(1));
            }
        }
    }
}
