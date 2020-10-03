package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;

public class PathfinderGoalPanic extends PathfinderGoal {

    protected final EntityCreature a;
    protected final double b;
    protected double c;
    protected double d;
    protected double e;
    protected boolean f;

    public PathfinderGoalPanic(EntityCreature entitycreature, double d0) {
        this.a = entitycreature;
        this.b = d0;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
    }

    @Override
    public boolean a() {
        if (this.a.getLastDamager() == null && !this.a.isBurning()) {
            return false;
        } else {
            if (this.a.isBurning()) {
                BlockPosition blockposition = this.a(this.a.world, this.a, 5, 4);

                if (blockposition != null) {
                    this.c = (double) blockposition.getX();
                    this.d = (double) blockposition.getY();
                    this.e = (double) blockposition.getZ();
                    return true;
                }
            }

            return this.g();
        }
    }

    protected boolean g() {
        Vec3D vec3d = RandomPositionGenerator.a(this.a, 5, 4);

        if (vec3d == null) {
            return false;
        } else {
            this.c = vec3d.x;
            this.d = vec3d.y;
            this.e = vec3d.z;
            return true;
        }
    }

    public boolean h() {
        return this.f;
    }

    @Override
    public void c() {
        this.a.getNavigation().a(this.c, this.d, this.e, this.b);
        this.f = true;
    }

    @Override
    public void d() {
        this.f = false;
    }

    @Override
    public boolean b() {
        return !this.a.getNavigation().m();
    }

    @Nullable
    protected BlockPosition a(IBlockAccess iblockaccess, Entity entity, int i, int j) {
        BlockPosition blockposition = entity.getChunkCoordinates();
        int k = blockposition.getX();
        int l = blockposition.getY();
        int i1 = blockposition.getZ();
        float f = (float) (i * i * j * 2);
        BlockPosition blockposition1 = null;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int j1 = k - i; j1 <= k + i; ++j1) {
            for (int k1 = l - j; k1 <= l + j; ++k1) {
                for (int l1 = i1 - i; l1 <= i1 + i; ++l1) {
                    blockposition_mutableblockposition.d(j1, k1, l1);
                    if (iblockaccess.getFluid(blockposition_mutableblockposition).a((Tag) TagsFluid.WATER)) {
                        float f1 = (float) ((j1 - k) * (j1 - k) + (k1 - l) * (k1 - l) + (l1 - i1) * (l1 - i1));

                        if (f1 < f) {
                            f = f1;
                            blockposition1 = new BlockPosition(blockposition_mutableblockposition);
                        }
                    }
                }
            }
        }

        return blockposition1;
    }
}
