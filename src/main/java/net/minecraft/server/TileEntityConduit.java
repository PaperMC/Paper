package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

public class TileEntityConduit extends TileEntity implements ITickable {

    private static final Block[] b = new Block[]{Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE};
    public int a;
    private float c;
    private boolean g;
    private boolean h;
    private final List<BlockPosition> i;
    @Nullable
    private EntityLiving target;
    @Nullable
    private UUID k;
    private long l;

    public TileEntityConduit() {
        this(TileEntityTypes.CONDUIT);
    }

    public TileEntityConduit(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
        this.i = Lists.newArrayList();
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        if (nbttagcompound.b("Target")) {
            this.k = nbttagcompound.a("Target");
        } else {
            this.k = null;
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (this.target != null) {
            nbttagcompound.a("Target", this.target.getUniqueID());
        }

        return nbttagcompound;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 5, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    @Override
    public void tick() {
        ++this.a;
        long i = this.world.getTime();

        if (i % 40L == 0L) {
            this.a(this.h());
            if (!this.world.isClientSide && this.d()) {
                this.j();
                this.k();
            }
        }

        if (i % 80L == 0L && this.d()) {
            this.a(SoundEffects.BLOCK_CONDUIT_AMBIENT);
        }

        if (i > this.l && this.d()) {
            this.l = i + 60L + (long) this.world.getRandom().nextInt(40);
            this.a(SoundEffects.BLOCK_CONDUIT_AMBIENT_SHORT);
        }

        if (this.world.isClientSide) {
            this.l();
            this.y();
            if (this.d()) {
                ++this.c;
            }
        }

    }

    private boolean h() {
        this.i.clear();

        int i;
        int j;
        int k;

        for (i = -1; i <= 1; ++i) {
            for (j = -1; j <= 1; ++j) {
                for (k = -1; k <= 1; ++k) {
                    BlockPosition blockposition = this.position.b(i, j, k);

                    if (!this.world.A(blockposition)) {
                        return false;
                    }
                }
            }
        }

        for (i = -2; i <= 2; ++i) {
            for (j = -2; j <= 2; ++j) {
                for (k = -2; k <= 2; ++k) {
                    int l = Math.abs(i);
                    int i1 = Math.abs(j);
                    int j1 = Math.abs(k);

                    if ((l > 1 || i1 > 1 || j1 > 1) && (i == 0 && (i1 == 2 || j1 == 2) || j == 0 && (l == 2 || j1 == 2) || k == 0 && (l == 2 || i1 == 2))) {
                        BlockPosition blockposition1 = this.position.b(i, j, k);
                        IBlockData iblockdata = this.world.getType(blockposition1);
                        Block[] ablock = TileEntityConduit.b;
                        int k1 = ablock.length;

                        for (int l1 = 0; l1 < k1; ++l1) {
                            Block block = ablock[l1];

                            if (iblockdata.a(block)) {
                                this.i.add(blockposition1);
                            }
                        }
                    }
                }
            }
        }

        this.b(this.i.size() >= 42);
        return this.i.size() >= 16;
    }

    private void j() {
        int i = this.i.size();
        int j = i / 7 * 16;
        int k = this.position.getX();
        int l = this.position.getY();
        int i1 = this.position.getZ();
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) k, (double) l, (double) i1, (double) (k + 1), (double) (l + 1), (double) (i1 + 1))).g((double) j).b(0.0D, (double) this.world.getBuildHeight(), 0.0D);
        List<EntityHuman> list = this.world.a(EntityHuman.class, axisalignedbb);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman) iterator.next();

                if (this.position.a((BaseBlockPosition) entityhuman.getChunkCoordinates(), (double) j) && entityhuman.isInWaterOrRain()) {
                    entityhuman.addEffect(new MobEffect(MobEffects.CONDUIT_POWER, 260, 0, true, true));
                }
            }

        }
    }

    private void k() {
        EntityLiving entityliving = this.target;
        int i = this.i.size();

        if (i < 42) {
            this.target = null;
        } else if (this.target == null && this.k != null) {
            this.target = this.x();
            this.k = null;
        } else if (this.target == null) {
            List<EntityLiving> list = this.world.a(EntityLiving.class, this.m(), (entityliving1) -> {
                return entityliving1 instanceof IMonster && entityliving1.isInWaterOrRain();
            });

            if (!list.isEmpty()) {
                this.target = (EntityLiving) list.get(this.world.random.nextInt(list.size()));
            }
        } else if (!this.target.isAlive() || !this.position.a((BaseBlockPosition) this.target.getChunkCoordinates(), 8.0D)) {
            this.target = null;
        }

        if (this.target != null) {
            this.world.playSound((EntityHuman) null, this.target.locX(), this.target.locY(), this.target.locZ(), SoundEffects.BLOCK_CONDUIT_ATTACK_TARGET, SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.target.damageEntity(DamageSource.MAGIC, 4.0F);
        }

        if (entityliving != this.target) {
            IBlockData iblockdata = this.getBlock();

            this.world.notify(this.position, iblockdata, iblockdata, 2);
        }

    }

    private void l() {
        if (this.k == null) {
            this.target = null;
        } else if (this.target == null || !this.target.getUniqueID().equals(this.k)) {
            this.target = this.x();
            if (this.target == null) {
                this.k = null;
            }
        }

    }

    private AxisAlignedBB m() {
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();

        return (new AxisAlignedBB((double) i, (double) j, (double) k, (double) (i + 1), (double) (j + 1), (double) (k + 1))).g(8.0D);
    }

    @Nullable
    private EntityLiving x() {
        List<EntityLiving> list = this.world.a(EntityLiving.class, this.m(), (entityliving) -> {
            return entityliving.getUniqueID().equals(this.k);
        });

        return list.size() == 1 ? (EntityLiving) list.get(0) : null;
    }

    private void y() {
        Random random = this.world.random;
        double d0 = (double) (MathHelper.sin((float) (this.a + 35) * 0.1F) / 2.0F + 0.5F);

        d0 = (d0 * d0 + d0) * 0.30000001192092896D;
        Vec3D vec3d = new Vec3D((double) this.position.getX() + 0.5D, (double) this.position.getY() + 1.5D + d0, (double) this.position.getZ() + 0.5D);
        Iterator iterator = this.i.iterator();

        float f;
        float f1;

        while (iterator.hasNext()) {
            BlockPosition blockposition = (BlockPosition) iterator.next();

            if (random.nextInt(50) == 0) {
                f = -0.5F + random.nextFloat();
                f1 = -2.0F + random.nextFloat();
                float f2 = -0.5F + random.nextFloat();
                BlockPosition blockposition1 = blockposition.b(this.position);
                Vec3D vec3d1 = (new Vec3D((double) f, (double) f1, (double) f2)).add((double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());

                this.world.addParticle(Particles.NAUTILUS, vec3d.x, vec3d.y, vec3d.z, vec3d1.x, vec3d1.y, vec3d1.z);
            }
        }

        if (this.target != null) {
            Vec3D vec3d2 = new Vec3D(this.target.locX(), this.target.getHeadY(), this.target.locZ());
            float f3 = (-0.5F + random.nextFloat()) * (3.0F + this.target.getWidth());

            f = -1.0F + random.nextFloat() * this.target.getHeight();
            f1 = (-0.5F + random.nextFloat()) * (3.0F + this.target.getWidth());
            Vec3D vec3d3 = new Vec3D((double) f3, (double) f, (double) f1);

            this.world.addParticle(Particles.NAUTILUS, vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z);
        }

    }

    public boolean d() {
        return this.g;
    }

    private void a(boolean flag) {
        if (flag != this.g) {
            this.a(flag ? SoundEffects.BLOCK_CONDUIT_ACTIVATE : SoundEffects.BLOCK_CONDUIT_DEACTIVATE);
        }

        this.g = flag;
    }

    private void b(boolean flag) {
        this.h = flag;
    }

    public void a(SoundEffect soundeffect) {
        this.world.playSound((EntityHuman) null, this.position, soundeffect, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
