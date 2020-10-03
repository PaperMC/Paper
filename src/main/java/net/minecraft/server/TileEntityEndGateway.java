package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntityEndGateway extends TileEntityEnderPortal implements ITickable {

    private static final Logger LOGGER = LogManager.getLogger();
    public long age;
    private int c;
    @Nullable
    public BlockPosition exitPortal;
    public boolean exactTeleport;

    public TileEntityEndGateway() {
        super(TileEntityTypes.END_GATEWAY);
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        nbttagcompound.setLong("Age", this.age);
        if (this.exitPortal != null) {
            nbttagcompound.set("ExitPortal", GameProfileSerializer.a(this.exitPortal));
        }

        if (this.exactTeleport) {
            nbttagcompound.setBoolean("ExactTeleport", this.exactTeleport);
        }

        return nbttagcompound;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.age = nbttagcompound.getLong("Age");
        if (nbttagcompound.hasKeyOfType("ExitPortal", 10)) {
            this.exitPortal = GameProfileSerializer.b(nbttagcompound.getCompound("ExitPortal"));
        }

        this.exactTeleport = nbttagcompound.getBoolean("ExactTeleport");
    }

    @Override
    public void tick() {
        boolean flag = this.d();
        boolean flag1 = this.f();

        ++this.age;
        if (flag1) {
            --this.c;
        } else if (!this.world.isClientSide) {
            List<Entity> list = this.world.a(Entity.class, new AxisAlignedBB(this.getPosition()), TileEntityEndGateway::a);

            if (!list.isEmpty()) {
                this.b((Entity) list.get(this.world.random.nextInt(list.size())));
            }

            if (this.age % 2400L == 0L) {
                this.h();
            }
        }

        if (flag != this.d() || flag1 != this.f()) {
            this.update();
        }

    }

    public static boolean a(Entity entity) {
        return IEntitySelector.g.test(entity) && !entity.getRootVehicle().ah();
    }

    public boolean d() {
        return this.age < 200L;
    }

    public boolean f() {
        return this.c > 0;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 8, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    public void h() {
        if (!this.world.isClientSide) {
            this.c = 40;
            this.world.playBlockAction(this.getPosition(), this.getBlock().getBlock(), 1, 0);
            this.update();
        }

    }

    @Override
    public boolean setProperty(int i, int j) {
        if (i == 1) {
            this.c = 40;
            return true;
        } else {
            return super.setProperty(i, j);
        }
    }

    public void b(Entity entity) {
        if (this.world instanceof WorldServer && !this.f()) {
            this.c = 100;
            if (this.exitPortal == null && this.world.getDimensionKey() == World.THE_END) {
                this.a((WorldServer) this.world);
            }

            if (this.exitPortal != null) {
                BlockPosition blockposition = this.exactTeleport ? this.exitPortal : this.k();
                Entity entity1;

                if (entity instanceof EntityEnderPearl) {
                    Entity entity2 = ((EntityEnderPearl) entity).getShooter();

                    if (entity2 instanceof EntityPlayer) {
                        CriterionTriggers.d.a((EntityPlayer) entity2, this.world.getType(this.getPosition()));
                    }

                    if (entity2 != null) {
                        entity1 = entity2;
                        entity.die();
                    } else {
                        entity1 = entity;
                    }
                } else {
                    entity1 = entity.getRootVehicle();
                }

                entity1.resetPortalCooldown();
                entity1.enderTeleportAndLoad((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D);
            }

            this.h();
        }
    }

    private BlockPosition k() {
        BlockPosition blockposition = a(this.world, this.exitPortal.b(0, 2, 0), 5, false);

        TileEntityEndGateway.LOGGER.debug("Best exit position for portal at {} is {}", this.exitPortal, blockposition);
        return blockposition.up();
    }

    private void a(WorldServer worldserver) {
        Vec3D vec3d = (new Vec3D((double) this.getPosition().getX(), 0.0D, (double) this.getPosition().getZ())).d();
        Vec3D vec3d1 = vec3d.a(1024.0D);

        int i;

        for (i = 16; a((World) worldserver, vec3d1).b() > 0 && i-- > 0; vec3d1 = vec3d1.e(vec3d.a(-16.0D))) {
            TileEntityEndGateway.LOGGER.debug("Skipping backwards past nonempty chunk at {}", vec3d1);
        }

        for (i = 16; a((World) worldserver, vec3d1).b() == 0 && i-- > 0; vec3d1 = vec3d1.e(vec3d.a(16.0D))) {
            TileEntityEndGateway.LOGGER.debug("Skipping forward past empty chunk at {}", vec3d1);
        }

        TileEntityEndGateway.LOGGER.debug("Found chunk at {}", vec3d1);
        Chunk chunk = a((World) worldserver, vec3d1);

        this.exitPortal = a(chunk);
        if (this.exitPortal == null) {
            this.exitPortal = new BlockPosition(vec3d1.x + 0.5D, 75.0D, vec3d1.z + 0.5D);
            TileEntityEndGateway.LOGGER.debug("Failed to find suitable block, settling on {}", this.exitPortal);
            BiomeDecoratorGroups.END_ISLAND.a(worldserver, worldserver.getChunkProvider().getChunkGenerator(), new Random(this.exitPortal.asLong()), this.exitPortal);
        } else {
            TileEntityEndGateway.LOGGER.debug("Found block at {}", this.exitPortal);
        }

        this.exitPortal = a(worldserver, this.exitPortal, 16, true);
        TileEntityEndGateway.LOGGER.debug("Creating portal at {}", this.exitPortal);
        this.exitPortal = this.exitPortal.up(10);
        this.a(worldserver, this.exitPortal);
        this.update();
    }

    private static BlockPosition a(IBlockAccess iblockaccess, BlockPosition blockposition, int i, boolean flag) {
        BlockPosition blockposition1 = null;

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (j != 0 || k != 0 || flag) {
                    for (int l = 255; l > (blockposition1 == null ? 0 : blockposition1.getY()); --l) {
                        BlockPosition blockposition2 = new BlockPosition(blockposition.getX() + j, l, blockposition.getZ() + k);
                        IBlockData iblockdata = iblockaccess.getType(blockposition2);

                        if (iblockdata.r(iblockaccess, blockposition2) && (flag || !iblockdata.a(Blocks.BEDROCK))) {
                            blockposition1 = blockposition2;
                            break;
                        }
                    }
                }
            }
        }

        return blockposition1 == null ? blockposition : blockposition1;
    }

    private static Chunk a(World world, Vec3D vec3d) {
        return world.getChunkAt(MathHelper.floor(vec3d.x / 16.0D), MathHelper.floor(vec3d.z / 16.0D));
    }

    @Nullable
    private static BlockPosition a(Chunk chunk) {
        ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
        BlockPosition blockposition = new BlockPosition(chunkcoordintpair.d(), 30, chunkcoordintpair.e());
        int i = chunk.b() + 16 - 1;
        BlockPosition blockposition1 = new BlockPosition(chunkcoordintpair.f(), i, chunkcoordintpair.g());
        BlockPosition blockposition2 = null;
        double d0 = 0.0D;
        Iterator iterator = BlockPosition.a(blockposition, blockposition1).iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition3 = (BlockPosition) iterator.next();
            IBlockData iblockdata = chunk.getType(blockposition3);
            BlockPosition blockposition4 = blockposition3.up();
            BlockPosition blockposition5 = blockposition3.up(2);

            if (iblockdata.a(Blocks.END_STONE) && !chunk.getType(blockposition4).r(chunk, blockposition4) && !chunk.getType(blockposition5).r(chunk, blockposition5)) {
                double d1 = blockposition3.distanceSquared(0.0D, 0.0D, 0.0D, true);

                if (blockposition2 == null || d1 < d0) {
                    blockposition2 = blockposition3;
                    d0 = d1;
                }
            }
        }

        return blockposition2;
    }

    private void a(WorldServer worldserver, BlockPosition blockposition) {
        WorldGenerator.END_GATEWAY.b((WorldGenFeatureConfiguration) WorldGenEndGatewayConfiguration.a(this.getPosition(), false)).a(worldserver, worldserver.getChunkProvider().getChunkGenerator(), new Random(), blockposition);
    }

    public void a(BlockPosition blockposition, boolean flag) {
        this.exactTeleport = flag;
        this.exitPortal = blockposition;
    }
}
