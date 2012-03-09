package net.minecraft.server;

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
        if (this.b.am().nextInt(this.b.isBaby() ? 50 : 1000) != 0) {
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
        this.b.ak().f();
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
                this.c.triggerEffect(2001, i, j, k, Block.LONG_GRASS.id + 4096);
                this.c.setTypeId(i, j, k, 0);
                this.b.z();
            } else if (this.c.getTypeId(i, j - 1, k) == Block.GRASS.id) {
                this.c.triggerEffect(2001, i, j - 1, k, Block.GRASS.id);
                this.c.setTypeId(i, j - 1, k, Block.DIRT.id);
                this.b.z();
            }
        }
    }
}
