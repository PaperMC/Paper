package net.minecraft.server;

import java.util.*;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPainting;
// CraftBukkit end

public class EntityPainting extends Entity {

    private int f;
    public int a;
    public int b;
    public int c;
    public int d;
    public EnumArt e;

    public EntityPainting(World world) {
        super(world);
        f = 0;
        a = 0;
        H = 0.0F;
        a(0.5F, 0.5F);
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftPainting(server, this);
        // CraftBukkit end
    }

    public EntityPainting(World world, int i, int j, int k, int l) {
        this(world);
        b = i;
        c = j;
        d = k;
        ArrayList arraylist = new ArrayList();
        EnumArt aenumart[] = EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; j1++) {
            EnumArt enumart = aenumart[j1];

            e = enumart;
            a(l);
            if (d()) {
                ((List) (arraylist)).add(((enumart)));
            }
        }

        if (((List) (arraylist)).size() > 0) {
            e = (EnumArt) ((List) (arraylist)).get(W.nextInt(((List) (arraylist)).size()));
        }
        a(l);
    }

    protected void a() {}

    public void a(int i) {
        a = i;
        x = v = i * 90;
        float f1 = e.A;
        float f2 = e.B;
        float f3 = e.A;

        if (i == 0 || i == 2) {
            f3 = 0.5F;
        } else {
            f1 = 0.5F;
        }
        f1 /= 32F;
        f2 /= 32F;
        f3 /= 32F;
        float f4 = (float) b + 0.5F;
        float f5 = (float) c + 0.5F;
        float f6 = (float) d + 0.5F;
        float f7 = 0.5625F;

        if (i == 0) {
            f6 -= f7;
        }
        if (i == 1) {
            f4 -= f7;
        }
        if (i == 2) {
            f6 += f7;
        }
        if (i == 3) {
            f4 += f7;
        }
        if (i == 0) {
            f4 -= d(e.A);
        }
        if (i == 1) {
            f6 += d(e.A);
        }
        if (i == 2) {
            f4 += d(e.A);
        }
        if (i == 3) {
            f6 -= d(e.A);
        }
        f5 += d(e.B);
        a((double)f4, (double)f5, (double)f6); // CraftBukkit -- forcecast all arguments to double
        float f8 = -0.00625F;

        z.c(f4 - f1 - f8, f5 - f2 - f8, f6 - f3 - f8, f4 + f1 + f8, f5 + f2 + f8, f6 + f3 + f8);
    }

    private float d(int i) {
        if (i == 32) {
            return 0.5F;
        }
        return i != 64 ? 0.0F : 0.5F;
    }

    public void b_() {
        if (f++ == 100 && !l.z) {
            f = 0;
            if (!d()) {
                q();
                l.a(((Entity) (new EntityItem(l, p, q, r, new ItemStack(Item.aq)))));
            }
        }
    }

    public boolean d() {
        if (this.l.a(((Entity) (this)), z).size() > 0) {
            return false;
        }
        int i = e.A / 16;
        int j = e.B / 16;
        int k = b;
        int l = c;
        int i1 = d;

        if (a == 0) {
            k = MathHelper.b(p - (double) ((float) e.A / 32F));
        }
        if (a == 1) {
            i1 = MathHelper.b(r - (double) ((float) e.A / 32F));
        }
        if (a == 2) {
            k = MathHelper.b(p - (double) ((float) e.A / 32F));
        }
        if (a == 3) {
            i1 = MathHelper.b(r - (double) ((float) e.A / 32F));
        }
        l = MathHelper.b(q - (double) ((float) e.B / 32F));
        for (int j1 = 0; j1 < i; j1++) {
            for (int k1 = 0; k1 < j; k1++) {
                Material material;

                if (a == 0 || a == 2) {
                    material = this.l.c(k + j1, l + k1, d);
                } else {
                    material = this.l.c(b, l + k1, i1 + j1);
                }
                if (!material.a()) {
                    return false;
                }
            }
        }

        List list = this.l.b(((Entity) (this)), z);

        for (int l1 = 0; l1 < list.size(); l1++) {
            if (list.get(l1) instanceof EntityPainting) {
                return false;
            }
        }

        return true;
    }

    public boolean c_() {
        return true;
    }

    public boolean a(Entity entity, int i) {
        if (!G && !l.z) {
            q();
            y();
            l.a(((Entity) (new EntityItem(l, p, q, r, new ItemStack(Item.aq)))));
        }
        return true;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Dir", (byte) a);
        nbttagcompound.a("Motive", e.z);
        nbttagcompound.a("TileX", b);
        nbttagcompound.a("TileY", c);
        nbttagcompound.a("TileZ", d);
    }

    public void b(NBTTagCompound nbttagcompound) {
        a = ((int) (nbttagcompound.b("Dir")));
        b = nbttagcompound.d("TileX");
        c = nbttagcompound.d("TileY");
        d = nbttagcompound.d("TileZ");
        String s = nbttagcompound.h("Motive");
        EnumArt aenumart[] = EnumArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; j++) {
            EnumArt enumart = aenumart[j];

            if (enumart.z.equals(((s)))) {
                e = enumart;
            }
        }

        if (e == null) {
            e = EnumArt.a;
        }
        a(a);
    }
}
