package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChicken;
// CraftBukkit end

public class EntityChicken extends EntityAnimals {

    public boolean a;
    public float b;
    public float c;
    public float f;
    public float ak;
    public float al;
    public int am;

    public EntityChicken(World world) {
        super(world);
        a = false;
        b = 0.0F;
        c = 0.0F;
        al = 1.0F;
        aP = "/mob/chicken.png";
        a(0.3F, 0.4F);
        aZ = 4;
        am = W.nextInt(6000) + 6000;
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftChicken(server, this);
        // CraftBukkit end
    }

    public void o() {
        super.o();
        ak = b;
        f = c;
        c += ((float) ((double) (A ? -1 : 4) * 0.29999999999999999D));
        if (c < 0.0F) {
            c = 0.0F;
        }
        if (c > 1.0F) {
            c = 1.0F;
        }
        if (!A && al < 1.0F) {
            al = 1.0F;
        }
        al *= 0.90000000000000002D;
        if (!A && t < 0.0D) {
            t *= 0.59999999999999998D;
        }
        b += al * 2.0F;
        if (!l.z && --am <= 0) {
            l.a(((Entity) (this)), "mob.chickenplop", 1.0F, (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
            a(Item.aN.ba, 1);
            am = W.nextInt(6000) + 6000;
        }
    }

    protected void a(float f1) {}

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected String e() {
        return "mob.chicken";
    }

    protected String f() {
        return "mob.chickenhurt";
    }

    protected String g() {
        return "mob.chickenhurt";
    }

    protected int h() {
        return Item.J.ba;
    }
}
