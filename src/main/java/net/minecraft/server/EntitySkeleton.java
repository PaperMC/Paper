package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;
// CraftBukkit end

public class EntitySkeleton extends EntityMobs {

    private static final ItemStack a;

    public EntitySkeleton(World world) {
        super(world);
        aP = "/mob/skeleton.png";
    }

    protected String e() {
        return "mob.skeleton";
    }

    protected String f() {
        return "mob.skeletonhurt";
    }

    protected String g() {
        return "mob.skeletonhurt";
    }

    public void o() {
        if (l.b()) {
            float f1 = b(1.0F);

            if (f1 > 0.5F && l.i(MathHelper.b(p), MathHelper.b(q), MathHelper.b(r)) && W.nextFloat() * 30F < (f1 - 0.4F) * 2.0F) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) l).getServer();
                EntityCombustEvent event = new EntityCombustEvent(Type.ENTITY_COMBUST, new CraftLivingEntity(server, (EntityLiving) this));
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Z = 300;
                }
                // CraftBukkit end
            }
        }
        super.o();
    }

    protected void a(Entity entity, float f1) {
        if (f1 < 10F) {
            double d = entity.p - p;
            double d1 = entity.r - r;

            if (bf == 0) {
                EntityArrow entityarrow = new EntityArrow(l, ((EntityLiving) (this)));

                entityarrow.q += 1.3999999761581421D;
                double d2 = entity.q - 0.20000000298023224D - entityarrow.q;
                float f2 = MathHelper.a(d * d + d1 * d1) * 0.2F;

                l.a(((Entity) (this)), "random.bow", 1.0F, 1.0F / (W.nextFloat() * 0.4F + 0.8F));
                l.a(((Entity) (entityarrow)));
                entityarrow.a(d, d2 + (double) f2, d1, 0.6F, 12F);
                bf = 30;
            }
            v = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            e = true;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected int h() {
        return Item.j.ba;
    }

    protected void g_() {
        int i = W.nextInt(3);

        for (int j = 0; j < i; j++) {
            a(Item.j.ba, 1);
        }

        i = W.nextInt(3);
        for (int k = 0; k < i; k++) {
            a(Item.aV.ba, 1);
        }
    }

    static {
        a = new ItemStack(Item.i, 1);
    }
}
