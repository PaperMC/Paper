package net.minecraft.server;

public class PathfinderGoalBreakDoor extends PathfinderGoalDoorInteract {

    private int i;

    public PathfinderGoalBreakDoor(EntityLiving entityliving) {
        super(entityliving);
    }

    public boolean a() {
        return !super.a() ? false : !this.e.d(this.a.world, this.b, this.c, this.d);
    }

    public void c() {
        super.c();
        this.i = 240;
    }

    public boolean b() {
        double d0 = this.a.e((double) this.b, (double) this.c, (double) this.d);

        return this.i >= 0 && !this.e.d(this.a.world, this.b, this.c, this.d) && d0 < 4.0D;
    }

    public void e() {
        super.e();
        if (this.a.am().nextInt(20) == 0) {
            this.a.world.triggerEffect(1010, this.b, this.c, this.d, 0);
        }

        if (--this.i == 0 && this.a.world.difficulty == 3) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityBreakDoorEvent(this.a, this.b, this.c, this.d).isCancelled()) {
                this.c();
                return;
            }
            // CraftBukkit end
            this.a.world.setTypeId(this.b, this.c, this.d, 0);
            this.a.world.triggerEffect(1012, this.b, this.c, this.d, 0);
            this.a.world.triggerEffect(2001, this.b, this.c, this.d, this.e.id);
        }
    }
}
