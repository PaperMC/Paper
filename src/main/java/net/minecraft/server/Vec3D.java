package net.minecraft.server;

public class Vec3D {

    public static final Vec3DPool a = new Vec3DPool(-1, -1);
    public final Vec3DPool b;
    public double c;
    public double d;
    public double e;
    public Vec3D next; // CraftBukkit

    public static Vec3D a(double d0, double d1, double d2) {
        return new Vec3D(a, d0, d1, d2);
    }

    protected Vec3D(Vec3DPool vec3dpool, double d0, double d1, double d2) {
        if (d0 == -0.0D) {
            d0 = 0.0D;
        }

        if (d1 == -0.0D) {
            d1 = 0.0D;
        }

        if (d2 == -0.0D) {
            d2 = 0.0D;
        }

        this.c = d0;
        this.d = d1;
        this.e = d2;
        this.b = vec3dpool;
    }

    protected Vec3D b(double d0, double d1, double d2) {
        this.c = d0;
        this.d = d1;
        this.e = d2;
        return this;
    }

    public Vec3D a() {
        double d0 = (double) MathHelper.sqrt(this.c * this.c + this.d * this.d + this.e * this.e);

        return d0 < 1.0E-4D ? this.b.create(0.0D, 0.0D, 0.0D) : this.b.create(this.c / d0, this.d / d0, this.e / d0);
    }

    public double b(Vec3D vec3d) {
        return this.c * vec3d.c + this.d * vec3d.d + this.e * vec3d.e;
    }

    public Vec3D add(double d0, double d1, double d2) {
        return this.b.create(this.c + d0, this.d + d1, this.e + d2);
    }

    public double d(Vec3D vec3d) {
        double d0 = vec3d.c - this.c;
        double d1 = vec3d.d - this.d;
        double d2 = vec3d.e - this.e;

        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double distanceSquared(Vec3D vec3d) {
        double d0 = vec3d.c - this.c;
        double d1 = vec3d.d - this.d;
        double d2 = vec3d.e - this.e;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double d(double d0, double d1, double d2) {
        double d3 = d0 - this.c;
        double d4 = d1 - this.d;
        double d5 = d2 - this.e;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double b() {
        return (double) MathHelper.sqrt(this.c * this.c + this.d * this.d + this.e * this.e);
    }

    public Vec3D b(Vec3D vec3d, double d0) {
        double d1 = vec3d.c - this.c;
        double d2 = vec3d.d - this.d;
        double d3 = vec3d.e - this.e;

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.c) / d1;

            return d4 >= 0.0D && d4 <= 1.0D ? this.b.create(this.c + d1 * d4, this.d + d2 * d4, this.e + d3 * d4) : null;
        }
    }

    public Vec3D c(Vec3D vec3d, double d0) {
        double d1 = vec3d.c - this.c;
        double d2 = vec3d.d - this.d;
        double d3 = vec3d.e - this.e;

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.d) / d2;

            return d4 >= 0.0D && d4 <= 1.0D ? this.b.create(this.c + d1 * d4, this.d + d2 * d4, this.e + d3 * d4) : null;
        }
    }

    public Vec3D d(Vec3D vec3d, double d0) {
        double d1 = vec3d.c - this.c;
        double d2 = vec3d.d - this.d;
        double d3 = vec3d.e - this.e;

        if (d3 * d3 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.e) / d3;

            return d4 >= 0.0D && d4 <= 1.0D ? this.b.create(this.c + d1 * d4, this.d + d2 * d4, this.e + d3 * d4) : null;
        }
    }

    public String toString() {
        return "(" + this.c + ", " + this.d + ", " + this.e + ")";
    }

    public void a(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.c;
        double d1 = this.d * (double) f1 + this.e * (double) f2;
        double d2 = this.e * (double) f1 - this.d * (double) f2;

        this.c = d0;
        this.d = d1;
        this.e = d2;
    }

    public void b(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.c * (double) f1 + this.e * (double) f2;
        double d1 = this.d;
        double d2 = this.e * (double) f1 - this.c * (double) f2;

        this.c = d0;
        this.d = d1;
        this.e = d2;
    }
}
