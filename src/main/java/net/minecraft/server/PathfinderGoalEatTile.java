package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.Material;
// CraftBukkit end

public class PathfinderGoalEatTile extends PathfinderGoal {

    private EntityLiving b;
    private World c;
    int a = 0;

    public PathfinderGoalEatTile(EntityLiving entityliving) {
        this.b = entityliving;
        this.c = entityliving.world;
        this.a(7);
    }

    public boolean a() {
        if (this.b.aA().nextInt(this.b.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            int i = MathHelper.floor(this.b.locX);
            int j = MathHelper.floor(this.b.locY);
            int k = MathHelper.floor(this.b.locZ);

            return this.c.getTypeId(i, j, k) == Block.LONG_GRASS.id && this.c.getData(i, j, k) == 1 ? true : this.c.getTypeId(i, j - 1, k) == Block.GRASS.id;
        }
    }

    public void c() {
        this.a = 40;
        this.c.broadcastEntityEffect(this.b, (byte) 10);
        this.b.getNavigation().g();
    }

    public void d() {
        this.a = 0;
    }

    public boolean b() {
        return this.a > 0;
    }

    public int f() {
        return this.a;
    }

    public void e() {
        this.a = Math.max(0, this.a - 1);
        if (this.a == 4) {
            int i = MathHelper.floor(this.b.locX);
            int j = MathHelper.floor(this.b.locY);
            int k = MathHelper.floor(this.b.locZ);

            if (this.c.getTypeId(i, j, k) == Block.LONG_GRASS.id) {
                // CraftBukkit start
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b.getBukkitEntity(), this.b.world.getWorld().getBlockAt(i, j, k), Material.AIR).isCancelled()) {
                    this.c.triggerEffect(2001, i, j, k, Block.LONG_GRASS.id + 4096);
                    this.c.setTypeId(i, j, k, 0);
                    this.b.aG();
                }
                // CraftBukkit end
            } else if (this.c.getTypeId(i, j - 1, k) == Block.GRASS.id) {
                // CraftBukkit start
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b.getBukkitEntity(), this.b.world.getWorld().getBlockAt(i, j - 1, k), Material.DIRT).isCancelled()) {
                    this.c.triggerEffect(2001, i, j - 1, k, Block.GRASS.id);
                    this.c.setTypeId(i, j - 1, k, Block.DIRT.id);
                    this.b.aG();
                }
                // CraftBukkit end
            }
        }
    }
}
