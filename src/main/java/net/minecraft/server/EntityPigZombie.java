package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityPigZombie extends EntityZombie {

    private int a = 0;
    private int b = 0;
    private static final ItemStack f = new ItemStack(Item.GOLD_SWORD, 1);

    public EntityPigZombie(World world) {
        super(world);
        this.texture = "/mob/pigzombie.png";
        this.az = 0.5F;
        this.c = 5;
        this.by = true;
    }

    public void f_() {
        this.az = this.d != null ? 0.95F : 0.5F;
        if (this.b > 0 && --this.b == 0) {
            this.world.a(this, "mob.zombiepig.zpigangry", this.i() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        super.f_();
    }

    public boolean b() {
        return this.world.j > 0 && this.world.a(this.boundingBox) && this.world.a((Entity) this, this.boundingBox).size() == 0 && !this.world.b(this.boundingBox);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Anger", (short) this.a);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.a = nbttagcompound.d("Anger");
    }

    protected Entity m() {
        return this.a == 0 ? null : super.m();
    }

    public void r() {
        super.r();
    }

    public boolean a(Entity entity, int i) {
        if (entity instanceof EntityHuman) {
            List list = this.world.b((Entity) this, this.boundingBox.b(32.0D, 32.0D, 32.0D));

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1 instanceof EntityPigZombie) {
                    EntityPigZombie entitypigzombie = (EntityPigZombie) entity1;

                    entitypigzombie.d(entity);
                }
            }

            this.d(entity);
        }

        return super.a(entity, i);
    }

    private void d(Entity entity) {
        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        org.bukkit.entity.Entity bukkitTarget = null;
        if (entity != null) {
            bukkitTarget = entity.getBukkitEntity();
        }

        EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, TargetReason.PIG_ZOMBIE_TARGET);
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if (event.getTarget() == null) {
                this.d = null;
            } else {
                this.d = ((CraftEntity) event.getTarget()).getHandle();
                this.a = 400 + this.random.nextInt(400);
                this.b = this.random.nextInt(40);
            }
        }
        // CraftBukkit end
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
        return Item.GRILLED_PORK.id;
    }
}
