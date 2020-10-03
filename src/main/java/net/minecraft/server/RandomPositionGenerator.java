package net.minecraft.server;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;

public class RandomPositionGenerator {

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j) {
        return a(entitycreature, i, j, 0, (Vec3D) null, true, 1.5707963705062866D, entitycreature::f, false, 0, 0, true);
    }

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j, int k, @Nullable Vec3D vec3d, double d0) {
        return a(entitycreature, i, j, k, vec3d, true, d0, entitycreature::f, true, 0, 0, false);
    }

    @Nullable
    public static Vec3D b(EntityCreature entitycreature, int i, int j) {
        entitycreature.getClass();
        return a(entitycreature, i, j, entitycreature::f);
    }

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j, ToDoubleFunction<BlockPosition> todoublefunction) {
        return a(entitycreature, i, j, 0, (Vec3D) null, false, 0.0D, todoublefunction, true, 0, 0, true);
    }

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j, Vec3D vec3d, float f, int k, int l) {
        return a(entitycreature, i, j, 0, vec3d, false, (double) f, entitycreature::f, true, k, l, true);
    }

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j, Vec3D vec3d) {
        Vec3D vec3d1 = vec3d.a(entitycreature.locX(), entitycreature.locY(), entitycreature.locZ());

        return a(entitycreature, i, j, 0, vec3d1, false, 1.5707963705062866D, entitycreature::f, true, 0, 0, true);
    }

    @Nullable
    public static Vec3D b(EntityCreature entitycreature, int i, int j, Vec3D vec3d) {
        Vec3D vec3d1 = vec3d.a(entitycreature.locX(), entitycreature.locY(), entitycreature.locZ());

        return a(entitycreature, i, j, 0, vec3d1, true, 1.5707963705062866D, entitycreature::f, false, 0, 0, true);
    }

    @Nullable
    public static Vec3D a(EntityCreature entitycreature, int i, int j, Vec3D vec3d, double d0) {
        Vec3D vec3d1 = vec3d.a(entitycreature.locX(), entitycreature.locY(), entitycreature.locZ());

        return a(entitycreature, i, j, 0, vec3d1, true, d0, entitycreature::f, false, 0, 0, true);
    }

    @Nullable
    public static Vec3D b(EntityCreature entitycreature, int i, int j, int k, Vec3D vec3d, double d0) {
        Vec3D vec3d1 = vec3d.a(entitycreature.locX(), entitycreature.locY(), entitycreature.locZ());

        return a(entitycreature, i, j, k, vec3d1, false, d0, entitycreature::f, true, 0, 0, false);
    }

    @Nullable
    public static Vec3D c(EntityCreature entitycreature, int i, int j, Vec3D vec3d) {
        Vec3D vec3d1 = entitycreature.getPositionVector().d(vec3d);

        return a(entitycreature, i, j, 0, vec3d1, true, 1.5707963705062866D, entitycreature::f, false, 0, 0, true);
    }

    @Nullable
    public static Vec3D d(EntityCreature entitycreature, int i, int j, Vec3D vec3d) {
        Vec3D vec3d1 = entitycreature.getPositionVector().d(vec3d);

        return a(entitycreature, i, j, 0, vec3d1, false, 1.5707963705062866D, entitycreature::f, true, 0, 0, true);
    }

    @Nullable
    private static Vec3D a(EntityCreature entitycreature, int i, int j, int k, @Nullable Vec3D vec3d, boolean flag, double d0, ToDoubleFunction<BlockPosition> todoublefunction, boolean flag1, int l, int i1, boolean flag2) {
        NavigationAbstract navigationabstract = entitycreature.getNavigation();
        Random random = entitycreature.getRandom();
        boolean flag3;

        if (entitycreature.ez()) {
            flag3 = entitycreature.ew().a((IPosition) entitycreature.getPositionVector(), (double) (entitycreature.ex() + (float) i) + 1.0D);
        } else {
            flag3 = false;
        }

        boolean flag4 = false;
        double d1 = Double.NEGATIVE_INFINITY;
        BlockPosition blockposition = entitycreature.getChunkCoordinates();

        for (int j1 = 0; j1 < 10; ++j1) {
            BlockPosition blockposition1 = a(random, i, j, k, vec3d, d0);

            if (blockposition1 != null) {
                int k1 = blockposition1.getX();
                int l1 = blockposition1.getY();
                int i2 = blockposition1.getZ();
                BlockPosition blockposition2;

                if (entitycreature.ez() && i > 1) {
                    blockposition2 = entitycreature.ew();
                    if (entitycreature.locX() > (double) blockposition2.getX()) {
                        k1 -= random.nextInt(i / 2);
                    } else {
                        k1 += random.nextInt(i / 2);
                    }

                    if (entitycreature.locZ() > (double) blockposition2.getZ()) {
                        i2 -= random.nextInt(i / 2);
                    } else {
                        i2 += random.nextInt(i / 2);
                    }
                }

                blockposition2 = new BlockPosition((double) k1 + entitycreature.locX(), (double) l1 + entitycreature.locY(), (double) i2 + entitycreature.locZ());
                if (blockposition2.getY() >= 0 && blockposition2.getY() <= entitycreature.world.getBuildHeight() && (!flag3 || entitycreature.a(blockposition2)) && (!flag2 || navigationabstract.a(blockposition2))) {
                    if (flag1) {
                        blockposition2 = a(blockposition2, random.nextInt(l + 1) + i1, entitycreature.world.getBuildHeight(), (blockposition3) -> {
                            return entitycreature.world.getType(blockposition3).getMaterial().isBuildable();
                        });
                    }

                    if (flag || !entitycreature.world.getFluid(blockposition2).a((Tag) TagsFluid.WATER)) {
                        PathType pathtype = PathfinderNormal.a((IBlockAccess) entitycreature.world, blockposition2.i());

                        if (entitycreature.a(pathtype) == 0.0F) {
                            double d2 = todoublefunction.applyAsDouble(blockposition2);

                            if (d2 > d1) {
                                d1 = d2;
                                blockposition = blockposition2;
                                flag4 = true;
                            }
                        }
                    }
                }
            }
        }

        if (flag4) {
            return Vec3D.c((BaseBlockPosition) blockposition);
        } else {
            return null;
        }
    }

    @Nullable
    private static BlockPosition a(Random random, int i, int j, int k, @Nullable Vec3D vec3d, double d0) {
        if (vec3d != null && d0 < 3.141592653589793D) {
            double d1 = MathHelper.d(vec3d.z, vec3d.x) - 1.5707963705062866D;
            double d2 = d1 + (double) (2.0F * random.nextFloat() - 1.0F) * d0;
            double d3 = Math.sqrt(random.nextDouble()) * (double) MathHelper.a * (double) i;
            double d4 = -d3 * Math.sin(d2);
            double d5 = d3 * Math.cos(d2);

            if (Math.abs(d4) <= (double) i && Math.abs(d5) <= (double) i) {
                int l = random.nextInt(2 * j + 1) - j + k;

                return new BlockPosition(d4, (double) l, d5);
            } else {
                return null;
            }
        } else {
            int i1 = random.nextInt(2 * i + 1) - i;
            int j1 = random.nextInt(2 * j + 1) - j + k;
            int k1 = random.nextInt(2 * i + 1) - i;

            return new BlockPosition(i1, j1, k1);
        }
    }

    static BlockPosition a(BlockPosition blockposition, int i, int j, Predicate<BlockPosition> predicate) {
        if (i < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + i + ", expected >= 0");
        } else if (!predicate.test(blockposition)) {
            return blockposition;
        } else {
            BlockPosition blockposition1;

            for (blockposition1 = blockposition.up(); blockposition1.getY() < j && predicate.test(blockposition1); blockposition1 = blockposition1.up()) {
                ;
            }

            BlockPosition blockposition2;
            BlockPosition blockposition3;

            for (blockposition3 = blockposition1; blockposition3.getY() < j && blockposition3.getY() - blockposition1.getY() < i; blockposition3 = blockposition2) {
                blockposition2 = blockposition3.up();
                if (predicate.test(blockposition2)) {
                    break;
                }
            }

            return blockposition3;
        }
    }
}
