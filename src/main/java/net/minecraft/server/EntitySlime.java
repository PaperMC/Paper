package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSkeleton;
import org.bukkit.craftbukkit.entity.CraftSlime;

public class EntitySlime extends EntityLiving implements IMobs {

    public float a;
    public float b;
    private int d;
    public int c;

    public EntitySlime(World world) {
        super(world);
        d = 0;
        c = 1;
        aP = "/mob/slime.png";
        c = 1 << W.nextInt(3);
        H = 0.0F;
        d = W.nextInt(20) + 10;
        a(c);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftSlime(server, this);
        //CraftBukkit end
    }

    public void a(int j) {
        c = j;
        a(0.6F * (float) j, 0.6F * (float) j);
        aZ = j * j;
        a(p, q, r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Size", c - 1);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        c = nbttagcompound.d("Size") + 1;
    }

    public void b_() {
        b = a;
        boolean flag = A;

        super.b_();
        if (A && !flag) {
            for (int j = 0; j < c * 8; j++) {
                float f1 = W.nextFloat() * 3.141593F * 2.0F;
                float f2 = W.nextFloat() * 0.5F + 0.5F;
                float f3 = MathHelper.a(f1) * (float) c * 0.5F * f2;
                float f4 = MathHelper.b(f1) * (float) c * 0.5F * f2;

                l.a("slime", p + (double) f3, z.b, r + (double) f4, 0.0D, 0.0D, 0.0D);
            }

            if (c > 2) {
                l.a(((Entity) (this)), "mob.slime", i(), ((W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }
            a = -0.5F;
        }
        a = a * 0.6F;
    }

    protected void d() {
        EntityPlayer entityplayer = l.a(((Entity) (this)), 16D);

        if (entityplayer != null) {
            b(((Entity) (entityplayer)), 10F);
        }
        if (A && d-- <= 0) {
            d = W.nextInt(20) + 10;
            if (entityplayer != null) {
                d /= 3;
            }
            bA = true;
            if (c > 1) {
                l.a(((Entity) (this)), "mob.slime", i(), ((W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }
            a = 1.0F;
            bx = 1.0F - W.nextFloat() * 2.0F;
            by = 1 * c;
        } else {
            bA = false;
            if (A) {
                bx = by = 0.0F;
            }
        }
    }

    public void q() {
        if (c > 1 && aZ == 0) {
            for (int j = 0; j < 4; j++) {
                float f1 = (((float) (j % 2) - 0.5F) * (float) c) / 4F;
                float f2 = (((float) (j / 2) - 0.5F) * (float) c) / 4F;
                EntitySlime entityslime = new EntitySlime(l);

                entityslime.a(c / 2);
                entityslime.c(p + (double) f1, q + 0.5D, r + (double) f2, W.nextFloat() * 360F, 0.0F);
                l.a(((Entity) (entityslime)));
            }
        }
        super.q();
    }

    public void b(EntityPlayer entityplayer) {
        if (c > 1 && i(((Entity) (entityplayer))) && (double) a(((Entity) (entityplayer))) < 0.59999999999999998D * (double) c && entityplayer.a(((Entity) (this)), c)) {
            l.a(((Entity) (this)), "mob.slimeattack", 1.0F, (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String f() {
        return "mob.slime";
    }

    protected String g() {
        return "mob.slime";
    }

    protected int h() {
        if (c == 1) {
            return Item.aK.ba;
        } else {
            return 0;
        }
    }

    public boolean b() {
        Chunk chunk = l.b(MathHelper.b(p), MathHelper.b(r));

        return (c == 1 || l.k > 0) && W.nextInt(10) == 0 && chunk.a(0x3ad8025fL).nextInt(10) == 0 && q < 16D;
    }

    protected float i() {
        return 0.6F;
    }
}
