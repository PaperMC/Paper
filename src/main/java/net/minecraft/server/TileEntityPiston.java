package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityPiston extends TileEntity {

    private int a;
    private int b;
    private int c;
    private boolean d;
    private boolean e;
    private float f;
    private float g;
    private List h = new ArrayList();

    public TileEntityPiston() {}

    public TileEntityPiston(int i, int j, int k, boolean flag, boolean flag1) {
        this.a = i;
        this.b = j;
        this.c = k;
        this.d = flag;
        this.e = flag1;
    }

    public int a() {
        return this.a;
    }

    public int p() {
        return this.b;
    }

    public boolean b() {
        return this.d;
    }

    public int c() {
        return this.c;
    }

    public float a(float f) {
        if (f > 1.0F) {
            f = 1.0F;
        }

        return this.g + (this.f - this.g) * f;
    }

    private void a(float f, float f1) {
        if (this.d) {
            f = 1.0F - f;
        } else {
            --f;
        }

        AxisAlignedBB axisalignedbb = Block.PISTON_MOVING.b(this.world, this.x, this.y, this.z, this.a, f, this.c);

        if (axisalignedbb != null) {
            List list = this.world.getEntities((Entity) null, axisalignedbb);

            if (!list.isEmpty()) {
                this.h.addAll(list);
                Iterator iterator = this.h.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.move((double) (f1 * (float) Facing.b[this.c]), (double) (f1 * (float) Facing.c[this.c]), (double) (f1 * (float) Facing.d[this.c]));
                }

                this.h.clear();
            }
        }
    }

    public void f() {
        if (this.g < 1.0F && this.world != null) {
            this.g = this.f = 1.0F;
            this.world.s(this.x, this.y, this.z);
            this.w_();
            if (this.world.getTypeId(this.x, this.y, this.z) == Block.PISTON_MOVING.id) {
                this.world.setTypeIdAndData(this.x, this.y, this.z, this.a, this.b, 3);
                this.world.g(this.x, this.y, this.z, this.a);
            }
        }
    }

    public void h() {
        if (this.world == null) return; // CraftBukkit

        this.g = this.f;
        if (this.g >= 1.0F) {
            this.a(1.0F, 0.25F);
            this.world.s(this.x, this.y, this.z);
            this.w_();
            if (this.world.getTypeId(this.x, this.y, this.z) == Block.PISTON_MOVING.id) {
                this.world.setTypeIdAndData(this.x, this.y, this.z, this.a, this.b, 3);
                this.world.g(this.x, this.y, this.z, this.a);
            }
        } else {
            this.f += 0.5F;
            if (this.f >= 1.0F) {
                this.f = 1.0F;
            }

            if (this.d) {
                this.a(this.f, this.f - this.g + 0.0625F);
            }
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getInt("blockId");
        this.b = nbttagcompound.getInt("blockData");
        this.c = nbttagcompound.getInt("facing");
        this.g = this.f = nbttagcompound.getFloat("progress");
        this.d = nbttagcompound.getBoolean("extending");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("blockId", this.a);
        nbttagcompound.setInt("blockData", this.b);
        nbttagcompound.setInt("facing", this.c);
        nbttagcompound.setFloat("progress", this.g);
        nbttagcompound.setBoolean("extending", this.d);
    }
}
