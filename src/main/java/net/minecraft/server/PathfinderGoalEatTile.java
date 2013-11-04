package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.Material;
// CraftBukkit end

public class PathfinderGoalEatTile extends PathfinderGoal {

    private EntityInsentient b;
    private World c;
    int a;

    public PathfinderGoalEatTile(EntityInsentient entityinsentient) {
        this.b = entityinsentient;
        this.c = entityinsentient.world;
        this.a(7);
    }

    public boolean a() {
        if (this.b.aI().nextInt(this.b.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            int i = MathHelper.floor(this.b.locX);
            int j = MathHelper.floor(this.b.locY);
            int k = MathHelper.floor(this.b.locZ);

            return this.c.getType(i, j, k) == Blocks.LONG_GRASS && this.c.getData(i, j, k) == 1 ? true : this.c.getType(i, j - 1, k) == Blocks.GRASS;
        }
    }

    public void c() {
        this.a = 40;
        this.c.broadcastEntityEffect(this.b, (byte) 10);
        this.b.getNavigation().h();
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

            if (this.c.getType(i, j, k) == Blocks.LONG_GRASS) {
                // CraftBukkit
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b, this.b.world.getWorld().getBlockAt(i, j, k), Material.AIR, !this.c.getGameRules().getBoolean("mobGriefing")).isCancelled()) {
                    this.c.setAir(i, j, k, false);
                }

                this.b.p();
            } else if (this.c.getType(i, j - 1, k) == Blocks.GRASS) {
                // CraftBukkit
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b, this.b.world.getWorld().getBlockAt(i, j - 1, k), Material.DIRT, !this.c.getGameRules().getBoolean("mobGriefing")).isCancelled()) {
                    this.c.triggerEffect(2001, i, j - 1, k, Block.b((Block) Blocks.GRASS));
                    this.c.setTypeAndData(i, j - 1, k, Blocks.DIRT, 0, 2);
                }

                this.b.p();
            }
        }
    }
}
