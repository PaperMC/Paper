package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSquid;

public class EntitySquid extends EntityWaterMob {

    public float a;
    public float b;
    public float c;
    public float f;
    public float ak;
    public float al;
    public float am;
    public float an;
    private float ao;
    private float ap;
    private float aq;
    private float ar;
    private float as;
    private float at;

    public EntitySquid(World world) {
        super(world);
        a = 0.0F;
        b = 0.0F;
        c = 0.0F;
        f = 0.0F;
        ak = 0.0F;
        al = 0.0F;
        am = 0.0F;
        an = 0.0F;
        ao = 0.0F;
        ap = 0.0F;
        aq = 0.0F;
        ar = 0.0F;
        as = 0.0F;
        at = 0.0F;
        aP = "/mob/squid.png";
        a(0.95F, 0.95F);
        ap = (1.0F / (W.nextFloat() + 1.0F)) * 0.2F;
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftSquid(server, this);
        //CraftBukkit end
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected String e() {
        return null;
    }

    protected String f() {
        return null;
    }

    protected String g() {
        return null;
    }

    protected float i() {
        return 0.4F;
    }

    protected int h() {
        return 0;
    }

    protected void g_() {
        int j = W.nextInt(3) + 1;

        for (int k = 0; k < j; k++) {
            a(new ItemStack(Item.aU, 1, 0), 0.0F);
        }
    }

    public boolean a(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.an.e();

        if (itemstack != null && itemstack.c == Item.au.ba) {
            entityplayer.an.a(entityplayer.an.c, new ItemStack(Item.aE));
            return true;
        } else {
            return false;
        }
    }

    public boolean v() {
        return l.a(z.b(0.0D, -0.60000002384185791D, 0.0D), Material.f, ((Entity) (this)));
    }

    public void o() {
        super.o();
        b = a;
        f = c;
        al = ak;
        an = am;
        ak += ap;
        if (ak > 6.283185F) {
            ak -= 6.283185F;
            if (W.nextInt(10) == 0) {
                ap = (1.0F / (W.nextFloat() + 1.0F)) * 0.2F;
            }
        }
        if (v()) {
            if (ak < 3.141593F) {
                float f1 = ak / 3.141593F;

                am = MathHelper.a(f1 * f1 * 3.141593F) * 3.141593F * 0.25F;
                if ((double) f1 > 0.75D) {
                    ao = 1.0F;
                    aq = 1.0F;
                } else {
                    aq = aq * 0.8F;
                }
            } else {
                am = 0.0F;
                ao = ao * 0.9F;
                aq = aq * 0.99F;
            }
            if (!aW) {
                s = ar * ao;
                t = as * ao;
                u = at * ao;
            }
            float f2 = MathHelper.a(s * s + u * u);

            aI += ((-(float) Math.atan2(s, u) * 180F) / 3.141593F - aI) * 0.1F;
            v = aI;
            c = c + 3.141593F * aq * 1.5F;
            a += ((-(float) Math.atan2(f2, t) * 180F) / 3.141593F - a) * 0.1F;
        } else {
            am = MathHelper.e(MathHelper.a(ak)) * 3.141593F * 0.25F;
            if (!aW) {
                s = 0.0D;
                t -= 0.080000000000000002D;
                t *= 0.98000001907348633D;
                u = 0.0D;
            }
            a += ((float) ((double) (-90F - a) * 0.02D));
        }
    }

    public void c(float f1, float f2) {
        c(s, t, u);
    }

    protected void d() {
        if (W.nextInt(50) == 0 || !ab || ar == 0.0F && as == 0.0F && at == 0.0F) {
            float f1 = W.nextFloat() * 3.141593F * 2.0F;

            ar = MathHelper.b(f1) * 0.2F;
            as = -0.1F + W.nextFloat() * 0.2F;
            at = MathHelper.a(f1) * 0.2F;
        }
    }
}
