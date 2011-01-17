package net.minecraft.server;

import java.util.Random;

public class EntityCreeper extends EntityMobs {

    int a;
    int b;

    public EntityCreeper(World world) {
        super(world);
        aP = "/mob/creeper.png";
    }

    protected void a() {
        super.a();
        af.a(16, ((Byte.valueOf((byte) -1))));
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void b_() {
        b = a;
        if (l.z) {
            int i = K();

            if (i > 0 && a == 0) {
                l.a(((Entity) (this)), "random.fuse", 1.0F, 0.5F);
            }
            a += i;
            if (a < 0) {
                a = 0;
            }
            if (a >= 30) {
                a = 30;
            }
        }
        super.b_();
    }

    protected String f() {
        return "mob.creeper";
    }

    protected String g() {
        return "mob.creeperdeath";
    }

    public void f(Entity entity) {
        super.f(entity);
        if (entity instanceof EntitySkeleton) {
            a(Item.aY.ba + W.nextInt(2), 1);
        }
    }

    protected void a(Entity entity, float f1) {
        int i = K();

        if (i <= 0 && f1 < 3F || i > 0 && f1 < 7F) {
            if (a == 0) {
                l.a(((Entity) (this)), "random.fuse", 1.0F, 0.5F);
            }
            a(1);
            a++;
            if (a >= 30) {
                l.a(((Entity) (this)), p, q, r, 3F);
                q();
            }
            e = true;
        } else {
            a(-1);
            a--;
            if (a < 0) {
                a = 0;
            }
        }
    }

    protected int h() {
        return Item.K.ba;
    }

    private int K() {
        return ((int) (af.a(16)));
    }

    private void a(int i) {
        af.b(16, ((Byte.valueOf((byte) i))));
    }
}
