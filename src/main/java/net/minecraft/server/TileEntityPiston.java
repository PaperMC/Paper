package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityPiston extends TileEntity {

    private Block a;
    private int i;
    private int j;
    private boolean k;
    private boolean l;
    private float m;
    private float n;
    private List o = new ArrayList();

    public TileEntityPiston() {}

    public TileEntityPiston(Block block, int i, int j, boolean flag, boolean flag1) {
        this.a = block;
        this.i = i;
        this.j = j;
        this.k = flag;
        this.l = flag1;
    }

    public Block a() {
        return this.a;
    }

    public int p() {
        return this.i;
    }

    public boolean b() {
        return this.k;
    }

    public int c() {
        return this.j;
    }

    public float a(float f) {
        if (f > 1.0F) {
            f = 1.0F;
        }

        return this.n + (this.m - this.n) * f;
    }

    private void a(float f, float f1) {
        if (this.k) {
            f = 1.0F - f;
        } else {
            --f;
        }

        AxisAlignedBB axisalignedbb = Blocks.PISTON_MOVING.a(this.world, this.x, this.y, this.z, this.a, f, this.j);

        if (axisalignedbb != null) {
            List list = this.world.getEntities((Entity) null, axisalignedbb);

            if (!list.isEmpty()) {
                this.o.addAll(list);
                Iterator iterator = this.o.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.move((double) (f1 * (float) Facing.b[this.j]), (double) (f1 * (float) Facing.c[this.j]), (double) (f1 * (float) Facing.d[this.j]));
                }

                this.o.clear();
            }
        }
    }

    public void f() {
        if (this.n < 1.0F && this.world != null) {
            this.n = this.m = 1.0F;
            this.world.p(this.x, this.y, this.z);
            this.s();
            if (this.world.getType(this.x, this.y, this.z) == Blocks.PISTON_MOVING) {
                this.world.setTypeAndData(this.x, this.y, this.z, this.a, this.i, 3);
                this.world.e(this.x, this.y, this.z, this.a);
            }
        }
    }

    public void h() {
        if (this.world == null) return; // CraftBukkit

        this.n = this.m;
        if (this.n >= 1.0F) {
            this.a(1.0F, 0.25F);
            this.world.p(this.x, this.y, this.z);
            this.s();
            if (this.world.getType(this.x, this.y, this.z) == Blocks.PISTON_MOVING) {
                this.world.setTypeAndData(this.x, this.y, this.z, this.a, this.i, 3);
                this.world.e(this.x, this.y, this.z, this.a);
            }
        } else {
            this.m += 0.5F;
            if (this.m >= 1.0F) {
                this.m = 1.0F;
            }

            if (this.k) {
                this.a(this.m, this.m - this.n + 0.0625F);
            }
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = Block.e(nbttagcompound.getInt("blockId"));
        this.i = nbttagcompound.getInt("blockData");
        this.j = nbttagcompound.getInt("facing");
        this.n = this.m = nbttagcompound.getFloat("progress");
        this.k = nbttagcompound.getBoolean("extending");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("blockId", Block.b(this.a));
        nbttagcompound.setInt("blockData", this.i);
        nbttagcompound.setInt("facing", this.j);
        nbttagcompound.setFloat("progress", this.n);
        nbttagcompound.setBoolean("extending", this.k);
    }
}
