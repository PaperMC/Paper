package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityCombustEvent;
// CraftBukkit end

public class EntityZombie extends EntityMonster {

    public EntityZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.aE = 0.5F;
        this.damage = 5;
    }

    public void u() {
        if (this.world.d()) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) this.world).getServer();
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity());
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.fireTicks = 300;
                }
                // CraftBukkit end
            }
        }

        super.u();
    }

    protected String g() {
        return "mob.zombie";
    }

    protected String h() {
        return "mob.zombiehurt";
    }

    protected String i() {
        return "mob.zombiedeath";
    }

    protected int j() {
        return Item.FEATHER.id;
    }
}
