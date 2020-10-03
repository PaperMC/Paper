package net.minecraft.server;

import java.util.Iterator;
import java.util.Optional;
import javax.annotation.Nullable;

public class AxisAlignedBB {

    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.minX = Math.min(d0, d3);
        this.minY = Math.min(d1, d4);
        this.minZ = Math.min(d2, d5);
        this.maxX = Math.max(d0, d3);
        this.maxY = Math.max(d1, d4);
        this.maxZ = Math.max(d2, d5);
    }

    public AxisAlignedBB(BlockPosition blockposition) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1));
    }

    public AxisAlignedBB(BlockPosition blockposition, BlockPosition blockposition1) {
        this((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
    }

    public AxisAlignedBB(Vec3D vec3d, Vec3D vec3d1) {
        this(vec3d.x, vec3d.y, vec3d.z, vec3d1.x, vec3d1.y, vec3d1.z);
    }

    public static AxisAlignedBB a(StructureBoundingBox structureboundingbox) {
        return new AxisAlignedBB((double) structureboundingbox.a, (double) structureboundingbox.b, (double) structureboundingbox.c, (double) (structureboundingbox.d + 1), (double) (structureboundingbox.e + 1), (double) (structureboundingbox.f + 1));
    }

    public static AxisAlignedBB a(Vec3D vec3d) {
        return new AxisAlignedBB(vec3d.x, vec3d.y, vec3d.z, vec3d.x + 1.0D, vec3d.y + 1.0D, vec3d.z + 1.0D);
    }

    public double a(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis.a(this.minX, this.minY, this.minZ);
    }

    public double b(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis.a(this.maxX, this.maxY, this.maxZ);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof AxisAlignedBB)) {
            return false;
        } else {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) object;

            return Double.compare(axisalignedbb.minX, this.minX) != 0 ? false : (Double.compare(axisalignedbb.minY, this.minY) != 0 ? false : (Double.compare(axisalignedbb.minZ, this.minZ) != 0 ? false : (Double.compare(axisalignedbb.maxX, this.maxX) != 0 ? false : (Double.compare(axisalignedbb.maxY, this.maxY) != 0 ? false : Double.compare(axisalignedbb.maxZ, this.maxZ) == 0))));
        }
    }

    public int hashCode() {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int) (i ^ i >>> 32);

        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int) (i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int) (i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB a(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

        if (d0 < 0.0D) {
            d3 -= d0;
        } else if (d0 > 0.0D) {
            d6 -= d0;
        }

        if (d1 < 0.0D) {
            d4 -= d1;
        } else if (d1 > 0.0D) {
            d7 -= d1;
        }

        if (d2 < 0.0D) {
            d5 -= d2;
        } else if (d2 > 0.0D) {
            d8 -= d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB b(Vec3D vec3d) {
        return this.b(vec3d.x, vec3d.y, vec3d.z);
    }

    public AxisAlignedBB b(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

        if (d0 < 0.0D) {
            d3 += d0;
        } else if (d0 > 0.0D) {
            d6 += d0;
        }

        if (d1 < 0.0D) {
            d4 += d1;
        } else if (d1 > 0.0D) {
            d7 += d1;
        }

        if (d2 < 0.0D) {
            d5 += d2;
        } else if (d2 > 0.0D) {
            d8 += d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB grow(double d0, double d1, double d2) {
        double d3 = this.minX - d0;
        double d4 = this.minY - d1;
        double d5 = this.minZ - d2;
        double d6 = this.maxX + d0;
        double d7 = this.maxY + d1;
        double d8 = this.maxZ + d2;

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB g(double d0) {
        return this.grow(d0, d0, d0);
    }

    public AxisAlignedBB a(AxisAlignedBB axisalignedbb) {
        double d0 = Math.max(this.minX, axisalignedbb.minX);
        double d1 = Math.max(this.minY, axisalignedbb.minY);
        double d2 = Math.max(this.minZ, axisalignedbb.minZ);
        double d3 = Math.min(this.maxX, axisalignedbb.maxX);
        double d4 = Math.min(this.maxY, axisalignedbb.maxY);
        double d5 = Math.min(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB b(AxisAlignedBB axisalignedbb) {
        double d0 = Math.min(this.minX, axisalignedbb.minX);
        double d1 = Math.min(this.minY, axisalignedbb.minY);
        double d2 = Math.min(this.minZ, axisalignedbb.minZ);
        double d3 = Math.max(this.maxX, axisalignedbb.maxX);
        double d4 = Math.max(this.maxY, axisalignedbb.maxY);
        double d5 = Math.max(this.maxZ, axisalignedbb.maxZ);

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB d(double d0, double d1, double d2) {
        return new AxisAlignedBB(this.minX + d0, this.minY + d1, this.minZ + d2, this.maxX + d0, this.maxY + d1, this.maxZ + d2);
    }

    public AxisAlignedBB a(BlockPosition blockposition) {
        return new AxisAlignedBB(this.minX + (double) blockposition.getX(), this.minY + (double) blockposition.getY(), this.minZ + (double) blockposition.getZ(), this.maxX + (double) blockposition.getX(), this.maxY + (double) blockposition.getY(), this.maxZ + (double) blockposition.getZ());
    }

    public AxisAlignedBB c(Vec3D vec3d) {
        return this.d(vec3d.x, vec3d.y, vec3d.z);
    }

    public boolean c(AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    }

    public boolean a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return this.minX < d3 && this.maxX > d0 && this.minY < d4 && this.maxY > d1 && this.minZ < d5 && this.maxZ > d2;
    }

    public boolean d(Vec3D vec3d) {
        return this.e(vec3d.x, vec3d.y, vec3d.z);
    }

    public boolean e(double d0, double d1, double d2) {
        return d0 >= this.minX && d0 < this.maxX && d1 >= this.minY && d1 < this.maxY && d2 >= this.minZ && d2 < this.maxZ;
    }

    public double a() {
        double d0 = this.b();
        double d1 = this.c();
        double d2 = this.d();

        return (d0 + d1 + d2) / 3.0D;
    }

    public double b() {
        return this.maxX - this.minX;
    }

    public double c() {
        return this.maxY - this.minY;
    }

    public double d() {
        return this.maxZ - this.minZ;
    }

    public AxisAlignedBB shrink(double d0) {
        return this.g(-d0);
    }

    public Optional<Vec3D> b(Vec3D vec3d, Vec3D vec3d1) {
        double[] adouble = new double[]{1.0D};
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.y - vec3d.y;
        double d2 = vec3d1.z - vec3d.z;
        EnumDirection enumdirection = a(this, vec3d, adouble, (EnumDirection) null, d0, d1, d2);

        if (enumdirection == null) {
            return Optional.empty();
        } else {
            double d3 = adouble[0];

            return Optional.of(vec3d.add(d3 * d0, d3 * d1, d3 * d2));
        }
    }

    @Nullable
    public static MovingObjectPositionBlock a(Iterable<AxisAlignedBB> iterable, Vec3D vec3d, Vec3D vec3d1, BlockPosition blockposition) {
        double[] adouble = new double[]{1.0D};
        EnumDirection enumdirection = null;
        double d0 = vec3d1.x - vec3d.x;
        double d1 = vec3d1.y - vec3d.y;
        double d2 = vec3d1.z - vec3d.z;

        AxisAlignedBB axisalignedbb;

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); enumdirection = a(axisalignedbb.a(blockposition), vec3d, adouble, enumdirection, d0, d1, d2)) {
            axisalignedbb = (AxisAlignedBB) iterator.next();
        }

        if (enumdirection == null) {
            return null;
        } else {
            double d3 = adouble[0];

            return new MovingObjectPositionBlock(vec3d.add(d3 * d0, d3 * d1, d3 * d2), enumdirection, blockposition, false);
        }
    }

    @Nullable
    private static EnumDirection a(AxisAlignedBB axisalignedbb, Vec3D vec3d, double[] adouble, @Nullable EnumDirection enumdirection, double d0, double d1, double d2) {
        if (d0 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d0, d1, d2, axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, EnumDirection.WEST, vec3d.x, vec3d.y, vec3d.z);
        } else if (d0 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d0, d1, d2, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, EnumDirection.EAST, vec3d.x, vec3d.y, vec3d.z);
        }

        if (d1 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d1, d2, d0, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, EnumDirection.DOWN, vec3d.y, vec3d.z, vec3d.x);
        } else if (d1 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d1, d2, d0, axisalignedbb.maxY, axisalignedbb.minZ, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, EnumDirection.UP, vec3d.y, vec3d.z, vec3d.x);
        }

        if (d2 > 1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d2, d0, d1, axisalignedbb.minZ, axisalignedbb.minX, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, EnumDirection.NORTH, vec3d.z, vec3d.x, vec3d.y);
        } else if (d2 < -1.0E-7D) {
            enumdirection = a(adouble, enumdirection, d2, d0, d1, axisalignedbb.maxZ, axisalignedbb.minX, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxY, EnumDirection.SOUTH, vec3d.z, vec3d.x, vec3d.y);
        }

        return enumdirection;
    }

    @Nullable
    private static EnumDirection a(double[] adouble, @Nullable EnumDirection enumdirection, double d0, double d1, double d2, double d3, double d4, double d5, double d6, double d7, EnumDirection enumdirection1, double d8, double d9, double d10) {
        double d11 = (d3 - d8) / d0;
        double d12 = d9 + d11 * d1;
        double d13 = d10 + d11 * d2;

        if (0.0D < d11 && d11 < adouble[0] && d4 - 1.0E-7D < d12 && d12 < d5 + 1.0E-7D && d6 - 1.0E-7D < d13 && d13 < d7 + 1.0E-7D) {
            adouble[0] = d11;
            return enumdirection1;
        } else {
            return enumdirection;
        }
    }

    public String toString() {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public Vec3D f() {
        return new Vec3D(MathHelper.d(0.5D, this.minX, this.maxX), MathHelper.d(0.5D, this.minY, this.maxY), MathHelper.d(0.5D, this.minZ, this.maxZ));
    }

    public static AxisAlignedBB g(double d0, double d1, double d2) {
        return new AxisAlignedBB(-d0 / 2.0D, -d1 / 2.0D, -d2 / 2.0D, d0 / 2.0D, d1 / 2.0D, d2 / 2.0D);
    }
}
