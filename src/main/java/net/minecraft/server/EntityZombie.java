package net.minecraft.server;

import org.bukkit.event.entity.EntityCombustEvent; // CraftBukkit

public class EntityZombie extends EntityMonster {

    public EntityZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.aY = 0.5F;
        this.damage = 4;
    }

    public int getMaxHealth() {
        return 20;
    }

    protected int O() {
        return 2;
    }

    public void d() {
        if (this.world.e() && !this.world.isStatic) {
            float f = this.a(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.setOnFire(event.getDuration());
                }
                // CraftBukkit end
            }
        }

        super.d();
    }

    protected String c_() {
        return "mob.zombie";
    }

    protected String m() {
        return "mob.zombiehurt";
    }

    protected String n() {
        return "mob.zombiedeath";
    }

    protected int e() {
        return Item.ROTTEN_FLESH.id;
    }

    public MonsterType getMonsterType() {
        return MonsterType.UNDEAD;
    }
}
