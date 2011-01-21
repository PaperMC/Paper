package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPigZombie;
// CraftBukkit end

public class EntityPigZombie extends EntityZombie {

    private int a;
    private int b;
    private static final ItemStack f;

    public EntityPigZombie(World world) {
        super(world);
        a = 0;
        b = 0;
        aP = "/mob/pigzombie.png";
        bC = 0.5F;
        c = 5;
        ae = true;
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftPigZombie(server, this);
        // CraftBukkit end
    }

    public void b_() {
        bC = d == null ? 0.5F : 0.95F;
        if (b > 0 && --b == 0) {
            l.a(((Entity) (this)), "mob.zombiepig.zpigangry", i() * 2.0F, ((W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
        super.b_();
    }

    public boolean b() {
        return l.k > 0 && l.a(z) && l.a(((Entity) (this)), z).size() == 0 && !l.b(z);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Anger", (short) a);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        a = ((int) (nbttagcompound.c("Anger")));
    }

    protected Entity l() {
        if (a == 0) {
            return null;
        } else {
            return super.l();
        }
    }

    public void o() {
        super.o();
    }

    public boolean a(Entity entity, int i) {
        if (entity instanceof EntityPlayer) {
            List list = l.b(((Entity) (this)), z.b(32D, 32D, 32D));

            for (int j = 0; j < list.size(); j++) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1 instanceof EntityPigZombie) {
                    EntityPigZombie entitypigzombie = (EntityPigZombie) entity1;

                    entitypigzombie.g(entity);
                }
            }

            g(entity);
        }
        return super.a(entity, i);
    }

    private void g(Entity entity) {
        d = entity;
        a = 400 + W.nextInt(400);
        b = W.nextInt(40);
    }

    protected String e() {
        return "mob.zombiepig.zpig";
    }

    protected String f() {
        return "mob.zombiepig.zpighurt";
    }

    protected String g() {
        return "mob.zombiepig.zpigdeath";
    }

    protected int h() {
        return Item.ap.ba;
    }

    static {
        f = new ItemStack(Item.E, 1);
    }
}
