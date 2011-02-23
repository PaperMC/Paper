package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;
// CraftBukkit end

public class EntityZombie extends EntityMonster {

    public EntityZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.az = 0.5F;
        this.c = 5;
    }

    public void q() {
        if (this.world.c()) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.world.i(MathHelper.b(this.locX), MathHelper.b(this.locY), MathHelper.b(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) this.world).getServer();
                EntityCombustEvent event = new EntityCombustEvent(Type.ENTITY_COMBUST, this.getBukkitEntity());
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.fireTicks = 300;
                }
                // CraftBukkit end
            }
        }

        super.q();
    }

    protected String e() {
        return "mob.zombie";
    }

    protected String f() {
        return "mob.zombiehurt";
    }

    protected String g() {
        return "mob.zombiedeath";
    }

    protected int h() {
        return Item.FEATHER.id;
    }
}
