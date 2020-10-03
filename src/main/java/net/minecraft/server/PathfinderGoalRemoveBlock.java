package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class PathfinderGoalRemoveBlock extends PathfinderGoalGotoTarget {

    private final Block g;
    private final EntityInsentient entity;
    private int i;

    public PathfinderGoalRemoveBlock(Block block, EntityCreature entitycreature, double d0, int i) {
        super(entitycreature, d0, 24, i);
        this.g = block;
        this.entity = entitycreature;
    }

    @Override
    public boolean a() {
        if (!this.entity.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            return false;
        } else if (this.c > 0) {
            --this.c;
            return false;
        } else if (this.n()) {
            this.c = 20;
            return true;
        } else {
            this.c = this.a(this.a);
            return false;
        }
    }

    private boolean n() {
        return this.e != null && this.a((IWorldReader) this.a.world, this.e) ? true : this.m();
    }

    @Override
    public void d() {
        super.d();
        this.entity.fallDistance = 1.0F;
    }

    @Override
    public void c() {
        super.c();
        this.i = 0;
    }

    public void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {}

    public void a(World world, BlockPosition blockposition) {}

    @Override
    public void e() {
        super.e();
        World world = this.entity.world;
        BlockPosition blockposition = this.entity.getChunkCoordinates();
        BlockPosition blockposition1 = this.a(blockposition, (IBlockAccess) world);
        Random random = this.entity.getRandom();

        if (this.l() && blockposition1 != null) {
            Vec3D vec3d;
            double d0;

            if (this.i > 0) {
                vec3d = this.entity.getMot();
                this.entity.setMot(vec3d.x, 0.3D, vec3d.z);
                if (!world.isClientSide) {
                    d0 = 0.08D;
                    ((WorldServer) world).a(new ParticleParamItem(Particles.ITEM, new ItemStack(Items.EGG)), (double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.7D, (double) blockposition1.getZ() + 0.5D, 3, ((double) random.nextFloat() - 0.5D) * 0.08D, ((double) random.nextFloat() - 0.5D) * 0.08D, ((double) random.nextFloat() - 0.5D) * 0.08D, 0.15000000596046448D);
                }
            }

            if (this.i % 2 == 0) {
                vec3d = this.entity.getMot();
                this.entity.setMot(vec3d.x, -0.3D, vec3d.z);
                if (this.i % 6 == 0) {
                    this.a((GeneratorAccess) world, this.e);
                }
            }

            if (this.i > 60) {
                world.a(blockposition1, false);
                if (!world.isClientSide) {
                    for (int i = 0; i < 20; ++i) {
                        d0 = random.nextGaussian() * 0.02D;
                        double d1 = random.nextGaussian() * 0.02D;
                        double d2 = random.nextGaussian() * 0.02D;

                        ((WorldServer) world).a(Particles.POOF, (double) blockposition1.getX() + 0.5D, (double) blockposition1.getY(), (double) blockposition1.getZ() + 0.5D, 1, d0, d1, d2, 0.15000000596046448D);
                    }

                    this.a(world, blockposition1);
                }
            }

            ++this.i;
        }

    }

    @Nullable
    private BlockPosition a(BlockPosition blockposition, IBlockAccess iblockaccess) {
        if (iblockaccess.getType(blockposition).a(this.g)) {
            return blockposition;
        } else {
            BlockPosition[] ablockposition = new BlockPosition[]{blockposition.down(), blockposition.west(), blockposition.east(), blockposition.north(), blockposition.south(), blockposition.down().down()};
            BlockPosition[] ablockposition1 = ablockposition;
            int i = ablockposition.length;

            for (int j = 0; j < i; ++j) {
                BlockPosition blockposition1 = ablockposition1[j];

                if (iblockaccess.getType(blockposition1).a(this.g)) {
                    return blockposition1;
                }
            }

            return null;
        }
    }

    @Override
    protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
        IChunkAccess ichunkaccess = iworldreader.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4, ChunkStatus.FULL, false);

        return ichunkaccess == null ? false : ichunkaccess.getType(blockposition).a(this.g) && ichunkaccess.getType(blockposition.up()).isAir() && ichunkaccess.getType(blockposition.up(2)).isAir();
    }
}
