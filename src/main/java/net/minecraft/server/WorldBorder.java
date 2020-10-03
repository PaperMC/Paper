package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.serialization.DynamicLike;
import java.util.Iterator;
import java.util.List;

public class WorldBorder {

    private final List<IWorldBorderListener> a = Lists.newArrayList();
    private double b = 0.2D;
    private double d = 5.0D;
    private int e = 15;
    private int f = 5;
    private double g;
    private double h;
    private int i = 29999984;
    private WorldBorder.a j = new WorldBorder.d(6.0E7D);
    public static final WorldBorder.c c = new WorldBorder.c(0.0D, 0.0D, 0.2D, 5.0D, 5, 15, 6.0E7D, 0L, 0.0D);
    public WorldServer world; // CraftBukkit

    public WorldBorder() {}

    public boolean a(BlockPosition blockposition) {
        return (double) (blockposition.getX() + 1) > this.e() && (double) blockposition.getX() < this.g() && (double) (blockposition.getZ() + 1) > this.f() && (double) blockposition.getZ() < this.h();
    }

    public boolean isInBounds(ChunkCoordIntPair chunkcoordintpair) {
        return (double) chunkcoordintpair.f() > this.e() && (double) chunkcoordintpair.d() < this.g() && (double) chunkcoordintpair.g() > this.f() && (double) chunkcoordintpair.e() < this.h();
    }

    public boolean a(AxisAlignedBB axisalignedbb) {
        return axisalignedbb.maxX > this.e() && axisalignedbb.minX < this.g() && axisalignedbb.maxZ > this.f() && axisalignedbb.minZ < this.h();
    }

    public double a(Entity entity) {
        return this.b(entity.locX(), entity.locZ());
    }

    public VoxelShape c() {
        return this.j.m();
    }

    public double b(double d0, double d1) {
        double d2 = d1 - this.f();
        double d3 = this.h() - d1;
        double d4 = d0 - this.e();
        double d5 = this.g() - d0;
        double d6 = Math.min(d4, d5);

        d6 = Math.min(d6, d2);
        return Math.min(d6, d3);
    }

    public double e() {
        return this.j.a();
    }

    public double f() {
        return this.j.c();
    }

    public double g() {
        return this.j.b();
    }

    public double h() {
        return this.j.d();
    }

    public double getCenterX() {
        return this.g;
    }

    public double getCenterZ() {
        return this.h;
    }

    public void setCenter(double d0, double d1) {
        this.g = d0;
        this.h = d1;
        this.j.k();
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.a(this, d0, d1);
        }

    }

    public double getSize() {
        return this.j.e();
    }

    public long j() {
        return this.j.g();
    }

    public double k() {
        return this.j.h();
    }

    public void setSize(double d0) {
        this.j = new WorldBorder.d(d0);
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.a(this, d0);
        }

    }

    public void transitionSizeBetween(double d0, double d1, long i) {
        this.j = (WorldBorder.a) (d0 == d1 ? new WorldBorder.d(d1) : new WorldBorder.b(d0, d1, i));
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.a(this, d0, d1, i);
        }

    }

    protected List<IWorldBorderListener> l() {
        return Lists.newArrayList(this.a);
    }

    public void a(IWorldBorderListener iworldborderlistener) {
        if (a.contains(iworldborderlistener)) return; // CraftBukkit
        this.a.add(iworldborderlistener);
    }

    public void a(int i) {
        this.i = i;
        this.j.j();
    }

    public int m() {
        return this.i;
    }

    public double getDamageBuffer() {
        return this.d;
    }

    public void setDamageBuffer(double d0) {
        this.d = d0;
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.c(this, d0);
        }

    }

    public double getDamageAmount() {
        return this.b;
    }

    public void setDamageAmount(double d0) {
        this.b = d0;
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.b(this, d0);
        }

    }

    public int getWarningTime() {
        return this.e;
    }

    public void setWarningTime(int i) {
        this.e = i;
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.a(this, i);
        }

    }

    public int getWarningDistance() {
        return this.f;
    }

    public void setWarningDistance(int i) {
        this.f = i;
        Iterator iterator = this.l().iterator();

        while (iterator.hasNext()) {
            IWorldBorderListener iworldborderlistener = (IWorldBorderListener) iterator.next();

            iworldborderlistener.b(this, i);
        }

    }

    public void s() {
        this.j = this.j.l();
    }

    public WorldBorder.c t() {
        return new WorldBorder.c(this);
    }

    public void a(WorldBorder.c worldborder_c) {
        this.setCenter(worldborder_c.a(), worldborder_c.b());
        this.setDamageAmount(worldborder_c.c());
        this.setDamageBuffer(worldborder_c.d());
        this.setWarningDistance(worldborder_c.e());
        this.setWarningTime(worldborder_c.f());
        if (worldborder_c.h() > 0L) {
            this.transitionSizeBetween(worldborder_c.g(), worldborder_c.i(), worldborder_c.h());
        } else {
            this.setSize(worldborder_c.g());
        }

    }

    public static class c {

        private final double a;
        private final double b;
        private final double c;
        private final double d;
        private final int e;
        private final int f;
        private final double g;
        private final long h;
        private final double i;

        private c(double d0, double d1, double d2, double d3, int i, int j, double d4, long k, double d5) {
            this.a = d0;
            this.b = d1;
            this.c = d2;
            this.d = d3;
            this.e = i;
            this.f = j;
            this.g = d4;
            this.h = k;
            this.i = d5;
        }

        private c(WorldBorder worldborder) {
            this.a = worldborder.getCenterX();
            this.b = worldborder.getCenterZ();
            this.c = worldborder.getDamageAmount();
            this.d = worldborder.getDamageBuffer();
            this.e = worldborder.getWarningDistance();
            this.f = worldborder.getWarningTime();
            this.g = worldborder.getSize();
            this.h = worldborder.j();
            this.i = worldborder.k();
        }

        public double a() {
            return this.a;
        }

        public double b() {
            return this.b;
        }

        public double c() {
            return this.c;
        }

        public double d() {
            return this.d;
        }

        public int e() {
            return this.e;
        }

        public int f() {
            return this.f;
        }

        public double g() {
            return this.g;
        }

        public long h() {
            return this.h;
        }

        public double i() {
            return this.i;
        }

        public static WorldBorder.c a(DynamicLike<?> dynamiclike, WorldBorder.c worldborder_c) {
            double d0 = dynamiclike.get("BorderCenterX").asDouble(worldborder_c.a);
            double d1 = dynamiclike.get("BorderCenterZ").asDouble(worldborder_c.b);
            double d2 = dynamiclike.get("BorderSize").asDouble(worldborder_c.g);
            long i = dynamiclike.get("BorderSizeLerpTime").asLong(worldborder_c.h);
            double d3 = dynamiclike.get("BorderSizeLerpTarget").asDouble(worldborder_c.i);
            double d4 = dynamiclike.get("BorderSafeZone").asDouble(worldborder_c.d);
            double d5 = dynamiclike.get("BorderDamagePerBlock").asDouble(worldborder_c.c);
            int j = dynamiclike.get("BorderWarningBlocks").asInt(worldborder_c.e);
            int k = dynamiclike.get("BorderWarningTime").asInt(worldborder_c.f);

            return new WorldBorder.c(d0, d1, d5, d4, j, k, d2, i, d3);
        }

        public void a(NBTTagCompound nbttagcompound) {
            nbttagcompound.setDouble("BorderCenterX", this.a);
            nbttagcompound.setDouble("BorderCenterZ", this.b);
            nbttagcompound.setDouble("BorderSize", this.g);
            nbttagcompound.setLong("BorderSizeLerpTime", this.h);
            nbttagcompound.setDouble("BorderSafeZone", this.d);
            nbttagcompound.setDouble("BorderDamagePerBlock", this.c);
            nbttagcompound.setDouble("BorderSizeLerpTarget", this.i);
            nbttagcompound.setDouble("BorderWarningBlocks", (double) this.e);
            nbttagcompound.setDouble("BorderWarningTime", (double) this.f);
        }
    }

    class d implements WorldBorder.a {

        private final double b;
        private double c;
        private double d;
        private double e;
        private double f;
        private VoxelShape g;

        public d(double d0) {
            this.b = d0;
            this.n();
        }

        @Override
        public double a() {
            return this.c;
        }

        @Override
        public double b() {
            return this.e;
        }

        @Override
        public double c() {
            return this.d;
        }

        @Override
        public double d() {
            return this.f;
        }

        @Override
        public double e() {
            return this.b;
        }

        @Override
        public long g() {
            return 0L;
        }

        @Override
        public double h() {
            return this.b;
        }

        private void n() {
            this.c = Math.max(WorldBorder.this.getCenterX() - this.b / 2.0D, (double) (-WorldBorder.this.i));
            this.d = Math.max(WorldBorder.this.getCenterZ() - this.b / 2.0D, (double) (-WorldBorder.this.i));
            this.e = Math.min(WorldBorder.this.getCenterX() + this.b / 2.0D, (double) WorldBorder.this.i);
            this.f = Math.min(WorldBorder.this.getCenterZ() + this.b / 2.0D, (double) WorldBorder.this.i);
            this.g = VoxelShapes.a(VoxelShapes.a, VoxelShapes.create(Math.floor(this.a()), Double.NEGATIVE_INFINITY, Math.floor(this.c()), Math.ceil(this.b()), Double.POSITIVE_INFINITY, Math.ceil(this.d())), OperatorBoolean.ONLY_FIRST);
        }

        @Override
        public void j() {
            this.n();
        }

        @Override
        public void k() {
            this.n();
        }

        @Override
        public WorldBorder.a l() {
            return this;
        }

        @Override
        public VoxelShape m() {
            return this.g;
        }
    }

    class b implements WorldBorder.a {

        private final double b;
        private final double c;
        private final long d;
        private final long e;
        private final double f;

        private b(double d0, double d1, long i) {
            this.b = d0;
            this.c = d1;
            this.f = (double) i;
            this.e = SystemUtils.getMonotonicMillis();
            this.d = this.e + i;
        }

        @Override
        public double a() {
            return Math.max(WorldBorder.this.getCenterX() - this.e() / 2.0D, (double) (-WorldBorder.this.i));
        }

        @Override
        public double c() {
            return Math.max(WorldBorder.this.getCenterZ() - this.e() / 2.0D, (double) (-WorldBorder.this.i));
        }

        @Override
        public double b() {
            return Math.min(WorldBorder.this.getCenterX() + this.e() / 2.0D, (double) WorldBorder.this.i);
        }

        @Override
        public double d() {
            return Math.min(WorldBorder.this.getCenterZ() + this.e() / 2.0D, (double) WorldBorder.this.i);
        }

        @Override
        public double e() {
            double d0 = (double) (SystemUtils.getMonotonicMillis() - this.e) / this.f;

            return d0 < 1.0D ? MathHelper.d(d0, this.b, this.c) : this.c;
        }

        @Override
        public long g() {
            return this.d - SystemUtils.getMonotonicMillis();
        }

        @Override
        public double h() {
            return this.c;
        }

        @Override
        public void k() {}

        @Override
        public void j() {}

        @Override
        public WorldBorder.a l() {
            return (WorldBorder.a) (this.g() <= 0L ? WorldBorder.this.new d(this.c) : this);
        }

        @Override
        public VoxelShape m() {
            return VoxelShapes.a(VoxelShapes.a, VoxelShapes.create(Math.floor(this.a()), Double.NEGATIVE_INFINITY, Math.floor(this.c()), Math.ceil(this.b()), Double.POSITIVE_INFINITY, Math.ceil(this.d())), OperatorBoolean.ONLY_FIRST);
        }
    }

    interface a {

        double a();

        double b();

        double c();

        double d();

        double e();

        long g();

        double h();

        void j();

        void k();

        WorldBorder.a l();

        VoxelShape m();
    }
}
