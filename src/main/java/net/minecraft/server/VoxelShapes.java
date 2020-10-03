package net.minecraft.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public final class VoxelShapes {

    private static final VoxelShape b = (VoxelShape) SystemUtils.a(() -> {
        VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(1, 1, 1);

        voxelshapebitset.a(0, 0, 0, true, true);
        return new VoxelShapeCube(voxelshapebitset);
    });
    public static final VoxelShape a = create(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    private static final VoxelShape c = new VoxelShapeArray(new VoxelShapeBitSet(0, 0, 0), new DoubleArrayList(new double[]{0.0D}), new DoubleArrayList(new double[]{0.0D}), new DoubleArrayList(new double[]{0.0D}));

    public static VoxelShape a() {
        return VoxelShapes.c;
    }

    public static VoxelShape b() {
        return VoxelShapes.b;
    }

    public static VoxelShape create(double d0, double d1, double d2, double d3, double d4, double d5) {
        return a(new AxisAlignedBB(d0, d1, d2, d3, d4, d5));
    }

    public static VoxelShape a(AxisAlignedBB axisalignedbb) {
        int i = a(axisalignedbb.minX, axisalignedbb.maxX);
        int j = a(axisalignedbb.minY, axisalignedbb.maxY);
        int k = a(axisalignedbb.minZ, axisalignedbb.maxZ);

        if (i >= 0 && j >= 0 && k >= 0) {
            if (i == 0 && j == 0 && k == 0) {
                return axisalignedbb.e(0.5D, 0.5D, 0.5D) ? b() : a();
            } else {
                int l = 1 << i;
                int i1 = 1 << j;
                int j1 = 1 << k;
                int k1 = (int) Math.round(axisalignedbb.minX * (double) l);
                int l1 = (int) Math.round(axisalignedbb.maxX * (double) l);
                int i2 = (int) Math.round(axisalignedbb.minY * (double) i1);
                int j2 = (int) Math.round(axisalignedbb.maxY * (double) i1);
                int k2 = (int) Math.round(axisalignedbb.minZ * (double) j1);
                int l2 = (int) Math.round(axisalignedbb.maxZ * (double) j1);
                VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(l, i1, j1, k1, i2, k2, l1, j2, l2);

                for (long i3 = (long) k1; i3 < (long) l1; ++i3) {
                    for (long j3 = (long) i2; j3 < (long) j2; ++j3) {
                        for (long k3 = (long) k2; k3 < (long) l2; ++k3) {
                            voxelshapebitset.a((int) i3, (int) j3, (int) k3, false, true);
                        }
                    }
                }

                return new VoxelShapeCube(voxelshapebitset);
            }
        } else {
            return new VoxelShapeArray(VoxelShapes.b.a, new double[]{axisalignedbb.minX, axisalignedbb.maxX}, new double[]{axisalignedbb.minY, axisalignedbb.maxY}, new double[]{axisalignedbb.minZ, axisalignedbb.maxZ});
        }
    }

    private static int a(double d0, double d1) {
        if (d0 >= -1.0E-7D && d1 <= 1.0000001D) {
            for (int i = 0; i <= 3; ++i) {
                double d2 = d0 * (double) (1 << i);
                double d3 = d1 * (double) (1 << i);
                boolean flag = Math.abs(d2 - Math.floor(d2)) < 1.0E-7D;
                boolean flag1 = Math.abs(d3 - Math.floor(d3)) < 1.0E-7D;

                if (flag && flag1) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    protected static long a(int i, int j) {
        return (long) i * (long) (j / IntMath.gcd(i, j));
    }

    public static VoxelShape a(VoxelShape voxelshape, VoxelShape voxelshape1) {
        return a(voxelshape, voxelshape1, OperatorBoolean.OR);
    }

    public static VoxelShape a(VoxelShape voxelshape, VoxelShape... avoxelshape) {
        return (VoxelShape) Arrays.stream(avoxelshape).reduce(voxelshape, VoxelShapes::a);
    }

    public static VoxelShape a(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        return b(voxelshape, voxelshape1, operatorboolean).c();
    }

    public static VoxelShape b(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        if (operatorboolean.apply(false, false)) {
            throw (IllegalArgumentException) SystemUtils.c((Throwable) (new IllegalArgumentException()));
        } else if (voxelshape == voxelshape1) {
            return operatorboolean.apply(true, true) ? voxelshape : a();
        } else {
            boolean flag = operatorboolean.apply(true, false);
            boolean flag1 = operatorboolean.apply(false, true);

            if (voxelshape.isEmpty()) {
                return flag1 ? voxelshape1 : a();
            } else if (voxelshape1.isEmpty()) {
                return flag ? voxelshape : a();
            } else {
                VoxelShapeMerger voxelshapemerger = a(1, voxelshape.a(EnumDirection.EnumAxis.X), voxelshape1.a(EnumDirection.EnumAxis.X), flag, flag1);
                VoxelShapeMerger voxelshapemerger1 = a(voxelshapemerger.a().size() - 1, voxelshape.a(EnumDirection.EnumAxis.Y), voxelshape1.a(EnumDirection.EnumAxis.Y), flag, flag1);
                VoxelShapeMerger voxelshapemerger2 = a((voxelshapemerger.a().size() - 1) * (voxelshapemerger1.a().size() - 1), voxelshape.a(EnumDirection.EnumAxis.Z), voxelshape1.a(EnumDirection.EnumAxis.Z), flag, flag1);
                VoxelShapeBitSet voxelshapebitset = VoxelShapeBitSet.a(voxelshape.a, voxelshape1.a, voxelshapemerger, voxelshapemerger1, voxelshapemerger2, operatorboolean);

                return (VoxelShape) (voxelshapemerger instanceof VoxelShapeCubeMerger && voxelshapemerger1 instanceof VoxelShapeCubeMerger && voxelshapemerger2 instanceof VoxelShapeCubeMerger ? new VoxelShapeCube(voxelshapebitset) : new VoxelShapeArray(voxelshapebitset, voxelshapemerger.a(), voxelshapemerger1.a(), voxelshapemerger2.a()));
            }
        }
    }

    public static boolean c(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        if (operatorboolean.apply(false, false)) {
            throw (IllegalArgumentException) SystemUtils.c((Throwable) (new IllegalArgumentException()));
        } else if (voxelshape == voxelshape1) {
            return operatorboolean.apply(true, true);
        } else if (voxelshape.isEmpty()) {
            return operatorboolean.apply(false, !voxelshape1.isEmpty());
        } else if (voxelshape1.isEmpty()) {
            return operatorboolean.apply(!voxelshape.isEmpty(), false);
        } else {
            boolean flag = operatorboolean.apply(true, false);
            boolean flag1 = operatorboolean.apply(false, true);
            EnumDirection.EnumAxis[] aenumdirection_enumaxis = EnumAxisCycle.d;
            int i = aenumdirection_enumaxis.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection.EnumAxis enumdirection_enumaxis = aenumdirection_enumaxis[j];

                if (voxelshape.c(enumdirection_enumaxis) < voxelshape1.b(enumdirection_enumaxis) - 1.0E-7D) {
                    return flag || flag1;
                }

                if (voxelshape1.c(enumdirection_enumaxis) < voxelshape.b(enumdirection_enumaxis) - 1.0E-7D) {
                    return flag || flag1;
                }
            }

            VoxelShapeMerger voxelshapemerger = a(1, voxelshape.a(EnumDirection.EnumAxis.X), voxelshape1.a(EnumDirection.EnumAxis.X), flag, flag1);
            VoxelShapeMerger voxelshapemerger1 = a(voxelshapemerger.a().size() - 1, voxelshape.a(EnumDirection.EnumAxis.Y), voxelshape1.a(EnumDirection.EnumAxis.Y), flag, flag1);
            VoxelShapeMerger voxelshapemerger2 = a((voxelshapemerger.a().size() - 1) * (voxelshapemerger1.a().size() - 1), voxelshape.a(EnumDirection.EnumAxis.Z), voxelshape1.a(EnumDirection.EnumAxis.Z), flag, flag1);

            return a(voxelshapemerger, voxelshapemerger1, voxelshapemerger2, voxelshape.a, voxelshape1.a, operatorboolean);
        }
    }

    private static boolean a(VoxelShapeMerger voxelshapemerger, VoxelShapeMerger voxelshapemerger1, VoxelShapeMerger voxelshapemerger2, VoxelShapeDiscrete voxelshapediscrete, VoxelShapeDiscrete voxelshapediscrete1, OperatorBoolean operatorboolean) {
        return !voxelshapemerger.a((i, j, k) -> {
            return voxelshapemerger1.a((l, i1, j1) -> {
                return voxelshapemerger2.a((k1, l1, i2) -> {
                    return !operatorboolean.apply(voxelshapediscrete.c(i, l, k1), voxelshapediscrete1.c(j, i1, l1));
                });
            });
        });
    }

    public static double a(EnumDirection.EnumAxis enumdirection_enumaxis, AxisAlignedBB axisalignedbb, Stream<VoxelShape> stream, double d0) {
        for (Iterator iterator = stream.iterator(); iterator.hasNext(); d0 = ((VoxelShape) iterator.next()).a(enumdirection_enumaxis, axisalignedbb, d0)) {
            if (Math.abs(d0) < 1.0E-7D) {
                return 0.0D;
            }
        }

        return d0;
    }

    public static double a(EnumDirection.EnumAxis enumdirection_enumaxis, AxisAlignedBB axisalignedbb, IWorldReader iworldreader, double d0, VoxelShapeCollision voxelshapecollision, Stream<VoxelShape> stream) {
        return a(axisalignedbb, iworldreader, d0, voxelshapecollision, EnumAxisCycle.a(enumdirection_enumaxis, EnumDirection.EnumAxis.Z), stream);
    }

    private static double a(AxisAlignedBB axisalignedbb, IWorldReader iworldreader, double d0, VoxelShapeCollision voxelshapecollision, EnumAxisCycle enumaxiscycle, Stream<VoxelShape> stream) {
        if (axisalignedbb.b() >= 1.0E-6D && axisalignedbb.c() >= 1.0E-6D && axisalignedbb.d() >= 1.0E-6D) {
            if (Math.abs(d0) < 1.0E-7D) {
                return 0.0D;
            } else {
                EnumAxisCycle enumaxiscycle1 = enumaxiscycle.a();
                EnumDirection.EnumAxis enumdirection_enumaxis = enumaxiscycle1.a(EnumDirection.EnumAxis.X);
                EnumDirection.EnumAxis enumdirection_enumaxis1 = enumaxiscycle1.a(EnumDirection.EnumAxis.Y);
                EnumDirection.EnumAxis enumdirection_enumaxis2 = enumaxiscycle1.a(EnumDirection.EnumAxis.Z);
                BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
                int i = MathHelper.floor(axisalignedbb.a(enumdirection_enumaxis) - 1.0E-7D) - 1;
                int j = MathHelper.floor(axisalignedbb.b(enumdirection_enumaxis) + 1.0E-7D) + 1;
                int k = MathHelper.floor(axisalignedbb.a(enumdirection_enumaxis1) - 1.0E-7D) - 1;
                int l = MathHelper.floor(axisalignedbb.b(enumdirection_enumaxis1) + 1.0E-7D) + 1;
                double d1 = axisalignedbb.a(enumdirection_enumaxis2) - 1.0E-7D;
                double d2 = axisalignedbb.b(enumdirection_enumaxis2) + 1.0E-7D;
                boolean flag = d0 > 0.0D;
                int i1 = flag ? MathHelper.floor(axisalignedbb.b(enumdirection_enumaxis2) - 1.0E-7D) - 1 : MathHelper.floor(axisalignedbb.a(enumdirection_enumaxis2) + 1.0E-7D) + 1;
                int j1 = a(d0, d1, d2);
                int k1 = flag ? 1 : -1;
                int l1 = i1;

                while (true) {
                    if (flag) {
                        if (l1 > j1) {
                            break;
                        }
                    } else if (l1 < j1) {
                        break;
                    }

                    for (int i2 = i; i2 <= j; ++i2) {
                        for (int j2 = k; j2 <= l; ++j2) {
                            int k2 = 0;

                            if (i2 == i || i2 == j) {
                                ++k2;
                            }

                            if (j2 == k || j2 == l) {
                                ++k2;
                            }

                            if (l1 == i1 || l1 == j1) {
                                ++k2;
                            }

                            if (k2 < 3) {
                                blockposition_mutableblockposition.a(enumaxiscycle1, i2, j2, l1);
                                IBlockData iblockdata = iworldreader.getType(blockposition_mutableblockposition);

                                if ((k2 != 1 || iblockdata.d()) && (k2 != 2 || iblockdata.a(Blocks.MOVING_PISTON))) {
                                    d0 = iblockdata.b((IBlockAccess) iworldreader, blockposition_mutableblockposition, voxelshapecollision).a(enumdirection_enumaxis2, axisalignedbb.d((double) (-blockposition_mutableblockposition.getX()), (double) (-blockposition_mutableblockposition.getY()), (double) (-blockposition_mutableblockposition.getZ())), d0);
                                    if (Math.abs(d0) < 1.0E-7D) {
                                        return 0.0D;
                                    }

                                    j1 = a(d0, d1, d2);
                                }
                            }
                        }
                    }

                    l1 += k1;
                }

                double[] adouble = new double[]{d0};

                stream.forEach((voxelshape) -> {
                    adouble[0] = voxelshape.a(enumdirection_enumaxis2, axisalignedbb, adouble[0]);
                });
                return adouble[0];
            }
        } else {
            return d0;
        }
    }

    private static int a(double d0, double d1, double d2) {
        return d0 > 0.0D ? MathHelper.floor(d2 + d0) + 1 : MathHelper.floor(d1 + d0) - 1;
    }

    public static VoxelShape a(VoxelShape voxelshape, EnumDirection enumdirection) {
        if (voxelshape == b()) {
            return b();
        } else {
            EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
            boolean flag;
            int i;

            if (enumdirection.e() == EnumDirection.EnumAxisDirection.POSITIVE) {
                flag = DoubleMath.fuzzyEquals(voxelshape.c(enumdirection_enumaxis), 1.0D, 1.0E-7D);
                i = voxelshape.a.c(enumdirection_enumaxis) - 1;
            } else {
                flag = DoubleMath.fuzzyEquals(voxelshape.b(enumdirection_enumaxis), 0.0D, 1.0E-7D);
                i = 0;
            }

            return (VoxelShape) (!flag ? a() : new VoxelShapeSlice(voxelshape, enumdirection_enumaxis, i));
        }
    }

    public static boolean b(VoxelShape voxelshape, VoxelShape voxelshape1, EnumDirection enumdirection) {
        if (voxelshape != b() && voxelshape1 != b()) {
            EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
            EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection = enumdirection.e();
            VoxelShape voxelshape2 = enumdirection_enumaxisdirection == EnumDirection.EnumAxisDirection.POSITIVE ? voxelshape : voxelshape1;
            VoxelShape voxelshape3 = enumdirection_enumaxisdirection == EnumDirection.EnumAxisDirection.POSITIVE ? voxelshape1 : voxelshape;

            if (!DoubleMath.fuzzyEquals(voxelshape2.c(enumdirection_enumaxis), 1.0D, 1.0E-7D)) {
                voxelshape2 = a();
            }

            if (!DoubleMath.fuzzyEquals(voxelshape3.b(enumdirection_enumaxis), 0.0D, 1.0E-7D)) {
                voxelshape3 = a();
            }

            return !c(b(), b(new VoxelShapeSlice(voxelshape2, enumdirection_enumaxis, voxelshape2.a.c(enumdirection_enumaxis) - 1), new VoxelShapeSlice(voxelshape3, enumdirection_enumaxis, 0), OperatorBoolean.OR), OperatorBoolean.ONLY_FIRST);
        } else {
            return true;
        }
    }

    public static boolean b(VoxelShape voxelshape, VoxelShape voxelshape1) {
        return voxelshape != b() && voxelshape1 != b() ? (voxelshape.isEmpty() && voxelshape1.isEmpty() ? false : !c(b(), b(voxelshape, voxelshape1, OperatorBoolean.OR), OperatorBoolean.ONLY_FIRST)) : true;
    }

    @VisibleForTesting
    protected static VoxelShapeMerger a(int i, DoubleList doublelist, DoubleList doublelist1, boolean flag, boolean flag1) {
        int j = doublelist.size() - 1;
        int k = doublelist1.size() - 1;

        if (doublelist instanceof VoxelShapeCubePoint && doublelist1 instanceof VoxelShapeCubePoint) {
            long l = a(j, k);

            if ((long) i * l <= 256L) {
                return new VoxelShapeCubeMerger(j, k);
            }
        }

        return (VoxelShapeMerger) (doublelist.getDouble(j) < doublelist1.getDouble(0) - 1.0E-7D ? new VoxelShapeMergerDisjoint(doublelist, doublelist1, false) : (doublelist1.getDouble(k) < doublelist.getDouble(0) - 1.0E-7D ? new VoxelShapeMergerDisjoint(doublelist1, doublelist, true) : (j == k && Objects.equals(doublelist, doublelist1) ? (doublelist instanceof VoxelShapeMergerIdentical ? (VoxelShapeMerger) doublelist : (doublelist1 instanceof VoxelShapeMergerIdentical ? (VoxelShapeMerger) doublelist1 : new VoxelShapeMergerIdentical(doublelist))) : new VoxelShapeMergerList(doublelist, doublelist1, flag, flag1))));
    }

    public interface a {

        void consume(double d0, double d1, double d2, double d3, double d4, double d5);
    }
}
