package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftItem;
// CraftBukkit end

public class EntityItem extends Entity {

    public ItemStack a;
    private int e;
    public int b;
    public int c;
    private int f;
    public float d;

    public EntityItem(World world, double d1, double d2, double d3, ItemStack itemstack) {
        // CraftBukkit start
        this(world);
        // CraftBukkit end
        a(d1, d2, d3);
        a = itemstack;
        v = (float) (Math.random() * 360D);
        s = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        t = 0.20000000298023224D;
        u = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        M = false;
        
    }

    public EntityItem(World world) {
        super(world);
        b = 0;
        f = 5;
        d = (float) (Math.random() * 3.1415926535897931D * 2D);
        a(0.25F, 0.25F);
        H = J / 2.0F;
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftItem(server, this);
        // CraftBukkit end
    }

    protected void a() {}

    public void b_() {
        super.b_();
        if (c > 0) {
            c--;
        }
        m = p;
        n = q;
        o = r;
        t -= 0.039999999105930328D;
        if (l.c(MathHelper.b(p), MathHelper.b(q), MathHelper.b(r)) == Material.g) {
            t = 0.20000000298023224D;
            s = (W.nextFloat() - W.nextFloat()) * 0.2F;
            u = (W.nextFloat() - W.nextFloat()) * 0.2F;
            l.a(((Entity) (this)), "random.fizz", 0.4F, 2.0F + W.nextFloat() * 0.4F);
        }
        g(p, q, r);
        c(s, t, u);
        float f1 = 0.98F;

        if (A) {
            f1 = 0.5880001F;
            int i = l.a(MathHelper.b(p), MathHelper.b(z.b) - 1, MathHelper.b(r));

            if (i > 0) {
                f1 = Block.m[i].bu * 0.98F;
            }
        }
        s *= f1;
        t *= 0.98000001907348633D;
        u *= f1;
        if (A) {
            t *= -0.5D;
        }
        e++;
        b++;
        if (b >= 6000) {
            q();
        }
    }

    public boolean v() {
        return l.a(z, Material.f, ((Entity) (this)));
    }

    private boolean g(double d1, double d2, double d3) {
        int i = MathHelper.b(d1);
        int j = MathHelper.b(d2);
        int k = MathHelper.b(d3);
        double d4 = d1 - (double) i;
        double d5 = d2 - (double) j;
        double d6 = d3 - (double) k;

        if (Block.o[l.a(i, j, k)]) {
            boolean flag = !Block.o[l.a(i - 1, j, k)];
            boolean flag1 = !Block.o[l.a(i + 1, j, k)];
            boolean flag2 = !Block.o[l.a(i, j - 1, k)];
            boolean flag3 = !Block.o[l.a(i, j + 1, k)];
            boolean flag4 = !Block.o[l.a(i, j, k - 1)];
            boolean flag5 = !Block.o[l.a(i, j, k + 1)];
            byte byte0 = -1;
            double d7 = 9999D;

            if (flag && d4 < d7) {
                d7 = d4;
                byte0 = 0;
            }
            if (flag1 && 1.0D - d4 < d7) {
                d7 = 1.0D - d4;
                byte0 = 1;
            }
            if (flag2 && d5 < d7) {
                d7 = d5;
                byte0 = 2;
            }
            if (flag3 && 1.0D - d5 < d7) {
                d7 = 1.0D - d5;
                byte0 = 3;
            }
            if (flag4 && d6 < d7) {
                d7 = d6;
                byte0 = 4;
            }
            if (flag5 && 1.0D - d6 < d7) {
                double d8 = 1.0D - d6;

                byte0 = 5;
            }
            float f1 = W.nextFloat() * 0.2F + 0.1F;

            if (byte0 == 0) {
                s = -f1;
            }
            if (byte0 == 1) {
                s = f1;
            }
            if (byte0 == 2) {
                t = -f1;
            }
            if (byte0 == 3) {
                t = f1;
            }
            if (byte0 == 4) {
                u = -f1;
            }
            if (byte0 == 5) {
                u = f1;
            }
        }
        return false;
    }

    protected void b(int i) {
        a(((Entity) (null)), i);
    }

    public boolean a(Entity entity, int i) {
        y();
        f -= i;
        if (f <= 0) {
            q();
        }
        return false;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) f);
        nbttagcompound.a("Age", (short) b);
        nbttagcompound.a("Item", a.a(new NBTTagCompound()));
    }

    public void b(NBTTagCompound nbttagcompound) {
        f = nbttagcompound.c("Health") & 0xff;
        b = ((int) (nbttagcompound.c("Age")));
        NBTTagCompound nbttagcompound1 = nbttagcompound.j("Item");

        a = new ItemStack(nbttagcompound1);
    }

    public void b(EntityPlayer entityplayer) {
        if (l.z) {
            return;
        }
        int i = a.a;

        if (c == 0 && entityplayer.an.a(a)) {
            l.a(((Entity) (this)), "random.pop", 0.2F, ((W.nextFloat() - W.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.c(((Entity) (this)), i);
            q();
        }
    }
}
