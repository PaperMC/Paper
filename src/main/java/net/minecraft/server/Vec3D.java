package net.minecraft.server;

public class Vec3D {

    private static final ThreadLocal d = new Vec3DPoolThreadLocal();
    public double a;
    public double b;
    public double c;

    public static Vec3D a(double d0, double d1, double d2) {
        return new Vec3D(d0, d1, d2);
    }

    public static Vec3DPool a() {
        return (Vec3DPool) d.get();
    }

    protected Vec3D(double d0, double d1, double d2) {
        if (d0 == -0.0D) {
            d0 = 0.0D;
        }

        if (d1 == -0.0D) {
            d1 = 0.0D;
        }

        if (d2 == -0.0D) {
            d2 = 0.0D;
        }

        this.a = d0;
        this.b = d1;
        this.c = d2;
    }

    protected Vec3D b(double d0, double d1, double d2) {
        this.a = d0;
        this.b = d1;
        this.c = d2;
        return this;
    }

    public Vec3D b() {
        double d0 = (double) MathHelper.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);

        return d0 < 1.0E-4D ? a().create(0.0D, 0.0D, 0.0D) : a().create(this.a / d0, this.b / d0, this.c / d0);
    }

    public double b(Vec3D vec3d) {
        return this.a * vec3d.a + this.b * vec3d.b + this.c * vec3d.c;
    }

    public Vec3D add(double d0, double d1, double d2) {
        return a().create(this.a + d0, this.b + d1, this.c + d2);
    }

    public double d(Vec3D vec3d) {
        double d0 = vec3d.a - this.a;
        double d1 = vec3d.b - this.b;
        double d2 = vec3d.c - this.c;

        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double distanceSquared(Vec3D vec3d) {
        double d0 = vec3d.a - this.a;
        double d1 = vec3d.b - this.b;
        double d2 = vec3d.c - this.c;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double d(double d0, double d1, double d2) {
        double d3 = d0 - this.a;
        double d4 = d1 - this.b;
        double d5 = d2 - this.c;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double c() {
        return (double) MathHelper.sqrt(this.a * this.a + this.b * this.b + this.c * this.c);
    }

    public Vec3D b(Vec3D vec3d, double d0) {
        double d1 = vec3d.a - this.a;
        double d2 = vec3d.b - this.b;
        double d3 = vec3d.c - this.c;

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.a) / d1;

            return d4 >= 0.0D && d4 <= 1.0D ? a().create(this.a + d1 * d4, this.b + d2 * d4, this.c + d3 * d4) : null;
        }
    }

    public Vec3D c(Vec3D vec3d, double d0) {
        double d1 = vec3d.a - this.a;
        double d2 = vec3d.b - this.b;
        double d3 = vec3d.c - this.c;

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.b) / d2;

            return d4 >= 0.0D && d4 <= 1.0D ? a().create(this.a + d1 * d4, this.b + d2 * d4, this.c + d3 * d4) : null;
        }
    }

    public Vec3D d(Vec3D vec3d, double d0) {
        double d1 = vec3d.a - this.a;
        double d2 = vec3d.b - this.b;
        double d3 = vec3d.c - this.c;

        if (d3 * d3 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (d0 - this.c) / d3;

            return d4 >= 0.0D && d4 <= 1.0D ? a().create(this.a + d1 * d4, this.b + d2 * d4, this.c + d3 * d4) : null;
        }
    }

    public String toString() {
        return "(" + this.a + ", " + this.b + ", " + this.c + ")";
    }

    public void a(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.a;
        double d1 = this.b * (double) f1 + this.c * (double) f2;
        double d2 = this.c * (double) f1 - this.b * (double) f2;

        this.a = d0;
        this.b = d1;
        this.c = d2;
    }

    public void b(float f) {
        float f1 = MathHelper.cos(f);
        float f2 = MathHelper.sin(f);
        double d0 = this.a * (double) f1 + this.c * (double) f2;
        double d1 = this.b;
        double d2 = this.c * (double) f1 - this.a * (double) f2;

        this.a = d0;
        this.b = d1;
        this.c = d2;
    }
}
