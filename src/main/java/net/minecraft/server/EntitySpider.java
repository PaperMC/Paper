package net.minecraft.server;

import java.util.Random;

//CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSpider;
//CraftBukkit stop

public class EntitySpider extends EntityMobs {

    public EntitySpider(World world) {
        super(world);
        aP = "/mob/spider.png";
        a(1.4F, 0.9F);
        bC = 0.8F;
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftSpider(server, this);
        //CraftBukkit end
    }

    public double k() {
        return (double) J * 0.75D - 0.5D;
    }

    protected Entity l() {
        float f1 = b(1.0F);

        if (f1 < 0.5F) {
            double d = 16D;

            return ((Entity) (l.a(((Entity) (this)), d)));
        } else {
            return null;
        }
    }

    protected String e() {
        return "mob.spider";
    }

    protected String f() {
        return "mob.spider";
    }

    protected String g() {
        return "mob.spiderdeath";
    }

    protected void a(Entity entity, float f1) {
        float f2 = b(1.0F);

        if (f2 > 0.5F && W.nextInt(100) == 0) {
            this.d = null;
            return;
        }
        if (f1 > 2.0F && f1 < 6F && W.nextInt(10) == 0) {
            if (A) {
                double d = entity.p - p;
                double d1 = entity.r - r;
                float f3 = MathHelper.a(d * d + d1 * d1);

                s = (d / (double) f3) * 0.5D * 0.80000001192092896D + s * 0.20000000298023224D;
                u = (d1 / (double) f3) * 0.5D * 0.80000001192092896D + u * 0.20000000298023224D;
                t = 0.40000000596046448D;
            }
        } else {
            super.a(entity, f1);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected int h() {
        return Item.I.ba;
    }

    public boolean m() {
        return B;
    }
}
