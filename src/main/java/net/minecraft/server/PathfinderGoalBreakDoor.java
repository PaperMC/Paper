package net.minecraft.server;

import java.util.function.Predicate;

public class PathfinderGoalBreakDoor extends PathfinderGoalDoorInteract {

    private final Predicate<EnumDifficulty> g;
    protected int a;
    protected int b;
    protected int c;

    public PathfinderGoalBreakDoor(EntityInsentient entityinsentient, Predicate<EnumDifficulty> predicate) {
        super(entityinsentient);
        this.b = -1;
        this.c = -1;
        this.g = predicate;
    }

    public PathfinderGoalBreakDoor(EntityInsentient entityinsentient, int i, Predicate<EnumDifficulty> predicate) {
        this(entityinsentient, predicate);
        this.c = i;
    }

    protected int f() {
        return Math.max(240, this.c);
    }

    @Override
    public boolean a() {
        return !super.a() ? false : (!this.entity.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? false : this.a(this.entity.world.getDifficulty()) && !this.g());
    }

    @Override
    public void c() {
        super.c();
        this.a = 0;
    }

    @Override
    public boolean b() {
        return this.a <= this.f() && !this.g() && this.door.a((IPosition) this.entity.getPositionVector(), 2.0D) && this.a(this.entity.world.getDifficulty());
    }

    @Override
    public void d() {
        super.d();
        this.entity.world.a(this.entity.getId(), this.door, -1);
    }

    @Override
    public void e() {
        super.e();
        if (this.entity.getRandom().nextInt(20) == 0) {
            this.entity.world.triggerEffect(1019, this.door, 0);
            if (!this.entity.ai) {
                this.entity.swingHand(this.entity.getRaisedHand());
            }
        }

        ++this.a;
        int i = (int) ((float) this.a / (float) this.f() * 10.0F);

        if (i != this.b) {
            this.entity.world.a(this.entity.getId(), this.door, i);
            this.b = i;
        }

        if (this.a == this.f() && this.a(this.entity.world.getDifficulty())) {
            this.entity.world.a(this.door, false);
            this.entity.world.triggerEffect(1021, this.door, 0);
            this.entity.world.triggerEffect(2001, this.door, Block.getCombinedId(this.entity.world.getType(this.door)));
        }

    }

    private boolean a(EnumDifficulty enumdifficulty) {
        return this.g.test(enumdifficulty);
    }
}
