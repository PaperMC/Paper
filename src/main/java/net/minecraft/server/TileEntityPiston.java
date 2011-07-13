package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityPiston extends TileEntity {

    private int a;
    private int b;
    private int c;
    private boolean i;
    private boolean j;
    private float k;
    private float l;
    private static List m = new ArrayList();

    public TileEntityPiston() {}

    public TileEntityPiston(int i, int j, int k, boolean flag, boolean flag1) {
        this.a = i;
        this.b = j;
        this.c = k;
        this.i = flag;
        this.j = flag1;
    }

    public int a() {
        return this.a;
    }

    public int e() {
        return this.b;
    }

    public boolean c() {
        return this.i;
    }

    public int d() {
        return this.c;
    }

    public float a(float f) {
        if (f > 1.0F) {
            f = 1.0F;
        }

        return this.l + (this.k - this.l) * f;
    }

    private void a(float f, float f1) {
        if (!this.i) {
            --f;
        } else {
            f = 1.0F - f;
        }

        AxisAlignedBB axisalignedbb = Block.PISTON_MOVING.a(this.world, this.x, this.y, this.z, this.a, f, this.c);

        if (axisalignedbb != null) {
            List list = this.world.b((Entity) null, axisalignedbb);

            if (!list.isEmpty()) {
                m.addAll(list);
                Iterator iterator = m.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.move((double) (f1 * (float) PistonBlockTextures.b[this.c]), (double) (f1 * (float) PistonBlockTextures.c[this.c]), (double) (f1 * (float) PistonBlockTextures.d[this.c]));
                }

                m.clear();
            }
        }
    }

    public void k() {
        if (this.l < 1.0F) {
            this.l = this.k = 1.0F;
            this.world.o(this.x, this.y, this.z);
            this.h();
            if (this.world.getTypeId(this.x, this.y, this.z) == Block.PISTON_MOVING.id) {
                this.world.setTypeIdAndData(this.x, this.y, this.z, this.a, this.b);
            }
        }
    }

    public void g_() {
        // CraftBukkit
        if (this.world == null) return;
        this.l = this.k;
        if (this.l >= 1.0F) {
            this.a(1.0F, 0.25F);
            this.world.o(this.x, this.y, this.z);
            this.h();
            if (this.world.getTypeId(this.x, this.y, this.z) == Block.PISTON_MOVING.id) {
                this.world.setTypeIdAndData(this.x, this.y, this.z, this.a, this.b);
            }
        } else {
            this.k += 0.5F;
            if (this.k >= 1.0F) {
                this.k = 1.0F;
            }

            if (this.i) {
                this.a(this.k, this.k - this.l + 0.0625F);
            }
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.e("blockId");
        this.b = nbttagcompound.e("blockData");
        this.c = nbttagcompound.e("facing");
        this.l = this.k = nbttagcompound.g("progress");
        this.i = nbttagcompound.m("extending");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("blockId", this.a);
        nbttagcompound.a("blockData", this.b);
        nbttagcompound.a("facing", this.c);
        nbttagcompound.a("progress", this.l);
        nbttagcompound.a("extending", this.i);
    }
}
