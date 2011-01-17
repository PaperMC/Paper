package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSheep;

public class EntitySheep extends EntityAnimals {

    public static final float a[][] = {
        {
            1.0F, 1.0F, 1.0F
        }, {
            0.95F, 0.7F, 0.2F
        }, {
            0.9F, 0.5F, 0.85F
        }, {
            0.6F, 0.7F, 0.95F
        }, {
            0.9F, 0.9F, 0.2F
        }, {
            0.5F, 0.8F, 0.1F
        }, {
            0.95F, 0.7F, 0.8F
        }, {
            0.3F, 0.3F, 0.3F
        }, {
            0.6F, 0.6F, 0.6F
        }, {
            0.3F, 0.6F, 0.7F
        }, {
            0.7F, 0.4F, 0.9F
        }, {
            0.2F, 0.4F, 0.8F
        }, {
            0.5F, 0.4F, 0.3F
        }, {
            0.4F, 0.5F, 0.2F
        }, {
            0.8F, 0.3F, 0.3F
        }, {
            0.1F, 0.1F, 0.1F
        }
    };

    public EntitySheep(World world) {
        super(world);
        aP = "/mob/sheep.png";
        a(0.9F, 1.3F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftSheep(server, this);
        //CraftBukkit end
    }

    protected void a() {
        super.a();
        af.a(16, ((new Byte((byte) 0))));
    }

    public boolean a(Entity entity, int i) {
        if (!l.z && !f_() && (entity instanceof EntityLiving)) {
            a(true);
            int j = 1 + W.nextInt(3);

            for (int k = 0; k < j; k++) {
                EntityItem entityitem = a(new ItemStack(Block.ab.bi, 1, e_()), 1.0F);

                entityitem.t += W.nextFloat() * 0.05F;
                entityitem.s += (W.nextFloat() - W.nextFloat()) * 0.1F;
                entityitem.u += (W.nextFloat() - W.nextFloat()) * 0.1F;
            }
        }
        return super.a(entity, i);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Sheared", f_());
        nbttagcompound.a("Color", (byte) e_());
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        a(nbttagcompound.l("Sheared"));
        a(((int) (nbttagcompound.b("Color"))));
    }

    protected String e() {
        return "mob.sheep";
    }

    protected String f() {
        return "mob.sheep";
    }

    protected String g() {
        return "mob.sheep";
    }

    public int e_() {
        return af.a(16) & 0xf;
    }

    public void a(int i) {
        byte byte0 = af.a(16);

        af.b(16, ((Byte.valueOf((byte) (byte0 & 0xf0 | i & 0xf)))));
    }

    public boolean f_() {
        return (af.a(16) & 0x10) != 0;
    }

    public void a(boolean flag) {
        byte byte0 = af.a(16);

        if (flag) {
            af.b(16, ((Byte.valueOf((byte) (byte0 | 0x10)))));
        } else {
            af.b(16, ((Byte.valueOf((byte) (byte0 & 0xffffffef)))));
        }
    }

    public static int a(Random random) {
        int i = random.nextInt(100);

        if (i < 5) {
            return 15;
        }
        if (i < 10) {
            return 7;
        }
        return i >= 15 ? 0 : 8;
    }
}
