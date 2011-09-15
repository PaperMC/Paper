package net.minecraft.server;

import org.bukkit.event.entity.EntityCombustEvent; // CraftBukkit

public class EntityZombie extends EntityMonster {

    public EntityZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.aU = 0.5F;
        this.damage = 5;
    }

    public void s() {
        if (this.world.d() && !this.world.isStatic) {
            float f = this.a_(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.fireTicks = 300;
                }
                // CraftBukkit end
            }
        }

        super.s();
    }

    protected String h() {
        return "mob.zombie";
    }

    protected String i() {
        return "mob.zombiehurt";
    }

    protected String j() {
        return "mob.zombiedeath";
    }

    protected int k() {
        return Item.ROTTEN_FLESH.id;
    }
}
